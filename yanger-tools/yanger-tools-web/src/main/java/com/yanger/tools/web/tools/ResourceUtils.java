package com.yanger.tools.web.tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.*;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 资源工具类
 * @Author yanger
 * @Date 2020/12/22 11:55
 */
public class ResourceUtils extends org.springframework.util.ResourceUtils {

    /** HTTP_REGEX */
    public static final String HTTP_REGEX = "^https?:.+$";

    /** FTP_URL_PREFIX */
    public static final String FTP_URL_PREFIX = "ftp:";

    /**
     * 获取资源
     * <p>
     * 支持以下协议:
     * <p>
     * 1. classpath:
     * 2. file:
     * 3. ftp:
     * 4. http: and https:
     * 5. classpath*:
     * 6. C:/dir1/ and /Users/lcm
     * </p>
     * @param resourceLocation 资源路径
     * @return {Resource}
     * @throws IOException IOException
     */
    @Contract("_ -> new")
    @NotNull
    @SuppressWarnings("checkstyle:ReturnCount")
    public static Resource getResource(String resourceLocation) throws IOException {
        Assert.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(resourceLocation);
        }
        if (resourceLocation.startsWith(FTP_URL_PREFIX)) {
            return new UrlResource(resourceLocation);
        }
        if (resourceLocation.matches(HTTP_REGEX)) {
            return new UrlResource(resourceLocation);
        }
        if (resourceLocation.startsWith(FILE_URL_PREFIX)) {
            return new FileUrlResource(resourceLocation);
        }
        return new FileSystemResource(resourceLocation);
    }

}
