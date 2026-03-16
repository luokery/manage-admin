package com.example.manageadmin.validation.project;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 项目排序字段校验注解
 * 有效值：0-已暂停，1-进行中，2-已完成
 */
//@Target({ElementType.FIELD, ElementType.PARAMETER}) // 参数校验: 字段
@Target({ElementType.TYPE})// 参数校验: 类
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ProjectSortByValidator.class)
public @interface ProjectSortBy {

    String message() default "项目排序字段无效, 或不在排序字段范围.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否允许为空
     */
    boolean required() default false;
}