/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2025, Vertigo.io, team@vertigo.io
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
package io.vertigo.audit.trace;

import java.time.Instant;

import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.util.DataModelUtil;

/**
 * This class defines the Audit Trace for an Object.
 *
 * @author xdurand
 */
public record TraceCriteria(
		String category,
		String username,
		Instant startBusinessDate,
		Instant endBusinessDate,
		Instant startExecutionDate,
		Instant endExecutionDate,
		String itemUrn) implements DataObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Static method factory for AuditTraceCriteriaBuilder
	 * @return AuditTraceCriteriaBuilder
	 */
	public static TraceCriteriaBuilder builder() {
		return new TraceCriteriaBuilder();
	}

	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
