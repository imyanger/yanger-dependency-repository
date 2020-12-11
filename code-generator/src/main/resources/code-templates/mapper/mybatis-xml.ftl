<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
		"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${daoInterfacePackage}.${daoInterfaceName}">

	<resultMap id="BaseResultMap" type="${modelPackage}.${modelName}">
        <#list columns as column >
	        <#if pkColumnName == column.columnName>
		<id column="${column.columnName}" property="${column.fieldName}" />
	        <#else >
		<result column="${column.columnName}" property="${column.fieldName}" />
	        </#if>
        </#list>
	</resultMap>

	<sql id="Base_Column_List">
        <#list columns as column >
		${column.columnName}<#if column_has_next>,</#if>
        </#list>
	</sql>

	<insert id="insert" useGeneratedKeys="true" keyColumn="id" keyProperty="id" parameterType="${modelPackage}.${modelName}">
		INSERT INTO `${tableName}`
		<trim prefix="(" suffix=")" suffixOverrides=",">
			<#list columns as column >
                <#if pkColumnName != column.columnName>
			${r"<if test='null != "}${column.fieldName}${r"'>"}
				${column.columnName}<#if column_has_next>,</#if>
			${r"</if>"}
				</#if>
            </#list>
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides=",">
            <#list columns as column >
                <#if pkColumnName != column.columnName>
			${r"<if test='null != "}${column.fieldName}${r"'>"}
				${r"#{"}${column.fieldName}${r"}"}<#if column_has_next>,</#if>
			${r"</if>"}
                </#if>
            </#list>
		</trim>
	</insert>

	<insert id="batchInsert" useGeneratedKeys="true" keyColumn="id" keyProperty="id" parameterType="list">
		INSERT INTO `${tableName}`
			( <include refid="Base_Column_List"/> )
		VALUES
		<foreach collection="list" item="entity" index="index" separator=",">
			(
            <#list columns as column >
	            ${r"#{entity."}${column.fieldName}${r"}"}<#if column_has_next>,</#if>
            </#list>
			)
		</foreach>
	</insert>

	<update id="delete" parameterType="${modelPackage}.${modelName}">
		DELETE FROM `${tableName}`
		<where>
            <#list columns as column >
                <#if pkColumnName != column.columnName>
			${r"<if test='null != "}${column.fieldName}${r"'>"}
				${r"AND "}${column.columnName}${r" = #{"}${column.fieldName}${r"}"}
			${r"</if>"}
                </#if>
            </#list>
		</where>
	</update>

	<delete id="deleteById" parameterType="long">
		DELETE FROM `${tableName}` WHERE ${pkColumnName}${r" = #{"}${pkFieldName}${r"}"}
	</delete>

	<delete id="deleteByIds" parameterType="list">
		DELETE FROM `${tableName}`
		WHERE ${pkColumnName} in
		(
			<foreach collection="ids" item="id" index="index" separator=",">
				${r"#{id}"}
			</foreach>
		)
	</delete>

	<update id="update" parameterType="${modelPackage}.${modelName}">
		UPDATE `${tableName}`
		<set>
			<#list columns as column >
				<#if pkColumnName != column.columnName>
			${column.columnName}${r" = #{"}${column.fieldName}${r"}"}<#if column_has_next>,</#if>
				</#if>
			</#list>
		</set>
		WHERE ${pkColumnName}${r" = #{"}${pkFieldName}${r"}"}
	</update>

	<update id="updateSelective" parameterType="${modelPackage}.${modelName}">
		UPDATE `${tableName}`
		<set>
			<#list columns as column >
				<#if pkColumnName != column.columnName>
			${r"<if test='null != "}${column.fieldName}${r"'>"}
				${column.columnName}${r" = #{"}${column.fieldName}${r"}"}<#if column_has_next>,</#if>
			${r"</if>"}
				</#if>
			</#list>
		</set>
		WHERE ${pkColumnName}${r" = #{"}${pkFieldName}${r"}"}
	</update>

	<select id="select" resultMap="BaseResultMap" parameterType="${modelPackage}.${modelName}">
		SELECT
			<include refid="Base_Column_List"/>
		FROM `${tableName}`
		<where>
			<#list columns as column >
				<#if pkColumnName != column.columnName>
			${r"<if test='null != "}${column.fieldName}${r"'>"}
				${r"AND "}${column.columnName}${r" = #{"}${column.fieldName}${r"}"}
			${r"</if>"}
				</#if>
			</#list>
		</where>
	</select>

	<select id="selectById" resultMap="BaseResultMap">
		SELECT
			<include refid="Base_Column_List"/>
		FROM `${tableName}`
		WHERE ${pkColumnName}${r" = #{"}${pkFieldName}${r"}"}
	</select>

	<select id="selectByIds" resultMap="BaseResultMap">
		SELECT
			<include refid="Base_Column_List"/>
		FROM `${tableName}`
		WHERE id in
		(
			<foreach collection="ids" item="id" index="index" separator=",">
				${r"#{id}"}
			</foreach>
		)
	</select>

	<select id="getCount" resultType="java.lang.Integer" parameterType="${modelPackage}.${modelName}">
		SELECT count(1) FROM `${tableName}`
		<where>
			<#list columns as column >
				<#if pkColumnName != column.columnName>
			${r"<if test='null != "}${column.fieldName}${r"'>"}
				${r"AND "}${column.columnName}${r" = #{"}${column.fieldName}${r"}"}
			${r"</if>"}
				</#if>
			</#list>
		</where>
	</select>

	<select id="getTotalCount" resultType="java.lang.Integer">
		SELECT count(1) FROM `${tableName}`
	</select>

</mapper>