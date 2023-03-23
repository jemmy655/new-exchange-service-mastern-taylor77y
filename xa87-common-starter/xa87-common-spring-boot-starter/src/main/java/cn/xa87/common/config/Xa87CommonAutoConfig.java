package cn.xa87.common.config;

import cn.xa87.common.exception.DefaultExceptionAdvice;
import cn.xa87.common.web.filter.RequestPerformanceFilter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;

@Configuration
public class Xa87CommonAutoConfig implements WebMvcConfigurer {

    /**
     * Token参数解析
     *
     * @param argumentResolvers 解析类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {

    }

    @Bean
    @ConditionalOnMissingBean({DefaultExceptionAdvice.class})
    public DefaultExceptionAdvice defaultExceptionAdvice() {
        return new DefaultExceptionAdvice();
    }

    /**
     * 过滤器配置
     */
    @Bean
    @ConditionalOnClass(RequestPerformanceFilter.class)
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<RequestPerformanceFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        RequestPerformanceFilter filter = new RequestPerformanceFilter();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addInitParameter("threshold", "3000");
        filterRegistrationBean.addInitParameter("includeQueryString", "true");
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }


    /**
     * 定义 Validator bean
     * 一个校验失败就立即返回
     */
    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(102400 * 10);
        factory.setMaxRequestSize(102400 * 10);
        return factory.createMultipartConfig();
    }
}
