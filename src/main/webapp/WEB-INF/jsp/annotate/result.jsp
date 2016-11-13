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
		<div class="row">
			<div class="span12 fc">
				<div class="table-responsive pd80">
					<table class="table table-hover">
						<thead>
							<tr>Annotate Result:
							</tr>
						</thead>
						<tbody>
							<c:if test="${annovarInput != null}">
								<tr>
									<td class="text-justify text-nowrap">annovar input:</td>
									<td class="text-justify text-nowrap"><a
										href="annotate/result/${annovarInput}">annovar input</a></td>
								</tr>
							</c:if>
							<c:if test="${annotateResult != null}">
								<tr>
									<td class="text-justify text-nowrap">variant function:</td>
									<td class="text-justify text-nowrap"><a
										href="annotate/result/${annotateResult}">variant function</a></td>
								</tr>
							</c:if>
							<c:if test="${exonicAnnotate != null}">
								<tr>
									<td class="text-justify text-nowrap">exonic variant
										function:</td>
									<td class="text-justify text-nowrap"><a
										href="annotate/result/${exonicAnnotate}">exonic variant
											function</a></td>
								</tr>
							</c:if>
							<c:if test="${combineAnnovarOut != null}">
								<tr>
									<td class="text-justify text-nowrap">combined annovar
										output:</td>
									<td class="text-justify text-nowrap"><a
										href="annotate/result/${combineAnnovarOut}">combined
											annovar output</a></td>
								</tr>
							</c:if>
							<c:if test="${annovarInvalidInput != null}">
								<tr>
									<td class="text-justify text-nowrap">invalid input for
										annovar:</td>
									<td class="text-justify text-nowrap"><a
										href="annotate/result/${annovarInvalidInput}">invalid
											input for annovar</a></td>
								</tr>
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