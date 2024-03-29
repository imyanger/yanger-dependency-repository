package com.yanger.starter.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.jetbrains.annotations.NotNull;

/**
 * 抽象的 插入一条数据（选择字段插入）
 * @Author yanger
 * @Date 2021/1/28 18:04
 */
@RequiredArgsConstructor
public class AbstractInsertMethod extends AbstractMethod {

    /** Sql method */
    private final MybatisSqlMethod sqlMethod;

    /**
     * Inject mapped statement mapped statement
     * @param mapperClass mapper class
     * @param modelClass  model class
     * @param tableInfo   table info
     * @return the mapped statement
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, @NotNull TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();

        // 主键处理
        String idPropertyStr = "";
        String idColumnStr = "";
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            idPropertyStr = "<if test=\"" + tableInfo.getKeyProperty() + " != null\">" + tableInfo.getKeyColumn() + ",</if>\n";
            idColumnStr = "<if test=\"" + tableInfo.getKeyProperty() + " != null\">#{" + tableInfo.getKeyProperty() + "},</if>\n";
        }

        String columnScript = SqlScriptUtils.convertTrim(idPropertyStr + tableInfo.getAllInsertSqlColumnMaybeIf(),
                                                         LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String valuesScript = SqlScriptUtils.convertTrim(idColumnStr + tableInfo.getAllInsertSqlPropertyMaybeIf(null),
                                                         LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        // if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
        //     if (tableInfo.getIdType() == IdType.AUTO) {
        //         // 自增主键
        //         keyGenerator = new Jdbc3KeyGenerator();
        //         keyProperty = tableInfo.getKeyProperty();
        //         keyColumn = tableInfo.getKeyColumn();
        //     } else {
        //         if (null != tableInfo.getKeySequence()) {
        //             keyGenerator = TableInfoHelper.genKeyGenerator(this.sqlMethod.getMethod(), tableInfo, this.builderAssistant);
        //             keyProperty = tableInfo.getKeyProperty();
        //             keyColumn = tableInfo.getKeyColumn();
        //         }
        //     }
        // }
        String sql = String.format(this.sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.sqlMethod.getMethod(), sqlSource, keyGenerator, keyProperty, keyColumn);
    }

}
