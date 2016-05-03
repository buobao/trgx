//表单保存
function form_save(form_id,actionUrl,opt,callback_func){
    var defaults={
        dialogId:null,
        msg:true
    };
    var plugin = this;
    plugin.settings = $.extend({}, defaults, opt);
    $("form input:disabled").removeAttr("disabled");

    var data = $("form#"+form_id).serialize();
    ajax_action(actionUrl,data,plugin.settings,function(pdata){
        if(plugin.settings.msg){
            _show(pdata);
        }

        if(typeof callback_func === 'function'){
            callback_func(pdata);
        }

    });

}
//ulId - 列表框ul的Id,获取dialog中选中的值
function getRowIds(ulId){
    var objChk = $("ul#"+ulId+" input:checked");
    var rowIds = [];
    if(objChk.length>0){
    	$(objChk).each(function(index,value){
    	    var rowData = $(this).attr("id");
            rowIds.push({"id":rowData});
    	});
    }
    var rowIds = JSON.stringify(rowIds);
    return rowIds;
}
//获取dialog中选中的name值
function getRowNames(ulId){
    var objChk = $("ul#"+ulId+" input:checked");
    var rowNames="";
    if(objChk.length>0){
        $(objChk).each(function(index,value){
            var rowDataName=$(this).next().attr("id");
            rowNames+=rowDataName+',';
        });
    }
    return rowNames;
}
//显示消息，data是返回的数据
function _show(data){
    var color = "#C46A69";
    var iconSmall = "fa fa-times fa-2x fadeInRight animated";
    var timeout = 6000;
    if(data.state == "200" || data.state ==200){
        color = "#659265";
        iconSmall = "fa fa-thumbs-up bounce animated";
        timeout = 4000;
    }
    $.smallBox({
        title : data.title,
        content : "<i class='fa fa-clock-o'></i> <i>"+ data.message + "</i>",
        color : color,
        iconSmall : iconSmall,
        timeout : timeout
    });
}
function _showResult(data){
    var result = data.result;
    var data = {
        state:result.errorCode,
        title:"操作状态",
        message:result.errorMessage
    }
    _show(data);
}
//发送ajax请求
function ajax_action(vActionUrl,data,opt,callback_func){
    var defaults={
    };
    var plugin = this;
    plugin.settings = $.extend({}, defaults, opt);
    $.ajax({
        url : vActionUrl,
        method:"post",
    	cache : false,
    	dataType : "json",
    	async : false,
    	data : data,
        success : function(data) {
            if(typeof callback_func === "function"){
                callback_func(data);
            }
    	},
        error:function(data){
            if(typeof plugin.settings.errorHandle === "function"){
                plugin.settings.errorHandle(data);
            }
        }
    });
}
//联动多选
function multList(ulId_first,ulId_second,actionUrl){
    $("ul#"+ulId_first+" input").each(function(index,value){
      	$(this).on("change",function(event){
            var check = $(this).is(":checked");
        	var data = {rowId:$(this).attr("id"),keyId:$("input#keyId").val()}
        	$.ajax({
                url : actionUrl,
                method:"post",
                cache : false,
                dataType : "json",
                async : false,
                data : data,
                success : function(data) {
                    var data = eval(data.rows);
                    $(data).each(function(index,value){
                        var str = "<li class='list-group-item td-li' id="+value.id+">"+
                                    "<div class='checkbox'>"+
                        	            "<label>"+
                        	                "<input id="+value.id+" type='checkbox' class='checkbox style-0' >"+
                        	                "<span id="+value.name+">"+value.name+"</span>"+
                        	            "</label>"+
                        	        "</div>"+
                        	        "</li>";
                        if(check == true){
                            $(str).appendTo("ul#"+ulId_second);
                        }else{
                            $("li#"+value.id).remove();
                        }

                    });
                }
            });
        })
    });
}



// ----- end x下拉多选 ----//

/**
 * input页面upload
 * @param opt objId--显示目标位置，entityName--实体名称，sourceId---记录已上传的Id
 */
function inputLoad(opt){
    var defaults={
        objId:null,
        entityName:null,
        sourceId:null
    };
    var o = $.extend({}, defaults, opt);
    $("#"+ o.objId).upload('init',{entityName: o.entityName});
    var fileId = $("input#"+ o.sourceId).val();
    $("#"+ o.objId).upload('fileList',{
        keyId:fileId,
        entityName: o.entityName
    });
    $("#"+ o.objId).upload('upload',{
        actionUrl:'../com/ajax!uploadFile.action',
        entityName: o.entityName,
        multi:true
    });
}
/**
 * read页面upload
 * @param opt objId--显示目标位置，entityName--实体名称，sourceId---记录已上传的Id
 */
function readLoad(opt){
    var defaults={
        objId:null,
        entityName:null,
        sourceId:null
    };
    var o = $.extend({}, defaults, opt);
    var fileId =$("input#"+ o.sourceId).val();
    $("#"+ o.objId).upload('init',{entityName: o.entityName});
    $("#"+ o.objId).upload('fileList',{
        keyId:fileId,
        entityName: o.entityName
    });
}

function doMultiDuty(pdata){
     oDialog.open({
                title:"请选择职权",
                data:pdata.data.data,
                type:"radio",
                foot:"hide",
                callback:function(){
                    var obj =$("#group input:checked");
                    if(obj.length > 0){
                          var text = "<span id='"+$(obj).attr("id")+"'>当前操作人:【"+$(obj).next().html()+"】</span>";
                          $("input#curDutyId").val($(obj).attr("id"));
                          $("header#title").append(text);
                    }else{
                      //  setTimeout(function(){doMultiDuty(pdata)},1500);
                        $.smallBox({
                            title : "提示",
                            content : "<i class='fa fa-clock-o'></i> <i>请选择职权</i>",
                            color : "#C46A69",
                            iconSmall : "fa fa-times fa-2x fadeInRight animated",
                            timeout : 5000
                        });
                    }
                } });
}
function multiDuty(pdata){
    ajax_action("../manage/ajax-dialog!dutyDlg.action",pdata,{},function(pdata){
        if(pdata.data.size > 1){
            doMultiDuty(pdata);
        }else{
            var data = pdata.data.data[0];
            if(data){
                var text = "<span id='"+data.id+"'>当前操作人:【"+data.name+"】</span>";
                $("input#curDutyId").val(data.id);
                $("header#title").append(text);
            }
        }
    });
}
function fn_del_file(id){
    console.debug(id);
    pdata = {keyId:id}
    $.ajax({
        url : "../com/ajax!delFile.action",
        method:"post",
        cache : false,
        dataType : "json",
        async : false,
        data : pdata,
        success : function(data) {
            var obj = $("li[key='"+id+"']");
            var idObj = $(obj).parent().prev().prev();

            $(idObj).val(idObj.val().replace(id,""));
            $(obj).remove();
            _show(data);
        }
    });
}
/**
 * dom Array 获取属性的方法
 * @param obj --- dom对象集合
 * @param prop --- 获取的属性
 * @returns {string}
 */
function getArrProp(obj , prop){
    var propArr = "";
    $(obj).each(function(i,v){
        propArr = propArr +","+ $(v).attr(prop)
    })
    propArr = propArr.substring(1,propArr.length);
    return propArr;
}