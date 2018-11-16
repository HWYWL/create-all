layui.use('form', function(){
    var form = layui.form,
        $ = layui.jquery;

    //监听提交
    form.on('submit(formGenerator)', function(data){
        var model = JSON.stringify(data.field);

        $.ajax({
            cache : true,
            type : "POST",
            url : "/analysisPerson",
            data : {"model":model},
            error : function() {
                layer.msg('系统异常');
            },
            success : function(data) {
                layer.open({
                    type: 1
                    ,title: false //不显示标题栏
                    ,closeBtn: false
                    ,area: '300px;'
                    ,shade: 0.8
                    ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                    ,btn: ['下载', '取消']
                    ,btnAlign: 'c'
                    ,moveType: 1 //拖拽模式，0或者1
                    ,about:true
                    ,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">下载代码<br>代码已生成,是否下载 ^_^</div>'
                    ,success: function(layero){
                        var btn = layero.find('.layui-layer-btn');
                        btn.find('.layui-layer-btn0').attr({
                            href: data.data.downloadPath
                        });
                    }
                });
            }
        });

        return false;
    });
});

