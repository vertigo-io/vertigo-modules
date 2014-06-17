package io.vertigo.dynamo.domain.metamodel;

import io.vertigo.kernel.lang.Assertion;
import io.vertigo.kernel.lang.Builder;
import io.vertigo.kernel.lang.MessageKey;
import io.vertigo.kernel.lang.MessageText;
import io.vertigo.kernel.metamodel.DefinitionUtil;

import java.util.ArrayList;
import java.util.List;

/** 
 * Builder de définition.
 * Tout DT doit avoir un nom en majuscule préfixé par DT_.
 * Pour obtenir la DtDéfinition utiliser la méthode build();
 * 
 * Le DtDefinitionsBuilder doit être flushée.
 * 
 * @author pchretien
 */
public final class DtDefinitionBuilder implements Builder<DtDefinition> {
	private static class MessageKeyImpl implements MessageKey {
		private static final long serialVersionUID = 6959551752755175151L;

		private final String name;

		MessageKeyImpl(final String name) {
			this.name = name;
		}

		/** {@inheritDoc} */
		public String name() {
			return name;
		}
	}

	private DtDefinition dtDefinition;
	private final String dtDefinitionName;
	private String packageName;
	private boolean dtDefinitionPersistent;
	private boolean dtDefinitionDynamic;
	private final List<DtField> fields = new ArrayList<>();

	/**
	 * Constructeur.
	 */
	public DtDefinitionBuilder(final String name) {
		Assertion.checkArgNotEmpty(name);
		//---------------------------------------------------------------------
		dtDefinitionName = name;
	}

	public DtDefinitionBuilder withPackageName(final String newPackageName) {
		//packageName peut être null
		//---------------------------------------------------------------------
		packageName = newPackageName;
		return this;
	}

	public DtDefinitionBuilder withPersistent(final boolean persistent) {
		dtDefinitionPersistent = persistent;
		return this;
	}

	public DtDefinitionBuilder withDynamic(final boolean dynamic) {
		dtDefinitionDynamic = dynamic;
		return this;
	}

	/**
	 * Ajout d'une FK.
	 * @param fieldName Nom du champ
	 * @param fkDtDefinitionName Definition référencée
	 * @param label Libellé du champ
	 * @param notNull Si la FK est obligatoire
	 */
	public DtDefinitionBuilder withForeignKey(final String fieldName, final String label, final Domain domain, final boolean notNull, final String fkDtDefinitionName, final boolean sort, final boolean display) {
		//Pour l'instant on ne gère pas les chamsp computed dynamiques
		final boolean persistent = true;
		final DtField dtField = createField(fieldName, DtField.FieldType.FOREIGN_KEY, domain, label, notNull, persistent, fkDtDefinitionName, null, false, sort, display);
		//On suppose que le build est déjà effectué.
		dtDefinition.registerDtField(dtField);
		return this;
	}

	/**
	 * Ajout d'un champs calculé.
	 * @param domain Domaine associé au champ
	 * @param fieldName Nom du champ
	 * @param label Libellé du champ
	 * @param computedExpression Expression du champs calculé
	 */
	public DtDefinitionBuilder withComputedField(final String fieldName, final String label, final Domain domain, final ComputedExpression computedExpression, final boolean sort, final boolean display) {
		//Pour l'instant on ne gère pas les chamsp computed dynamiques
		final DtField dtField = createField(fieldName, DtField.FieldType.COMPUTED, domain, label, false, false, null, computedExpression, false, sort, display);
		fields.add(dtField);
		return this;
	}

	/**
	 * Ajout d'un champ de type DATA.
	 * @param fieldName Nom du champ
	 * @param domain Domaine associé au champ
	 * @param label Libellé du champ
	 * @param notNull Si le champ est obligatoire
	 * @param persistent Si le champ est persisté
	 */
	public DtDefinitionBuilder withDataField(final String fieldName, final String label, final Domain domain, final boolean notNull, final boolean persistent, final boolean sort, final boolean display) {
		//le champ  est dynamic SSI la définition est dynamique
		final DtField dtField = createField(fieldName, DtField.FieldType.DATA, domain, label, notNull, persistent, null, null, dtDefinitionDynamic, sort, display);
		fields.add(dtField);
		return this;
	}

	/**
	 * Ajout d'un champ de type ID.
	 * @param fieldName Nom du champ
	 * @param domain Domaine associé au champ
	 * @param label Libellé du champ
	 */
	public DtDefinitionBuilder withIdField(final String fieldName, final String label, final Domain domain, final boolean sort, final boolean display) {
		//le champ ID est tjrs notNull
		final boolean notNull = true;
		//le champ ID est persistant SSI la définition est persitante.
		final boolean persistent = dtDefinitionPersistent;
		//le champ  est dynamic SSI la définition est dynamique
		final DtField dtField = createField(fieldName, DtField.FieldType.PRIMARY_KEY, domain, label, notNull, persistent, null, null, dtDefinitionDynamic, sort, display);
		fields.add(dtField);
		return this;
	}

	private DtField createField(final String fieldName, final DtField.FieldType type, final Domain domain, final String strLabel, final boolean notNull, final boolean persistent, final String fkDtDefinitionName, final ComputedExpression computedExpression, final boolean dynamic, final boolean sort, final boolean display) {

		final String shortName = DefinitionUtil.getLocalName(dtDefinitionName, DtDefinition.class);
		//----------------------------------------------------------------------
		// Le DtField vérifie ses propres règles et gère ses propres optimisations
		final String id = DtField.PREFIX + shortName + '$' + fieldName;

		Assertion.checkArgNotEmpty(strLabel, "Label doit être non vide");
		//2. Sinon Indication de longueur portée par le champ du DT.
		//-----------------------------------------------------------------------
		final MessageText label = new MessageText(strLabel, new MessageKeyImpl(id));
		// Champ CODE_COMMUNE >> getCodeCommune()
		//Un champ est persisanty s'il est marqué comme tel et si la définition l'est aussi.
		return new DtField(id, fieldName, type, domain, label, notNull, persistent && dtDefinitionPersistent, fkDtDefinitionName, computedExpression, dynamic, sort, display);
	}

	public DtDefinition build() {
		Assertion.checkState(dtDefinition == null, "Build deja effectué");
		//-----------------------------------------------------------------
		dtDefinition = new DtDefinition(dtDefinitionName, packageName, dtDefinitionPersistent, fields, dtDefinitionDynamic);
		return dtDefinition;
	}

}
