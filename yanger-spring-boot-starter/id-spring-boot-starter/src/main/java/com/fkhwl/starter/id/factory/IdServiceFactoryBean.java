package com.fkhwl.starter.id.factory;

import com.fkhwl.starter.id.enums.ProviderType;
import com.fkhwl.starter.id.service.IdService;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:25
 * @since 1.5.0
 */
@Data
@Slf4j
public class IdServiceFactoryBean implements FactoryBean<IdService>, InitializingBean {

    /** Provider type */
    private ProviderType providerType;

    /** Id service */
    private IdService idService;

    /**
     * After properties set
     *
     * @since 1.5.0
     */
    @Override
    public void afterPropertiesSet() {
        if (this.providerType == null) {
            log.error("The type of Id service is mandatory.");
            throw new IllegalArgumentException("The type of Id service is mandatory.");
        }
    }

    /**
     * Gets object *
     *
     * @return the object
     * @since 1.5.0
     */
    @Override
    public IdService getObject() {
        return this.idService;
    }

    /**
     * Gets object type *
     *
     * @return the object type
     * @since 1.5.0
     */
    @Override
    public Class<?> getObjectType() {
        return IdService.class;
    }

    /**
     * Is singleton
     *
     * @return the boolean
     * @since 1.5.0
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
