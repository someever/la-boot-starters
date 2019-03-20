package com.arvin.la.common.aop;

import com.arvin.la.common.constant.ResultCodeEnum;
import com.arvin.la.common.exception.FieldValidError;
import com.arvin.la.common.exception.ParamValidException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 参数校验 aop
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
@Aspect
public class ValidateParamAop {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateParamAop.class);

    //获取方法的参数的名称，一般的API无法获取，需要通过解析字节码获取，通常ASM，spring也是使用这个进行获取。
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 切入点
     */
    @Pointcut("execution(public * com.arvin.la..rest..*.*(..)) || execution(public * com.arvin.la..controller..*.*(..)) ")
    public void requestLogPoint() {
        LOGGER.info("--Define the pointCut---");
    }

    @Before("requestLogPoint()")
    public void before(JoinPoint point) throws NoSuchMethodException, SecurityException, ParamValidException {
        //  获得切入目标对象
        Object target = point.getThis();
        // 获得切入方法参数
        Object [] args = point.getArgs();
        // 获得切入的方法
        Method method = ((MethodSignature)point.getSignature()).getMethod();

        // 执行校验，获得校验结果
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);

        ValidateConstraintViolationThrowExpection(validResult,method);
    }



    private void ValidateConstraintViolationThrowExpection(Set<ConstraintViolation<Object>> validResult, Method method){
        if(!validResult.isEmpty()){
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            LOGGER.info(parameterNames.toString());
            List<FieldValidError> errors = new ArrayList<>();
            for(ConstraintViolation item :validResult){
                //获取参数路径的信息(参数的位置，参数的名称等等)
                PathImpl path = (PathImpl)item.getPropertyPath();
                int paramIndex = path.getLeafNode().getParameterIndex();
                String parameterName = parameterNames[paramIndex];
                FieldValidError fieldError = new FieldValidError(ResultCodeEnum.INVALID_REQUEST.getCode(),item.getMessage(),parameterName);
                errors.add(fieldError);
            }
            throw new ParamValidException(errors);
        }
    }

    //进行返回值的校验
    private <T> Set<ConstraintViolation<T>> validReturnValue(T obj, Method method, Object  returnValue){
        ExecutableValidator validatorParam = validator.forExecutables();
        return validatorParam.validateReturnValue(obj,method,returnValue);
    }

    //进行参数校验
    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object [] params){
        ExecutableValidator validatorParam = validator.forExecutables();
        return validatorParam.validateParameters(obj,method,params);
    }

}
