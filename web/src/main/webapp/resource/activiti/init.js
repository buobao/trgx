/**
 * Created by hpj on 2015/1/13.
 */
function loadTree(data,parent){
    var obj = "<ul><li id='"+data.id+"'>";
    obj = obj + "<a href='"+data.url+"' title='"+ data.name+"'>"
    obj = obj + "<i class='fa fa-lg fa-fw "+ data.icon+"'></i><span class='menu-item-parent'>"+ data.name;
    obj = obj + "</span></a></li></ul>";
    $(parent).append(obj);
    if(data.child){
        loadTree(data.child,$("li#"+data.id))
    }


}
$(function(){
    /*
    //加载一级导航
    ajax_action("ajax-permission!navList.action",{},{},function(data){
        var obj = $("div#shortcut ul");
        $(data.dataRows).each(function(i,v){
            var li = "<li id='"+ v.id+"' key='"+ v.key+"'><a href='index.action?_t="+ v.key+"' class='jarvismetro-tile big-cubes "+ v.color+"'>";
            var span = li+"<span class='iconbox'> <i class='fa "+v.icon+" fa-4x'></i><span>"+ v.name+"</span></span>"+"</a><li>";
            $(obj).append(span);
        });

    });
    var liObj = $("div#shortcut li:first");
    $(liObj).addClass("selected");
    //读取二级导航
    var _t = $("input#_t").val();
    var unid = $(liObj).prop("id");
    $("div#shortcut li").each(function(i,v){
        if($(v).attr("key") == _t){
            unid = $(v).prop("id");
            $(v).find("a").addClass("selected");
        }else{
            $(v).find("a").removeClass("selected");
        }
    });

    ajax_action("ajax-permission!leftList.action",{keyId:unid},{},function(data){
        $(data.dataRows).each(function(i,v){
            var obj = "<li id='"+ v.id+"'>";
            obj = obj + "<a href='"+v.url+"' title='"+ v.name+"'>"
            obj = obj + "<i class='fa fa-lg fa-fw "+ v.icon+"'></i><span class='menu-item-parent'>"+ v.name;
            obj = obj + "</span></a></li>";
            $("ul#treeMenu").append(obj);
            if(v.child){
                $(v.child).each(function(i,child){
                    loadTree(child,$("li#"+ v.id));
                })
            }
        });

        checkURL();
    });*/


});