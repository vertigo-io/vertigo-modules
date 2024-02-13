package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.util.List;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsUiComponent;
import io.vertigo.easyforms.easyformsrunner.model.definitions.IEasyFormsUiComponentSupplier;
import io.vertigo.easyforms.easyformsrunner.model.template.EasyFormsTemplateField;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;

public class UiComponentDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsUiComponent> {

	public enum UiComponentEnum implements EnumDefinition<EasyFormsUiComponent, UiComponentEnum> {

		TEXT_FIELD(new TextFieldUiComponent()), //
		TEXT_AREA, //

		CHECKBOX, //
		RADIO(new RadioUiComponent()), //

		DATE, //
		DATE_TIME, //

		SLIDER, //

		SELECT(new SelectUiComponent()), //

		// Internal use
		INTERNAL_MAP,//
		;

		// ---

		private final String definitionName;
		private final IEasyFormsUiComponentSupplier componentSupplier;

		UiComponentEnum() {
			this(IEasyFormsUiComponentSupplier.NO_PARAM);
		}

		UiComponentEnum(final IEasyFormsUiComponentSupplier componentSupplier) {
			definitionName = EasyFormsUiComponent.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.componentSupplier = componentSupplier;
		}

		@Override
		public EasyFormsUiComponent buildDefinition(final DefinitionSpace definitionSpace) {
			return componentSupplier.get(definitionName);
		}

		@Override
		public EasyFormsUiComponent get() {
			return EasyFormsUiComponent.resolve(definitionName);
		}

		@Override
		public String getDefinitionName() {
			return definitionName;
		}
	}

	@Override
	public Class<UiComponentEnum> getEnumClass() {
		return UiComponentEnum.class;
	}

	public static class TextFieldUiComponent implements IEasyFormsUiComponentSupplier {

		public static final String AUTOCOMPLETE = "textFieldAutocomplete";

		@Override
		public List<EasyFormsTemplateField> getUiComponentParams() {
			return List.of(
					new EasyFormsTemplateField(AUTOCOMPLETE, FieldTypeEnum.LABEL));
		}

	}

	public static class RadioUiComponent implements IEasyFormsUiComponentSupplier {

		public static final String LAYOUT = "radioLayout";

		@Override
		public List<EasyFormsTemplateField> getUiComponentParams() {
			return List.of(
					IEasyFormsUiComponentSupplier.LIST_SUPPLIER_FIELD_PARAM,
					new EasyFormsTemplateField(LAYOUT, FieldTypeEnum.INTERNAL_RADIO_LAYOUT));
		}

	}

	public static class SelectUiComponent implements IEasyFormsUiComponentSupplier {

		public static final String SEARCHABLE = "selectMasterDataSearchable";

		@Override
		public List<EasyFormsTemplateField> getUiComponentParams() {
			return List.of(
					IEasyFormsUiComponentSupplier.LIST_SUPPLIER_FIELD_PARAM,
					new EasyFormsTemplateField(SEARCHABLE, FieldTypeEnum.YES_NO));
		}

	}

}
