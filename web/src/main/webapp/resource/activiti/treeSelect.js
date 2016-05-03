/*
 * multiSelect jQuery plugin
 *
 * Copyright (c) 2010 Giovanni Casassa (senamion.com - senamion.it)
 *
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://www.senamion.com
 * jqgrid multiSelect page
 */
(function($){
    var setting = {
        actionUrl:"",
        divId:"tree_menu",
        treeId:"pTree"
    }
    function NodeClick(event, treeId, treeNode){
        var zTree = $.fn.zTree.getZTreeObj("pTree"),
            nodes = zTree.getCheckedNodes(true),
            v = "",f = "";

        for (var i=0, l=nodes.length; i<l; i++) {
            v += nodes[i].sName + ",";
            f += nodes[i].id + ",";
        }
        if (v.length > 0 ) v = v.substring(0, v.length-1);
        if (f.length > 0 ) f = f.substring(0, f.length-1);
        var pObj = $("input#fid");
        var fObj = $("input#parentId");
        pObj.attr("value",v);
        fObj.attr("value",f);
        if(fObj.val().indexOf(',')>0){
            var data={state:400,title:"操作状态",message:"只能选择一个上级节点"};
            _show(data);
            //return (treeNode.id !== 1);
        }
    };
    function onBodyDown(event) {
        if (!(event.target.id == "fid" || event.target.id == "tree_menu" || $(event.target).parents("#tree_menu").length>0)) {
            hideMenu();
        }
    }
    function hideMenu() {
        $("#"+setting.divId).slideUp("slow");
        $("body").unbind("mousedown", onBodyDown);
    }
    var methods = {
        init:function(options){
            return this.each(function(){
                var o =$.extend(o, setting);
                var el = $(this);
                var data = el.data('dutySelect');
                if (options)
                    $.extend(o,options);
                if(!data)
                    el.data('dutySelect', o);

                var treeSetting = {
                    check: {
                        enable: true,
                        chkboxType: {"Y":"", "N":""}
                    },
                    view: {
                        dblClickExpand: false,
                        showLine: true,
                        selectedMulti: false
                    },
                    data: {
                        simpleData: {
                            enable:true,
                            idKey: "id",
                            pIdKey: "pId",
                            rootPId: ""
                        }
                    },
                    callback: {
                        onCheck: NodeClick
                    }
                };
                $.ajax({
                    type: "get",
                    url: o.actionUrl,
                    success: function(data){
                        if(data.result.errorCode == "1"){
                            var t = $("#"+ o.treeId);
                            console.debug(o.treeId);
                            t = $.fn.zTree.init(t, treeSetting, data.data.data);

                        }
                    }
                });
            });
        },
        show:function(options){
            return this.each(function() {
                var o = $.extend(o, setting);
                var el = $(this);
                var data = el.data('dutySelect');
                if (options)
                    $.extend(o, options);
                if (!data)
                    el.data('dutySelect', o);
                var obj=$("ul#"+ o.treeId);
                if(obj!=null){
                    $(obj).show();
                    $("body").bind("mousedown", onBodyDown);
                }
            })
        }
    };
    $.fn.treeSelect = function( method ) {
        if ( methods[method] ) {
            return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof method === 'object' || ! method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.multiselect2side' );
        }
    };
})(jQuery)