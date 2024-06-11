package io.vertigo.easyforms.impl.runner.pack.provider.fieldtype;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.easyforms.impl.runner.Resources;
import io.vertigo.easyforms.impl.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.impl.runner.pack.constraint.EasyFormsConstraint;
import io.vertigo.easyforms.impl.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.impl.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;
import io.vertigo.easyforms.runner.EasyFormsRunnerManager;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;

public class InternalMapFieldType implements IEasyFormsFieldTypeDefinitionSupplier {

	@Override
	public String getCategory() {
		return null; // no category means "hidden"
	}

	@Override
	public EasyFormsSmartTypes getSmartType() {
		return EasyFormsSmartTypes.EfIMapData;
	}

	@Override
	public EnumDefinition<EasyFormsUiComponentDefinition, ?> getUiComponent() {
		return UiComponentEnum.INTERNAL_MAP;
	}

	@Override
	public Function<EasyFormsTemplateItemField, List<Constraint>> getConstraintsProvider() {
		return field -> List.of(
				new mandatoryInternalFieldsConstraint(),
				new uniqueValueConstraint());
	}

	private static final class mandatoryInternalFieldsConstraint implements EasyFormsConstraint<Boolean, List<Map<String, Object>>> {
		private final String defaultLang;

		public mandatoryInternalFieldsConstraint() {
			final var easyFormsRunnerManager = Node.getNode().getComponentSpace().resolve(EasyFormsRunnerManager.class);
			defaultLang = easyFormsRunnerManager.getSupportedLang().get(0);
		}

		@Override
		public boolean checkConstraint(final List<Map<String, Object>> value) {
			return value.stream()
					.allMatch(this::checkValues);
		}

		private boolean checkValues(final Map<String, Object> value) {
			return !StringUtil.isBlank((String) value.get("value"))
					&& !StringUtil.isBlank(((Map<String, String>) value.get("label")).get(defaultLang));
		}

		@Override
		public LocaleMessageText getErrorMessage() {
			return LocaleMessageText.of(Resources.EfIMapMandatory);
		}
	}

	private static final class uniqueValueConstraint implements EasyFormsConstraint<Boolean, List<Map<String, Object>>> {

		@Override
		public boolean checkConstraint(final List<Map<String, Object>> value) {
			final var distinctValuesCount = value.stream()
					.map(m -> m.get("value"))
					.distinct()
					.count();

			return distinctValuesCount == value.size();
		}

		@Override
		public LocaleMessageText getErrorMessage() {
			return LocaleMessageText.of(Resources.EfIMapDuplicateValue);
		}
	}

}
