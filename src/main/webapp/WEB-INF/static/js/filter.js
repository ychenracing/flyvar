(function() {
	$(document).on('click', '#filter', function() {
		if (!checkInputOrFileSize()) {
			showEmailInput();
			return;
		}
		waitingDialog.show();
		$("#filterForm").attr("action", "filter/filter.htm");
		$("#filterForm").submit();
	});
	$(document).on('click', '#filter_and_annotate', function() {
		if (!checkInputOrFileSize()) {
			showEmailInput();
			return;
		}
		waitingDialog.show();
		$("#filterForm").attr("action", "filter/annotate.htm");
		$("#filterForm").submit();
	});
})();

function checkInputOrFileSize() {
	var filterInput = $("#filter_input").val();
	var filterFile = $("#filter_file");
	var filterEmail = $("#filter_email").val();
	var emailPattern = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (filterInput != "") {
		return true;
	}
	if (filterFile.val() != "" && filterFile[0].files[0].size > 20 * 1024 * 1024) {
		if (emailPattern.test(filterEmail)) {
			return true;
		}
	}
	if (filterFile.val() === "") {
		return true;
	}
	return false;
}

function showEmailInput() {
	$("#div_input_email").removeClass("dn");
	$("#filter_email").focus();
}

function filterByFourColumn() {
	$("#filter_input").attr("placeholder", "The format of input: chr pos ref var. You can input multiple variations with one variation in a single line.");
	$("#filter_input").attr("title", "Example: chr2L 22025 T G");
}

function filterByVcf() {
	$("#filter_input").attr("placeholder", "Please input variations in vcf format. You can input multiple variations with one variation in a single line.");
}

