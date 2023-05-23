/**
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
package io.vertigo.ui.data.search.movies;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.core.lang.Generated;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.node.definition.DefinitionProvider;
import io.vertigo.core.node.definition.DefinitionSpace;
import io.vertigo.core.node.definition.DefinitionSupplier;
import io.vertigo.datafactory.collections.definitions.FacetDefinition.FacetOrder;
import io.vertigo.datafactory.collections.definitions.FacetRangeDefinitionSupplier;
import io.vertigo.datafactory.collections.definitions.FacetTermDefinitionSupplier;
import io.vertigo.datafactory.collections.definitions.FacetedQueryDefinitionSupplier;
import io.vertigo.datafactory.collections.model.FacetedQueryResult;
import io.vertigo.datafactory.collections.model.SelectedFacetValues;
import io.vertigo.datafactory.search.SearchManager;
import io.vertigo.datafactory.search.definitions.SearchIndexDefinition;
import io.vertigo.datafactory.search.definitions.SearchIndexDefinitionSupplier;
import io.vertigo.datafactory.search.model.SearchQuery;
import io.vertigo.datafactory.search.model.SearchQueryBuilder;
import io.vertigo.datamodel.structure.model.DtListState;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.ui.data.domain.movies.Movie;
import io.vertigo.ui.data.domain.movies.MovieIndex;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class MovieSearchClient implements Component, DefinitionProvider {

	private final SearchManager searchManager;
	private final VTransactionManager transactionManager;

	/**
	 * Contructeur.
	 * @param searchManager Search Manager
	 * @param transactionManager Transaction Manager
	 */
	@Inject
	public MovieSearchClient(final SearchManager searchManager, final VTransactionManager transactionManager) {
		this.searchManager = searchManager;
		this.transactionManager = transactionManager;
	}

	/**
	 * Création d'une SearchQuery de type : Movie.
	 * @param criteria Critères de recherche
	 * @param selectedFacetValues Liste des facettes sélectionnées à appliquer
	 * @return SearchQueryBuilder pour ce type de recherche
	 */

	public SearchQueryBuilder createSearchQueryBuilderMovie(final java.lang.String criteria, final SelectedFacetValues selectedFacetValues) {
		return SearchQuery.builder("QryMovie")
				.withCriteria(criteria)
				.withFacet(selectedFacetValues);
	}

	/**
	 * Création d'une SearchQuery de type : MovieWithPoster.
	 * @param criteria Critères de recherche
	 * @param selectedFacetValues Liste des facettes sélectionnées à appliquer
	 * @return SearchQueryBuilder pour ce type de recherche
	 */
	public SearchQueryBuilder createSearchQueryBuilderMovieWithPoster(final java.lang.String criteria, final SelectedFacetValues selectedFacetValues) {
		return SearchQuery.builder("QryMovieWithPoster")
				.withCriteria(criteria)
				.withFacet(selectedFacetValues);
	}

	/**
	 * Récupération du résultat issu d'une requête.
	 * @param searchQuery critères initiaux
	 * @param listState Etat de la liste (tri et pagination)
	 * @return Résultat correspondant à la requête (de type MovieIndex)
	 */
	public FacetedQueryResult<MovieIndex, SearchQuery> loadList(final SearchQuery searchQuery, final DtListState listState) {
		final SearchIndexDefinition indexDefinition = searchManager.findFirstIndexDefinitionByKeyConcept(Movie.class);
		return searchManager.loadList(indexDefinition, searchQuery, listState);
	}

	/**
	 * Mark an entity as dirty. Index of these elements will be reindexed if Tx commited.
	 * Reindexation isn't synchrone, strategy is dependant of plugin's parameters.
	 *
	 * @param entityUID Key concept's UID
	 */
	public void markAsDirty(final UID<Movie> entityUID) {
		transactionManager.getCurrentTransaction().addAfterCompletion((final boolean txCommitted) -> {
			if (txCommitted) {// reindex only is tx successful
				searchManager.markAsDirty(Arrays.asList(entityUID));
			}
		});
	}

	/**
	 * Mark an entity as dirty. Index of these elements will be reindexed if Tx commited.
	 * Reindexation isn't synchrone, strategy is dependant of plugin's parameters.
	 *
	 * @param entity Key concept
	 */
	public void markAsDirty(final Movie entity) {
		markAsDirty(UID.of(entity));
	}

	/** {@inheritDoc} */
	@Override
	public List<DefinitionSupplier> get(final DefinitionSpace definitionSpace) {
		return List.of(
				//---
				// SearchIndexDefinition
				//-----
				new SearchIndexDefinitionSupplier("IdxMovie")
						.withKeyConcept("DtMovie")
						.withIndexDtDefinition("DtMovieIndex")
						.withLoaderId("MovieSearchLoader"),
				//---
				// FacetTermDefinition
				//-----
				new FacetTermDefinitionSupplier("FctMovieType")
						.withDtDefinition("DtMovieIndex")
						.withFieldName("movieType")
						.withLabel("Par type")
						.withOrder(FacetOrder.count),
				new FacetRangeDefinitionSupplier("FctMovieTitle")
						.withDtDefinition("DtMovieIndex")
						.withFieldName("titleSortOnly")
						.withLabel("Par titre")
						.withRange("r1", "titleSortOnly:[* TO a]", "#")
						.withRange("r2", "titleSortOnly:[a TO g]", "a-f")
						.withRange("r3", "titleSortOnly:[g TO n]", "g-m")
						.withRange("r4", "titleSortOnly:[n TO t]", "n-s")
						.withRange("r5", "titleSortOnly:[t TO *]", "t-z")
						.withOrder(FacetOrder.definition),
				new FacetRangeDefinitionSupplier("FctMovieYear")
						.withDtDefinition("DtMovieIndex")
						.withFieldName("productionYear")
						.withLabel("Par date")
						.withRange("r1", "productionYear:[* TO 1930]", "< années 30")
						.withRange("r2", "productionYear:[1930 TO 1940]", "années 30")
						.withRange("r3", "productionYear:[1940 TO 1950]", "années 40")
						.withRange("r4", "productionYear:[1950 TO 1960]", "années 50")
						.withRange("r5", "productionYear:[1960 TO 1970]", "années 60")
						.withRange("r6", "productionYear:[1970 TO 1980]", "années 70")
						.withRange("r7", "productionYear:[1980 TO 1990]", "années 80")
						.withRange("r8", "productionYear:[1990 TO 2000]", "années 90")
						.withRange("r9", "productionYear:[2000 TO 2010]", "années 2000")
						.withRange("r10", "productionYear:[2010 TO *]", "> années 2010")
						.withOrder(FacetOrder.definition),
				//---
				// FacetedQueryDefinition
				//-----
				new FacetedQueryDefinitionSupplier("QryMovie")
						.withListFilterBuilderClass(io.vertigo.datafactory.impl.search.dsl.DslListFilterBuilder.class)
						.withListFilterBuilderQuery("_all:#+query*#")
						.withCriteriaSmartType("STyLabel")
						.withFacet("FctMovieType")
						.withFacet("FctMovieTitle")
						.withFacet("FctMovieYear"),
				new FacetedQueryDefinitionSupplier("QryMovieWithPoster")
						.withListFilterBuilderClass(io.vertigo.datafactory.impl.search.dsl.DslListFilterBuilder.class)
						.withListFilterBuilderQuery("_all:#+query*# +_exists_:poster")
						.withCriteriaSmartType("STyLabel")
						.withFacet("FctMovieType")
						.withFacet("FctMovieTitle")
						.withFacet("FctMovieYear"));
	}

}
