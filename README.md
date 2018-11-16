# create-all
代码生成器，想你所想

## 简介
这是一个代码生成器，基于Blade MVC框架编写，可生成dao、service、controller各层的基础框架。

### 打包
```
mvn clean package -DskipTests
```

### 部署
```
java -jar create-all.jar
```

### 效果图
![](https://i.imgur.com/ZoYuTea.jpg)

### 生成的代码
![](https://i.imgur.com/IprQZQm.jpg)

举个controller层的代码实例
```java
/**
 *
 * @author YI
 * @date 2018-11-16 16:04:48
 */
@RestController
@RequestMapping("/baike")
public class BaikeController {
    @Resource
    private BaikeService baikeService;

    @PostMapping("/add")
    public MessageResult add(Baike baike) {
        baikeService.save(baike);
        return MessageResult.ok();
    }

    @PostMapping("/delete")
    public MessageResult delete(Integer id) {
        baikeService.deleteById(id);
        return MessageResult.ok();
    }

    @PostMapping("/update")
    public MessageResult update(Baike baike) {
        baikeService.update(baike);
        return MessageResult.ok();
    }

    @PostMapping("/detail")
    public MessageResult detail(Integer id) {
        Baike baike = baikeService.findById(id);
        return MessageResult.ok(baike);
    }

    @PostMapping("/list")
    public MessageResult list(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Baike> list = baikeService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return MessageResult.ok(pageInfo);
    }
}
```

### 问题建议

- 联系我的邮箱：ilovey_hwy@163.com
- 我的博客：http://www.hwy.ac.cn
- GitHub：https://github.com/HWYWL