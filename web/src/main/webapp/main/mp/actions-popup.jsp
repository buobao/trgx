<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!-- 
<a id="" href="#popupMenu" data-rel="popup" class="ui-btn ui-btn-right ui-corner-all ui-shadow ui-btn-inline ui-icon-plus ui-btn-icon-left ui-btn-a ui-btn-icon-notext">销售7步</a>
 -->
<a href="#nav-panel"  class="ui-btn ui-shadow ui-btn-inline ui-icon-bullets ui-btn-a ui-corner-all ui-btn-icon-left">&nbsp;</a>
<h1 style="float:left;margin: 0 10px;"></h1>
<div data-role="controlgroup" data-type="horizontal" class="ui-mini ui-btn-right">

		    <a id="pro_signin" mp-panel-id="proinfo-listview-panel" href="#" class="ui-btn ui-shadow ui-btn-inline ui-icon-check ui-btn-a ui-corner-all ui-btn-icon-left">签到</a>
            <a id="pro_feedback" mp-panel-id="proinfo-listview-panel" href="#" class="ui-btn ui-shadow ui-btn-inline ui-icon-forward ui-btn-a ui-corner-all ui-btn-icon-left">反馈</a>
            <a id="pro_leave" mp-panel-id="proinfo-listview-panel" href="#" class="ui-btn ui-shadow ui-btn-inline ui-icon-grid ui-btn-a ui-corner-all ui-btn-icon-left">请假</a>
            <%--<a href="#nav-panel"  class="ui-btn ui-shadow ui-btn-inline ui-icon-bullets ui-btn-a ui-corner-all ui-btn-icon-left">更多</a>--%>
           	<!-- ui-btn-icon-notext -->
</div>
<%--<div data-role="popup" id="popupMenu" data-theme="b" data-history="false" data-corners="false">--%>
    <%--<a href="#" data-rel="back" class="ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-left">Close</a>--%>
	    <%--<ul data-role="listview" data-inset="true" style="min-width:180px;">--%>
			<%--<li data-icon="tag"><a id="pro_signin" mp-panel-id="proinfo-listview-panel" >签到</a></li>--%>
			<%--<li data-icon="user"><a id="pro_feedback" mp-panel-id="proinfo-listview-panel" >反馈</a></li>--%>
		<%--</ul>--%>
<%--</div>--%>
<!-- 该用户相关的实施中项目选择框 -->
<div data-role="panel" data-position="right" data-position-fixed="true" data-display="overlay" data-theme="a" id="proinfo-listview-panel"
     mp-url="ajax-proinfo!listRelated.action" >
    <div id="scroller">
    <div id="proHandleType" style="display: none"></div>
    <form class="ui-filterable" style="height: 25px">
        <input data-type="search" style="height: 30px;" placeholder="按项目名称搜索"  id="panel-search"/>
    </form>
    <ul data-role="listview"
        mp-page="1" mp-keyword=""
        data-filter-placeholder="按项目名称搜索" data-input="#panel-search" data-filter="true">
    </ul>
    <hr/>
    <div>
        <a href="#" key="prePage" class="ui-btn ui-btn-icon-notext ui-corner-all ui-shadow ui-mini ui-btn-inline ui-icon-carat-l "> 上一页</a>
        <a href="#" key="nextPage" class="ui-btn ui-btn-icon-notext ui-corner-all ui-shadow ui-mini ui-btn-inline ui-icon-carat-r ">下一页 </a>
				<span class="ui-btn-inline">
					页数  <span key="pageNumber1"></span>/<span key="pageCount1"></span> 共<span key="totalCount1"></span>条
				</span>
    </div>
        <%--<!-- 上拉按钮 new -->--%>
        <%--<div id="pullUp">--%>
            <%--<span class="pullUpIcon"></span><span class="pullUpLabel">上拉显示更多...</span>--%>
        <%--</div>--%>
    </div>
</div>
<script type="text/javascript">

    $(function(){
        //点击签到按钮
        $("#pro_signin").click(function(){
            $( "#popupMenu" ).popup( "close" );
            $( "#"+ $(this).attr("mp-panel-id") ).panel( "open" );
            $("#proHandleType").text("signin");
        });
        //点击反馈按钮
        $("#pro_feedback").click(function(){
            $( "#popupMenu" ).popup( "close" );
            $( "#"+ $(this).attr("mp-panel-id") ).panel( "open" );
            $("#proHandleType").text("feedback");
        })
        //点击请假按钮
        $("#pro_leave").click(function(){
            $( "#popupMenu" ).popup( "close" );
            $( "#"+ $(this).attr("mp-panel-id") ).panel( "open" );
            $("#proHandleType").text("leave");
        })


        //滑动显示更多
//        $("div[data-role='page']").on("swiperight",function(){
//            //alert("right");
//            $( "#nav-panel" ).panel( "open" );
//        });
    });
</script>
<script>
    document.addEventListener('touchmove', function(e) {
       // e.preventDefault();
    }, false);
    $(function(){
        loadListview($("#proinfo-listview-panel"),{},function(rdata){ });
       // loadListviewScroll($("#proinfo-listview-panel"),{},function(rdata){loaded()});
        $("#back_to_top").click(function(e){
            myScroll.scrollToElement('#scroller li:nth-child(1)', 300)
            e.preventDefault();
            e.stopPropagation();
        });
    });
</script>
