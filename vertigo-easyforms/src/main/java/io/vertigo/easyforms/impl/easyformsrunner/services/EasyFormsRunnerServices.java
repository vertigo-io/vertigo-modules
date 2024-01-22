package io.vertigo.easyforms.impl.easyformsrunner.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.analytics.AnalyticsManager;
import io.vertigo.core.lang.Cardinality;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.smarttype.SmarttypeResources;
import io.vertigo.datamodel.smarttype.definitions.SmartTypeDefinition;
import io.vertigo.datamodel.structure.definitions.ConstraintException;
import io.vertigo.datamodel.structure.definitions.FormatterException;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsData;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsFieldType;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate;
import io.vertigo.easyforms.easyformsrunner.model.EasyFormsTemplate.Field;
import io.vertigo.easyforms.impl.easyformsrunner.library.i18n.MetaFormulaireResources;
import io.vertigo.easyforms.impl.easyformsrunner.library.provider.FieldConstraintDefinitionProvider.FieldConstraintEnum;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Transactional
public class EasyFormsRunnerServices implements Component {

	private static final String FORMULAIRE_PREFIX = "formulaire_";
	private static final String ERREUR_CONTROLE_FORMULAIRE_MEASURE = "erreurControleFormulaire";

	private static final int AGE_13_ANS = 13;
	private static final int AGE_16_ANS = 16;
	private static final int AGE_MAJORITE = 18;

	//vérifie qu'on a +33 ou 0033 ou 0 + 9 chiffres . peut avoir des () des . ou des espaces. doit finir par 2 chiffres consécutifs
	public static final Predicate<String> PREDICATE_TELEPHONE_FR = Pattern.compile("^((((?:\\+|00)33\\W*)|0)[1-9](?:\\W*\\d){7}\\d)$").asMatchPredicate();

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
	public static final Predicate<String> PREDICATE_TELEPHONE_SMS = Pattern.compile(
			"^(?:(((?:(?:\\+|00)33\\W*)|0)[67](?:\\W*\\d){7}\\d)"
					+ "|(((?:\\+|00)590\\W*)6\\W*9\\W*0(?:\\W*\\d){5}\\d)"
					+ "|(((?:\\+|00)594\\W*)6\\W*9\\W*4(?:\\W*\\d){5}\\d)"
					+ "|(((?:\\+|00)596\\W*)6\\W*9\\W*6(?:\\W*\\d){5}\\d)"
					+ "|(((?:\\+|00)262\\W*)6\\W*(?:3\\W*9|9\\W*2|9\\W*3)(?:\\W*\\d){5}\\d)"
					+ "|(((?:\\+|00)(?:508|689|681|687)\\W*)[0-9](?:\\W*\\d){7}\\d))$")
			.asMatchPredicate();

	private static final Set<String> EMAIL_DOMAIN_BLACK_LIST = Set.of("yopmail.com", "yopmail.net", "mailinator.com", "jetable.org", "trashmail.com", "throwawaymail.com", "emailondeck.com",
			"emailfake.com");

	@Inject
	private SmartTypeManager smartTypeManager;
	@Inject
	private AnalyticsManager analyticsManager;

	public void checkFormulaire(final Entity formulaireOwner, final EasyFormsData formulaire, final EasyFormsTemplate modeleFormulaire, final UiMessageStack uiMessageStack) {
		final Set<String> champsAutorises = modeleFormulaire.getFields().stream().map(Field::getFieldCode).collect(Collectors.toSet());
		for (final String champFormulaire : formulaire.keySet()) {
			if (!champsAutorises.contains(champFormulaire)) {
				uiMessageStack.error("Champ non autorisé ", formulaireOwner, FORMULAIRE_PREFIX + champFormulaire);
			}
		}
		//---
		for (final Field champ : modeleFormulaire.getFields()) {
			checkChampFormulaire(champ, formulaire, formulaireOwner, uiMessageStack);
		}

		//---
		if (uiMessageStack.hasErrors()) {
			throw new ValidationUserException();
		}
	}

	private void checkChampFormulaire(final Field champ, final EasyFormsData formulaire, final Entity formulaireOwner, final UiMessageStack uiMessageStack) {
		final var typeChamp = EasyFormsFieldType.resolve(champ.getFieldType());
		final var smartTypeDefinition = getSmartTypeByNom(typeChamp.getSmartType());
		final var inputValue = formulaire.get(champ.getFieldCode());
		if (inputValue == null || inputValue.isBlank()) {
			// when null, the only check is for mandatory
			if (champ.isMandatory()) {
				uiMessageStack.error(LocaleMessageText.of(SmarttypeResources.SMARTTYPE_MISSING_VALUE).getDisplay(), formulaireOwner, FORMULAIRE_PREFIX + champ.getFieldCode());
				analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
						.incMeasure(ERREUR_CONTROLE_FORMULAIRE_MEASURE, 1)
						.setTag("controle", "Obligatoire")
						.setTag("champ", champ.getLabel()));
			}
		} else {
			try {
				final var typedValue = smartTypeManager.stringToValue(smartTypeDefinition, inputValue);
				final var formatedValue = smartTypeManager.valueToString(smartTypeDefinition, typedValue);
				formulaire.put(champ.getFieldCode(), formatedValue);

				smartTypeManager.validate(smartTypeDefinition, Cardinality.OPTIONAL_OR_NULLABLE, typedValue);
				checkFieldConstraints(champ, typedValue, formulaireOwner, uiMessageStack);
			} catch (final FormatterException e) {
				uiMessageStack.error(e.getMessageText().getDisplay(), formulaireOwner, FORMULAIRE_PREFIX + champ.getFieldCode());
				analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
						.incMeasure(ERREUR_CONTROLE_FORMULAIRE_MEASURE, 1)
						.setTag("controle", "Formatter")
						.setTag("smartType", smartTypeDefinition.id().shortName())
						.setTag("champ", champ.getLabel()));
			} catch (final ConstraintException e) {
				uiMessageStack.error(e.getMessageText().getDisplay(), formulaireOwner, FORMULAIRE_PREFIX + champ.getFieldCode());
				analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
						.incMeasure(ERREUR_CONTROLE_FORMULAIRE_MEASURE, 1)
						.setTag("controle", "Constraints")
						.setTag("smartType", smartTypeDefinition.id().shortName())
						.setTag("champ", champ.getLabel()));
			}
		}
	}

	private void checkFieldConstraints(final Field champ, final Object typedValue, final Entity formulaireOwner, final UiMessageStack uiMessageStack) {
		for (final String fieldConstraint : champ.getFieldConstraints()) {
			//on tente le valueOf de l'enum malgres l'exception car il ne faut pas manquer de contrôles, et le code doit être maitrisée
			var controlePasse = false;
			switch (FieldConstraintEnum.valueOf(StringUtil.camelToConstCase(fieldConstraint))) {
				case EMAIL_NOT_IN_BLACKLIST:
					controlePasse = checkEmailNotInBlackList((String) typedValue, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_EMAIL_NOT_IN_BLACK_LIST_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				case GTE_13_ANS:
					controlePasse = checkAgeRevolu((LocalDate) typedValue, LocalDate.now(), age -> age >= AGE_13_ANS, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_G_T_E_13ANS_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				case GTE_16_ANS:
					controlePasse = checkAgeRevolu((LocalDate) typedValue, LocalDate.now(), age -> age >= AGE_16_ANS, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_G_T_E_16ANS_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				case LT_16_ANS:
					controlePasse = checkAgeRevolu((LocalDate) typedValue, LocalDate.now(), age -> age < AGE_16_ANS, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_L_T_16ANS_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				case GTE_18_ANS:
					controlePasse = checkAgeRevolu((LocalDate) typedValue, LocalDate.now(), age -> age >= AGE_MAJORITE, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_G_T_E_18ANS_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				case LT_18_ANS:
					controlePasse = checkAgeRevolu((LocalDate) typedValue, LocalDate.now(), age -> age < AGE_MAJORITE, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_L_T_18ANS_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				case LTE_18_ANS:
					controlePasse = checkAgeRevolu((LocalDate) typedValue, LocalDate.now(), age -> age <= AGE_MAJORITE, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_L_T_E_18ANS_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				case TELEPHONE_FR:
					controlePasse = checkTelephoneFr((String) typedValue, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_TELEPHONE_FR_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				case TELEPHONE_MOBILE_SMS:
					controlePasse = checkTelephoneMobileSms((String) typedValue, uiMessageStack,
							MetaFormulaireResources.EF_FORMULAIRE_CONTROLE_TELEPHONE_MOBILE_SMS_ERROR, formulaireOwner, champ.getFieldCode());
					break;
				default:
					throw new IllegalArgumentException("Contrôle de champ inconnu " + fieldConstraint);
			}
			if (!controlePasse) {
				analyticsManager.getCurrentTracer().ifPresent(tracer -> tracer
						.incMeasure(ERREUR_CONTROLE_FORMULAIRE_MEASURE, 1)
						.setTag("controle", fieldConstraint)
						.setTag("champ", champ.getLabel()));

				break; //si un contrôle ne passe pas sur un champ, on passe au champ suivant
			}
		}
	}

	private static boolean checkTelephoneFr(
			final String telephone,
			final UiMessageStack uiMessageStack,
			final MetaFormulaireResources metaFormulaireResource,
			final Entity formulaireOwner, final String fieldCode) {
		if (PREDICATE_TELEPHONE_FR.test(telephone)) {
			return true;
		}
		uiMessageStack.error(LocaleMessageText.of(metaFormulaireResource).getDisplay(), formulaireOwner, FORMULAIRE_PREFIX + fieldCode);
		return false;
	}

	private static boolean checkTelephoneMobileSms(
			final String telephone,
			final UiMessageStack uiMessageStack,
			final MetaFormulaireResources metaFormulaireResource,
			final Entity formulaireOwner, final String fieldCode) {
		if (PREDICATE_TELEPHONE_SMS.test(telephone)) {
			return true;
		}
		uiMessageStack.error(LocaleMessageText.of(metaFormulaireResource).getDisplay(), formulaireOwner, FORMULAIRE_PREFIX + fieldCode);
		return false;
	}

	private static boolean checkAgeRevolu(
			final LocalDate dateNaissance,
			final LocalDate dateRdv,
			final IntPredicate ageValidator,
			final UiMessageStack uiMessageStack,
			final MetaFormulaireResources metaFormulaireResource,
			final Entity formulaireOwner,
			final String fieldCode) {
		final var age = Period.between(dateNaissance, dateRdv).getYears();
		if (ageValidator.test(age)) {
			return true;
		}
		uiMessageStack.error(LocaleMessageText.of(metaFormulaireResource).getDisplay(), formulaireOwner, FORMULAIRE_PREFIX + fieldCode);
		return false;
	}

	private static boolean checkEmailNotInBlackList(
			final String email,
			final UiMessageStack uiMessageStack,
			final MetaFormulaireResources metaFormulaireResource,
			final Entity formulaireOwner,
			final String fieldCode) {
		final int arobaIndex = email.indexOf('@');//le format a été vérifié
		final String emailDomain = email.substring(arobaIndex + 1).toLowerCase(Locale.ROOT);
		if (!EMAIL_DOMAIN_BLACK_LIST.contains(emailDomain)) {
			return true;
		}
		uiMessageStack.error(LocaleMessageText.of(metaFormulaireResource).getDisplay(), formulaireOwner, FORMULAIRE_PREFIX + fieldCode);
		return false;
	}

	private static SmartTypeDefinition getSmartTypeByNom(final String nomSmartType) {
		return Node.getNode().getDefinitionSpace().resolve(nomSmartType, SmartTypeDefinition.class);
	}

}
