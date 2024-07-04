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

import java.io.IOException;

import io.vertigo.quarto.publisher.model.PublisherData;

/**
 * Interface d'un processor pour le reportMerger.
 * Ce processor à une entrée et une sortie de meme type,
 * la sortie est issus d'un traitement prenant une entrée et les parametres du merger.
 * @author npiedeloup
 */
public interface MergerProcessor {
	/**
	 * Utilise la chaine d'entrée et les parametres de merge pour faire un traitement spécifique.
	 * 
	 * Attention la valeur du input peut avoir été mutée.
	 * @param input Donnée d'entrée.
	 * @param publisherData Données de la fusion d'édition
	 * @return Chaine manipulée par le processor
	 * @throws IOException Erreur I/O
	 */
	String execute(String input, final PublisherData publisherData) throws IOException;
}
