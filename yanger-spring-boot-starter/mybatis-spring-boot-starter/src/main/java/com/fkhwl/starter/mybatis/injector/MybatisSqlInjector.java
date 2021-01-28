package com.fkhwl.starter.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.fkhwl.starter.mybatis.injector.methods.InsertIgnore;
import com.fkhwl.starter.mybatis.injector.methods.Replace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 自定义的 sql 注入 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 */
public class MybatisSqlInjector extends DefaultSqlInjector {

    /**
     * Gets method list *
     *
     * @param mapperClass mapper class
     * @return the method list
     * @since 1.0.0
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = new ArrayList<>();
        methodList.add(new InsertIgnore());
        methodList.add(new Replace());
        methodList.addAll(super.getMethodList(mapperClass));
        return Collections.unmodifiableList(methodList);
    }
}
