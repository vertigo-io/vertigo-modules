package io.vertigo.easyforms.impl.designer.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.locale.LocaleMessageText;
import io.vertigo.core.node.Node;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.util.VCollectors;
import io.vertigo.easyforms.dao.EasyFormDAO;
import io.vertigo.easyforms.designer.services.IEasyFormsDesignerServices;
import io.vertigo.easyforms.domain.DtDefinitions.EasyFormsItemUiFields;
import io.vertigo.easyforms.domain.EasyForm;
import io.vertigo.easyforms.domain.EasyFormsFieldTypeUi;
import io.vertigo.easyforms.domain.EasyFormsFieldValidatorTypeUi;
import io.vertigo.easyforms.domain.EasyFormsItemUi;
import io.vertigo.easyforms.domain.EasyFormsSectionUi;
import io.vertigo.easyforms.impl.designer.Resources;
import io.vertigo.easyforms.runner.model.data.EasyFormsContext;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldValidatorTypeDefinition;
import io.vertigo.easyforms.runner.model.template.AbstractEasyFormsTemplateItem;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateSection;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemBlock;
import io.vertigo.easyforms.runner.model.template.item.EasyFormsTemplateItemField;
import io.vertigo.vega.webservice.validation.UiMessageStack;
import io.vertigo.vega.webservice.validation.ValidationUserException;

@Transactional
public class EasyFormsDesignerServices implements IEasyFormsDesignerServices {

	@Inject
	private EasyFormDAO easyFormDAO;

	@Override
	public DtList<EasyFormsFieldTypeUi> getFieldTypeUiList() {
		return Node.getNode().getDefinitionSpace().getAll(EasyFormsFieldTypeDefinition.class)
				.stream()
				.filter(d -> d.getCategory() != null)
				.map(fieldType -> {
					final var fieldTypeUi = new EasyFormsFieldTypeUi();
					fieldTypeUi.setName(fieldType.id().fullName());
					fieldTypeUi.setCategory(fieldType.getCategory());
					fieldTypeUi.setUiComponentName(fieldType.getUiComponentName());
					fieldTypeUi.setLabel(fieldType.getLabel());
					fieldTypeUi.setUiParameters(fieldType.getUiParameters());
					fieldTypeUi.setParamTemplate(fieldType.getParamTemplate());
					return fieldTypeUi;
				})
				.collect(VCollectors.toDtList(EasyFormsFieldTypeUi.class));
	}

	@Override
	public DtList<EasyFormsFieldValidatorTypeUi> getFieldValidatorTypeUiList() {
		return Node.getNode().getDefinitionSpace().getAll(EasyFormsFieldValidatorTypeDefinition.class)
				.stream()
				.sorted(Comparator.comparingInt(EasyFormsFieldValidatorTypeDefinition::getPriority))
				.map(fieldValidator -> {
					final var fieldValidatorUi = new EasyFormsFieldValidatorTypeUi();

					fieldValidatorUi.setName(fieldValidator.id().fullName());
					fieldValidatorUi.setLabel(fieldValidator.getLabel());
					fieldValidatorUi.setDescription(fieldValidator.getDescription());
					fieldValidatorUi.setFieldTypes(fieldValidator.getFieldTypes().stream().map(EasyFormsFieldTypeDefinition::getName).toList());
					return fieldValidatorUi;
				})
				.collect(VCollectors.toDtList(EasyFormsFieldValidatorTypeUi.class));
	}

	@Override
	public void checkUpdateSection(final List<EasyFormsTemplateSection> sections, final Integer editIndex, final EasyFormsSectionUi sectionEdit, final UiMessageStack uiMessageStack) {
		for (int i = 0; i < sections.size(); i++) {
			if (i != editIndex
					&& sections.get(i).getCode().equalsIgnoreCase(sectionEdit.getCode())) {
				// section code must be unique
				throw new ValidationUserException(LocaleMessageText.of(Resources.EfDesignerSectionCodeUnicity),
						sectionEdit, EasyFormsItemUiFields.fieldCode);
			}
		}
	}

	@Override
	public void checkUpdateField(final List<AbstractEasyFormsTemplateItem> items, final Integer editIndex, final Optional<Integer> editIndex2, final EasyFormsItemUi fieldEdit,
			final UiMessageStack uiMessageStack) {
		// field code must be unique in section
		for (int i = 0; i < items.size(); i++) {
			if (i != editIndex
					&& items.get(i) instanceof final EasyFormsTemplateItemField field
					&& field.getCode().equalsIgnoreCase(fieldEdit.getFieldCode())) {
				throw new ValidationUserException(LocaleMessageText.of(Resources.EfDesignerFieldCodeUnicity),
						fieldEdit, EasyFormsItemUiFields.fieldCode);
			} else if (items.get(i) instanceof final EasyFormsTemplateItemBlock block) {
				final var blockItems = block.getItems();
				for (int j = 0; j < blockItems.size(); j++) {
					if ((editIndex2.isEmpty() || editIndex2.get() != j)
							&& blockItems.get(j) instanceof final EasyFormsTemplateItemField field
							&& field.getCode().equalsIgnoreCase(fieldEdit.getFieldCode())) {
						throw new ValidationUserException(LocaleMessageText.of(Resources.EfDesignerFieldCodeUnicity),
								fieldEdit, EasyFormsItemUiFields.fieldCode);
					}
				}
			}
		}

		/*
		 * TODO: Example of multi-field parameter constraint that should be parametrable by project (via manager ?)
				if (!editField.getIsDefault() && editField.getIsDisplay()) {
					//on retire tous les autres qui auraient isDisplay (sauf les defaults)
					for (var champIndex = 0; champIndex < champs.size(); ++champIndex) {
						final var champUi = champs.get(champIndex);
						if (champIndex != editIndex && champUi.getIsDisplay() && !champUi.getIsDefault()) {
							champUi.setIsDisplay(false);
							uiMessageStack.warning("Il ne peut y avoir qu'un seul champ complémentaire affiché dans le formulaire.\nLe champ \"" + champUi.getFieldCode() + "\" n'est plus inclus");
						}
					}
				}
				*/
	}

	public boolean checkFormula(final EasyFormsContext context, final String formula) {
		final var contextKeys = context.keySet();
		return true;
	}

	@Override
	public Long saveForm(final EasyForm easyForm) {
		easyFormDAO.save(easyForm);
		return easyForm.getEfoId();
	}

	@Override
	public EasyForm createEasyForm(final EasyForm easyForm) {
		Assertion.check().isNotNull(easyForm);
		//---
		return easyFormDAO.create(easyForm);
	}

}
