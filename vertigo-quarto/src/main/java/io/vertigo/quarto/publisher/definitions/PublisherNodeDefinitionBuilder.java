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
package io.vertigo.quarto.publisher.definitions;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Builder;

/**
 * Builder de la définition d'un modèle de noeud d'édition.
 * Un noeud d'edition compose l'arbre des des données d'une édition.
 *
 * @author npiedeloup, pchretien
 */
public final class PublisherNodeDefinitionBuilder implements Builder<PublisherNodeDefinition> {
	private final List<PublisherField> publisherFields = new ArrayList<>();

	/**
	 * Ajoute un champ booléen.
	 * @param fieldName Nom du champ
	 * @return Builder
	 */
	public PublisherNodeDefinitionBuilder addBooleanField(final String fieldName) {
		return addField(fieldName, PublisherFieldType.Boolean, null);
	}

	/**
	 * Ajoute un champ String.
	 * @param fieldName Nom du champ
	 * @return Builder
	 */
	public PublisherNodeDefinitionBuilder addStringField(final String fieldName) {
		return addField(fieldName, PublisherFieldType.String, null);
	}

	/**
	 * Ajoute un champ Image.
	 * @param fieldName Nom du champ
	 * @return Builder
	 */
	public PublisherNodeDefinitionBuilder addImageField(final String fieldName) {
		return addField(fieldName, PublisherFieldType.Image, null);
	}

	/**
	 * Ajoute un champ Data (autre noeud).
	 * @param fieldName Nom du champ
	 * @param nodeDefinition Définition du noeud
	 * @return Builder
	 */
	public PublisherNodeDefinitionBuilder addNodeField(final String fieldName, final PublisherNodeDefinition nodeDefinition) {
		return addField(fieldName, PublisherFieldType.Node, nodeDefinition);
	}

	/**
	 * Ajoute un champ List (liste composée de noeud).
	 * @param fieldName Nom du champ
	 * @param nodeDefinition Définition des éléments de la liste
	 * @return Builder
	 */
	public PublisherNodeDefinitionBuilder addListField(final String fieldName, final PublisherNodeDefinition nodeDefinition) {
		return addField(fieldName, PublisherFieldType.List, nodeDefinition);
	}

	private PublisherNodeDefinitionBuilder addField(final String fieldName, final PublisherFieldType fieldType, final PublisherNodeDefinition nodeDefinition) {
		publisherFields.add(new PublisherField(fieldName, fieldType, nodeDefinition));
		return this;
	}

	/**
	 * @return PublisherDataNodeDefinition
	 */
	@Override
	public PublisherNodeDefinition build() {
		return new PublisherNodeDefinition(publisherFields);
	}
}
