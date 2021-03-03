package com.yanger.starter.test.mongo;

import com.yanger.starter.mongo.annotation.MongoCollection;
import com.yanger.starter.mongo.mapper.MongoPO;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "mongo_test_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel
@EqualsAndHashCode(callSuper=false)
@MongoCollection
public class MongoTestLog extends MongoPO<String, MongoTestLog> {

    private static final long serialVersionUID = 1L;

    // @Id
    // private String id;

    private String name;

    private Integer age;

    /**
     * 新增时间
     */
    // @CreatedDate
    // private Date createTime;

}
