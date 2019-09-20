package com.fangdd.framework.config;

import com.fangdd.framework.contract.CommonResponse;
import com.fangdd.framework.exceptions.BusinessException;
import com.fangdd.framework.exceptions.SystemErrorCodeConst;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lantian
 * @date 2019/08/30
 */
@ControllerAdvice
@Component
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = {BusinessException.class})
    @ResponseBody
    public CommonResponse handleBusinessException(HttpServletResponse res, BusinessException e) throws Exception {
        LOGGER.error(e.getMessage(), e);
        res.setContentType("application/json");
        return new CommonResponse(e.getCode(), e.getErrorMsg());
    }

    @ExceptionHandler(RpcException.class)
    @ResponseBody
    public CommonResponse resolveRpcException(HttpServletResponse res, RpcException e) throws Exception {
        LOGGER.error("rpc code : {}  message : {}", e.getCode(), e.getMessage(), e);
        res.setContentType("application/json");
        return new CommonResponse(SystemErrorCodeConst.REMOTE_SERVICE_FAILED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResponse resolveException(HttpServletResponse res, Exception e) throws Exception {
        LOGGER.error("处理异常----",e.getMessage(), e);
        res.setContentType("application/json");
        return new CommonResponse(SystemErrorCodeConst.SERVER_ERROR);
    }

}
