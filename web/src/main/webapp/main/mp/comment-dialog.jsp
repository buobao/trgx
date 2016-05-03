<%--
  Created by IntelliJ IDEA.
  User: dqf
  Date: 2015/3/12
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<div data-role="panel" data-position-fixed="true" data-display="overlay" data-theme="a" id="comment-dialog">
    <s:iterator value="listComm" id="list">
        <li>
                <%--<a data-ajax="false"  href="ajax-comment!read.action?keyId=<s:property value="#list.id"/>">--%>
            <span class="h2"><s:property value="#list.text" /></span>
            <p>
                <span>[<s:property value="#list.clientName" />]</span>
            </p>
            </a>
        </li>
    </s:iterator>
    <div class="textarea-div">
        <div class="typearea">
            <textarea placeholder="请填写审批意见..." name="comment" id="chat_textarea-expand" class="custom-scroll"></textarea>
        </div>
    </div>
    <span class="textarea-controls">
        <button id="left_foot_btn_close" data="" class="btn btn-sm btn-primary pull-right">关闭</button>
        <button id="left_foot_btn_comment" data="" class="btn btn-sm btn-primary pull-right">发送</button>
    </span>
</div>
