(function() {
	$(document).on('click', '#annotate', function() {
		if (!checkInputOrFileSize()) {
			showEmailInput();
			return;
		}
		waitingDialog.show();
		$("#annotateForm").attr("action", "annotate/annotate.htm");
		$("#annotateForm").submit();
	});
})();

function checkInputOrFileSize() {
	var annotateInput = $("#annotate_input").val();
	var annotateFile = $("#annotate_file");
	var annotateEmail = $("#annotate_email").val();
	var emailPattern = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (annotateInput != "") {
		return true;
	}
	if (annotateFile.val() != "" && annotateFile[0].files[0].size > 20 * 1024 * 1024) {
		if (emailPattern.test(annotateEmail)) {
			return true;
		}
	}
	return false;
}

function showEmailInput() {
	$("#div_input_email").removeClass("dn");
	$("#annotate_email").focus();
}

function annotateByFourColumn() {
	$("#annotate_input").attr("placeholder", "The format of input: chr pos ref var. You can input multiple variations with one variation in a single line.");
	$("#annotate_input").attr("title", "Example: chr2L 22025 T G");
}

function annotateByVcf() {
	$("#annotate_input").attr("placeholder", "Please input variations in vcf format. You can input multiple variations with one variation in a single line.");
}
