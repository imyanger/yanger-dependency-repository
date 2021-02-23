package com.yanger.starter.id.util;

import java.util.Arrays;

/**
 * @Description
 * @Author yanger
 * @Date 2021/1/28 19:08
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
     */
    public static boolean isOn(String swtch) {
        return Arrays.asList(SWITCH_ON_EXP).contains(swtch);
    }

    /**
     * Is prop key on
     *
     * @param key key
     * @return the boolean
     */
    public static boolean isPropKeyOn(String key) {
        String prop = System.getProperty(key);
        return Arrays.asList(SWITCH_ON_EXP).contains(prop);
    }

}
