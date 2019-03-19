package com.arvin.la.redis.autoconfigure;

import com.arvin.la.redis.config.RedisProperties;
import com.arvin.la.redis.config.RedisSerializerEnum;
import com.arvin.la.redis.serializer.KryoSerialzer;
import com.arvin.la.redis.service.RedisLockService;
import com.arvin.la.redis.service.RedisService;
import com.arvin.la.redis.service.impl.RedisLockServiceImpl;
import com.arvin.la.redis.service.impl.RedisServiceImpl;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 自动初始化
 * <p/>
 *
 * @author arvin.
 * @date 2019-03-06 14:40.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class RedisAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean("redisService")
    public RedisService initRedisService(){
        return new RedisServiceImpl();
    }

    @Bean("redisLockService")
    public RedisLockService initRedisLockService(){
        return new RedisLockServiceImpl();
    }

    /**
     *  实例化 RedisTemplate 对象
     * @return RedisTemplate
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
        log.info("初始化RedisTemplate");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(getSerializer());
        redisTemplate.setValueSerializer(getSerializer());
        redisTemplate.afterPropertiesSet();
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    /**
     * 根据配置参数创建不同的序列化器
     * @return 序列化器
     */
    private RedisSerializer getSerializer(){
        if (RedisSerializerEnum.JDK.toString().equalsIgnoreCase(redisProperties.getSerializerName())) {
            return new JdkSerializationRedisSerializer();
        } else if (RedisSerializerEnum.KRYO.toString().equalsIgnoreCase(redisProperties.getSerializerName())) {
            return new KryoSerialzer();
        } else if (RedisSerializerEnum.JACKSON.toString().equalsIgnoreCase(redisProperties.getSerializerName())){
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            return jackson2JsonRedisSerializer;
        } else {
            return new JdkSerializationRedisSerializer();
        }
    }
}
