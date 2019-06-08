//获取用户登录页

function getLoginPage(){
    $(".register-title").removeClass("border-active");
    $(".login-title").addClass("border-active");
    $.ajax({
        url:'/toLogin',
        success:function (data) {
            $("#form-container").html(data);
        },
        error:function () {
            toastr.error("error!");
        }
    });
}

//获取用户注册页
function getRegisterPage(){
    $(".login-title").removeClass("border-active");
    $(".register-title").addClass("border-active");
    $.ajax({
        url:'/toRegister',
        success:function (data) {
            $("#form-container").html(data);
        },
        error:function () {
            toastr.error("error!");
        }
    });
}

//提交登录表单
$("#LRDialog").on("click","#submitLogin",function (){
    var username=$("#username");
    var password=$("#password");
    var checkL =true;
    //检验用户名
    if (username.val().trim()=="") {
        username.addClass("is-invalid");
        $("#username-errorText").text("用户名不能为空").show();
        checkL=false;
    }else if (username.val().length>20) {
        username.addClass("is-invalid");
        $("#username-errorText").text("用户名长度不能大于20").show();
        checkL = false;
    }else if(!/^[A-Za-z0-9]+$/.test(username.val())){
        username.addClass("is-invalid");
        $("#username-errorText").text("用户名格式为英文或者数字").show();
        checkL = false;
    }else {
        username.removeClass("is-invalid");
        $("#username-errorText").hide();
    }
    //检验密码
    if (password.val().trim()=="") {
        password.addClass("is-invalid");
        $("#password-errorText").text("密码不能为空").show();
        checkL =false;
    }else if (password.val().length<6 || password.val().length>16) {
        password.addClass("is-invalid");
        $("#password-errorText").text("密码长度在6-16之间").show();
        checkL=false;
    }else{
        password.removeClass("is-invalid");
        $("#password-errorText").hide();
    }

    if (!checkL) {
        return false;
    }else {
        $.ajax({
            url:"/login",
            type:"POST",
            data:$("#loginForm").serialize(),  //序列化表单数据，创建以标准 URL 编码表示的文本字符串
            success: function(data) {
                $("#loginForm")[0].reset();
                //重新刷新主页面

                window.location.reload();
            },
            error:function () {
                toastr.error("error!");
            }
        });
    }
});

//提交注册表单
$("#LRDialog").on("click","submitRegister",function () {
    var username=$("#username");
    var password=$("#password");
    var email = $("#email");
    var again = $("#again-errorText");
    var reg= /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)/;
    var checkR =true;

    //检验用户名
    if (username.val().trim()=="") {
        username.addClass("is-invalid");
        $("#username-errorText").text("用户名不能为空").show();
        checkR=false;
    }else if (username.val().length>20) {
        username.addClass("is-invalid");
        $("#username-errorText").text("用户名长度不能大于20").show();
        checkR = false;
    }else if(!/^[A-Za-z0-9]+$/.test(username.val())){
        username.addClass("is-invalid");
        $("#username-errorText").text("用户名格式为英文或者数字").show();
        checkR = false;
    }else {
        username.removeClass("is-invalid");
        $("#username-errorText").hide();
    }

    //检验邮箱
    if (email.val().trim()==""){
        email.addClass("is-invalid");
        $("#email-errorText").text("邮箱不能为空").show();
        checkR = false;
    }else if (reg.test(email.val())) {
        email.addClass("is-invalid");
        $("#email-errorText").text("邮箱不能为空").show();
        checkR =false;
    }else{
        password.removeClass("is-invalid");
        $("#password-errorText").hide();
    }

    //检验密码
    if (password.val().trim()=="") {
        password.addClass("is-invalid");
        $("#password-errorText").text("密码不能为空").show();
        checkR =false;
    }else if (password.val().length<6 || password.val().length>16) {
        password.addClass("is-invalid");
        $("#password-errorText").text("密码长度在6-16之间").show();
        checkR=false;
    }else{
        password.removeClass("is-invalid");
        $("#password-errorText").hide();
    }

    //检验确认密码
    if (again.val().trim()=="") {
        again.addClass("is-invalid");
        $("#again-errorText").text("确认密码不能为空").show();
        checkR =false;
    }else if (again.val()!=password.val()) {
        again.addClass("is-invalid");
        $("#again-errorText").text("两次密码输入不一致").show();
        checkR=false;
    }else{
        again.removeClass("is-invalid");
        $("#again-errorText").hide();
    }

    if(!checkR) {
        return false;
    }else {
    }
});



