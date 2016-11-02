(function() {
	getDgrpSamples();
	setVariationDb();
	checkQueryBySample();
	$(document).on('click', '#query', function() {
		waitingDialog.show();
		$("#queryForm").attr("action", "query/query.htm");
		disableVariationDb(false);
		$("#queryForm").submit();
	});
	$(document).on('click', '#query_and_annotate', function() {
		waitingDialog.show();
		$("#queryForm").attr("action", "query/queryAnnotate.htm");
		disableVariationDb(false);
		$("#queryForm").submit();
	});
})();

function disableVariationDb(disable) {
	$('input:radio[name="variationDb"]').prop("disabled", disable);
}

function getDgrpSamples() {
	$.ajax({
		url : "query/getDgrpSampleList.json",
		method : 'POST',
		data : {},
		dataType : "json"
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
