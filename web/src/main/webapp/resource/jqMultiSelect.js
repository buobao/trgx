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
    var methods = {
        initValue:function(options){
            var o = {
                id:'id',
                name:'name',
                gridId:'ajax_user_list_table',
                inputId:'',
                inputName:''
            }
            return this.each(function(){
                var el = $(this);
                var data = el.data('multiSelect');
                if (options)
                    $.extend(o, options);
                if(!data)
                    el.data('multiSelect', o);
                var gridObj = $("#"+o.gridId);
                var inputObj = $("#"+o.inputId);
                var inputNameObj = $("#"+o.inputName);
                var idObj = $("input#selected"+o.id);
                var nameObj = $("input#selected"+o.name);

                if(idObj.val() ==  "" && inputObj !="" ){
                    idObj = inputObj;
                    nameObj = inputNameObj;
                }
                $(idObj.val().split(",")).each(function(i,v){
                    if(v!=""){
                        $(gridObj).jqGrid('setSelection',v);
                    }
                });

            });
        },
        addValue:function(options){
            var oAddOption = {
                id:'id',
                name:'name',
                gridId:'ajax_user_list_table',
                rowId:''
            }
            return this.each(function(){
                var el = $(this);
                var data = el.data('multiSelect');
                if (options)
                    $.extend(oAddOption, options);
                if(!data)
                    el.data('multiSelect', oAddOption);
                var idObj = $("input#selected"+oAddOption.id);
                var nameObj = $("input#selected"+oAddOption.name);
                var gridObj = $("#"+oAddOption.gridId);
                var rowId = oAddOption.rowId;
                $(idObj).val(rowId+","+$(idObj).val());
                var rowName = $(gridObj).jqGrid("getRowData",rowId)[oAddOption.name];
                $(nameObj).val(rowName+","+$(nameObj).val());
            });
        },
        delValue:function(options){
            var oDelOption = {
                id:'id',
                name:'name',
                gridId:'ajax_user_list_table',
                rowId:''
            }
            return this.each(function(){
                var el = $(this);
                var data = el.data('multiSelect');
                if (options)
                    $.extend(oDelOption, options);
                if(!data)
                    el.data('multiSelect', oDelOption);
                var idObj = $("input#selected"+oDelOption.id);
                var nameObj = $("input#selected"+oDelOption.name);
                var gridObj = $("#"+oDelOption.gridId);
                var rowId = oDelOption.rowId;
                $(idObj).val($(idObj).val().replace(rowId+",",""));
                var rowName = $(gridObj).jqGrid("getRowData",rowId)[oDelOption.name];
                $(nameObj).val($(nameObj).val().replace(rowName+",",""));
            });
        }
    };
    $.fn.multiSelect = function( method ) {
        if ( methods[method] ) {
          return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof method === 'object' || ! method ) {
          return methods.init.apply( this, arguments );
        } else {
          $.error( 'Method ' +  method + ' does not exist on jQuery.multiselect2side' );
        }
    };
})(jQuery);