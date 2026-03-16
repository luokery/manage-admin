package com.example.manageadmin.validation;

import jakarta.validation.groups.Default;

/**
 * 校验分组接口
 * 用于区分不同操作场景的校验规则
 */
public interface ValidateGroup extends Default {
    
    /**
     * 新增操作分组
     * 用于创建时的校验
     */
    interface Add extends ValidateGroup {}
    /**
     * 删除操作分组
     * 用于删除时的校验
     */
    interface Delete extends ValidateGroup {}
    /**
     * 更新操作分组
     * 用于更新时的校验
     */
    interface Update extends ValidateGroup {}
    /**
     * 查询操作分组
     * 用于查询时的校验
     */
    interface Query extends ValidateGroup {}
}
