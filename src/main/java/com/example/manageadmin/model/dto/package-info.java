/**
 * 
 */
/**
 * DTO是一个设计模式，主要用于在不同的层之间传输数据
 * 接口请求参数封装 前端 to Controller
 * 
 * 它的含义是Data Transfer Object，即数据传输对象。
 * 
 * 职责：跨层/跨服务数据传输，屏蔽敏感字段
 * 特征：
 * 属性集是PO的子集（如排除password字段）
 * 支持序列化（实现Serializable）
 */
package com.example.manageadmin.model.dto;