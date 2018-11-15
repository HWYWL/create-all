package com.generate.controller;

import cn.hutool.json.JSONUtil;
import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.generate.model.GeneratorModel;
import com.generate.service.GeneratorService;
import com.generate.utils.MessageResult;

import java.util.List;

/**
 * 控制器
 * @author YI
 * @date 2018-11-15 11:15:19
 */
@Path
public class IndexController {
    @Inject
    private GeneratorService generatorService;

    @GetRoute(value = {"/", "/index"})
    public String index(){
        return "index.html";
    }

    @PostRoute("/analysisPerson")
    @JSON
    public MessageResult analysisPerson(Request request){
        List<String> list = request.parameterValues("model");

        if (list == null || list.isEmpty()){
            return MessageResult.errorMsg("数据不能为空！");
        }

        GeneratorModel model = JSONUtil.toBean(list.get(0), GeneratorModel.class);


        return MessageResult.ok(model);
    }
}
