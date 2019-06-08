//DOM加载完再执行
$(function(){
    var _pageSize; //存储用于搜素

    //根据用户名、页面索引、页面大小获取用户列表
    function getUserByUsername(pageIndex,pageSize) {
        $.ajax({
            url:"/users",
            contentType:"application/json",
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "username":$("#searchName").val()
            },
            success:function(data){
                $("#mainContainer").html(data);
            },
            error:function(){
                toastr.error("error!");
            }
        });
    }

    //分页
    $.tbpage("#mainContainer",function (pageIndex,pageSize) {
        getUserByUsername(pageIndex,pageSize);
        _pageSize =pageSize;
    });

    //搜索
    $("#searchNameBtn").click(function () {
        getUserByUsername(0,_pageSize);
    });

    //获取添加用户的界面
    $("#addUser").on("click",function(){
        $.ajax({
            url:"/users/add",
            success:function (data) {
                $("#userFormContainer").html(data);
            },
            error: function (data) {
                toastr.error("error!");
            }
        });
    });

    // 获取编辑用户的界面
    $("#rightContainer").on("click",".blog-edit-user", function () {
        $.ajax({
            url: "/users/edit/" + $(this).attr("userId"),
            success: function(data){
                $("#userFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    //提交变更后，清空表单
    $("#submitEdit").on("click",function () {
        $.ajax({
            url:"/users",
            type:"POST",
            data:$("#userForm").serialize(),  //序列化表单数据，创建以标准 URL 编码表示的文本字符串
            success: function(data) {
                $("#userForm")[0].reset();
                if (data.success){
                    //重新刷新主页面
                    getUserByUsername(0,_pageSize);
                } else{
                    toastr.error("error!");
                }
            }
        });
    });

    //删除用户
    $("#rightContainer").on("click",".blog-delete-user",function () {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader =$("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url:"/users/"+$(this).attr("userId"),
            type:"DELETE",
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader,csrfToken);  //添加CSRF 请求头
            },
            success: function(data) {
                if (data.success) {
                    //重新刷新界面
                    getUserByUsername(0,_pageSize);
                } else{
                    toastr.error(data.message);
                }
            },
            error :function () {
                toastr.error("error!");
            }
        });
    });
});