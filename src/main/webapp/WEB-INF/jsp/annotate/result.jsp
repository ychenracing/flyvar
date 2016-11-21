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

		<div class="row">
			<div class="span12 fc">
				<div class="table-responsive pd80">
					<table class="table table-hover">
						<thead>
							<th></th>
							<th class="text-justify text-nowrap">Annotate Result:</th>
						</thead>
						<tbody>
							<c:if test="${annovarInput != null}">
								<tr>
									<td class="text-right">annovar input:</td>
									<td class="text-justify text-nowrap"><a
										href="annotate/result/${annovarInput}">annovar input</a></td>
								</tr>
							</c:if>

							<c:if test="${annotateResult != null}">
								<tr>
									<td class="text-right">variant function:</td>
									<c:if test="${combinedExonicResult != null}">
										<td class="text-justify text-nowrap"><a
											href="annotate/result/${combinedExonicResult}">variant
												function</a></td>
									</c:if>
									<c:if test="${combinedExonicResult == null}">
										<td class="text-justify text-nowrap"><a
											href="annotate/result/${annotateResult}">variant function</a></td>
									</c:if>
								</tr>
							</c:if>

							<c:if test="${exonicAnnotateResult != null}">

								<tr>
									<td class="text-right">exonic variant function:</td>
									<c:if test="${combinedExonicResult == null}">
										<td class="text-justify text-nowrap"><a
											href="annotate/result/${exonicAnnotateResult}">exonic
												variant function</a></td>
									</c:if>
								</tr>
							</c:if>

							<c:if test="${combineAnnovarOut != null}">
								<tr>
									<td class="text-right">combined annovar output:</td>
									<td class="text-justify text-nowrap"><a
										href="annotate/result/${combineAnnovarOut}">combined
											annovar output</a></td>
								</tr>
							</c:if>
							<c:if test="${annovarInvalidInput != null}">
								<tr>
									<td class="text-right">invalid input for annovar:</td>
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