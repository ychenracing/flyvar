(function() {
	getDgrpSamples();
	setVariationDb();
	checkQueryBySample();
	$(document).on('click', '#query', function() {
		if (!checkQuerySubmit()) {
			return;
		}
		waitingDialog.show();
		$("#queryForm").attr("action", "query/query.htm");
		disableVariationDb(false);
		$("#queryForm").submit();
	});
	$(document).on('click', '#query_and_annotate', function() {
		if (!checkInputOrFileSize()) {
			showEmailInput();
			return;
		}
		if (!checkQuerySubmit()) {
			return;
		}
		waitingDialog.show();
		$("#queryForm").attr("action", "query/annotate.htm");
		disableVariationDb(false);
		$("#queryForm").submit();
	});
})();

function checkInputOrFileSize() {
	var queryInput = $("#query_input").val();
	var queryFile = $("#query_file");
	var queryEmail = $("#query_email").val();
	var emailPattern = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (queryInput != "") {
		return true;
	}
	if (queryFile.val() != "" && queryFile[0].files[0].size > 20 * 1024 * 1024) {
		if (emailPattern.test(queryEmail)) {
			return true;
		}
	}
	if (queryFile.val() === "") {
		return true;
	}
	return false;
}

function checkQuerySubmit() {
	var queryType = $('input:radio[name="queryType"]:checked').val();
	switch (queryType) {
	case "1":
		return checkByVariationSubmit();
	case "2":
		return checkBySampleSubmit();
	case "3":
		return checkByRegionSubmit();
	case "4":
	case "5":
		return checkByGeneNameSubmit();
	}
	return false;
}

function showEmailInput() {
	$("#div_input_email").removeClass("dn");
	$("#query_email").focus();
}

function disableVariationDb(disable) {
	$('input:radio[name="variationDb"]').prop("disabled", disable);
}

function getDgrpSamples() {
	var header = $("meta[name='_csrf_header']").attr("content");
	var token = $("meta[name='_csrf']").attr("content");
	$.ajax({
		url : "query/getDgrpSampleList.json",
		method : 'POST',
		data : {},
		dataType : "json",
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		}
	}).done(
			function(data) {
				var sampleNameList = data.data;
				var selectSample = $("#select_sample");
				for (var i = 0; i < sampleNameList.length; i++) {
					var option = "<option value='" + sampleNameList[i] + "'>"
							+ sampleNameList[i].toUpperCase() + "</option>";
					selectSample.append(option);
				}
			}).fail(function(jqXHR, textStatus) {
		console.error("Request failed: " + textStatus);
	});
}

function setVariationDb() {
	var queryType = $('input:radio[name="queryType"]:checked').val();
	if (queryType == null) {
		alert("choose type for query please!");
		return false;
	}

	if (queryType === "2") {
		$("#db_dgrp").prop("checked", true);
		disableVariationDb(true);
	} else {
		disableVariationDb(false);
	}
}

function queryByVariation() {
	setVariationDb();
	$("#div_query_input").removeClass("dn");
	$("#div_select_sample").addClass("dn");
	$("#div_input_email").addClass("dn");
	$("#div_choose_a_file").removeClass("dn");
	$("#query_input")
			.attr(
					"placeholder",
					"The format of input: chr pos ref var. You can input multiple variations with one variation in a single line.");
	$("#query_input").attr("title", "Example: chr2L 22025 T G");
}

function checkByVariationSubmit() {
	var variationStr = $("#query_input").val();
	var queryFile = $("#query_file").val();
	var errorPrompt = "error format for query variation input!";
	if (variationStr === "") {
		if (queryFile === "") {
			setInputFormatError(errorPrompt);
			return false;
		}
	}
	if (queryFile === "") {
		var splittedVariations = variationStr.split(/\s+/);
		if (splittedVariations.length % 4 !== 0) {
			setInputFormatError(errorPrompt);
			return false;
		}
		var posReg = /^\d+$/;
		var nucleotideReg = /^[ATCGN]+$/;
		for (var i = 0; i < splittedVariations.length;) {
			var posStr = splittedVariations[i + 1];
			var refStr = splittedVariations[i + 2];
			var varStr = splittedVariations[i + 3];
			if (!posReg.test(posStr) || !nucleotideReg.test(refStr)
					|| !nucleotideReg.test(varStr)) {
				setInputFormatError(errorPrompt);
				return false;
			}
			i += 4;
		}
	}
	return true;
}

function setInputFormatError(promptStr) {
	$("#queryInputErrorPrompt").removeClass("dn");
	$("#queryInputErrorPrompt").text(promptStr);
	$("#query_input").focus();
}

function checkBySampleSubmit() {
	var selectSample = $("#select_sample").val();
	var queryEmail = $("#div_input_email");
	var emailPattern = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (selectSample === "") {
		$("#queryInputErrorPrompt").removeClass("dn");
		$("#queryInputErrorPrompt").text("Select a sample please!");
		return false;
	}
	if (queryEmail.val() === "" || !emailPattern.test(queryEmail).val()) {
		queryEmail.removeClass("dn");
		queryEmail.focus();
		$("#queryInputErrorPrompt").removeClass("dn");
		$("#queryInputErrorPrompt").text("Error format for email!");
		return false;
	}
	return true;
}

function queryBySample() {
	setVariationDb();
	checkQueryBySample();
}

function checkQueryBySample() {
	var queryType = $('input:radio[name="queryType"]:checked').val();
	if (queryType === "2") {
		$("#div_query_input").addClass("dn");
		$("#div_select_sample").removeClass("dn");
		$("#div_input_email").removeClass("dn");
		$("#div_choose_a_file").addClass("dn");
	}
}

function queryByRegion() {
	setVariationDb();
	$("#div_query_input").removeClass("dn");
	$("#div_select_sample").addClass("dn");
	$("#div_input_email").addClass("dn");
	$("#div_choose_a_file").removeClass("dn");
	$("#query_input").attr("placeholder", "The format of input: chr start end");
	$("#query_input").attr("title", "Example: chr2L 4000 8000");
}

function checkByRegionSubmit() {
	var regionStr = $("#query_input").val();
	var queryFile = $("#query_file").val();
	var errorPrompt = "error format for query region input!";
	if (regionStr === "") {
		if (queryFile === "") {
			setInputFormatError(errorPrompt);
			return false;
		}
	}
	if (queryFile === "") {
		var splittedRegions = regionStr.split(/\s+/);
		if (splittedRegions.length % 3 !== 0) {
			setInputFormatError(errorPrompt);
			return false;
		}
		var posReg = /^\d+$/;
		for (var i = 0; i < splittedRegions.length;) {
			var startPosStr = splittedRegions[i + 1];
			var endPosStr = splittedRegions[i + 2];
			if (!posReg.test(startPosStr) || !posReg.test(endPosStr)) {
				setInputFormatError(errorPrompt);
				return false;
			}
			i += 3;
		}
	}
	return true;
}

function checkByGeneNameSubmit() {
	var geneNameStr = $("#query_input").val();
	var queryFile = $("#query_file").val();
	var errorPrompt = "error format for query gene name input!";
	if (regionStr === "") {
		if (queryFile === "") {
			setInputFormatError(errorPrompt);
			return false;
		}
	}
	return true;
}

function queryByGeneName() {
	setVariationDb();
	$("#div_query_input").removeClass("dn");
	$("#div_select_sample").addClass("dn");
	$("#div_input_email").addClass("dn");
	$("#div_choose_a_file").removeClass("dn");
	$("#query_input").attr("placeholder",
			"INPUT flybase id OR annotation symbol OR symbol");
	$("#query_input").attr("title", "Example: FBgn0003278");
}
