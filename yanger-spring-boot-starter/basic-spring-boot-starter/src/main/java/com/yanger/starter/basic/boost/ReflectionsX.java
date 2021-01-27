package com.yanger.starter.basic.boost;

import com.yanger.starter.basic.util.ConfigKit;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;

import java.util.Set;

import static org.reflections.ReflectionUtils.forName;
import static org.reflections.util.Utils.index;

/**
 * @Description 复写 Reflections ，去掉日志输出
 * @Author yanger
 * @Date 2021/1/27 9:58
 */
public class ReflectionsX extends Reflections {

    public ReflectionsX(final String prefix, final Scanner... scanners) {
        super((Object) prefix, scanners);
    }

    @Override
    public void expandSuperTypes() {
        String index = index(SubTypesScanner.class);
        Set<String> keys = store.keys(index);
        keys.removeAll(store.values(index));
        for (String key : keys) {
            final Class<?> type = forName(key, loaders());
            if (type != null) {
                expandSupertypes(store, key, type);
            }
        }
    }

    private ClassLoader[] loaders() {
        return configuration.getClassLoaders();
    }

    private void expandSupertypes(Store store, String key, Class<?> type) {
        for (Class<?> supertype : ReflectionUtils.getSuperTypes(type)) {
            if (store.put(SubTypesScanner.class, supertype.getName(), key)) {
                if (log != null && ConfigKit.isDebugModel()) {
                    log.debug("expanded subtype {} -> {}", supertype.getName(), key);
                }
                expandSupertypes(store, supertype.getName(), supertype);
            }
        }
    }

}
