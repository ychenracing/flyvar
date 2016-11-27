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
<title>Filter</title>
<%@ include file="../common/sheet_js.jsp"%>
</head>
<body>
	<div class="container">
		<%@ include file="../common/navi.jsp"%>
		<div class="row mtd5p">
			<div class="span12 text-justify pd80">The filtering module
				removes polymorphisms stored in the database from user's input
				variation files, resulting in a narrower list of candidate
				mutations. Thus it will greatly reduce lab work to identify the
				potential phenotype relevant mutation.</div>
		</div>

		<div class="row mtd5p">
			<div class="span12 text-justify pd80">
				<strong>Note: Release 5 coordinates are used in this
					database.</strong>
			</div>
		</div>

		<div class="row mtd5p">
			<div class="span12 text-justify pd80">
				<strong>To ensure that our website work consistently, we
					ask you to limit input file size to 200,000 lines. If your files
					are bigger than that, we suggest you divide your files by
					chromosome. You can also send an email to us and we are glad to
					help.</strong>
			</div>
		</div>

		<div class="row mtd10p pd80">
			<form:form id="filterForm" role="form" commandName="filterForm"
				method="post" action="${basePath}filter/filter.htm"
				enctype="multipart/form-data">
				<div class="row">
					<div
						class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center pull-left vertical-center">
						<span class="inline-block vc115">choose database</span>
					</div>
					<div class="col-xs-12 col-sm-8 col-md-8 col-lg-8 pull-right">
						<label class="radio"> <form:radiobutton id="db_dgrp"
								path="variationDb" value="1" checked="checked" /> DGRP
						</label> <label class="radio"> <form:radiobutton id="db_hugo"
								path="variationDb" value="2" /> EMS screening
						</label> <label class="radio"> <form:radiobutton id="db_other"
								path="variationDb" value="3" /> Other public databases
						</label> <label class="radio"> <form:radiobutton
								id="db_dgrp_hugo_other" path="variationDb" value="4" /> DGRP +
							EMS screening + Other public databases
						</label>
						<form:errors path="variationDb" cssClass="error" />
					</div>
				</div>

				<div class="row">
					<div
						class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center pull-left vertical-center">
						<span class="inline-block vc65">choose type</span>
					</div>
					<div class="col-xs-12 col-sm-8 col-md-8 col-lg-8 pull-right">
						<label class="radio"> <form:radiobutton id="four_column"
								path="inputFormatType" value="1" checked="checked"
								onclick="filterByFourColumn()" /> 4 column tab separated format
						</label> <label class="radio"> <form:radiobutton id="vcf_format"
								path="inputFormatType" value="2" onclick="filterByVcf()" /> vcf
							format
						</label>
						<form:errors path="inputFormatType" cssClass="error" />
					</div>
				</div>

				<div class="row">
					<div
						class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center pull-left vertical-center">
						<span class="inline-block vc85">alternative choice</span>
					</div>
					<div
						class="col-xs-12 col-sm-8 col-md-8 col-lg-8 pull-right form-group">
						<div class="row">
							<span class="text-justify"><strong>Would you want
									to remove variations within dispensable gene regions?</strong></span>
						</div>
						<div class="">
							<label class="radio"> <form:radiobutton
									id="dispensable_yes" path="removeDispensable" value="1" /> Yes
							</label> <label class="radio"> <form:radiobutton
									id="dispensable_no" path="removeDispensable" value="2"
									checked="checked" /> No
							</label>
						</div>

						<form:errors path="removeDispensable" cssClass="error" />
					</div>
				</div>

				<div class="row mtd5p">
					<div
						class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center pull-left">
						<span class="inline-block vc168">input or choose a file</span>
					</div>
					<div class="col-xs-12 col-sm-8 col-md-8 col-lg-8 pull-right">
						<div id="div_query_input" class="row">
							<form:textarea path="filterInput" rows="5" id="filter_input"
								name="filter_input" class="form-control" type="text"
								scrolling="auto" title="Example: chr2L 22025 T G"
								placeholder="The format of input: chr pos ref var. You can input multiple variations with one variation in a single line." />
						</div>
						<div id="div_choose_a_file" class="row">
							<div class="">Or Choose from a file:</div>
							<div class="">
								<input id="filter_file" name="filterFile" type="file"
									class="file" data-show-preview="false">
							</div>
						</div>
						<div id="div_input_email" class="row dn">
                            <div class=" text-justify">Input e-mail address for
                                getting your results:</div>
                            <div class="">
                                <form:input path="filterEmail" id="filter_email"
                                    name="filter_email" class="form-control"
                                    placeholder="input email address" />
                                <form:errors path="filterEmail" cssClass="error" />
                            </div>
                        </div>
                        <span id="filterInputErrorPrompt" class="error dn"></span>
						<form:errors path="filterInput" cssClass="error" />
					</div>
				</div>


				<div class="row">
					<div
						class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center pull-left">
						<span class="inline-block vc45">submit</span>
					</div>
					<div
						class="col-xs-12 col-sm-8 col-md-8 col-lg-8 text-center pull-right">
						<div
							class="col-xs-12 col-sm-6 col-md-6 col-lg-6 text-center pull-left">
							<button id="filter" type="button" name="filter" value="filter"
								class="btn btn-default btn-lg btn-block">filter</button>
						</div>
						<div
							class="col-xs-12 col-sm-6 col-md-6 col-lg-6 text-center pull-left">
							<button id="filter_and_annotate" type="button"
								name="filter_and_annotate"
								class="btn btn-default btn-lg btn-block"
								value="filter_and_annotate">filter and annotate</button>
						</div>
					</div>
				</div>
			</form:form>

		</div>
		<%@ include file="../common/footer.jsp"%>
		<script src="static/js/filter.js"></script>
	</div>
</body>
</html>