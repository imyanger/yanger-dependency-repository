package com.yanger.starter.mongo.convert;

import java.io.Serializable;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public abstract class AbstractEnumToSerializableConverter<T extends Enum<?>> extends AbstractEnumToGenericConverter<T, Serializable> {

}
