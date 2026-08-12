/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2026, Vertigo.io, team@vertigo.io
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
package io.vertigo.geo.plugins.geosearch.es;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.HealthStatus;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.vertigo.commons.codec.CodecManager;
import io.vertigo.connectors.elasticsearch.RestElasticSearchConnector;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.param.ParamValue;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.data.definitions.DataFieldName;
import io.vertigo.datamodel.data.model.DataObject;
import io.vertigo.datamodel.data.model.DtList;
import io.vertigo.datamodel.data.util.VCollectors;
import io.vertigo.geo.geocoder.GeoLocation;
import io.vertigo.geo.impl.geosearch.GeoSearchPlugin;
import jakarta.inject.Inject;

public final class ESGeoSearchPlugin implements GeoSearchPlugin, Activeable {

	private final RestElasticSearchConnector elasticSearchConnector;

	private ElasticsearchClient esClient;
	private final String envIndexPrefix;
	private final CodecManager codecManager;

	@Inject
	public ESGeoSearchPlugin(
			@ParamValue("envIndexPrefix") final String envIndexPrefix,
			@ParamValue("connectorName") final Optional<String> connectorNameOpt,
			final List<RestElasticSearchConnector> elasticSearchConnectors,
			final CodecManager codecManager) {
		Assertion.check()
				.isNotBlank(envIndexPrefix)
				.isNotNull(elasticSearchConnectors)
				.isFalse(elasticSearchConnectors.isEmpty(), "At least one ElasticSearchConnector espected");
		//-----
		this.envIndexPrefix = envIndexPrefix;
		final String connectorName = connectorNameOpt.orElse("main");
		elasticSearchConnector = elasticSearchConnectors.stream()
				.filter(connector -> connectorName.equals(connector.getName()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Can't found ElasticSearchConnector named '" + connectorName + "' in " + elasticSearchConnectors));

		this.codecManager = codecManager;
	}

	/** {@inheritDoc} */
	@Override
	public void start() {
		//Init ElasticSearch Client
		esClient = elasticSearchConnector.getClient();
		//must wait yellow status to be sure prepareExists works fine (instead of returning false on a already exist index)
		waitForYellowStatus();

	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		// nothing
	}

	private String obtainIndexName(final String indexName) {
		return StringUtil.camelToConstCase(envIndexPrefix + indexName).toLowerCase(Locale.ROOT);
	}

	@Override
	public <D extends DataObject> DtList<D> searchInBoundingBox(
			final GeoLocation topLeft,
			final GeoLocation bottomRight,
			final String indexName,
			final Class<D> dtIndexClass,
			final DataFieldName<D> fieldName,
			final Integer maxRows) {
		try {
			// On précise Map.class car on veut récupérer le JSON brut sous forme de Map
			final SearchResponse<Map> searchResponse = esClient.search(s -> s
					.index(obtainIndexName(indexName))
					.size(maxRows)
					.query(q -> q.geoBoundingBox(g -> g
							.field(fieldName.name())
							.boundingBox(bb -> bb.tlbr(tlbr -> tlbr
									.topLeft(loc -> loc.latlon(ll -> ll.lat(topLeft.getLatitude()).lon(topLeft.getLongitude())))
									.bottomRight(loc -> loc.latlon(ll -> ll.lat(bottomRight.getLatitude()).lon(bottomRight.getLongitude())))))))
					.source(src -> src.filter(f -> f.includes("fullResult"))), Map.class);
			return searchResponse.hits().hits().stream()
					.map(hit -> ((D) codecManager.getCompressedSerializationCodec().decode(codecManager.getBase64Codec().decode((String) hit.source().get("fullResult")))))
					.collect(VCollectors.toDtList(dtIndexClass));
		} catch (final IOException e) {
			throw WrappedException.wrap(e);
		}

	}

	private void waitForYellowStatus() {
		try {
			final var response = esClient.cluster().health(b -> b
					.timeout(t -> t.time("30s"))
					.waitForStatus(HealthStatus.Yellow));
			//-----
			Assertion.check().isFalse(response.timedOut(), "ElasticSearch cluster waiting yellow status Timedout");
		} catch (final IOException e) {
			throw WrappedException.wrap(e, "Error on waitForYellowStatus");
		}
	}

}
