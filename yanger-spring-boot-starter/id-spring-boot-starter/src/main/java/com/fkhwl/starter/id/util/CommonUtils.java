package com.fkhwl.starter.id.util;

import java.util.Arrays;

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
public class CommonUtils {
    /** SWITCH_ON_EXP */
    public static final String[] SWITCH_ON_EXP = new String[] {"ON", "TRUE", "on", "true"};

    /** SWITCH_OFF_EXP */
    public static final String[] SWITCH_OFF_EXP = new String[] {"OFF", "FALSE", "off", "false"};

    /**
     * Is on
     *
     * @param swtch swtch
     * @return the boolean
     * @since 1.5.0
     */
    public static boolean isOn(String swtch) {
        return Arrays.asList(SWITCH_ON_EXP).contains(swtch);
    }

    /**
     * Is prop key on
     *
     * @param key key
     * @return the boolean
     * @since 1.5.0
     */
    public static boolean isPropKeyOn(String key) {

        String prop = System.getProperty(key);

        return Arrays.asList(SWITCH_ON_EXP).contains(prop);
    }
}
