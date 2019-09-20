package com.fangdd.framework.controller;

import com.fangdd.framework.contract.RestHeader;
import com.fangdd.framework.contract.CommonResponse;
import com.fangdd.framework.exceptions.BusinessException;
import com.fangdd.framework.exceptions.ErrorCode;
import com.fangdd.framework.exceptions.SystemErrorCodeConst;
import org.springframework.http.HttpHeaders;

import java.util.IllegalFormatConversionException;
import java.util.List;

/**
 * @author lantian
 * @date 2019/08/28
 */
public abstract class BaseController {

    public BaseController() {
    }

    protected RestHeader getRestHeader(HttpHeaders headers) {
        RestHeader header = new RestHeader();
        return header.initialize(headers);
    }

    protected <T> CommonResponse<T> buildResponse(ErrorCode code, T data) {
        return CommonResponse.create(code.getCode(), code.getMessage(), data);
    }

    protected <T> CommonResponse<T> buildResponse(T data) {
        return CommonResponse.create(SystemErrorCodeConst.SUCCESS.getCode(), SystemErrorCodeConst.SUCCESS.getMessage(), data);
    }

    protected CommonResponse<Void> buildResponse(ErrorCode code) {
        return CommonResponse.create(code.getCode(), code.getMessage());
    }

    protected CommonResponse<Void> buildResponse() {
        return CommonResponse.create(SystemErrorCodeConst.SUCCESS.getCode(), SystemErrorCodeConst.SUCCESS.getMessage());
    }

    protected CommonResponse<Void> buildResponseWithParam(ErrorCode code, List<Object> params) {
        try {
            Object[] tParams = params == null ? null : params.toArray();
            return CommonResponse.create(code.getCode(), String.format(code.getMessage(), tParams));
        } catch (IllegalFormatConversionException var4) {
            return CommonResponse.create(code.getCode(), code.getMessage());
        }
    }

    protected <T> CommonResponse<T> buildResponseWithParam(ErrorCode code, T data, List<Object> params) {
        try {
            Object[] tParams = params == null ? null : params.toArray();
            return CommonResponse.create(code.getCode(), String.format(code.getMessage(), tParams), data);
        } catch (IllegalFormatConversionException var5) {
            return CommonResponse.create(code.getCode(), code.getMessage(), data);
        }
    }

    protected <T> void checkNotNull(T reference, ErrorCode ErrorCode) {
        if (reference == null) {
            throw new BusinessException(ErrorCode);
        }
    }

    protected void checkState(boolean expression, ErrorCode ErrorCode) {
        if (!expression) {
            throw new BusinessException(ErrorCode);
        }
    }

    protected void checkGreaterThanZero(Integer param, ErrorCode ErrorCode) {
        if (param == null || param <= 0) {
            throw new BusinessException(ErrorCode);
        }
    }

    protected void checkGreaterThanZero(Long param, ErrorCode ErrorCode) {
        if (param == null || param <= 0L) {
            throw new BusinessException(ErrorCode);
        }
    }

    protected void checkGreaterThanZero(Float param, ErrorCode ErrorCode) {
        if (param == null || (double)param <= 0.0D) {
            throw new BusinessException(ErrorCode);
        }
    }

    protected void checkGreaterThanZero(Double param, ErrorCode ErrorCode) {
        if (param == null || param <= 0.0D) {
            throw new BusinessException(ErrorCode);
        }
    }

}
