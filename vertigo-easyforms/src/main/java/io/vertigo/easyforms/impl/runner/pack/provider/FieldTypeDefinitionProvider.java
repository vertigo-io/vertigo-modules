package io.vertigo.easyforms.impl.runner.pack.provider;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.provider.FieldValidatorTypeDefinitionProvider.FieldValidatorEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.RadioCheckUiComponent;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.TextAreaUiComponent;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.TextFieldUiComponent;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.easyforms.runner.model.ui.EasyFormsListItem;

public final class FieldTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldTypeDefinition> {

	public enum FieldTypeEnum implements EnumDefinition<EasyFormsFieldTypeDefinition, FieldValidatorEnum> {
		LABEL(EasyFormsSmartTypes.EfLabel, UiComponentEnum.TEXT_FIELD),

		LAST_NAME(new AutocompleteFieldType(EasyFormsSmartTypes.EfNom, "family-name")),

		FIRST_NAME(new AutocompleteFieldType(EasyFormsSmartTypes.EfPrenom, "given-name")),

		EMAIL(new AutocompleteFieldType(EasyFormsSmartTypes.EfEmail, "email")),

		DATE(EasyFormsSmartTypes.EfDate, UiComponentEnum.DATE),

		BIRTH_DATE(EasyFormsSmartTypes.EfDatePassee, UiComponentEnum.DATE
		// , Map.of(TextFieldUiComponent.AUTOCOMPLETE, "bday") // TODO check on input if it is used
		),

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

		// internal use
		INTERNAL_LAYOUT(new RadioLayoutFieldType()),
		INTERNAL_MAP(null, EasyFormsSmartTypes.EfIMapData, UiComponentEnum.INTERNAL_MAP), // null category to hide this item
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

	// ---

	public static class SimpleFieldType implements IEasyFormsFieldTypeDefinitionSupplier {
		private final String category;
		private final EasyFormsSmartTypes smartType;
		private final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent;

		public SimpleFieldType(final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
			this(DEFAULT_CATEGORY, smartType, uiComponent);
		}

		public SimpleFieldType(final String category, final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
			this.category = category;
			this.smartType = smartType;
			this.uiComponent = uiComponent;
		}

		@Override
		public String getCategory() {
			return category;
		}

		@Override
		public EasyFormsSmartTypes getSmartType() {
			return smartType;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
			return uiComponent;
		}
	}

	public static class AutocompleteFieldType implements IEasyFormsFieldTypeDefinitionSupplier {
		private final EasyFormsSmartTypes smartType;
		private final String uiAutocompleteInputAttribute;

		public AutocompleteFieldType(final EasyFormsSmartTypes smartType, final String uiAutocompleteInputAttribute) {
			this.smartType = smartType;
			this.uiAutocompleteInputAttribute = uiAutocompleteInputAttribute;
		}

		@Override
		public EasyFormsSmartTypes getSmartType() {
			return smartType;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
			return UiComponentEnum.TEXT_FIELD;
		}

		@Override
		public Map<String, Object> getUiParams() {
			return Map.of(TextFieldUiComponent.AUTOCOMPLETE, uiAutocompleteInputAttribute);
		}
	}

	public static class TextAreaType implements IEasyFormsFieldTypeDefinitionSupplier {

		@Override
		public EasyFormsSmartTypes getSmartType() {
			return EasyFormsSmartTypes.EfText;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
			return UiComponentEnum.TEXT_AREA;
		}

		@Override
		public List<AbstractEasyFormsTemplateItem> getExposedComponentParams() {
			return List.of(new EasyFormsTemplateItemField(DtProperty.MAX_LENGTH.getName(), FieldTypeEnum.COUNT));
		}

		@Override
		public Map<String, Object> getUiParams() {
			return Map.of(TextAreaUiComponent.AUTOGROW, true);
		}

	}

	public static class YesNoFieldType implements IEasyFormsFieldTypeDefinitionSupplier {
		@Override
		public EasyFormsSmartTypes getSmartType() {
			return EasyFormsSmartTypes.EfBooleen;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
			return UiComponentEnum.RADIO;
		}

		@Override
		public Map<String, Object> getUiParams() {
			return Map.of(
					IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
					IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
					(Serializable) List.of(
							new EasyFormsListItem(true, "#{EfTrue}"),
							new EasyFormsListItem(false, "#{EfFalse}")));
		}
	}

	public static class RadioLayoutFieldType implements IEasyFormsFieldTypeDefinitionSupplier {

		@Override
		public String getCategory() {
			return null; // no category means "hidden"
		}

		@Override
		public EasyFormsSmartTypes getSmartType() {
			return EasyFormsSmartTypes.EfLabel;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
			return UiComponentEnum.RADIO;
		}

		@Override
		public Map<String, Object> getUiParams() {
			return Map.of(
					IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
					IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
					(Serializable) List.of(
							new EasyFormsListItem("", "#{EfFtyCustomListRadio$layoutVerticalLabel}"),
							new EasyFormsListItem("horizontal", "#{EfFtyCustomListRadio$layoutHorizontalLabel}")));
		}

		@Override
		public Object getDefaultValue() {
			return ""; // Vertical
		}

	}

	public static class CustomListFieldType implements IEasyFormsFieldTypeDefinitionSupplier {

		private final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent;

		public CustomListFieldType(final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
			this.uiComponent = uiComponent;
		}

		@Override
		public EasyFormsSmartTypes getSmartType() {
			return EasyFormsSmartTypes.EfLabel;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
			return uiComponent;
		}

		@Override
		public Map<String, Object> getUiParams() {
			if (uiComponent == UiComponentEnum.RADIO || uiComponent == UiComponentEnum.CHECKBOX) {
				return Map.of(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME,
						RadioCheckUiComponent.LAYOUT, "horizontal");
			}
			return Map.of(IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER, IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME);
		}

		@Override
		public List<AbstractEasyFormsTemplateItem> getExposedComponentParams() {
			if (uiComponent == UiComponentEnum.RADIO || uiComponent == UiComponentEnum.CHECKBOX) {
				return List.of(
						new EasyFormsTemplateItemField(RadioCheckUiComponent.LAYOUT, FieldTypeEnum.INTERNAL_LAYOUT)
								.withParameters(Map.of(RadioCheckUiComponent.LAYOUT, "horizontal")),
						new EasyFormsTemplateItemField(IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.INTERNAL_MAP).withMandatory());
			}
			return List.of(new EasyFormsTemplateItemField(IEasyFormsUiComponentDefinitionSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.INTERNAL_MAP).withMandatory());
		}

		@Override
		public boolean isList() {
			return uiComponent == UiComponentEnum.CHECKBOX;
		}

	}
}
