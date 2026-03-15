package com.example.manageadmin.interceptors;

import java.util.Date;
import java.util.List;
import java.util.Random;


import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import com.example.manageadmin.cosnst.ConstSysBase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 请求处理:
 * 1. 进入前(预处理): (预处理) a.增加请求流水号 TODO 流水号设计(未完成) b. TODO 包装响应报文(未完成)
 * 2. 处理后(渲染前): 在试图渲染前处理(无)
 * 3. 结  束(终结前): a.打印执行时间. TODO 需要评估性能不建议(未完成)(预处理)
 * @author izhemo
 */
@Component
public class SerialNumberInterceptor implements HandlerInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(SerialNumberInterceptor.class);
	
	/**
	 * 预处理回调
	 * true表示继续流程
	 * false表示流程中断: 手动设置 response返回结果
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		StringBuilder serialNo = new StringBuilder();
		serialNo.append( FastDateFormat.getInstance("yyyyMMddHHmmssSSS").format( new Date()));
		serialNo.append( getRandomNum());
		
		// 日志中的流水号
		MDC.put( ConstSysBase.SERIAL_NUMBER_KEY, serialNo.toString());
		
		String path = request.getServletPath();
//		// 放入request中
//		request.setAttribute(ConstSysBase.BUSINESS_NO_KEY, serialNo.toString());
//		
//		String method = request.getMethod();
//		switch(method) {
//		case "GET":
//			
//			break;
//		default:
//			//ResponseVO<UserVO> add(@RequestBody RequestVO
//			String body = null;
//	        if (request instanceof RequestWrapper) {
//				RequestWrapper requestWrapper = new RequestWrapper(request);
//		        body = requestWrapper.getBody();
//	        }
//	        log.info("body--- {}", body);
//		}
		log.info("处理开始--- 生成请求流水拦截: path={} ", path);
		return true;
	}
	
	/**
	 * 结束回调
	 * 注意, 只有preHandle true的时候才处理
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		log.info("处理结束---");
		MDC.clear();
	}

    /**
     * 随机生成六位数验证码
     * @return
     */
    public static int getRandomNum(){
        Random random = new Random();
        return random.nextInt(900000)+100000;
    }
}
