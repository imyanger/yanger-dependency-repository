package com.yanger.generator.util;

import com.yanger.generator.entity.sql.ColumnInfo;
import com.yanger.generator.entity.sql.TableInfo;
import com.yanger.generator.enums.CodeNameCase;
import com.yanger.generator.exception.CodeGenerateException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**

 * @Author yanger
 * @Date 2020/5/26 11:12
 * @throws
 */
public class SqlParser {

    /**
     * 解析sql
     * @return com.yanger.generator.entity.TableInfo
     * @Author yanger
     * @Date 2020/5/26 10:22
     * @Param [sql]
     */
    public static TableInfo parseSql(String sql) {
        return parseSql(sql, "boolean");
    }

    /**
     * 解析sql
     * @return com.yanger.generator.entity.TableInfo
     * @Author yanger
     * @Date 2020/5/26 10:21
     * @Param []
     */
    public static TableInfo parseSql(String sql, String tinyintTransType) {
        return parseSql(sql, tinyintTransType, CodeNameCase.CAMEL);
    }

    /**
     * 解析sql
     * @Author yanger
     * @Date 2020/6/30 10:38
     * @Param [sql, tinyintTransType, nameCase]
     * @return com.yanger.generator.entity.TableInfo
     */
    public static TableInfo parseSql(String sql, String tinyintTransType, CodeNameCase codeNameCase) {
        if (sql == null || sql.trim().length() == 0) {
            throw new CodeGenerateException("Table structure can not be empty. 表结构不能为空。");
        }
        //特殊字符替换
        sql = replaceSpecialChar(sql);
        //解析表名
        String tableName = getTableName(sql);
        // class name
        String className = StrUtils.upperCaseFirst(StrUtils.underlineToCamelCase(tableName));
        // 解析表注释
        String tableComment = getTableComment(sql);
        // 解析列
        List<ColumnInfo> columns = getColumns(sql, codeNameCase, tinyintTransType);
        // 解析主键
        List<ColumnInfo> pkColumns = getPkColumns(sql, columns);
        return TableInfo.builder()
                .className(className)
                .columns(columns)
                .pkColumns(pkColumns)
                .tableComment(tableComment == null || tableComment == "" ? tableName : tableComment)
                .tableName(tableName)
                .build();
    }

    /**
     * @Author yanger
     * 获取字段类型
     * @Date 2020/5/26 13:37
     * @Param [typeName, tinyintTransType]
     * @return java.lang.String
     */
    public static String getFieldClassOfColumnName(String typeName, String tinyintTransType) {
        return getFieldClass(" " + typeName, tinyintTransType);
    }

    /**
     * 处理特殊字符
     * @return java.lang.String
     * @Author yanger
     * @Date 2020/5/26 10:42
     * @Param [sql]
     */
    private static String replaceSpecialChar(String sql) {
        return sql.trim()
                .replaceAll("'", "`")
                .replaceAll("\"", "`")
                .replaceAll("，", ",")
                .replaceAll("\\\\n`", "")
                .replaceAll("\\+", "")
                .replaceAll("``", "`")
                .replaceAll("\\\\", "");
    }

    /**
     * @Author yanger
     * 获取表名
     * @Date 2020/5/26 10:54
     * @Param [sql]
     * @return java.lang.String
     */
    private static String getTableName(String sql) {
        // table Name
        String tableName = null;
        if (sql.contains("TABLE") && sql.contains("(")) {
            tableName = sql.substring(sql.indexOf("TABLE") + 5, sql.indexOf("("));
        } else if (sql.contains("table") && sql.contains("(")) {
            tableName = sql.substring(sql.indexOf("table") + 5, sql.indexOf("("));
        } else {
            throw new CodeGenerateException("Table structure incorrect.表结构不正确。");
        }
        // 新增处理create table if not exists members情况
        if (tableName.contains("if not exists")) {
            tableName = tableName.replaceAll("if not exists", "");
        } else if (tableName.contains("IF NOT EXISTS")) {
            tableName = tableName.replaceAll("IF NOT EXISTS", "");
        }
        if (tableName.contains("`")) {
            tableName = tableName.substring(tableName.indexOf("`") + 1, tableName.lastIndexOf("`"));
        } else {
            // 空格开头的，需要替换掉\n\t空格
            tableName = tableName.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "");
        }
        // 优化对byeas`.`ct_bd_customerdiscount这种命名的支持
        if (tableName.contains("`.`")) {
            tableName = tableName.substring(tableName.indexOf("`.`") + 3);
        } else if (tableName.contains(".")) {
            // 优化对likeu.members这种命名的支持
            tableName = tableName.substring(tableName.indexOf(".") + 1);
        }
        // class Name
        String className = StrUtils.upperCaseFirst(StrUtils.underlineToCamelCase(tableName));
        if (className.contains("_")) {
            className = className.replaceAll("_", "");
        }
        return tableName;
    }

    /**
     * @Author yanger
     * 获取表的注释
     * @Date 2020/5/26 10:54
     * @Param [sql]
     * @return java.lang.String
     */
    private static String getTableComment(String sql) {
        String lowerSql = sql.toLowerCase();
        String tableComment = "";
        // mysql是comment=,pgsql/oracle是comment on table,
        if (lowerSql.contains("comment=")) {
            String tableCommentTmp = sql.substring(lowerSql.lastIndexOf("comment=") + 8).replaceAll("`", "").trim();
            if (tableCommentTmp.indexOf(" ") != tableCommentTmp.lastIndexOf(" ")) {
                tableCommentTmp = tableCommentTmp.substring(tableCommentTmp.indexOf(" ") + 1, tableCommentTmp.lastIndexOf(" "));
            }
            if (tableCommentTmp != null && tableCommentTmp.trim().length() > 0) {
                tableComment = tableCommentTmp;
            }
        } else if (lowerSql.contains("comment on table")) {
            //COMMENT ON TABLE CT_BAS_FEETYPE IS 'CT_BAS_FEETYPE';
            String tableCommentTmp = sql.substring(lowerSql.lastIndexOf("comment on table") + 17).trim();
            //证明这是一个常规的COMMENT ON TABLE  xxx IS 'xxxx'
            if (tableCommentTmp.contains("`")) {
                tableCommentTmp = tableCommentTmp.substring(tableCommentTmp.indexOf("`") + 1);
                tableCommentTmp = tableCommentTmp.substring(0, tableCommentTmp.indexOf("`"));
                tableComment = tableCommentTmp;
            }
        }
        //如果备注跟;混在一起，需要替换掉
        tableComment = tableComment.replaceAll(";", "");
        return tableComment;
    }

    /**
     * @Author yanger
     * 获取列信息
     * @Date 2020/5/26 16:09
     * @Param [sql, nameCase, tinyintTransType]
     * @return java.util.List<com.yanger.generator.entity.ColumnInfo>
     */
    private static List<ColumnInfo> getColumns(String sql, CodeNameCase codeNameCase, String tinyintTransType) {
        List<ColumnInfo> columns = new ArrayList<>(0);
        // deal column sql
        String columnSql = dealColumnSql(sql);
        String[] columnSqls = columnSql.split(",");
        if (columnSqls.length > 0) {
            // i为了解决primary key关键字出现的地方，出现在前3行，一般和id有关
            for (int i = 0; i < columnSqls.length; i++) {
                String columnLine = columnSqls[i];
                columnLine = columnLine.replaceAll("\n", "")
                        .replaceAll("\t", "").trim();
                // 判断是否正常的列
                boolean specialFlag = isNormalLine(columnLine, i);
                if (specialFlag) {
                    // 如果是oracle的number(x,x)，可能出现最后分割残留的,x)，这里做排除处理
                    if (columnLine.length() < 5) {
                        continue;
                    }
                    // 支持'符号以及空格的oracle语句// userid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                    String columnName = "";
                    columnLine = columnLine.replaceAll("`", " ")
                            .replaceAll("\"", " ").replaceAll("'", "")
                            .replaceAll("  ", " ").trim();
                    // 如果遇到username varchar(65) default '' not null,这种情况，判断第一个空格是否比第一个引号前
                    columnName = columnLine.substring(0, columnLine.indexOf(" "));
                    // field Name
                    String fieldName = "";
                    if(CodeNameCase.CAMEL.equals(codeNameCase)) {
                        fieldName = StrUtils.lowerCaseFirst(StrUtils.underlineToCamelCase(columnName));
                    } else if (CodeNameCase.UNDER_LINE.equals(codeNameCase)) {
                        fieldName = StrUtils.camelCaseToUnderline(columnName);
                    }
                    // field class
                    columnLine = columnLine.substring(columnLine.indexOf("`") + 1).trim();
                    // int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                    String fieldClass = getFieldClass(columnLine, tinyintTransType);
                    // field comment
                    String columnComment = getColumnComment(sql, columnName, columnLine);
                    // list add
                    columns.add(ColumnInfo.builder()
                            .columnName(columnName)
                            .columnComment(columnComment != null ? columnComment : columnName)
                            .fieldClass(fieldClass)
                            .fieldName(fieldName)
                            .build());
                }
            }
        }
        return columns;
    }

    /**
     * @Author yanger
     * 获取包含列的sql
     * @Date 2020/5/26 13:45
     * @Param [sql]
     * @return java.lang.String
     */
    private static String dealColumnSql(String sql){
        // 正常( )内的一定是字段相关的定义
        String columnSql = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")"));
        // 匹配 comment，替换备注里的小逗号, 防止不小心被当成切割符号切割
        String commentPattenStr1 = "comment `(.*?)\\`";
        Matcher matcher1 = Pattern.compile(commentPattenStr1).matcher(columnSql);
        while (matcher1.find()) {
            String commentTmp = matcher1.group();
            //不替换，只处理，支持COMMENT评论里面多种注释
            if (commentTmp.contains(",")) {
                String commentTmpFinal = commentTmp.replaceAll(",", "，");
                columnSql = columnSql.replace(matcher1.group(), commentTmpFinal);
            }
        }
        // 支持`double(10, 2)`等类型中有英文逗号的特殊情况
        String commentPattenStr2 = "\\`(.*?)\\`";
        Matcher matcher2 = Pattern.compile(commentPattenStr2).matcher(columnSql);
        while (matcher2.find()) {
            String commentTmp2 = matcher2.group();
            if (commentTmp2.contains(",")) {
                String commentTmpFinal = commentTmp2.replaceAll(",", "，").replaceAll("\\(", "（").replaceAll("\\)", "）");
                columnSql = columnSql.replace(matcher2.group(), commentTmpFinal);
            }
        }
        // 支持double(10, 2)等类型中有英文逗号的特殊情况
        String commentPattenStr3 = "\\((.*?)\\)";
        Matcher matcher3 = Pattern.compile(commentPattenStr3).matcher(columnSql);
        while (matcher3.find()) {
            String commentTmp3 = matcher3.group();
            if (commentTmp3.contains(",")) {
                String commentTmpFinal = commentTmp3.replaceAll(",", "，");
                columnSql = columnSql.replace(matcher3.group(), commentTmpFinal);
            }
        }
        return columnSql;
    }

    /**
     * @Author yanger
     * 判断是否是正常的列
     * @Date 2020/5/26 11:56
     * @Param [columnLine, lineIndex]
     * @return boolean
     */
    private static boolean isNormalLine(String columnLine, int lineIndex){
        columnLine = columnLine.toLowerCase();
        return !columnLine.contains("key ") && !columnLine.contains("constraint")
                && !columnLine.contains("using") && !columnLine.contains("unique")
                && !(columnLine.contains("primary ") && columnLine.indexOf("storage") + 3 > columnLine.indexOf("("))
                && !columnLine.contains("pctincrease")
                && !columnLine.contains("buffer_pool") && !columnLine.contains("tablespace")
                && !(columnLine.contains("primary ") && lineIndex > 3);
    }

    /**
     * @Author yanger
     * 获取字段类型
     * @Date 2020/5/26 13:37
     * @Param [columnLine, tinyintTransType]
     * @return java.lang.String
     */
    private static String getFieldClass(String columnLine, String tinyintTransType){
        String fieldClass = null;
        columnLine = columnLine.toLowerCase();
        // 字段的处理
        if (columnLine.contains(" tinyint")) {
            // 对tinyint的特殊处理
            fieldClass = tinyintTransType;
        } else if (columnLine.contains(" int") || columnLine.contains(" smallint")) {
            fieldClass = Integer.class.getSimpleName();
        } else if (columnLine.contains(" bigint")) {
            fieldClass = Long.class.getSimpleName();
        } else if (columnLine.contains(" float")) {
            fieldClass = Float.class.getSimpleName();
        } else if (columnLine.contains(" double")) {
            fieldClass = Double.class.getSimpleName();
        } else if (columnLine.contains(" time") || columnLine.contains(" date") || columnLine.contains(" datetime") || columnLine.contains(" timestamp")) {
            fieldClass = Date.class.getSimpleName();
        } else if (columnLine.contains(" varchar") || columnLine.contains(" text") || columnLine.contains(" char")
                || columnLine.contains(" clob") || columnLine.contains(" blob") || columnLine.contains(" json")) {
            fieldClass = String.class.getSimpleName();
        } else if (columnLine.contains(" decimal") || columnLine.contains(" number")) {
            // 如果startKh大于等于0，则表示有设置取值范围
            int startKh = columnLine.indexOf("(");
            if (startKh >= 0) {
                int endKh = columnLine.indexOf(")", startKh);
                String[] fanwei = columnLine.substring(startKh + 1, endKh).split("，");
                //如果括号里是1位或者2位且第二位为0，则进行特殊处理，只有有小数位，都设置为BigDecimal
                if ((fanwei.length > 1 && "0".equals(fanwei[1])) || fanwei.length == 1) {
                    int length = Integer.parseInt(fanwei[0]);
                    if (fanwei.length > 1) {
                        length = Integer.valueOf(fanwei[1]);
                    }
                    //数字范围9位及一下用Integer，大的用Long
                    if (length <= 9) {
                        fieldClass = Integer.class.getSimpleName();
                    } else {
                        fieldClass = Long.class.getSimpleName();
                    }
                } else {
                    //有小数位数一律使用BigDecimal
                    fieldClass = BigDecimal.class.getSimpleName();
                }
            } else {
                fieldClass = BigDecimal.class.getSimpleName();
            }
        } else if (columnLine.contains(" boolean")) {
            // 对boolean的处理（感谢@violinxsc的反馈）以及修复tinyint类型字段无法生成boolean类型问题
            fieldClass = Boolean.class.getSimpleName();
        } else {
            fieldClass = String.class.getSimpleName();
        }
        return fieldClass;
    }

    /**
     * @Author yanger
     * 获取列注释信息
     * @Date 2020/5/26 13:46
     * @Param [sql, columnName, columnLine]
     * @return java.lang.String
     */
    private static String getColumnComment(String sql, String columnName, String columnLine){
        String columnComment = null;
        String lowerSql = sql.toLowerCase();
        String lowerCloumnLine = columnLine.toLowerCase();
        // field comment，MySQL的一般位于field行，而pgsql和oralce多位于后面。
        if (lowerSql.contains("comment on column")
                && (lowerSql.contains("." + columnName + " is ") || lowerSql.contains(".`" + columnName + "` is"))) {
            //新增对pgsql/oracle的字段备注支持
            //COMMENT ON COLUMN public.check_info.check_name IS '检查者名称';
            lowerSql = lowerSql.replaceAll(".`" + columnName + "` is", "." + columnName + " is");
            Matcher columnCommentMatcher = Pattern.compile("\\." + columnName + " is `").matcher(sql);
            while (columnCommentMatcher.find()) {
                String columnCommentTmp = columnCommentMatcher.group();
                //System.out.println(columnCommentTmp);
                columnComment = sql.substring(lowerSql.indexOf(columnCommentTmp) + columnCommentTmp.length()).trim();
                columnComment = columnComment.substring(0, columnComment.indexOf("`")).trim();
            }
        } else if (lowerCloumnLine.contains(" comment")) {
            //20200518 zhengkai 修复包含comment关键字的问题
            String commentTmp = columnLine.substring(lowerCloumnLine.lastIndexOf("comment") + 7).trim();
            // '用户ID',
            if (commentTmp.contains("`") || commentTmp.indexOf("`") != commentTmp.lastIndexOf("`")) {
                commentTmp = commentTmp.substring(commentTmp.indexOf("`") + 1, commentTmp.lastIndexOf("`"));
            }
            //解决最后一句是评论，无主键且连着)的问题:album_id int(3) default '1' null comment '相册id：0 代表头像 1代表照片墙')
            if (commentTmp.contains(")")) {
                commentTmp = commentTmp.substring(0, commentTmp.lastIndexOf(")") + 1);
            }
            columnComment = commentTmp;
        }
        return columnComment;
    }


    /**
     * @Author yanger
     * 解析主键
     * @Date 2020/5/26 13:46
     * @Param [sql]
     * @return java.lang.String
     */
    private static List<ColumnInfo> getPkColumns(String sql, List<ColumnInfo> columns) {
        List<ColumnInfo> pkColumns = new ArrayList<>(0);
        String upperSql = sql.toUpperCase();
        int primaryIndex = upperSql.indexOf("PRIMARY ");
        if (primaryIndex > 0) {
            String primarySql = sql.substring(primaryIndex + 1);
            primarySql = primarySql.substring(primarySql.indexOf("(") + 1, primarySql.indexOf(")")).replaceAll(" ", "").replaceAll("`", "");
            if (primarySql != null) {
                Map<String, ColumnInfo> columnMap = columns.stream().collect(Collectors.toMap(ColumnInfo::getColumnName, Function.identity()));
                Arrays.stream(primarySql.split(",")).forEach(s -> {
                    if (columnMap.containsKey(s)) {
                        pkColumns.add(columnMap.get(s));
                    }
                });
            }
        }
        return pkColumns;
    }


}
