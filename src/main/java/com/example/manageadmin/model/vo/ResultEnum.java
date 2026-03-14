package com.example.manageadmin.model.vo;

import java.text.MessageFormat;

/**
 *      200                         请求成功
 *	    400 bad request	    		常用在参数校验
 *	    401 unauthorized			未经验证的用户，常见于未登录。如果经过验证后依然没权限，应该 403(即 authentication 和 authorization 的区别)。
 *	    403 forbidden	    		无权限
 *	    404 not found	    		资源不存在
 *	    500 internal server error	非业务类异常
 *	    503 service unavaliable		由容器抛出，自己的代码不要抛这个异常
 * @author 
 */
public enum ResultEnum implements ResultFace {
	
    SUCCESS(200, "成功"),
    ERROR(800, "失败"),
    BAD_REQUEST(400, "错误的请求, 请检查"),// 400 Bad Request
    Unauthorized(401, "需要登录"),
    Forbidden(403, "没有权限"),
    NOT_FOUND(404, "资源不存在"),// 404 Not Found
    internal_server_error(500, "未知错误"),// 500 Internal Server Error
    service_unavaliable(503, "由容器抛出，自己的代码不要抛这个异常"),
    
    LoginFailure(401, "登录凭证已失效，请重新登录"),
    
    PARAM_VERIFY_ERROR(6, "参数验证失败"),
    PARAM_c_ERROR(5, "参数转换失败"),
    PARAM_v_ERROR(6, "参数验证失败"),
    SPRING_MASSAGE_ERROR(9 , "请检查参数或者请求消息格式."),
    SPRING_RESOURCE_NOT_FOUND(9 , "资源未找到."),
    PARAM_DATA_NOT_NULL(9 , "添加失败: 传入的实体为空"), 
    SQL_Integrity_Constraint_Violation_ERROR(9 , "错误: 请检查违反完整性约束."),
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

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