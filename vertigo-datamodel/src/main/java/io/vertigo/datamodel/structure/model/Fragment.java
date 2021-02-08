/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2021, Vertigo.io, team@vertigo.io
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
package io.vertigo.datamodel.structure.model;

/**
 * Fragment of an entity.
 *
 * An fragment placed in a domain model.
 * An fragment belongs to an entity.
 *
 * @author pchretien
 * @param <E> Entity type
 */
public interface Fragment<E extends Entity> extends DtObject {

	/**
	 * @return UID of linked entity
	 */
	UID<E> getEntityUID();
}
