/*
	Copyright (c) 2004-2016, The JS Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/

//>>built
define("dojo/_base/kernel",["../global","../has","./config","require","module"],function(_1,_2,_3,_4,_5){
var i,p,_6={},_7={},_8={config:_3,global:_1,dijit:_6,dojox:_7};
var _9={dojo:["dojo",_8],dijit:["dijit",_6],dojox:["dojox",_7]},_a=(_4.map&&_4.map[_5.id.match(/[^\/]+/)[0]]),_b;
for(p in _a){
if(_9[p]){
_9[p][0]=_a[p];
}else{
_9[p]=[_a[p],{}];
}
}
for(p in _9){
_b=_9[p];
_b[1]._scopeName=_b[0];
if(!_3.noGlobals){
_1[_b[0]]=_b[1];
}
}
_8.scopeMap=_9;
_8.baseUrl=_8.config.baseUrl=_4.baseUrl;
_8.isAsync=!1||_4.async;
_8.locale=_3.locale;
var _c="$Rev: d6e8ff38 $".match(/[0-9a-f]{7,}/);
_8.version={major:1,minor:14,patch:2,flag:"",revision:_c?_c[0]:NaN,toString:function(){
var v=_8.version;
return v.major+"."+v.minor+"."+v.patch+v.flag+" ("+v.revision+")";
}};
1||_2.add("extend-dojo",1);
if(!_2("csp-restrictions")){
(Function("d","d.eval = function(){return d.global.eval ? d.global.eval(arguments[0]) : eval(arguments[0]);}"))(_8);
}
if(0){
_8.exit=function(_d){
quit(_d);
};
}else{
_8.exit=function(){
};
}
if(!_2("host-webworker")){
1||_2.add("dojo-guarantee-console",1);
}
if(1){
_2.add("console-as-object",function(){
return Function.prototype.bind&&console&&typeof console.log==="object";
});
typeof console!="undefined"||(console={});
var cn=["assert","count","debug","dir","dirxml","error","group","groupEnd","info","profile","profileEnd","time","timeEnd","trace","warn","log"];
var tn;
i=0;
while((tn=cn[i++])){
if(!console[tn]){
(function(){
var _e=tn+"";
console[_e]=("log" in console)?function(){
var a=Array.prototype.slice.call(arguments);
a.unshift(_e+":");
console["log"](a.join(" "));
}:function(){
};
console[_e]._fake=true;
})();
}else{
if(_2("console-as-object")){
console[tn]=Function.prototype.bind.call(console[tn],console);
}
}
}
}
_2.add("dojo-debug-messages",!!_3.isDebug);
_8.deprecated=_8.experimental=function(){
};
if(_2("dojo-debug-messages")){
_8.deprecated=function(_f,_10,_11){
var _12="DEPRECATED: "+_f;
if(_10){
_12+=" "+_10;
}
if(_11){
_12+=" -- will be removed in version: "+_11;
}
console.warn(_12);
};
_8.experimental=function(_13,_14){
var _15="EXPERIMENTAL: "+_13+" -- APIs subject to change without notice.";
if(_14){
_15+=" "+_14;
}
console.warn(_15);
};
}
1||_2.add("dojo-modulePaths",1);
if(1){
if(_3.modulePaths){
_8.deprecated("dojo.modulePaths","use paths configuration");
var _16={};
for(p in _3.modulePaths){
_16[p.replace(/\./g,"/")]=_3.modulePaths[p];
}
_4({paths:_16});
}
}
1||_2.add("dojo-moduleUrl",1);
if(1){
_8.moduleUrl=function(_17,url){
_8.deprecated("dojo.moduleUrl()","use require.toUrl","2.0");
var _18=null;
if(_17){
_18=_4.toUrl(_17.replace(/\./g,"/")+(url?("/"+url):"")+"/*.*").replace(/\/\*\.\*/,"")+(url?"":"/");
}
return _18;
};
}
_8._hasResource={};
return _8;
});
