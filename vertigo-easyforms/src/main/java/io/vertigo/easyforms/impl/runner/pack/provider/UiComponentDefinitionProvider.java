package io.vertigo.easyforms.impl.runner.pack.provider;

import java.util.List;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.DtProperty;
import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public final class UiComponentDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsUiComponentDefinition> {

	public enum UiComponentEnum implements EnumDefinition<EasyFormsUiComponentDefinition, UiComponentEnum> {

		TEXT_FIELD(new TextFieldUiComponent()), //
		TEXT_AREA(new TextAreaUiComponent()), //

		CHECKBOX(new RadioCheckUiComponent()), //
		RADIO(new RadioCheckUiComponent()), //

		NUMBER, //

		DATE, //
		DATE_TIME, //

		SLIDER, //

		SELECT(new SelectUiComponent()), //

		FILE(new FileUiComponent()), //

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
		public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
			return List.of(
					new EasyFormsTemplateItemField(AUTOCOMPLETE, FieldTypeEnum.CUSTOM_LIST_RADIO));
		}

	}

	public static class TextAreaUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

		public static final String AUTOGROW = "autogrow";

		@Override
		public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
			return List.of(
					new EasyFormsTemplateItemField(DtProperty.MAX_LENGTH.getName(), FieldTypeEnum.COUNT_STRICT),
					new EasyFormsTemplateItemField(AUTOGROW, FieldTypeEnum.YES_NO));
		}

	}

	public static class RadioCheckUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

		public static final String LAYOUT = "layout";

		@Override
		public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
			return List.of(
					IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_FIELD_PARAM,
					new EasyFormsTemplateItemField(LAYOUT, FieldTypeEnum.INTERNAL_LAYOUT));
		}

	}

	public static class SelectUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

		public static final String SEARCHABLE = "selectSearchable";

		@Override
		public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
			return List.of(
					IEasyFormsUiComponentDefinitionSupplier.LIST_SUPPLIER_FIELD_PARAM,
					new EasyFormsTemplateItemField(SEARCHABLE, FieldTypeEnum.YES_NO));
		}

	}

	public static class FileUiComponent implements IEasyFormsUiComponentDefinitionSupplier {

		public static final String MAX_SIZE = "maxSize";
		public static final String MAX_FILE_SIZE = "maxFileSize";
		public static final String ACCEPT = "accept";

		@Override
		public List<AbstractEasyFormsTemplateItem> getUiComponentParams() {
			return List.of(
					new EasyFormsTemplateItemField(MAX_SIZE, FieldTypeEnum.COUNT_STRICT),
					new EasyFormsTemplateItemField(ACCEPT, FieldTypeEnum.INTERNAL_EXTENSIONS));
		}

	}

}
