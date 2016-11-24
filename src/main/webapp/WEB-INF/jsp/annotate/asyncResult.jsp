<%@ include file="../common/taglib.jsp"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                      + request.getServerPort() + path + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href=" <%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Annotate Result</title>
<%@ include file="../common/sheet_js.jsp"%>
</head>
<body>
	<div class="container">
		<%@ include file="../common/navi.jsp"%>
		<div class="row mtd5p">
			<div class="span12 text-justify pd90">
				<img class="width100" src="static/images/flyvar.png" />
			</div>
		</div>

		<div class="row mtd10">
			<div class="span12 fc">
				<div class="table-responsive pd80">
					<span class="text-justify">Your annotate results are
						processing! We'll send you an email to download your results in
						few minutes.</span>
				</div>
			</div>
		</div>
		<%@ include file="../common/footer.jsp"%>
	</div>
</body>
</html>