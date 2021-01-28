package com.yanger.starter.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description 自定义的 sql 注入
 * @Author yanger
 * @Date 2021/1/28 18:02
 */
public class MybatisSqlInjector extends DefaultSqlInjector {

    /**
     * Gets method list *
     *
     * @param mapperClass mapper class
     * @return the method list
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = new ArrayList<>();
        methodList.add(new InsertIgnore());
        methodList.add(new InsertReplace());
        methodList.addAll(super.getMethodList(mapperClass));
        return Collections.unmodifiableList(methodList);
    }

}
