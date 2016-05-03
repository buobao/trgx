<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<div data-role="panel" data-position-fixed="true" data-display="overlay" data-theme="b" id="nav-panel">
	<ul data-role="listview">
	    <li data-icon="delete"><a href="#" data-rel="close"><i class="fa fa-angle-double-left"></i> 关闭更多</a></li>
        <li data-role="list-divider">工程泛联客</li>
	    <li><a data-ajax="false" href="ajax!client.action"><i class="fa fa-sitemap"></i> 客户一览</a></li>
	    <li><a data-ajax="false"  href="ajax!linkman.action"><i class="fa fa-user"></i> 联系人一览</a></li>
		<li ><a href="ajax!proInfo.action" data-ajax="false" ><i class="fa fa-calendar"></i> 项目一览</a></li>
		<li ><a href="ajax!proSignin.action" data-ajax="false"><i class="fa fa-comments-o"></i> 考勤一览</a></li>
		<li><a href="ajax!proBack.action" data-ajax="false"><i class="fa fa-check-square"></i> 反馈一览</a></li>
		<li ><a href="ajax!comment.action" data-ajax="false"><i class="fa fa-flag"></i> 消息一览</a></li>
		<li ><a href="ajax!announce.action" data-ajax="false"><i class="fa fa-list"></i> 通知一览</a></li>
		<li ><a href="ajax-users!input.action" data-ajax="false"><i class="fa fa-list"></i> 修改密码</a></li>
	    <%--<li><a data-ajax="false"  href="comment!list.action"><i class="fa fa-comment-o"></i> 批注</a></li>--%>
		<%--<li><a data-ajax="false"  href="message!list.action"><i class="fa fa-bell"></i> 通知</a></li>--%>
	</ul>
</div><!-- /panel -->
	    
<div data-role="footer" data-position="fixed" data-theme="a">
    <div data-role="navbar" data-iconpos="top" >
	    <ul>
		    <li><div><a href="index.action" style="color: rgb(11, 11, 11) !important;" data-ajax="false" data-icon="calendar" id="index_menu">个人平台</a></div></li>
			<li><a href="ajax!proInfo.action" style="color: rgb(11, 11, 11) !important;" data-ajax="false" data-icon="shop" id="pro_menu">当前项目</a></li>
			<li><a href="ajax!knowledge.action" style="color: rgb(11, 11, 11) !important;" data-ajax="false" data-icon="comment" id="knowledge_menu">知识共享</a></li>
		</ul>
	</div><!-- /navbar -->
</div><!-- /footer -->