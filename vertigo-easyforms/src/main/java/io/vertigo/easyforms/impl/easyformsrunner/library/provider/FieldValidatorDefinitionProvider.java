package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.util.Arrays;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldValidator;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;

public class FieldValidatorDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldValidator> {

	public enum FieldValidatorEnum implements EnumDefinition<EasyFormsFieldValidator, FieldValidatorEnum> {

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

		private FieldValidatorEnum(final String label, final int priorite, final FieldTypeEnum... fieldTypes) {
			code = StringUtil.constToUpperCamelCase(name());
			this.label = label;
			this.priorite = priorite;
			this.fieldTypes = fieldTypes;
		}

		@Override
		public EasyFormsFieldValidator buildDefinition(final DefinitionSpace definitionSpace) {
			final var types = Arrays.stream(fieldTypes).map(FieldTypeEnum::get).toList();
			return EasyFormsFieldValidator.of(code, label, priorite, types);
		}

		@Override
		public EasyFormsFieldValidator get() {
			return EasyFormsFieldValidator.resolve(code);
		}
	}

	@Override
	public Class<FieldValidatorEnum> getEnumClass() {
		return FieldValidatorEnum.class;
	}

}
