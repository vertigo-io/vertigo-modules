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
package io.vertigo.social.plugins.comment.redis;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import io.vertigo.account.account.Account;
import io.vertigo.connectors.redis.RedisConnector;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.MapBuilder;
import io.vertigo.core.param.ParamValue;
import io.vertigo.datamodel.data.model.KeyConcept;
import io.vertigo.datamodel.data.model.UID;
import io.vertigo.social.comment.Comment;
import io.vertigo.social.impl.comment.CommentPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * @author pchretien
 */
public final class RedisCommentPlugin implements CommentPlugin {
	private static final String REDIS_PREFIX = "{comment}:";
	private final RedisConnector redisConnector;

	/**
	 * @param connectorNameOpt Redis connector Name
	 */
	@Inject
	public RedisCommentPlugin(
			@ParamValue("connectorName") final Optional<String> connectorNameOpt,
			final List<RedisConnector> redisConnectors) {
		Assertion.check()
				.isNotNull(connectorNameOpt)
				.isNotNull(redisConnectors);
		//-----
		final String connectorName = connectorNameOpt.orElse("main");
		redisConnector = redisConnectors.stream()
				.filter(connector -> connectorName.equals(connector.getName()))
				.findFirst().get();
		//current version don't support multi nodes. Migration needs to rework keys and will break compatibility (need data migration).
		Assertion.check().isFalse(redisConnector.isMultiNodes(), this.getClass().getSimpleName() + " isn't compatible with RedisConnector multiNodes (cluster)");
	}

	/** {@inheritDoc} */
	@Override
	public <S extends KeyConcept> void publish(final Comment comment, final UID<S> keyConceptUri) {
		try (final Jedis jedis = redisConnector.getClient(REDIS_PREFIX)) {
			try (final Transaction tx = jedis.multi()) {
				tx.hmset("comment:" + comment.uuid(), toMap(comment));
				tx.lpush("comments:" + keyConceptUri.urn(), comment.uuid().toString());
				tx.exec();
			}
		}

	}

	/** {@inheritDoc} */
	@Override
	public void update(final Comment comment) {
		try (final Jedis jedis = redisConnector.getClient(REDIS_PREFIX)) {
			//On vérifie la présence de l'élément en base pour s'assurer la cohérence du stockage,
			//et notament qu'il soit référencé dans "comments:keyConceptUrn"
			final boolean elementExist = jedis.exists("comment:" + comment.uuid());
			if (!elementExist) {
				throw new UnsupportedOperationException("Comment " + comment.uuid() + " doesn't exists");
			}
			jedis.hmset("comment:" + comment.uuid(), toMap(comment));
		}
	}

	/** {@inheritDoc} */
	@Override
	public Comment get(final UUID uuid) {
		try (final Jedis jedis = redisConnector.getClient(REDIS_PREFIX)) {
			return fromMap(jedis.hgetAll("comment:" + uuid));
		}
	}

	/** {@inheritDoc} */
	@Override
	public <S extends KeyConcept> List<Comment> getComments(final UID<S> keyConceptUri) {
		final List<Response<Map<String, String>>> responses = new ArrayList<>();
		try (final Jedis jedis = redisConnector.getClient(REDIS_PREFIX)) {
			final List<String> uuids = jedis.lrange("comments:" + keyConceptUri.urn(), 0, -1);
			try (final Transaction tx = jedis.multi()) {
				for (final String uuid : uuids) {
					responses.add(tx.hgetAll("comment:" + uuid));
				}
				tx.exec();
			}
		}
		//----- we are using tx to avoid roundtrips
		final List<Comment> comments = new ArrayList<>();
		for (final Response<Map<String, String>> response : responses) {
			final Map<String, String> data = response.get();
			if (!data.isEmpty()) {
				comments.add(fromMap(data));
			}
		}
		return comments;
	}

	private static Map<String, String> toMap(final Comment comment) {
		final String lastModified = comment.lastModified() != null ? comment.lastModified().toString() : null;
		return new MapBuilder<String, String>()
				.put("uuid", comment.uuid().toString())
				.put("author", String.valueOf(Serializable.class.cast(comment.author().getId())))
				.put("msg", comment.msg())
				.put("creationDate", comment.creationDate().toString())
				.putNullable("lastModified", lastModified)
				.build();
	}

	private static Comment fromMap(final Map<String, String> data) {
		final Instant creationDate = Instant.parse(data.get("creationDate"));
		final Instant lastModified = data.get("lastModified") != null ? Instant.parse(data.get("lastModified")) : null;

		return Comment.builder()
				.withUuid(UUID.fromString(data.get("uuid")))
				.withAuthor(UID.of(Account.class, data.get("author")))
				.withCreationDate(creationDate)
				.withMsg(data.get("msg"))
				.withLastModified(lastModified)
				.build();

	}
}
