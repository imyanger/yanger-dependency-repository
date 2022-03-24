package com.yanger.starter.web.listener;

import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.EndpointConst;
import com.yanger.starter.basic.constant.OrderConstant;
import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.starter.basic.listener.StarterInfoRunner;
import com.yanger.starter.web.annotation.EnumName;
import com.yanger.starter.web.context.SerializeEnumContainer;
import com.yanger.starter.web.entity.SerializeEnumData;
import com.yanger.starter.web.property.EnumProperties;
import com.yanger.tools.general.tools.StringTools;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载 SerializeEnum 实现类 到容器中
 * @Author yanger
 * @Date 2022/3/22/022 21:42
 */
@Slf4j
@Order(OrderConstant.ORDER_SERIALIZE_ENUM_CONTAINER_RUNNER)
@Component
@AutoConfigureAfter(StarterInfoRunner.class)
public class SerializeEnumContainerRunner implements ApplicationRunner {

    @Autowired
    private EnumProperties enumProperties;

    /**
     * Run
     * @param args args
     */
    @Override
    public void run(ApplicationArguments args) {
        if(!enumProperties.getEnable()) {
            log.info("{} 服务未开启 SerializeEnum Api，如要开启请设置 yanger.serialize-enum.enable = true ", App.applicationName);
            return;
        }
        ClassInfoList implClasses = new ClassGraph()
                .enableClassInfo()
                .scan()
                .getClassesImplementing(SerializeEnum.class.getName());
        implClasses.forEach(s -> {
            try {
                String className = s.getName();
                EnumName enumNameAnno = Class.forName(className).getAnnotation(EnumName.class);
                String enumName = enumNameAnno != null && StringTools.isNotBlank(enumNameAnno.value()) ? enumNameAnno.value() : s.getSimpleName();
                Object[] enumConstants = Class.forName(className).getEnumConstants();
                List<SerializeEnumData> serializeEnumDataList = new ArrayList<>(enumConstants.length);
                for (Object enumConstant : enumConstants) {
                    SerializeEnum serializeEnum = (SerializeEnum) enumConstant;
                    SerializeEnumData serializeEnumData = SerializeEnumData
                            .builder()
                            .value(serializeEnum.getValue())
                            .desc(serializeEnum.getDesc())
                            .name(serializeEnum.name())
                            .ordinal(serializeEnum.ordinal())
                            .build();
                    serializeEnumDataList.add(serializeEnumData);
                }
                SerializeEnumContainer.put(enumName, serializeEnumDataList);
            } catch (Exception e) {
                // ignore each enum data put
            }
        });
        if (App.serverUrl != null) {
            log.info("{} 服务已开启 SerializeEnum Api，获取枚举数据集：{} ", App.applicationName, App.serverUrl + EndpointConst.GET_SERIALIZE_ENUM_CONTAINER_URL);
        }
    }

}
