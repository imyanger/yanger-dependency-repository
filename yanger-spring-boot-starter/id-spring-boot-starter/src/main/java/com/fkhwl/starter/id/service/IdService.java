package com.fkhwl.starter.id.service;

import com.fkhwl.starter.id.entity.Id;

import java.util.Date;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 15:27
 * @since 1.5.0
 */
public interface IdService {

    /**
     * 生成分布式 id
     *
     * @return the long
     * @since 1.5.0
     */
    long genId();

    /**
     * 反解分布式 id
     *
     * @param id id
     * @return the id
     * @since 1.5.0
     */
    Id expId(long id);

    /**
     * 生成某一时段的 id
     *
     * @param time time
     * @param seq  seq
     * @return the long
     * @since 1.5.0
     */
    long makeId(long time, long seq);

    /**
     * Make id
     *
     * @param time    time
     * @param seq     seq
     * @param machine machine
     * @return the long
     * @since 1.5.0
     */
    long makeId(long time, long seq, long machine);

    /**
     * Make id
     *
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     * @since 1.5.0
     */
    long makeId(long deployType, long time, long seq, long machine);

    /**
     * Make id
     *
     * @param idType     idType
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     * @since 1.5.0
     */
    long makeId(long idType, long deployType, long time,
                long seq, long machine);

    /**
     * Make id
     *
     * @param version   version
     * @param idType      idType
     * @param deployType gen method
     * @param time      time
     * @param seq       seq
     * @param machine   machine
     * @return the long
     * @since 1.5.0
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    long makeId(long version,
                long idType,
                long deployType,
                long time,
                long seq,
                long machine);

    /**
     * 将整型时间戳转换为时间
     *
     * @param time time
     * @return the date
     * @since 1.5.0
     */
    Date transTime(long time);
}
