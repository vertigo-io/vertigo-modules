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
package io.vertigo.quarto.plugins.publisher.odt;

import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertigo.core.util.StringUtil;

/**
 * Classe de nettoyage d'une arborescence XML.
 * Gestion du cas ou il y a des balises ouvertes non fermées.
 *
 * @author brenard
 */
final class ODTCleanerUtil {

	private static final Logger LOGGER = LogManager.getLogger(ODTCleanerUtil.class);

	/**
	 * Constructeur privé pour classe utilitaire.
	 */
	private ODTCleanerUtil() {
		//RAS
	}

	/**
	 * Corrige le contenu qui est une arborescence XML pour gérer le cas
	 * ou il y a des balises ouvertes non fermées.
	 *
	 * @param xmlContent Arborescence XML.
	 * @return Arborescence nettoyée.
	 */
	static String clean(final String xmlContent) {
		final StringBuilder contentClean = new StringBuilder();
		cleanContent(xmlContent.toCharArray(), contentClean);
		return contentClean.toString();
	}

	private static void cleanContent(final char[] content, final StringBuilder contentClean) {
		final Stack<String> pileBalise = new Stack<>();

		// On parcours le contenu
		char current;
		final StringBuilder currentOuvrante = new StringBuilder();
		final StringBuilder currentFermante = new StringBuilder();
		boolean baliseOuvrante = false;
		boolean baliseOuvranteEnCours = false;
		boolean baliseFermante = false;
		boolean baliseFermanteEnCours = false;

		final int length = content.length;
		for (int i = 0; i < length; i++) {
			current = content[i];
			// debut de balise
			if (current == '<') {
				// On regarde s'il s'agit d'une ouverture ou d'une fermeture
				if (content[i + 1] == '/') {
					baliseFermante = true;
					baliseFermanteEnCours = true;
					i += 2;
					current = content[i];
				} else if (content[i + 1] != ' ') {
					contentClean.append(current);
					i++;
					current = content[i];
					baliseOuvrante = true;
					baliseOuvranteEnCours = true;
				}
			}

			// On ajoute le contenu courant que dans le cas ou l'on ne traite pas de
			// balise fermante. En effet si la balise fermante courante ne correspond
			// pas à le dernière balise ouvrante alors il faut inserer la bonne avant
			// de mettre la balise fermante courante.
			if (!baliseFermanteEnCours) {
				contentClean.append(current);
			}

			// Cas particulier du <? xml ?>
			if (baliseOuvrante && current == '?') {
				currentOuvrante.setLength(0);
				baliseOuvrante = false;
				baliseOuvranteEnCours = false;
			}

			// Si on rencontre un de ces caractères alors on connait le nom de la balise
			// ouvrante
			if (baliseOuvrante && (current == '/' || current == ' ' || current == '>')) {
				baliseOuvrante = false;
			}

			// On ajoute la balise ouvrante dans la pile lorsque l'on a atteind
			// le signe de fermeture
			if (current == '>' && baliseOuvranteEnCours) {
				if (content[i - 1] != '/') {
					pileBalise.push(currentOuvrante.toString());
				}
				baliseOuvranteEnCours = false;
				currentOuvrante.setLength(0);
			}

			// Lorsque l'on a atteind la fin de la balise fermante alors on regarde
			// si elle correspond bien à la dernière balise ouvrante
			if (baliseFermante && current == '>') {
				fermeBalisesOuvertes(contentClean, pileBalise, currentFermante);
				currentFermante.setLength(0);
				baliseFermante = false;
				baliseFermanteEnCours = false;
			}

			if (baliseOuvrante) {
				currentOuvrante.append(current);
			}

			if (baliseFermante) {
				currentFermante.append(current);
			}
		}
	}

	private static void fermeBalisesOuvertes(final StringBuilder contentClean, final Stack<String> pileBalise, final StringBuilder currentFermante) {
		//Si la balise fermante n'est pas présent dans la pile des balises déjà ouvertes, c'est qu'elle a disparu lors de la fusion,
		//on retire alors la balise fermante (corrige le nullPointer lors des pop() ).
		if (!pileBalise.contains(currentFermante.toString())) {
			LOGGER.warn(StringUtil.format("La balise fermante </{0}> n'est plus ouverte dans le document généré, elle est retiré du document.", currentFermante));
		} else {
			String lastBalise = pileBalise.pop();
			//Tant que la balise fermante ne correspond pas à la
			// balise ouvrante alors on ferme les balises ouvrantes
			while (currentFermante.length() != lastBalise.length() && currentFermante.indexOf(lastBalise) != 0) {
				contentClean.append("</").append(lastBalise).append('>');
				lastBalise = pileBalise.pop();
			}
			contentClean.append("</").append(currentFermante).append('>');
		}
	}
}
