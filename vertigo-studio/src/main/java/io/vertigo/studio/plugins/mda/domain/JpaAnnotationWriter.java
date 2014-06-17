package io.vertigo.studio.plugins.mda.domain;

import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.metamodel.association.AssociationNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestion centralisée des annotations sur les objets kasper générés.
 * Cette implémentation complète les annotations standards et rajoute celles propre à JPA.
 * @author  pchretien, npiedeloup
 */
final class JpaAnnotationWriter extends AnnotationWriter {
	private final String sequencePrefix = "SEQ_";

	/**
	 * Ectiture des annotations sur une DT_DEFINITION.
	 * @param dtDefinition DtDefinition
	 * @return Liste des lignes de code java à ajouter.
	 */
	@Override
	List<String> writeAnnotations(final DtDefinition dtDefinition) {
		final List<String> lines;
		lines = writeJpaAnnotations(dtDefinition);
		lines.addAll(super.writeAnnotations(dtDefinition));
		return lines;
	}

	/**
	 * Ectiture des annotations sur une DT_DEFINITION.
	 * @param dtDefinition DtDefinition
	 * @return Liste des lignes de code java à ajouter.
	 */
	private List<String> writeJpaAnnotations(final DtDefinition dtDefinition) {
		final List<String> lines = new ArrayList<>();
		if (dtDefinition.getIdField().isDefined()) { //Il faut un Id pour déclarer l'élément comme Entity. Nous faisons le choix de déclarer comme Entity même les Objects non persistant.
			lines.add("@javax.persistence.Entity");
			if (dtDefinition.isPersistent()) {
				lines.add("@javax.persistence.Table (name = \"" + getTableName(dtDefinition) + "\")");
				if (containsDataStreamField(dtDefinition)) {
					lines.add("@org.hibernate.annotations.TypeDefs(value = { @org.hibernate.annotations.TypeDef(name = \"DO_STREAM\", typeClass = io.vertigo.dynamo.plugins.database.connection.hibernate.DataStreamType.class) })");
				}
			}
		}
		return lines;
	}

	private boolean containsDataStreamField(final DtDefinition dtDefinition) {
		for (final DtField field : dtDefinition.getFields()) {
			if (field.isPersistent() && field.getDomain().getDataType() == DataType.DataStream) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ecriture des annotations sur un DT_FIELD.
	 * @param dtField Champ de la DT_DEFINITION
	 * @return Liste des lignes de code java à ajouter.
	 */
	@Override
	List<String> writeAnnotations(final DtField dtField, final DtDefinition dtDefinition) {
		final List<String> lines;
		lines = writeJpaAnnotations(dtField, dtDefinition);
		lines.addAll(super.writeAnnotations(dtField, dtDefinition));
		return lines;
	}

	/**
	 * Ectiture des annotations sur un DT_FIELD.
	 * @param field Champ de la DT_DEFINITION
	 * @return Liste des lignes de code java à ajouter.
	 */
	private List<String> writeJpaAnnotations(final DtField field, final DtDefinition dtDefinition) {
		final List<String> lines = new ArrayList<>();

		//Générations des annotations JPA / hibernate
		if (field.getType() == DtField.FieldType.PRIMARY_KEY) {
			lines.add("@javax.persistence.Id");
			//TODO la gestion des sequences est propre à Oracle, HSQL, PostgreSql : autres bdd, autres stratégies
			if (dtDefinition.isPersistent()) {
				final String sequence = getSequenceName(dtDefinition);
				lines.add("@javax.persistence.SequenceGenerator(name = \"sequence\", sequenceName = \"" + sequence + "\")");
				lines.add("@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = \"sequence\")");
			}
		}
		if (dtDefinition.isPersistent()) {
			final String fieldName = field.getName();
			lines.add("@javax.persistence.Column(name = \"" + fieldName + "\")");
			if (!field.isPersistent()) {
				lines.add("@javax.persistence.Transient");
			}
			if (field.isPersistent() && field.getDomain().getDataType() == DataType.DataStream) {
				lines.add("@org.hibernate.annotations.Type(type = \"DO_STREAM\")");
			}
		}
		return lines;
	}

	/**
	 * Ectiture des annotations sur un DT_FIELD gérant une association.
	 * @param associationNode Noeud de l'association
	 * @return Liste des lignes de code java à ajouter.
	 */
	@Override
	List<String> writeAnnotations(final AssociationNode associationNode) {
		final List<String> lines;
		lines = writeJpaAnnotations(associationNode);
		lines.addAll(super.writeAnnotations(associationNode));
		return lines;
	}

	/**
	 * Ectiture des annotations sur un DT_FIELD gérant une association.
	 * @param associationNode Noeud de l'association
	 * @return Liste des lignes de code java à ajouter.
	 */
	private List<String> writeJpaAnnotations(final AssociationNode associationNode) {
		final List<String> lines = new ArrayList<>();
		lines.add("@javax.persistence.Transient"); //On ne crée pas de grappe d'objet
		return lines;
	}

	/**
	 * Nom de la table en fonction de la définition du DT mappé.
	 *
	 * @param dtDefinition Définition du DT mappé
	 * @return Nom de la table
	 */
	private final String getTableName(final DtDefinition dtDefinition) {
		return dtDefinition.getLocalName();
	}

	/**
	 * Nom de la séquence utilisée lors des inserts
	 * @param dtDefinition Définition du DT mappé
	 * @return String Nom de la sequence
	 */
	private String getSequenceName(final DtDefinition dtDefinition) {
		//oracle n'autorise pas de sequence de plus de 30 char.
		String seqName = sequencePrefix + getTableName(dtDefinition);
		if (seqName.length() > 30) {
			seqName = seqName.substring(0, 30);
		}
		return seqName;
	}
}
