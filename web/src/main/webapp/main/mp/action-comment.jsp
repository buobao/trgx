<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<jsp:include page="inc.jsp"	/>
</head>
<body>
<div data-role="page" id="page-comment" data-theme="a" targetId="<s:property value="announce.id" />" targetObject="com.joint.core.entity.Announce" data="{}" createrId="<s:property value="announce.creater.id" />">
	<div data-role="header"  >
		<a href="#" key="page-main-action" data-ajax="false" id="" class="ui-btn ui-btn-left ui-icon-back ui-corner-all ui-btn-icon-left ui-btn-icon-notext">视图</a>
		<h1>批注信息</h1>
	</div><!-- /header -->
	<div role="main" class="ui-content" id="comment-listview-content"
		 mp-url="ajax-comment!listDocument.action?keyId=<s:property value="announce.id" />">
		<ul data-role="listview" data-theme="a" data-dividertheme="b"
			mp-page="1" mp-keyword="" data-split-icon="forward" data-split-theme="a" data-inset="false">
			<li><a data-ajax="false"  href="javascript:void(0)">
				没有批注信息.
			</a>
			</li>
		</ul>

		<div id="ToolBar">
			<table style="width:100%">
				<tr>
					<td>
						<a href="#" data-role="button" id="btn_choose" data-icon="plus" style="display: block;height: 12px;margin-top: 7px;margin-left: -2px;margin-right:-4px;padding-left:1.6em;border-radius:0px;"></a>
					</td>
					<td>
						<input type="text" placeholder="请选择@对象" id="chart_user"  name="chart_user" />
					</td>
					<td>
						<a href="#" data-role="button" id="chart_btn" data-icon="check" style="display: block;margin-top: 7px;height: 12px;margin-left: -4px;margin-right: -4px;padding-left:1.6em;border-radius:0px;"></a>
					</td>
				</tr>
			</table>
		</div>

	</div><!-- /content -->
	<div style="height: 40px;">

	</div>
</div>
<div data-role="page" id="page-user" data-theme="a">
	<div data-role="header"  >
		<a href="#page-comment" key="page-comment-action" data-ajax="false"  class="ui-btn ui-btn-left ui-icon-back ui-corner-all ui-btn-icon-left ui-btn-icon-notext">视图</a>
		<h1>请选择要回复的人</h1>
		<a id="comuser_btn" mp-panel-id="proinfo-listview-panel" href="#" class="ui-btn ui-shadow ui-mini ui-btn-right">确认</a>
	</div>
	<div role="main" class="ui-content" id="comment-user-content"
		 mp-url="ajax-comment!usersPage.action">
		<div id="scroller">
			<ul data-role="listview" data-theme="a" data-dividertheme="b"
				mp-page="1" mp-keyword="" data-split-icon="forward" data-split-theme="a" data-inset="false">
				<li>
				</li>
			</ul>
		</div>

	</div>
</div>
</body>

