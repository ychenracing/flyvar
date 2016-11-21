<%@ include file="../common/taglib.jsp"%>
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
<title>Dispensable Gene</title>
<%@ include file="../common/sheet_js.jsp"%>
</head>
<body>
	<div class="container">
		<%@ include file="../common/navi.jsp"%>
		<div class="row mtd5p">
			<div class="span12 text-justify pd80">
				<img class="width100" src="static/images/flyvar.png" />
			</div>
		</div>
		<div class="row mtd5p pd90">
			<div class="span12 text-justify pd80">We indentify dispensable
				genes by choosing genes with homozygous variants that leads to
				nonsense/or frameshift mutation in the non last one constitute
				exons.</div>
		</div>
		<div class="row mtd10p">
			<div class="span12 pd90">
				<div class="table-responsive pd80">
					<table class="table table-hover">
						<tbody>
							<tr>
								<td class="text-right">gene list:</td>
								<td class="text-justify text-nowrap"><a
									href="dispensable/dispensible_nonlastANDsharedExon_stopgain_or_frameshift.frameshift">click
										to download</a></td>
							</tr>
							<tr>
								<td class="text-right">gene list with annotation:</td>
								<td class="text-justify text-nowrap"><a
									href="dispensable/LOF_variants_dispensible_genes.txt">click
										to download</a></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<%@ include file="../common/footer.jsp"%>
	</div>
</body>
</html>