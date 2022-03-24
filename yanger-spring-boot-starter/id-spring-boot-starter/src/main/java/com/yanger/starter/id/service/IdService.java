package com.yanger.starter.id.service;

import com.yanger.starter.id.entity.Id;

import java.util.Date;

/**
 * id 生成服务
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface IdService {

    /**
     * 生成分布式 id
     * @return the long
     */
    long genId();

    /**
     * 反解分布式 id
     * @param id id
     * @return the id
     */
    Id expId(long id);

    /**
     * 生成某一时段的 id
     * @param time time
     * @param seq  seq
     * @return the long
     */
    long makeId(long time, long seq);

    /**
     * Make id
     * @param time    time
     * @param seq     seq
     * @param machine machine
     * @return the long
     */
    long makeId(long time, long seq, long machine);

    /**
     * Make id
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     */
    long makeId(long deployType, long time, long seq, long machine);

    /**
     * Make id
     * @param idType     idType
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     */
    long makeId(long idType, long deployType, long time, long seq, long machine);

    /**
     * Make id
     * @param version    version
     * @param idType     idType
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    long makeId(long version, long idType, long deployType, long time, long seq, long machine);

    /**
     * 将整型时间戳转换为时间
     * @param time time
     * @return the date
     */
    Date transTime(long time);

}
