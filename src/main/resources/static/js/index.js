layui.use('form', function(){
    var form = layui.form,
        $ = layui.jquery;

    //监听提交
    form.on('submit(formGenerator)', function(data){
        var model = JSON.stringify(data.field);

        $.ajax({
            cache : true,
            type : "POST",
            dataType: 'json',
            url : "/analysisPerson",
            data : model,
            async : false,
            error : function() {
                layer.msg('系统异常');
            },
            success : function(data) {
                layer.msg(data);
            }
        });

        return false;
    });
});

