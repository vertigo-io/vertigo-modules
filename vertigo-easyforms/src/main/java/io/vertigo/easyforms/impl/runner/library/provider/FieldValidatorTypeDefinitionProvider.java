package io.vertigo.easyforms.impl.runner.library.provider;

import java.util.Arrays;
import java.util.Optional;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.easyforms.impl.runner.library.constraint.ConstraintAgeMaximum;
import io.vertigo.easyforms.impl.runner.library.constraint.ConstraintAgeMinimum;
import io.vertigo.easyforms.impl.runner.library.constraint.ConstraintEmailBlackList;
import io.vertigo.easyforms.impl.runner.library.constraint.ConstraintPhone;
import io.vertigo.easyforms.impl.runner.library.constraint.ConstraintPhone.ConstraintPhoneEnum;
import io.vertigo.easyforms.impl.runner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldValidatorTypeDefinition;

public class FieldValidatorTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldValidatorTypeDefinition> {

	public enum FieldValidatorEnum implements EnumDefinition<EasyFormsFieldValidatorTypeDefinition, FieldValidatorEnum> {

		EMAIL_NOT_IN_BLACKLIST(20, new ConstraintEmailBlackList(ConstraintEmailBlackList.DISPOSABLE, Optional.empty(), Optional.empty()), FieldTypeEnum.EMAIL),
		GTE_13_ANS(10, new ConstraintAgeMinimum("13", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		LT_16_ANS(20, new ConstraintAgeMaximum("15", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		GTE_16_ANS(30, new ConstraintAgeMinimum("16", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		LT_18_ANS(40, new ConstraintAgeMaximum("17", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		GTE_18_ANS(60, new ConstraintAgeMinimum("18", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		PHONE_FR(10, ConstraintPhone.ofEnum(ConstraintPhoneEnum.FR_METRO, Optional.empty(), Optional.empty()), FieldTypeEnum.PHONE),
		PHONE_FR_FIXE(10, ConstraintPhone.ofEnum(ConstraintPhoneEnum.FR_FIXE, Optional.empty(), Optional.empty()), FieldTypeEnum.PHONE),
		PHONE_FR_MOBILE(20, ConstraintPhone.ofEnum(ConstraintPhoneEnum.FR_MOBILE, Optional.empty(), Optional.empty()), FieldTypeEnum.PHONE),
		;

		private final String definitionName;
		private final int priority;
		private final Constraint constraint;
		private final FieldTypeEnum[] fieldTypes;

		private FieldValidatorEnum(final int priority, final Constraint constraint, final FieldTypeEnum... fieldTypes) {
			definitionName = EasyFormsFieldValidatorTypeDefinition.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.priority = priority;
			this.constraint = constraint;
			this.fieldTypes = fieldTypes;
		}

		@Override
		public EasyFormsFieldValidatorTypeDefinition buildDefinition(final DefinitionSpace definitionSpace) {
			final var types = Arrays.stream(fieldTypes).map(FieldTypeEnum::get).toList();
			return EasyFormsFieldValidatorTypeDefinition.of(definitionName, priority, types, constraint);
		}

		@Override
		public EasyFormsFieldValidatorTypeDefinition get() {
			return EasyFormsFieldValidatorTypeDefinition.resolve(definitionName);
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
