package com.fangdd.framework.exceptions;

/**
 * @author lantian
 * @date 2018/08/28
 */
public class SystemErrorCodeConst {
    public static final ErrorCode SUCCESS = new ErrorCode("200", "SUCCESS.");
    public static final ErrorCode JSON_FORMAT_ERROR = new ErrorCode("10000", "Json格式不正确.");
    public static final ErrorCode LOGIN_FAILURE_ERROR = new ErrorCode("10001", "您的账户信息已过期,请重新登录.");
    public static final ErrorCode PARAM_ERROR = new ErrorCode("10002", "参数错误");
    public static final ErrorCode SERVER_ERROR = new ErrorCode("10003", "服务器打了个盹儿~");
    public static final ErrorCode NOT_FOUND = new ErrorCode("10004", "找不到这条记录");
    public static final ErrorCode INSERT_ERROR = new ErrorCode("10005", "增加数据失败");
    public static final ErrorCode UPDATE_ERROR = new ErrorCode("10006", "更新失败");
    public static final ErrorCode OPERATE_FAILED = new ErrorCode("10007", "操作失败,请稍后再试");
    public static final ErrorCode REMOTE_SERVICE_FAILED = new ErrorCode("10008", "远程服务异常");


    public SystemErrorCodeConst() {
    }
}