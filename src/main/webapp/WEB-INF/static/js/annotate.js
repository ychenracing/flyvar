(function() {
	$(document).on('click', '#annotate', function() {
		waitingDialog.show();
		$("#annotateForm").attr("action", "annotate/annotate.htm");
		$("#annotateForm").submit();
	});
})();

function annotateByFourColumn() {
	$("#annotate_input").attr("placeholder", "The format of input: chr pos ref var. You can input multiple variations with one variation in a single line.");
	$("#annotate_input").attr("title", "Example: chr2L 22025 T G");
}

function annotateByVcf() {
	$("#annotate_input").attr("placeholder", "Please input variations in vcf format. You can input multiple variations with one variation in a single line.");
}