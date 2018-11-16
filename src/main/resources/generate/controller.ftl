package ${basePackage}.web;

import ${basePackage}.core.MessageResult;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author ${author}
 * @date ${date}
 */
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {
    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @PostMapping("/add")
    public MessageResult add(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
        return MessageResult.ok();
    }

    @PostMapping("/delete")
    public MessageResult delete(Integer id) {
        ${modelNameLowerCamel}Service.deleteById(id);
        return MessageResult.ok();
    }

    @PostMapping("/update")
    public MessageResult update(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
        return MessageResult.ok();
    }

    @PostMapping("/detail")
    public MessageResult detail(Integer id) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.findById(id);
        return MessageResult.ok(${modelNameLowerCamel});
    }

    @PostMapping("/list")
    public MessageResult list(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return MessageResult.ok(pageInfo);
    }
}
