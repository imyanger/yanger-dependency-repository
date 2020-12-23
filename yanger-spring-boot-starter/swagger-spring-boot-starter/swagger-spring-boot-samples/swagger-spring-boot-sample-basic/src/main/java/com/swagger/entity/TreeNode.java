package com.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Levin
 * @since 2018/10/12 0012
 */
@ApiModel
public class TreeNode {

    @ApiModelProperty("ID")
    private Integer id;
    @ApiModelProperty("节点名字")
    private String name;
    private List<TreeNode> testTreeNodes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getTestTreeNodes() {
        return testTreeNodes;
    }

    public void setTestTreeNodes(List<TreeNode> testTreeNodes) {
        this.testTreeNodes = testTreeNodes;
    }
}
