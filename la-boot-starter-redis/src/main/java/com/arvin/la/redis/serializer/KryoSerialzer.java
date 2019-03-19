package com.arvin.la.redis.serializer;

import com.arvin.la.redis.util.KryoUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 *
 * Redis 使用kryo序列化
 *
 * @author arvin
 * Date: 2018/3/15
 */
public class KryoSerialzer implements RedisSerializer {
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return KryoUtil.serializationObject(o);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return KryoUtil.deserializationObject(bytes);
    }
}
