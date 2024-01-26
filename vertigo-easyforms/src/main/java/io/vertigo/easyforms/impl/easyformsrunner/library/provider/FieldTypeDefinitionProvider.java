package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsUiComponent;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsUiComponent.UiComponentParam;
import io.vertigo.easyforms.easyformsrunner.model.IEasyFormsFieldTypeSupplier;
import io.vertigo.easyforms.easyformsrunner.model.IEasyFormsUiComponentSupplier;
import io.vertigo.easyforms.impl.easyformsrunner.library.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldValidatorDefinitionProvider.FieldValidatorEnum;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.UiComponentDefinitionProvider.RadioUiComponent;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.UiComponentDefinitionProvider.TextFieldUiComponent;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.UiComponentDefinitionProvider.UiComponentEnum;

public class FieldTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldType> {

	public enum FieldTypeEnum implements EnumDefinition<EasyFormsFieldType, FieldValidatorEnum> {
		LABEL(EasyFormsSmartTypes.EfLabel, UiComponentEnum.TEXT_FIELD),

		NOM(new AutocompleteFieldType(EasyFormsSmartTypes.EfNom, "family-name")),

		PRENOM(new AutocompleteFieldType(EasyFormsSmartTypes.EfPrenom, "given-name")),

		EMAIL(new AutocompleteFieldType(EasyFormsSmartTypes.EfEmail, "email")),

		DATE(EasyFormsSmartTypes.EfDate, UiComponentEnum.DATE),

		DATE_NAISSANCE(EasyFormsSmartTypes.EfDatePassee, UiComponentEnum.DATE
		// , Map.of(TextFieldUiComponent.AUTOCOMPLETE, "bday") // TODO check on input if it is used
		),

		TELEPHONE(new AutocompleteFieldType(EasyFormsSmartTypes.EfTelephone, "tel")),

		VISA(EasyFormsSmartTypes.EfVisa, UiComponentEnum.TEXT_FIELD),

		NATIONALITE(EasyFormsSmartTypes.EfNationalite, UiComponentEnum.TEXT_FIELD),

		CODE_POSTAL(new AutocompleteFieldType(EasyFormsSmartTypes.EfCodePostal, "postal-code")),

		YES_NO(new YesNoFieldType()),

		CUSTOM_LIST_SELECT(new CustomListFieldType(UiComponentEnum.SELECT)),
		CUSTOM_LIST_RADIO(new CustomListFieldType(UiComponentEnum.RADIO)),
		CUSTOM_LIST_CHECKBOX(new CustomListFieldType(UiComponentEnum.CHECKBOX)),

		// internals
		I_RADIO_LAYOUT(new RadioLayoutFieldType()),
		I_MAP(EasyFormsSmartTypes.EfFormData, UiComponentEnum.I_MAP),
		;

		// ---

		private final String definitionName;
		private final IEasyFormsFieldTypeSupplier typeSupplier;

		private FieldTypeEnum(final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponent, ?> uiComponent) {
			this(new IEasyFormsFieldTypeSupplier() {

				@Override
				public EasyFormsSmartTypes getSmartType() {
					return smartType;
				}

				@Override
				public EnumDefinition<EasyFormsUiComponent, ?> getUiComponent() {
					return uiComponent;
				}

			});
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
		public Map<String, Serializable> getUiParams() {
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
		public Map<String, Serializable> getUiParams() {
			return Map.of(IEasyFormsUiComponentSupplier.LIST_SUPPLIER,
					(Serializable) Map.of(
							true, "#{TRUE}",
							false, "#{FALSE}"));
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
		public Map<String, Serializable> getUiParams() {
			return Map.of(IEasyFormsUiComponentSupplier.LIST_SUPPLIER,
					(Serializable) Map.of(
							"Horizontal", "",
							"Vertical", "vertical"));
		}
	}

	public static class CustomListFieldType implements IEasyFormsFieldTypeSupplier {

		private final EnumDefinition<EasyFormsUiComponent, ?> uiComponent;

		public CustomListFieldType(final EnumDefinition<EasyFormsUiComponent, ?> uiComponent) {
			this.uiComponent = uiComponent;
		}

		@Override
		public EasyFormsSmartTypes getSmartType() {
			return EasyFormsSmartTypes.EfCode;
		}

		@Override
		public EnumDefinition<EasyFormsUiComponent, ?> getUiComponent() {
			return uiComponent;
		}

		@Override
		public Map<String, Serializable> getUiParams() {
			if (uiComponent == UiComponentEnum.RADIO) {
				return Map.of(IEasyFormsUiComponentSupplier.LIST_SUPPLIER, IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME,
						RadioUiComponent.LAYOUT, "vertical");
			}
			return Map.of(IEasyFormsUiComponentSupplier.LIST_SUPPLIER, IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME);
		}

		@Override
		public List<UiComponentParam> getUiComponentParams() {
			return List.of(new UiComponentParam(IEasyFormsUiComponentSupplier.CUSTOM_LIST_ARG_NAME, FieldTypeEnum.I_MAP, null, true));
		}

	}
}
