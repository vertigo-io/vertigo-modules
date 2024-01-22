package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.structure.model.DtObject;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldType;
import io.vertigo.easyforms.impl.easyformsrunner.library.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldConstraintDefinitionProvider.FieldConstraintEnum;

public class FieldTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldType> {

	public enum FieldTypeEnum implements EnumDefinition<EasyFormsFieldType, FieldConstraintEnum> {
		LABEL("Label", EasyFormsSmartTypes.EfLabel, "textfield", null),
		NOM("Nom", EasyFormsSmartTypes.EfNom, "textfield", "family-name"),
		PRENOM("Prénom", EasyFormsSmartTypes.EfPrenom, "textfield", "given-name"),
		EMAIL("Email", EasyFormsSmartTypes.EfEmail, "textfield", "email"),
		DATE("Date", EasyFormsSmartTypes.EfDate, "date", null),
		DATE_NAISSANCE("Date de naissance", EasyFormsSmartTypes.EfDatePassee, "date", "bday"),
		TELEPHONE("Téléphone", EasyFormsSmartTypes.EfTelephone, "textfield", "tel"),
		VISA("N° de visa", EasyFormsSmartTypes.EfVisa, "textfield", null),
		NATIONALITE("Nationalité", EasyFormsSmartTypes.EfNationalite, "textfield", null),
		CODE_POSTAL("Code postal", EasyFormsSmartTypes.EfCodePostal, "textfield", "postal-code"),
		;

		private final String code;
		private final String label;
		private final String smartType;
		private final String uiComponent;
		private final String uiAutocompleteInputAttribute;
		private final Class<? extends DtObject> listClass;

		private FieldTypeEnum(final String label, final EasyFormsSmartTypes smartType, final String uiComponent, final String uiAutocompleteInputAttribute) {
			this(label, smartType, uiComponent, uiAutocompleteInputAttribute, null);
		}

		private FieldTypeEnum(final String label, final EasyFormsSmartTypes smartType, final String uiComponent, final String uiAutocompleteInputAttribute,
				final Class<? extends DtObject> listClass) {

			code = StringUtil.constToUpperCamelCase(name());
			this.label = label;
			this.smartType = smartType.name();
			this.uiComponent = uiComponent;
			this.uiAutocompleteInputAttribute = uiAutocompleteInputAttribute;
			this.listClass = listClass;
		}

		@Override
		public EasyFormsFieldType buildDefinition(final DefinitionSpace definitionSpace) {
			return EasyFormsFieldType.of(code, label, smartType, uiComponent, uiAutocompleteInputAttribute, listClass);
		}

		@Override
		public EasyFormsFieldType get() {
			return EasyFormsFieldType.resolve(code);
		}

	}

	@Override
	public Class<FieldTypeEnum> getEnumClass() {
		return FieldTypeEnum.class;
	}

}
