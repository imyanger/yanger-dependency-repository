package com.yanger.starter.mongo.repository;

import com.yanger.starter.mongo.mapper.Model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.*;

/**
 * @param <T> parameter 主键类型
 * @param <M> parameter 实体类型
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface MongoEntityRepository<T extends Serializable, M extends Model<M>> extends MongoRepository<M, T> {

}
