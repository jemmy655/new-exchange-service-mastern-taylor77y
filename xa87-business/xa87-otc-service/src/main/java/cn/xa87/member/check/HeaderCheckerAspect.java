package cn.xa87.member.check;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.exception.TokenException;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;


@Aspect
@Component
public class HeaderCheckerAspect extends HandlerInterceptorAdapter {
    @Autowired
    private Xa87RedisRepository redisRepository;

    @Before("@annotation(headerChecker)")
    public void doBefore(HeaderChecker headerChecker) {
        HttpServletRequest request = currentRequest();
        if (Objects.isNull(request)) {
            throw new TokenException("request is null");
        }
        String[] headerNames = headerChecker.headerNames();
        //目前交易所请求头只有token一个参数暂时取第一个即可。
        for (String headerName : headerNames) {
            String value = request.getHeader(headerName);
            if (StringUtils.isEmpty(value)) {
                throw new TokenException("Header :" + headerName + " is null");
            }
        }
        String userId = request.getHeader(headerNames[1]);
        Object tokenRedis = redisRepository.get(CacheConstants.MEMBER_TOKEN_KEY + userId);
        if (tokenRedis != null) {
            tokenRedis = tokenRedis.toString().replaceAll("\"", "");
        }
        if (tokenRedis == null) {
            throw new TokenException("Token已过期");
        }
        String token = request.getHeader(headerNames[0]);
        if (!token.equals(tokenRedis)) {
            throw new TokenException("Token不一致");
        }
        String param = request.getParameter("userId");
        if (StringUtils.isEmpty(param)) {
            param = request.getParameter("userid");
            if (StringUtils.isEmpty(param)) {
                param = request.getParameter("UserId");
            }
        }
        if (param != null) {
            if (!param.equals(userId)) {
                throw new TokenException("用户与Token不一致");
            }
        }
    }

    /**
     * Return request current thread bound or null if none bound.
     *
     * @return Current request or null
     */
    private HttpServletRequest currentRequest() {
        // Use getRequestAttributes because of its return null if none bound
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(servletRequestAttributes).map(ServletRequestAttributes::getRequest).orElse(null);
    }
}
