package cn.xa87.gateway.zuul.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfigration {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(CorsConfiguration.ALL);
        config.setAllowCredentials(true);
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);
        config.setMaxAge(3600l);//加上去就可以了
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    /**
     * 配置过滤器
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(corsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("corsFilter");
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }
}
