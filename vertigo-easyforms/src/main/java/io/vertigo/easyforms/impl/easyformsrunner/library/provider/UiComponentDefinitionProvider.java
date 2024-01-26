package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.util.List;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsUiComponent;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsUiComponent.UiComponentParam;
import io.vertigo.easyforms.easyformsrunner.model.IEasyFormsUiComponentSupplier;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;

public class UiComponentDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsUiComponent> {

	public enum UiComponentEnum implements EnumDefinition<EasyFormsUiComponent, UiComponentEnum> {

		TEXT_FIELD(new TextFieldUiComponent()),
		TEXT_AREA,

		CHECKBOX,
		RADIO(new RadioUiComponent()),

		DATE,
		DATE_TIME,

		SLIDER,

		SELECT(new SelectUiComponent()),

		// Internals
		I_MAP,
		;

		// ---

		private final String definitionName;
		private final IEasyFormsUiComponentSupplier componentSupplier;

		private <T> UiComponentEnum() {
			this(IEasyFormsUiComponentSupplier.NO_PARAM);
		}

		private <T> UiComponentEnum(final IEasyFormsUiComponentSupplier componentSupplier) {
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
		public List<UiComponentParam> getUiComponentParams() {
			return List.of(
					new UiComponentParam(AUTOCOMPLETE, FieldTypeEnum.LABEL, null, false));
		}

	}

	public static class RadioUiComponent implements IEasyFormsUiComponentSupplier {

		public static final String LAYOUT = "radioLayout";

		@Override
		public List<UiComponentParam> getUiComponentParams() {
			return List.of(
					IEasyFormsUiComponentSupplier.LIST_SUPPLIER_FIELD_PARAM,
					new UiComponentParam(LAYOUT, FieldTypeEnum.I_RADIO_LAYOUT, null, false));
		}

	}

	public static class SelectUiComponent implements IEasyFormsUiComponentSupplier {

		public static final String SEARCHABLE = "selectMasterDataSearchable";

		@Override
		public List<UiComponentParam> getUiComponentParams() {
			return List.of(
					IEasyFormsUiComponentSupplier.LIST_SUPPLIER_FIELD_PARAM,
					new UiComponentParam(SEARCHABLE, FieldTypeEnum.YES_NO, null, false));
		}

	}

}
