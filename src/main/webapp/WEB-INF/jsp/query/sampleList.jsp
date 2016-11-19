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
<title>Sample List</title>
<%@ include file="../common/sheet_js.jsp"%>
</head>
<body>
	<div class="container">
		<%@ include file="../common/navi.jsp"%>
		<div class="row">
			<div class="span12 fc">
				<div class="table-responsive pd80">
					<table class="table table-hover">
						<thead>
							<tr>
								Sample list:
								<c:if test="${sampleListResult != null}">${sampleListResult.size()} records</c:if>
							</tr>
						</thead>
						<tbody>
							<c:if test="${sampleListResult != null}">
								<c:forEach var="sampleLink" items="${sampleListResult}"
									varStatus="status">
									<tr>
										<td class="text-justify text-nowrap text-center">${status.count}</td>
										<td class="text-justify text-nowrap text-center">${sampleLink.sampleName}</td>
										<td class="text-justify text-nowrap"><c:if
												test="${sampleLink.linkNum == null}">
										no link to flybase
										</c:if> <c:if test="${sampleLink.linkNum != null}">
												<a
													href="http://flybase.org/reports/FBst00${sampleLink.linkNum}.html">${sampleLink.sampleName}</a>
											</c:if></td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<%@ include file="../common/footer.jsp"%>
	</div>
</body>
</html>