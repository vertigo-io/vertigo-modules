/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023_LABEL, Vertigo.io_LABEL, team@vertigo.io
 *
 * Licensed under the Apache License_LABEL, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing_LABEL, software
 * distributed under the License is distributed on an "AS IS" BASIS_LABEL,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND_LABEL, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.easyforms.impl.runner.pack;

import io.vertigo.core.locale.LocaleMessageKey;

/**
 * Resources dictionary.
 *
 * @author skerdudou
 */
public enum EfPackResources implements LocaleMessageKey {
	EfTrue,
	EfFalse,

	// Field types
	EfFtyLabelLabel,
	EfFtyLastNameLabel,
	EfFtyFirstNameLabel,
	EfFtyEmailLabel,
	EfFtyDateLabel,
	EfFtyBirthDateLabel,
	EfFtyPhoneLabel,
	EfFtyVisaLabel,
	EfFtyNationalityLabel,
	EfFtyPostalCodeLabel,
	EfFtyTextLabel,
	EfFtyText$maxLengthLabel,
	EfFtyYesNoLabel,
	EfFtyCountLabel,
	EfFtyCustomListSelectLabel,
	EfFtyCustomListRadioLabel,
	EfFtyCustomListCheckboxLabel,

	EfFtyInternalLayoutLabel,
	EfFtyInternalMapLabel,

	EfFtyCustomListSelect$customListLabel,
	EfFtyCustomListRadio$customListLabel,
	EfFtyCustomListCheckbox$customListLabel,

	EfFtyCustomListRadio$layoutLabel,
	EfFtyCustomListRadio$layoutVerticalLabel,
	EfFtyCustomListRadio$layoutHorizontalLabel,

	EfFtyCustomListCheckbox$layoutLabel,
	EfFtyCustomListCheckbox$layoutVerticalLabel,
	EfFtyCustomListCheckbox$layoutHorizontalLabel,

	// Validators
	EfFvaEmailNotInBlacklistLabel,
	EfFvaEmailNotInBlacklistDescription,
	EfFvaGte13AnsLabel,
	EfFvaLt16AnsLabel,
	EfFvaGte16AnsLabel,
	EfFvaLt18AnsLabel,
	EfFvaGte18AnsLabel,
	EfFvaPhoneFrLabel,
	EfFvaPhoneFrFixeLabel,
	EfFvaPhoneFrMobileLabel,
}
