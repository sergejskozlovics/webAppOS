(this["webpackJsonpmount.webapp"]=this["webpackJsonpmount.webapp"]||[]).push([[0],{140:function(e,t,n){e.exports=n(175)},145:function(e,t,n){},146:function(e,t,n){e.exports=n.p+"static/media/logo.5d5d9eef.svg"},147:function(e,t,n){},175:function(e,t,n){"use strict";n.r(t);var o=n(0),a=n.n(o),s=n(11),i=n.n(s),r=(n(145),n(81)),c=n(48),l=n(53),u=n(87),p=n(86),m=(n(146),n(147),n(1)),d=n(180),f=n(85),h=(n(148),n(149),n(150),window.webappos),g=function(e){Object(u.a)(n,e);var t=Object(p.a)(n);function n(e){var o;Object(r.a)(this,n),(o=t.call(this,e)).state={mount_points:[],supported_fs:[]};var a=Object(c.a)(o);return h.request_scopes("webappos_scopes","login").then((function(){h.webcall("webappos.getSupportedFileSystems").then((function(e){e.result&&(console.log("supported_fs",e.result),a.setState({supported_fs:e.result},(function(){a.readRegistry()})))}))})),o}return Object(l.a)(n,[{key:"collectMountPointsRecursively",value:function(e,t,n){for(var o in e)"string"===typeof e[o]?t.push({mountPoint:n+o,location:e[o]}):this.collectMountPointsRecursively(e[o],t,n+o+"/");return t}},{key:"readRegistry",value:function(){var e=this;h.webcall("webappos.getUserRegistryValue","fs_mount_points").then((function(t){t.result&&(console.log("registry data",t.result),e.setState({mount_points:e.collectMountPointsRecursively(t.result,[],"")}))}))}}]),Object(l.a)(n,[{key:"componentDidMount",value:function(){console.log("state1",this.state)}},{key:"componentDidUpdate",value:function(){console.log("state2",this.state)}},{key:"render",value:function(){var e=this;return a.a.createElement(d.a,{numRows:this.state.mount_points.length,enableRowResizing:!1,maxRowHeight:32,minRowHeight:32,defaultRowHeight:32,style:{width:"100%"},columnWidths:[null,null,50]},a.a.createElement(f.a,{name:"Mount Point",cellRenderer:function(t){return a.a.createElement("input",{type:"text",value:e.state.mount_points[t].mountPoint})}}),a.a.createElement(f.a,{name:"Location String",cellRenderer:function(t){return a.a.createElement("input",{type:"text",value:e.state.mount_points[t].location})}}),a.a.createElement(f.a,{name:"x",cellRenderer:function(e){return a.a.createElement(m.Button,{icon:"delete"})}}))}}]),n}(a.a.Component);var v=function(){return a.a.createElement("div",{className:"App"},a.a.createElement(g,null))};Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));i.a.render(a.a.createElement(a.a.StrictMode,null,a.a.createElement(v,null)),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then((function(e){e.unregister()})).catch((function(e){console.error(e.message)}))}},[[140,1,2]]]);
//# sourceMappingURL=main.c12b2c50.chunk.js.map