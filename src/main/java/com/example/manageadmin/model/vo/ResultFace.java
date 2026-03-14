package com.example.manageadmin.model.vo;


public interface ResultFace {
	
	static final String toLogMsgPattern = "code=[{0}] {1}";
	
	/**
	 * 回去代码
	 * @return
	 */
	Integer code();
	/**
	 * 获取消息
	 * @return
	 */
	String message();
	
	/**
	 * 输出日志消息
	 * @return
	 */
	String toLogMsg();
}
