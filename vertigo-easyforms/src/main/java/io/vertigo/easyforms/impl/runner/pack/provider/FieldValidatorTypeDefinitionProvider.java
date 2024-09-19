/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.easyforms.impl.runner.pack.provider;

import java.util.Arrays;
import java.util.Optional;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintAgeMaximum;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintAgeMinimum;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintEmailBlackList;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintLocalDateMaximum;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintLocalDateMinimum;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintPhone;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintPhone.ConstraintPhoneEnum;
import io.vertigo.easyforms.impl.runner.pack.provider.FieldTypeDefinitionProvider.FieldTypeEnum;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldValidatorTypeDefinition;

public final class FieldValidatorTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldValidatorTypeDefinition> {

	public enum FieldValidatorEnum implements EnumDefinition<EasyFormsFieldValidatorTypeDefinition, FieldValidatorEnum> {

		EMAIL_NOT_IN_BLACKLIST(20, new ConstraintEmailBlackList(ConstraintEmailBlackList.DISPOSABLE, Optional.empty(), Optional.empty()), FieldTypeEnum.EMAIL),
		GTE_13_ANS(10, new ConstraintAgeMinimum("13", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		LT_16_ANS(20, new ConstraintAgeMaximum("15", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		GTE_16_ANS(30, new ConstraintAgeMinimum("16", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		LT_18_ANS(40, new ConstraintAgeMaximum("17", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		GTE_18_ANS(60, new ConstraintAgeMinimum("18", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		IN_FUTURE(10, new ConstraintLocalDateMinimum("now", Optional.empty(), Optional.of("EfDateInFutureError")), FieldTypeEnum.DATE),
		IN_PAST(20, new ConstraintLocalDateMaximum("now", Optional.empty(), Optional.of("EfDateInPastError")), FieldTypeEnum.DATE),
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
