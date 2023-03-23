package cn.xa87.member.check;


import cn.xa87.member.mapper.UserUseLogsMapper;
import cn.xa87.model.UserUseLogs;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Aspect
@Component
@Slf4j
public class LogHeaderCheckerAspect {

    @Autowired
    UserUseLogsMapper userUseLogsMapper;

    @Pointcut("@annotation(LogHeaderChecker)")
    public void statisticalPointCut() {

    }


    @AfterReturning(pointcut = "statisticalPointCut()", returning = "retVal")
    public void after(JoinPoint join, Object retVal) {


        if (retVal.toString().contains("SUCCEED")) {
            MethodSignature signature = (MethodSignature) join.getSignature();
            Method method = signature.getMethod();
            String[] s = method.toString().split(" ");
            String allMethodName = s[s.length - 1];
            //方法名
            String endMethodName = allMethodName.split("controller.")[1].split("\\(")[0];
            HttpServletRequest httpServletRequest = currentRequest();
            Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
            Set<String> strings = parameterMap.keySet();
            //方法参数
            StringBuffer sb = new StringBuffer();
            if (strings.size() > 0) {
                for (String string : strings) {
                    String[] strings1 = parameterMap.get(string);
                    sb.append(string).append(":").append(strings1[0]).append(",");
                }
            }
            //用户id
            String userId = httpServletRequest.getHeader("userId");
            //body
            BufferedReader br = null;
            StringBuilder sb1 = new StringBuilder("");

            try {
                br = httpServletRequest.getReader();
                String str;
                while ((str = br.readLine()) != null) {
                    sb1.append(str);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            saveLog(userId, endMethodName, sb.toString(), sb1.toString());
        }

//                Thread t = new Thread(new SaveLogs(join,retVal,currentRequest()));
//                t.run();
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


    @Async
    public void saveLog(String userId, String methodName, String methodParm, String body) {
        UserUseLogs userUseLogs = new UserUseLogs();
        userUseLogs.setUserId(userId);
        userUseLogs.setMethodName(methodName);
        userUseLogs.setMethodParm(methodParm);
        userUseLogs.setMethodBody(body);
        userUseLogs.setCreateTime(new Date());
        userUseLogsMapper.insert(userUseLogs);
    }

}
