define(function(){

    var login_redirect = function() { // private function
      localStorage.removeItem("login");
      localStorage.removeItem("ws_token");
      webappos.login = null;
      webappos.ws_token = null;
      var redirect = window.location.href;
      if ((redirect.indexOf("://login.")<0) && (redirect.indexOf("/apps/login/")<0))
         window.location.href = "/apps/login?redirect="+redirect;
    };
  
    var request_login = async function() { // private function
      var validateToken = false;
      if (!webappos.ws_token) {
          webappos.ws_token = webappos.js_util.get_and_remove_query_value("ws_token");
          if (!webappos.ws_token) {
            webappos.ws_token = localStorage.getItem("ws_token");
            if (webappos.ws_token)
              validateToken = true; // validating tokens only from localStorage
          }
      }
    
      if (!webappos.login) {
        var login = webappos.js_util.get_and_remove_query_value("login");
        if (login)
            webappos.login = login;
    
        if (!webappos.login) {
            if (webappos.project_id) {
              var i = webappos.project_id.indexOf("/");
              if (i>=0) {
                webappos.login = webappos.project_id.substring(0, i);
              }
            }
            if (!webappos.login) {
              webappos.login = localStorage.getItem("login");
            }
        }
      }

      if (validateToken) {
        console.log("Validating ws_token "+webappos.ws_token+" for "+webappos.login);
        var data = ("login="+webappos.login+"&ws_token="+webappos.ws_token).replace(/%20/g, '+');
  
        var xhr = new XMLHttpRequest();
  
        xhr.open("POST", "/services/login/check_ws_token", false);
  
        var retVal = null;
        xhr.onreadystatechange = function() {
            if (this.readyState == this.DONE) {
              var x = xhr.responseText.trim();
              try {
                var json = JSON.parse(x);
                if (json.error)
                  throw json.error;
              }
              catch(t) {
                console.log("Sign-in required: "+t);
                login_redirect();
                return false;
              }
            }
        };
  
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.send(data);
      }

      if (!webappos.login || !webappos.ws_token) {
        login_redirect();
        return false;
      }
      else {
        if (webappos.project_id!="standalone") {
          // store ws_token in localstorage
          if (webappos.login!=null)
            localStorage.setItem("login", webappos.login);
          if (webappos.ws_token!=null)
            localStorage.setItem("ws_token", webappos.ws_token);
        }        
      }

      return true;
    };
  
    var request_project_id = async function() {

      if (!webappos.project_id) {
        let q = webappos.js_util.get_query_value("project_id"); // do not remove from the URL!
        if (q) {
            webappos.set_project_id(q);
        }
  
      }

      if (webappos.project_id) {
        webappos.init_web_memory();
        if (webappos.project_id=="standalone") {
              tda.ee = {
                // simulating tda.ee functions via alert...
                searchForFrameAndExecuteCommand: function(obj) {
                  alert("#COMMAND#"+obj.reference);
                },
                searchForFrameAndHandleEvent: function(obj) {
                  alert("#EVENT#"+obj.reference);
                },
                log: function(s) {
                  alert("#LOG#"+s);//arguments.join(", "));
                },
                resizeFrame: function(frameRef, w, h) {
                  alert("#RESIZE#"+frameRef+","+w+","+h);
                },
                getScreenWidth: function() {
                  return prompt("#SCREEN_WIDTH#", 800);
                },
                getScreenHeight: function() {
                  return prompt("#SCREEN_HEIGHT#", 600);
                },
              };            
              console.log = tda.ee.log;
        }

        return true;
      }

      let promise = new Promise(async function(resolve,reject) {
        window["resolve_project_id"] = resolve;

        var newDiv = document.createElement("div");
        newDiv.id = "div"+webappos.js_util.timestamp();
        newDiv.style = 'position:absolute; left:0; top:0; width:100%; height:100%; overflow:auto; padding:15px;background-image: linear-gradient(45deg, #ffffff, #d0f0ff);';//background-color:white;';
        var contentURI = "<h1>"+webappos.app_displayed_name+"</h1>";
        if (webappos.app_icon_url)
          contentURI = "<img height='100' width='auto' src='"+webappos.app_icon_url+"'></img>"+contentURI;
  
        var INIT_CODE = "document.body.removeChild(document.getElementById('"+newDiv.id+"'));webappos.init_web_memory();let f=window.resolve_project_id; delete window.resolve_project_id; f();";
  
        if (!window.webappos.webcall("webappos.appRequiresTemplate", webappos.app_full_name).result) {
              contentURI+="<div style='border-bottom:1px dotted #888;'>New</div>";
              contentURI+="<div onclick=\"javascript:"+INIT_CODE+"\" style='display:inline-block; align:center; width:100px; min-height:100px; cursor:pointer; margin:10px; vertical-align:top;'>"+
                      "<div style='display:inline-block;width:100px;height:80px;'><img src='template-icons/new.png' width='100px' height='auto'></img></div>"+
                      "<p style='word-wrap: break-word; padding:0; margin:0;' align='center'>Bootstrap new</p>"+
                      "</div>";
        }
  
        contentURI+="<div style='border-bottom:1px dotted #888;'>Browse</div>";
        var s ="<div onclick=\"javascript:webappos.desktop.browse_for_file('open', '"+webappos.app_displayed_name+" files(*."+webappos.project_extension+")').then(function(fname){if (!fname) return;webappos.set_project_id(webappos.login+'/'+fname);"+INIT_CODE+"});\" style='display:inline-block; align:center; width:100px; min-height:100px; cursor:pointer; margin:10px; vertical-align:top; border:1px dotted; #888;'>"+
                      "<div style='display:inline-block;width:100px;height:80px;'><img src='template-icons/browse.png' width='100px' height='auto'></img></div>"+
                      "<p style='word-break: break-all; word-wrap: break-word; padding:0; margin:0;' align='center'>Browse</p>"+
                      "</div>";
        contentURI+=s;
  
        var arr = await window.webappos.webcall("webappos.getAppTemplates", webappos.app_full_name);
        if (webappos.js_util.is_array(arr)&&arr.length) {
          contentURI+="<p><div style='border-bottom:1px dotted #888;'>New from template</div>";
          for (var i=0; i<arr.length; i++) {
              var j = (arr[i]+"").indexOf(".");
              if (j>=0)
                arr[i] = (arr[i]+"").substring(0, j);
  
              contentURI+="<div onclick=\"javascript:webappos.project_id='apptemplate:"+arr[i]+"."+webappos.project_extension+"';"+INIT_CODE+"\" style='display:inline-block; align:center; width:100px; min-height:100px; cursor:pointer; margin:10px; vertical-align:top;'>"+
                      "<div style='display:inline-block;width:100px;height:80px;'><img src='template-icons/"+arr[i]+".png' width='100px' height='auto'></img></div>"+
                      "<p style='word-wrap: break-word; padding:0; margin:0;' align='center'>"+arr[i]+"</p>"+
                      "</div>";
  
          }
        }
  
        arr = await window.webappos.webcall("webappos.getPublishedTemplates", webappos.app_full_name);
        if (webappos.js_util.is_array(arr)&&arr.length) {
          contentURI+="<p><div style='border-bottom:1px dotted #888;'>New from published template</div>";
          for (var i=0; i<arr.length; i++) {
              var j = (arr[i]+"").indexOf(".");
              if (j>=0)
                arr[i] = (arr[i]+"").substring(0, j);
  
              contentURI+="<div onclick=\"javascript:webappos.project_id='publishedtemplate:"+arr[i]+"."+webappos.project_extension+"';"+INIT_CODE+"\" style='display:inline-block; align:center; width:100px; min-height:100px; cursor:pointer; margin:10px; vertical-align:top;'>"+
                      "<div style='display:inline-block;width:100px;height:80px;'><img src='template-icons/user.png' width='100px' height='auto'></img></div>"+
                      "<p style='word-wrap: break-word; padding:0; margin:0;' align='center'>"+arr[i]+"</p>"+
                      "</div>";
          }
        }
  
        arr = await window.webappos.webcall("webappos.getUserTemplates", webappos.app_full_name);
        if (webappos.js_util.is_array(arr)&&arr.length) {
          contentURI+="<p><div style='border-bottom:1px dotted #888;'>New from user template</div>";
          for (var i=0; i<arr.length; i++) {
              var j = (arr[i]+"").indexOf(".");
              if (j>=0)
                arr[i] = (arr[i]+"").substring(0, j);
  
              contentURI+="<div onclick=\"javascript:webappos.project_id='usertemplate:"+arr[i]+"."+webappos.project_extension+"';"+INIT_CODE+"\" style='display:inline-block; align:center; width:100px; min-height:100px; cursor:pointer; margin:10px; vertical-align:top;'>"+
                      "<div style='display:inline-block;width:100px;height:80px;'><img src='template-icons/user.png' width='100px' height='auto'></img></div>"+
                      "<p style='word-wrap: break-word; padding:0; margin:0;' align='center'>"+arr[i]+"</p>"+
                      "</div>";
          }
        }
  
        // word-break: break-all;
  
        newDiv.innerHTML = contentURI+"<br/><br/><br/><br/>";
  
        document.body.appendChild(newDiv);
  
      });

      return promise;
    };
  
    return {
        request_access: async function(scopes, serverless_only) {
          var arr = scopes.split(" ");
          for (var i=0; i<arr.length; i++) {
            if (arr[i]=="login")
              await request_login();
            else
            if (arr[i]=="project_id") {
              await request_login();
              await request_project_id();
            }
            else
              throw new Error("Only login and project_id scopes are supported.");
          }
          return true;
        },

        revoke_serverless_access: async function() {
          localStorage.removeItem("login");
          localStorage.removeItem("ws_token");
          var data = ("login="+webappos.login+"&ws_token="+webappos.ws_token).replace(/%20/g, '+');
          return new Promise((resolve,reject)=> {
            webappos.login = null;
            webappos.ws_token = null;
      
            var xhr = new XMLHttpRequest();
      
            xhr.open("POST", "/services/login/signout", false);
      
            var retVal = null;
            xhr.onreadystatechange = function() {
                if (this.readyState == this.DONE) {
                  window.location.href = "/apps/login?signout=true";
                  return resolve(true);
                }
            };
      
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.send(data);      
          });

        }
    };
});


