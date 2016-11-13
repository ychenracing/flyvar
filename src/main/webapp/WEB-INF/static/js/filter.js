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

