package cn.xa87.gateway.zuul.filter.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static cn.xa87.gateway.zuul.filter.MyFilterConstants.PRE_REQUEST_LOG_ORDER;
import static cn.xa87.gateway.zuul.filter.MyFilterConstants.PRE_TYPE;

@Component
public class PreRequestLogFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(PreRequestLogFilter.class);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_REQUEST_LOG_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {
        final RequestContext requestContext = RequestContext.getCurrentContext();
        final HttpServletRequest request = requestContext.getRequest();
        log.info(String.format("send %s request to %s", request.getMethod(), request.getRequestURL().toString()));
        return null;
    }

}
