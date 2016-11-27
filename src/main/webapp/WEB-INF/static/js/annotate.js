(function() {
	$(document).on('click', '#annotate', function() {
		if (!checkInputOrFileSize()) {
			showEmailInput();
			return;
		}
		if (!checkAnnotateSubmit()) {
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
	if (annotateFile.val() != ""
			&& annotateFile[0].files[0].size > 20 * 1024 * 1024) {
		if (emailPattern.test(annotateEmail)) {
			return true;
		}
	}
	if (annotateFile.val() === "") {
		return true;
	}
	return false;
}

function showEmailInput() {
	$("#div_input_email").removeClass("dn");
	$("#annotate_email").focus();
}

function checkAnnotateSubmit() {
	var variationStr = $("#annotate_input").val();
	var annotateFile = $("#annotate_file").val();
	var errorPrompt = "error format for annotate variation input!";
	if (variationStr === "") {
		if (annotateFile === "") {
			setInputFormatError(errorPrompt);
			return false;
		}
	}
	if (queryFile === "") {
		var annotateType = $('input:radio[name="inputFormatType"]:checked')
				.val();
		switch (annotateType) {
		case "1":
			try {
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
			} catch (ex) {
				setInputFormatError(errorPrompt);
				return false;
			}
			break;
		case "2":
			try {
				var splittedVariationLines = variationStr.split(/\n+/);
				var posReg = /^\d+$/;
				var nucleotideReg = /^[ATCGN]+$/;
				for (var j = 0; j < splittedVariationLines.length; j++) {
					var variationLine = splittedVariationLines[j];
					if (variationLine !== "") {
						var splittedVariations = variationLine.split(/\s+/);
						var posStr = splittedVariations[1];
						var refStr = splittedVariations[3];
						var varStr = splittedVariations[4];
						if (splittedVariations.length < 6
								|| !posReg.test(posStr)
								|| !nucleotideReg.test(refStr)
								|| !nucleotideReg.test(varStr)) {
							setInputFormatError(errorPrompt);
							return false;
						}
					}
				}
			} catch (ex) {
				setInputFormatError(errorPrompt);
				return false;
			}
			break;
		}
	}
	return true;
}

function setInputFormatError(promptStr) {
	$("#annotateInputErrorPrompt").removeClass("dn");
	$("#annotateInputErrorPrompt").text(promptStr);
	$("#annotate_input").focus();
}

function annotateByFourColumn() {
	$("#annotate_input")
			.attr(
					"placeholder",
					"The format of input: chr pos ref var. You can input multiple variations with one variation in a single line.");
	$("#annotate_input").attr("title", "Example: chr2L 22025 T G");
}

function annotateByVcf() {
	$("#annotate_input")
			.attr(
					"placeholder",
					"Please input variations in vcf format. You can input multiple variations with one variation in a single line.");
}
