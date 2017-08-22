var secs = 5; //倒计时的秒数
var URL;
function Load(url, msg) {
    URL = url;
    for(var i = secs; i >= 0; i--) {
        window.setTimeout('doUpdate(' + i + ',\'' + msg + '\')', (secs-i) * 1000);
    }
}

function doUpdate(num, msg) {
    var show = document.getElementById('show');
    if (msg.indexOf('成功') > 0) {
        show.style.color = "#00FF00";
    } else {
        show.style.color = "#FF0000";
    }
    show.innerHTML = num + msg;
    if(num == 0) {
        window.location = URL;
    }
}