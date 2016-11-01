var calendar = new Date();
var day = calendar.getDay();
var month = calendar.getMonth() + 1;
var date = calendar.getDate();
var year = calendar.getYear();
if (year < 1900)
	year = 1900 + year;
if (year < 100)
	year = 1900 + year;
cent = parseInt(year / 100);
g = year % 19;
k = parseInt((cent - 17) / 25);
i = (cent - parseInt(cent / 4) - parseInt((cent - k) / 3) + 19 * g + 15) % 30;
i = i - parseInt(i / 28) * (1 - parseInt(i / 28) * parseInt(29 / (i + 1)) * parseInt((21 - g) / 11));
j = (year + parseInt(year / 4) + i + 2 - cent + parseInt(cent / 4)) % 7;
l = i - j;
emonth = 3 + parseInt((l + 40) / 44);
edate = l + 28 - 31 * parseInt((emonth / 4));
emonth--;
var dayname = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
		"Friday", "Saturday");
var now, hours, minutes, seconds, timeValue;
function showtime() {
	now = new Date();
	hours = now.getHours();
	minutes = now.getMinutes();
	seconds = now.getSeconds();
	timeValue = "";
	timeValue += hours + ":";
	timeValue += ((minutes < 10) ? "0" : "") + minutes + ":";
	timeValue += ((seconds < 10) ? "0" : "") + seconds + " ";
	clock_time.innerHTML = year + "/" + month + "/" + date + " "
			+ timeValue + " " + dayname[day] + "<br />";
	setTimeout("showtime()", 100);
}
showtime();