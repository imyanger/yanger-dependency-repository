package com.yanger.starter.mongo.entity;

import com.yanger.starter.mongo.annotation.MongoCollection;
import com.yanger.starter.mongo.annotation.MongoColumn;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * @Description mongodb id 自增序列实体
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Data
@MongoCollection(value = "sequence")
public class Sequence {

    /** SEQ_ID */
    public static final String SEQ_ID = "seq_id";
    /** COLLECTION_NAME */
    public static final String COLLECTION_NAME = "collection_name";

    /** 主键 */
    @Id
    private String id;
    /** 当前自增id值 */
    @MongoColumn(name = SEQ_ID)
    private Long seqId;
    /** 需要自增 id 的集合名称 */
    @MongoColumn(name = COLLECTION_NAME)
    private String collectionName;
}
