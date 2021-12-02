package com.yanger.generator.util;

import com.yanger.generator.entity.sql.ColumnInfo;
import com.yanger.generator.entity.sql.TableInfo;
import com.yanger.generator.enums.CodeNameCase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库处理工具类
 * @Author yanger
 * @Date 2020/11/18 16:22
 */
@Slf4j
public class DatabaseUtils {

    /**
     * 获取数据库连接
     * @Author yanger
     * @Date 2020/11/18 16:27
     * @param: driverName
     * @param: url
     * @param: userName
     * @param: password
     * @return: java.sql.Connection
     */
    public static Connection getConnection(String driverName, String url, String userName, String password) {
        try {
            Class.forName(driverName);
            Properties props = new Properties();
            props.setProperty("user", userName);
            props.setProperty("password", password);
            // 设置可以获取remarks信息
            props.setProperty("remarks", "true");
            // 设置可以获取tables
            props.setProperty("useInformationSchema", "true");
            return DriverManager.getConnection(url, props);
        } catch (ClassNotFoundException e) {
            log.error("获取驱动类{}异常", driverName, e);
        } catch (SQLException e) {
            log.error("获取连接异常", e);
        }
        return null;
    }

    /**
     * 获取数据库产品名称
     * @Author yanger
     * @Date 2020/11/18 16:27
     * @param: conn
     * @return: java.lang.String
     */
    public static String getDbName(Connection conn) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            log.info("当前数据库：{}，版本：{}", metaData.getDatabaseProductName(), metaData.getDatabaseProductVersion());
            return metaData.getDatabaseProductName();
        } catch (Exception e) {
            log.warn("获取数据库产品名称异常", e);
            return null;
        }
    }

    /**
     * 获取所有的表
     * @Author yanger
     * @Date 2020/11/18 19:16
     * @param: conn
     * @param: schema
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String, String> getAllTableNamesMap(Connection conn, String schema) {
        Map<String, String> result = new HashMap<>();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, schema, null, new String[] { "TABLE" });
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                result.put(tableName.toLowerCase(), tableName);
            }
            rs.close();
        } catch (SQLException e) {
            log.warn("获取数据库表异常", e);
        }
        return result;
    }

    /**
     * 组织表信息
     * @Author yanger
     * @Date 2020/11/18 19:16
     * @param: conn
     * @param: schema
     * @param: tableName
     * @param: codeNameCase
     * @param: tinyintTransType
     * @return: com.yanger.generator.entity.sql.TableInfo
     */
    public static TableInfo getTableInfo(Connection conn, String schema, String tableName, CodeNameCase codeNameCase, String tinyintTransType) {
        TableInfo tableInfo = null;
        try {
            // 获取表基本信息
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, schema, tableName, new String[] { "TABLE" });
            if (rs.next()) {
                String tableComment = rs.getString("REMARKS");
                String className = StrUtils.upperCaseFirst(StrUtils.underlineToCamelCase(tableName));
                tableInfo = TableInfo.builder().tableName(tableName).className(className).tableComment(tableComment).build();
            }
            rs.close();
        } catch (SQLException e) {
            log.warn("{}", e);
        }
        if (tableInfo != null) {
            tableInfo.setColumns(getTableColumns(conn, schema, tableName, codeNameCase, tinyintTransType));
            List<String> tablePrimaryKeys = getTablePrimaryKeys(conn, schema, tableName);
            List<ColumnInfo> pkColumns = tableInfo.getColumns().stream().filter(s -> tablePrimaryKeys.contains(s.getColumnName())).collect(Collectors.toList());
            tableInfo.setPkColumns(pkColumns);
        }
        return tableInfo;
    }

    /**
     * 组织列信息
     * @Author yanger
     * @Date 2020/11/18 19:16
     * @param: conn
     * @param: schema
     * @param: tableName
     * @param: codeNameCase
     * @param: tinyintTransType
     * @return: java.util.List<com.yanger.generator.entity.sql.ColumnInfo>
     */
    private static List<ColumnInfo> getTableColumns(Connection conn, String schema, String tableName, CodeNameCase codeNameCase, String tinyintTransType) {
        List<ColumnInfo> columns = new ArrayList<>(0);
        try {
            // 获取字段基本信息
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(null, schema, tableName, null);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String typeName = rs.getString("TYPE_NAME");
                String remarks = rs.getString("REMARKS");
                // int column_size = rs.getInt("COLUMN_SIZE");
                // boolean nullable = rs.getBoolean("NULLABLE");
                // String columnDef = rs.getString("COLUMN_DEF");
                // boolean isAutoincrement = rs.getBoolean("IS_AUTOINCREMENT");
                // field Name
                String fieldName = "";
                if(CodeNameCase.CAMEL.equals(codeNameCase)) {
                    fieldName = StrUtils.lowerCaseFirst(StrUtils.underlineToCamelCase(columnName));
                } else if (CodeNameCase.UNDER_LINE.equals(codeNameCase)) {
                    fieldName = StrUtils.camelCaseToUnderline(columnName);
                }
                String fieldClass = SqlParser.getFieldClassOfColumnName(typeName, tinyintTransType);
                columns.add(ColumnInfo.builder()
                                .columnName(columnName)
                                .columnComment(remarks != null ? remarks : columnName)
                                .fieldClass(fieldClass)
                                .fieldName(fieldName)
                                .build());
            }
            rs.close();
        } catch (SQLException e) {
            log.warn("获取表字段异常", e);
        }
        return columns;
    }

    /**
     * 获取所有的主键
     * @Author yanger
     * @Date 2020/11/18 19:16
     * @param: conn
     * @param: schema
     * @param: tableName
     * @return: java.util.List<java.lang.String>
     */
    private static List<String> getTablePrimaryKeys(Connection conn, String schema, String tableName) {
        List<String> primaryKeys = new ArrayList<>();
        try {
            // 获取字段基本信息
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getPrimaryKeys(null, schema, tableName);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                // int keySeq = rs.getInt("KEY_SEQ");
                // String pkName = rs.getString("PK_NAME");
                primaryKeys.add(columnName);
            }
            rs.close();
        } catch (SQLException e) {
            log.warn("获取主键异常", e);
        }
        return primaryKeys;
    }


}
