package com.example.manageadmin.cosnst;

import java.text.MessageFormat;



public enum SecurityEnum implements ResultFace {
	
    bad_request(400, "错误的请求, 请检查"),
    unauthorized(401, "需要登录"),
    forbidden(403, "没有权限"),
    not_found(404, "资源不存在"),
    internal_server_error(500, "未知错误"),
    
    LOGIN_SUCCESS(200, "登陆成功"),
    LOGOUT_SUCCESS(201, "已经安全退出系统"),
    WRONG_ACCOUNT_OR_PASSWORD(30, "账号或密码有误"),
    
    BUSS_DATA_NON_EXISTENT_FIELD_USERNAME(41, "用户名为空"),
    BUSS_DATA_NON_EXISTENT_FIELD_password(42, "密码不能为空"),
    BUSS_DATA_NON_EXISTENT_FIELD_CAPTCHA(43, "验证码为空"),
    BUSS_DATA_ERROR_FIELD_CAPTCHA(44, "请输入正确的验证码!"),
    LOGIN_count_fail(45, "短时间重复多次登陆失败， 请30分钟后在试"), 
    CHECK_IN_SUCCESS(202, "登陆检入成功"),
    CHECK_IN_fail(203, "登陆检入失败"),
    ;
	
    private Integer code;

    private String msg;

    SecurityEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private static final String toLogMsgPattern = "code=[{0}] {1}";
    @Override
	public String toLogMsg() {
		return MessageFormat.format(toLogMsgPattern, code, msg);
	}

	@Override
	public Integer code() {
		return code;
	}
	
	@Override
	public String message() {
		return msg;
	}
}
