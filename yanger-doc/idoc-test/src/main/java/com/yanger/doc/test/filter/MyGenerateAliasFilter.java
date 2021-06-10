package com.yanger.doc.test.filter;

import com.yanger.doc.common.model.metadata.DocClass;
import com.yanger.doc.generator.filter.IDocGenerateFilter;

/**
 * 自定义生成过滤器
 *
 * @author binbin.hou
 * @since 0.1.0
 */
public class MyGenerateAliasFilter implements IDocGenerateFilter {

    @Override
    public boolean include(DocClass docClass) {
        if ("TypeAliasSimpleBean".equalsIgnoreCase(docClass.getName())) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//        com.yanger.doc.generator.filter.IDocGenerateFilter f = new com.yanger.doc.test.filter.MyGenerateAliasFilter();
        com.yanger.doc.generator.filter.IDocGenerateFilter f = (IDocGenerateFilter) Class.forName("com.yanger.doc.test.filter.MyGenerateAliasFilter").newInstance();
        System.out.println(f);
    }

}
