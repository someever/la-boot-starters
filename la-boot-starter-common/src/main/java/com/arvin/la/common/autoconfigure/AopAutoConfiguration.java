package com.arvin.la.common.autoconfigure;

import com.arvin.la.common.aop.RequestLogAop;
import com.arvin.la.common.aop.ValidateParamAop;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AOP 自动配置类
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
@Slf4j
@Configuration
public class AopAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = {"spring.validaop.enabled"},matchIfMissing = true)
    public ValidateParamAop getValidateParamAop(){
        log.info("init ValidateParamAop {0} ",ValidateParamAop.class);
        return new ValidateParamAop();
    }

    @Bean
    @ConditionalOnProperty(value = {"spring.logaop.enabled"},matchIfMissing = true)
    public RequestLogAop getRequestLogAop(){
        log.info("init RequestLogAop {0} ",RequestLogAop.class);
        return new RequestLogAop();
    }
}
