package com.example.manageadmin.exception.business;

import com.example.manageadmin.cosnst.BusinessEnum;
import com.example.manageadmin.cosnst.ResultFace;

/**
 * 业务异常
 * @author 
 */
public class BusinessException extends RuntimeException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer code;
	
    public BusinessException() {
        super();
    }
    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
    
    public BusinessException(BusinessEnum BusinessEnum) {
        super(BusinessEnum.getMsg());
        this.code = BusinessEnum.getCode();
    }
    
    public BusinessException(ResultFace ResultFaceEnum) {
        super(ResultFaceEnum.message());
        this.code = ResultFaceEnum.code();
    }
    
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
