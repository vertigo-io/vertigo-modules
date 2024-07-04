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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;
import java.util.zip.ZipFile;

import javax.inject.Inject;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.vertigo.core.node.AutoCloseableNode;
import io.vertigo.core.node.component.di.DIInjector;
import io.vertigo.core.node.config.BootConfig;
import io.vertigo.core.node.config.NodeConfig;
import io.vertigo.core.plugins.resource.classpath.ClassPathResourceResolverPlugin;
import io.vertigo.core.resource.ResourceManager;

/**
 * Tests sur l'implémentation du plugin DOCX.
 *
 * @author adufranne
 */
public final class DOCXProcessorTest {

	/**
	 * Fichier de test.
	 */
	static final String TEST_FILE = "io/vertigo/quarto/plugins/publisher/docx/data/test.docx";

	/**
	 * Footer d'un docx.
	 */
	private static final String DOCX_FOOTER = "</w:body></w:document>";

	/**
	 * Header d'un docx.
	 */
	private static final String DOCX_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 wp14\"><w:body>";

	/**
	 * Ligne.
	 */
	private static final String ROW_TOKEN = "<w:r><w:instrText>{0}</w:instrText></w:r>";
	/**
	 * Colonne.
	 */
	private static final String COL_TOKEN = "<w:p>{0}</w:p>";

	@Inject
	private ResourceManager resourceManager;

	// //////////////////////////////////////////
	//
	// test des fonctions concernant les editions
	//
	// //////////////////////////////////////////////

	private AutoCloseableNode node;

	@BeforeEach
	public final void setUp() {
		node = new AutoCloseableNode(buildNodeConfig());
		DIInjector.injectMembers(this, node.getComponentSpace());
	}

	@AfterEach
	public final void tearDown() {
		if (node != null) {
			node.close();
		}
	}

	private NodeConfig buildNodeConfig() {
		return NodeConfig.builder()
				.withBoot(BootConfig.builder()
						.addPlugin(ClassPathResourceResolverPlugin.class)
						.build())
				.build();
	}

	/**
	 * Extraction puis rechargement d'un DOCX.
	 * Permet de tester l'accès aux fichiers dans le docx.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public void testExtractionReecritureDOCX() throws URISyntaxException, IOException {

		final ZipFile docxFile;
		final Map<String, String> files;
		try {
			final URL modelFileURL = resourceManager.resolve(TEST_FILE);
			docxFile = new ZipFile(new File(modelFileURL.toURI()));
			files = DOCXUtil.extractDOCXContents(docxFile); // méthode testée.
		} catch (final IOException e) {
			Assertions.fail("impossible de lire le modèle " + TEST_FILE);
			throw e;
		}

		try {
			DOCXUtil.createDOCX(docxFile, files);
		} catch (final IOException e) {
			Assertions.fail("impossible de réécrire le fichier " + TEST_FILE);
		}

	}

	/**
	 * Test de factoristion des tags multiples .
	 *
	 * @throws XPathExpressionException xpath mal formée.
	 */
	@Test
	public void testFactorMultipleTags() throws XPathExpressionException {
		final String[] simpleTags = { "=", "TEST", ".", "TEST" };
		final String[] results = { "<#=TEST.TEST#>", "=", "TEST", ".", "TEST" };
		final String content = buildBESTag(simpleTags, simpleTags);
		final Document xmlDoc = DOCXUtil.loadDOM(DOCXTest.factorMultipleTags(getDOCX(content)));
		final NodeList nodeList = (NodeList) DOCXUtil.loadXPath().evaluate("//w:r[w:instrText]", xmlDoc, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Assertions.assertEquals(results[i], nodeList.item(i).getLastChild().getTextContent());
		}
	}

	/**
	 * Test de nettoyage des tags .
	 * Doit laisser les tags words inchangés et supprimer les separate des tags .
	 *
	 * @throws XPathExpressionException xpath mal formée.
	 */
	@Test
	public void testCleanNotBESTags() throws XPathExpressionException {

		final String[] results = { "PAGE", "7", "<#=TEST.TEST#>" };
		String content = buildBESTag("PAGE", "7");
		content += buildBESTag("=TEST.TEST", "Erreur");
		final Document xmlDoc = DOCXUtil.loadDOM(DOCXTest.cleanNotBESTags(getDOCX(content)));
		final NodeList nodeList = (NodeList) DOCXUtil.loadXPath().evaluate("//w:r[w:instrText]", xmlDoc, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Assertions.assertEquals(results[i], nodeList.item(i).getLastChild().getTextContent());
		}
	}

	/**
	 * Test du namespace des documents.
	 */
	@Test
	public void testNamespaceContext() {
		final DOCXNamespaceContext namespace = new DOCXNamespaceContext();
		final String[] prefixToText = { "wpc", "mc", "o", "r", "m", "v", "wp14", "wp", "w10", "w", "w14", "wpg", "wpi", "wne", "wps" };

		for (final String prefix : prefixToText) {
			Assertions.assertNotNull("Unknowned prefix:" + prefix, namespace.getNamespaceURI(prefix));
			Assertions.assertEquals(prefix, namespace.getPrefix(namespace.getNamespaceURI(prefix)), "Invalid URI for prefix:" + prefix);
		}
	}

	/**
	 * Test des passages à la ligne.
	 *
	 * @throws XPathExpressionException si le xpath est mal formé.
	 */
	@Test
	public void testHandleCarriageReturn() throws XPathExpressionException {
		final String[] simpleTags = { "two\ntwo", "three\nthree\nthree", "one" };
		final String[] expected = { "in", "two", "two", "out", "in", "three", "three", "three", "out", "in", "one", "out" };
		final StringBuilder sb = new StringBuilder();
		for (final String tag : simpleTags) {
			sb.append(wrapInColToken(wrapInRowToken("in") + wrapInRowToken(tag) + wrapInRowToken("out")));
		}
		final String content = getDOCX(sb.toString());
		final String result = DOCXTest.handleCarriageReturn(content);
		final XPath xpath = DOCXUtil.loadXPath();

		checkCarriageReturn(DOCXUtil.loadDOM(result), xpath, expected);

	}

	private static void checkCarriageReturn(final Document xmlDoc, final XPath xpath, final String[] expected) throws XPathExpressionException {
		final String findNode = "//w:r";
		final NodeList nodeList = (NodeList) xpath.evaluate(findNode, xmlDoc, XPathConstants.NODESET);
		String content;
		for (int i = 0; i < nodeList.getLength(); i++) {
			content = nodeList.item(i).getLastChild().getTextContent();
			Assertions.assertEquals(expected[i], content, "Checking node " + i);
		}
	}

	/**
	 * Test des reformatages de balises.
	 *
	 * @throws XPathExpressionException erreur xpath.
	 */
	@Test
	public void testReformatString() throws XPathExpressionException {
		final String[] simpleTags = { "&lt;#=test#&gt;", "test", "=test", "if TEST=\"test\"" };
		final String[] results = { "<#=test#>", "test", "<#=test#>", "<#if TEST=\"test\"#>" };

		final StringBuilder sb = new StringBuilder();
		for (final String tag : simpleTags) {
			sb.append(wrapInRowToken(tag));
		}
		final XPath xpath = DOCXUtil.loadXPath();

		final Document xmlDoc = DOCXUtil.loadDOM(DOCXTest.convertWrongFormattedTags(getDOCX(sb.toString())));
		final NodeList nodeList = (NodeList) xpath.evaluate(DOCXUtil.XPATH_TAG_NODES, xmlDoc, XPathConstants.NODESET);
		Node domNode;
		String content;
		// on vérifie le contenu de chaque noeud.
		for (int i = 0; i < nodeList.getLength(); i++) {
			domNode = nodeList.item(i);
			content = domNode.getLastChild().getTextContent();
			Assertions.assertEquals(results[i], content);
		}
		// vérifier l'intégrité du docx.
		DOCXUtil.renderXML(xmlDoc);
	}

	private static String buildBESTag(final String beginContent, final String separateContent) {
		final String[] bContent = new String[1];
		bContent[0] = beginContent;
		final String[] sContent = new String[1];
		sContent[0] = separateContent;
		return buildBESTag(bContent, sContent);

	}

	private static String buildBESTag(final String[] beginContent, final String[] separateContent) {
		final StringBuilder sb = new StringBuilder();
		appendBESTag("begin", sb);
		for (final String be : beginContent) {
			sb.append(wrapInRowToken(be));
		}
		appendBESTag("separate", sb);
		for (final String se : separateContent) {
			sb.append(wrapInRowToken(se));
		}
		appendBESTag("end", sb);
		return sb.toString();

	}

	/**
	 * Construit un DOCX depuis son contenu (ajoute le header et le footer).
	 *
	 * @param content le contenu.
	 * @return le docx.
	 */
	private static String getDOCX(final String content) {
		return new StringBuilder()
				.append(getDOCXHeader())
				.append(content)
				.append(getDOCXFooter())
				.toString();
	}

	private static void appendBESTag(final String tagType, final StringBuilder sb) {
		sb
				.append("<w:r>")
				.append("<w:fldChar w:fldCharType=\"")
				.append(tagType)
				.append("\"/>")
				.append("</w:r>");
	}

	// METHODES DE CONSTRUCTION DOCX.
	private static String wrapInColToken(final String content) {
		return MessageFormat.format(COL_TOKEN, content);
	}

	private static String wrapInRowToken(final String content) {
		return MessageFormat.format(ROW_TOKEN, content);
	}

	private static String getDOCXHeader() {
		return DOCX_HEADER;
	}

	private static String getDOCXFooter() {
		return DOCX_FOOTER;

	}
}
