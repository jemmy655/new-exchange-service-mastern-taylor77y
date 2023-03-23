package cn.xa87.gateway.zuul.filter.pre;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

public class OrderRateLimiterFilter extends ZuulFilter {

    //每秒产生1000个令牌
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        //限流的接口
        List<String> noFIlter = new ArrayList<>();
        noFIlter.add("/entrust/entrust/setEntrust");
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : noFIlter) {//pattern--/user/**
            if (StringUtils.isNotEmpty(pattern)
                    && matcher.match(pattern, request.getRequestURI())) {
                return true;
            }
        }

        return false;


    }

    @Override
    public Object run() throws ZuulException {
        //可以用JMeter来进行测试
        RequestContext context = RequestContext.getCurrentContext();
        //tryAcquire达到最大流量时,立刻限流,也可以配置参数
        if (!RATE_LIMITER.tryAcquire()) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 429);
            result.put("msg", "操作频繁!");
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            context.setResponseBody(JSON.toJSONString(result));
            //解决中文乱码
            context.getResponse().setCharacterEncoding("UTF-8");
            context.getResponse().setContentType("text/html;charset=UTF-8");
        }
        return null;
    }

    @Override
    public String filterType() {
        // TODO Auto-generated method stub
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // TODO Auto-generated method stub
        return 1;
    }

}
