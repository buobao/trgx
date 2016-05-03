<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
	<script>
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		    WeixinJSBridge.call('hideToolbar');
		    WeixinJSBridge.call('hideOptionMenu');
		});
	</script>
	<link rel="stylesheet" href="../resource/mp/jquery.mobile/jquery.mobile-1.4.5.min.css">
	<link href="../resource/mp/fonts/css/font-awesome.min.css?_v=20141110" rel="stylesheet">
	<script src="../resource/mp/jquery.mobile/js/jquery-2.0.2.min.js"></script>
	<script src="../resource/mp/js/iscroll.js"></script>
	<!-- 把这个index.js研究透了，基本上jquery mobile几个重要的功能就了解透了，参考
	http://api.jquerymobile.com/hashchange/ 
	<script src="../resource/mp/_assets/js/index.js"></script>-->
	<script src="../resource/mp/jquery.mobile/jquery.mobile-1.4.5.min.js"></script>
	
	<!-- 加载mobileScroll -->
	
	<script src="../resource/mp/jquery.mobile/mobiscroll/js/mobiscroll.zepto.js"></script>
	<script src="../resource/mp/jquery.mobile/mobiscroll/js/mobiscroll.core.js"></script>
	<script src="../resource/mp/jquery.mobile/mobiscroll/js/mobiscroll.scroller.js"></script>
	
	<script src="../resource/mp/jquery.mobile/mobiscroll/js/mobiscroll.datetime.js"></script>
	<script src="../resource/mp/jquery.mobile/mobiscroll/js/mobiscroll.select.js"></script>
	
	<script src="../resource/mp/jquery.mobile/mobiscroll/js/mobiscroll.scroller.android-ics.js"></script>
	<script type="text/javascript" src="../resource/mp/jquery.mobile/mobiscroll/js/mobiscroll.i18n.zh.js"></script>
    <script type="text/javascript" src="../resource/com/common.js?v=20150505"></script>
	
	<link rel="stylesheet" href="../resource/mp/jquery.mobile/mobiscroll/css/mobiscroll.scroller.css?_v=20140611">
	<link rel="stylesheet" href="../resource/mp/jquery.mobile/mobiscroll/css/mobiscroll.scroller.android-ics.css?_v=20140611">
	<link rel="stylesheet" href="../resource/mp/jquery.mobile/mobiscroll/css/mobiscroll.animation.css?_v=20140611">
	<!-- end mobileScroll -->

	<style>

	h1.ui-left{
		float:left;text-align:left;margin: 0 60px;
	}
	.subtitle{
		color: #726969;
		font-weight: normal;
		font-size: 14px;
		padding-left: 3px;
	}
	.title{
		color: #0e5986;
	}
	div.author {
		font-size: 12px;
		font-weight: normal;
		color: #999797;
		}
	div.author>span{
		float: right;
		margin-right: 30px;
	}
	.ui-input-search{
		margin-top:0;
		margin-bottom:0;

	}
		.label-readonly{
			color:#666666
		}
	#chart_div{
		z-index:-1;height: 500px; width: 800px;margin-top: -20px;padding-bottom: -20px;margin-left: -40px;margin-right: -20px;
	}
	#search-mini{
		margin-top: 6px;
	}

	#listview-content h1 {
		font-size: 13px;
	}

	#comment-listview-content{
		font-size: 13px;
	}

	.ui-mobile label, div.ui-controlgroup-label{
		font-size: 13px;
	}
	.ui-content input, .ui-content select, .ui-content textarea, .ui-content button,.ui-content .ui-btn, .ui-content span{
		font-size: 13px;
	}

	#pullDown,#pullUp {
		border-bottom: 0!important;
		background-color: #f9f9f9!important;
	}
	/*.ui-header{*/
		/*background-color: #00A2ED!important;*/
	/*}*/
	.ui-header h1{
		color: #ffffff;
		text-shadow: none!important;
	}
	#search .list-search{
		background-color: #f9f9f9!important;
	}

	.ui-input-text input, .ui-input-search input, textarea.ui-input-text{
		padding-top:1px;
		padding-bottom: 1px;
		font-size: 12px;

	}
	</style>
	<script>
	//监控浏览器，去除微信操作框
	$( document ).on( "pageinit", function() {
	    //$( "#popupMenu" ).on({
	    $("[data-role=popup]").on({
	        popupbeforeposition: function() {
	        	//document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    		//});
	        	//显示的时候开始执行
	        	try { 
	        		WeixinJSBridge.call('hideToolbar');
	    		    WeixinJSBridge.call('hideOptionMenu');
	        	} catch (e) { 
		        	
	        	}
	        },
	        popupafterclose: function() {
	        	try { 
	        		WeixinJSBridge.call('hideToolbar');
	    		    WeixinJSBridge.call('hideOptionMenu');
	        	} catch (e) { 
		        	
	        	}
	        }
	    });
	    //页面初始化设置时间戳
	    $("#timestamp").val(Date.parse(new Date()));
	    
	    
	    $("#window_close").click(function(e){
			WeixinJSBridge.invoke('closeWindow',{},function(res){
				
			});
			return ;
		})
	});
	//绑定beforeunload事件
	function bindClose(){
		$(window).bind('beforeunload',function(){return '提示：未保存数据可能会丢失。';});
	}
	//解除绑定，一般放在提交触发事件中，放在result成功内 $(window).unbind('beforeunload');
	function unbindClose(){
		$(window).unbind('beforeunload');
	}
	function hide(){
		$.mobile.loading( "hide" );
	}
	function SetCookie(name,value)//两个参数，一个是cookie的名子，一个是值
	{
	    var Days = 365; //此 cookie 将被保存 30 天
	    var exp = new Date();    //new Date("December 31, 9998");
	    exp.setTime(exp.getTime() + Days*24*60*60*1000);
	    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
	}
	function getCookie(name)//取cookies函数       
	{
	    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	     if(arr != null) return unescape(arr[2]); return null;
	}
	function delCookie(name)//删除cookie
	{
	    var exp = new Date();
	    exp.setTime(exp.getTime() - 1);
	    var cval=getCookie(name);
	    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
	}
	
	function loadListview(container,vdata,callback){
		$.mobile.loading( "show", {
            text: "正在努力为您加载...",
            textVisible: true,
            theme: "b",
            textonly: false,
            html: ""
	    });
		//loadListview
		//container->url    container ul->page keyword
		var url = container.attr("mp-url");
		var page = 1;
		var pageKeyword = "";
		var $ul = $("ul:first",container);
		
		if($ul.length > 0){
			page = $("ul:first",container).attr("mp-page");
			pageKeyword = $("ul:first",container).attr("mp-keyword");
		}
		//alert(url + " " + page + " " + pageKeyword)
		$.extend( vdata, {page:page,pageKeyword:pageKeyword} );
		$.ajax({
			type : "POST",
			url : url,
			dataType : 'html',
			data : vdata,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
			cache : false, // (warning: this will cause a timestamp and will call the request twice)
			async:true,
			beforeSend : function() {
				// cog placed
				//container.html( "<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>" );
            	//$ul.listview( "refresh" );
            	
				/**/ if (container[0] == $("#content")[0]) {
					//drawBreadCrumb();
					// update title with breadcrumb...
					//document.title = _title + " - " + $(".breadcrumb li:last-child").text();
					// scroll up
					$("html, body").animate({
						scrollTop : 0
					}, "fast");
				} else {
					container.animate({
						scrollTop : 0
					}, "fast");
				} 
			},
			/*complete: function(){
		    	// Handle the complete event
		    	// alert("complete")
			},*/
			success : function(data) {
				hide();
				// cog replaced here...
				// alert("success")
				$ul.css({
					opacity : '0.0'
				}).html(data).delay(50).animate({
					opacity : '1.0'
				}, 300);
				
				if($ul.length > 0){
					$ul.listview("refresh");
					$ul.trigger( "updatelayout");
				}
				
				var pageNumber = $("input[name=pageNumber1]",$ul).val();
				var pageCount = $("input[name=pageCount1]",$ul).val();
				var pageKeyword = $("input[name=pageKeyword1]",$ul).val();
				var totalCount = $("input[name=totalCount1]",$ul).val();
				//alert("view"+pageNumber + "-" + pageCount + "-" + pageKeyword)
				
				$("span[key=pageNumber1]").text(pageNumber);
				$("span[key=pageCount1]").text(pageCount);
				$("span[key=totalCount1]").text(totalCount);
				
				if(parseInt(pageNumber)>1){
					$("a[key=prePage]").show();
				}else{
					$("a[key=prePage]").hide();
				}
				if(parseInt(pageNumber)>=parseInt(pageCount)){
					$("a[key=nextPage]").hide();
				}else{
					$("a[key=nextPage]").show();
				}
				$ul.attr("mp-page",pageNumber);
				$ul.attr("mp-keyword",pageKeyword);
				
				$("a[key=prePage]",container).unbind('click').bind("click",function(e){
					var pageNumber = $ul.attr("mp-page");
					$ul.attr("mp-page",parseInt(pageNumber)-1);
					loadListview(container,{},callback);
					e.preventDefault();
					e.stopPropagation();
				})
				$("a[key=nextPage]",container).unbind('click').bind("click",function(e){
					var pageNumber = $ul.attr("mp-page");
					$ul.attr("mp-page",parseInt(pageNumber)+1);
					loadListview(container,{},callback);
					e.preventDefault();
					e.stopPropagation();
				})
				
				callback({});
			},
			error : function(xhr, ajaxOptions, thrownError) {
				container.html('<h4 style="margin-top:10px; display:block; text-align:left"><i class="fa fa-warning txt-color-orangeDark"></i> 错误 404! 页面没找到！</h4>');
			}
		});
	}
	
	function confirmDialog(text, callback) {
		var popupDialogId = 'popupDialog';
		$('<div data-role="popup" id="' + popupDialogId + '" data-confirmed="no" data-transition="pop" data-overlay-theme="b" data-theme="b" data-dismissible="false" style="max-width:500px;"> \
		                    <div data-role="header" data-theme="a">\
		                        <h1>Question</h1>\
		                    </div>\
		                    <div role="main" class="ui-content">\
		                        <h3 class="ui-title">' + text + '</h3>\
		                        <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionConfirm" data-rel="back">Yes</a>\
		                        <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionCancel" data-rel="back" data-transition="flow">No</a>\
		                    </div>\
		                </div>')
		    .appendTo($.mobile.pageContainer);
		var popupDialogObj = $('#' + popupDialogId);
		popupDialogObj.trigger('create');
		popupDialogObj.popup({
		    afterclose: function (event, ui) {
		        popupDialogObj.find(".optionConfirm").first().off('click');
		        var isConfirmed = popupDialogObj.attr('data-confirmed') === 'yes' ? true : false;
		        $(event.target).remove();
		        if (isConfirmed && callback) {
		            callback();
		        }
		    }
		});
		popupDialogObj.popup('open');
		popupDialogObj.find(".optionConfirm").first().on('click', function () {
		    popupDialogObj.attr('data-confirmed', 'yes');
		});
	}
	
	/**批注相关JS**/
	function sendComment(parentId,createrId,text,targetId,targetObject,callback){
		$("#comment-popup2 button").hide();
		$( "#comment-popup2" ).popup( "close");
		$( "#comment-popup2 textarea" ).val("");
		
		//parentId='<s:property value="#list.id"/>' createrName='<s:property value="#list.creater.name"/>' createrId='<s:property value="#list.creater.id"/>'
		var vdata = {parentId:parentId,toUsersId:createrId,text:text,targetId:targetId,targetObject:targetObject};
		$.ajax({
			   type: "POST",
			   url: 'ajax-comment!save.action',
			   cache:false,dataType:"json",
			   data: vdata,
			   async:true,
			   success: function(data){
				 hide();
				 $("#comment-popup2 button").show();
				 if(data.state == "200"){
					 $("#chart_user").val("");
					 $("#hi_ids").val("");
					 $("#comment-popup2 textarea" ).attr("placeholder","请填写您的批注信息");
						//同上点击批注列表信息
						loadListview($("#comment-listview-content"),{},function(){

					 })
					 callback();
			     }else{
			    	 alert(data.message);
			     }
			   }
		}); 
	}
	//审批操作
	function sendApproveComment(parentId,createrId,text,targetId,targetObject,callback,isAgree){
		$("#comment-popup3 button").hide();
		$( "#comment-popup3" ).popup( "close");
		$( "#comment-popup3 textarea" ).val("");
		
		//parentId='<s:property value="#list.id"/>' createrName='<s:property value="#list.creater.name"/>' createrId='<s:property value="#list.creater.id"/>'
		var vdata = {parentId:parentId,toUsersId:createrId,text:text,targetId:targetId,targetObject:targetObject,isAgree:isAgree};
		$.ajax({
			   type: "POST",
			   url: 'comment!approve.action',
			   cache:false,dataType:"json",
			   data: vdata,
			   async:true,
			   success: function(data){
				 hide();
				 $("#comment-popup3 button").show();
				 if(data.result.errorCode == 1 || data.result.errorCode == "1"){
					 $("#comment-popup2 textarea" ).attr("placeholder","请填写您的批注信息");
						//同上点击批注列表信息
						loadListview($("#comment-listview-content"),{},function(rdata){
				    		$("a[key=comment-popup2]").unbind('click').bind("click",function(e){
						    	$( "#comment-popup2" ).data("parentId",$(this).attr("parentId")).data("createrId",$(this).attr("createrId"));
				    			$( "#comment-popup2 textarea" ).attr("placeholder","@"+$(this).attr("createrName"));
						    	$( "#comment-popup2" ).popup( "open", {transition:'pop',positionTo:'window'} );
						    })
					 })
					 callback();
			     }else{
			    	 //$( "#comment-popup3" ).popup( "open");
			    	 //alert(data.result.errorMessage);
			     }
			   }
		}); 
	}
	
	//可拉动刷新视图的公共参数
	 var myScroll;
     var pullDownEl;
     var pullDownOffset;
     var pullUpEl;
     var pullUpOffset;
     
     function pullDownAction() {
             setTimeout(function() {
             		//loadListviewScroll($("#listview-content"),{},function(rdata){});
             		$("#pullUp").attr("end","false");
             		pullUpEl = document.getElementById('pullUp');
 					//pullUpEl.className ='';
                     pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉显示更多...';
                     
             		var container = $("#listview-content");
             		$("ul:first",container).attr("mp-page",1);
             		loadListviewScroll(container,{},function(rdata){myScroll.refresh();});
                      // 刷新
             }, 1000); //1秒
     }
     function pullUpAction() {
             setTimeout(function() {
             		var container = $("#listview-content");
             		var pageNumber = $("ul:first",container).attr("mp-page");
             		$("ul:first",container).attr("mp-page",parseInt(pageNumber)+1);
             		loadListviewScroll(container,{},function(rdata){myScroll.refresh();}); 
             		
             }, 1000);
     }
     function loaded() {//加载完成
             pullDownEl = document.getElementById('pullDown');
             pullDownOffset = pullDownEl.offsetHeight;
             pullUpEl = document.getElementById('pullUp');
		     $("#pullUp").show();
             pullUpOffset = pullUpEl.offsetHeight;
             myScroll = new iScroll(
                             'listview-content',
                             {
                             		scrollbarClass: 'myScrollbar', /* 重要样式 */
                                     useTransition : true,
                                     topOffset : pullDownOffset,
                                     onRefresh : function() {
                                             if (pullDownEl.className.match('loading')) {
                                                     pullDownEl.className = '';
                                                     pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
                                             } else if (pullUpEl.className.match('loading')) {
                                                     pullUpEl.className = '';
                                                     pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉显示更多...';
                                             }
                                     },
                                     onScrollMove : function() {
                                             if (this.y > 5 && !pullDownEl.className.match('flip')) {
                                                     pullDownEl.className = 'flip';
                                                     pullDownEl.querySelector('.pullDownLabel').innerHTML = '准备刷新...';
                                                     this.minScrollY = 0;
                                             } else if (this.y < 5
                                                             && pullDownEl.className.match('flip')) {
                                                     pullDownEl.className = '';
                                                     pullDownEl.querySelector('.pullDownLabel').innerHTML = '准备刷新...';
                                                     this.minScrollY = -pullDownOffset;
                                             } else if (this.y < (this.maxScrollY - 5)
                                                             && !pullUpEl.className.match('flip')) {
                                                     pullUpEl.className = 'flip';
                                                     pullUpEl.querySelector('.pullUpLabel').innerHTML = '准备刷新...';
                                                     this.maxScrollY = this.maxScrollY;
                                             } else if (this.y > (this.maxScrollY + 5)
                                                             && pullUpEl.className.match('flip')) {
                                                     pullUpEl.className = '';
                                                     pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉显示更多...';
                                                     this.maxScrollY = pullUpOffset;
                                             }
                                     },
                                     onScrollEnd : function() {
                                             if (pullDownEl.className.match('flip')) {
                                                     pullDownEl.className = 'loading';
                                                     pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Loading...';
                                                     pullDownAction(); // Execute custom function (ajax call?)
                                             } else if (pullUpEl.className.match('flip')) {
                                                     pullUpEl.className = 'loading';
                                                     pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Loading...';
                                                     pullUpAction(); // Execute custom function (ajax call?)
                                             }
                                     }
                             });

             /* setTimeout(function() {
                     document.getElementById('listview-content').style.left = '0';
             }, 800); */
     }

     
     
     function loadListviewScroll(container,vdata,callback){
		 $("#pullUp").show();
     	if($("#pullUp").attr("end")=="true"){
     		pullUpEl = document.getElementById('pullUp');
			pullUpEl.className = '';
            pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载完毕，没有更多了...';
			//3秒后隐藏
			setTimeout(function(){
				$("#pullUp").hide();
			},3000);
            return ;
     	}
 		$.mobile.loading( "show", {
             text: "正在努力为您加载...",
             textVisible: true,
             theme: "b",
             textonly: false,
             html: ""
 	    });
 		//loadListview
 		//container->url    container ul->page keyword
 		var url = container.attr("mp-url");
 		var page = 1;
 		var pageKeyword = "";
 		var $ul = $("ul:first",container);
 		
 		if($ul.length > 0){
 			page = $("ul:first",container).attr("mp-page");
 			pageKeyword = $("ul:first",container).attr("mp-keyword");
 		}
 	//	alert("Scroll"+url + " " + page + " " + pageKeyword)
 		$.extend( vdata, {page:page,pageKeyword:pageKeyword} );
 		$.ajax({
 			type : "POST",
 			url : url,
 			dataType : 'html',
 			data : vdata,
 			async:true,
 			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
 			cache : false, // (warning: this will cause a timestamp and will call the request twice)
 			beforeSend : function() {
 				// cog placed
 				//container.html( "<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>" );
             	//$ul.listview( "refresh" );
             	
 				/**/ if (container[0] == $("#content")[0]) {
 					//drawBreadCrumb();
 					// update title with breadcrumb...
 					//document.title = _title + " - " + $(".breadcrumb li:last-child").text();
 					// scroll up
 					$("html, body").animate({
 						scrollTop : 0
 					}, "fast");
 				} else {
 					container.animate({
 						scrollTop : 0
 					}, "fast");
 				} 
 			},
 			/*complete: function(){
 		    	// Handle the complete event
 		    	// alert("complete")
 			},*/
 			success : function(data) {
 				hide();
 				// cog replaced here...
 				// alert("success")
 				//判断是否下拉刷新，还是上拉加载，清楚已经加载的隐藏字段
 				$("input[name=pageNumber]",$ul).remove();
 				$("input[name=pageCount]",$ul).remove();
 				$("input[name=pageKeyword]",$ul).remove();
 				$("input[name=totalCount]",$ul).remove();
 				
 				if(parseInt(page)==1){
 					$ul.css({
     					opacity : '0.0'
     				}).html(data).delay(50).animate({
     					opacity : '1.0'
     				}, 300);
 				}else{
 					$ul.css({
     					opacity : '0.0'
     				}).append(data).delay(50).animate({
     					opacity : '1.0'
     				}, 300);
 				}
 				
 				
 				if($ul.length > 0){
 					$ul.listview("refresh");
 					$ul.trigger( "updatelayout");
 				}
 				
 				var pageNumber = $("input[name=pageNumber]",$ul).val();
 				var pageCount = $("input[name=pageCount]",$ul).val();
 				var pageKeyword = $("input[name=pageKeyword]",$ul).val();
 				var totalCount = $("input[name=totalCount]",$ul).val();
 				//alert("Scroll:"+pageNumber + "-" + pageCount + "-" + pageKeyword)
 				
 				$("span[key=pageNumber]").text(pageNumber);
 				$("span[key=pageCount]").text(pageCount);
 				$("span[key=totalCount]").text(totalCount);
 				
 				/* if(parseInt(pageNumber)>1){
 					$("a[key=prePage]").show();
 				}else{
 					$("a[key=prePage]").hide();
 				} */
 				if(parseInt(pageNumber)>=parseInt(pageCount)){
 					//没有更多时候
 					pullUpEl = document.getElementById('pullUp');
 					pullUpEl.className = '';
                     pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载完毕，没有更多了...';
                     $("#pullUp").attr("end","true");
					//5秒后隐藏
					setTimeout(function(){
						$("#pullUp").hide();
					},3000);
 					//$("a[key=nextPage]").hide();
 				}else{
 					//$("a[key=nextPage]").show();
 				}
 				$ul.attr("mp-page",pageNumber);
 				$ul.attr("mp-keyword",pageKeyword);
 				
 				/* $("a[key=prePage]",container).unbind('click').bind("click",function(e){
 					var pageNumber = $ul.attr("mp-page");
 					$ul.attr("mp-page",parseInt(pageNumber)-1);
 					loadListviewScroll(container,{},callback);
 					e.preventDefault();
 					e.stopPropagation();
 				})
 				$("a[key=nextPage]",container).unbind('click').bind("click",function(e){
 					var pageNumber = $ul.attr("mp-page");
 					$ul.attr("mp-page",parseInt(pageNumber)+1);
 					loadListviewScroll(container,{},callback);
 					e.preventDefault();
 					e.stopPropagation();
 				}) */
 				callback({});
 			},
 			error : function(xhr, ajaxOptions, thrownError) {
 				container.html('<h4 style="margin-top:10px; display:block; text-align:left"><i class="fa fa-warning txt-color-orangeDark"></i> 错误 404! 页面没找到！</h4>');
 			}
 		});
 	}
	</script>
	<script type="text/javascript">
	function listSearch(){
		$('#search-mini').textinput({ clearSearchButtonText: "x" });
		$("a#search-button").bind("click",function(){
			var searchObj = $("#search-mini").val();
			$("ul:first",$("#listview-content")).attr("mp-keyword",searchObj);
			pullDownAction();
		});
		$("a.ui-input-clear").unbind("click").bind("click",function(){
			$("#search-mini").val("");
			$("ul:first",$("#listview-content")).attr("mp-keyword","");
			pullDownAction();
		})
	}
	
	// 对Date的扩展，将 Date 转化为指定格式的String
	// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
	// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
	// 例子： 
	// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
	// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
	Date.prototype.Format = function (fmt) { //author: meizz 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}

	var countdown=120;
	function settime(btn) {
		if (countdown == 0) {
			btn.button({ mini: true });
			btn.attr("value","获取验证码");
			btn.text("获取验证码");
			btn.button( "refresh" );
			btn.button( "enable" );
			countdown = 120;
		} else {
			btn.button({ mini: true });
			btn.attr("value","重新发送(" + countdown + ")");
			btn.text("重新发送(" + countdown + ")");
			btn.button( "refresh" );
			btn.button( "disable" );
			countdown--;

			setTimeout(function() {
				if (countdown >= 0) {
					settime(btn)
				}
			},1000)
		}
	}
</script>

