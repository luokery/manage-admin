package com.example.manageadmin.model.vo.project;

import java.text.MessageFormat;

import com.example.manageadmin.model.vo.ResultEnum;
import com.example.manageadmin.model.vo.ResultFace;

/**
 * 用户异常
 * 
 *      200                         请求成功
 *	    400 bad request	    		常用在参数校验
 *	    401 unauthorized			未经验证的用户，常见于未登录。如果经过验证后依然没权限，应该 403(即 authentication 和 authorization 的区别)。
 *	    403 forbidden	    		无权限
 *	    404 not found	    		资源不存在
 *	    500 internal server error	非业务类异常
 *	    503 service unavaliable		由容器抛出，自己的代码不要抛这个异常
 * @author 
 */
public enum ProjectEnum implements ResultFace {
    
    PARAM_c_ERROR(5, "参数转换失败"),
    PARAM_v_ERROR(6, "参数验证失败"), 
    FILE_v_0001(7, "参数校验失败"),
    BUSS_v_ERROR(8, "业务处理异常"),
    BUSS_DATA_CRT_ERROR(9, "数据拷贝失败!"), 
    
    BUSS_DATA_NON_EXISTENT(10, "数据不存在!"),
    BUSS_delete_Batch_ids_ERROR(11, "批量删除错误, id不存在!"),
    BUSS_DATA_NON_EXISTENT_FIELD(12, "数据校验失败: 字段为空"),
    BUSS_DATA_NON_EXISTENT_DELETE(13, "批量删除失败: 用户编号解析失败."),
    BUSS_DATA_NON_EXISTENT_PAGE(14, "分页: 没有查到数据."),
    BUSS_DATA_USER_NAME_EXISTENT(154, "错误: 用户名存在."),
    
    
    PRODUCT_NOT_FOUND(404, "商品未找到"),
    PRODUCT_already_exists(404, "商品已经存在"), 
    //-------------------------------------------------------------------
    LIST_SUCCESS( ResultEnum.SUCCESS.code(), "获取项目列表成功"), 
    Details_SUCCESS( ResultEnum.SUCCESS.code(), "获取项目详情成功")
    ;

    private Integer code;

    private String msg;

    ProjectEnum(Integer code, String msg) {
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