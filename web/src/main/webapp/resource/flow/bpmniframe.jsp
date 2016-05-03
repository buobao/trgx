<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String keyId = request.getParameter("keyId");
String link = request.getParameter("link");	//对象是instance还是definition
%>
<style type="text/css">
ul.legendContainer{
	position: relative;
	top:10px;
	left:10px;
}
	
ul.legendContainer li{
	list-style: none;
	font-size: 14px;
	display: inline-block;
	font-weight: bold;
}
	
ul.legendContainer li .legend{
	width:14px;
	height: 14px;
	border: 1px solid black;
	margin-right:5px;
	margin-top:2px;
	float: left;
}
</style>
<div class="panel-body" style="background-color:#fff;margin:0;overflow:hidden;border:0">
		<ul class="legendContainer">
			<li><div style="background-color:gray; " class="legend"></div>未执行</li>
			<li><div style="background-color:#00FF00;" class="legend"></div>同意</li>
			<li><div style="background-color:orange;" class="legend"></div>弃权</li>
			<li><div style="background-color:red;" class="legend"></div>当前节点</li>
			<li><div style="background-color:blue;" class="legend"></div>反对</li>
			<li><div style="background-color:#8A0902;" class="legend"></div>驳回</li>
			<li><div style="background-color:#023B62;" class="legend"></div>追回</li>
			<li><div style="background-color:#338848;" class="legend"></div>会签通过</li>
			<li><div style="background-color:#82B7D7;" class="legend"></div>会签不通过</li>
		</ul>
		<div style="padding-top:20px;padding-left:45px;background-color: white;">
			<div><b>说明：</b>点击任务节点可以查看节点的执行人员</div>
		</div>
</div>
<iframe scrolling="no" width="100%" frameborder="0" for="tab" src="<%=path %>/manage/ajax-running!export.action?keyId=<%=keyId %>&link=<%=link%>" allowtransparency="true" style="height: 500px;background-color:#DBEEFD;"></iframe>
			