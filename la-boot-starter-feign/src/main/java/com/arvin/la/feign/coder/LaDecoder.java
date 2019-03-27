package com.arvin.la.feign.coder;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * feign 自定义decoder
 * <p/>
 *
 * @author arvin.
 * @date 2019-03-27 09:44.
 */
public class LaDecoder extends ResponseEntityDecoder {


    public LaDecoder(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        //此处可以自定义decoder，处理一些特殊解析
        return super.decode(response, type);
    }
}
