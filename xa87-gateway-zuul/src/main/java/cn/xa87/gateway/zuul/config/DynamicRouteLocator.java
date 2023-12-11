package cn.xa87.gateway.zuul.config;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.entity.SysRoute;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import java.util.*;

@Slf4j
public class DynamicRouteLocator extends DiscoveryClientRouteLocator {

    private ZuulProperties properties;

    private Xa87RedisRepository redisRepository;

    public DynamicRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties,
                               ServiceInstance localServiceInstance, Xa87RedisRepository redisRepository) {
        super(servletPath, discovery, properties, localServiceInstance);
        this.properties = properties;
        this.redisRepository = redisRepository;
    }

    /**
     * 重写路由配置
     *
     * @return 路由表
     */
    @Override
    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
        SetRoute();
        //读取properties配置、eureka默认配置
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>(super.locateRoutes());
        log.debug("初始默认的路由配置完成");
        routesMap.putAll(locateRoutesFromCache());
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StrUtil.isNotBlank(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    /**
     * 从Redis中读取缓存的路由信息
     *
     * @return 缓存中的路由表
     */
    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromCache() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();

        String vals = redisRepository.get(CacheConstants.ROUTE_KEY);
        if (vals == null) {
            return routes;
        }

        List<SysRoute> results = JSONArray.parseArray(vals, SysRoute.class);
        for (SysRoute result : results) {
            if (StrUtil.isBlank(result.getPath()) && StrUtil.isBlank(result.getUrl())) {
                continue;
            }

            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            try {
                zuulRoute.setId(result.getServiceId());
                zuulRoute.setPath(result.getPath());
                zuulRoute.setServiceId(result.getServiceId());
                zuulRoute.setRetryable(StrUtil.equals(result.getRetryable(), "0") ? Boolean.FALSE : Boolean.TRUE);
                zuulRoute.setStripPrefix(StrUtil.equals(result.getStripPrefix(), "0") ? Boolean.FALSE : Boolean.TRUE);
                zuulRoute.setUrl(result.getUrl());
                List<String> sensitiveHeadersList = StrUtil.splitTrim(result.getSensitiveheadersList(), ",");
                if (sensitiveHeadersList != null) {
                    Set<String> sensitiveHeaderSet = CollUtil.newHashSet();
                    sensitiveHeaderSet.addAll(sensitiveHeadersList);
                    zuulRoute.setSensitiveHeaders(sensitiveHeaderSet);
                    zuulRoute.setCustomSensitiveHeaders(true);
                }
            } catch (Exception e) {
                log.error("从数据库加载路由配置异常", e);
            }
            log.debug("添加数据库自定义的路由配置,path：{}，serviceId:{}", zuulRoute.getPath(), zuulRoute.getServiceId());
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }



    public void SetRoute() {
        List<SysRoute> list = new ArrayList<SysRoute>();
        SysRoute sysRoute = new SysRoute();
        sysRoute.setServiceId("xa87-member-service");
        sysRoute.setPath("/member/**");
        sysRoute.setRetryable("1");
        sysRoute.setEnabled("1");
        sysRoute.setDelFlag("0");
        sysRoute.setStripPrefix("1");
        list.add(sysRoute);

        SysRoute sysRoute1 = new SysRoute();
        sysRoute1.setServiceId("xa87-data-service");
        sysRoute1.setPath("/data/**");
        sysRoute1.setRetryable("1");
        sysRoute1.setEnabled("1");
        sysRoute1.setDelFlag("0");
        sysRoute1.setStripPrefix("1");
        list.add(sysRoute1);

        SysRoute sysRoute2 = new SysRoute();
        sysRoute2.setServiceId("xa87-contract-service");
        sysRoute2.setPath("/contract/**");
        sysRoute2.setRetryable("1");
        sysRoute2.setEnabled("1");
        sysRoute2.setDelFlag("0");
        sysRoute2.setStripPrefix("1");
        list.add(sysRoute2);

        SysRoute sysRoute3 = new SysRoute();
        sysRoute3.setServiceId("xa87-otc-service");
        sysRoute3.setPath("/otc/**");
        sysRoute3.setRetryable("1");
        sysRoute3.setEnabled("1");
        sysRoute3.setDelFlag("0");
        sysRoute3.setStripPrefix("1");
        list.add(sysRoute3);

        SysRoute sysRoute4 = new SysRoute();
        sysRoute4.setServiceId("xa87-entrust-service");
        sysRoute4.setPath("/entrust/**");
        sysRoute4.setRetryable("1");
        sysRoute4.setEnabled("1");
        sysRoute4.setDelFlag("0");
        sysRoute4.setStripPrefix("1");
        list.add(sysRoute4);

        SysRoute sysRoute5 = new SysRoute();
        sysRoute5.setServiceId("xa87-wallet-service");
        sysRoute5.setPath("/wallet/**");
        sysRoute5.setRetryable("1");
        sysRoute5.setEnabled("1");
        sysRoute5.setDelFlag("0");
        sysRoute5.setStripPrefix("1");
        list.add(sysRoute5);

        SysRoute sysRoute6 = new SysRoute();
        sysRoute6.setServiceId("xa87-fund-service");
        sysRoute6.setPath("/fund/**");
        sysRoute6.setRetryable("1");
        sysRoute6.setEnabled("1");
        sysRoute6.setDelFlag("0");
        sysRoute6.setStripPrefix("1");
        list.add(sysRoute6);

        redisRepository.set(CacheConstants.ROUTE_KEY, JSONObject.toJSONString(list));

    }
}
