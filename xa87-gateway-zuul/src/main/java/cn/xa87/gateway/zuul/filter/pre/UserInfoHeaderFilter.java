package cn.xa87.gateway.zuul.filter.pre;

import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;

@Component
public class UserInfoHeaderFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {

        return null;
    }
}
