package com.fkhwl.starter.mongo.repository;

import com.fkhwl.starter.mongo.mapper.Model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.*;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @param <T> parameter 组件类型
 * @param <M> parameter 实体类型
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 13:49
 * @since 1.0.0
 */
public interface MongoEntityRepository<T extends Serializable, M extends Model<M>> extends MongoRepository<M, T> {

}
