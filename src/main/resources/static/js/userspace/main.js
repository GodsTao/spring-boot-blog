/*!
 *Avatar JS
 */
"use strict;"

//DOM加载完再执行
$(function(){
    var avatarApi;

    //获取编辑用户头像的界面
    $(".blog-content-container").on("click",".blog-edit-avatar",function() {
        avatarApi="/u/"+$(this).attr("username")+"/avatar";
        $.ajax({
            url:avatarApi ,
            success:function(data) {
                $("#avatarFormContainer").html(data);
            },
            error: function() {
                toastr.error("error!");
            }
        });
    });
    /**
     * 将以base64的图片url数据转换为blob
     * @param urlData
     * 用URL方式表示的base64图片数据
     */
    function convertBase64UrlToBlob(urlData) {
        var bytes= window.atob(urlData.split(",")[1]); //去掉url的头，并转换为byte
        //处理异常，将ASCII码小于0的转换为大于0
        var ab =new ArrayBuffer(bytes.length);
        var ia= new Uint8Array(ab);
        for(var i=0;i<bytes.length;i++){
            ia[i]=bytes.charCodeAt(i);
        }
        return new Blob([ab],{type: 'image/png'});
    }

    //提交用户头像的图片数据
    $("#submitEditAvatar").on("click",function(){
        var form =$("#avatarformid")[0];
        var formData =new FormData(form);
        var base64Codes =$(".cropImg>img").attr("src");
        formData.append("file",convertBase64UrlToBlob(base64Codes));

        $.ajax({
            url:fileServerUrl,
            type:'POST',
            cache:false,
            data:formData,
            processData:false,
            contentType:false,
            success:function(data) {
                var avatarUrl =data ;
                //获取CSRF TOKEN
                var csrfToken =$("meta[name='_csrf']").attr("content");

                var csrfHeader =$("meta[name='_csrf_header']").attr("content");
                //保存头像更改到数据库
                $.ajax({
                    url:avatarApi,
                    type:'POST',
                    contentType:"application/json;charset=utf-8",
                    data:JSON.stringify({"id":Number($("#userId").val()),"avatar":avatarUrl}),
                   beforeSend: function(request) {
                       request.setRequestHeader(csrfHeader,csrfToken); //增添CSRF TOKEN
                   },
                    success:function(data) {
                        if(data.success) {
                            //成功后，置换头像图片
                            $(".blog-avatar").attr("src",data.body);
                        }else{
                            toastr.error("error!"+data.message);
                        }
                    },
                    error: function() {
                        toastr.error("error2!");
                    }
                });
            },
            error: function() {
                toastr.error("error1!");
            }
        });
    });
});