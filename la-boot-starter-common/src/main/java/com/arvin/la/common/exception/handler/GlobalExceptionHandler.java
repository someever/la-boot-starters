package com.arvin.la.common.exception.handler;


import com.arvin.la.common.constant.ResultCodeEnum;
import com.arvin.la.common.exception.BaseException;
import com.arvin.la.common.exception.FieldValidError;
import com.arvin.la.common.exception.ParamValidException;
import com.arvin.la.common.rest.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * 统一异常处理<p/>
 * 各module只需引入该jar，不需要单独定义异常
 *
 * @author arvin.
 * @date 2019-03-20 14:27.
 */
@Slf4j
@Configuration
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * | 分隔符
     */
    private static final String SPLIT_STRING = "\\|";

    /**
     * 统一处理非业务异常，返回500，系统异常
     * @param req 请求
     * @param e 异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result otherExceptionHandler(HttpServletRequest req, Exception e){
        log.error("ERROR ! GET REQUEST URL:"+req.getRequestURL());
        log.error("error message :"+e.getMessage(),e);
        return new Result(ResultCodeEnum.SYSTEM_EXCEPTION);
    }

    /**
     * 统一处理业务异常
     * @param req 请求
     * @param e 异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public Result baseExceptionHandler(HttpServletRequest req, BaseException e){
        log.error("ERROR ! GET REQUEST URL:"+req.getRequestURL());
        log.error("error message :"+e.getMessage(),e);
        return new Result(e.getErrorCode(),e.getMessage(),e.getMessage());
    }

    /**
     * 统一处request参数校验异常
     * @param req 请求
     * @param e 异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ParamValidException.class)
    public Result paramValidExceptionHandler(HttpServletRequest req, ParamValidException e){
        log.error("ERROR ! GET REQUEST URL:"+req.getRequestURL());
        log.error("error message :"+e.getMessage(),e);
        List<FieldValidError> fieldValidErrors = e.getFieldValidErrorList();
        return new Result<>(ResultCodeEnum.INVALID_REQUEST,fieldValidErrors);
    }

    /**
     * 统一处request参数校验异常(Bean对象)
     * 普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
     * @param req 请求
     * @param e 异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result beanValidExceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e){
        log.error("ERROR ! GET REQUEST URL:"+req.getRequestURL());
        log.error("error message :"+e.getMessage(),e);
        BeanPropertyBindingResult result = (BeanPropertyBindingResult)e.getBindingResult();
        if(result.hasErrors()){
            List<FieldError> fieldErrors = result.getFieldErrors();
            List<FieldValidError> fieldValidErrors = fieldErrors.stream().map(
                    fieldError -> {
                        FieldValidError fieldValidError = returnFieldValidError(fieldError.getField(),fieldError.getDefaultMessage());
                        return fieldValidError;
                    }
            ).collect(Collectors.toList());
            return new Result<>(ResultCodeEnum.INVALID_REQUEST,fieldValidErrors);
        }
        return new Result<>(ResultCodeEnum.SUCCESS);
    }
    /**
     * 统一处request参数校验异常(requesparam对象)
     * 普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
     * @param req 请求
     * @param exception 异常
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result paramValidExceptionHandle(HttpServletRequest req, ValidationException exception) {
        log.error("ERROR ! GET REQUEST URL:"+req.getRequestURL());

        log.error("error message :"+exception.getMessage(),exception);
        if(exception instanceof ConstraintViolationException){
            ConstraintViolationException exs = (ConstraintViolationException) exception;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            List<FieldValidError> fieldValidErrors = violations.stream().map(
                    fieldError -> {
                        FieldValidError fieldValidError = returnFieldValidError("",fieldError.getMessage());
                        return fieldValidError;
                    }
            ).collect(Collectors.toList());

            return new Result<>(ResultCodeEnum.INVALID_REQUEST,fieldValidErrors);
        }
        return new Result<>(ResultCodeEnum.SUCCESS);
    }

    /**
     * 解析参数校验message="code|message"
     * @param field 属性名称
     * @param message 属性说明"code|message"
     * @return FieldValidError 自定义参数错误对象
     */
    private static FieldValidError returnFieldValidError(String field, String message){

        String [] errorMessage = message.split(SPLIT_STRING);
        FieldValidError fieldValidError = new FieldValidError();
        if(errorMessage.length > 1){
            fieldValidError.setCode(Integer.valueOf(ResultCodeEnum.INVALID_REQUEST.getCode()+errorMessage[0]));
            fieldValidError.setMessage(errorMessage[1]);
        }else{
            fieldValidError.setCode(ResultCodeEnum.INVALID_REQUEST.getCode());
            fieldValidError.setMessage(errorMessage[0]);
        }
        fieldValidError.setField(field);
        return fieldValidError;
    }
}
