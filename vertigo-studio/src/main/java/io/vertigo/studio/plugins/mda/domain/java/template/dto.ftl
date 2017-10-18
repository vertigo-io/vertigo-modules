package ${dtDefinition.packageName};

import ${dtDefinition.stereotypePackageName};
<#if dtDefinition.entity || dtDefinition.fragment>
import io.vertigo.dynamo.domain.model.URI;
</#if>
<#if dtDefinition.containsAccessor()>
import io.vertigo.dynamo.domain.model.VAccessor;
</#if>	
<#if dtDefinition.containsEnumAccessor()>
import io.vertigo.dynamo.domain.model.EnumVAccessor;
</#if>	
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
<#list annotations(dtDefinition.dtDefinition) as annotation>
${annotation}
</#list>
public final class ${dtDefinition.classSimpleName} implements ${dtDefinition.stereotypeInterfaceName} {
	private static final long serialVersionUID = 1L;

	<#list dtDefinition.fields as dtField>
		<#if dtField.foreignKey>
			<#list annotations(dtField.association.definition) as annotation>
	${annotation}
			</#list>
			<#if dtField.association.targetStaticMasterData>
	private final EnumVAccessor<${dtField.association.returnType}, ${dtField.association.returnType}Enum> ${dtField.upperCamelCaseName?uncap_first}Accessor = new EnumVAccessor<>(${dtField.association.returnType}.class, "${dtField.association.role?uncap_first}", ${dtField.association.returnType}Enum.class);
			<#else>
	private final VAccessor<${dtField.association.returnType}> ${dtField.upperCamelCaseName?uncap_first}Accessor = new VAccessor<>(${dtField.association.returnType}.class, "${dtField.association.role?uncap_first}");
			</#if>
		<#else>
	private ${dtField.javaType} ${dtField.upperCamelCaseName?uncap_first};
		</#if>

	</#list>
	<#list dtDefinition.associations as association>
		<#if association.navigable>
			<#if association.multiple>
	private io.vertigo.dynamo.domain.model.DtList<${association.returnType}> ${association.role?uncap_first};
			</#if>
		</#if>
	</#list>
	<#if dtDefinition.entity>

	/** {@inheritDoc} */
		<#list annotations("URI") as annotation>
	${annotation}
		</#list>
	@Override
	public URI<${dtDefinition.classSimpleName}> getURI() {
		return DtObjectUtil.createURI(this);
	}
	</#if>
	<#if dtDefinition.fragment>

	/** {@inheritDoc} */
		<#list annotations("URI") as annotation>
	${annotation}
		</#list>
	@Override
	public URI<${dtDefinition.entityClassSimpleName}> getEntityURI() {
		return DtObjectUtil.createEntityURI(this); 
	}
	</#if>

	<#list dtDefinition.fields as dtField>
	
	<#if dtField.foreignKey>
	/**
	 * Champ : ${dtField.type}.
	 * Récupère la valeur de la propriété '${dtField.display}'.
	 * @return ${dtField.javaType} ${dtField.upperCamelCaseName?uncap_first}<#if dtField.required> <b>Obligatoire</b></#if>
	 */
		<#list annotations(dtField) as annotation>
	${annotation}
		</#list>
	@Deprecated
	public ${dtField.javaType} get${dtField.upperCamelCaseName}() {
		return (${dtField.javaType})  ${dtField.upperCamelCaseName?uncap_first}Accessor.getId();
	}

	/**
	 * Champ : ${dtField.type}.
	 * Définit la valeur de la propriété '${dtField.display}'.
	 * @param ${dtField.upperCamelCaseName?uncap_first} ${dtField.javaType}<#if dtField.required> <b>Obligatoire</b></#if>
	 */
	@Deprecated
	public void set${dtField.upperCamelCaseName}(final ${dtField.javaType} ${dtField.upperCamelCaseName?uncap_first}) {
		${dtField.upperCamelCaseName?uncap_first}Accessor.setId(${dtField.upperCamelCaseName?uncap_first});
	}
	
	</#if>
	
	/**
	 * Champ : ${dtField.type}.
	 * Récupère la valeur de la propriété '${dtField.display}'.
	 * @return ${dtField.javaType} ${dtField.upperCamelCaseName?uncap_first}<#if dtField.required> <b>Obligatoire</b></#if>
	 */
		<#list annotations(dtField) as annotation>
	${annotation}
		</#list>
	public ${dtField.javaType} get${dtField.upperCamelCaseName}() {
		return ${dtField.upperCamelCaseName?uncap_first};
	}

	/**
	 * Champ : ${dtField.type}.
	 * Définit la valeur de la propriété '${dtField.display}'.
	 * @param ${dtField.upperCamelCaseName?uncap_first} ${dtField.javaType}<#if dtField.required> <b>Obligatoire</b></#if>
	 */
	<#if dtField.foreignKey>@Deprecated</#if>
	public void set${dtField.upperCamelCaseName}(final ${dtField.javaType} ${dtField.upperCamelCaseName?uncap_first}) {
		this.${dtField.upperCamelCaseName?uncap_first} = ${dtField.upperCamelCaseName?uncap_first};
	}
	
	</#list>
	<#list dtDefinition.dtComputedFields as dtField>
	/**
	 * Champ : ${dtField.type}.
	 * Récupère la valeur de la propriété calculée '${dtField.display}'.
	 * @return ${dtField.javaType} ${dtField.upperCamelCaseName?uncap_first}<#if dtField.required> <b>Obligatoire</b></#if>
	 */
		<#list annotations(dtField) as annotation>
	${annotation}
		</#list>
	public ${dtField.javaType} get${dtField.upperCamelCaseName}() {
		${dtField.javaCode}
	}

	</#list>
	<#if dtDefinition.associations?has_content>
		<#list dtDefinition.associations as association>
			<#if association.navigable>
	
				<#if association.multiple>
	/**
	 * Association : ${association.label}.
	 * @return io.vertigo.dynamo.domain.model.DtList<${association.returnType}>
	 */
	public io.vertigo.dynamo.domain.model.DtList<${association.returnType}> get${association.role?cap_first}List() {
		// On doit avoir une clé primaire renseignée. Si ce n'est pas le cas, on renvoie une liste vide
		if (io.vertigo.dynamo.domain.util.DtObjectUtil.getId(this) == null) {
			return new io.vertigo.dynamo.domain.model.DtList<>(${association.returnType}.class);
		}
		final io.vertigo.dynamo.domain.model.DtListURI fkDtListURI = get${association.role?cap_first}DtListURI();
		io.vertigo.lang.Assertion.checkNotNull(fkDtListURI);
		//---------------------------------------------------------------------
		//On est toujours dans un mode lazy.
		if (${association.role?uncap_first} == null) {
			${association.role?uncap_first} = io.vertigo.app.Home.getApp().getComponentSpace().resolve(io.vertigo.dynamo.store.StoreManager.class).getDataStore().findAll(fkDtListURI);
		}
		return ${association.role?uncap_first};
	}

	/**
	 * Association URI: ${association.label}.
	 * @return URI de l'association
	 */
					
	public io.vertigo.dynamo.domain.metamodel.association.DtListURIFor<#if association.simple>Simple<#else>NN</#if>Association get${association.role?cap_first}DtListURI() {
		return io.vertigo.dynamo.domain.util.DtObjectUtil.createDtListURIFor<#if association.simple>Simple<#else>NN</#if>Association(this, "${association.urn}", "${association.role}");
	}

				<#else>
	 /**
	 * Association : ${association.label}.
	 * @return l'accesseur vers la propriété '${association.label}'
	 */
	 <#list annotations('transientField') as annotation>
	${annotation}
	</#list>
<#if association.targetStaticMasterData>
	public EnumVAccessor<${association.returnType}, ${association.returnType}Enum> get${association.role?cap_first}Accessor() {
<#else>
	public VAccessor<${association.returnType}> get${association.role?cap_first}Accessor() {
</#if>
		return ${association.upperCamelCaseFkFieldName?uncap_first}Accessor;
	}
	
	@Deprecated
	public ${association.returnType} get${association.role?cap_first}() {
		return ${association.upperCamelCaseFkFieldName?uncap_first}Accessor.get();
	}

	/**
	 * Retourne l'URI: ${association.label}.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.URI<${association.returnType}> get${association.role?cap_first}URI() {
		return ${association.upperCamelCaseFkFieldName?uncap_first}Accessor.getURI();
	}

				</#if>
			</#if>
		</#list>
	</#if>
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
