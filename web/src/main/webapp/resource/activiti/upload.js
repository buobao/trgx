/**
 * Created by hpj on 2014/12/8.
 */
(function($){
    var setting = {
        actionUrl   :"",
        multi       :false,
        fileTypeDesc:"任意格式",
        fileTypeExts:"*.*",
        entityName   :""
    };
    function uploadify_onSelectError(file, errorCode, errorMsg) {
        var msgText = "上传失败<br/>";
        switch (errorCode) {
            case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
                msgText += "每次最多上传 3个文件";
                break;
            case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                msgText += "文件大小超过限制( " + this.settings.fileSizeLimit + " )";
                break;
            case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                msgText += "文件大小为0";
                break;
            case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                msgText += "文件格式不正确，仅限 " + this.settings.fileTypeExts;
                break;
            default:
                msgText += "错误代码：" + errorCode + "\n" + errorMsg;
        }
        var data = {state:"400",title:"上传过程出错",message:msgText};
        _show(data);
        return false;
    };
    function uploadify_onUploadError(file, errorCode, errorMsg, errorString){
        if (errorCode == SWFUpload.UPLOAD_ERROR.FILE_CANCELLED
            || errorCode == SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
            return;
        }
        var msgText = "上传失败<br/>";
        switch (errorCode) {
            case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                msgText += "HTTP 错误\n" + errorMsg;
                break;
            case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
                msgText += "上传文件丢失，请重新上传";
                break;
            case SWFUpload.UPLOAD_ERROR.IO_ERROR:
                msgText += "IO错误";
                break;
            case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
                msgText += "安全性错误\n" + errorMsg;
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                msgText += "每次最多上传 " + this.settings.uploadLimit + "个";
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
                msgText += errorMsg;
                break;
            case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
                msgText += "找不到指定文件，请重新操作";
                break;
            case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
                msgText += "参数错误";
                break;
            default:
                msgText += "文件:" + file.name + "\n错误码:" + errorCode + "\n"
                + errorMsg + "\n" + errorString;
        }
        var data = {state:"400",title:"上传过程出错",message:msgText};
        _show(data);
        return ;
    }
    //执行删除操作

    var methods = {
        init:function(options){
            return this.each(function(){
                var o =$.extend(o, setting);
                if (options) $.extend(o,options);
                var divMsg = '<div id="import_message_alert'+ o.entityName+'"></div>';
                var inputMsg = '<input id="'+ o.entityName+'" name="'+o.entityName+'"type="hidden"/>';
                var ol = '<ol id="'+ o.entityName+'" class="dd-list"></ol>'
                $(this).parent().append(inputMsg).append(divMsg).append(ol);
            });
        },
        upload:function(options){

            return this.each(function(){
                var o =$.extend(o, setting);
                if (options) $.extend(o,options);

                var resultMsg = '<div id="resultMsg'+ o.entityName+'" class="alert alert-info"><button class="close" data-dismiss="alert">×</button><i class="fa-fw fa fa-info"></i></div>';

                $(this).uploadify({
                    height        	: 30,
                    buttonClass     : "btn btn-primary",
                    multi           : o.multi,
                    fileSizeLimit   : '2MB',
                    fileDataName    : 'uploadify',
                    progressData    : 'percentage',
                    fileTypeDesc  : o.fileTypeDesc,
                    fileTypeExts    : o.fileTypeExts,
                    buttonText      : '选择上传文件',
                    swf             : '../resource/com/js/plugin/uploadify/uploadify.swf',
                    uploader        : o.actionUrl,
                    width           : 140,
                    uploadLimit     : 3,
                    overrideEvents  :['onDialogClose','onSelectError','onUploadError'],
                    onUploadStart   :function(file){
                        $("#import_message_alert"+ o.entityName).empty();
                        var result = '<strong>正在努力为您上传！</strong>'+file.name+' 请耐心等待...';
                        $("#import_message_alert"+ o.entityName).append(resultMsg);
                        $("#resultMsg").append(result);
                    },
                    onInit          :function(){
                        $("div#"+$(this).attr("id")+"-button").removeClass("uploadify-button").css("line-height","15px");
                    },
                    onUploadError   : uploadify_onUploadError,
                    onSelectError   : uploadify_onSelectError,
                    onUploadSuccess : function(file, data, response) {
                        $("#import_message_alert"+ o.entityName).empty();
                        var json = jQuery.parseJSON(data);
                        if(json && json.state == "200"){
                            var result = '<strong>上传成功！</strong>';
                            $("#import_message_alert"+ o.entityName).append(resultMsg);
                            $("#resultMsg"+ o.entityName).append(result);
                            var hidObj = $("input#"+ o.entityName);
                            hidObj.val()==""?hidObj.val(json.fileId):hidObj.val(hidObj.val()+","+json.fileId);

                            var li = '<li  class="dd-item" key="'+json.fileId+'"><div class="dd-handle" style="cursor:pointer"><i class="fa fa-ils fa-lg text-success"></i>'+json.name+'<div class="pull-right" id="'+json.fileId+'"></div></div></li>';
                            var downBtn = '<a id="'+json.fileId+'" class="btn btn-info  btn-xs" href="'+json.url+'"><i class="fa fa-lg fa-cloud-download"></i>下载</a>';
                            var delBtn = '<a id="'+json.fileId+'" key="del" class="btn btn-danger  btn-xs" onclick="fn_del_file(\''+json.fileId+'\')"><i class="fa fa-lg fa-trash"></i>删除</a>';


                            $("ol#"+o.entityName).append(li)
                            $("div#"+json.fileId).append(downBtn).append(delBtn);
                            $("#import_message_alert"+ o.entityName).fadeIn();
                            $("#import_message_alert"+ o.entityName).fadeOut(7000);
                        }else{
                            var result = '<strong>上传失败！</strong>';
                            $("#import_message_alert"+ o.entityName).append(resultMsg);
                            $("#resultMsg"+ o.entityName).append(result);
                        }
                    }
                });

            });
        },
        fileList:function(options){
            var oFileOpt = {
                keyId:""
            }
            return this.each(function(){
                var o =$.extend(o, setting);
                if (options)
                    $.extend(o,oFileOpt, options);
                var pdata = {keyId: o.keyId}
                $.ajax({
                    url : "../com/ajax!fileList.action",
                    method:"post",
                    cache : false,
                    dataType : "json",
                    async : false,
                    data : pdata,
                    success : function(data) {
                        if(data.num>0){
                            var json = data.dataRows;
                            var hidObj = $("input#"+ o.entityName);

                            $(data.dataRows).each(function(i,json){
                                var li = '<li  class="dd-item"><div class="dd-handle" style="cursor:pointer"><i class="fa fa-ils fa-lg text-success"></i>'+json.name+'<div class="pull-right" id="'+json.fileId+'"></div></div></li>';
                                var downBtn = '<a id="'+json.fileId+'" class="btn btn-info  btn-xs" href="'+json.url+'"><i class="fa fa-lg fa-cloud-download"></i>下载</a>';
                                var delBtn = '<a id="'+json.fileId+'" key="del" class="btn btn-danger  btn-xs"><i class="fa fa-lg fa-trash"></i>删除</a>';

                                $("ol#"+o.entityName).append(li);
                                $("div#"+json.fileId).append(downBtn);

                                hidObj.val()==""?hidObj.val(json.fileId):hidObj.val(hidObj.val()+","+json.fileId);
                            });

                        }
                    }
                });
            });
        }
    };


    $.fn.upload = function( method ) {
        if ( methods[method] ) {
            return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof method === 'object' || ! method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.multiselect2side' );
        }
    };
})(jQuery);