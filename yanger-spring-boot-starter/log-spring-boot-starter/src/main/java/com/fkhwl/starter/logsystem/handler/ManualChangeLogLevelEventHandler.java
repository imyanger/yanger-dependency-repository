package com.fkhwl.starter.logsystem.handler;

import com.fkhwl.starter.logsystem.AbstractLoggingLevelConfiguration;
import com.fkhwl.starter.logsystem.event.ChangeLogLevelEvent;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 日志等级修改事件处理 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.09.06 14:00
 * @since 1.6.0
 */
public class ManualChangeLogLevelEventHandler extends AbstractLoggingLevelConfiguration<ChangeLogLevelEvent> {

    /**
     * Changed levels
     *
     * @param event event
     * @return the map
     * @since 1.6.0
     */
    @Override
    protected Map<String, String> changedLevels(@NotNull ChangeLogLevelEvent event) {
        return event.getSource();
    }

}
