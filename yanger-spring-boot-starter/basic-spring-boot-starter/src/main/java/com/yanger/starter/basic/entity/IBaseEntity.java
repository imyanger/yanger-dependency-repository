package com.yanger.starter.basic.entity;

import java.io.*;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/1/29 9:44
 */
public interface IBaseEntity<T extends Serializable> extends Serializable {

    /** ID */
    String ID = "id";

    T getId();

}
