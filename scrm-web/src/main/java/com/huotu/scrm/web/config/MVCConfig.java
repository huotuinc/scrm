package com.huotu.scrm.web.config;

import com.huotu.scrm.service.config.ServiceConfig;
import com.huotu.scrm.web.interceptor.HeaderInterceptor;
import com.huotu.scrm.web.interceptor.CustomerInterceptor;
import com.huotu.scrm.web.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import java.util.Arrays;
import java.util.List;

/**
 * Created by helloztt on 2017-06-27.
 */
@Configuration
@EnableWebMvc
@ComponentScan({
        "com.huotu.scrm.web.controller",
        "com.huotu.scrm.web.interceptor"
})
@Import({MVCConfig.ThymeleafConfig.class, ServiceConfig.class})
public class MVCConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private HeaderInterceptor headerInterceptor;
    @Autowired
    private CustomerInterceptor customerInterceptor;
    @Autowired
    private UserInterceptor userInterceptor;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(headerInterceptor).addPathPatterns("/mall/**");
        registry.addInterceptor(customerInterceptor).addPathPatterns("/mall/**");
        registry.addInterceptor(userInterceptor).addPathPatterns("/site/**");
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        converters.add(converter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/resource/**/*", "/image/**/*", "/js/**/*", "/**/*.html")
                .addResourceLocations("/resource/", "/image/", "/js/", "/");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(thymeleafViewResolver);
    }

    @Configuration
    public static class ThymeleafConfig {
        @Autowired
        private ApplicationContext applicationContext;
        @Autowired
        private Environment env;

        @Bean
        public SpringResourceTemplateResolver templateResolver() {
            // SpringResourceTemplateResolver automatically integrates with Spring's own
            // resource resolution infrastructure, which is highly recommended.
            SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
            templateResolver.setApplicationContext(this.applicationContext);
            templateResolver.setPrefix("/view/");
            templateResolver.setSuffix(".html");
            // HTML is the default value, added here for the sake of clarity.
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCharacterEncoding("UTF-8");
            // Template cache is true by default. Set to false if you want
            // templates to be automatically updated when modified.
            if (env.acceptsProfiles("!container")) {
                templateResolver.setCacheable(false);
            } else {
                templateResolver.setCacheable(true);
            }
            return templateResolver;
        }

        @Bean
        public SpringTemplateEngine templateEngine() {
            // SpringTemplateEngine automatically applies SpringStandardDialect and
            // enables Spring's own MessageSource message resolution mechanisms.
            SpringTemplateEngine templateEngine = new SpringTemplateEngine();
            templateEngine.setTemplateResolver(templateResolver());
            templateEngine.addDialect(new Java8TimeDialect());
            return templateEngine;
        }

        @Bean
        public ThymeleafViewResolver viewResolver() {
            ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
            viewResolver.setTemplateEngine(templateEngine());
            // NOTE 'order' and 'viewNames' are optional
            viewResolver.setOrder(1);
            viewResolver.setCharacterEncoding("UTF-8");
            viewResolver.setContentType("text/html;charset=utf-8");
            return viewResolver;
        }
    }
}
