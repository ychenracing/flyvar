(function() {
	judgeBrowser();
	activeNavigation();
	getVisitTime();
})();

function getVisitTime() {
	$.ajax({
		url : "visit.json",
		method : 'POST',
	}).done(function(data) {
		$("#visitTime").html(data.visit);
	});
}

function activeNavigation() {
	var curUrl = window.location.href;
	var indexReg = /flyvar\/*$/g;
	if (indexReg.test(curUrl) || curUrl.endsWith("index.htm")) {
		$("#navi_home").attr("class", "active");
	} else if (curUrl.endsWith("flyvar/query.htm") || curUrl.endsWith("flyvar/query/result.htm")) {
		$("#navi_query").attr("class", "active");
	} else if (curUrl.endsWith("flyvar/annotate.htm") || curUrl.endsWith("flyvar/annotate/result.htm")) {
		$("#navi_annotate").attr("class", "active");
	} else if (curUrl.endsWith("flyvar/filter.htm") || curUrl.endsWith("flyvar/annotate/result.htm")) {
		$("#navi_filter").attr("class", "active");
	} else if (curUrl.endsWith("flyvar/dispensable.htm")) {
		$("#navi_dispensable").attr("class", "active");
	} else if (curUrl.endsWith("flyvar/source.htm")) {
		$("#navi_source").attr("class", "active");
	}
}

function judgeBrowser() {
	var userAgent = navigator.userAgent.toLowerCase(), s, o = {};
	var browser = {
		version : (userAgent
				.match(/(?:firefox|opera|safari|chrome|msie)[\/: ]([\d.]+)/))[1],
		safari : /version.+safari/.test(userAgent),
		chrome : /chrome/.test(userAgent),
		firefox : /firefox/.test(userAgent),
		ie : /msie/.test(userAgent),
		opera : /opera/.test(userAgent)
	};
	if (browser.ie && browser.version < 10) {
		alert("your browser is IE "
				+ browser.version
				+ ". For better experience, we suggest that do use Chrome or FireFox or Safari. If you insist in IE, please use ones whose version is above 10.");
	}
}

// source for downloading page
function OtherPublicProjectDownload() {
	var downloadfilename = document
			.getElementById("OtherPublicProjectDownload").value;
	if (downloadfilename == "selectplease") {
		document.getElementById("OtherPublicquerywordsmsg").innerText = "Select please!";
		return false;
	} else if (downloadfilename == "other_public.tsv") {
		document.getElementById("OtherPublicquerywordsmsg").innerText = "";
		window.location.href = "./myfiles/other_public.tsv";
		return true;
	} else
		return false;
}

// source for downloading page
function DGRPProjectDownload() {
	var downloadfilename = document.getElementById("DGRPProjectDownload").value;
	if (downloadfilename == "selectplease") {
		document.getElementById("DGRPquerywordsmsg").innerText = "Select please!";
		return false;
	} else if (downloadfilename == "dgrp_project.tsv") {
		document.getElementById("DGRPquerywordsmsg").innerText = "";
		window.location.href = "./myfiles/dgrp_project.tsv";
		return true;
	} else
		return false;
}

function select(s) {
	if (s == "indels in IFTiso.zip" || s == "snps in IFTiso.zip")
		document.getElementById("EMSquerywordsmsg").innerText = "IFTiso: The fly which the EMS starts with X chromosome isogenized.";
	else
		document.getElementById("EMSquerywordsmsg").innerText = "";
}

// source for downloading page
function EMSProjectDownload() {
	var downloadfilename = document.getElementById("EMSProjectDownload").value;
	if (downloadfilename == "selectplease") {
		document.getElementById("EMSquerywordsmsg").innerText = "Select please!";
		return false;
	} else if (downloadfilename == "summary of indels with frequency information.zip") {
		document.getElementById("EMSquerywordsmsg").innerText = "";
		window.location.href = "./myfiles/summary of indels with frequency information.zip";
		return true;
	} else if (downloadfilename == "summary of snps frequency information.zip") {
		document.getElementById("EMSquerywordsmsg").innerText = "";
		window.location.href = "./myfiles/summary of snps frequency information.zip";
		return true;
	} else if (downloadfilename == "a big vcf of all indels.zip") {
		document.getElementById("EMSquerywordsmsg").innerText = "";
		window.location.href = "./myfiles/a big vcf of all indels.zip";
		return true;
	} else if (downloadfilename == "a big vcf of all snps.zip") {
		document.getElementById("EMSquerywordsmsg").innerText = "";
		window.location.href = "./myfiles/a big vcf of all snps.zip";
		return true;
	} else if (downloadfilename == "indels in IFTiso.zip") {
		document.getElementById("EMSquerywordsmsg").innerText = "";
		window.location.href = "./myfiles/indels in IFTiso.zip";
		return true;
	} else if (downloadfilename == "snps in IFTiso.zip") {
		document.getElementById("EMSquerywordsmsg").innerText = "";
		window.location.href = "./myfiles/snps in IFTiso.zip";
		return true;
	} else
		return false;
}

function ischeckemail() {
	var email = document.getElementById("email_input").value;
	if (email != "") {
		var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
		isok = reg.test(email);
		if (!isok) {
			$("#emailmsg").empty();
			$("#emailmsg").append("The format of email errors!");
			return false;
		} else {
			$("#emailmsg").empty();
			$("#emailmsg").append("                           ");
			return true;
		}
	}
	;
}

function querybyvariant() {
	judgebysample();
	document.getElementById("chooseFromAFile").style.display = "";
	document
			.getElementById("pastealist")
			.setAttribute(
					"placeholder",
					"The format of input:chr pos ref var.                                       example:chr2L 22025 T G");
	document.getElementById("result2email").style.display = "none";
	document.getElementById("divAnnotation").style.display = "";
	document.getElementById("choosesample").style.display = "none";
	document.getElementById("pastealist").style.display = "";
}
function querybysample() {
	judgebysample();
	document.getElementById("chooseFromAFile").style.display = "none";
	document.getElementById("result2email").style.display = "";
	document.getElementById("divAnnotation").style.display = "";
	document.getElementById("pastealist").style.display = "none";
	document.getElementById("above18M").style.display = "none";
	document.getElementById("choosesample").style.display = "";
}
function querybyregion() {
	judgebysample();
	document.getElementById("chooseFromAFile").style.display = "";
	document
			.getElementById("pastealist")
			.setAttribute(
					"placeholder",
					"The format of input:chr start end                                             example:chr2L 4000 8000");
	document.getElementById("result2email").style.display = "none";
	document.getElementById("divAnnotation").style.display = "";
	document.getElementById("choosesample").style.display = "none";
	document.getElementById("pastealist").style.display = "";
}
function querybygenenamewhole() {
	judgebysample();
	document.getElementById("chooseFromAFile").style.display = "";
	document
			.getElementById("pastealist")
			.setAttribute(
					"placeholder",
					"Input flybase id or annotation symbol or symbol                                                                                      example:FBgn0003278 or CG4033 or RpI135");
	document.getElementById("result2email").style.display = "none";
	document.getElementById("divAnnotation").style.display = "";
	document.getElementById("choosesample").style.display = "none";
	document.getElementById("pastealist").style.display = "";
}
function querybygenenameexon() {
	judgebysample();
	document.getElementById("chooseFromAFile").style.display = "";
	document
			.getElementById("pastealist")
			.setAttribute(
					"placeholder",
					"Input flybase id or annotation symbol or symbol                                                                                       example:FBgn0003278 or CG4033 or RpI135");
	document.getElementById("result2email").style.display = "none";
	document.getElementById("divAnnotation").style.display = "";
	document.getElementById("choosesample").style.display = "none";
	document.getElementById("pastealist").style.display = "";
}
function judgebysample() {
	var list = $('input:radio[name="typeForQuery"]:checked').val();
	if (list == null) {
		alert("choose type for query please!");
		return false;
	} else {

		if (list == "by_sample") {
			document.getElementById("databaseDGRP").checked = true;
			document.getElementById("databaseDGRP").disabled = true;
			document.getElementById("databaseHUGO").disabled = true;
			document.getElementById("databaseOTHER_PUBLIC").disabled = true;
			document.getElementById("databaseDGRP+HUGO").disabled = true;
		} else {
			document.getElementById("databaseDGRP").disabled = false;
			document.getElementById("databaseHUGO").disabled = false;
			document.getElementById("databaseOTHER_PUBLIC").disabled = false;
			document.getElementById("databaseDGRP+HUGO").disabled = false;
		}

	}
}

function querychangetype() {
	var rtl = document.getElementById("typeForQuery");
	var typeQuery = rtl.options[rtl.selectedIndex].value;
	if (typeQuery == "by_sample")
		document.getElementById("inputorchoose").innerText = "Choose a sample:";
	else
		document.getElementById("inputorchoose").innerText = "Input query words:";

	if (typeQuery == "by_sample") {
		document.getElementById("databaseForQuery").disabled = "disabled";
		var o = document.getElementById("databaseForQuery").options;
		o[1].selected = true;
	} else
		document.getElementById("databaseForQuery").disabled = "";
	if (typeQuery == "by_variant") {
		document
				.getElementById("pastealist")
				.setAttribute(
						"placeholder",
						"The format of input:chr pos ref var.                                       example:chr2L 5040 G GCC");
		document.getElementById("result2email").style.display = "none";
		document.getElementById("divAnnotation").style.display = "";
		document.getElementById("choosesample").style.display = "none";
		document.getElementById("pastealist").style.display = "";
	} else if (typeQuery == "by_sample") {
		document.getElementById("result2email").style.display = "";
		document.getElementById("divAnnotation").style.display = "";
		document.getElementById("pastealist").style.display = "none";
		document.getElementById("above18M").style.display = "none";
		document.getElementById("choosesample").style.display = "";

	} else if (typeQuery == "by_region") {
		document
				.getElementById("pastealist")
				.setAttribute(
						"placeholder",
						"The format of input:chr start end                                             example:chr2L 4000 8000");
		document.getElementById("result2email").style.display = "none";
		document.getElementById("divAnnotation").style.display = "";
		document.getElementById("choosesample").style.display = "none";
		document.getElementById("pastealist").style.display = "";
	} else if (typeQuery == "by_gene_name_whole") {
		document
				.getElementById("pastealist")
				.setAttribute(
						"placeholder",
						"Input flybase id or annotation symbol or symbol                                                                                      example:FBgn0039595 or CG10001 or AR-2");
		document.getElementById("result2email").style.display = "none";
		document.getElementById("divAnnotation").style.display = "";
		document.getElementById("choosesample").style.display = "none";
		document.getElementById("pastealist").style.display = "";
	} else if (typeQuery == "by_gene_name_exon") {
		document
				.getElementById("pastealist")
				.setAttribute(
						"placeholder",
						"Input flybase id or annotation symbol or symbol                                                                                       example:FBgn0039595 or CG10001 or AR-2");
		document.getElementById("result2email").style.display = "none";
		document.getElementById("divAnnotation").style.display = "";
		document.getElementById("choosesample").style.display = "none";
		document.getElementById("pastealist").style.display = "";
	} else {
		document.getElementById("pastealist").setAttribute("placeholder",
				"Paste a list");
		document.getElementById("result2email").style.display = "none";
		document.getElementById("divAnnotation").style.display = "";
		document.getElementById("choosesample").style.display = "none";
		document.getElementById("pastealist").style.display = "";
	}
}
function querycheck() {
	/*
	 * var databaseForQuery = document.getElementById("databaseForQuery").value;
	 * var typeForQuery = document.getElementById("typeForQuery").value;
	 */
	var pastealist = document.getElementById("pastealist").value;
	var file = document.getElementById("file").value;
	var filesize = queryFileSelect();
	var email_input = document.getElementById("email_input").value;
	var chooseSample = document.getElementById("choosesample").value;
	var typelist = $('input:radio[name="typeForQuery"]:checked').val();
	if (typelist == "by_sample") {
		if (email_input == "") {
			document.getElementById("emailmsg").innerText = "Input email address please!";
			return false;
		} else if (chooseSample == "selectplease") {
			document.getElementById("querywordsmsg").innerText = "Choose a sample please!";
			return false;
		} else if (!ischeckemail())
			return false;
	}
	if (pastealist != "" && typelist == "by_variant") {
		var temppastealist1 = pastealist;
		var str_byvariant = temppastealist1.trim().replace(/\s+/g, "_")
				.replace(/\t+/g, "_").replace(/\r\n/g, "_");
		var pastearray = new Array();
		pastearray = str_byvariant.split(/_+/g);
		if (pastearray.length % 4 != 0) {
			document.getElementById("querywordsmsg").innerText = "The format of input:chr pos ref var!";
			return false;
		}
	}
	if (pastealist != "" && typelist == "by_region") {
		var temppastealist2 = pastealist;
		var str_byregion = temppastealist2.trim().replace(/\s+/g, "_").replace(
				/\t+/g, "_").replace(/\r\n/g, "_");
		var by_regionarray = new Array();
		by_regionarray = str_byregion.split(/_+/g);
		if (by_regionarray.length % 3 != 0) {
			document.getElementById("querywordsmsg").innerText = "The format of input:chr start end!";
			return false;
		}
	}
	if (pastealist == "" && file == "" && typelist != "by_sample") {
		document.getElementById("querywordsmsg").innerText = "Input query words or choose a file please!";
		return false;
	} else {
		document.getElementById("querywordsmsg").innerText = "                                          ";
	}
	if (filesize > 18)
		if (!ischeckemail())
			return false;
	loadinggif();
	return true;
}
function queryFileSelect() {

	var browserCfg = {};
	var ua = window.navigator.userAgent;
	if (ua.indexOf("MSIE") >= 1) {
		browserCfg.ie = true;
	} else if (ua.indexOf("Firefox") >= 1) {
		browserCfg.firefox = true;
	} else if (ua.indexOf("Chrome") >= 1) {
		browserCfg.chrome = true;
	}
	if (ua.indexOf("MSIE") >= 1) {
		browserCfg.ie = true;
	}
	// function checkfile(){
	try {
		var obj_file = document.getElementById("file");
		var filesize = 0;

		if (browserCfg.ie) {
			var obj_img = document.getElementById("tempimg");
			obj_img.dynsrc = obj_file.value;
			filesize = obj_img.fileSize;
		} else {
			if (browserCfg.ie) {
				var obj_img = document.getElementById("tempimg");
				obj_img.dynsrc = obj_file.value;
				filesize = obj_img.fileSize;
			} else {
				filesize = obj_file.files[0].size;
			}
		}
		if (Math.ceil(filesize / (1024 * 1024)) > 18) {
			document.getElementById("result2email").style.display = "";
			document.getElementById("above18M").style.display = "";
			var oInput = document.getElementById("email_input");
			oInput.focus();
		} else {
			document.getElementById("result2email").style.display = "none";
			document.getElementById("above18M").style.display = "none";
		}
		return Math.ceil(filesize / (1024 * 1024));
	} catch (e) {
		// alert(e);
	}
	// }
}
function loadinggif() {
	var msgw, msgh;
	msgw = 70;
	msgh = 70;
	var sWidth, sHeight;
	sWidth = document.body.offsetWidth;
	// screen.width
	sHeight = screen.height;
	var bgObj = document.createElement("div");
	// <div id="bgDiv" style="position:absolute; top:0; background-color:#777;
	// filter:progid:DXImagesTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75);
	// opacity:0.6; left:0; width:918px; height:768px; z-index:10000;"></div>
	bgObj.setAttribute("id", "bgDiv");
	bgObj.style.position = "absolute";
	bgObj.style.top = "0";
	bgObj.style.background = "#777";
	bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
	bgObj.style.opacity = "0.6";
	bgObj.style.left = "0";
	bgObj.style.width = sWidth + "px";
	bgObj.style.height = sHeight + "px";
	bgObj.style.zIndex = "10000";
	document.body.appendChild(bgObj);
	var msgObj = document.createElement("div")
	// <div id="msgDiv" align="center" style="background-color:white; border:1px
	// solid #336699; position:absolute; left:50%; top:50%; font:12px/1.6em
	// Verdana,Geneva,Arial,Helvetica,sans-serif; margin-left:-225px;
	// margin-top:npx; width:400px; height:100px; text-align:center;
	// line-height:25px; z-index:100001;"></div>
	msgObj.setAttribute("id", "msgDiv");
	msgObj.setAttribute("align", "center");
	msgObj.style.background = "white";
	msgObj.style.position = "absolute";
	msgObj.style.left = "60%";
	msgObj.style.top = "50%";
	msgObj.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	msgObj.style.marginLeft = "-225px";
	msgObj.style.marginTop = -75 + document.documentElement.scrollTop + "px";
	msgObj.style.width = msgw + "px";
	msgObj.style.height = msgh + "px";
	msgObj.style.textAlign = "center";
	msgObj.style.lineHeight = "25px";
	msgObj.style.zIndex = "10001";
	msgObj.className = "load_rotate";
	// msgObj.innerHTML = "<img src=\"img/loading.gif\" style=\"float:left;\"
	// />&nbsp;&nbsp;<span style=\"color:#000000;
	// font-size:15px\">loading...</span>";
	var img = document.createElement("span");
	// img.setAttribute("id","loadinglabel");
	// img.setAttribute("value","Loading...Wait for a while please!");
	document.body.appendChild(msgObj);
	document.getElementById("msgDiv").appendChild(img);
}

function registercheck() {
	var accountvalue = document.getElementById("account").value;
	var passwordvalue = document.getElementById("password").value;
	var emailvalue = document.getElementById("email_input").value;
	if (accountvalue == null || accountvalue == "") {
		document.getElementById("accountmsg").innerText = "account is empty!";
		return false;
	} else
		document.getElementById("accountmsg").innerText = "";
	if (passwordvalue == null || passwordvalue == "") {
		document.getElementById("passwordmsg").innerText = "password is empty!";
		return false;
	} else
		document.getElementById("passwordmsg").innerText = "";
	if (emailvalue == null || emailvalue == "") {
		document.getElementById("emailmsg").innerText = "email is empty!";
		return false;
	} else {
		document.getElementById("emailmsg").innerText = "";
		if (!ischeckemail())
			return false;
	}
	return true;
}
function logincheck() {
	var accountvalue = document.getElementById("account").value;
	var passwordvalue = document.getElementById("password").value;
	if (accountvalue == null || accountvalue == "") {
		document.getElementById("accountmsg").innerText = "account is empty!";
		return false;
	} else
		document.getElementById("accountmsg").innerText = "";
	if (passwordvalue == null || passwordvalue == "") {
		document.getElementById("passwordmsg").innerText = "password is empty!";
		return false;
	} else
		document.getElementById("passwordmsg").innerText = "";
	return true;
}
function annotationColumn() {
	document
			.getElementById("pastealist")
			.setAttribute(
					"placeholder",
					"The format of input:chr pos ref var.                                       example:chr2L 5040 G GCC");
}
function annotationVcfFormat() {
	document.getElementById("pastealist").setAttribute("placeholder",
			"Input in vcf format please!");
}
function annotationcheck() {
	var annotationemail = document.getElementById("email_input").value;
	var pastealistvalue = document.getElementById("pastealist").value;
	var file = document.getElementById("file").value;
	var filesize = annotationFileSelect();
	var New = document.getElementsByName("typeofinput");
	var strNew;
	for (var i = 0; i < New.length; i++) {
		if (New.item(i).checked) {
			strNew = New.item(i).getAttribute("value");
			break;
		} else {
			continue;
		}
	}
	if (pastealistvalue == "" && file == "") {
		document.getElementById("querywordsmsg").innerText = "input query words or choose a file please!";
		// alert('input query words or choose a file please!');
		return false;
	}
	if (pastealistvalue != "" && file == "" && strNew == "0") {
		var temppastealist_anno = pastealistvalue;
		var str__anno = temppastealist_anno.trim().replace(/\s+/g, "_")
				.replace(/\t+/g, "_").replace(/\r\n/g, "_");
		var pastearray_anno = new Array();
		pastearray_anno = str__anno.split(/_+/g);
		if (pastearray_anno.length % 4 != 0) {
			document.getElementById("annotationwordsmsg").innerText = "The format of input:chr pos ref var!";
			return false;
		}
	}
	if (filesize > 18) {
		if (!ischeckemail())
			return false;
	}
	loadinggif();
	return true;
}
function annotationFileSelect() {
	var browserCfg = {};
	var ua = window.navigator.userAgent;
	if (ua.indexOf("MSIE") >= 1) {
		browserCfg.ie = true;
	}
	// function checkfile(){
	try {
		var obj_file = document.getElementById("file");
		var filesize = 0;

		if (browserCfg.ie) {
			var obj_img = document.getElementById('tempimg');
			obj_img.dynsrc = obj_file.value;
			filesize = obj_img.fileSize;
		} else {
			filesize = obj_file.files[0].size;
		}
		if (Math.ceil(filesize / (1024 * 1024)) > 18) {
			document.getElementById("result2email").style.display = "";
			var oInput = document.getElementById("email_input");
			oInput.focus();
		} else
			document.getElementById("result2email").style.display = "none";
		return Math.ceil(filesize / (1024 * 1024));
	} catch (e) {
		// alert(e);
	}
	// }
}

function filtercheck() {

	var pastealistvalue = document.getElementById("pastealist").value;
	// var databaseForFilter=document.getElementById("databaseForFilter").value;
	var rtl = document.getElementsByName("formatofinput");
	var file = document.getElementById("file").value;
	var filesize = annotationFileSelect();
	var format;
	var email = document.getElementById("email_input").value;
	for (var i = 0; i < rtl.length; i++) {
		if (rtl[i].checked) {
			format = i;
		}
	}
	if (pastealistvalue == "" && file == "") {
		document.getElementById("querywordsmsg").innerText = "input query words or choose a file please!";
		// alert('input query words or choose a file please!');
		return false;

	} else
		document.getElementById("querywordsmsg").innerText = "                                          ";
	if (pastealistvalue != "") {

		var str = pastealistvalue.trim().replace(/\s+/g, "_").replace(/\t+/g,
				"_").replace(/\r\n/g, "_");
		var pastearray = new Array();
		pastearray = str.split(/_+/g);
		if (format == 0) {
			if (pastearray.length % 4 != 0) {
				document.getElementById("querywordsmsg").innerText = "The format of input:chr pos ref var!";
				return false;
			}
		}
	}
	if (filesize > 18) {
		if (email == "") {
			document.getElementById("emailmsg").innerText = "email is empty!";
			return false;
		}
		if (!ischeckemail())
			return false;
	}

	loadinggif();
	return true;
}
function filterFormat() {
	var rtl = document.getElementsByName("formatofinput");
	for (var i = 0; i < rtl.length; i++) {
		if (rtl[i].checked) {
			if (i == 0)
				document
						.getElementById("pastealist")
						.setAttribute(
								"placeholder",
								"format of input:chr pos ref alt                                       example:chr2L 5040 G GCC");
			else
				document.getElementById("pastealist").setAttribute(
						"placeholder", "input in vcf format");
		}
	}
}
function checkfrequency() {
	// var frequency=document.getElementById("frequency");
	if ($("#frequency").is(":checked")) {
		document.getElementById("frequencydiv").style.display = "";
	} else
		document.getElementById("frequencydiv").style.display = "none";
}

// function checkdownload()
// {
// window.location.href="./myfiles/dispensible_nonlastANDsharedExon_stopgain_or_frameshift.frameshift";
// }

function suggestionscheck() {
	var accountvalue = document.getElementById("suggestions").value;
	if (accountvalue == null || accountvalue == "") {
		document.getElementById("accountmsg").innerText = "suggestions is empty!";
		return false;
	} else
		document.getElementById("accountmsg").innerText = "";
	return true;
}
