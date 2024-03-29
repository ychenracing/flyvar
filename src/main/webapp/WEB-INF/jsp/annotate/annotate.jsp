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
<title>Annotate</title>
<%@ include file="../common/sheet_js.jsp"%>
</head>
<body>
	<div class="container">
		<%@ include file="../common/navi.jsp"%>
		<div class="row mtd5p">
			<div class="span12 text-justify pd80">The annotation module
				provides a user-friendly interface to annotate variants input by the
				users. Currently, flybase version 5.12 and ANNOVAR are used to
				perform functional annotation. Input with standard vcf format or
				user defined tab-separated plain text are both accepted. For the
				custom plain text file, only essential information to define a
				variant, including chromosome, coordinate, reference base, and
				variant base are required. This module will generate two output
				files: the first file contains annotation for all the variants while
				the second file contains annotation for only variants that affect
				protein coding.</div>
		</div>

		<div class="row mtd5p">
			<div class="span12 text-justify pd80">
				<strong>Note: Release 5 coordinates are used in this
					database.</strong>
			</div>
		</div>

		<div class="row mtd10p pd80">
			<form:form id="annotateForm" role="form" commandName="annotateForm"
				method="post" action="${basePath}annotate/annotate.htm"
				enctype="multipart/form-data">
				<div class="row">
					<div
						class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center pull-left vertical-center">
						<span class="inline-block vc65">choose type</span>
					</div>
					<div class="col-xs-12 col-sm-8 col-md-8 col-lg-8 pull-right">
						<label class="radio"> <form:radiobutton id="four_column"
								path="inputFormatType" value="1" checked="checked"
								onclick="annotateByFourColumn()" /> 4 column tab separated
							format
						</label> <label class="radio"> <form:radiobutton id="vcf_format"
								path="inputFormatType" value="2" onclick="annotateByVcf()" />vcf
							format
						</label>
						<form:errors path="inputFormatType" cssClass="error" />
					</div>
				</div>

				<div class="row mtd5p">
					<div
						class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center pull-left">
						<span class="inline-block vc168">input or choose a file</span>
					</div>
					<div class="col-xs-12 col-sm-8 col-md-8 col-lg-8 pull-right">
						<div id="div_annotate_input" class="row">
							<form:textarea path="annotateInput" rows="5" id="annotate_input"
								name="annotate_input" class="form-control" type="text"
								scrolling="auto" title="Example: chr2L 22025 T G"
								placeholder="The format of input: chr pos ref var. You can input multiple variations with one variation in a single line." />
						</div>
						<div id="div_choose_a_file" class="row">
							<div class="">Or Choose from a file:</div>
							<div class="">
								<input id="annotate_file" name="annotateFile" type="file"
									class="file" data-show-preview="false">
							</div>
						</div>
						<div id="div_input_email" class="row dn">
                            <div class=" text-justify">Input e-mail address for
                                getting your results:</div>
                            <div class="">
                                <form:input path="annotateEmail" id="annotate_email"
                                    name="annotate_email" class="form-control"
                                    placeholder="input email address" />
                                <form:errors path="annotateEmail" cssClass="error" />
                            </div>
                        </div>
                        <span id="annotateInputErrorPrompt" class="error dn"></span>
						<form:errors path="annotateInput" cssClass="error" />
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
							class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center pull-left">
							<button id="annotate" type="button" name="annotate"
								value="annotate" class="btn btn-default btn-lg btn-block">annotate</button>
						</div>
					</div>
				</div>
			</form:form>

		</div>
		<%@ include file="../common/footer.jsp"%>
		<script src="static/js/annotate.js"></script>
	</div>
</body>
</html>