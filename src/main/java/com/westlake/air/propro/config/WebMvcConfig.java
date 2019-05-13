package com.westlake.air.propro.config;

import com.westlake.air.propro.domain.db.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 跨域脚本规则,仅允许来自Aliyun 指定OSS文件可以访问
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/api/**");
        registry.addMapping("/**")
                .allowedOrigins("http://wias.oss-cn-shanghai.aliyuncs.com")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
                .allowCredentials(false).maxAge(3600);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addWebRequestInterceptor(new WebRequestInterceptor() {
//            @Override
//            public void preHandle(WebRequest webRequest) throws Exception {
//
//            }
//
//            @Override
//            public void postHandle(WebRequest webRequest, ModelMap modelMap) throws Exception {
//
//            }
//
//            @Override
//            public void afterCompletion(WebRequest webRequest, Exception e) throws Exception {
//
//            }
//        });
//    }
}
