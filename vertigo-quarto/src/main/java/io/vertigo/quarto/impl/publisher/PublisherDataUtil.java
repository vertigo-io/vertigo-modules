/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.quarto.impl.publisher;

import java.util.ArrayList;
import java.util.List;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.BasicType;
import io.vertigo.core.node.Node;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.structure.definitions.DtDefinition;
import io.vertigo.datamodel.structure.definitions.DtField;
import io.vertigo.datamodel.structure.definitions.DtProperty;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.DtObject;
import io.vertigo.datamodel.structure.util.DtObjectUtil;
import io.vertigo.quarto.publisher.definitions.PublisherField;
import io.vertigo.quarto.publisher.definitions.PublisherNodeDefinition;
import io.vertigo.quarto.publisher.model.PublisherNode;

/**
 * Classe de récupération des données pour les editions.
 *
 * @author oboitel, npiedeloup
 */
public final class PublisherDataUtil {
	/**
	 * Constructeur privé pour class utilitaire.
	 */
	private PublisherDataUtil() {
		//rien
	}

	/**
	 * Peuple un champs de type data dans un node.
	 * @param parentNode Node parent
	 * @param fieldName Nom du champs
	 * @param dtoValue Dto contenant les valeurs
	 * @return publisherNode du champ
	 */
	public static PublisherNode populateField(final PublisherNode parentNode, final String fieldName, final DtObject dtoValue) {
		final PublisherNode childNode = parentNode.createNode(fieldName);
		PublisherDataUtil.populateData(dtoValue, childNode);
		parentNode.setNode(fieldName, childNode);
		return childNode;
	}

	/**
	 * Peuple un champs de type data dans un node.
	 * @param parentNode Node parent
	 * @param fieldName Nom du champs
	 * @param dtcValue DTC contenant les valeurs
	 */
	public static void populateField(final PublisherNode parentNode, final String fieldName, final DtList<?> dtcValue) {
		final List<PublisherNode> publisherNodes = new ArrayList<>();
		for (final DtObject dto : dtcValue) {
			final PublisherNode childNode = parentNode.createNode(fieldName);
			PublisherDataUtil.populateData(dto, childNode);
			publisherNodes.add(childNode);
		}
		parentNode.setNodes(fieldName, publisherNodes);
	}

	/**
	 * Peuple un publisherDataNode à partir de champs du Dto qui correspondent.
	 * @param dto Objet de données
	 * @param publisherDataNode PublisherDataNode
	 */
	public static void populateData(final DtObject dto, final PublisherNode publisherDataNode) {
		Assertion.check()
				.isNotNull(dto)
				.isNotNull(publisherDataNode);
		//-----
		final DtDefinition dtDefinition = DtObjectUtil.findDtDefinition(dto);
		final List<String> dtFieldNames = getDtFieldList(dtDefinition);
		final PublisherNodeDefinition pnDefinition = publisherDataNode.getNodeDefinition();
		int nbMappedField = 0;
		for (final PublisherField publisherField : pnDefinition.getFields()) {
			final String fieldName = publisherField.getName();
			if (!dtFieldNames.contains(fieldName)) {
				continue;
			}
			final DtField dtField = dtDefinition.getField(fieldName);
			final Object value = dtField.getDataAccessor().getValue(dto);
			nbMappedField++;
			switch (publisherField.getFieldType()) {
				case Boolean:
					Assertion.check().isTrue(value instanceof Boolean, "Le champ {0} du DT {1} doit être un Boolean (non null)", fieldName, dtDefinition.getName());
					publisherDataNode.setBoolean(fieldName, (Boolean) value);
					break;
				case String:
					final String renderedField = value != null ? renderStringField(dto, dtField) : "";//un champ null apparait comme vide
					publisherDataNode.setString(fieldName, renderedField);
					break;
				case List:
					if (value != null) { //on autorise les listes null et on la traite comme vide
						//car la composition d'objet métier n'est pas obligatoire
						//et le champ sera peut être peuplé plus tard
						final DtList<?> dtc = (DtList<?>) value;
						final List<PublisherNode> publisherNodes = new ArrayList<>();
						for (final DtObject element : dtc) {
							final PublisherNode publisherNode = publisherDataNode.createNode(fieldName);
							populateData(element, publisherNode);
							publisherNodes.add(publisherNode);
						}
						publisherDataNode.setNodes(fieldName, publisherNodes);
					}
					break;
				case Node:
					if (value != null) { //on autorise les objet null,
						//car la composition d'objet métier n'est pas obligatoire
						//et le champ sera peut être peuplé plus tard
						final DtObject element = (DtObject) value;
						final PublisherNode elementPublisherDataNode = publisherDataNode.createNode(fieldName);
						populateData(element, elementPublisherDataNode);
						publisherDataNode.setNode(fieldName, elementPublisherDataNode);
					}
					break;
				case Image:
					throw new IllegalArgumentException("Type unsupported : " + publisherField.getFieldType());
				default:
					throw new IllegalArgumentException("Type unknown : " + publisherField.getFieldType());
			}
		}
		Assertion.check().isTrue(nbMappedField > 0,
				"Aucun champ du Dt ne correspond à ceux du PublisherNode, vérifier vos définitions. ({0}:{1}) et ({2}:{3})", "PN",
				pnDefinition.getFields(), dtDefinition.getName(), dtFieldNames);
	}

	/**
	 * Gére le rendu d'un champs de type String.
	 * @param dto l'objet sur lequel porte le champs
	 * @param dtField le champs à rendre
	 * @return la chaine de caractère correspondant au rendu du champs
	 */
	public static String renderStringField(final DtObject dto, final DtField dtField) {
		final String unit = dtField.smartTypeDefinition().getProperties().getValue(DtProperty.UNIT);
		final SmartTypeManager smartTypeManager = Node.getNode().getComponentSpace().resolve(SmartTypeManager.class);
		final Object value = dtField.getDataAccessor().getValue(dto);
		final String formattedValue = smartTypeManager.valueToString(dtField.smartTypeDefinition(), value);
		return formattedValue + (!StringUtil.isBlank(unit) ? " " + unit : "");
	}

	private static List<String> getDtFieldList(final DtDefinition dtDefinition) {
		final List<String> dtFieldNames = new ArrayList<>();
		for (final DtField dtField : dtDefinition.getFields()) {
			dtFieldNames.add(dtField.name());
		}
		return dtFieldNames;
	}

	//=========================================================================
	//======Génération de PublisherNode en KSP à partir des DtDefinitions======
	//=========================================================================

	/**
	 * Méthode utilitaire pour générer une proposition de définition de PublisherNode, pour des DtDefinitions.
	 * @param dtDefinitions DtDefinition à utiliser.
	 * @return Proposition de PublisherNode.
	 */
	public static String generatePublisherNodeDefinitionAsKsp(final String... dtDefinitions) {
		final StringBuilder sb = new StringBuilder();
		for (final String dtDefinitionUrn : dtDefinitions) {
			appendPublisherNodeDefinition(sb, Node.getNode().getDefinitionSpace().resolve(dtDefinitionUrn, DtDefinition.class));
			sb.append('\n');
		}
		return sb.toString();
	}

	private static void appendPublisherNodeDefinition(final StringBuilder sb, final DtDefinition dtDefinition) {
		sb.append("PN_").append(dtDefinition.id().shortName()).append("  = new PublisherNode (\n");
		for (final DtField dtField : dtDefinition.getFields()) {
			final String fieldName = dtField.name();
			switch (dtField.smartTypeDefinition().getScope()) {
				case BASIC_TYPE:
					if (BasicType.Boolean == dtField.smartTypeDefinition().getBasicType()) {
						sb.append("\t\tbooleanField[").append(fieldName).append(")] = new DataField ();\n");
					} else {
						sb.append("\t\tstringField[").append(fieldName).append(")] = new DataField ();\n");
					}
					break;
				case DATA_TYPE:
					if (dtField.cardinality().hasMany()) {
						sb.append("\t\tlistField[").append(fieldName).append(")] = new NodeField (type = PN_").append(DtDefinition.PREFIX + dtField.smartTypeDefinition().getJavaClass().getSimpleName()).append(";);\n");
					} else {
						sb.append("\t\tdataField[").append(fieldName).append(")] = new NodeField (type = PN_").append(DtDefinition.PREFIX + dtField.smartTypeDefinition().getJavaClass().getSimpleName()).append(";);\n");
					}
					break;
				case VALUE_TYPE:
				default:
					throw new IllegalArgumentException("Value object smartTypes are not supported yet in publisher");

			}

		}
		sb.append(");\n");

	}
}
