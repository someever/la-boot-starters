package com.arvin.la.mybatis.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus 自动初始化
 * <p/>
 *
 * @author arvin.
 * @date 2019-03-19 17:30.
 */
@Configuration
public class MybatisPlusAutoConfiguration {
    /**
     * 分页插件
     */
    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
