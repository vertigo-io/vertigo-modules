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
package io.vertigo.quarto.impl.publisher.merger.processor;

import java.util.Queue;

import io.vertigo.core.lang.Assertion;
import io.vertigo.quarto.publisher.model.PublisherData;

/**
 * Replace les tags <# #> pour les inscrire correctement dans le xml
 *
 * On cherche a les replacer de faéon é ce que la position du tag de début et celui de fin
 * 	(ex: <#if ... #> <#endif#>) soit au méme niveau de l'arbre XML,
 * ainsi qu'on les suppriment ou qu'on les multiplient (cas de <#loop#>)
 * le XML produit reste correct.
 * L'ODTCleaner devient alors inutil, en tout cas il n'a plus besoin de tenter
 * de rectifier (avec plus ou moins de sccés) un XML corrompu.
 *
 * @author npiedeloup
 */
public final class GrammarXMLBalancerProcessor implements MergerProcessor {

	/** Configure le mode de ce XML Balancer :
	 * 	soit on déplace au plus pret, soit on étend le XML pour inclure les balises
	 *  Dans le premier on peut retirer une balise pour equilibrer le XML, alors que sinon on les garde forcément
	 */
	private static final boolean MODE_CLOSER_CLOSED_XML = true;

	private static final String BEGIN_XML_CODE = "&lt;#";
	private static final String BEGIN_END_XML_CODE = "&lt;#end";
	private static final String END_XML_CODE = "#&gt;";

	/**
	 * Trouve une balise la balise de grammaire fermante, en tenant compte de la
	 * présence éventuelle de sous-balises
	 *
	 * @param grammarTag tag de grammaire (ex : "loop")
	 * @param fromIndex index é partir duquel il faut chercher
	 * @return la position de la balise de grammaire fermante, -1 si pas trouvé.
	 */
	private static int findEndGrammarIndex(final StringBuilder output, final String grammarTag, final int fromIndex) {
		int openCount = 1;

		int currentIndex = fromIndex;
		int nextBeginGramarIndex = output.indexOf(BEGIN_XML_CODE + grammarTag, currentIndex);
		int nextEndGramarIndex = output.indexOf(BEGIN_END_XML_CODE + grammarTag, currentIndex);

		while (true) {
			if (nextEndGramarIndex == -1) {
				// Pas de balise fermante trouvé
				return -1;
			} else if (nextBeginGramarIndex > -1 && nextBeginGramarIndex < nextEndGramarIndex) {
				// La prochaine balise de grammaire est ouvrante
				// => on continue après cette balise
				openCount++;
				currentIndex = nextBeginGramarIndex + 1;
			} else if (openCount > 1) {
				// La prochaine balise de grammaire est fermante, mais elle
				// ferme une sous-balise
				// => on continue après cette balise
				openCount--;
				currentIndex = nextEndGramarIndex + 1;
			} else if (nextEndGramarIndex > -1) {
				// La prochaine balise de grammaire est fermante et elle est au
				// bon niveau
				// => on a trouvé
				return nextEndGramarIndex;
			}

			nextBeginGramarIndex = output.indexOf(BEGIN_XML_CODE + grammarTag, currentIndex);
			nextEndGramarIndex = output.indexOf(BEGIN_END_XML_CODE + grammarTag, currentIndex);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String execute(final String input, final PublisherData publisherData) {
		final StringBuilder output = new StringBuilder(input);
		String grammarTag;
		int beginGramarIndex;
		int beginGramarLenght;
		int endGramarIndex;
		int endGramarLenght;
		int beginXmlMoveToIndex;
		int endXmlMoveToIndex;
		Queue<TagXML> pileTag;
		int indexOffset;
		int firstBodyIndex;
		int lastBodyEndIndex;

		beginGramarIndex = nextBeginGramarIndex(output, 0);

		while (beginGramarIndex != -1) {

			final int endTagNameIndex = output.indexOf(" ", beginGramarIndex);

			Assertion.check().isTrue(endTagNameIndex > 0,
					"La gramaire du XML source est incorrecte, le tag {0} (i:{1}) n''est pas construit correctement (il manque l'espace apres le nom de tag) ",
					output.substring(beginGramarIndex, beginGramarIndex + 10) + "...", beginGramarIndex);
			grammarTag = output.substring(beginGramarIndex + BEGIN_XML_CODE.length(), endTagNameIndex);
			beginGramarLenght = output.indexOf(END_XML_CODE, beginGramarIndex + BEGIN_XML_CODE.length()) - beginGramarIndex + END_XML_CODE.length();
			Assertion.check().isTrue(beginGramarLenght > 0,
					"La gramaire du XML source est incorrecte, le tag <#{0} (i:{1}) n''est pas fermé correctement (il manque #>) ",
					grammarTag, beginGramarIndex);

			endGramarIndex = findEndGrammarIndex(output, grammarTag, beginGramarIndex + beginGramarLenght);
			endGramarLenght = output.indexOf(END_XML_CODE, endGramarIndex + (BEGIN_END_XML_CODE + grammarTag).length()) - endGramarIndex + END_XML_CODE.length();
			Assertion.check()
					.isTrue(endGramarIndex > 0,
							"La gramaire du XML source est incorrecte, le tag {0} (i:{1}) n''est pas construit correctement (la balise de fin est introuvable)",
							grammarTag, beginGramarIndex)
					.isTrue(endGramarLenght > 0,
							"La gramaire du XML source est incorrecte, le tag <#end{0} (i:{1}) n''est pas fermé correctement (il manque #>)",
							grammarTag, endGramarIndex);
			// on determine la pile des tags internes non closes ou ouverte :
			final String content = output.substring(beginGramarIndex + beginGramarLenght, endGramarIndex);
			pileTag = ProcessorXMLUtil.extractUnrepeatableTag(content.toCharArray());
			int nbIteration = 0;
			while (!pileTag.isEmpty()) {
				TagXML firstOpenTag = null;
				TagXML lastCloseTag = null;
				for (final TagXML tag : pileTag) {
					if (!tag.isOpenTag()) {
						lastCloseTag = tag;
					} else {
						firstOpenTag = tag;
						break; // on a terminé
					}
				}
				beginXmlMoveToIndex = beginGramarIndex;
				endXmlMoveToIndex = endGramarIndex;
				indexOffset = beginGramarIndex + beginGramarLenght;

				// Les balises ne peuvent étre déplacée au dela des elements affichés, on cherche les bornes le 1er et le dernier corps de tag
				firstBodyIndex = ProcessorXMLUtil.getFirstBodyIndex(content) + indexOffset;
				lastBodyEndIndex = ProcessorXMLUtil.getLastBodyEndIndex(content) + indexOffset;

				if (lastCloseTag != null) {
					// si c'est un tag fermant, on étend le début
					// on regarde pour inclure le tag ouvrant
					final int moveBackwardToIndex = output.lastIndexOf('<' + lastCloseTag.getName(), beginGramarIndex);
					// Ou exclure le tag fermant
					final int moveForwardToIndex = lastCloseTag.getIndex() + indexOffset + lastCloseTag.getLength();
					Assertion.check().isTrue(moveBackwardToIndex != -1, "Le XML source est incorrect, la tag de début : {0} n''a pas été trouvée", '<' + lastCloseTag.toString() + '>');
					if (!MODE_CLOSER_CLOSED_XML ||
							beginGramarIndex - moveBackwardToIndex <= moveForwardToIndex - beginGramarIndex - beginGramarLenght
							|| moveForwardToIndex > firstBodyIndex) {
						beginXmlMoveToIndex = moveBackwardToIndex;
					} else {
						beginXmlMoveToIndex = moveForwardToIndex;
					}
				}
				if (firstOpenTag != null) {
					// si c'est un tag ouvrant, on étend la fin
					// on regarde pour inclure le tag fermant
					int moveForwardToIndex = output.indexOf("</" + firstOpenTag.getName(), endGramarIndex);
					moveForwardToIndex = output.indexOf(">", moveForwardToIndex) + 1;
					// ou pour exclure le tag ouvrant
					final int moveBackwardToIndex = firstOpenTag.getIndex() + indexOffset;
					Assertion.check().isTrue(moveForwardToIndex != -1, "Le XML source est incorrect, la tag de fin : {0} n''a pas été trouvée", "</" + firstOpenTag + '>');

					if (MODE_CLOSER_CLOSED_XML && endGramarIndex - moveBackwardToIndex < moveForwardToIndex - endGramarIndex - endGramarLenght && moveBackwardToIndex >= lastBodyEndIndex) {
						endXmlMoveToIndex = moveBackwardToIndex;
					} else {
						endXmlMoveToIndex = moveForwardToIndex;
					}
				}

				// on deplace
				if (beginXmlMoveToIndex != beginGramarIndex) {
					moveGramar(beginGramarIndex, beginXmlMoveToIndex, beginGramarLenght, output);

					// On recalcul la position pour faciliter le test de postcondition
					if (beginXmlMoveToIndex > beginGramarIndex) {
						beginGramarIndex = beginXmlMoveToIndex - beginGramarLenght;
					} else {
						beginGramarIndex = beginXmlMoveToIndex;
					}
				}
				if (endXmlMoveToIndex != endGramarIndex) {
					moveGramar(endGramarIndex, endXmlMoveToIndex, endGramarLenght, output);

					// On recalcul la position pour faciliter le test de postcondition
					if (endXmlMoveToIndex > endGramarIndex) {
						endGramarIndex = endXmlMoveToIndex - endGramarLenght;
					} else {
						endGramarIndex = endXmlMoveToIndex;
					}
				}

				// on test
				pileTag = ProcessorXMLUtil.extractUnrepeatableTag(output.substring(beginGramarIndex + beginGramarLenght, endGramarIndex).toCharArray());
				Assertion.check().isTrue(nbIteration++ < 2 || pileTag.isEmpty(),
						"Le XML n''a pas été corrigé aprés 3 itérations : il reste : {1}\ndans le corps de la balise {2}\ncontent: {0}",
						output.toString(), pileTag, output.substring(beginGramarIndex, beginGramarIndex + beginGramarLenght));
			}
			Assertion.check().isTrue(pileTag.isEmpty(),
					"Le XML n''a pas été corrigé : il reste : {1}\ndans le corps de la balise {2}\ncontent: {0}",
					output.toString(), pileTag, output.substring(beginGramarIndex, beginGramarIndex + beginGramarLenght));
			// On recupére le prochain
			beginGramarIndex = nextBeginGramarIndex(output, beginGramarIndex + beginGramarLenght);
		}

		return output.toString();
	}

	private static void moveGramar(final int gramarIndex, final int moveToIndex, final int gramarLenght, final StringBuilder output) {
		final String copy = output.substring(gramarIndex, gramarIndex + gramarLenght);
		if (moveToIndex < gramarIndex) {
			output.delete(gramarIndex, gramarIndex + gramarLenght);
			output.insert(moveToIndex, copy);
		} else {
			output.insert(moveToIndex, copy);
			output.delete(gramarIndex, gramarIndex + gramarLenght);
		}
	}

	private static int nextBeginGramarIndex(final StringBuilder output, final int fromIndex) {
		int index = output.indexOf(BEGIN_XML_CODE, fromIndex);
		while (index != -1 && (BEGIN_END_XML_CODE.equals(output.substring(index, index + BEGIN_END_XML_CODE.length())) || output.charAt(index + BEGIN_XML_CODE.length()) == '=')) {
			index = output.indexOf(BEGIN_XML_CODE, index + BEGIN_END_XML_CODE.length());
		}
		return index;
	}
}
