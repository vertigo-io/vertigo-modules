package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.util.Arrays;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldValidatorType;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;

public class FieldValidatorDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldValidatorType> {

	public enum FieldValidatorEnum implements EnumDefinition<EasyFormsFieldValidatorType, FieldValidatorEnum> {

		EMAIL_NOT_IN_BLACKLIST(20, FieldTypeEnum.EMAIL),
		GTE_13_ANS(10, FieldTypeEnum.BIRTH_DATE),
		LT_16_ANS(20, FieldTypeEnum.BIRTH_DATE),
		GTE_16_ANS(30, FieldTypeEnum.BIRTH_DATE),
		LT_18_ANS(40, FieldTypeEnum.BIRTH_DATE),
		GTE_18_ANS(60, FieldTypeEnum.BIRTH_DATE),
		TELEPHONE_FR(10, FieldTypeEnum.PHONE),
		TELEPHONE_MOBILE_SMS(20, FieldTypeEnum.PHONE),
		;

		private final String definitionName;
		private final int priorite;
		private final FieldTypeEnum[] fieldTypes;

		private FieldValidatorEnum(final int priorite, final FieldTypeEnum... fieldTypes) {
			definitionName = EasyFormsFieldValidatorType.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.priorite = priorite;
			this.fieldTypes = fieldTypes;
		}

		@Override
		public EasyFormsFieldValidatorType buildDefinition(final DefinitionSpace definitionSpace) {
			final var types = Arrays.stream(fieldTypes).map(FieldTypeEnum::get).toList();
			return EasyFormsFieldValidatorType.of(definitionName, priorite, types);
		}

		@Override
		public EasyFormsFieldValidatorType get() {
			return EasyFormsFieldValidatorType.resolve(definitionName);
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
