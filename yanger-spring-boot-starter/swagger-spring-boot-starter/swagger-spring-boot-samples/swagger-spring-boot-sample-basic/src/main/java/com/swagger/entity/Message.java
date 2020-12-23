package com.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "描述消息")
public class Message {

    @ApiModelProperty(value = "消息啊", required = true)
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Message() {
    }

    public Message(String msg) {
        this.msg = msg;
    }
}
