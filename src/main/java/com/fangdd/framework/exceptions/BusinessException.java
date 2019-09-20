package com.fangdd.framework.exceptions;

/**
 * @author lantian
 * @date
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 3149156546197834931L;
    private String code = "500";
    private String errorMsg = "服务器开小差了~";

    public BusinessException() {
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.errorMsg = message;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
        this.errorMsg = errorCode.getMessage();
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.errorMsg = message;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
