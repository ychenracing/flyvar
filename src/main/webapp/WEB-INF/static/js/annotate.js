(function() {
	$(document).on('click', '#annotate', function() {
		waitingDialog.show();
		$("#annotateForm").attr("action", "annotate/annotate.htm");
		$("#annotateForm").submit();
	});
})();
