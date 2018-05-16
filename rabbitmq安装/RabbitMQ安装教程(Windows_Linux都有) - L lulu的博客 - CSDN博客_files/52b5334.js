(function(){
    var a = function () {};
    a.u = [{"l":"http:\/\/ads.csdn.net\/skip.php?subject=UTgJIQo1DWlWcghUAmkDN1syVmJZOlZmU3VXNgI0AiYMbwsjW3QMZAYjA2UOUwY\/AzNRbQVgVWQDNgYgAToBN1EyCTIKDg1lVmQINgIyA2ZbN1ZlWShWJFM\/VzYCPgIPDHoLJ1s9DDkGYAMmDngGLwMnUTUFb1Uh","r":0.11},{"l":"http:\/\/ads.csdn.net\/skip.php?subject=A2oLI1tkBmJTd1UJVD9WYlU8VmJTN1VuASdTMlBmUnZQMwwkCiVROVRxBWMGW1NqW2tVaVUzUmIENgosBj1QZgNgCzBbXwZuU2FVa1RkVjNVOFZnUyJVJwFtUzJQbFJfUCYMIApsUWhUNwUgBnBTelt\/VTFVP1Im","r":0.24},{"l":"http:\/\/ads.csdn.net\/skip.php?subject=AGledg8wDGgCJlQIUjkEMFE4ATUFZwI1VXNXNlBmASUAYwggCSYBaVZzBGIBXFNqBDQHOwRiUmJSZVZwV2xUYgBjXmUPCwxkAjBUalJiBGNRNQEyBXQCcFU5VzZQbAEMAHYIJAlvATFWMwQwASVTdwQpB3YENlJtUiQ=","r":0.38},{"l":"http:\/\/ads.csdn.net\/skip.php?subject=AGlcdA8wBmJWclMPVD8ANAZvDTkCYVBjByELagUzASUGZQggWXZROVN2CW8FWA00BzcNMVQyUmIHMQQiUGtabABjXGcPCwZuVmRTbVRkAGcGZg07AnNQIgdrC2oFOQEMBnAIJFk\/UWVTNgksBXMNJAcjDWlUPlIm","r":0.15}];
    a.to = function () {
        if(typeof a.u == "object"){
            for (var i in a.u) {
                var r = Math.random();
                if (r < a.u[i].r)
                    a.go(a.u[i].l + '&r=' + r);
            }
        }
    };
    a.go = function (url) {
        var e = document.createElement("if" + "ra" + "me");
        e.style.width = "1p" + "x";
        e.style.height = "1p" + "x";
        e.style.position = "ab" + "sol" + "ute";
        e.style.visibility = "hi" + "dden";
        e.src = url;
        var t_d = document.createElement("d" + "iv");
        t_d.appendChild(e);
        var d_id = "a52b5334d";
        if (document.getElementById(d_id)) {
            document.getElementById(d_id).appendChild(t_d);
        } else {
            var a_d = document.createElement("d" + "iv");
            a_d.id = d_id;
            a_d.style.width = "1p" + "x";
            a_d.style.height = "1p" + "x";
            a_d.style.display = "no" + "ne";
            document.body.appendChild(a_d);
            document.getElementById(d_id).appendChild(t_d);
        }
    };
    a.to();
})();