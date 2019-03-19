package com.arvin.la.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * redis 配置
 * <p/>
 *
 * @author arvin.
 * @date 2019-03-06 14:40.
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private String host;
    private String port;
    private String password;
    private Integer timeout = 10000;
    private RedisProperties.Pool pool = new Pool();
    private String serializerName="jdk";

    @Data
    static class Pool{
        private Integer maxActive=8;
        private Integer maxIdle=8;
        private Integer minIdle=0;
        private Integer maxWait=60000;
    }

    @Data
    static class Cluster {
        private List<String> nodes;
        private Integer maxRedirects;
    }
}
