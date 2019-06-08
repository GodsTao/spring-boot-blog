$(function(){
      var _pageSize; //存储用于搜索

    //根据用户名、页面索引、页面大小获取用户列表
    function getBlogsByName(pageIndex,pageSize) {
        $.ajax({
            url:'/blogs',
            contentType: 'appliacation/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword": $("#indexkeyword").val()
            },
            success:function (data) {
                $("#mainContainer").html(data);
                var keyword = $("#indexkeyword").val();
                //如果是分类查询，则取消最新、最热选中样式
                if(keyword.length>0) {
                    $(".nav-item .nav-link").removeClass("active");
                }
            },
            error:function () {
                toastr.error("error!");
            }
        });
    }

    //分页
    // $.tbpage("#mainContainer",function(pageIndex,pageSize) {
    //     getBlogsByName(pageIndex,pageSize);
    //     _pageSize =pageSize;
    // });

    //关键字搜索
    // $("#indexsearch").click(function () {
    //     getBlogsByName(0,_pageSize);
    // });
    //
    // //最新/最热切换事件
    // $(".nav-item .nav-link").click(function () {
    //     var url = $(this).attr("url");
    //     if(url!=null) {
    //         //先移除其它的单击样式，再添加当前的单击样式
    //         $(".nav-item .nav-link").removeClass("active");
    //         $(this).addClass("active");
    //
    //         //加载其他模块的页面到右侧工作区
    //         $.ajax({
    //             url:url+'&async=true',
    //             success:function(data) {
    //                 $("#mainContainer").html(data);
    //             },
    //             error:function () {
    //                 toastr.error("error!");
    //             }
    //         })
    //         //清空搜索框内容
    //         $("#indexkeyword").val("");
    //     }
    // })

    //轮播图代码
    var imgCount = 5;
    var index = 1;
    var intervalId;
    var buttonSpan = $('.pointer')[0].children;

    //设置定时器定时轮播
    function auto() {
        intervalId = setInterval(function () {
            nextPage(true);
        },3000);
    }
    auto();
    $(".slide").mouseover(function () {
        clearInterval(intervalId);
    });

    $(".slide").mouseout(function () {
        auto();
    });
    //点击下一页 上一页的功能
    $('.left').click(function(){
        nextPage(false);
    });
    $('.right').click(function(){
        nextPage(true);
    });

    clickButtons();
    function clickButtons(){
        var length = buttonSpan.length;
        for(var i=0;i<length;i++){
            buttonSpan[i].onclick = function(){
                $(buttonSpan[index-1]).removeClass('selected');
                if($(this).attr('index')==1){
                    index = 5;
                }else{
                    index = $(this).attr('index')-1;
                }
                nextPage(true);

            };
        }
    }
    //轮播
    function nextPage(next) {
        var targetLeft = 0;
        //当前的圆点去掉on样式
        $(buttonSpan[index - 1]).removeClass('selected');
        if (next) {//往后走
            if (index == 5) {//到最后一张，直接跳到第一张
                targetLeft = 0;
                index = 1;
            } else {
                index++;
                targetLeft = -550 * (index - 1);
            }

        } else {//往前走
            if (index == 1) {//在第一张，直接跳到第五张
                index = 5;
                targetLeft = -550 * (imgCount - 1);
            } else {
                index--;
                targetLeft = -550 * (index - 1);
            }
        }
        //动画
        $('.img-list').animate({left:targetLeft+'px'});
        //更新后的圆点加上样式
        $(buttonSpan[index-1]).addClass('selected');
    }

});