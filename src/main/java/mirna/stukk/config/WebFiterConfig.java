package mirna.stukk.config;

import mirna.stukk.interceptor.ApiLimitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-21 9:40
 **/
@Configuration
public class WebFiterConfig implements WebMvcConfigurer {

    @Bean
    public ApiLimitInterceptor getApiLimitInterceptor(){
        return new ApiLimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getApiLimitInterceptor()).addPathPatterns("/**");
    }

}
