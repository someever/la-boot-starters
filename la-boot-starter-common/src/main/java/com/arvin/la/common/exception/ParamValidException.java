package com.arvin.la.common.exception;

import java.util.List;

/**
 * 自定义 request 参数校验异常
 * Created by Kaven
 * Date: 2018/4/10
 * Desc:
 */
public class ParamValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private FieldValidError fieldValidError;

    private List<FieldValidError> fieldValidErrorList;

    public ParamValidException(){
        super();
    }

    public ParamValidException(FieldValidError fieldValidError){
        this.fieldValidError = fieldValidError;
    }

    public ParamValidException(List<FieldValidError> fieldValidErrorList){
        this.fieldValidErrorList = fieldValidErrorList;
    }


    public FieldValidError getFieldValidError() {
        return fieldValidError;
    }

    public void setFieldValidError(FieldValidError fieldValidError) {
        this.fieldValidError = fieldValidError;
    }

    public List<FieldValidError> getFieldValidErrorList() {
        return fieldValidErrorList;
    }

    public void setFieldValidErrorList(List<FieldValidError> fieldValidErrorList) {
        this.fieldValidErrorList = fieldValidErrorList;
    }
}
