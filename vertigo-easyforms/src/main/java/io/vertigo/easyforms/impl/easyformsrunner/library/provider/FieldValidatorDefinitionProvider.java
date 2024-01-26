package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.util.Arrays;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldValidator;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;

public class FieldValidatorDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldValidator> {

	public enum FieldValidatorEnum implements EnumDefinition<EasyFormsFieldValidator, FieldValidatorEnum> {

		EMAIL_NOT_IN_BLACKLIST(20, FieldTypeEnum.EMAIL),
		GTE_13_ANS(10, FieldTypeEnum.DATE_NAISSANCE),
		LT_16_ANS(20, FieldTypeEnum.DATE_NAISSANCE),
		GTE_16_ANS(30, FieldTypeEnum.DATE_NAISSANCE),
		LT_18_ANS(40, FieldTypeEnum.DATE_NAISSANCE),
		GTE_18_ANS(60, FieldTypeEnum.DATE_NAISSANCE),
		TELEPHONE_FR(10, FieldTypeEnum.TELEPHONE),
		TELEPHONE_MOBILE_SMS(20, FieldTypeEnum.TELEPHONE),
		;

		private final String definitionName;
		private final int priorite;
		private final FieldTypeEnum[] fieldTypes;

		private FieldValidatorEnum(final int priorite, final FieldTypeEnum... fieldTypes) {
			definitionName = EasyFormsFieldValidator.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.priorite = priorite;
			this.fieldTypes = fieldTypes;
		}

		@Override
		public EasyFormsFieldValidator buildDefinition(final DefinitionSpace definitionSpace) {
			final var types = Arrays.stream(fieldTypes).map(FieldTypeEnum::get).toList();
			return EasyFormsFieldValidator.of(definitionName, priorite, types);
		}

		@Override
		public EasyFormsFieldValidator get() {
			return EasyFormsFieldValidator.resolve(definitionName);
		}

		@Override
		public String getDefinitionName() {
			return definitionName;
		}
	}

	@Override
	public Class<FieldValidatorEnum> getEnumClass() {
		return FieldValidatorEnum.class;
	}

}
