package com.example.manageadmin.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 日期范围校验注解
 * 用于校验开始日期必须早于结束日期
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidDateRange {

    String message() default "开始日期必须早于结束日期";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 开始日期字段名
     */
    String startField() default "startDate";

    /**
     * 结束日期字段名
     */
    String endField() default "endDate";

    /**
     * 是否允许为空（当任一日期为空时跳过校验）
     */
    boolean allowEmpty() default true;
}
