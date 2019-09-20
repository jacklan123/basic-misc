package com.fangdd.framework.exceptions;

import java.io.Serializable;

/**
 * @author lantian
 * @date 2019/08/28
 * 异常码
 */
public class ErrorCode implements Serializable {
    private static final long serialVersionUID = -6223401254481225194L;
    private String code;
    private String message;

    public ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
