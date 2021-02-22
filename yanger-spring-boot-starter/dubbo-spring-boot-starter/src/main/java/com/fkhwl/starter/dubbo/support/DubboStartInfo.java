package com.fkhwl.starter.dubbo.support;

import com.google.common.collect.Lists;

import com.yanger.tools.general.tools.StringTools;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/2/22 15:43
 */
@Slf4j
@UtilityClass
public class DubboStartInfo {

    public static List<CustomInfo> customComponentInfos = Lists.newArrayList();

    public static @NotNull StringBuilder padding(String libraryName) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder
            .append(StringTools.padAfter(libraryName, 20, " "))
            .append(": ");
    }

    public static void addCustomInfo(List<CustomInfo> customInfo) {
        customComponentInfos.addAll(customInfo);
    }

}
