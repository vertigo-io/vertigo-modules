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
package io.vertigo.easyforms.impl.runner.pack;

import java.time.LocalDate;
import java.util.List;

import io.vertigo.basics.constraint.ConstraintIntegerLength;
import io.vertigo.basics.constraint.ConstraintNumberMinimum;
import io.vertigo.basics.constraint.ConstraintRegex;
import io.vertigo.basics.constraint.ConstraintStringLength;
import io.vertigo.basics.formatter.FormatterDate;
import io.vertigo.basics.formatter.FormatterDefault;
import io.vertigo.basics.formatter.FormatterId;
import io.vertigo.basics.formatter.FormatterString;
import io.vertigo.core.lang.BasicType;
import io.vertigo.datamodel.smarttype.annotations.Adapter;
import io.vertigo.datamodel.smarttype.annotations.Constraint;
import io.vertigo.datamodel.smarttype.annotations.Formatter;
import io.vertigo.datamodel.smarttype.annotations.SmartTypeDefinition;
import io.vertigo.datamodel.smarttype.annotations.SmartTypeProperty;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintLocalDateMaximum;
import io.vertigo.easyforms.impl.runner.pack.constraint.ConstraintLocalDateMinimum;
import io.vertigo.easyforms.impl.runner.pack.formatter.FormatterExtensions;
import io.vertigo.easyforms.runner.model.adapter.EasyFormsJsonAdapter;
import io.vertigo.easyforms.runner.model.adapter.EasyFormsMapInputAdapter;
import io.vertigo.easyforms.runner.model.template.EasyFormsData;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplate;
import io.vertigo.easyforms.runner.model.template.EasyFormsTemplateFieldValidator;
import io.vertigo.ui.core.FileInfoURIAdapter;

public enum EasyFormsSmartTypes {

	@SmartTypeDefinition(Long.class)
	@Formatter(clazz = FormatterId.class)
	EfId,

	@SmartTypeDefinition(Boolean.class)
	@Formatter(clazz = FormatterDefault.class)
	EfBoolean,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintRegex.class, arg = "^[a-z][a-zA-Z0-9]*$", resourceMsg = "EfInvalidCode")
	@Constraint(clazz = ConstraintStringLength.class, arg = "100")
	@SmartTypeProperty(property = "indexType", value = "code:keyword")
	EfCode,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintStringLength.class, arg = "200")
	@SmartTypeProperty(property = "indexType", value = "text_fr:facetable:sortable")
	EfLabel,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintStringLength.class, arg = "1000")
	EfLongLabel,

	@SmartTypeDefinition(Integer.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintIntegerLength.class, arg = "9")
	@Constraint(clazz = ConstraintNumberMinimum.class, arg = "0")
	EfCount,

	@SmartTypeDefinition(Integer.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintIntegerLength.class, arg = "9")
	@Constraint(clazz = ConstraintNumberMinimum.class, arg = "1")
	EfCountStrict,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintStringLength.class, arg = "10000")
	EfText,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	EfTextHtml,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterString.class, arg = "UPPER")
	@Constraint(clazz = ConstraintStringLength.class, arg = "80")
	@Constraint(clazz = ConstraintRegex.class, arg = "^[^<>&\"]*$", resourceMsg = "EfInvalidSpecialChars")
	EfNom,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterString.class, arg = "UPPER_FIRST")
	@Constraint(clazz = ConstraintStringLength.class, arg = "80")
	@Constraint(clazz = ConstraintRegex.class, arg = "^[^<>&\"]*$", resourceMsg = "EfInvalidSpecialChars")
	EfPrenom,

	@SmartTypeDefinition(LocalDate.class)
	@Formatter(clazz = FormatterDate.class, arg = "dd/MM/yyyy;yyyy-MM-dd")
	@Constraint(clazz = ConstraintLocalDateMaximum.class, arg = "now+100y", resourceMsg = "EfDateTooLate")
	@Constraint(clazz = ConstraintLocalDateMinimum.class, arg = "now-200y", resourceMsg = "EfDateTooEarly")
	EfDate,

	@SmartTypeDefinition(LocalDate.class)
	@Formatter(clazz = FormatterDate.class, arg = "dd/MM/yyyy;yyyy-MM-dd")
	@Constraint(clazz = ConstraintLocalDateMaximum.class, arg = "now", resourceMsg = "EfDateInFutureError")
	@Constraint(clazz = ConstraintLocalDateMinimum.class, arg = "now-200y", resourceMsg = "EfDateTooEarly")
	EfDatePassee,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterString.class, arg = "LOWER")
	@Constraint(clazz = ConstraintRegex.class, arg = "^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*(\\.[a-zA-Z0-9-]{2,3})+$", resourceMsg = "EfInvalidEmail")
	@Constraint(clazz = ConstraintStringLength.class, arg = "80")
	EfEmail,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterTelephone.class, arg = "0033|+33:+33 #;005|+5:+5## #;006|+6:+6## #;0026|+26:+26# #;0:+33 #;+1:+1 #;+7:+7 #;+:+## #;#")
	@Constraint(clazz = ConstraintRegex.class,
			//vérifie un numéro international avec +XX ou 00XX ou un numéro francais.
			//Pour l'international seul le prefix 17 est filtré (pas d'autre commencant pas 1), et entre 2 et 13 chiffres après le prefix (la reco UIT-T limite à 15 le total)
			//pour la france vérifie qu'on a +33 ou 0033 ou 0 + 9 chiffres . peut avoir des () des . ou des espaces. doit finir par 2 chiffres consécutifs
			arg = "^((?:\\+|00)([17]|[245689]\\d|3[0-24-9]\\d)(?:\\W*\\d){2,13}\\d)|((((?:\\+|00)33\\W*)|0)[1-9](?:\\W*\\d){7}\\d)$", resourceMsg = "EfInvalidPhoneNumber")
	@Constraint(clazz = ConstraintStringLength.class, arg = "20")
	EfTelephone,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintRegex.class, arg = "^(?:D|[A-Z]{3})[0-9]{9}$", resourceMsg = "EfInvalidVisa")
	@Constraint(clazz = ConstraintStringLength.class, arg = "12", resourceMsg = "EfInvalidVisa")
	EfVisa,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterString.class, arg = "UPPER_FIRST")
	@Constraint(clazz = ConstraintStringLength.class, arg = "80")
	@Constraint(clazz = ConstraintRegex.class, arg = "^[^<>&\"]*$", resourceMsg = "EfInvalidSpecialChars")
	EfNationalite,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterDefault.class)
	@Constraint(clazz = ConstraintRegex.class, arg = "[0-9]{5}", resourceMsg = "EfInvalidPostalCode")
	@Constraint(clazz = ConstraintStringLength.class, arg = "5")
	EfCodePostal,

	// INTERNALS

	@SmartTypeDefinition(EasyFormsData.class)
	@Formatter(clazz = FormatterDefault.class)
	@Adapter(clazz = EasyFormsJsonAdapter.class, targetBasicType = BasicType.String, type = "ui")
	@Adapter(clazz = EasyFormsJsonAdapter.class, targetBasicType = BasicType.String, type = "sql")
	@SmartTypeProperty(property = "indexType", value = "text_fr")
	EfFormData,

	@SmartTypeDefinition(List.class)
	@Formatter(clazz = FormatterDefault.class)
	@Adapter(clazz = EasyFormsJsonAdapter.class, targetBasicType = BasicType.String)
	@Adapter(clazz = EasyFormsMapInputAdapter.class, targetBasicType = BasicType.String, type = "easyForm")
	EfIMapData,

	@SmartTypeDefinition(String.class)
	@Formatter(clazz = FormatterExtensions.class)
	EfIExtList, // used by file upload

	@SmartTypeDefinition(EasyFormsTemplate.class)
	@Formatter(clazz = FormatterDefault.class)
	@Adapter(clazz = EasyFormsJsonAdapter.class, targetBasicType = BasicType.String, type = "sql")
	EfFormTemplate,

	@SmartTypeDefinition(EasyFormsTemplateFieldValidator.class)
	@Formatter(clazz = FormatterDefault.class)
	@Adapter(clazz = EasyFormsJsonAdapter.class, targetBasicType = BasicType.String, type = "sql")
	EfFieldValidator,

	@SmartTypeDefinition(FileInfoURI.class)
	@Formatter(clazz = FormatterDefault.class)
	@Adapter(clazz = FileInfoURIAdapter.class, targetBasicType = BasicType.String)
	EfFileInfoURI,

}
