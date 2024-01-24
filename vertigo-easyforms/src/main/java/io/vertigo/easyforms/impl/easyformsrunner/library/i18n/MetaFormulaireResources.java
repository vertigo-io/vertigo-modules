/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2022, Vertigo.io, team@vertigo.io
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
package io.vertigo.easyforms.impl.easyformsrunner.library.i18n;

import io.vertigo.core.locale.LocaleMessageKey;

/**
 * Dictionnaire des ressources.
 *
 * @author  npiedeloup
*/
public enum MetaFormulaireResources implements LocaleMessageKey {

	EF_FORM_CONTROL_OPTIONEL_LABEL, EF_FORM_CONTROL_OPTIONEL_DESCRIPTION, EF_FORM_CONTROL_OPTIONEL_ERROR,

	/**
	 * Pour le message d'erreur unique : 0 : nom de la démarche, 1 label du champs non unique
	 */
	EF_FORM_CONTROL_UNIQUE_LABEL, EF_FORM_CONTROL_UNIQUE_DESCRIPTION, EF_FORM_CONTROL_UNIQUE_ERROR,

	EF_FORM_CONTROL_EMAIL_NOT_IN_BLACK_LIST_LABEL, EF_FORM_CONTROL_EMAIL_NOT_IN_BLACK_LIST_DESCRIPTION, EF_FORM_CONTROL_EMAIL_NOT_IN_BLACK_LIST_ERROR,

	EF_FORM_CONTROL_G_T_E_18ANS_LABEL, EF_FORM_CONTROL_G_T_E_18ANS_DESCRIPTION, EF_FORM_CONTROL_G_T_E_18ANS_ERROR,

	EF_FORM_CONTROL_L_T_E_18ANS_LABEL, EF_FORM_CONTROL_L_T_E_18ANS_DESCRIPTION, EF_FORM_CONTROL_L_T_E_18ANS_ERROR,

	EF_FORM_CONTROL_L_T_18ANS_LABEL, EF_FORM_CONTROL_L_T_18ANS_DESCRIPTION, EF_FORM_CONTROL_L_T_18ANS_ERROR, //

	EF_FORM_CONTROL_G_T_E_13ANS_LABEL, EF_FORM_CONTROL_G_T_E_13ANS_DESCRIPTION, EF_FORM_CONTROL_G_T_E_13ANS_ERROR, //

	EF_FORM_CONTROL_L_T_16ANS_LABEL, EF_FORM_CONTROL_L_T_16ANS_DESCRIPTION, EF_FORM_CONTROL_L_T_16ANS_ERROR, //

	EF_FORM_CONTROL_G_T_E_16ANS_LABEL, EF_FORM_CONTROL_G_T_E_16ANS_DESCRIPTION, EF_FORM_CONTROL_G_T_E_16ANS_ERROR,

	EF_FORM_CONTROL_TELEPHONE_FR_LABEL, EF_FORM_CONTROL_TELEPHONE_FR_DESCRIPTION, EF_FORM_CONTROL_TELEPHONE_FR_ERROR, //

	EF_FORM_CONTROL_TELEPHONE_MOBILE_SMS_LABEL, EF_FORM_CONTROL_TELEPHONE_MOBILE_SMS_DESCRIPTION, EF_FORM_CONTROL_TELEPHONE_MOBILE_SMS_ERROR, //

	EF_FORM_CONTROL_CODE_DEPARTEMENT_LABEL, EF_FORM_CONTROL_CODE_DEPARTEMENT_DESCRIPTION, EF_FORM_CONTROL_CODE_DEPARTEMENT_ERROR

}
