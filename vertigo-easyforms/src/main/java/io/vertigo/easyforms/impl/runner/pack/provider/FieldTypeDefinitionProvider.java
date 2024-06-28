package io.vertigo.easyforms.impl.runner.pack.provider;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.provider.FieldValidatorTypeDefinitionProvider.FieldValidatorEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.fieldtype.AutocompleteFieldType;
import io.vertigo.easyforms.impl.runner.pack.provider.fieldtype.FileFieldType;
import io.vertigo.easyforms.impl.runner.pack.provider.fieldtype.CustomListFieldType;
import io.vertigo.easyforms.impl.runner.pack.provider.fieldtype.InternalMapFieldType;
import io.vertigo.easyforms.impl.runner.pack.provider.fieldtype.RadioLayoutFieldType;
import io.vertigo.easyforms.impl.runner.pack.provider.fieldtype.SimpleFieldType;
import io.vertigo.easyforms.impl.runner.pack.provider.fieldtype.TextAreaType;
import io.vertigo.easyforms.impl.runner.pack.provider.fieldtype.YesNoFieldType;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;

public final class FieldTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldTypeDefinition> {

	public enum FieldTypeEnum implements EnumDefinition<EasyFormsFieldTypeDefinition, FieldValidatorEnum> {
		LABEL(EasyFormsSmartTypes.EfLabel, UiComponentEnum.TEXT_FIELD),

		LAST_NAME(new AutocompleteFieldType(EasyFormsSmartTypes.EfNom, "family-name")),

		FIRST_NAME(new AutocompleteFieldType(EasyFormsSmartTypes.EfPrenom, "given-name")),

		EMAIL(new AutocompleteFieldType(EasyFormsSmartTypes.EfEmail, "email")),

		DATE(EasyFormsSmartTypes.EfDate, UiComponentEnum.DATE),

		BIRTH_DATE(EasyFormsSmartTypes.EfDatePassee, UiComponentEnum.DATE),

		PHONE(new AutocompleteFieldType(EasyFormsSmartTypes.EfTelephone, "tel")),

		VISA(EasyFormsSmartTypes.EfVisa, UiComponentEnum.TEXT_FIELD),

		NATIONALITY(EasyFormsSmartTypes.EfNationalite, UiComponentEnum.TEXT_FIELD),

		POSTAL_CODE(new AutocompleteFieldType(EasyFormsSmartTypes.EfCodePostal, "postal-code")),

		TEXT(new TextAreaType()),

		YES_NO(new YesNoFieldType()),

		COUNT(EasyFormsSmartTypes.EfCount, UiComponentEnum.NUMBER),

		CUSTOM_LIST_SELECT(new CustomListFieldType(UiComponentEnum.SELECT)),
		CUSTOM_LIST_RADIO(new CustomListFieldType(UiComponentEnum.RADIO)),
		CUSTOM_LIST_CHECKBOX(new CustomListFieldType(UiComponentEnum.CHECKBOX)),

		FILE(new FileFieldType()),

		// internal use (null category means "hidden")
		INTERNAL_LAYOUT(new RadioLayoutFieldType()),
		INTERNAL_MAP(new InternalMapFieldType()),
		INTERNAL_EXTENSIONS(null, EasyFormsSmartTypes.EfIExtList, UiComponentEnum.TEXT_FIELD), // used by file upload
		COUNT_STRICT(null, EasyFormsSmartTypes.EfCountStrict, UiComponentEnum.NUMBER),
		;

		// ---

		private final String definitionName;
		private final IEasyFormsFieldTypeDefinitionSupplier typeSupplier;

		private FieldTypeEnum(final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
			this(new SimpleFieldType(smartType, uiComponent));
		}

		private FieldTypeEnum(final String category, final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
			this(new SimpleFieldType(category, smartType, uiComponent));
		}

		private FieldTypeEnum(final IEasyFormsFieldTypeDefinitionSupplier typeSupplier) {
			definitionName = EasyFormsFieldTypeDefinition.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.typeSupplier = typeSupplier;
		}

		@Override
		public EasyFormsFieldTypeDefinition buildDefinition(final DefinitionSpace definitionSpace) {
			return typeSupplier.get(definitionName);
		}

		@Override
		public EasyFormsFieldTypeDefinition get() {
			return EasyFormsFieldTypeDefinition.resolve(definitionName);
		}

		@Override
		public String getDefinitionName() {
			return definitionName;
		}

	}

	@Override
	public Class<FieldTypeEnum> getEnumClass() {
		return FieldTypeEnum.class;
	}

}
