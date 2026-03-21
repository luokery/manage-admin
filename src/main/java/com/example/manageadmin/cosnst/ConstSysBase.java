package com.example.manageadmin.cosnst;

/**
 * 系统基础常量
 * @author 
 */
public interface ConstSysBase {
	
	/**
	 * 认证token
	 */
	public static final String AUTHORIZATION_KEY = "access_token";
	
	/**
	 * 日志流水号
	 */
	public static final String SERIAL_NUMBER_KEY = "SERIAL_NUMBER_TRACE_ID";
	
	/**
	 * 验证码存活时间
	 */
	public static final Long VERIFICATION_CODE_TIME = 5l;
	
	public static final String SESSION_ID_KEY_PREFIX = "session:redis:";

	public static final Integer STATUS_DALETE = 3;

	public static final String BUSINESS_LOCKKEY_project_create = "project:create";
	
}
