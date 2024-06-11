package io.vertigo.easyforms.impl.runner.pack.provider;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.FileUiComponent;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.RadioCheckUiComponent;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.SelectUiComponent;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.TextAreaUiComponent;
import io.vertigo.easyforms.impl.runner.pack.provider.uicomponent.TextFieldUiComponent;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsUiComponentDefinitionSupplier;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;

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

}
