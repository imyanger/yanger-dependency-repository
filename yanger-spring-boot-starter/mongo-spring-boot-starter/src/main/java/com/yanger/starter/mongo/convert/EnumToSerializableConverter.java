package com.yanger.starter.mongo.convert;

/**
 * 枚举转换为 Serializable 类型, 实体 -> DB
 *     将 SerializeEnum[S] 转换为 Serializable[R] 子类[T]进行存储到 DB
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class EnumToSerializableConverter extends AbstractEnumToSerializableConverter<Enum<?>> {

}
