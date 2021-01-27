package com.yanger.starter.web.entity;

import com.yanger.starter.web.enums.FileType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/1/27 19:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnData {

    private Integer id;

    private String name;

    private FileType fileType;

}
