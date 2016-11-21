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
<title>Source For Downloading</title>
<%@ include file="../common/sheet_js.jsp"%>
</head>
<body>
	<div class="container">
		<%@ include file="../common/navi.jsp"%>
		<div class="row-fluid mtd5p">
			<div class="span12 text-justify pd80">
				<img class="width100" src="static/images/flyvar.png" />
			</div>
		</div>
		<div class="row-fluid mtd10p">
			<div class="span12 pd90">
				<div class="table-responsive pd80">
					<table class="table table-hover">
						<thead>
							<th class="text-center"></th>
							<th class="text-justify text-nowrap">Sources</th>
						</thead>
						<tbody>
							<tr>
								<td class="text-right">DGRP project:</td>
								<td class="text-justify text-nowrap"><a
									href="source/dgrp_project.tsv">dgrp_project</a></td>
							</tr>
							<tr>
								<td class="text-right">EMS screening project:</td>
								<td class="text-justify text-nowrap">
									<div class="row-fluid">
										<a
											href="source/summary_of_indels_with_frequency_information.zip">summary
											of indels with frequency information</a>
									</div>
									<div class="row-fluid">
										<a
											href="source/summary_of_snps_frequency_information.zip">summary
											of snps frequency information</a>
									</div>
									<div class="row-fluid">
										<a href="source/a_big_vcf_of_all_indels.zip">a
											big vcf of all indels</a>
									</div>
									<div class="row-fluid">
										<a href="source/a_big_vcf_of_all_snps.zip">a
											big vcf of all snps</a>
									</div>
									<div class="row-fluid">
										<a href="source/indels_in_IFTiso.zip">
											indels in IFTiso</a>
									</div>
									<div class="row-fluid">
										<a href="source/snps_in_IFTiso.zip"> snps in
											IFTiso</a>
									</div>
								</td>
							</tr>
							<tr>
								<td class="text-right">Other public project:</td>
								<td class="text-justify text-nowrap"><a
									href="source/other_public.tsv">other_public</a></td>
							</tr>
							<tr>
								<td class="text-right">EMS screening project:</td>
								<td class="text-justify text-nowrap"><a
									href="source/summary_of_indels_with_frequency_information.zip">summary
										of indels with frequency information</a></td>
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