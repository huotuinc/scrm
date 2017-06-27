package com.huotu.scrm.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by helloztt on 2017-06-27.
 */
@Configuration
@ComponentScan({
        "com.huotu.scrm.service",
        "com.huotu.scrm.common"
})
@EnableJpaRepositories(
        basePackages = "com.huotu.scrm.service.repository"

)
@EnableTransactionManagement
@ImportResource({"classpath:hbm_config_prod.xml", "classpath:hbm_config_test.xml"})
public class ServiceConfig {
}
