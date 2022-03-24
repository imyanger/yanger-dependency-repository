package com.yanger.starter.mongo.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
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
