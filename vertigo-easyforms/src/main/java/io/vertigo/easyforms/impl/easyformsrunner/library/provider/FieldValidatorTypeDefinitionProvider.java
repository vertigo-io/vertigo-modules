package io.vertigo.easyforms.impl.easyformsrunner.library.provider;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import io.vertigo.basics.constraint.ConstraintRegex;
import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.definitions.Constraint;
import io.vertigo.easyforms.easyformsrunner.model.definitions.EasyFormsFieldValidatorType;
import io.vertigo.easyforms.impl.easyformsrunner.library.constraint.ConstraintAgeMaximum;
import io.vertigo.easyforms.impl.easyformsrunner.library.constraint.ConstraintAgeMinimum;
import io.vertigo.easyforms.impl.easyformsrunner.library.constraint.ConstraintEmailBlackList;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldTypeDefinitionProvider.FieldTypeEnum;

public class FieldValidatorTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldValidatorType> {

	//vérifie qu'on a +33 ou 0033 ou 0 + 9 chiffres . peut avoir des () des . ou des espaces. doit finir par 2 chiffres consécutifs
	private static final String PREDICATE_TELEPHONE_FR = "^((((?:\\+|00)33\\W*)|0)[1-9](?:\\W*\\d){7}\\d)$";

	//FR mobile : +33|0 puis 6 ou 7
	//Les numéros mobiles outre mer:
	//+590 690 (Guadeloupe)
	//+594 694 (Guyane)
	//+596 696 (Martinique)
	//+262 639 (Mayotte)
	//+262 692 (La Réunion)
	//+262 693 (La Réunion)
	//+508 (Saint-Pierre-et-Miquelon)
	//+689 (Polynésie française)
	//+681 (Wallis et Futuna)
	//+687 (Nouvelle Calédonie)
	private static final String PREDICATE_TELEPHONE_SMS = "^(?:(((?:(?:\\+|00)33\\W*)|0)[67](?:\\W*\\d){7}\\d)"
			+ "|(((?:\\+|00)590\\W*)6\\W*9\\W*0(?:\\W*\\d){5}\\d)"
			+ "|(((?:\\+|00)594\\W*)6\\W*9\\W*4(?:\\W*\\d){5}\\d)"
			+ "|(((?:\\+|00)596\\W*)6\\W*9\\W*6(?:\\W*\\d){5}\\d)"
			+ "|(((?:\\+|00)262\\W*)6\\W*(?:3\\W*9|9\\W*2|9\\W*3)(?:\\W*\\d){5}\\d)"
			+ "|(((?:\\+|00)(?:508|689|681|687)\\W*)[0-9](?:\\W*\\d){7}\\d))$";

	private static final Set<String> EMAIL_DOMAIN_BLACK_LIST = Set.of("yopmail.com", "yopmail.net", "mailinator.com", "jetable.org", "trashmail.com", "throwawaymail.com", "emailondeck.com",
			"emailfake.com");

	public enum FieldValidatorEnum implements EnumDefinition<EasyFormsFieldValidatorType, FieldValidatorEnum> {

		EMAIL_NOT_IN_BLACKLIST(20, new ConstraintEmailBlackList(String.join(",", EMAIL_DOMAIN_BLACK_LIST), Optional.empty(), Optional.empty()), FieldTypeEnum.EMAIL),
		GTE_13_ANS(10, new ConstraintAgeMinimum("13", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		LT_16_ANS(20, new ConstraintAgeMaximum("15", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		GTE_16_ANS(30, new ConstraintAgeMinimum("16", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		LT_18_ANS(40, new ConstraintAgeMaximum("17", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		GTE_18_ANS(60, new ConstraintAgeMinimum("18", Optional.empty(), Optional.empty()), FieldTypeEnum.BIRTH_DATE),
		TELEPHONE_FR(10, new ConstraintRegex(PREDICATE_TELEPHONE_FR, Optional.empty(), Optional.empty()), FieldTypeEnum.PHONE),
		TELEPHONE_MOBILE_SMS(20, new ConstraintRegex(PREDICATE_TELEPHONE_SMS, Optional.empty(), Optional.empty()), FieldTypeEnum.PHONE),
		;

		private final String definitionName;
		private final int priority;
		private final Constraint constraint;
		private final FieldTypeEnum[] fieldTypes;

		private FieldValidatorEnum(final int priority, final Constraint constraint, final FieldTypeEnum... fieldTypes) {
			definitionName = EasyFormsFieldValidatorType.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.priority = priority;
			this.constraint = constraint;
			this.fieldTypes = fieldTypes;
		}

		@Override
		public EasyFormsFieldValidatorType buildDefinition(final DefinitionSpace definitionSpace) {
			final var types = Arrays.stream(fieldTypes).map(FieldTypeEnum::get).toList();
			return EasyFormsFieldValidatorType.of(definitionName, priority, types, constraint);
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
