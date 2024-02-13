package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsListItem;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsUiComponent;
import io.vertigo.easyforms.easyformsrunner.model.definitions.IEasyFormsFieldTypeSupplier;
import io.vertigo.easyforms.easyformsrunner.model.definitions.IEasyFormsUiComponentSupplier;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateField;
import io.vertigo.easyforms.impl.easyformsrunner.library.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldValidatorDefinitionProvider.FieldValidatorEnum;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.UiComponentDefinitionProvider.RadioUiComponent;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.UiComponentDefinitionProvider.TextFieldUiComponent;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.UiComponentDefinitionProvider.UiComponentEnum;

public class FieldTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldType> {

	public enum FieldTypeEnum implements EnumDefinition<EasyFormsFieldType, FieldValidatorEnum> {
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

		YES_NO(new YesNoFieldType()),

		CUSTOM_LIST_SELECT(new CustomListFieldType(UiComponentEnum.SELECT)),
		CUSTOM_LIST_RADIO(new CustomListFieldType(UiComponentEnum.RADIO)),
		CUSTOM_LIST_CHECKBOX(new CustomListFieldType(UiComponentEnum.CHECKBOX)),

		// internal use
		INTERNAL_RADIO_LAYOUT(new RadioLayoutFieldType()),
		INTERNAL_MAP(EasyFormsSmartTypes.EfFormData, UiComponentEnum.INTERNAL_MAP),
		;

		// ---

		private final String definitionName;
		private final IEasyFormsFieldTypeSupplier typeSupplier;

		private FieldTypeEnum(final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponent, ?> uiComponent) {
			this(new SimpleFieldType(smartType, uiComponent));
		}

		private FieldTypeEnum(final IEasyFormsFieldTypeSupplier typeSupplier) {
			definitionName = EasyFormsFieldType.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.typeSupplier = typeSupplier;
		}

		@Override
		public EasyFormsFieldType buildDefinition(final DefinitionSpace definitionSpace) {
			return typeSupplier.get(definitionName);
		}

		@Override
		public EasyFormsFieldType get() {
			return EasyFormsFieldType.resolve(definitionName);
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

	public static class SimpleFieldType implements IEasyFormsFieldTypeSupplier {
		private final EasyFormsSmartTypes smartType;
		private final EnumDefinition<EasyFormsUiComponent, ?> uiComponent;

		public SimpleFieldType(final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponent, ?> uiComponent) {
			this.smartType = smartType;
			this.uiComponent = uiComponent;
		}

		@Override
		public EasyFormsSmartTypes getSmartType() {
			return smartType;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponent, ?> getUiComponent() {
			return uiComponent;
		}
	}

	public static class AutocompleteFieldType implements IEasyFormsFieldTypeSupplier {
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
		public EnumDefinition<EasyFormsUiComponent, ?> getUiComponent() {
			return UiComponentEnum.TEXT_FIELD;
		}

		@Override
		public Map<String, Object> getUiParams() {
			return Map.of(TextFieldUiComponent.AUTOCOMPLETE, uiAutocompleteInputAttribute);
		}
	}

	public static class YesNoFieldType implements IEasyFormsFieldTypeSupplier {
		@Override
		public EasyFormsSmartTypes getSmartType() {
			return EasyFormsSmartTypes.EfBooleen;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponent, ?> getUiComponent() {
			return UiComponentEnum.RADIO;
		}

		@Override
		public Map<String, Object> getUiParams() {
			return Map.of(
					IEasyFormsUiComponentSupplier.LIST_SUPPLIER, IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME,
					IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME,
					(Serializable) List.of(
							new EasyFormsListItem("true", "#{EfTrue}"),
							new EasyFormsListItem("false", "#{EfFalse}")));
		}
	}

	public static class RadioLayoutFieldType implements IEasyFormsFieldTypeSupplier {
		@Override
		public EasyFormsSmartTypes getSmartType() {
			return EasyFormsSmartTypes.EfLabel;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponent, ?> getUiComponent() {
			return UiComponentEnum.RADIO;
		}

		@Override
		public Map<String, Object> getUiParams() {
			return Map.of(
					IEasyFormsUiComponentSupplier.LIST_SUPPLIER, IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME,
					IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME,
					(Serializable) List.of(
							new EasyFormsListItem("", "#{EfFtyCustomListRadio$radioLayoutVerticalLabel}"),
							new EasyFormsListItem("horizontal", "#{EfFtyCustomListRadio$radioLayoutHorizontalLabel}")));
		}

		@Override
		public Object getDefaultValue() {
			return ""; // Vertical
		}

	}

	public static class CustomListFieldType implements IEasyFormsFieldTypeSupplier {

		private final EnumDefinition<EasyFormsUiComponent, ?> uiComponent;

		public CustomListFieldType(final EnumDefinition<EasyFormsUiComponent, ?> uiComponent) {
			this.uiComponent = uiComponent;
		}

		@Override
		public EasyFormsSmartTypes getSmartType() {
			return EasyFormsSmartTypes.EfLabel;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponent, ?> getUiComponent() {
			return uiComponent;
		}

		@Override
		public Map<String, Object> getUiParams() {
			if (uiComponent == UiComponentEnum.RADIO) {
				return Map.of(IEasyFormsUiComponentSupplier.LIST_SUPPLIER, IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME,
						RadioUiComponent.LAYOUT, "horizontal");
			}
			return Map.of(IEasyFormsUiComponentSupplier.LIST_SUPPLIER, IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME);
		}

		@Override
		public List<EasyFormsTemplateField> getExposedComponentParams() {
			if (uiComponent == UiComponentEnum.RADIO) {
				return List.of(
						new EasyFormsTemplateField(RadioUiComponent.LAYOUT, FieldTypeEnum.INTERNAL_RADIO_LAYOUT)
								.withParameters(Map.of(RadioUiComponent.LAYOUT, "horizontal")),
						new EasyFormsTemplateField(IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.INTERNAL_MAP).withMandatory());
			}
			return List.of(new EasyFormsTemplateField(IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.INTERNAL_MAP).withMandatory());
		}

	}
}
