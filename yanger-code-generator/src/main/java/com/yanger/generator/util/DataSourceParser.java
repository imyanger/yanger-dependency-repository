package com.yanger.generator.util;

import com.yanger.generator.entity.config.DataSourceConfig;
import com.yanger.generator.entity.sql.TableInfo;
import com.yanger.generator.enums.CodeNameCase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 数据库解析工具
 * @Author yanger
 * @Date 2020/11/18 16:08
 */
@Slf4j
public class DataSourceParser {

    /**
     * @Description 根据数据源获取数据库表结构信息
     * @Author yanger
     * @Date 2020/11/18 19:17
     * @param: dbConfig
     * @param: nameCase
     * @param: tinyintTransType
     * @return: java.util.List<com.yanger.generator.entity.sql.TableInfo>
     */
    public static List<TableInfo> parse(DataSourceConfig dbConfig, String nameCase, String tinyintTransType) {
        CodeNameCase codeNameCase = Arrays.stream(CodeNameCase.values())
            .filter(s -> s.getValue().equalsIgnoreCase(nameCase) || s.getCode().toString().equals(nameCase)).findFirst().orElse(CodeNameCase.CAMEL);
        return parse(dbConfig, codeNameCase, tinyintTransType);
    }

    /**
     * @Description 根据数据源获取数据库表结构信息
     * @Author yanger
     * @Date 2020/11/18 19:17
     * @param: dbConfig
     * @param: codeNameCase
     * @param: tinyintTransType
     * @return: java.util.List<com.yanger.generator.entity.sql.TableInfo>
     */
    public static List<TableInfo> parse(DataSourceConfig dbConfig, CodeNameCase codeNameCase, String tinyintTransType) {
        List<TableInfo> tableInfos = new ArrayList<>(0);
        Connection conn = DatabaseUtils.getConnection(dbConfig.getDriverName(), dbConfig.getUrl(), dbConfig.getUserName(), dbConfig.getPassword());
        if (conn == null) {
            return null;
        }
        String dbName = DatabaseUtils.getDbName(conn);
        List<String> tables = dbConfig.getTables();
        Map<String, String> allTableNamesMap = DatabaseUtils.getAllTableNamesMap(conn, dbConfig.getSchema());
        for (String table : tables) {
            if (allTableNamesMap.containsKey(table.toLowerCase())) {
                String tableName = allTableNamesMap.get(table.toLowerCase());
                TableInfo tableInfo = DatabaseUtils.getTableInfo(conn, dbConfig.getSchema(), tableName, codeNameCase, tinyintTransType);
                tableInfos.add(tableInfo);
            }
        }
        return tableInfos;
    }

}

