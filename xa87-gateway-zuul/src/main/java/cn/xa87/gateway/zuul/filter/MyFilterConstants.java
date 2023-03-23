package cn.xa87.gateway.zuul.filter;

import cn.xa87.gateway.zuul.filter.pre.PreRequestLogFilter;
import com.netflix.zuul.ZuulFilter;

public interface MyFilterConstants {

    // ORDER constant -----------------------------------

    /**
     * Filter Order for {@link PreRequestLogFilter#filterOrder()}
     */
    int PRE_REQUEST_LOG_ORDER = Integer.MAX_VALUE;

    // Zuul Filter TYPE constant -----------------------------------

    /**
     * {@link ZuulFilter#filterType()} error type.
     */
    String ERROR_TYPE = "error";

    /**
     * {@link ZuulFilter#filterType()} post type.
     */
    String POST_TYPE = "post";

    /**
     * {@link ZuulFilter#filterType()} pre type.
     */
    String PRE_TYPE = "pre";

    /**
     * {@link ZuulFilter#filterType()} route type.
     */
    String ROUTE_TYPE = "route";

}
