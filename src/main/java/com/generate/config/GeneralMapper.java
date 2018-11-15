package com.generate.config;

import tk.mybatis.mapper.common.*;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * 定制MyBatis Mapper插件接口，可以参考官方文档自行添加
 * @author YI
 * @date 2018-6-27 16:04:15
 */
public interface GeneralMapper<T> extends
        Mapper<T>,
        MySqlMapper<T>,
        BaseMapper<T>,
        ConditionMapper<T>,
        IdsMapper<T>,
        InsertListMapper<T> {

}
