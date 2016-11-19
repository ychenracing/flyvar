(function() {
	$(document).on('click', '#filter', function() {
		waitingDialog.show();
		$("#filterForm").attr("action", "filter/filter.htm");
		$("#filterForm").submit();
	});
	$(document).on('click', '#filter_and_annotate', function() {
		waitingDialog.show();
		$("#filterForm").attr("action", "filter/annotate.htm");
		$("#filterForm").submit();
	});
})();

function filterByFourColumn() {
	$("#filter_input").attr("placeholder", "The format of input: chr pos ref var. You can input multiple variations with one variation in a single line.");
	$("#filter_input").attr("title", "Example: chr2L 22025 T G");
}

function filterByVcf() {
	$("#filter_input").attr("placeholder", "Please input variations in vcf format. You can input multiple variations with one variation in a single line.");
}

