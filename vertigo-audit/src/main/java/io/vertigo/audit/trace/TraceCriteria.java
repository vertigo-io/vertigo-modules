/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
public final class TraceCriteria implements DataObject {
	private static final long serialVersionUID = 1L;

	private final String category;
	private final String username;
	private final Instant startBusinessDate;
	private final Instant endBusinessDate;
	private final Instant startExecutionDate;
	private final Instant endExecutionDate;
	private final String itemUrn;

	TraceCriteria(
			final String category,
			final String username,
			final Instant startBusinessDate,
			final Instant endBusinessDate,
			final Instant startExecutionDate,
			final Instant endExecutionDate,
			final String itemUrn) {
		this.category = category;
		this.username = username;
		this.startBusinessDate = startBusinessDate;
		this.endBusinessDate = endBusinessDate;
		this.startExecutionDate = startExecutionDate;
		this.endExecutionDate = endExecutionDate;
		this.itemUrn = itemUrn;
	}

	/**
	 * Static method factory for AuditTraceCriteriaBuilder
	 * @return AuditTraceCriteriaBuilder
	 */
	public static TraceCriteriaBuilder builder() {
		return new TraceCriteriaBuilder();
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return the user
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the startBusinessDate
	 */
	public Instant getStartBusinessDate() {
		return startBusinessDate;
	}

	/**
	 * @return the endBusinessDate
	 */
	public Instant getEndBusinessDate() {
		return endBusinessDate;
	}

	/**
	 * @return the startExecutionDate
	 */
	public Instant getStartExecutionDate() {
		return startExecutionDate;
	}

	/**
	 * @return the endExecutionDate
	 */
	public Instant getEndExecutionDate() {
		return endExecutionDate;
	}

	/**
	 * @return the item Urn
	 */
	public String getItemUrn() {
		return itemUrn;
	}

	@Override
	public String toString() {
		return DataModelUtil.toString(this);
	}
}
