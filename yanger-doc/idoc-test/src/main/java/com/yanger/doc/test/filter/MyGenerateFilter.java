package com.yanger.doc.test.filter;

import com.yanger.doc.common.model.metadata.DocClass;
import com.yanger.doc.generator.filter.IDocGenerateFilter;

/**
 * 自定义生成过滤器
 *
 * @author binbin.hou
 * @since 0.0.1
 */
public class MyGenerateFilter implements IDocGenerateFilter {

    @Override
    public boolean include(DocClass docClass) {
        if ("QueryUserService".equalsIgnoreCase(docClass.getName())) {
            return true;
        }
        return false;
    }

}
