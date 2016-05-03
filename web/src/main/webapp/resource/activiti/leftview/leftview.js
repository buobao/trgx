/**
 * Created by hpj on 2015/1/15.
 */
(function($){
    var setting = {
        title:"",
        actionUrl:"",
        content:"",
        data:"",
        callback:function(){}
    }
    var content = '<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6" id="left-view-box" style="display:none;z-index:1000;position: fixed;right:0;top:90px">';
    content = content +'<div class="row">';
    content = content +'<div class="col-sm-12">';
    content = content +'<div class="modal-dialog demo-modal" style="margin-top:0">';
    content = content + '<div class="modal-content">';
    content = content + '<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="left-view-box-close"><i class="fa fa-times"></i></button><button style="margin-right:20px" type="button" class="close" data-dismiss="modal" aria-hidden="true" id="left_box_btn_top"><i class="fa fa-arrow-circle-up"></i></button><button style="margin-right:20px" type="button" class="close" data-dismiss="modal" aria-hidden="true" id="left_box_btn_bottom"><i class="fa fa-arrow-circle-down"></i></button><h4 class="modal-title" id="left_box_title"><i class="fa fa-quote-left "></i> 查看消息 <i class="fa fa-quote-right "></i></h4></div>';
    content = content +'';
    content = content +'</div>';
    content = content +'</div>';
    content = content +'</div>';
    content = content +'</div>';
    content = content +'</div>';

    var body = '<div class="modal-body no-padding"><div id="left-view-body" class="chat-body custom-scroll"><i class="fa fa-spinner fa-spin"></i>，正在努力为您加载，请稍后...</div></div>';

    var foot = '<div class="chat-footer">';
    foot = foot + '<div class="textarea-div" style="display:none"><div class="typearea"><textarea class="inputorr" placeholder="请填写审批意见..." name="comment" id="chat_textarea-expand" class="custom-scroll"></textarea></div></div>';
    foot = foot + '<span class="textarea-controls"><button id="left_foot_btn_close" data="" class="btn btn-sm btn-primary pull-right">关闭</button></span>';
    foot = foot + '</div>';
    var box_total_height = $(window).height();
    $.left_box ,$.left_box_close,$.left_box_body;

    function initDom(){
        if(!$("#left-view-box").html()){
            $("#content").append(content);
        }
        if(!$("div.modal-body").html()){
            $("div.modal-content").append(body);
        }
        if(!$("div.chat-footer").html()){
            $("div.modal-body").append(foot);

        }
        $.left_box = $("#left-view-box");
        $.left_box_close = $("#left-view-box-close");
        $.left_box_body = $("#left-view-body");

        //设置高度

        $.left_box_body.height(box_total_height-200);
    }
    function destoryDom(){
        $.left_box.effect( "drop", null, 500 ,function(){
            //$("div.modal-body").remove();
            $("div#left-view-box").remove();
        });

    }
    var methods = {
        init:function(options){
            return this.each(function(){
                var o =options ? $.extend(o, setting,options) : $.extend(o,options);
                initDom();
                //显示左侧滑动
                $.left_box.effect( "slide", null, 430 );
                //设置标题
                var titleInfo = "<i class='fa fa-quote-left '></i> "+o.title+" <i class='fa fa-quote-right '></i>";
                $("#left_box_title").html(titleInfo);

                //载入数据
                $.left_box_body.empty();
                if(o.actionUrl)
                    loadURL(o.actionUrl, $.left_box_body,{data: o.data});
                if(o.content)
                    $.left_box_body.append(o.content);

                //关闭侧滑
                $("#left-view-box-close,#left_foot_btn_close").off("click").on("click",function(e){
                    destoryDom();
                    e.preventDefault();
                    e.stopPropagation();
                })
                //上下查找操作
                $("#left_box_btn_top").click(function(e){
                    $.left_box_body.animate({
                        scrollTop: 0
                    }, 300);
                    event.preventDefault();
                });
                $("#left_box_btn_bottom").click(function(e){
                    $.left_box_body.animate({
                        scrollTop: $.chat_body[0].scrollHeight
                    }, 300);
                    event.preventDefault();
                });
            });

        },
        foot:function(options){
            return this.each(function(){
                var o =options ? $.extend(o, setting,options) : $.extend(o,options);
                $("div.textarea-div").show();
                var data = {keyId: o.unid};
                var content = "";
                var showDuty = false;
                $.left_box_body.height(box_total_height-420);
                //发送ajax请求按钮类型
                ajax_action("../manage/ajax-config!operateType.action",data,null,function(pdata){
                    var area = $("span.textarea-controls");
                    $(pdata.data.datarows).each(function(i,v){
                        var str = '<button id="left_foot_btn_'+ v.action+'" data="" class="btn btn-sm btn-primary pull-right">'+ v.name+'</button>';
                        $(area).append(str);
                        if(v.action == "approve" || v.action=="sendback"  || v.action=="deny"){
                            showDuty = true;
                        }
                    })
                });
                if(showDuty == true){
                    var pdata = {
                        keyId: o.unid,
                        flowName: o.flowName
                    };
                    multiDuty(pdata);
                }
                $("span.textarea-controls").append(content);
                if(typeof o.callback == 'function'){
                    o.callback();
                }
            });
        },
        leftClose:function(options){
            return this.each(function(){
                var o =options ? $.extend(o, setting,options) : $.extend(o,options);
                //关闭侧滑
                destoryDom();

            });
        }
    }

    $.fn.leftview = function( method ) {
        if ( methods[method] ) {
            return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof method === 'object' || ! method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.multiselect2side' );
        }
    };
})(jQuery,window);