package com.yanger.generator.entity.sql;

import java.io.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * field info
 *
 * @author yanger 2020-05-02 20:11:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String columnName;

    private String columnComment;

    private String fieldName;

    private String fieldClass;

}

