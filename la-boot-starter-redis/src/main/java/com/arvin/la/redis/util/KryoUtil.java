package com.arvin.la.redis.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * kryo序列化util
 *
 * @author arvin
 * Date: 2018/3/15
 */
public class KryoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(KryoUtil.class);
    private static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * 每个线程的kryo实例
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        //支持对象循环引用
        kryo.setReferences(true);
        //不强制要求注册类
        kryo.setRegistrationRequired(false);
        ((Kryo.DefaultInstantiatorStrategy)kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });

    /**
     * 获取当前线程的kryo实例
     * @return Kryo
     */
    public static Kryo getInstance(){
        return KRYO_THREAD_LOCAL.get();
    }

    /**
     * 将对象[及对象类型]序列化
     * @param obj 任意对象
     * @param <T> 任意类型
     * @return 序列化后base64字符串
     */
    public static <T> byte[] serializationObject(T obj) {
       if(obj == null){
           return EMPTY_ARRAY;
       }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        Kryo kryo = getInstance();
        kryo.writeClassAndObject(output, obj);
        output.flush();
        output.close();
        return baos.toByteArray();
    }

    /**
     * 将序列化后的byte反序列化为对象
     * @param byteArray 对象序列化后的
     * @param <T> 原对象类型
     * @return 原对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserializationObject(byte[] byteArray) {
        if(byteArray == null || byteArray.length == 0){
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        Input input = new Input(bais);
        Kryo kryo = getInstance();
        return (T) kryo.readClassAndObject(input);
    }
}
