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
<title>Query Result</title>
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
								<th class="ft text-nowrap">NO.</th>
								<th class="ft text-nowrap">chr</th>
								<th class="ft text-nowrap">pos</th>
								<th class="ft text-nowrap">ref</th>
								<th class="ft text-nowrap">var</th>
								<c:if
									test="${queryType != null && (queryType == 1 || queryType == 2)}">
									<th class="text-nowrap">number of flies</th>
								</c:if>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="variationItem" items="${queryResults}" varStatus="itemInfo">
								<tr>
									<td class="ft text-nowrap"><c:out value="${itemInfo.index+1}" /></td>
									<td class="ft text-nowrap"><c:out value="${variationItem.getChr()}" /></td>
									<td class="ft text-nowrap"><c:out value="${variationItem.getPos()}" /></td>
									<td class="ft break-line"><c:out value="${variationItem.getRef()}" /></td>
									<td class="ft break-line"><c:out value="${variationItem.getAlt()}" /></td>
									<c:if
										test="${queryType != null && (queryType == 1 || queryType == 2)}">
										<td class="ft text-nowrap"><c:out value="${variationItem.getCount()}" /></td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<%@ include file="../common/footer.jsp"%>
	</div>
</body>
</html>