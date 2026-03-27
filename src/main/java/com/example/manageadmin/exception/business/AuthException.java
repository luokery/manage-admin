package com.example.manageadmin.exception.business;

import com.example.manageadmin.cosnst.SecurityEnum;

public class AuthException extends BusinessException {
	
	private static final long serialVersionUID = 1L;
	
    public AuthException() {
        super();
    }
    
    public AuthException(Integer code, String msg) {
        super(code, msg);
    }
    
    public AuthException(SecurityEnum SecurityEnum) {
        super(SecurityEnum.getCode(), SecurityEnum.getMsg());
    }
}
