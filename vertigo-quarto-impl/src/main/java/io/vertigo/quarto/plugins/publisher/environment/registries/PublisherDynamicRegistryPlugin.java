/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
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
package io.vertigo.quarto.plugins.publisher.environment.registries;

import io.vertigo.core.Home;
import io.vertigo.core.lang.Assertion;
import io.vertigo.dynamo.impl.environment.kernel.meta.Entity;
import io.vertigo.dynamo.impl.environment.kernel.model.DynamicDefinition;
import io.vertigo.dynamo.plugins.environment.registries.AbstractDynamicRegistryPlugin;
import io.vertigo.quarto.publisher.metamodel.PublisherDataDefinition;
import io.vertigo.quarto.publisher.metamodel.PublisherNodeDefinition;
import io.vertigo.quarto.publisher.metamodel.PublisherNodeDefinitionBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DynamicRegistry de PublisherManager.
 * @author npiedeloup
 */
public final class PublisherDynamicRegistryPlugin extends AbstractDynamicRegistryPlugin {
	private final Map<String, PublisherNodeDefinitionBuilder> publisherDefinitionMap = new HashMap<>();
	private final Set<String> unusedNodes = new HashSet<>();

	/**
	 * Constructeur.
	 */
	public PublisherDynamicRegistryPlugin() {
		super(PublisherGrammar.grammar);
		Home.getDefinitionSpace().register(PublisherDataDefinition.class);
	}

	/** {@inheritDoc} */
	public void onDefinition(final DynamicDefinition xdefinition) {
		final Entity metaDefinition = xdefinition.getEntity();

		if (metaDefinition.equals(PublisherGrammar.publisherDefinition)) {
			final PublisherDataDefinition definition = createPublisherDataDefinition(xdefinition);
			Home.getDefinitionSpace().put(definition, PublisherDataDefinition.class);
		} else if (metaDefinition.equals(PublisherGrammar.publisherNodeDefinition)) {
			createPublisherNodeDefinition(xdefinition);
		} else {
			throw new IllegalArgumentException("Type de définition non gérée: " + xdefinition.getDefinitionKey().getName());
		}
	}

	private PublisherDataDefinition createPublisherDataDefinition(final DynamicDefinition xpublisherDefinition) {
		final String definitionName = xpublisherDefinition.getDefinitionKey().getName();
		final String publisherNodeRootName = xpublisherDefinition.getDefinitionKey("root").getName();

		final PublisherNodeDefinition rootDefinition = getNodeDefinitionBuilder(publisherNodeRootName, "root", definitionName).build();
		return new PublisherDataDefinition(definitionName, rootDefinition);
	}

	private void createPublisherNodeDefinition(final DynamicDefinition xpublisherNodeDefinition) {
		final PublisherNodeDefinitionBuilder publisherDataNodeDefinitionBuilder = new PublisherNodeDefinitionBuilder();
		final String publisherDataNodeName = xpublisherNodeDefinition.getDefinitionKey().getName();

		//Déclaration des champs string
		final List<DynamicDefinition> stringFields = xpublisherNodeDefinition.getChildDefinitions(PublisherGrammar.STRING_FIELD);
		for (final DynamicDefinition field : stringFields) {
			final String fieldName = field.getDefinitionKey().getName();
			publisherDataNodeDefinitionBuilder.withStringField(fieldName);
		}

		//Déclaration des champs boolean
		final List<DynamicDefinition> booleanFields = xpublisherNodeDefinition.getChildDefinitions(PublisherGrammar.BOOLEAN_FIELD);
		for (final DynamicDefinition field : booleanFields) {
			publisherDataNodeDefinitionBuilder.withBooleanField(field.getDefinitionKey().getName());
		}

		//Déclaration des champs images
		final List<DynamicDefinition> imageFields = xpublisherNodeDefinition.getChildDefinitions(PublisherGrammar.IMAGE_FIELD);
		for (final DynamicDefinition field : imageFields) {
			publisherDataNodeDefinitionBuilder.withImageField(field.getDefinitionKey().getName());
		}

		//Déclaration des champs data
		final List<DynamicDefinition> dataFields = xpublisherNodeDefinition.getChildDefinitions(PublisherGrammar.DATA_FIELD);
		for (final DynamicDefinition field : dataFields) {
			final String fieldName = field.getDefinitionKey().getName();
			final String refNodeName = field.getDefinitionKey("type").getName();
			final PublisherNodeDefinitionBuilder publisherNode = getNodeDefinitionBuilder(refNodeName, fieldName, publisherDataNodeName);
			publisherDataNodeDefinitionBuilder.withNodeField(fieldName, publisherNode.build());
		}

		//Déclaration des champs list
		final List<DynamicDefinition> listFields = xpublisherNodeDefinition.getChildDefinitions(PublisherGrammar.LIST_FIELD);
		for (final DynamicDefinition field : listFields) {
			final String fieldName = field.getDefinitionKey().getName();
			final String refNodeName = field.getDefinitionKey("type").getName();
			final PublisherNodeDefinitionBuilder publisherNode = getNodeDefinitionBuilder(refNodeName, fieldName, publisherDataNodeName);
			publisherDataNodeDefinitionBuilder.withListField(fieldName, publisherNode.build());
		}

		//		System.out.println("Add " + publisherDataNodeName);
		publisherDefinitionMap.put(publisherDataNodeName, publisherDataNodeDefinitionBuilder);
		unusedNodes.add(publisherDataNodeName);
	}

	private PublisherNodeDefinitionBuilder getNodeDefinitionBuilder(final String name, final String fieldName, final String parentNodeName) {
		final PublisherNodeDefinitionBuilder publisherNodeDefinitionBuilder = publisherDefinitionMap.get(name);
		Assertion.checkNotNull(publisherNodeDefinitionBuilder, "Le PublisherNode {0} est introuvable pour le field {1} de {2}.", name, fieldName, parentNodeName);
		unusedNodes.remove(name);
		//		System.out.println("ref " + name + "  in " + parentNodeName + "." + fieldName);

		return publisherNodeDefinitionBuilder;
	}
}
