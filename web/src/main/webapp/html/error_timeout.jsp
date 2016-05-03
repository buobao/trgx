<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!--[if IE 9]>
	<style>
		.error-text {
			color: #333 !important;
		}
	</style>
<![endif]-->

<!-- row -->
<div class="row">

	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">

		<div class="row">
			<div class="col-sm-12">
				<div class="text-center error-box">
					<h1 class="error-text tada animated" style="font-size:500%;"><i class="fa fa-times-circle text-danger error-icon-shadow"></i> 会话超时</h1>
					<h2 class="font-xl"><strong>系统提示，您的会话已超时!</strong></h2>
					<br />
					<p class="lead semi-bold">
						<strong>如需继续浏览，请 <a href="login.action"> 重新登录   <i class="fa fa-arrow-right"></i></small></a> 。</strong><br><br>
						
					</p>
				</div>

			</div>

		</div>

	</div>

	<!-- end row -->

	<script type="text/javascript">
		// DO NOT REMOVE : GLOBAL FUNCTIONS!
		// pageSetUp();

		// PAGE RELATED SCRIPTS
		$("#search-error").focus();
	</script>