/**
 * Created by hpj on 2015/1/14.
 * json格式 : {"id":"8a8a8b934abce94e014abce95f090000","name":"宋鹏杰"}
 */
(function($,w){
    w.oDialog = w.oDialog || {};
    var dialog = w.oDialog;
    var setting = {
        title       :"",
        data        :"",
        type        :"checkbox",
        name        :"dlgInput",
        foot        :"show",
        callback    :function(){}
    };

    var ul  ='<div class="modal-body"><fieldset><ul class="list-group td-ul" id="group" style="width: 100%">';
    var end = '</ul></fieldset></div><div class="modal-footer"><button class="btn" id="btnCancle" data-dismiss="modal" aria-hidden="true">取消</button><button id="dialog-ok" class="btn btn-primary">确定</button></div>';

    $.extend(dialog,{
        open : function(opt){
            var o = $.extend(o, setting);
            if (opt) $.extend(o,opt);
            var list = "";

            $(o.data).each(function(i,v){
                //临时解决 （判断v.key是不是存在，存在读v.key否则v.name）
                var key = v.key ? v.key : v.name;
                list = list + '<li id="'+ v.id+'" class="list-group-item">';
                list = list +'<div class="'+o.type+'"><label>';
                list = list +'<input id="'+ v.id+'" type="'+o.type+'" name="'+ o.name+'" class="'+o.type+'box style-0" key="'+ key+'" '+v.checked+'>';
                list = list + '<span>'+ v.name+'</span>';
                list = list + '</label></div></li>';
            });
            list = ul + list + end;
            gDialog.fCreate({
                title: o.title,
                content: list,
                width:500
            }).show();
            //隐藏顶部取消按钮
            $("div.modal-header > button.close").hide();

            if(o.foot == "hide") $("#btnCancle").hide();

            $("#dialog-ok").on("click",function(){
                var obj =$("#group input:checked");
                if(obj.length > 0){
                    gDialog.fClose();
                    if(typeof o.callback == 'function'){
                        o.callback();
                    }
                }else{
                    $.smallBox({
                        title : "提示",
                        content : "<i class='fa fa-clock-o'></i> <i>请选择数据</i>",
                        color : "#C46A69",
                        iconSmall : "fa fa-times fa-2x fadeInRight animated",
                        timeout : 3000
                    });
                }

            })

        }
    })

})(jQuery,window);


(function($,w){
    w.sDialog = w.sDialog || {};
    var dialog = w.sDialog;
    var setting = {
        title       :"",
        data        :"",
        type        :"checkbox",
        name        :"dlgInput",
        foot        :"show",
        callback    :function(){}
    };

    var allUserList = '';
    var detail = '';
    var ul  ='<div><input id="dlgSearch" style="margin-left: 16px;height:30px;width: 93.6%;border: 2px solid #ccc;" placeholder="请输入查询内容"/></div><div class="modal-body" style="padding-top: 5px"><fieldset><ul class="list-group td-ul" id="group" style="width: 40%;float:left">';
    var middle = '</ul><ul class="list-group td-ul" id="dlgDetail" style="width: 60%;float:left;">';
    var end = '</ul></fieldset></div><div class="modal-footer"><button class="btn" id="btnCancle" data-dismiss="modal" aria-hidden="true">取消</button><button id="dialog-ok" class="btn btn-primary">确定</button></div>';

    var query_func = function(id,type){
        var ls = '';
        $.ajax({
            url : "ajax-memberrecord!usertasks.action",
            method:"post",
            cache : false,
            dataType : "json",
            async : false,
            data : {userId:id},
            success : function(data) {
                var detailList;
                if (type == "show") {
                    detailList = $("#dlgDetail");
                    detailList.children('li').remove();
                }
                if (data.result.errorCode == "200") {
                    var item = '';
                    $(data.data).each(function (i, v) {
                        //console.log(v.data[i]);
                        item = '<li style="margin:3px 0px 3px 3px;padding-bottom:3px;border-bottom:1px solid #ccc;" id="' + v.data[i].id + '"><span>' + v.data[i].name + '</span>[' + v.data[i].state + ']<br/><span>[所属合同：' + v.data[i].contractName + ']</span><br/><span>[任务类别：' + v.data[i].taskType + ']</span></li>';
                        if (type == "show"){
                            detailList.append(item);
                        }else{
                            ls = ls + item;
                        }
                    });
                } else if(data.result.errorCode == "500"){
                    if (type == "show") {
                        detailList.append("<li><span>查询结果有误!</span></li>");
                    }
                    ls = "<li><span>查询结果有误!</span></li>";
                } else {
                    if (type == "show") {
                        detailList.append("<li><span>该人员当前无任务信息!</span></li>");
                    }
                   ls = "<li><span>该人员当前无任务信息!</span></li>";
                }
            },
            error:function(data){
            }
        });

        return ls;
    };

    $.extend(dialog,{
        open : function(opt){
            var o = $.extend(o, setting);
            if (opt) $.extend(o,opt);
            var list = "";

            $(o.data).each(function(i,v){
                var key = v.key ? v.key : v.name;
                if (v.checked == "checked"){
                    detail = query_func(v.id,"string");
                }
                list = list + '<li id="'+ v.id+'" class="list-group-item">';
                list = list +'<div class="'+o.type+'"><label>';
                list = list +'<input id="'+ v.id+'" type="'+o.type+'" name="'+ o.name+'" class="'+o.type+'box style-0" key="'+ key+'" '+v.checked+'>';
                list = list + '<span>'+ v.name+'</span>';
                list = list + '</label></div></li>';
            });
            allUserList = list;
            allUserList = list;
            list = ul + list + middle + detail + end;
            gDialog.fCreate({
                title: o.title,
                content: list,
                width:500
            }).show();

            $("input[name='dlgInput']").click(function(){
                //alert($(this).attr("id"));
                query_func($(this).attr("id"),"show");
            });

            $("#dlgSearch").keypress(function(event){
                if (event.keyCode == 13) {
                    var searchStr = $.trim($(this).val());
                    $("#group").children('li').remove();
                    $("#dlgDetail").children('li').remove();
                    if (searchStr != null && searchStr != "") {
                        $.ajax({
                            url : "ajax-dialog!usersDlg.action",
                            method:"post",
                            cache : false,
                            dataType : "json",
                            async : false,
                            data : {key:searchStr},
                            success : function(data) {
                                var lst = "";
                                console.log(data)
                                $(data.data.data).each(function(i,v){
                                    var key = v.key ? v.key : v.name;
                                    lst = lst + '<li id="'+ v.id+'" class="list-group-item">';
                                    lst = lst +'<div class="'+o.type+'"><label>';
                                    lst = lst +'<input id="'+ v.id+'" type="'+o.type+'" name="'+ o.name+'" class="'+o.type+'box style-0" key="'+ key+'">';
                                    lst = lst + '<span>'+ v.name+'</span>';
                                    lst = lst + '</label></div></li>';
                                });
                                $("#group").append(lst);
                            },
                            error:function(data){
                            }
                        });
                    } else {
                        $("#group").append(allUserList);
                        $("#dlgDetail").append(detail);
                    }
                    $("input[name='dlgInput']").click(function(){
                        query_func($(this).attr("id"),"show");
                    });
                }
            });

            //隐藏顶部取消按钮
            $("div.modal-header > button.close").hide();

            if(o.foot == "hide") $("#btnCancle").hide();

            $("#dialog-ok").on("click",function(){
                var obj =$("#group input:checked");
                if(obj.length > 0){
                    gDialog.fClose();
                    if(typeof o.callback == 'function'){
                        o.callback();
                    }
                }else{
                    $.smallBox({
                        title : "提示",
                        content : "<i class='fa fa-clock-o'></i> <i>请选择数据</i>",
                        color : "#C46A69",
                        iconSmall : "fa fa-times fa-2x fadeInRight animated",
                        timeout : 3000
                    });
                }
                detail = '';
            });
            $("#btnCancle").on("click",function(){
                detail = '';
            });

        }
    })
})(jQuery,window);












































