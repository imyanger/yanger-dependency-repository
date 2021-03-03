package com.yanger.starter.dubbo.balance;

import com.yanger.starter.basic.util.ConfigKit;
import com.yanger.starter.basic.util.JsonUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 自定义 dubbo 负载均衡策略, 根据配置来选择服务
 *     存在多个 provider 且配置 dubbo.provider.loadbalance=grap 时才会生效
 *     配置说明:
 *     1. dubbo.provider.parameters.gray-enable=true 生产端是否启用灰度模式
 *     2. dubbo.consumer.parameters.gray-flag 消费端设置灰度标识 (userId/ip 等), 需要在代码中将设置的标识添加的 RpcContext 中
 *     3. dubbo.consumer.parameters.gray-values 消费端设置需要调用灰度服务的特定 userId 或 ip
 *     4. dubbo.provider.loadbalance=gray 消费端和生产端配置, 设置服务负载为自定义(gray)负载
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class GrayLoadBalance extends RandomLoadBalance {

    /**
     * 根据配置来选择灰度服务
     *
     * @param <T>        the type parameter
     * @param invokers   the invokers           提供者服务列表
     * @param url        the url                消费者发起调用的 url
     * @param invocation the invocation         RPC 参数封装
     * @return the invoker
     * @throws RpcException rpc exception
     */
    @Override
    public <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, @NotNull Invocation invocation) throws RpcException {
        log.debug("服务选择开始: {}", JsonUtils.toJson(invokers));
        List<Invoker<T>> allInvokers = new ArrayList<>(invokers);

        //region 1. 将正常服务和灰度服务分开
        Iterator<Invoker<T>> iterator = allInvokers.iterator();
        List<Invoker<T>> grayInvokers = new ArrayList<>(invokers.size());
        List<Invoker<T>> nomalInvokers = new ArrayList<>(invokers.size());
        while (iterator.hasNext()) {
            Invoker<T> invoker = iterator.next();
            // 从所有的提供者中获取配置的负载类型: dubbo.provider.parameters.gray-enable=true
            String providerStatus = invoker.getUrl().getParameter("gray.enable", "false");
            if (Boolean.parseBoolean(providerStatus)) {
                grayInvokers.add(invoker);
            } else {
                nomalInvokers.add(invoker);
            }
        }
        //endregion

        //region 2. 根据用户配置返回不同的服务类型
        // 获取 RpcContext 中设置的参数 (使用 userId 贯穿所有服务[可扩展为其他标识如: ip])
        Map<String, String> map = invocation.getAttachments();
        String grayFlagValue = map.get(ConfigKit.getProperty("dubbo.consumer.parameters.gray-flag"));
        if (StringUtils.isBlank(grayFlagValue)) {
            // 消费者没有配置灰度标识, 即使开启了灰度模式, 也会调用正常的服务, 保证调用不出错
            return this.gettInvoker(url, invocation, nomalInvokers);
        }

        // 获取请求中的参数, 在配置文件中设置: dubbo.consumer.parameters.gray-values=1,2,3,4,5,6,7,8,9,10
        String grayUserIds = url.getParameter("gray.values", "");
        String[] arrs = grayUserIds.split(",");

        // 如果配置的灰度值与灰度标识中的灰度值一致, 则返回一个灰度服务
        if (Arrays.asList(arrs).contains(grayFlagValue)) {
            return this.gettInvoker(url, invocation, grayInvokers);
        }
        //endregion
        return this.gettInvoker(url, invocation, nomalInvokers);
    }

    /**
     * 调用父类负载方法进行随机负载
     *
     * @param <T>        parameter
     * @param url        url
     * @param invocation invocation
     * @param invokers   invokers
     * @return the invoker
     */
    @Nullable
    private <T> Invoker<T> gettInvoker(URL url, @NotNull Invocation invocation, List<Invoker<T>> invokers) {
        if (CollectionUtils.isEmpty(invokers)) {
            throw new RpcException("No provider available for " + url);
        }
        if (invokers.size() == 1) {
            return invokers.get(0);
        }
        return super.doSelect(invokers, url, invocation);
    }

}

