package io.vertigo.easyforms.impl.runner.library.provider;

import java.util.List;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.impl.runner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateField;

public class UiComponentDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsUiComponentDefinition> {

	public enum UiComponentEnum implements EnumDefinition<EasyFormsUiComponentDefinition, UiComponentEnum> {

		TEXT_FIELD(new TextFieldUiComponent()), //
		TEXT_AREA, //

		CHECKBOX(new RadioCheckUiComponent()), //
		RADIO(new RadioCheckUiComponent()), //

		DATE, //
		DATE_TIME, //

		SLIDER, //

		SELECT(new SelectUiComponent()), //

		// Internal use
		INTERNAL_MAP,//
		;

		// ---

		private final String definitionName;
		private final IEasyFormsUiComponentDefinitionSupplier componentSupplier;

		UiComponentEnum() {
			this(IEasyFormsUiComponentDefinitionSupplier.NO_PARAM);
		}

		UiComponentEnum(final IEasyFormsUiComponentDefinitionSupplier componentSupplier) {
			definitionName = EasyFormsUiComponentDefinition.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.componentSupplier = componentSupplier;
		}

		@Override
		public EasyFormsUiComponentDefinition buildDefinition(final DefinitionSpace definitionSpace) {
			return componentSupplier.get(definitionName);
		}

		@Override
		public EasyFormsUiComponentDefinition get() {
			return EasyFormsUiComponentDefinition.resolve(definitionName);
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

	public static class TextFieldUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

		public static final String AUTOCOMPLETE = "textFieldAutocomplete";

		@Override
		public List<EasyFormsTemplateField> getUiComponentParams() {
			return List.of(
					new EasyFormsTemplateField(AUTOCOMPLETE, FieldTypeEnum.LABEL));
		}

	}

	public static class RadioCheckUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

		public static final String LAYOUT = "layout";

		@Override
		public List<EasyFormsTemplateField> getUiComponentParams() {
			return List.of(
					IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_FIELD_PARAM,
					new EasyFormsTemplateField(LAYOUT, FieldTypeEnum.INTERNAL_LAYOUT));
		}

	}

	public static class SelectUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

		public static final String SEARCHABLE = "selectSearchable";

		@Override
		public List<EasyFormsTemplateField> getUiComponentParams() {
			return List.of(
					IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_FIELD_PARAM,
					new EasyFormsTemplateField(SEARCHABLE, FieldTypeEnum.YES_NO));
		}

	}

}
