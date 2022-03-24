package com.yanger.starter.id.enums;

/**
 * id 同步类型
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public enum SyncType {

    /** synchronized */
    SYNCHRONIZED,

    /** cas */
    CAS,

    /** lock 默认 sync type */
    LOCK

}
