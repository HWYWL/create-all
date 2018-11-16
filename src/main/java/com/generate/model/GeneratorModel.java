package com.generate.model;

import lombok.Data;

/**
 * 数据参数
 * @author YI
 * @date 2018-11-15 12:02:38
 */
@Data
public class GeneratorModel {

    /**
     * 数据库jdbc连接参数
     */
    public String jdbcUrl;
    public String jdbcUserName;
    public String jdbcPassWord;
    public String port;

    /**
     * 创建者
     */
    public String author;

    /**
     * 数据库名
     */
    public String databaseName;

    /**
     * 数据库表名
     */
    public String tableName;

    /**
     * 项目基础包路径
     */
    public String basePackagePath;

    /**
     * 生成的Service存放路径
     */
    public String packagePathService;

    /**
     * 生成的Service实现存放路径
     */
    public String packagePathServiceImpl;

    /**
     * 生成的Controller实现存放路径
     */
    String packagePathController;

    /**
     * 下载地址
     */
    String downloadPath;
}
