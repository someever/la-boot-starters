package com.arvin.la.feign.autoconfigure;

import com.arvin.la.feign.coder.LaDecoder;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * feign 初始化
 * <p/>
 *
 * @author arvin.
 * @date 2019-03-27 09:45.
 */
@Configuration
public class FeignAutoConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @ConditionalOnMissingBean
    public Decoder feinDecoder(){
        return new LaDecoder(new SpringDecoder(this.messageConverters));
    }
}
