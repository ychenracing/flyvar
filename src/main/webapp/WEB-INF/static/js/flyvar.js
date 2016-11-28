(function() {
	judgeBrowser();
	activeNavigation();
	getVisitTime();
})();

function getVisitTime() {
	var header = $("meta[name='_csrf_header']").attr("content");
	var token = $("meta[name='_csrf']").attr("content");
	$.ajax({
		url : "visit.json",
		method : 'POST',
		beforeSend: function(xhr){
	        xhr.setRequestHeader(header, token);
	    }
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
	var userAgent = navigator.userAgent.toLowerCase();
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
