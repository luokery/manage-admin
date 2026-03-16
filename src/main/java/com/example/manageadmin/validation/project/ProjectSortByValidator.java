package com.example.manageadmin.validation.project;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

/**
 * 项目排序校验器
 */
public class ProjectSortByValidator implements ConstraintValidator<ProjectSortBy, String> {

    /**
     * 范围值：createdAt 创建时间
     */
    private static final List<String> VALID_VALUES = Arrays.asList("createdAt", "status", "startDate", "endDate", "projectCode");

    private boolean required;

    @Override
    public void initialize(ProjectSortBy constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果不是必填且值为空，则通过校验
        if (!required && value == null) {
            return true;
        }

        // 如果是必填且值为空，则不通过
        if (required && value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("项目状态不能为空").addConstraintViolation();
            return false;
        }

        // 校验状态值
        if (value != null && !VALID_VALUES.contains(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("项目状态无效，有效值为：0-已暂停，1-进行中，2-已完成").addConstraintViolation();
            return false;
        }

        return true;
    }
}
