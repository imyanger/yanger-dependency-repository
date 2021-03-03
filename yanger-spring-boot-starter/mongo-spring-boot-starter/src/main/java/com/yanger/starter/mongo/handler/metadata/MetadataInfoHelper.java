package com.yanger.starter.mongo.handler.metadata;

import com.yanger.tools.web.tools.ClassUtils;
import com.yanger.tools.web.tools.ReflectionUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

/**
 * @Description 实体类反射表辅助类
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class MetadataInfoHelper {

    /** logger */
    private static final Log LOG = LogFactory.getLog(MetadataInfoHelper.class);

    /**
     * 储存反射类表信息
     */
    private static final Map<Class<?>, MetadataInfo> METADATA_INFO_CACHE = new ConcurrentHashMap<>();

    /**
     * <p>
     * 获取实体映射表信息
     * </p>
     *
     * @param clazz 反射实体类
     * @return 数据库表反射信息 table info
     */
    @Contract("null -> null")
    public static MetadataInfo getTableInfo(Class<?> clazz) {
        if (clazz == null
            || ReflectionUtils.isPrimitiveOrWrapper(clazz)
            || clazz == String.class) {
            return null;
        }
        MetadataInfo metadataInfo = METADATA_INFO_CACHE.get(ClassUtils.getUserClass(clazz));
        if (null != metadataInfo) {
            return metadataInfo;
        }
        //尝试获取父类缓存
        Class<?> currentClass = clazz;
        while (null == metadataInfo && Object.class != currentClass) {
            currentClass = currentClass.getSuperclass();
            metadataInfo = METADATA_INFO_CACHE.get(ClassUtils.getUserClass(currentClass));
        }
        if (metadataInfo != null) {
            METADATA_INFO_CACHE.put(ClassUtils.getUserClass(clazz), metadataInfo);
        }
        return metadataInfo;
    }

    /**
     * <p>
     * 获取所有实体映射表信息
     * </p>
     *
     * @return 数据库表反射信息集合 metadata infos
     */
    @Contract(" -> new")
    @SuppressWarnings("unused")
    public static @NotNull List<MetadataInfo> getMetadataInfos() {
        return new ArrayList<>(METADATA_INFO_CACHE.values());
    }

    /**
     * <p>
     * 实体类反射获取表信息【初始化】
     * </p>
     *
     * @param clazz 反射实体类
     * @return 数据库表反射信息 metadata info
     */
    public static synchronized @NotNull MetadataInfo initTableInfo(Class<?> clazz) {
        MetadataInfo metadataInfo = METADATA_INFO_CACHE.get(clazz);
        if (metadataInfo != null) {
            return metadataInfo;
        }

        // 没有获取到缓存信息,则初始化
        metadataInfo = new MetadataInfo(clazz);

        // 初始化字段相关
        initTableFields(clazz, metadataInfo);

        METADATA_INFO_CACHE.put(clazz, metadataInfo);

        return metadataInfo;
    }

    /**
     * 初始化 字段
     *
     * @param clazz        实体类
     * @param metadataInfo 数据库表反射信息
     */
    public static void initTableFields(Class<?> clazz, @NotNull MetadataInfo metadataInfo) {
        List<Field> list = getAllFields(clazz);

        List<MetadataFieldInfo> fieldList = new ArrayList<>();
        for (Field field : list) {
            /* 有 @MetadataField 注解的字段初始化 */
            if (initTableFieldWithAnnotation(metadataInfo, fieldList, field)) {
                continue;
            }

            /* 无 @MetadataField 注解的字段初始化 */
            fieldList.add(new MetadataFieldInfo(metadataInfo, field));
        }

        /* 字段列表,不可变集合 */
        metadataInfo.setFieldList(Collections.unmodifiableList(fieldList));
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz 反射类
     * @return 属性集合 all fields
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fieldList = ReflectionUtils.getFieldList(ClassUtils.getUserClass(clazz));
        return fieldList.stream()
            .filter(field -> {
                // 过滤注解非表字段属性
                MetadataField metadataField = field.getAnnotation(MetadataField.class);
                return (metadataField == null);
            }).collect(toList());
    }

    /**
     * 字段属性初始化
     *
     * @param metadataInfo 表信息
     * @param fieldList    字段列表
     * @param field        field
     * @return true 继续下一个属性判断, 返回 continue;
     */
    private static boolean initTableFieldWithAnnotation(MetadataInfo metadataInfo,
                                                        List<MetadataFieldInfo> fieldList, @NotNull Field field) {
        /* 获取注解属性, 自定义字段 */
        MetadataField metadataField = field.getAnnotation(MetadataField.class);
        if (null == metadataField) {
            return false;
        }
        fieldList.add(new MetadataFieldInfo(metadataInfo, field, metadataField));
        return true;
    }

}
