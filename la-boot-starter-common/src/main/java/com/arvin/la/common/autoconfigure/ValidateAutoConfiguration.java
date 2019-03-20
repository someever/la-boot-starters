package com.arvin.la.common.autoconfigure;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 参数校验配置
 * 两种验证模式配置方式：
 * failFast：true  快速失败返回模式    false 普通模式
 * 1、普通模式（默认是这个模式）
 * 普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
 * 2、快速失败返回模式
 * 快速失败返回模式(只要有一个验证失败，则返回)
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
@Configuration
public class ValidateAutoConfiguration {

    @Value("${validator.fail_fast.enabled:true}")
    private boolean failFastEnabled;

    /**
     * 开启对requestParam参数的valid校验
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        return postProcessor;
    }


    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast(failFastEnabled)
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }
}
