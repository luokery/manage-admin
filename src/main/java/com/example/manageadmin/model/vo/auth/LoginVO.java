package com.example.manageadmin.model.vo.auth;


import com.example.manageadmin.model.vo.user.UserVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登陆响应")
public class LoginVO {
    
	// 访问TOKEN
	@Schema(description = "访问TOKEN", example = "dasfassfasdf")
	private String token;
	
	private UserVO user;
}
