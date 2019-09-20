package com.fangdd.framework.contract;


import com.alibaba.fastjson.annotation.JSONField;
import com.fangdd.framework.exceptions.ErrorCode;
import com.fangdd.framework.exceptions.SystemErrorCodeConst;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author lantian
 * @date 2019/08/28
 */
public class CommonResponse<T> implements java.io.Serializable {

    @JSONField(
        ordinal = 1
    )
    private String code;
    @JSONField(
        ordinal = 2
    )
    private String msg;
    @JSONField(
        ordinal = 3
    )
    private T data;

    public CommonResponse() {
    }

    public CommonResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CommonResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResponse(ErrorCode rc) {
        this.code = rc.getCode();
        this.msg = rc.getMessage();
    }

    public CommonResponse(ErrorCode rc, T data) {
        this.code = rc.getCode();
        this.msg = rc.getMessage();
        this.data = data;
    }

    public static CommonResponse create(String code, String msg, Object data) {
        return new CommonResponse(code, msg, data);
    }

    public static CommonResponse create(String code, String msg) {
        return new CommonResponse(code, msg);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public boolean isSuccess() {
        return SystemErrorCodeConst.SUCCESS.getCode().equals(this.code);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
