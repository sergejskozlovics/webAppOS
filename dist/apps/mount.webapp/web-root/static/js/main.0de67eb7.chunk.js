(this["webpackJsonpmount.webapp"]=this["webpackJsonpmount.webapp"]||[]).push([[0],[,,,,,function(e,t,n){e.exports=n.p+"static/media/logo.5d5d9eef.svg"},,,function(e,t,n){e.exports=n(15)},,,,,function(e,t,n){},function(e,t,n){},function(e,t,n){"use strict";n.r(t);var o=n(0),a=n.n(o),r=n(3),s=n.n(r),c=(n(13),n(4)),l=n(1),i=n(7),u=n(6),p=n(5),m=n.n(p),d=(n(14),window.webappos),f=function(e){Object(i.a)(n,e);var t=Object(u.a)(n);function n(e){var o;return Object(c.a)(this,n),(o=t.call(this,e)).state={mount_points:[],supported_fs:[]},o}return Object(l.a)(n,[{key:"collectMountPointsRecursively",value:function(e,t,n){for(var o in e)"string"===typeof e[o]?t.push({mountPoint:n+o,location:e[o]}):this.collectMountPointsRecursively(e[o],t,n+o+"/");return t}},{key:"updateState",value:function(){var e=this;d.webcall_and_wait("webappos.getUserRegistryValue","fs_mount_points").then((function(t){t.result&&e.setState((function(){e.collectMountPointsRecursively(t.result,[],"")}))}))}}]),Object(l.a)(n,[{key:"componentDidMount",value:function(){var e=this;d.request_scopes("webappos_scopes","login").then((function(){d.webcall_and_wait("webappos.getSupportedFileSystems").then((function(t){t.result&&e.setState((function(){return{supported_fs:t.result}}),(function(){e.updateState()}))}))}))}},{key:"componentDidUpdate",value:function(){console.log("state",this.state)}}]),n}(a.a.Component);var h=function(){return a.a.createElement("div",{className:"App"},a.a.createElement("header",{className:"App-header"},a.a.createElement("img",{src:m.a,className:"App-logo",alt:"logo"}),a.a.createElement("p",null,"Edit ",a.a.createElement("code",null,"src/App.js")," and save to reload."),a.a.createElement("p",null),a.a.createElement(f,null),a.a.createElement("a",{className:"App-link",href:"https://reactjs.org",target:"_blank",rel:"noopener noreferrer"},"Learn React")))};Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));s.a.render(a.a.createElement(a.a.StrictMode,null,a.a.createElement(h,null)),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then((function(e){e.unregister()})).catch((function(e){console.error(e.message)}))}],[[8,1,2]]]);
//# sourceMappingURL=main.0de67eb7.chunk.js.map