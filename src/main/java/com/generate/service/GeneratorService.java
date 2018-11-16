package com.generate.service;

import com.generate.model.GeneratorModel;

/**
 * 代码生成接口
 * @author YI
 * @date 2018-11-15 14:43:29
 */
public interface GeneratorService {
    /**
     * 生成mapper
     * @param model 数据参数
     */
    void getModelAndMapper(GeneratorModel model);

    /**
     * 生成service
     * @param model 数据参数
     */
    void getService(GeneratorModel model);

    /**
     * 生成Controller
     * @param model 数据参数
     */
    void getController(GeneratorModel model);

    void coreAdapt(GeneratorModel model);
}

