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
<title>FlyVar</title>
<%@ include file="common/sheet_js.jsp"%>
</head>
<body>
	<div class="container">
		<%@ include file="common/navi.jsp"%>
		<div class="row-fluid mtd5p">
			<div class="span12 text-justify pd90">
				<img class="width100" src="static/images/flyvar.png" />
			</div>
		</div>
		<div class="row mtd5p">
			<div class="span12 text-justify pd80">Welcome to the fly
				variation database(FlyVar) website. FlyVar is a free integrative
				platform that addresses the increasing need of next generation
				sequencing data analysis in the Drosophila research community. It is
				composed of three parts. First, a database that contains 5.94
				million DNA polymorphisms found in Drosophila melanogaster derived
				from whole genome shotgun sequencing of 561 genomes of D.
				melanogaster. It shows an average of one variant per 20 bases across
				the genome. Second, a GUI interface has been implemented to allow
				easy and flexible query of the database. Third, a set of interactive
				online tools enables filtering and annotation of genomic sequence
				obtained from individual D. melanogaster strains to identify
				candidate mutations. FlyVar permits the analysis of next generation
				sequencing data without the need of extensive computational training
				or resources.</div>
		</div>
		<div class="row mtd10p">
			<div class="span12 pd80">
				<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center">
					<a href="query.htm"><img id="img_query" class="img-circle"
						src="static/images/query_index.jpg"
						alt="click to visit this function"
						style="width: 140px; height: 140px;"></a>
					<h3>variation query</h3>
					<p class="text-justify">Information of variants in the database
						could be flexibly queried based on fly strain ID, genomic
						coordinate, genomic interval, and gene ID.</p>
				</div>
				<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center">
					<a href="annotate.htm"><img id="img_annotate"
						class="img-circle" src="static/images/annotate_index.png"
						alt="click to visit this function"
						style="width: 140px; height: 140px;"></a>
					<h3>variation annotate</h3>
					<p class="text-justify">This tool is a user friendly interface
						to annotate variants at batch model. You will know thousands of
						variants' function all at once.</p>
				</div>
				<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center">
					<a href="filter.htm"><img id="img_filter" class="img-circle"
						src="static/images/filter_index.jpg"
						alt="click to visit this function"
						style="width: 140px; height: 140px;"></a>
					<h3>variation filter</h3>
					<p class="text-justify">This function will remove common
						variants. It will greatly reduce lab work to identify the
						potential phenotype relevant mutation.</p>
				</div>
			</div>
		</div>
		<%@ include file="common/footer.jsp"%>
	</div>
</body>
</html>