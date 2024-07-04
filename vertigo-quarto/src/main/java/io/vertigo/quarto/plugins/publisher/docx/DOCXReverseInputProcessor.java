/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.quarto.plugins.publisher.docx;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.quarto.impl.publisher.merger.processor.MergerProcessor;
import io.vertigo.quarto.publisher.model.PublisherData;

/**
 * Mise en forme des tags et suppression des champs inutiles dans le document DOCX.
 *
 * @author adufranne
 */
final class DOCXReverseInputProcessor implements MergerProcessor {

	/**
	 * Tag w:instrText
	 */
	private static final String W_INSTR_TEXT = "w:instrText";

	/**
	 * Tag w:fldCharType.
	 */
	private static final String W_FLD_CHAR_TYPE = "w:fldCharType";

	/**
	 * Tag w:fldChar.
	 */
	private static final String W_FLD_CHAR = "w:fldChar";

	/**
	 * Pattern pour reconnaitre un champ ksp.
	 */
	private static final String KSP_WRAPPING_TAG = "\\s*<#(.*)#>\\s*";

	/** {@inheritDoc} */
	@Override
	public String execute(final String xmlInput, final PublisherData publisherData) {
		final Document xmlDoc = DOCXUtil.loadDOM(xmlInput);
		final XPath xpath = DOCXUtil.loadXPath();

		// nettoyage des tags inutiles de type bookmark.
		try {
			cleanTagsByXPATH(DOCXUtil.XPATH_CLEAN_BOOKMARKS, xmlDoc, xpath);

			// factorisation des tags multiples.
			factorMultipleTags(xmlDoc, xpath);

			// nettoyage des noeuds de type KSP.
			cleanNotWordBESTags(xmlDoc, xpath);

			// conversion des tags mal formatés en tags valides.
			convertWrongFormattedTags(xmlDoc, xpath);

			// rendu du xml final
			return DOCXUtil.renderXML(xmlDoc);
		} catch (final XPathExpressionException e) {
			throw WrappedException.wrap(e, "Erreur de format du Docx");
		}
	}

	/**
	 * Adaptation de la syntaxe utilisateur vers la syntaxe KSP.
	 * les <# #> sont rajoutés si ils sont manquants.
	 * NB: visibilité package nécessaire pour les tests!.
	 * @param xmlDoc Document source
	 * @param xpath Moteur Xpath
	 * @throws XPathExpressionException Erreur Xpath
	 */
	static void convertWrongFormattedTags(final Document xmlDoc, final XPath xpath) throws XPathExpressionException {
		final NodeList nodeList = (NodeList) xpath.evaluate(DOCXUtil.XPATH_TAG_NODES, xmlDoc, XPathConstants.NODESET);

		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node node = nodeList.item(i);
			node.getLastChild().setTextContent(convertWrongFormattedTagString(node.getLastChild().getTextContent()));
		}
	}

	private static String convertWrongFormattedTagString(final String tag) {
		final String tagTrimmed = tag.trim();
		final Pattern p = Pattern.compile(KSP_WRAPPING_TAG);
		final Matcher m = p.matcher(tag);
		String tagContent = tagTrimmed;
		if (m.matches()) {
			tagContent = m.group(1).trim();
		}
		// ne pas changer les champs personnalisés de word.
		if (DOCXUtil.isWordTag(tagContent)) {
			return tag;
		}
		return "<#" + tagContent + "#>";
	}

	/**
	 * Méthode de suppression de tags.
	 * Supprime l'ensemble des résultats de la requête xpath.
	 *
	 * @param xmlDoc Document source
	 * @param xpath Moteur Xpath
	 * @throws XPathExpressionException Erreur Xpath
	 */
	private static void cleanTagsByXPATH(final String xpathExpr, final Document xmlDoc, final XPath xpath) throws XPathExpressionException {
		final NodeList nodeList = (NodeList) xpath.evaluate(xpathExpr, xmlDoc, XPathConstants.NODESET);

		for (int i = 0; i < nodeList.getLength(); i++) {

			final Node node = nodeList.item(i);
			node.getParentNode().removeChild(node);
		}
	}

	/**
	 * Nettoyage des tags KSP.
	 * *IMPORTANT* : On suppose que les tags multiples sont factorisés.
	 * On passe sur tous les noeuds begin, en allant jusque au noeud end.
	 * Si le champ n'est pas un champ word, on supprime le begin, le end,
	 * le separate.
	 * NB: visibilité package nécessaire pour les tests!.
	 * @param xmlDoc Document source
	 * @param xpath Moteur Xpath
	 * @throws XPathExpressionException Erreur Xpath
	 */
	private static void cleanNotWordBESTags(final Document xmlDoc, final XPath xpath) throws XPathExpressionException {
		String controlContent; // valeur de controle pour vérifier que le champ n'est pas WORD.
		final List<Node> removeNodes = new ArrayList<>(); // liste des noeuds à supprimer
		Node currentNode; // noeud en cours de traitement.
		Node controlNode;
		Node node;
		boolean afterSeparate;
		final NodeList nodeList = (NodeList) xpath.evaluate(DOCXUtil.XPATH_BEGIN, xmlDoc, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			afterSeparate = false;
			node = nodeList.item(i);
			controlNode = node.getNextSibling().getLastChild();
			controlContent = controlNode.getTextContent();
			if (DOCXUtil.isWordTag(controlContent)) { // tag word => on ne supprime rien
				continue;
			}
			removeNodes.add(node);
			currentNode = node.getNextSibling();

			while (true) {
				if (isDOCXNode(currentNode, DOCXUtil.DOCXNode.END)) {
					removeNodes.add(currentNode);
					break;
				}
				if (!afterSeparate && isDOCXNode(currentNode, DOCXUtil.DOCXNode.SEPARATE)) {
					removeNodes.add(currentNode);
					afterSeparate = true;
					currentNode = currentNode.getNextSibling();
					continue;
				}

				if (afterSeparate) {
					removeNodes.add(currentNode);
				}
				currentNode = currentNode.getNextSibling();
			}

		}

		removeNodes(removeNodes);

	}

	private static void removeNodes(final List<Node> removeNodes) {
		for (final Node removeNode : removeNodes) {
			removeNode.getParentNode().removeChild(removeNode);
		}
	}

	private static boolean isDOCXNode(final Node node, final DOCXUtil.DOCXNode nodeType) {
		Assertion.check()
				.isNotNull(node)
				.isNotNull(nodeType);
		//-----
		if (!node.hasChildNodes()) {
			return false;
		}
		if (!W_FLD_CHAR.equals(node.getLastChild().getNodeName())) {
			return false;
		}
		if (!node.getLastChild().hasAttributes()) {
			return false;
		}
		final Node namedNode = node.getLastChild().getAttributes().getNamedItem(W_FLD_CHAR_TYPE);
		return namedNode != null && nodeType.getNs().equals(namedNode.getTextContent());
	}

	/**
	 * Méthode de factorisation des tags "multiples" présents dans le docx.
	 * @param xmlDoc Document source
	 * @param xpath Moteur Xpath
	 * @throws XPathExpressionException Erreur Xpath
	 */
	private static void factorMultipleTags(final Document xmlDoc, final XPath xpath) throws XPathExpressionException {
		final NodeList nodeList = (NodeList) xpath.evaluate(DOCXUtil.XPATH_BEGIN, xmlDoc, XPathConstants.NODESET);
		Node startNode;
		Node factorNode;
		StringBuilder builder;
		List<Node> removeNodes;
		for (int i = 0; i < nodeList.getLength(); i++) {
			startNode = nodeList.item(i).getNextSibling();
			factorNode = nodeList.item(i).getNextSibling();
			Node firstValidNode = null;

			builder = new StringBuilder();
			removeNodes = new ArrayList<>();
			if (!factorNode.hasChildNodes()) {
				removeNodes.add(factorNode);
			} else {
				firstValidNode = factorNode;
				builder.append(startNode.getLastChild().getTextContent());
			}

			boolean factor = true;
			while (factor) {
				factorNode = factorNode.getNextSibling();
				if (!factorNode.hasChildNodes()) { // noeud inconnu
					removeNodes.add(factorNode);
					continue;
				}
				if (W_INSTR_TEXT.equals(factorNode.getLastChild().getNodeName())) {
					if (firstValidNode == null) {
						firstValidNode = factorNode;
					} else {
						removeNodes.add(factorNode);
					}
					builder.append(factorNode.getLastChild().getTextContent());
				} else {
					factor = false;
				}
			}
			if (firstValidNode == null) { // aucune factorisation n'a pu être faite.
				continue;
			}
			firstValidNode.getLastChild().setTextContent(builder.toString().trim());
			// nettoyage des noeuds.
			for (final Node removeNode : removeNodes) {
				removeNode.getParentNode().removeChild(removeNode);
			}
		}
	}
}
