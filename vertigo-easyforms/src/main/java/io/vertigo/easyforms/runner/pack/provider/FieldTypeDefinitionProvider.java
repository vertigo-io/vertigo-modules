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
package io.vertigo.easyforms.runner.pack.provider;

import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.SimpleEnumDefinitionProvider;
import io.vertigo.core.util.StringUtil;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsFieldTypeDefinition;
import io.vertigo.easyforms.runner.model.definitions.EasyFormsUiComponentDefinition;
import io.vertigo.easyforms.runner.pack.EasyFormsSmartTypes;
import io.vertigo.easyforms.runner.pack.provider.FieldValidatorTypeDefinitionProvider.FieldValidatorEnum;
import io.vertigo.easyforms.runner.pack.provider.UiComponentDefinitionProvider.UiComponentEnum;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.AutocompleteFieldType;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.CustomListFieldType;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.DateType;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.FileFieldType;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.InternalMapFieldType;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.RadioLayoutFieldType;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.SimpleFieldType;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.TextAreaType;
import io.vertigo.easyforms.runner.pack.provider.fieldtype.YesNoFieldType;
import io.vertigo.easyforms.runner.suppliers.IEasyFormsFieldTypeDefinitionSupplier;

public final class FieldTypeDefinitionProvider implements SimpleEnumDefinitionProvider<EasyFormsFieldTypeDefinition> {

	public enum FieldTypeEnum implements EnumDefinition<EasyFormsFieldTypeDefinition, FieldValidatorEnum> {
		LABEL(EasyFormsSmartTypes.EfLabel, UiComponentEnum.TEXT_FIELD),

		LAST_NAME(new AutocompleteFieldType(EasyFormsSmartTypes.EfNom, "family-name")),

		FIRST_NAME(new AutocompleteFieldType(EasyFormsSmartTypes.EfPrenom, "given-name")),

		EMAIL(new AutocompleteFieldType(EasyFormsSmartTypes.EfEmail, "email")),

		DATE(new DateType()),

		BIRTH_DATE(EasyFormsSmartTypes.EfDatePassee, UiComponentEnum.DATE),

		PHONE(new AutocompleteFieldType(EasyFormsSmartTypes.EfTelephone, "tel")),

		VISA(EasyFormsSmartTypes.EfVisa, UiComponentEnum.TEXT_FIELD),

		NATIONALITY(EasyFormsSmartTypes.EfNationalite, UiComponentEnum.TEXT_FIELD),

		POSTAL_CODE(new AutocompleteFieldType(EasyFormsSmartTypes.EfCodePostal, "postal-code")),

		TEXT(new TextAreaType()),

		YES_NO(new YesNoFieldType()),

		COUNT(EasyFormsSmartTypes.EfCount, UiComponentEnum.NUMBER),
		AMOUNT(EasyFormsSmartTypes.EfAmount, UiComponentEnum.NUMBER),

		CUSTOM_LIST_SELECT(new CustomListFieldType(UiComponentEnum.SELECT)),
		CUSTOM_LIST_RADIO(new CustomListFieldType(UiComponentEnum.RADIO)),
		CUSTOM_LIST_CHECKBOX(new CustomListFieldType(UiComponentEnum.CHECKBOX)),

		FILE(new FileFieldType()),

		COMPUTED_AMOUNT(EasyFormsSmartTypes.EfAmount, UiComponentEnum.READ_ONLY),

		// internal use (null category means "hidden")
		INTERNAL_LAYOUT(new RadioLayoutFieldType()),
		INTERNAL_MAP(new InternalMapFieldType()),
		INTERNAL_EXTENSIONS(null, EasyFormsSmartTypes.EfIExtList, UiComponentEnum.TEXT_FIELD), // used by file upload
		INTERNAL_SELECT(null, EasyFormsSmartTypes.EfId, UiComponentEnum.SELECT),
		COUNT_STRICT(null, EasyFormsSmartTypes.EfCountStrict, UiComponentEnum.NUMBER),
		;

		// ---

		private final String definitionName;
		private final IEasyFormsFieldTypeDefinitionSupplier typeSupplier;

		private FieldTypeEnum(final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
			this(new SimpleFieldType(smartType, uiComponent));
		}

		private FieldTypeEnum(final String category, final EasyFormsSmartTypes smartType, final EnumDefinition<EasyFormsUiComponentDefinition, ?> uiComponent) {
			this(new SimpleFieldType(category, smartType, uiComponent));
		}

		private FieldTypeEnum(final IEasyFormsFieldTypeDefinitionSupplier typeSupplier) {
			definitionName = EasyFormsFieldTypeDefinition.PREFIX + StringUtil.constToUpperCamelCase(name());
			this.typeSupplier = typeSupplier;
		}

		@Override
		public EasyFormsFieldTypeDefinition buildDefinition(final DefinitionSpace definitionSpace) {
			return typeSupplier.get(definitionName);
		}

		@Override
		public EasyFormsFieldTypeDefinition get() {
			return EasyFormsFieldTypeDefinition.resolve(definitionName);
		}

		@Override
		public String getDefinitionName() {
			return definitionName;
		}

	}

	@Override
	public Class<FieldTypeEnum> getEnumClass() {
		return FieldTypeEnum.class;
	}

}
