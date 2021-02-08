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
package io.vertigo.datamodel.structure.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.vertigo.core.lang.Cardinality;

/**
 * @author pchretien
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface ForeignKey {

	/**
	 * SmartType.
	 */
	String smartType();

	/**
	 * Field's label.
	 */
	String label();

	/**
	 * Foreign DtDefinition
	 */
	String fkDefinition();

	/**
	 * Field's cardinality
	 */
	Cardinality cardinality() default Cardinality.OPTIONAL_OR_NULLABLE;
}
