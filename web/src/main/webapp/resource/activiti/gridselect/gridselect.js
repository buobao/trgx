/**
 * Created by wei on 2015/11/19.
 */
(function($){
    var defaults={
        table:"",
        rowId:"",
        status:"",
        id:"",
        name:"",
        type:"",
        callback:function(){}
    }
    var methods={
        onecheck:function(options){
            return this.each(function(){
                var o =options ? $.extend(o, defaults,options) : $.extend(o,options);
                var datarows=$("#"+ o.table).jqGrid("getRowData", o.rowId);
                var ids = $("#"+ o.id).val();
                var names = $("#"+ o.name).val();
                var type= o.type;
               if(type=="radio"){
                   $("#"+ o.id).val(o.rowId);
                   $("#"+ o.name).val(datarows.name);
               }else{
                   if (ids != ""){
                       var idsArr = ids.split(",");
                       var namesArr = names.split(",");
                       var isSelected = false;
                       for (var i=0; i<idsArr.length;i++){
                           if (idsArr[i] == o.rowId){
                               isSelected = true;
                               break;
                           }
                       }
                       if (o.status){  //选中时
                           if (!isSelected){
                               $("#"+o.id).val(ids + "," + o.rowId);
                               $("#"+ o.name).val(names.substring(0,names.length)+","+datarows.name);
                           }
                       }
                       else{
                           if (isSelected) {
                               if (idsArr.length < 2){
                                   $("#"+ o.id).val("");
                                   $("#"+ o.name).val("");
                               }
                               else{
                                   for (var i = 0; i < idsArr.length; i++) {
                                       if (idsArr[i] == o.rowId) {
                                           for (var j = i; j < idsArr.length - 1; j++) {
                                               idsArr[j] = idsArr[j + 1];
                                               namesArr[j] = namesArr[j + 1];
                                           }
                                           idsArr.length = idsArr.length - 1;
                                           namesArr.length = namesArr.length - 1;
                                           break;
                                       }
                                   }
                                   $("#"+ o.id).val(idsArr.join(","));
                                   $("#"+ o.name).val(namesArr.join(","));
                               }
                           }
                       }
                   }
                   else{
                       if(o.rowId==""){

                       }

                       $("#"+ o.id).val(o.rowId);
                       $("#"+ o.name).val(datarows.name);
                   }
               }
            });
        },
        morecheck:function(options){
            return this.each(function(){
                var o =options ? $.extend(o, defaults,options) : $.extend(o,options);
                var ids = $("#"+ o.id).val();
                var names = $("#"+ o.name).val();
                var this_names = new Array();
                for (var s=0;s< o.rowId.length;s++){
                    this_names.push($("#"+ o.table).jqGrid("getRowData", o.rowId[s]).name);
                }
                if (ids != "") {
                    var idsArr = ids.split(",");
                    var namesArr = names.split(",");
                    ids.substring(0,ids.Length-1);
                    names.substring(0,names.Length-1);
                    for (var i = 0; i < o.rowId.length; i++) {
                        var isSelected = false;
                        var index = -1;
                        for (var j = 0; j < idsArr.length; j++) {
                            if (o.rowId[i] == idsArr[j]) {
                                isSelected = true;
                                index = j;
                                break;
                            }
                        }
                        if (o.status) {
                            if (!isSelected) {
                                idsArr.push(o.rowId[i]);
                                namesArr.push($("#"+o.table).jqGrid("getRowData", o.rowId[i]).name);
                            }
                        }
                        else {
                            if (isSelected) {
                                for (var n = index; n < idsArr.length - 1; n++) {
                                    idsArr[n] = idsArr[n + 1];
                                    namesArr[n] = namesArr[n + 1];
                                }
                                idsArr.length = idsArr.length - 1;
                                namesArr.length = namesArr.length - 1;
                            }
                        }
                    }
                    $("#"+ o.id).val(idsArr.join(","));
                    $("#"+ o.name).val(namesArr.join(","));
                }
                else{
                    $("#"+ o.id).val(o.rowId.join(","));
                    $("#"+ o.name).val(this_names.join(","));
                }
            });
        }
    }

    $.fn.gridselect = function( method ) {
        if ( methods[method] ) {
            return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof method === 'object' || ! method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.multiselect2side' );
        }
    };
})(jQuery);