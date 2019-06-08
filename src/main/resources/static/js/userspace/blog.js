/*!
 * blog.html 页面脚本.
 *
 * @since: 1.0.0 2017-03-26
 * @author Way Lau <https://waylau.com>
 */
"use strict";
$( function(){
    //处理删除博客事件
    $(".blog-content-container").on("click",".blog-delete-blog",function(){
        //获取CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader =$("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url:blogUrl,
            type:'DELETE',
            beforeSend:function (request) {
                request.setRequestHeader(csrfHeader,csrfToken); //添加 CSRF Token
            },
            success: function(data) {
                if(data.success){
                    //成功后，重定向
                    window.location =data.body;
                }else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    //获取评论列表
    function getComment(blogId) {
        $.ajax({
            url: '/comments',
            type: 'GET',
            data: {"blogId":blogId},
            success: function(data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        })
    }

    //提交评论
    $(".blog-content-container").on("click","#submitComment",function(){
        //获取CSRF Token
        var csrfToken =$("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/comments',
            type: 'POST',
            data: {"blogId":blogId ,"commentContent":$('#commentContent').val()},
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader,csrfToken);  //添加CSRF TOKEN
            },
            success: function(data) {
                if(data.success) {
                    //清空评论框
                    $('#commentContent').val('');
                    //获取评论列表
                    getComment(blogId);
                }else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    //删除评论
    $(".blog-content-container").on("click",".blog-delete-comment",function () {
        //获取CSRF TOKEN
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr('content');

        $.ajax({
            url: '/comments/'+$(this).attr("commentId")+ '?blogId='+blogId,
            type:'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader,csrfToken);
            },
            success:function(data) {
                if(data.success) {
                    //获取评论列表
                    getComment(blogId);
                }else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                    toastr.error("error!");
            }
        });
    });
    //提交点赞
    $(".blog-content-container").on("click","#submitVote",function(){
        //获取CSRF TOKEN
        var csrfToken =$("meta[name='_csrf']").attr("content");
        var csrfHeader =$("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/votes',
            type: 'POST',
            data: {"blogId":blogId},
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader,csrfToken);
            },
            success: function(data) {
                if(data.success) {
                    toastr.info(data.message);
                    //成功后重定向
                    window.location =blogUrl;
                }else{
                    toastr.error(data.message);
                }
            },
            error: function() {
                toastr.error("error!");
            }
        });
    });
    //取消点赞
    $(".blog-content-container").on("click","#cancelVote",function () {
        //获取CSRF Token
        var csrfToken =$("meta[name='_csrf']").attr("content");
        var csrfHeader =$("meta[name = '_csrf_header']").attr("content");

        $.ajax({
            url:'/votes/'+$(this).attr('voteId')+'?blogId='+blogId,
            type: "DELETE",
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader,csrfToken);
            },
            success: function(data) {
                if(data.success) {
                    toastr.info(data.message);
                    window.location=blogUrl;
                }else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        })

    })
    //初始化博客评论
    getComment(blogId);
    //随机标签颜色
    function changeColor(data){
        var red = parseInt(Math.random()*230+10).toString(16);
        var blue = parseInt(Math.random()*230+10).toString(16);
        var green= parseInt(Math.random()*230+10).toString(16);

        var bc ="#"+red+blue+green;
        data.style.backgroundColor=bc;
        console.log(bc);
    }
    $(".badge").each(function (i,element) {
        changeColor(element);
    })





});