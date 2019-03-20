package com.arvin.la.common.aop;

import cn.hutool.core.util.NetUtil;
import com.alibaba.fastjson.JSON;
import com.arvin.la.common.rest.Result;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数日志aop
 * 请求参数支持自定义对象脱敏
 * 自定义对象必须实现序列化
 * 响应参数必须定义为Result，将对象封装在data中
 *
 * @author arvin.
 * @date 2019-03-20 14:26.
 */
@Aspect
public class RequestLogAop {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogAop.class);

    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.arvin.la..rest..*.*(..)) || " +
            "execution(public * com.arvin.la..controller..*.*(..))")
    public void webLog(){}

    @Pointcut("@annotation(com.arvin.la.common.annotation.IgnoreAopLog)")
    public void ignoreLog(){}

    @Before("webLog() && !ignoreLog()")
    public void doBefore(JoinPoint joinPoint){

        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String method = request.getMethod();

        // 记录下请求内容
        LOGGER.info("URL : " + request.getRequestURL().toString());
        LOGGER.info("HTTP_METHOD : " + method);
        LOGGER.info("IP : " +  NetUtil.getIpByHost(NetUtil.getLocalhostStr()));
        LOGGER.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        LOGGER.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        // 获得切入的方法
        Method httpMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(httpMethod);
        Map<String,Object> map = new HashMap<>();

        for (int i=0;i<parameterNames.length;i++){
            Object object = joinPoint.getArgs()[i];
            //排除
            if((object instanceof HttpServletRequest)
                    || (object instanceof HttpServletResponse)
                    || (object instanceof MultipartFile)){
                continue;
            }


            //基本类型、string、枚举,直接保存在map中
            if(isJDKType(object.getClass()) || (object instanceof String) ||
                    (object.getClass().isPrimitive()) ||
                    (object.getClass().isEnum())){
                map.put(parameterNames[i],object);
            }else {//用户自定义对象,map,集合
                String s = JSON.toJSONString(object);
                map.put(parameterNames[i],s);
            }
        }
        LOGGER.info("REQUEST ARGS ---: " + map.toString());

    }

    @AfterReturning(returning = "result", pointcut = "webLog() && !ignoreLog()")
    public void doAfterReturning(Object result){
        // 处理完请求，返回内容
        if(null != result){
            if(result instanceof Result){
                LOGGER.info("RESPONSE ARGS : " + result);
            }
        }else{
            LOGGER.info("RESPONSE : " + null);
        }
        LOGGER.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));

    }


    /**
     * 是否jdk类型变量
     *
     * @param clazz class
     * @return boolean
     */
    private static boolean isJDKType(Class clazz){
        return clazz.getPackage() != null && (StringUtils.startsWith(clazz.getPackage().getName(), "javax.")
                || StringUtils.startsWith(clazz.getPackage().getName(), "java.")
                || StringUtils.startsWith(clazz.getName(), "javax.")
                || StringUtils.startsWith(clazz.getName(), "java."));
    }

}
