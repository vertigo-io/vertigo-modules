/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
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
package io.vertigo.vega.impl.rest.catalog;

import io.vertigo.dynamo.collections.model.FacetedQueryResult;
import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.dynamo.file.model.VFile;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.Builder;
import io.vertigo.lang.Option;
import io.vertigo.util.ClassUtil;
import io.vertigo.util.StringUtil;
import io.vertigo.vega.rest.EndPointTypeUtil;
import io.vertigo.vega.rest.metamodel.EndPointDefinition;
import io.vertigo.vega.rest.metamodel.EndPointDefinition.Verb;
import io.vertigo.vega.rest.metamodel.EndPointParam;
import io.vertigo.vega.rest.metamodel.EndPointParam.RestParamType;
import io.vertigo.vega.rest.metamodel.EndPointParamBuilder;
import io.vertigo.vega.rest.model.UiListState;
import io.vertigo.vega.rest.validation.UiMessageStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Swagger RestService to list services published.
 * @see "https://github.com/wordnik/swagger-spec/blob/master/versions/2.0.md"
 * @author npiedeloup (22 juil. 2014 11:12:02)
 */
public final class SwaggerApiBuilder implements Builder<Map<String, Object>> {

	private static final String UNKNOWN_OBJECT_NAME = "unkwnown";

	private final Map<String, Object> builderDefinitions = new LinkedHashMap<>();
	private final Map<String, Object> unknownObjectRef = new LinkedHashMap<>();

	private String builderContextPath = "/";
	private Collection<EndPointDefinition> builderEndPointDefinitions;

	/**
	 * Constructor.
	 */
	public SwaggerApiBuilder() {
		final Map<String, Object> schema = new LinkedHashMap<>();
		schema.put("type", "object");
		builderDefinitions.put(UNKNOWN_OBJECT_NAME, schema);
		unknownObjectRef.put("$ref", "#/definitions/" + UNKNOWN_OBJECT_NAME);
	}

	/**
	 * @param contextPath ContextPath of API request
	 * @return this builder
	 */
	public SwaggerApiBuilder withContextPath(final String contextPath) {
		Assertion.checkArgNotEmpty(contextPath, "contextPath can't be empty (minimal should be '/')");
		//-----
		builderContextPath = contextPath;
		return this;
	}

	/**
	 * @param endPointDefinitions EndPointDefinitions to use for swagger api
	 * @return this builder
	 */
	public SwaggerApiBuilder withEndPointDefinitions(final Collection<EndPointDefinition> endPointDefinitions) {
		Assertion.checkNotNull(endPointDefinitions, "endPointDefinitions can't be null");
		Assertion.checkArgument(!endPointDefinitions.isEmpty(), "endPointDefinitions can't be empty");
		Assertion.checkState(builderEndPointDefinitions == null, "endPointDefinitions was already set");
		//-----
		builderEndPointDefinitions = endPointDefinitions;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Object> build() {
		Assertion.checkNotNull(builderEndPointDefinitions, "endPointDefinitions must be set");
		//-----
		final Map<String, Object> swagger = new LinkedHashMap<>();
		swagger.put("swagger", 2.0);
		swagger.put("info", createInfoObject());
		swagger.put("basePath", builderContextPath);

		//host, basePath, schemes, consumes, produces
		swagger.put("paths", createPathsObject());
		//definitions, parameters, responses, security, tags, externalDocs
		putIfNotEmpty(swagger, "definitions", builderDefinitions);
		return swagger;
	}

	private Map<String, Object> createPathsObject() {
		final Map<String, Object> paths = new LinkedHashMap<>();
		for (final EndPointDefinition endPointDefinition : builderEndPointDefinitions) {
			final Map<String, Object> pathItem = (Map<String, Object>) paths.get(endPointDefinition.getPath());
			if (pathItem != null) {
				pathItem.putAll(createPathItemObject(endPointDefinition));
				paths.put(endPointDefinition.getPath(), pathItem);
			} else {
				paths.put(endPointDefinition.getPath(), createPathItemObject(endPointDefinition));
			}
		}
		return paths;
	}

	private Map<String, Object> createPathItemObject(final EndPointDefinition endPointDefinition) {
		final Map<String, Object> pathItem = new LinkedHashMap<>();
		pathItem.put(endPointDefinition.getVerb().name().toLowerCase(), createOperationObject(endPointDefinition));
		return pathItem;
	}

	private Map<String, Object> createOperationObject(final EndPointDefinition endPointDefinition) {
		final Map<String, Object> operation = new LinkedHashMap<>();
		operation.put("summary", endPointDefinition.getMethod().getName());
		final StringBuilder description = new StringBuilder();
		if (!endPointDefinition.getDoc().isEmpty()) {
			description.append(endPointDefinition.getDoc());
			description.append("<br/>");
		}
		if (endPointDefinition.isServerSideSave()) {
			description.append("This operation keep a full ServerSide state of returned object");
			description.append("<br/>");
		}
		putIfNotEmpty(operation, "description", description.toString());
		operation.put("operationId", endPointDefinition.getName());
		putIfNotEmpty(operation, "consumes", createConsumesArray(endPointDefinition));
		putIfNotEmpty(operation, "parameters", createParametersArray(endPointDefinition));
		putIfNotEmpty(operation, "responses", createResponsesObject(endPointDefinition));
		putIfNotEmpty(operation, "tags", createTagsArray(endPointDefinition));
		return operation;
	}

	private static void putIfNotEmpty(final Map<String, Object> entity, final String key, final Object value) {
		if (value instanceof List && ((List) value).isEmpty()) {
			return;
		}
		if (value != null) {
			entity.put(key, value);
		}
	}

	private Map<String, Object> createResponsesObject(final EndPointDefinition endPointDefinition) {
		final Map<String, Object> responses = new LinkedHashMap<>();
		final Map<String, Object> headers = createResponsesHeaders(endPointDefinition);

		final Type returnType = endPointDefinition.getMethod().getGenericReturnType();
		if (void.class.isAssignableFrom(endPointDefinition.getMethod().getReturnType())) {
			responses.put("204", createResponseObject("No content", returnType, headers));
		} else if (endPointDefinition.getMethod().getName().startsWith("create")) {
			responses.put("201", createResponseObject("Created", returnType, headers));
		} else {
			responses.put("200", createResponseObject("Success", returnType, headers));
		}
		if (!endPointDefinition.getEndPointParams().isEmpty()) {
			responses.put("400", createResponseObject("Bad request : parsing error (json, number, date, ...)", ErrorMessage.class, headers));
		}
		if (endPointDefinition.isNeedAuthentification()) {
			//endPointDefinition.isNeedSession() don't mean that session is mandatory, it just say to create a session
			responses.put("401", createResponseObject("Unauthorized : no valid session", ErrorMessage.class, headers));
			responses.put("403", createResponseObject("Forbidden : not enought rights", ErrorMessage.class, headers));
		}
		if (!endPointDefinition.getEndPointParams().isEmpty()) {
			responses.put("422", createResponseObject("Unprocessable entity : validations or business error", UiMessageStack.class, headers));
		}
		responses.put("429", createResponseObject("Too many request : anti spam security (must wait for next time window)", ErrorMessage.class, headers));
		responses.put("500", createResponseObject("Internal server error", ErrorMessage.class, headers));
		return responses;
	}

	private Map<String, Object> createResponsesHeaders(final EndPointDefinition endPointDefinition) {
		final Map<String, Object> headers = new LinkedHashMap<>();
		if (endPointDefinition.isAccessTokenPublish()) {
			headers.put("x-access-token", createSchemaObject(String.class));
		}
		return headers;
	}

	/**
	 * ErrorMessage for json converssion.
	 * @author npiedeloup
	 */
	static final class ErrorMessage {
		private final List<String> globalErrorMessages = Collections.emptyList();

		/**
		 * @return Error message
		 */
		public List<String> getGlobalErrorMessages() {
			return globalErrorMessages;
		}
	}

	private Map<String, Object> createResponseObject(final String description, final Type returnType, final Map<String, Object> headers) {
		final Map<String, Object> response = new LinkedHashMap<>();
		response.put("description", description);
		putIfNotEmpty(response, "schema", createSchemaObject(returnType));
		putIfNotEmpty(response, "headers", headers);
		return response;
	}

	private Map<String, Object> createSchemaObject(final Type type) {
		if (type == null) { //Si le type est null, on a pas réussi à récupérer la class : souvant dans le cas des generics
			return unknownObjectRef;
		}
		//-----
		final Map<String, Object> schema = new LinkedHashMap<>();
		final Class<?> objectClass = EndPointTypeUtil.castAsClass(type);
		final String[] typeAndFormat = toSwaggerType(objectClass);
		schema.put("type", typeAndFormat[0]);
		if (typeAndFormat[1] != null) {
			schema.put("format", typeAndFormat[1]);
		}
		if (EndPointTypeUtil.isAssignableFrom(void.class, type)) {
			return null;
		} else if (EndPointTypeUtil.isAssignableFrom(List.class, type)) {
			final Type itemsType = ((ParameterizedType) type).getActualTypeArguments()[0]; //we known that List has one parameterized type
			//Si le itemsType est null, on prend le unknownObject
			schema.put("items", createSchemaObject(itemsType));
		} else if ("object".equals(typeAndFormat[0])) {
			final String objectName;
			final Class<?> parameterClass;
			if (type instanceof ParameterizedType
					&& (((ParameterizedType) type).getActualTypeArguments().length == 1 || FacetedQueryResult.class.isAssignableFrom(objectClass))
					&& !(((ParameterizedType) type).getActualTypeArguments()[0] instanceof WildcardType)) {
				//We have checked there is one parameter or we known that FacetedQueryResult has two parameterized type
				final Type itemsType = ((ParameterizedType) type).getActualTypeArguments()[0];
				parameterClass = EndPointTypeUtil.castAsClass(itemsType);
				objectName = objectClass.getSimpleName() + "&lt;" + parameterClass.getSimpleName() + "&gt;";
			} else {
				objectName = objectClass.getSimpleName();
				parameterClass = null;
			}
			schema.put("$ref", "#/definitions/" + objectName);
			schema.remove("type");
			if (!builderDefinitions.containsKey(objectName)) {
				final Map<String, Object> definition = new LinkedHashMap<>();
				builderDefinitions.put(objectName, definition); //we put definitions first to avoid infinite resolution loop
				if (DtObject.class.isAssignableFrom(objectClass)) {
					final Class<? extends DtObject> dtClass = (Class<? extends DtObject>) objectClass;
					appendPropertiesDtObject(definition, dtClass);
				} else {
					appendPropertiesObject(definition, objectClass, parameterClass);
				}
			}
		}
		return schema;
	}

	private void appendPropertiesDtObject(final Map<String, Object> entity, final Class<? extends DtObject> objectClass) {
		//can't be a primitive nor array nor DtListDelta
		final Map<String, Object> properties = new LinkedHashMap<>();
		final List<String> required = new ArrayList<>(); //mandatory fields
		final DtDefinition dtDefinition = DtObjectUtil.findDtDefinition(objectClass);
		for (final DtField dtField : dtDefinition.getFields()) {
			final String fieldName = StringUtil.constToLowerCamelCase(dtField.getName());
			final Type fieldType = getFieldType(dtField);
			final Map<String, Object> fieldSchema = createSchemaObject(fieldType);
			fieldSchema.put("title", dtField.getLabel().getDisplay());
			if (dtField.isNotNull()) {
				required.add(fieldName);
			}
			//could add enum on field to specify all values authorized
			properties.put(fieldName, fieldSchema);
		}
		putIfNotEmpty(entity, "required", required);
		putIfNotEmpty(entity, "properties", properties);
	}

	private Type getFieldType(final DtField dtField) {
		final DataType dataType = dtField.getDomain().getDataType();
		if (DataType.DtObject == dataType) {
			return ClassUtil.classForName(dtField.getDomain().getDtDefinition().getClassCanonicalName());
		} else if (DataType.DtList == dataType) {
			final Class<?> dtClass = ClassUtil.classForName(dtField.getDomain().getDtDefinition().getClassCanonicalName());
			return new CustomParameterizedType(DtList.class, dtClass);
		}
		return dataType.getJavaClass();
	}

	private void appendPropertiesObject(final Map<String, Object> entity, final Type type, final Class<? extends Object> parameterClass) {
		final Class<?> objectClass = EndPointTypeUtil.castAsClass(type);
		//can't be a primitive nor array nor DtListDelta
		final Map<String, Object> properties = new LinkedHashMap<>();
		final List<String> requireds = new ArrayList<>(); //mandatory fields
		for (final Field field : objectClass.getDeclaredFields()) {
			if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT | 0x1000)) == 0) { //0x1000 is for synthetic field (excludes)
				final Map<String, Object> fieldSchema = obtainFieldSchema(field, parameterClass, requireds);
				properties.put(field.getName(), fieldSchema);
			}
		}
		putIfNotEmpty(entity, "required", requireds);
		putIfNotEmpty(entity, "properties", properties);
	}

	private Map<String, Object> obtainFieldSchema(final Field field, final Class<? extends Object> parameterClass, final List<String> requireds) {
		final Type fieldType = field.getGenericType();
		Type usedFieldType = fieldType;
		if (fieldType instanceof ParameterizedType) {
			final Type[] actualTypeArguments = ((ParameterizedType) fieldType).getActualTypeArguments();
			if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof TypeVariable) {
				usedFieldType = new CustomParameterizedType(fieldType, parameterClass);
			}
		} else if (fieldType instanceof TypeVariable) {
			usedFieldType = parameterClass;
		}
		final Map<String, Object> fieldSchema = createSchemaObject(usedFieldType);
		if ((field.getModifiers() & Modifier.FINAL) != 0
				&& !Option.class.isAssignableFrom(field.getType())) {
			requireds.add(field.getName());
		}
		return fieldSchema;
	}

	private static List<String> createTagsArray(final EndPointDefinition endPointDefinition) {
		final List<String> tags = new ArrayList<>();
		tags.add(endPointDefinition.getMethod().getDeclaringClass().getSimpleName());
		return tags;
	}

	private static List<String> createConsumesArray(final EndPointDefinition endPointDefinition) {
		if (endPointDefinition.getEndPointParams().isEmpty()) {
			return Collections.emptyList();
		}
		return Collections.singletonList(endPointDefinition.getAcceptType());
	}

	private List<Map<String, Object>> createParametersArray(final EndPointDefinition endPointDefinition) {
		final Map<String, Object> bodyParameter = new LinkedHashMap<>();
		final List<Map<String, Object>> parameters = new ArrayList<>();
		for (final EndPointParam endPointParam : endPointDefinition.getEndPointParams()) {
			if (endPointParam.getParamType() != RestParamType.Implicit) {//if implicit : no public parameter
				appendParameters(endPointParam, endPointDefinition, parameters, bodyParameter);
			}
		}

		if (endPointDefinition.isAccessTokenMandatory()) {
			final Map<String, Object> parameter = new LinkedHashMap<>();
			parameter.put("name", "x-access-token");
			parameter.put("in", "header");
			parameter.put("description", "Security access token, must be sent to allow operation.");
			parameter.put("required", "true");
			parameter.put("type", "string");
			parameters.add(parameter);
		}
		if (!bodyParameter.isEmpty()) {
			final String bodyName = StringUtil.constToUpperCamelCase(endPointDefinition.getName().replaceAll("__", "_")) + "Body";
			final Map<String, Object> compositeSchema = (Map<String, Object>) bodyParameter.get("schema");
			bodyParameter.put("schema", Collections.singletonMap("$ref", bodyName));
			final Map<String, Object> bodyDefinition = new LinkedHashMap<>();
			bodyDefinition.put("required", compositeSchema.keySet().toArray(new String[compositeSchema.size()]));
			putIfNotEmpty(bodyDefinition, "properties", compositeSchema);
			builderDefinitions.put(bodyName, bodyDefinition);

			parameters.add(0, bodyParameter);
		}

		return parameters;
	}

	private void appendParameters(final EndPointParam endPointParam, final EndPointDefinition endPointDefinition, final List<Map<String, Object>> parameters, final Map<String, Object> bodyParameter) {
		if (isOneInMultipleOutParams(endPointParam)) {
			for (final EndPointParam pseudoEndPointParam : createPseudoEndPointParams(endPointParam)) {
				final Map<String, Object> parameter = createParameterObject(pseudoEndPointParam, endPointDefinition);
				parameter.remove("required"); //query params aren't required
				parameters.add(parameter);
			}
		} else if (isMultipleInOneOutParams(endPointParam)) {
			final Map<String, Object> parameter = createParameterObject(endPointParam, endPointDefinition);
			if (bodyParameter.isEmpty()) {
				bodyParameter.putAll(parameter);
			} else {
				final String newDescription = (String) parameter.get("description");
				final String oldDescription = (String) bodyParameter.get("description");
				bodyParameter.put("description", oldDescription + ", " + newDescription);

				final Map<String, Object> newSchema = (Map<String, Object>) parameter.get("schema");
				final Map<String, Object> oldSchema = (Map<String, Object>) bodyParameter.get("schema");
				oldSchema.putAll(newSchema);
			}
		} else {
			final Map<String, Object> parameter = createParameterObject(endPointParam, endPointDefinition);
			parameters.add(parameter);
		}
	}

	private List<EndPointParam> createPseudoEndPointParams(final EndPointParam endPointParam) {
		final List<EndPointParam> pseudoEndPointParams = new ArrayList<>();
		final String prefix = (!endPointParam.getName().isEmpty()) ? (endPointParam.getName() + ".") : "";
		if (UiListState.class.isAssignableFrom(endPointParam.getType())) {
			pseudoEndPointParams.add(new EndPointParamBuilder(int.class)
					.with(endPointParam.getParamType(), prefix + "top").build());
			pseudoEndPointParams.add(new EndPointParamBuilder(int.class)
					.with(endPointParam.getParamType(), prefix + "skip").build());
			pseudoEndPointParams.add(new EndPointParamBuilder(String.class)
					.with(endPointParam.getParamType(), prefix + "sortFieldName").build());
			pseudoEndPointParams.add(new EndPointParamBuilder(boolean.class)
					.with(endPointParam.getParamType(), prefix + "sortDesc").build());
		} else if (DtObject.class.isAssignableFrom(endPointParam.getType())) {
			final Class<? extends DtObject> paramClass = (Class<? extends DtObject>) endPointParam.getType();
			final DtDefinition dtDefinition = DtObjectUtil.findDtDefinition(paramClass);
			for (final DtField dtField : dtDefinition.getFields()) {
				final String fieldName = StringUtil.constToLowerCamelCase(dtField.name());
				pseudoEndPointParams.add(new EndPointParamBuilder(dtField.getDomain().getDataType().getJavaClass())
						.with(endPointParam.getParamType(), prefix + fieldName)
						.build());
			}
		}
		return pseudoEndPointParams;
	}

	private static boolean isOneInMultipleOutParams(final EndPointParam endPointParam) {
		final Class<?> paramClass = endPointParam.getType();
		return endPointParam.getParamType() == RestParamType.Query && (UiListState.class.isAssignableFrom(paramClass) || DtObject.class.isAssignableFrom(paramClass));
	}

	private static boolean isMultipleInOneOutParams(final EndPointParam endPointParam) {
		return endPointParam.getParamType() == RestParamType.InnerBody;
	}

	private Map<String, Object> createParameterObject(final EndPointParam endPointParam, final EndPointDefinition endPointDefinition) {

		final String inValue;
		final String nameValue;
		String description = null;
		switch (endPointParam.getParamType()) {
			case Body:
				inValue = "body";
				nameValue = "body";
				break;
			case InnerBody:
				inValue = "body";
				nameValue = "body"; //only one body parameter is accepted : must append in body
				description = "InnerBody:" + endPointParam.getName();
				break;
			case Path:
				inValue = "path";
				nameValue = endPointParam.getName();
				break;
			case Query:
				inValue = endPointDefinition.getVerb() == Verb.GET ? "query" : "formData";
				nameValue = endPointParam.getName();
				break;
			case Header:
				inValue = "header";
				nameValue = endPointParam.getName();
				break;
			case Implicit://must be escape before
			default:
				throw new RuntimeException("Unsupported type : " + endPointParam.getParamType());
		}

		final Map<String, Object> parameter = new LinkedHashMap<>();
		parameter.put("name", nameValue);
		parameter.put("in", inValue);
		putIfNotEmpty(parameter, "description", description);
		parameter.put("required", "true");
		if (endPointParam.getParamType() == RestParamType.Body) {
			parameter.put("schema", createSchemaObject(endPointParam.getGenericType()));
		} else if (endPointParam.getParamType() == RestParamType.InnerBody) {
			final Map<String, Object> bodyParameter = new LinkedHashMap<>();
			bodyParameter.put(endPointParam.getName(), createSchemaObject(endPointParam.getGenericType()));
			parameter.put("schema", bodyParameter);
		} else {
			final String[] typeAndFormat = toSwaggerType(endPointParam.getType());
			parameter.put("type", typeAndFormat[0]);
			if (typeAndFormat[1] != null) {
				parameter.put("format", typeAndFormat[1]);
			}
			//items, collectionFormat, default, maximum, ...
		}
		return parameter;
	}

	private static String[] toSwaggerType(final Class paramClass) {
		if (String.class.isAssignableFrom(paramClass)) {
			return new String[] { "string", null };
		} else if (boolean.class.isAssignableFrom(paramClass) || Boolean.class.isAssignableFrom(paramClass)) {
			return new String[] { "boolean", null };
		} else if (int.class.isAssignableFrom(paramClass) || Integer.class.isAssignableFrom(paramClass)) {
			return new String[] { "integer", "int32" };
		} else if (long.class.isAssignableFrom(paramClass) || Long.class.isAssignableFrom(paramClass)) {
			return new String[] { "integer", "int64" };
		} else if (float.class.isAssignableFrom(paramClass) || Float.class.isAssignableFrom(paramClass)) {
			return new String[] { "integer", "int32" };
		} else if (double.class.isAssignableFrom(paramClass) || Double.class.isAssignableFrom(paramClass)) {
			return new String[] { "integer", "int64" };
		} else if (Date.class.isAssignableFrom(paramClass)) {
			return new String[] { "string", "date-time" };
		} else if (VFile.class.isAssignableFrom(paramClass)) {
			return new String[] { "file", null };
		} else if (List.class.isAssignableFrom(paramClass) || Collection.class.isAssignableFrom(paramClass)) {
			return new String[] { "array", null };
		} else {
			return new String[] { "object", null };
		}
	}

	private static Map<String, Object> createInfoObject() {
		final Map<String, Object> infoObject = new LinkedHashMap<>();
		infoObject.put("title", "MySwaggerAPI Tester");
		//description, termOfService, contact
		infoObject.put("license", createLicense());
		infoObject.put("version", "1.0");
		return infoObject;
	}

	private static Map<String, Object> createLicense() {
		final Map<String, Object> licence = new LinkedHashMap<>();
		licence.put("name", "Apache 2.0");
		licence.put("url", "http://www.apache.org/licenses/LICENSE-2.0.html");
		return licence;
	}

	private static final class CustomParameterizedType implements ParameterizedType {
		private final Type fieldType;
		private final Type[] typeArguments;

		CustomParameterizedType(final Type fieldType, final Type paramType) {
			this.fieldType = fieldType;
			typeArguments = new Type[] { paramType };
		}

		/** {@inheritDoc} */
		@Override
		public Type[] getActualTypeArguments() {
			return typeArguments;
		}

		/** {@inheritDoc} */
		@Override
		public Type getOwnerType() {
			return fieldType instanceof ParameterizedType ? ((ParameterizedType) fieldType).getOwnerType() : null;
		}

		/** {@inheritDoc} */
		@Override
		public Type getRawType() {
			return fieldType instanceof ParameterizedType ? ((ParameterizedType) fieldType).getRawType() : fieldType;
		}
	}

}