package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.util.Arrays;
import java.util.stream.Collectors;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider.EnumDefinition;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldConstraint;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;

public class FieldConstraintDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldConstraint> {

	public enum FieldConstraintEnum implements EnumDefinition<EasyFormsFieldConstraint, FieldConstraintEnum> {

		UNIQUE("Unique pour cette démarche", 5, FieldTypeEnum.EMAIL, FieldTypeEnum.TELEPHONE, FieldTypeEnum.VISA),
		EMAIL_NOT_IN_BLACKLIST("Email non jetable", 20, FieldTypeEnum.EMAIL),
		GTE_13_ANS(">= 13 ans révolus", 10, FieldTypeEnum.DATE_NAISSANCE),
		LT_16_ANS("< 16 ans révolus", 20, FieldTypeEnum.DATE_NAISSANCE),
		GTE_16_ANS(">= 16 ans révolus", 30, FieldTypeEnum.DATE_NAISSANCE),
		LT_18_ANS("< 18 ans révolus", 40, FieldTypeEnum.DATE_NAISSANCE),
		LTE_18_ANS("<= 18 ans révolus", 50, FieldTypeEnum.DATE_NAISSANCE),
		GTE_18_ANS(">= 18 ans révolus", 60, FieldTypeEnum.DATE_NAISSANCE),
		TELEPHONE_FR("Numéro en france", 10, FieldTypeEnum.TELEPHONE),
		TELEPHONE_MOBILE_SMS("Numéro mobile en france ou en outre-mer", 20, FieldTypeEnum.TELEPHONE),
		;

		private final String code;
		private final String label;
		private final int priorite;
		private final FieldTypeEnum[] fieldTypes;

		private FieldConstraintEnum(final String label, final int priorite, final FieldTypeEnum... fieldTypes) {
			code = StringUtil.constToUpperCamelCase(name());
			this.label = label;
			this.priorite = priorite;
			this.fieldTypes = fieldTypes;
		}

		@Override
		public EasyFormsFieldConstraint buildDefinition(final DefinitionSpace definitionSpace) {
			final var types = Arrays.stream(fieldTypes).map(FieldTypeEnum::get).collect(Collectors.toUnmodifiableSet());
			return EasyFormsFieldConstraint.of(code, label, priorite, types);
		}

		@Override
		public EasyFormsFieldConstraint get() {
			return EasyFormsFieldConstraint.resolve(code);
		}
	}

	@Override
	public Class<FieldConstraintEnum> getEnumClass() {
		return FieldConstraintEnum.class;
	}

}
