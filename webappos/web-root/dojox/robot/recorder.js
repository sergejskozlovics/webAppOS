//>>built
define("dojox/robot/recorder",["dojo","dijit","dojox"],function(_1,_2,_3){
_1.provide("dojox.robot.recorder");
_1.experimental("dojox.robot.recorder");
(function(){
var _4=1000;
var _5=500;
var _6=10000;
var _7=[];
var _8=0;
var _9=null;
var _a=null;
var _b=function(){
alert("Started recording.");
_7=[];
_9=new Date();
_a=new Date();
};
var _c=function(_d,_e){
if(_9==null||_d=="doh.robot.keyPress"&&_e[0]==_1.keys.ENTER&&eval("("+_e[2]+")").ctrl&&eval("("+_e[2]+")").alt){
return;
}
var dt=Math.max(Math.min(Math.round((new Date()).getTime()-_a.getTime()),_6),1);
if(_d=="doh.robot.mouseMove"){
_e[2]=dt;
}else{
_e[1]=dt;
}
_7.push({name:_d,args:_e});
_a=new Date();
};
var _f=function(){
var c=_7;
if(c[0].name=="doh.robot.keyPress"&&(c[0].args[0]==_1.keys.ENTER||c[0].args[0]==77)){
c.splice(0,1);
}
for(var i=c.length-1;(i>=c.length-2)&&(i>=0);i--){
if(c[i].name=="doh.robot.keyPress"&&c[i].args[0]==_1.keys.ALT||c[i].args[0]==_1.keys.CTRL){
c.splice(i,1);
}
}
for(i=0;i<c.length;i++){
var _10,_11;
if(c[i+1]&&c[i].name=="doh.robot.mouseMove"&&c[i+1].name==c[i].name&&c[i+1].args[2]<_5){
_10=c[i+1];
_11=0;
while(_10&&_10.name==c[i].name&&_10.args[2]<_5){
c.splice(i+1,1);
_11+=_10.args[2];
c[i].args[0]=_10.args[0];
c[i].args[1]=_10.args[1];
_10=c[i+1];
}
c[i].args[3]=_11;
}else{
if(c[i+1]&&c[i].name=="doh.robot.mouseWheel"&&c[i+1].name==c[i].name&&c[i+1].args[1]<_5){
_10=c[i+1];
_11=0;
while(_10&&_10.name==c[i].name&&_10.args[1]<_5){
c.splice(i+1,1);
_11+=_10.args[1];
c[i].args[0]+=_10.args[0];
_10=c[i+1];
}
c[i].args[2]=_11;
}else{
if(c[i+2]&&c[i].name=="doh.robot.mouseMoveAt"&&c[i+2].name=="doh.robot.scrollIntoView"){
var _12=c.splice(i+2,1)[0];
c.splice(i,0,_12);
}else{
if(c[i+1]&&c[i].name=="doh.robot.mousePress"&&c[i+1].name=="doh.robot.mouseRelease"&&c[i].args[0]==c[i+1].args[0]){
c[i].name="doh.robot.mouseClick";
c.splice(i+1,1);
if(c[i+1]&&c[i+1].name=="doh.robot.mouseClick"&&c[i].args[0]==c[i+1].args[0]){
c.splice(i+1,1);
}
}else{
if(c[i+1]&&c[i-1]&&c[i-1].name=="doh.robot.mouseMoveAt"&&c[i].name=="doh.robot.mousePress"&&c[i+1].name=="doh.robot.mouseMove"){
var cmd={name:"doh.robot.mouseMoveAt",args:[c[i-1].args[0],1,100,c[i-1].args[3]+1,c[i-1].args[4]]};
c.splice(i+1,0,cmd);
}else{
if(c[i+1]&&((c[i].name=="doh.robot.keyPress"&&typeof c[i].args[0]=="string")||c[i].name=="doh.robot.typeKeys")&&c[i+1].name=="doh.robot.keyPress"&&typeof c[i+1].args[0]=="string"&&c[i+1].args[1]<=_4&&!eval("("+c[i].args[2]+")").ctrl&&!eval("("+c[i].args[2]+")").alt&&!eval("("+c[i+1].args[2]+")").ctrl&&!eval("("+c[i+1].args[2]+")").alt){
c[i].name="doh.robot.typeKeys";
c[i].args.splice(3,1);
_10=c[i+1];
var _13=0;
while(_10&&_10.name=="doh.robot.keyPress"&&typeof _10.args[0]=="string"&&_10.args[1]<=_4&&!eval("("+_10.args[2]+")").ctrl&&!eval("("+_10.args[2]+")").alt){
c.splice(i+1,1);
c[i].args[0]+=_10.args[0];
_13+=_10.args[1];
_10=c[i+1];
}
c[i].args[2]=_13;
c[i].args[0]="'"+c[i].args[0]+"'";
}else{
if(c[i].name=="doh.robot.keyPress"){
if(typeof c[i].args[0]=="string"){
c[i].args[0]="'"+c[i].args[0]+"'";
}else{
if(c[i].args[0]==0){
c.splice(i,1);
}else{
for(var j in _1.keys){
if(_1.keys[j]==c[i].args[0]){
c[i].args[0]="dojo.keys."+j;
break;
}
}
}
}
}
}
}
}
}
}
}
}
};
var _14=function(){
if(!_9){
_b();
}else{
_15();
}
};
var _15=function(){
var dt=Math.round((new Date()).getTime()-_9.getTime());
_9=null;
_f();
var c=_7;
if(c.length){
var s="doh.register('dojox.robot.AutoGeneratedTestGroup',{\n";
s+="     name: 'autotest"+(_8++)+"',\n";
s+="     timeout: "+(dt+2000)+",\n";
s+="     runTest: function(){\n";
s+="          var d = new doh.Deferred();\n";
for(var i=0;i<c.length;i++){
s+="          "+c[i].name+"(";
for(var j=0;j<c[i].args.length;j++){
var arg=c[i].args[j];
s+=arg;
if(j!=c[i].args.length-1){
s+=", ";
}
}
s+=");\n";
}
s+="          doh.robot.sequence(function(){\n";
s+="               if(/*Your condition here*/){\n";
s+="                    d.callback(true);\n";
s+="               }else{\n";
s+="                    d.errback(new Error('We got a failure'));\n";
s+="               }\n";
s+="          }, 1000);\n";
s+="          return d;\n";
s+="     }\n";
s+="});\n";
var div=document.createElement("div");
div.id="dojox.robot.recorder";
div.style.backgroundColor="white";
div.style.position="absolute";
var _16={y:(window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0),x:(window.pageXOffset||(window["dojo"]?_1._fixIeBiDiScrollLeft(document.documentElement.scrollLeft):undefined)||document.body.scrollLeft||0)};
div.style.left=_16.x+"px";
div.style.top=_16.y+"px";
var h1=document.createElement("h1");
h1.innerHTML="Your code:";
div.appendChild(h1);
var pre=document.createElement("pre");
if(pre.innerText!==undefined){
pre.innerText=s;
}else{
pre.textContent=s;
}
div.appendChild(pre);
var _17=document.createElement("button");
_17.innerHTML="Close";
var _18=_1.connect(_17,"onmouseup",function(e){
_1.stopEvent(e);
document.body.removeChild(div);
_1.disconnect(_18);
});
div.appendChild(_17);
document.body.appendChild(div);
_7=[];
}
};
var _19=function(_1a){
if(typeof _1a=="string"){
return "'"+_1a+"'";
}else{
if(_1a.id){
return "'"+_1a.id+"'";
}else{
var _1b=document.getElementsByTagName(_1a.nodeName);
var i;
for(i=0;i<_1b.length;i++){
if(_1b[i]==_1a){
break;
}
}
return "function(){ return document.getElementsByTagName('"+_1a.nodeName+"')["+i+"]; }";
}
}
};
var _1c=function(b){
return "{left:"+(b==0)+", middle:"+(b==1)+", right:"+(b==2)+"}";
};
var _1d=function(e){
return "{'shift':"+(e.shiftKey)+", 'ctrl':"+(e.ctrlKey)+", 'alt':"+(e.altKey)+"}";
};
_1.connect(document,"onkeydown",function(e){
if((e.keyCode==_1.keys.ENTER||e.keyCode==77)&&e.ctrlKey&&e.altKey){
_1.stopEvent(e);
_14();
}
});
var _1e={type:""};
var _1f=function(e){
if(!e||_1e.type==e.type&&_1e.button==e.button){
return;
}
_1e={type:e.type,button:e.button};
var _20=_19(e.target);
var _21=_1.coords(e.target);
_c("doh.robot.mouseMoveAt",[_20,0,100,e.clientX-_21.x,e.clientY-_21.y]);
_c("doh.robot.mousePress",[_1c(e.button-(_1.isIE?1:0)),0]);
};
var _22=function(e){
if(!e||_1e.type==e.type&&_1e.button==e.button){
return;
}
_1e={type:e.type,button:e.button};
var _23=_19(e.target);
var _24=_1.coords(e.target);
_c("doh.robot.mouseClick",[_1c(e.button-(_1.isIE?1:0)),0]);
};
var _25=function(e){
if(!e||_1e.type==e.type&&_1e.button==e.button){
return;
}
_1e={type:e.type,button:e.button};
var _26=_19(e.target);
var _27=_1.coords(e.target);
_c("doh.robot.mouseRelease",[_1c(e.button-(_1.isIE?1:0)),0]);
};
var _28=function(e){
if(!e||_1e.type==e.type&&_1e.pageX==e.pageX&&_1e.pageY==e.pageY){
return;
}
_1e={type:e.type,pageX:e.pageX,pageY:e.pageY};
_c("doh.robot.mouseMove",[e.pageX,e.pageY,0,100,true]);
};
var _29=function(e){
if(!e||_1e.type==e.type&&_1e.pageX==e.pageX&&_1e.pageY==e.pageY){
return;
}
_1e={type:e.type,detail:(e.detail?(e.detail):(-e.wheelDelta/120))};
_c("doh.robot.mouseWheel",[_1e.detail]);
};
var _2a=function(e){
if(!e||_1e.type==e.type&&(_1e.charCode==e.charCode&&_1e.keyCode==e.keyCode)){
return;
}
_1e={type:e.type,charCode:e.charCode,keyCode:e.keyCode};
_c("doh.robot.keyPress",[e.charOrCode==_1.keys.SPACE?" ":e.charOrCode,0,_1d(e)]);
};
var _2b=function(e){
if(!e||_1e.type==e.type&&(_1e.charCode==e.charCode&&_1e.keyCode==e.keyCode)){
return;
}
_1e={type:e.type,charCode:e.charCode,keyCode:e.keyCode};
};
_1.connect(document,"onmousedown",_1f);
_1.connect(document,"onmouseup",_25);
_1.connect(document,"onclick",_22);
_1.connect(document,"onkeypress",_2a);
_1.connect(document,"onkeyup",_2b);
_1.connect(document,"onmousemove",_28);
_1.connect(document,!_1.isMozilla?"onmousewheel":"DOMMouseScroll",_29);
_1.addOnLoad(function(){
if(_1.window){
_1.connect(_1.window,"scrollIntoView",function(_2c){
_c("doh.robot.scrollIntoView",[_19(_2c)]);
});
}
});
_1.connect(_1,"connect",function(_2d,_2e,f){
if(_2d&&(!f||!f._mine)){
var _2f=null;
if(_2e.toLowerCase()=="onmousedown"){
_2f=_1.hitch(this,_1f);
}else{
if(_2e.toLowerCase()==(!_1.isMozilla?"onmousewheel":"dommousescroll")){
_2f=_1.hitch(this,_29);
}else{
if(_2e.toLowerCase()=="onclick"){
_2f=_1.hitch(this,_22);
}else{
if(_2e.toLowerCase()=="onmouseup"){
_2f=_1.hitch(this,_25);
}else{
if(_2e.toLowerCase()=="onkeypress"){
_2f=_1.hitch(this,_2a);
}else{
if(_2e.toLowerCase()=="onkeyup"){
_2f=_1.hitch(this,_2b);
}
}
}
}
}
}
if(_2f==null){
return;
}
_2f._mine=true;
_1.connect(_2d,_2e,_2f);
}
});
})();
});
