package com.yanger.starter.id.factory;

import com.yanger.starter.id.enums.ProviderType;
import com.yanger.starter.id.service.IdService;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description id service 工厂bean
 * @Author yanger
 * @Date 2021/1/28 19:08
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
     */
    @Override
    public IdService getObject() {
        return this.idService;
    }

    /**
     * Gets object type *
     *
     * @return the object type
     */
    @Override
    public Class<?> getObjectType() {
        return IdService.class;
    }

    /**
     * Is singleton
     *
     * @return the boolean
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

}
