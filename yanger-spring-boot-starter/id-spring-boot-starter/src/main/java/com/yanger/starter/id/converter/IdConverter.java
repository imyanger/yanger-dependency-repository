package com.yanger.starter.id.converter;

import com.yanger.starter.id.entity.Id;


/**
 * @Description ID 对象和长整型 id 互转
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface IdConverter {

    /**
     * ID 对象 转 长整型
     */
    long convert(Id id);

    /**
     * 长整型 id 转 ID 对象
     */
    Id convert(long id);

}
