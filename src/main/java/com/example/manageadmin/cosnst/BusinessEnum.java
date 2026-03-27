package com.example.manageadmin.cosnst;

import java.text.MessageFormat;


/**
 * 系统结果异常
 * @author 
 */
public enum BusinessEnum implements ResultFace {
	
    UNKONW_ERROR(500, "未处理的运行时异常"),
    SUCCESS(0, "成功"),
    ERROR(1, "失败"),
    PARAM_c_ERROR(5, "参数转换失败"),
    PARAM_v_ERROR(6, "参数验证失败"),
    DATA_COPY_ERROR(7, "拷贝数据失败")
    , NOT_FONUT_FILE(8, "没有找到文件")
    , FILE_TYPE_ERROR(8, "文件格式错误")
    ;

    private Integer code;

    private String msg;

    BusinessEnum(Integer code, String msg) {
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