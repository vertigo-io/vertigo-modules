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
package io.vertigo.easyforms.designer;

import io.vertigo.core.locale.LocaleMessageKey;

/**
 * Resources dictionary.
 *
 * @author npiedeloup
 */
public enum Resources implements LocaleMessageKey {
	EfDesignerMandatory,

	EfDesignerReservedCode,
	EfDesignerSectionCodeUnicity,
	EfDesignerSectionConditionInvalid,
	EfDesignerFieldCodeUnicity,

	EfDesignerPendingChanges,

	EfDesignerNoCondition,

	EfDesignerAddSection,
	EfDesignerMoveSectionUp,
	EfDesignerMoveSectionDown,
	EfDesignerEditSection,
	EfDesignerDeleteSection,

	EfDesignerSectionDeleted,
	EfDesignerSectionMoved,
	EfDesignerSectionValidated,

	EfDesignerAddStatic,
	EfDesignerAddBlock,
	EfDesignerAddField,
	EfDesignerMoveItemUp,
	EfDesignerMoveItemDown,
	EfDesignerMoveItemUpLeft,
	EfDesignerMoveItemDownLeft,
	EfDesignerMoveItemUpRight,
	EfDesignerMoveItemDownRight,
	EfDesignerEditItem,
	EfDesignerDeleteItem,

	EfDesignerItemDeleted,
	EfDesignerItemMoved,
	EfDesignerItemValidated,

	EfDesignerNoDeleteField,
	EfDesignerIsDefaultField,
	EfDesignerHasControl,

	EfDesignerClose,
	EfDesignerSave,
	EfDesignerPopupTitle,
	EfDesignerPopupSectionTitle,

	EfDesignerFieldCodeHint,
	EfDesignerFieldAria,
	EfDesignerRequiredNotModifiable,

	EfDesignerContextDump,

	EfUicMap$value,
	EfUicMap$label,
}
