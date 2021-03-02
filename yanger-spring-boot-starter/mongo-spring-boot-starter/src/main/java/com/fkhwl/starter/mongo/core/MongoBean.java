package com.fkhwl.starter.mongo.core;

import org.springframework.data.mongodb.core.MongoTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: ${description}</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:49
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoBean {
    /** Collection name */
    private String collectionName;
    /** Desc */
    private String desc;
    /** Mongo template */
    private MongoTemplate mongoTemplate;
}
