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
<title>Filter Result</title>
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
								<th class="text-justify text-nowrap">NO.</th>
								<th class="text-justify text-nowrap">chr</th>
								<th class="text-justify text-nowrap">pos</th>
								<th class="text-justify text-nowrap">ref</th>
								<th class="text-justify text-nowrap">var</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="variationItem" items="${filterResults}" varStatus="itemInfo">
								<tr>
									<td class="text-justify text-nowrap"><c:out value="${itemInfo.index+1}" /></td>
									<td class="text-justify text-nowrap"><c:out value="${variationItem.getChr()}" /></td>
									<td class="text-justify text-nowrap"><c:out value="${variationItem.getPos()}" /></td>
									<td class="text-justify break-line"><c:out value="${variationItem.getRef()}" /></td>
									<td class="text-justify break-line"><c:out value="${variationItem.getAlt()}" /></td>
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