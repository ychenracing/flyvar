<%@ include file="common/taglib.jsp"%>
<%
    String path = request.getContextPath();
    String _basePath = request.getScheme() + "://" + request.getServerName() + ":"
                       + request.getServerPort() + path + "/";
%>
<c:set value="<%=_basePath%>" var="basePath" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="${basePath}">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Contact us</title>
<%@ include file="common/sheet_js.jsp"%>
</head>
<body>
	<div class="container">
		<%@ include file="common/navi.jsp"%>
		<div class="row mtd5p">
			<div class="span12 text-justify pd80">
				<img class="width100" src="static/images/flyvar.png" />
			</div>
		</div>
		<div class="row mtd5p pd90">
			<div class="span12 text-justify pd80">
				If you have any question, or you want to get more details about
				functions on this website, please send an email to: <a
					href="mailto:wangfei@fudan.edu.cn">wangfei@fudan.edu.cn</a>, <a
					href="mailto:yongchen13@fudan.edu.cn">yongchen13@fudan.edu.cn</a>
			</div>
		</div>
		<div class="row mtd5p pd90">
			<div class="span12 text-justify pd80">
				we'll appreciate your suggestions on construction of this website,
				please send an email to: <a href="mailto:yongchen13@fudan.edu.cn">yongchen13@fudan.edu.cn</a>
			</div>
		</div>
		<%@ include file="common/footer.jsp"%>
	</div>
</body>
</html>