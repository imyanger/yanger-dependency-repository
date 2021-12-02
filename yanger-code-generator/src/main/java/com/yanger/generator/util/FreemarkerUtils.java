package com.yanger.generator.util;

import com.yanger.generator.entity.param.GenerateParam;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Locale;
import java.util.Map;

/**
 * freemarker tool
 *
 * @author yanger
 */
public class FreemarkerUtils {

    protected static Logger log = LoggerFactory.getLogger(FreemarkerUtils.class);

    private static Configuration freemarkerConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    static {
        // fix bug:修复引入jar包后无法读取模板的问题
        String protocol = FreemarkerUtils.class.getResource("").getProtocol();
        if("file".equals(protocol)){
            // 非jar 中 （文件class 中）
            String templatePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            int path = templatePath.lastIndexOf("WEB-INF/classes/");
            if (path > -1) {
                templatePath = templatePath.substring(0, path);
            }
            try {
                freemarkerConfig.setDirectoryForTemplateLoading(new File(templatePath, "code-templates"));
            } catch (IOException e) {
                freemarkerConfig.setClassLoaderForTemplateLoading(FreemarkerUtils.class.getClassLoader(), "code-templates");
            }
        } else if("jar".equals(protocol)) {
            // jar 中
            freemarkerConfig.setClassLoaderForTemplateLoading(FreemarkerUtils.class.getProtectionDomain().getClassLoader(), "code-templates");
        }
        freemarkerConfig.setNumberFormat("#");
        freemarkerConfig.setClassicCompatible(true);
        freemarkerConfig.setDefaultEncoding("UTF-8");
        freemarkerConfig.setLocale(Locale.CHINA);
        freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    /**
     * 传入需要转义的字符串进行转义
     * @author yanger
     * @date 2020/7/17
     * @param originStr 源串
     * @return java.lang.String
     */
    private static String escapeString(String originStr) {
        return originStr.replaceAll("井", "\\#").replaceAll("￥", "\\$");
    }

    /**
     * process Template Into String
     * @author yanger
     * @date 2020/7/17
     * @param template 模板
     * @param model 参数
     * @return java.lang.String
     */
    private static String processTemplateIntoString(Template template, Object model)
            throws IOException, TemplateException {
        StringWriter result = new StringWriter();
        template.process(model, result);
        return result.toString();
    }

    /**
     * process String
     * @author yanger
     * @date 2020/7/17
     * @param templateName 模板名字
     * @param params 模板参数
     * @return java.lang.String
     */
    public static String processString(String templateName, Map<String, Object> params)
            throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(templateName);
        String htmlText = escapeString(processTemplateIntoString(template, params));
        return htmlText;
    }

    /**
     * process String
     * @author yanger
     * @date 2020/7/17
     * @param templateName 模板名字
     * @param param 模板参数
     * @return java.lang.String
     */
    public static String processString(String templateName, GenerateParam param)
        throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(templateName);
        String htmlText = escapeString(processTemplateIntoString(template, param));
        return htmlText;
    }

}
