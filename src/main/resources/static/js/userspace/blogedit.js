// DOM 加载完再执行
$(function() {

// 初始化 md 编辑器
    $("#md").markdown({
        language: 'zh',
        fullscreen: {
            enable: true
        },
        resize: 'vertical',
        localStorage: 'md'
    });

//插入图片

    $("#uploadImage").click(function () {
        $.ajax({
            url: fileServerUrl,  //文件服务器URL
            type: 'POST',
            cache: false,
            data: new FormData($('#uploadformid')[0]),
            processData: false,
            contentType: false,
            success: function (data) {
                var mdcontent = $('#md').val();
                $('#md').val(mdcontent +'\n![]('+ data +')\n');
            }
        }).done(function (res) {
            $('#file').val('');
        }).fail(function (res) {
        });
    });


//发布博客

    $("#submitBlog").click(function () {
        //获取CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/u/' + $(this).attr("username") + '/blogs/edit',
            type: 'POST',
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                "id": $('#blogId').val(),
                "title": $('#title').val(),
                "summary": $('#summary').val(),
                "content": $("#md").val(),
                "catalog": {"id":$("#catalogSelect").val()},
                "tags": $(".form-control-tag").val()
            }),
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); //添加CSRF TOKEN请求头
            },
            success: function (data) {
                if (data.success) {
                    //成功后，重定向
                    window.location = data.body;
                } else {
                    toastr.error("error" + data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    $('.form-control-tag').tagsInput({
        'defaultText':'输入标签'
    });
});