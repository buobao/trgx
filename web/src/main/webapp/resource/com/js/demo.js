//ie下乱码
	$.ajaxSetup({
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		cache:false
	});
$('#ribbon')
    .append(
        '<div class="demo"><span id="demo-setting"><i class="fa fa-cog txt-color-blueDark"></i></span> <form><legend class="no-padding margin-bottom-10">个性化配置</legend><section><label><input name="subscription" id="smart-fixed-nav" type="checkbox" class="checkbox style-0"><span>固定头</span></label><label><input type="checkbox" name="terms" id="smart-fixed-ribbon" class="checkbox style-0"><span>固定指示条</span></label><label><input type="checkbox" name="terms" id="smart-fixed-navigation" class="checkbox style-0"><span>固定导航</span></label></section><section><h6 class="margin-top-10 semi-bold margin-bottom-5">清除本地数据</h6><a href="javascript:void(0);" class="btn btn-xs btn-block btn-primary" id="reset-smart-widget"><i class="fa fa-refresh"></i> 重置界面</a></section> <h6 class="margin-top-10 semi-bold margin-bottom-5">皮肤选择</h6><section id="smart-styles"><a href="javascript:void(0);" id="smart-style-3" data-skinlogo="../resource/com/img/logo.png" class="btn btn-block btn-xs txt-color-white margin-right-5" style="background-color:#4E463F;"><i class="fa fa-check fa-fw" id="skin-checked"></i>默认</a><a href="javascript:void(0);" id="smart-style-1" data-skinlogo="../resource/com/img/logo-white.png" class="btn btn-block btn-xs txt-color-white" style="background:#3A4558;">雅黑</a><a href="javascript:void(0);" id="smart-style-2" data-skinlogo="../resource/com/img/logo-blue.png" class="btn btn-xs btn-block txt-color-darken margin-top-5" style="background:#fff;">亮丽</a></section></form> </div>'
)

// hide bg options
var smartbgimage =
    "<h6 class='margin-top-10 semi-bold'>Background</h6><img src='img/pattern/graphy-xs.png' data-htmlbg-url='img/pattern/graphy.png' width='22' height='22' class='margin-right-5 bordered cursor-pointer'><img src='img/pattern/tileable_wood_texture-xs.png' width='22' height='22' data-htmlbg-url='img/pattern/tileable_wood_texture.png' class='margin-right-5 bordered cursor-pointer'><img src='img/pattern/sneaker_mesh_fabric-xs.png' width='22' height='22' data-htmlbg-url='img/pattern/sneaker_mesh_fabric.png' class='margin-right-5 bordered cursor-pointer'><img src='img/pattern/nistri-xs.png' data-htmlbg-url='img/pattern/nistri.png' width='22' height='22' class='margin-right-5 bordered cursor-pointer'><img src='img/pattern/paper-xs.png' data-htmlbg-url='img/pattern/paper.png' width='22' height='22' class='bordered cursor-pointer'>";
$("#smart-bgimages")
    .fadeOut();

$('#demo-setting')
    .click(function () {
        //console.log('setting');
        $('#ribbon .demo')
            .toggleClass('activate');
    })

/*
 * FIXED HEADER
 */
$('input[type="checkbox"]#smart-fixed-nav')
    .click(function () {
        if ($(this)
            .is(':checked')) {
            //checked
            $.root_.addClass("fixed-header");
            
            var config = {"method":"set",
            		"smartHeader" : "1"
            		};
            smartConfig(config);
        } else {
            //unchecked
            $('input[type="checkbox"]#smart-fixed-ribbon')
                .prop('checked', false);
            $('input[type="checkbox"]#smart-fixed-navigation')
                .prop('checked', false);

            $.root_.removeClass("fixed-header");
            $.root_.removeClass("fixed-navigation");
            $.root_.removeClass("fixed-ribbon");
            
            var config = {"method":"set",
            		"smartHeader" : "0",
            		"smartRibbon" : "0",
            		"smartNav" : "0"
            		};
            smartConfig(config);
        }
    });

/*
 * FIXED RIBBON
 */
$('input[type="checkbox"]#smart-fixed-ribbon')
    .click(function () {
        if ($(this)
            .is(':checked')) {
            //checked
            $('input[type="checkbox"]#smart-fixed-nav')
                .prop('checked', true);

            $.root_.addClass("fixed-header");
            $.root_.addClass("fixed-ribbon");

            $('input[type="checkbox"]#smart-fixed-container')
                .prop('checked', false);
            $.root_.removeClass("container");

            var config = {"method":"set",
            		"smartHeader" : "1",
            		"smartRibbon" : "1"
            		};
            smartConfig(config);
        } else {
            //unchecked
            $('input[type="checkbox"]#smart-fixed-navigation')
                .prop('checked', false);
            $.root_.removeClass("fixed-ribbon");
            $.root_.removeClass("fixed-navigation");
            
            var config = {"method":"set",
            		"smartRibbon" : "0",
            		"smartNav" : "0"
            		};
            smartConfig(config);
        }
    });


/*
 * FIXED NAV
 */
$('input[type="checkbox"]#smart-fixed-navigation')
    .click(function () {
        if ($(this)
            .is(':checked')) {

            //checked
            $('input[type="checkbox"]#smart-fixed-nav')
                .prop('checked', true);
            $('input[type="checkbox"]#smart-fixed-ribbon')
                .prop('checked', true);

            //apply
            $.root_.addClass("fixed-header");
            $.root_.addClass("fixed-ribbon");
            $.root_.addClass("fixed-navigation");

            $('input[type="checkbox"]#smart-fixed-container')
                .prop('checked', false);
            $.root_.removeClass("container");
            
            var config = {"method":"set",
            		"smartHeader" : "1",
            		"smartRibbon" : "1",
            		"smartNav" : "1"
            		};
            smartConfig(config);
        } else {
            //unchecked
            $.root_.removeClass("fixed-navigation");
            
            var config = {"method":"set",
            		"smartNav" : "0"
            		};
            smartConfig(config);
        }
    });

/*
 * RTL SUPPORT
 */
$('input[type="checkbox"]#smart-rtl')
    .click(function () {
        if ($(this)
            .is(':checked')) {

            //checked
            $.root_.addClass("smart-rtl");

        } else {
            //unchecked
            $.root_.removeClass("smart-rtl");
        }
    });


/*
 * INSIDE CONTAINER
 */
$('input[type="checkbox"]#smart-fixed-container')
    .click(function () {
        if ($(this)
            .is(':checked')) {
            //checked
            $.root_.addClass("container");

            $('input[type="checkbox"]#smart-fixed-ribbon')
                .prop('checked', false);
            $.root_.removeClass("fixed-ribbon");

            $('input[type="checkbox"]#smart-fixed-navigation')
                .prop('checked', false);
            $.root_.removeClass("fixed-navigation");

            if (smartbgimage) {
                $("#smart-bgimages")
                    .append(smartbgimage)
                    .fadeIn(1000);
                $("#smart-bgimages img")
                    .bind("click", function () {
                        $this = $(this);
                        $html = $('html')
                        bgurl = ($this.data("htmlbg-url"));
                        $html.css("background-image", "url(" +
                            bgurl + ")");
                    })

                smartbgimage = null;
            } else {
                $("#smart-bgimages")
                    .fadeIn(1000);
            }
        } else {
            //unchecked
            $.root_.removeClass("container");
            $("#smart-bgimages")
                .fadeOut();
            // console.log("container off");
        }
    });

/*
 * REFRESH WIDGET
 */
$("#reset-smart-widget")
    .bind("click", function () {
        $('#refresh')
            .click();
        return false;
    });

/*
 * STYLES
 */
$("#smart-styles > a")
    .bind("click", function () {
        $this = $(this);
        $logo = $("#logo img");
        $.root_.removeClassPrefix('smart-style')
            .addClass($this.attr("id"));
        $logo.attr('src', $this.data("skinlogo"));
        $("#smart-styles > a #skin-checked")
            .remove();
        $this.prepend(
            "<i class='fa fa-check fa-fw' id='skin-checked'></i>"
        );
        
        //smartConfig
        var config = {"method":"set",
        		"smartStyle" : $this.attr("id")
        		};
        smartConfig(config);
        //$.post("users!theme.action", config );
    });

/**
 * 将个人配置导入到持久层
 * data = {"method":"set"}
 * */
function smartConfig(config){
	//ajax执行返回时触发
	var vActionUrl = "users!theme.action";
	$.ajax({url:vActionUrl,cache:false,dataType:"json",async:false,
		data:config,
		success: function(data){
			if(config.method == "get"){
				config = data;
				if (config.smartStyle) {
					$('#' + config.smartStyle).click();
				}
				if (config.smartHeader) {
					$('input[type="checkbox"]#smart-fixed-nav')
				    .click();
				}
				if (config.smartRibbon) {
					$('input[type="checkbox"]#smart-fixed-ribbon')
				    .click();
				}
				if (config.smartNav) {
					$('input[type="checkbox"]#smart-fixed-navigation')
				    .click();
				}
			}else{
				return ;
				/*
				if(data.state=="200"){
					//前台显示出来
					$.smallBox({
						title : "提示：",
						content : "<i class='fa fa-clock-o'></i> <i>"+data.message+"</i>",
						color : "#659265",
						iconSmall : "fa fa-thumbs-up bounce animated",
						timeout : 4000
					});
				}else{
					$.smallBox({
						title : "操作失败",
						content : "<i class='fa fa-clock-o'></i> <i>"+data.message+"</i>",
						color : "#C46A69",
						iconSmall : "fa fa-times fa-2x fadeInRight animated",
						timeout : 6000
					});
				}
				*/
			}
		}
	});
}

/**
 * 加载用户自定义数据
 * */
function loadConfig() {
	var config = {
		"method" : "get"
	};
	smartConfig(config);
}

function full_search_select(){
	//full_search_text
	$("#full_search_select li a").click(function(){
		$("li.active",$(this).parent().parent()).removeClass("active");
		$(this).parent().addClass("active");
		$("#full_search_text").text($(this).text());
		$("#full_search_text").attr("key",$(this).attr("key"));
		$("#search-clazz").val($(this).attr("key"));
	});
}

function full_search(clazz,keyName){
	loadURL("ajax!search.action?keyName="+encodeURIComponent(keyName)+"&clazz=" + clazz, $('#content'));
}

/**
 * 对该组织账号信息进行校验，提示是否设置某些信息
 * 1、组织信息管理
 * 2、成员管理
 * 3、业务组管理
 * 4、年度销售目标管理
 * 5、产品管理
 * 6、系统字典
 * */
function fn_companySetting_checked(){
	var vActionUrl = "ajax!systemSetting.action";
	$.ajax({url:vActionUrl,cache:false,dataType:"json",async:false,
		data:{},
		async : true,
		success: function(data){
			if(data.state == "200"){
				$.bigBox({
					title : "<i class='fa fa-volume-up'></i> 系统云设置提醒",
					content : "<i class='fa fa-warning'></i> 请点击【左上角】视图进入【系统云设置】依次设置以下信息：1、组织信息管理 2、成员管理 3、业务组管理 4、年度销售目标管理 5、产品管理 6、系统字典 </br><i class='fa fa-warning'></i> 进入组织信息管理可设置 【取消】 该提醒",
					color : "#3276B1",
					//timeout: 8000,
					icon : "fa fa-cog fadeInLeft animated",
					number : ""
				}, function() {
					
				});
			}else{
				return ;
			}
		}
	});
	
}

$(function(){
	//loadConfig();
	full_search_select();
	
	$(".header-search").keydown(function(event){
		var keycode = event.which;   
	     //处理回车的情况   
	     if(keycode==13){
	    	full_search($("#search-clazz").val(),$("#search-fld").val())
	    	event.preventDefault();
	     }   
	     //处理esc的情况   
	     if(keycode == 27){   
	         
	     }
	});
	$(".header-search button").click(function(event){
		full_search($("#search-clazz").val(),$("#search-fld").val())
		event.preventDefault();
	});
})

