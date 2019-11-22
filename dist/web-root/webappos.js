/**
 Document: webappos.js functions

 Each webAppOS app should include the webappos.js script, which ensures the existence of the window.webappos JavaScript object for calling webAppOS-related functions and services.

 */

script_label: {


  ///// ensure the require is defined /////
  if (typeof require == 'undefined') {

    let webappos_script = document.currentScript.src || document.querySelector('script[src*=webappos.js]').src;

    /*    document.write('<script src="/dojo/dojo.js" data-dojo-config="async:1"></script>');*/

    document.write('<script src="/dojo/dojo.js" data-dojo-config="async:1, packages: [{ name: \'jquery\', location: \'/\', main: \'jquery\' }]"></script>');
    /*    document.write('<script>define.amd.jQuery = true;</script>');*/

    document.write('<script src="' + webappos_script + '"></script>');
    document.write("<link id=\"the_webappos_css\" href=\"" + "/webappos/webappos.css\" rel=\"stylesheet\" type=\"text/css\" />");
    document.write("<link id=\"the_webappos_css\" href=\"" + "/webappos/djtheme/webappos.css\" rel=\"stylesheet\" type=\"text/css\" />");
    document.write("<link id=\"the_webappos_css\" href=\"" + "/webappos/djtheme/webappos.css\" rel=\"stylesheet\" type=\"text/css\" />");

  
    console.log(webappos_script + " loaded without dojo; adding the dojo script tag and loading " + webappos_script + " again...");
    break script_label;
    //variant w/o a label: throw new Error(webappos_script+" loaded without dojo; adding the dojo script tag and loading "+webappos_script+" again...");
  }


  ///// adding webAppOS fonts /////
  if (!document.getElementById("the_webappos_css"))
    document.write("<link id=\"the_webappos_css\" href=\"webappos/webappos.css\" rel=\"stylesheet\" type=\"text/css\" />");


  // Trying to get window.webappos from the parent window
  if (!window.webappos) {
    try {
      if (window.parent) {
        window.webappos = Object.create(window.parent["webappos"]);
        window.webappos.js_util = Object.create(window.parent["webappos"].js_util);
        window.webappos.parent_desktop = window.parent.webappos.parent_desktop || window.parent.webappos.in_desktop;
        window.webappos.in_desktop = false;
      }
    } catch (t) {
      // assume the parent is on another domain;
      // sending a message to check, whether the parent frame is a desktop frame...
      // if the parent or some of its ascendants indeed is a desktop, the parent will reply to us "i_am_desktop!"
      setTimeout(function () {
        window.parent.postMessage({
          protocol: "webappos_desktop",
          method: "are_you_desktop?",
          caller_id: webappos.caller_id
        }, "*");
      }, 0); // sending after webappos object is defined fully
    }
  }

  // If could not get from the parent, define window.webappos here...
  if (!window.webappos) {
    window.webappos_defined_here = true;

    /**
     * Property: window.webappos
     * 
     * The webappos object contains useful webAppOS-specific functions and fields.
     * The object is shared between all iframes loaded from the same domain; some functions and fields, however, are iframe-specific.
     **/

    window.webappos = {};

    webappos.webcalls = {};
    // TODO: get static webcalls

    if (!Date.now) {
      Date.now = function now() {
        return new Date().getTime();
      };
    };
    window.webappos.caller_id = Date.now();


    try {
      window.webappos.top_location = window.top.document.location;
    } catch (t) {
      window.webappos.top_location = window.location;
    }
  
    /**
     * Property: webappos.interrupt
     * 
     * A one-argument function for managing web calls and special submitter links in web memory.
     * If specified, the interrupt function is called each time before a web call is being invoked or a submitter link created.
     * The argument can be one of the following:
     * ---code---
     * {
     *   type: "webcall",
     *   isClient: true|false, // whether the web call is client-side or not
     *   actionName: "[web call action name]",
     *   argument: JSON|string|web memory object reference
     * }
     * ----------
     * or 
     * ---code---
     * {
     *   type: "submit",
     *   argument: [event or command object reference in web memory]
     * }
     * ----------
     * 
     * The function must return true, if the invocation has to be interrupted, or false otherwise (the webcall or event/command will be handled as usual by webAppOS).
     **/

    window.webappos.interrupt = null;
  }


  /** Group: web calls-related functions (iframe-specific) */

  window.webappos.client_webcall_jsoncall = async function (actionName, arg) {
    var action = webappos.webcalls[actionName];
    if (!action)
      return;

    var intr_obj = {
      type: "webcall",
      isClient: true,
      actionName: actionName,
      argument: arg,
      callingConventions: action.callingConventions
    };
  
    if ((webappos.interrupt)&& (webappos.interrupt(intr_obj))) {
      return "ERROR: web call interrupted";
    };


    var p = new Promise(async function (resolve, reject) {
      require([action.resolvedInstructionSet + "_webcalls_adapter.js"], function (adapter) {
        if (adapter.jsoncall) {
          resolve(adapter.jsoncall(action.resolvedLocation, arg));
        } else {
          console.log("ERROR: Webcalls adapter '" + action.resolvedInstructionSet + "' does not support jsoncall calling conventions.");
          reject("ERROR: Webcalls adapter '" + action.resolvedInstructionSet + "' does not support jsoncall calling conventions.");
        }
      });

    });

    return p;
  };

  window.webappos.client_webcall_webmemcall = function (actionName, obj) {
    var action = webappos.webcalls[actionName];
    if (!action)
      return;

      var intr_obj = {
        type: "webcall",
        isClient: true,
        actionName: actionName,        
        argument: obj&&obj.reference?obj.reference:0,
        callingConventions: action.callingConventions
      };
    
      if ((webappos.interrupt)&& (webappos.interrupt(intr_obj))) {
        return;
      };

      require([action.resolvedInstructionSet + "_webcalls_adapter.js"], function (adapter) {
      if (adapter.webmemcall) {
        adapter.webmemcall(action.resolvedLocation, obj);
      } else
        console.log("ERROR: Webcalls adapter '" + action.resolvedInstructionSet + "' does not support webmemcall calling conventions.");
    });
  };

  /**
   * Function: webappos.webcall_and_wait (may become deprecated)
   *   Executes the given web call synchronously via the webAppOS webcalls service.
   * Parameters:
   *   action - the name of the action to call (defined at the server side in *.webcalls files)
   *   arg - action argument; usually, a JSON object; sometimes - a string object; for webmemcall conventions: an integer representing the object reference
   * Returns:
   *   a parsed JSON object as JavaScript object; the returned object can contain the "error" attribute (containing an error message) to specify that an error occurred
   */
  window.webappos.webcall_and_wait = function (action, arg) {
    if (webappos.js_util.is_object(arg)) {
      try {
        arg = JSON.stringify(arg);
      } catch (t) {
        return null;
      }
    } else
    if (typeof arg == 'number')
      arg = arg + "";

    var a = webappos.webcalls[action];
    if (a && a.isClient) {
      return {
        "error": "Please, do not use webcall_and_wait for client web calls."
      };
    }

    var intr_obj = {
      type: "webcall",
      isClient: false,
      actionName: action,
      argument: arg
    };
    if (a)
      intr_obj.callingConventions = a.callingConventions;

    if ((webappos.interrupt)&& (webappos.interrupt(intr_obj))) {
      return {
            error: "web call interrupted"
      };
    }


    var xhr = new XMLHttpRequest();
    var q = "";
    if (webappos.project_id) {
      q = "?login=" + webappos.login + "&ws_token=" + webappos.ws_token + "&project_id=" + webappos.project_id;
    } else {
      if (webappos.login && webappos.ws_token)
        q = "?login=" + webappos.login + "&ws_token=" + webappos.ws_token;
    }
    xhr.open("POST", "/services/webcalls/" + action + q, false /*not async*/ );

    var retVal = null;
    xhr.onreadystatechange = function () {
      if (this.readyState == this.DONE) {
        var x = xhr.responseText.trim();
        try {
          var json = JSON.parse(x);
          if (json.error) {
            console.log(json.error);
            retVal = json;
          } else
            retVal = (json == "null") ? null : json;
        } catch (t) {
          retVal = (x == "null") ? null : {
            error: x.toString()
          };
        }
      }
    };

    if (typeof arg == "undefined") {
      if (a.callingConventions == "webmemcall")
        arg = 0;
      else
        arg = "";
    }

    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(arg);
    return retVal;
  };

  /**
   * Function: webappos.webcall (async)
   *   Executes the given web call asynchronously via the webAppOS webcalls service.
   * Parameters:
   *   action - the name of the action to call (defined at the server side in *.webcalls files)
   *   arg - action argument; usually, a JSON object; sometimes - a string object; for webmemcall conventions: an integer representing the object reference
   * Returns:
   *   a Promise which resolves in a parsed resulting JSON of the web call (with jsoncall calling conventions) as a JavaScript object;
   *   the promise is rejected if the returned object contains the "error" attribute, or if some other error occurs
   */
  window.webappos.webcall = async function (action, arg) {

    let promise = new Promise((resolve, reject) => {
      if (webappos.js_util.is_object(arg)) {
        try {
          arg = JSON.stringify(arg);
        } catch (t) {
          return reject(new Error("Invalid argument"));
        }
      }

      var a = webappos.webcalls[action];
      if (a && a.isClient) {
        // do it right away...
        if (a.callingConventions == "jsoncall") {
          webappos.client_webcall_jsoncall(action, arg).then(function (result) {
            resolve(result);
          }).catch(function (t) {
            resolve({
              error: t + ""
            });
          });
        } else
        if (a.callingConventions == "webmemcall") {
          if (typeof arg == 'number')
            webappos.client_webcall_webmemcall(action, tda.model[arg]);
          else
            webappos.client_webcall_webmemcall(action, arg);
          resolve({});
        }
        return; // return from promise body
      }


      var intr_obj = {
        type: "webcall",
        isClient: false,
        actionName: action,
        argument: arg
      };
      if (a)
        intr_obj.callingConventions = a.callingConventions;
  
      if ((webappos.interrupt)&& (webappos.interrupt(intr_obj))) {
        return resolve( {
              error: "web call interrupted"
        } );
      }
  
      if (typeof arg == 'number')
        arg = arg + "";

      var xhr = new XMLHttpRequest();
      var q = "";
      if (webappos.project_id) {
        q = "?login=" + webappos.login + "&ws_token=" + webappos.ws_token + "&project_id=" + webappos.project_id;
      } else {
        if (webappos.login && webappos.ws_token)
          q = "?login=" + webappos.login + "&ws_token=" + webappos.ws_token;
      }
      if (webappos.app_url_name) {
        if (q=="")
          q = "?app_url_name="+webappos.app_url_name;
        else
          q += "&app_url_name="+webappos.app_url_name;
      }
      xhr.open("POST", "/services/webcalls/" + action + q, true /*async*/ );

      var retVal = null;
      xhr.onreadystatechange = function () {
        if (this.readyState == this.DONE) {
          var json = null;
          try {
            var x = xhr.responseText.trim();
            json = JSON.parse(x);
          } catch (t) {
            console.log(t);
            console.log("action was "+action);
            json = (x == "null") ? null : x.toString();
          }
          if (json && (webappos.js_util.is_object(json)) && json.error) {
            console.log(json.error);
            return reject(new Error(json.error));
          } else {
            return resolve(json);
          }
        }
      };

      xhr.setRequestHeader('Content-Type', 'application/json');

      if (typeof arg == "undefined") {
        if (a && a.callingConventions == "webmemcall")
          arg = 0;
        else
          arg = "";
      }

      xhr.send(arg);
    });

    return promise;
  };

  if (window.webappos_defined_here) {
    /** Group: Desktop API */

    /**
     * Variable: webappos.desktop
     * 
     * Provides access to Desktop API. Either passes Desktop API calls
     * to the parent window (if the parent provides Desktop API), or implements by its own.
     * In the latter case, the default implementation is tailored for single-page
     * webAppOS applications that do not use the desktop. However, webAppOS desktop applications (like the shipped default Desktop app) should redefine
     * functions from Desktop API marked as [DESKTOP-SPECIFIC].
     */
    window.webappos.desktop = {};

    /**
     * Function: webappos.desktop.launch_in_desktop [DESKTOP-SPECIFIC]
     * Asks the desktop to open the given URL as desktop window associated with the given app.
     * 
     * The default (non-desktop) implementation just opens the given URL in a new tab.
     * 
     * Parameters:
     * 
     * url - the URL to open in a desktop window (the URL usually starts with "/apps/<app-url-name>")
     * app_url_name - the lowest-case name of the webAppOS app used within the URL; the desktop can visualize that this app is active (e.g., by adding the icon to
     *                the taskbar/dock); the desktop can store the last coordinates and dimensions of the last open app window for restoring them
     *                when the same app is being opened again;
     * 
     *                app_url_name can be null; in this case, the desktop should not visualize any app (the page at the passed URL will be displayed like system window);
     * 
     * Returns:
     *  nothing (possible async execution)
     */
    webappos.desktop.launch_in_desktop = function (url, app_url_name) {
      if (webappos.parent_desktop)
        window.parent.postMessage({
          protocol: "webappos_desktop",
          method: "launch_in_desktop",
          url: url,
          app_url_name: app_url_name,
          caller_id: webappos.caller_id
        }, "*");
      else
        window.open(url, "_blank"); // no desktop present - open in full screen
    };


    /**
     * Function: webappos.desktop.open_path
     * Opens the given path by launching the corresponding webAppOS app and passing the path as argument. 
     * For webAppOS project_ids launches the corresponding associated app.
     * For directories and unidentified apps, launches FileBrowser app.
     * 
     * Parameters:
     * path - the path (project_id or folder path) to open; the path must be in format "login/path/relative/to/user's/home"
     * full_screen - a boolean flag denoting whether to open in a new tab, not inside the desktop app;
     *               default (non-desktop) implementation always assumes full_screen (new tab) mode;
     *               if the desktop is present, relies on webappos.desktop.launch_in_desktop
     * 
     * Returns:
     *  nothing (possible async execution)
     */
    webappos.desktop.open_path = function (path, full_screen) {
      if (!path)
        return;
      var url = null;
      var i = path.lastIndexOf('.');
      var app_url_name = null;

      var is_dir = window.webappos.webcall_and_wait("webappos.isDirectory", path);
      if (!is_dir)
        is_dir = {
          result: false
        };

      if ((i >= 0) && (!is_dir.result)) {
        var ext = path.substring(i + 1);
        var arr = window.webappos.webcall_and_wait("webappos.getAssociatedAppsByExtension", ext);
        if (arr && arr.length > 0)
          app_url_name = arr[0].urlName; // TODO: choose among multiple apps
        if (app_url_name) {
          // open with app...
          url = "/apps/" + app_url_name +
            "?project_id=" + path;
          // login & ws_token will be obtained from localStorage;
          // alternatively, login and ws_token can be specified in URL query string (e.g., for implementing "run as")
        }
      }

      if (url == null) {
        // TODO: find associated app or offer to open with some app,
        // but pass the arg not via project_id, but somehow else (e.g., via arg=)

        // Currently: opening with filebrowser
        i = path.indexOf("/");
        url = "/apps/filebrowser?path=/home" + path.substring(i); // replace "<login>" with "/home"
        app_url_name = "filebrowser";
      }

      if (url) {
        if (full_screen)
          window.open(url, "_blank");
        else
          webappos.desktop.launch_in_desktop(url, app_url_name);
      }

    };

    /**
     * Function: webappos.desktop.launch_app
     * Launches the given webAppOS app.
     * 
     * Parameters:
     * app_url_name - the lower-case URL app name of the app to launch
     * full_screen - a boolean flag denoting whether to open in a new tab, not inside the desktop app;
     *               default (non-desktop) implementation always assumes full_screen (new tab) mode;
     *               if the desktop is present, relies on webappos.desktop.launch_in_desktop
     * 
     * Returns:
     *  nothing
     */
    webappos.desktop.launch_app = function (app_url_name, full_screen) {
      if (!app_url_name)
        return;

      var url = "/apps/" + app_url_name;
      // login & ws_token will be obtained from localStorage;
      // alternatively, login and ws_token can be specified in URL query string (e.g., for implementing "run as")

      if (full_screen)
        window.open(url, "_blank");
      else
        webappos.desktop.launch_in_desktop(url, app_url_name);
    };

    webappos.desktop.callback_map = {};
    webappos.desktop.callback_id_counter = 0;
    webappos.desktop.shared_values = {};

    /**
     * Function: webappos.desktop.set_shared_value
     * 
     * Sets some desktop-wide property that can be used for inter-app communication.
     * 
     * Parameters:
     * key - a string representing the name of the value
     * value - the value
     * 
     * Returns:
     *   nothing (possible async execution)
     */
    webappos.desktop.set_shared_value = function (key, value) {
      if (webappos.parent_desktop) {
        window.parent.postMessage({
          protocol: "webappos_desktop",
          method: "set_shared_value",
          key: key,
          value: value,
          caller_id: webappos.caller_id
        }, "*");
      } else {
        webappos.desktop.shared_values[key] = value;
      }
    };

    /**
     * Function: webappos.desktop.get_shared_value (async)
     * 
     * Returns the desired desktop-wide property.
     * 
     * Parameters:
     *   key - a string representing the name of the value
     * 
     * Returns:
     *   a Promise which resolves in the requested value
     */
    webappos.desktop.get_shared_value = async function (key) {
      let promise = new Promise((resolve, reject) => {
        if (webappos.parent_desktop) {
          window.webappos.desktop.callback_id_counter++;
          window.webappos.desktop.callback_map[window.webappos.desktop.callback_id_counter] = function (retVal) {
            resolve(retVal);
          };
          window.parent.postMessage({
            protocol: "webappos_desktop",
            method: "get_shared_value",
            key: key,
            callback_id: window.webappos.desktop.callback_id_counter,
            caller_id: webappos.caller_id
          }, "*");
        } else {
          resolve(webappos.desktop.shared_values[key]);
        }
      });
      return promise;
    };

    require(["dijit/Dialog", "dojox/widget/DialogSimple", "dojo/domReady!"], function (Dialog, DialogSimple) {

      window.webappos.desktop.dialog_counter = 0;
      if (!window.webappos.desktop.show_dialog) {
        /**
         * Function: webappos.desktop.show_dialog [DESKTOP-SPECIFIC]
         * Asks the desktop to show a dialog window within a desktop.
         * 
         * The default (non-desktop) implementation just opens a simple dojo dialog.
         * 
         * Parameters:
         * 
         * title - the window title
         * content - the innerHTML content of the dialog
         * fOnClose - a function to call, when the user tries to close the dialog by pressing the "x" button;
         *            if not specified, defaults to the function which just closes the dialog
         * w - desired width (optional)
         * h - desired height (optional)
         * 
         * Returns:
         *   a handle (a number or an object) that can be passed to webappos.desktop.close_dialog;
         *   the dialog itself can be displayed later asynchronously
         */
        window.webappos.desktop.show_dialog = function (title, content, fOnClose, w, h) {

          var ww=w;
          var hh=h;
          if (!ww)
            ww = window.innerWidth / 2;
          if (!hh)
            hh = window.innerHeight / 2;
          if (ww < 600)
            ww = 600;
          if (hh < 400)
            hh = 400;

          if (window.webappos.parent_desktop) {
            window.webappos.desktop.dialog_counter++;
            if (fOnClose) {
              window.webappos.desktop.callback_id_counter++;
              window.webappos.desktop.callback_map[window.webappos.desktop.callback_id_counter] = function (data) {
                fOnClose();
              };
            }

            window.parent.postMessage({
              handle: window.webappos.desktop.dialog_counter,
              protocol: "webappos_desktop",
              method: "show_dialog",
              title: title,
              content: content,
              width: ww,
              height: hh,
              handle: window.webappos.desktop.dialog_counter,
              caller_id: webappos.caller_id,
              callback_id: fOnClose ? webappos.desktop.callback_id_counter : null
            }, "*");
            return window.webappos.desktop.dialog_counter;
          } else {
            myDialog = new DialogSimple({
              title: title,
              content: content,
              executeScripts: true,
              //style: "width: " + w + "; height:" + h + ";",
              style: "width: "+(w?w+"":"auto")+"; height: "+(h?hh+"":"auto")+";",
              onHide: function () {
                if (fOnClose) fOnClose();
                myDialog.destroy();
              }
            });
            myDialog.show();
            return myDialog;
          }
        };
      }

      if (!window.webappos.desktop.close_dialog) {
        /**
         * Function: webappos.desktop.close_dialog [DESKTOP-SPECIFIC]
         * Asks the desktop to close the previously open dialog window.
         * 
         * The default (non-desktop) implementation just destroys the dojo dialog.
         * 
         * Parameters:
         * 
         * handle - a dialog handle returned by webappos.desktop.show_dialog
         * 
         * Returns:
         *   nothing (possible async execution)
         */
        window.webappos.desktop.close_dialog = function (handle) {
          if (window.webappos.parent_desktop)
            window.parent.postMessage({
              protocol: "webappos_desktop",
              method: "close_dialog",
              handle: handle,
              caller_id: webappos.caller_id
            }, "*");
          else {
            handle.hide();
            handle.destroy();
          }
        };
      }
    });


    /**
     * Function: webappos.desktop.browse_for_file (async)
     * 
     *   Shows the browse for file dialog. Relies on webappos.desktop.show_dialog, webappos.desktop.close_dialog, webappos.desktop.set_shared_value, and webappos.desktop.get_shared_value.
     * 
     * Parameters:
     *   dialog_type - one of "open", "save", "upload", "dir"
     *   files_filter - a string containing descriptions of file filters to look for;
     *     each description is some text followed by file masks in parentheses;
     *     filters are delimited by \n, extensions are delimited by ",";
     *     Example:  Word document (*.doc,*.rtf), C++ file (*.cpp,*.h,*.hpp)
     * Returns:
     *   a Promise which resolves in a string containing the chosen file name relative to the current login's home folder or null, if no file was chosen;
     */
    webappos.desktop.browse_for_file = async function (dialog_type, files_filter) {
      var d = new Date();
      var browse_id = d.getTime();
      var content = "<iframe id='theiframe' style='width:700px;height:465px;' src='/apps/filedialog/FileDialog.html?type=" + dialog_type + "&filter=" + files_filter + "&browse_id=" + browse_id + "'></iframe>" +
        "<script>" +
        "var receiveMessage2 = function(event) {" +
        "  if (event.data.browse_id!=" + browse_id + ") return;" +
        "  webappos.desktop.set_shared_value('file_dialog_result" + browse_id + "', event.data.result);" +
        "" +
        "};" +
        "window.addEventListener(\"message\", receiveMessage2, true);" +
        "</script>";

      var title = "";
      if (dialog_type == "open")
        title = "Open file";
      else
      if (dialog_type == "save")
        title = "Save file";
      else
      if (dialog_type == "upload")
        title = "Upload file";
      else
      if (dialog_type == "dir")
        title = "Choose folder";

      var handle = webappos.desktop.show_dialog(title, content, function () {
        webappos.desktop.set_shared_value('file_dialog_result' + browse_id, "");
      });//, 700, 500);

      webappos.desktop.set_shared_value('file_dialog_result' + browse_id, null);

      let promise = new Promise(async function (resolve, reject) {

        for (;;) {
          let value = await webappos.desktop.get_shared_value('file_dialog_result' + browse_id);
          if (value != null) {
            webappos.desktop.close_dialog(handle);
            if (value == "") {
              return resolve(null);
            } else {
              if (webappos.js_util.starts_with(value, "/home/"))
                return resolve(value.substring(6));
              else
                return resolve(value);
            }
          }
          await webappos.js_util.wait(1000);
        }

      });

      return promise;
    };

    webappos.desktop.last_launcherDialog = null;
    /**
     * Function: webappos.desktop.show_launcher
     * 
     *   Shows (or hides, if shown) the launcher menu containing the list of available apps.
     * 
     * Parameters:
     *   around_id  - the ID of the HTML DOM element, around which the launcher menu has to be shown
     * Returns:
     *   nothing (possible async execution)
     */
    webappos.desktop.show_launcher = function (around_id) {
      require(["dojo", "dojo/dom", "dijit/popup", "dijit/DialogUnderlay", "dijit/form/DropDownButton", "dijit/TooltipDialog"],
        function (dojo, dom, popup, DialogUnderlay, DropDownButton, TooltipDialog) {
          var launcherDialog = window.webappos.desktop.last_launcherDialog;
          if (!launcherDialog) {
            var apps = window.webappos.webcall_and_wait("webappos.getAvailableApps", "");
            if (apps.error) {
              console.log("webappos.getAvailableApps error: " + apps.error);
              return;
            }

            var row_size = 1;
            while ((row_size < 8) && (row_size * row_size < apps.length * 1.6))
              row_size++;
            var content = "";
            for (var i = 0; i < apps.length; i++) {
              content += "<div onclick=\"javascript:webappos.desktop.launch_app('" + apps[i].urlName + "');\" style='display:inline-block; align:center; width:80px; height:100px; cursor:pointer; margin:10px;'>" +
                "<div style='display:inline-block;width:80px;height:80px;'><img src='" + apps[i].iconURL + "' width='80px' height='auto'></img></div>" +
                "<p style='padding:0; margin:0;' align='center'>" + apps[i].displayedName + "</p>" +
                "</div>";
            }

            launcherDialog = new TooltipDialog({
              style: "width: " + (100 * row_size + 30) + "px; margin-right:0px;",
              content: content,
              onClick: function () {
                if (dijit._underlay)
                  dijit._underlay.destroy();
                delete dijit._underlay;
                popup.close(launcherDialog);
                launcherDialog.destroy();
                window.webappos.desktop.last_launcherDialog = null;
              }
            });

            var dij = null;
            if (dijit._underlay) {
              dijit._underlay.destroy();
            }
            dijit._underlay = new DialogUnderlay({
              style: "opacity:0.1;"
            });
            dijit._underlay.show();
            dij = dijit._underlay.domNode;
            dojo.connect(dij, "onclick", function (e) {
              if (dijit._underlay)
                dijit._underlay.destroy();
              delete dijit._underlay;
              popup.close(launcherDialog);
              if (launcherDialog)
                launcherDialog.destroy();
              window.webappos.desktop.last_launcherDialog = null;
            });

            window.webappos.desktop.last_launcherDialog = launcherDialog;
            popup.open({
              popup: launcherDialog,
              around: dom.byId(around_id)
            });
          } else {
            popup.close(window.webappos.desktop.last_launcherDialog);
            window.webappos.desktop.last_launcherDialog.destroy();
            window.webappos.desktop.last_launcherDialog = null;
          }
        } // function
      ); // require
    };


    /**
     * Function: webappos.desktop.show_user_menu
     * 
     *   Shows (or hides, if shown) the user menu containing the "Sign out" button.
     * 
     * Parameters:
     *   around_id  - the ID of the HTML DOM element, around which the user menu has to be shown
     * Returns:
     *   nothing (possible async execution)
     */
    webappos.desktop.show_user_menu = function (around_id) {
      require(["dojo", "dojo/dom", "dijit/popup", "dijit/form/DropDownButton", "dijit/TooltipDialog"],
        function (dojo, dom, popup, DropDownButton, TooltipDialog) {
          var launcherDialog = window.webappos.desktop.last_launcherDialog;
          if (!launcherDialog) {

            var content = "<div onclick='window.webappos.sign_out();' style='height:30px;display:inline-block;vertical-align: middle;cursor:pointer;'><img src='/apps/" + webappos.app_url_name + "/icons/sign_out.png' width='30px' height='auto'></img> <span style='height:38px;display:inline-block;vertical-align: middle;'>Sign out</span></div>";

            launcherDialog = new TooltipDialog({
              //               style: "width: 300px",
              content: content,
              onClick: function () {
                if (dijit._underlay)
                  dijit._underlay.destroy();
                delete dijit._underlay;
                popup.close(launcherDialog);
                launcherDialog.destroy();
                window.webappos.desktop.last_launcherDialog = null;
              }
            });

            var dij = null;
            if (dijit._underlay) {
              dijit._underlay.destroy();
            }
            dijit._underlay = new dijit.DialogUnderlay({
              style: "opacity:0.1;"
            });
            dijit._underlay.show();
            dij = dijit._underlay.domNode;
            dojo.connect(dij, "onclick", function (e) {
              if (dijit._underlay)
                dijit._underlay.destroy();
              delete dijit._underlay;
              popup.close(launcherDialog);
              if (launcherDialog)
                launcherDialog.destroy();
              window.webappos.desktop.last_launcherDialog = null;
            });

            window.webappos.desktop.last_launcherDialog = launcherDialog;
            popup.open({
              popup: launcherDialog,
              around: dom.byId(around_id)
            });
          } else {
            popup.close(window.webappos.desktop.last_launcherDialog);
            window.webappos.desktop.last_launcherDialog.destroy();
            window.webappos.desktop.last_launcherDialog = null;
          }
        } // function
      ); // require
    };

    /**
     * Function: webappos.close_current_project
     * 
     *   Closes the current project, but keeps the user signed in.
     * 
     * Returns:
     *   nothing; redirects to another page
     */
    webappos.close_current_project = function () {
      if (webappos.app_url_name)
        window.location.href = "/apps/" + webappos.app_url_name;
      else
        window.location.href = "/apps/filebrowser";
    };

  }; // if (window.webappos_defined_here)...

  /**
   * Function: webappos.set_project_id (iframe-specific)
   * 
   *   Updates/changes the current project_id. If TDA Environment Engine is used, changes the displayed project name.
   * 
   * Returns:
   *   nothing; redirects to another page
   */
  window.webappos.set_project_id = function (new_project_id) {
    if (webappos.project_id == new_project_id)
      return;

    webappos.project_id = new_project_id;
    if (window.tda && tda.ee && tda.ee.updateProjectId)
      tda.ee.updateProjectId(webappos.project_id);
    webappos.js_util.update_query_value("project_id", new_project_id);
  };



  /** Group: webAppOS-scopes related functions (iframe-specific) */

  webappos.server_mode = 0; // 0=unknown (no scopes requested); +1=server mode; -1=serverless mode
  webappos.set_server_mode = function (mode) {
    webappos.server_mode = mode;
  };
  webappos.drivers_set = {};
  webappos.drivers_stack = [];

  /**
    Function: webappos.request_scopes (async)
  
    Authenticates the user to use one or more particular scopes via the given scopes driver.
  
    For "webappos_scopes" only the "login" and "project_id" ("project_id" is a superset of "login") are currently supported.
    If the user is not authenticated, webappos_scopes driver redirects the user to the login page
    (if a web application is being run as a standalone desktop application, the
    authentication is automatic and does not require user intervention).
    The authentication info is then stored in browser's localStorage keys "login" and "ws_token"
    as well as in as properties webappos.login and webappos.ws_token.
    For the "project_id" scope, the webappos.project_id property is set and web memory is being initialized by connecting to the
    corresponding server-side MRAM slot.
  
    Parameters:
      driver_name - the name of the scopes driver (e.g., "webappos_scopes" or "google_scopes") that is able to authenticate the requested scopes;
                    the driver must be accessible as scope service (or static .js files for serverless access)
                    at /services/<driver-name>/;
      scopes - a space-separated list of desired scopes (specific to scope provider)
  
    Example:
      ---Code---
      webappos.request_scopes("webappos_scopes", "login").then( ()=>
        webappos.request_scopes("google_scopes", "https://www.googleapis.com/auth/spreadsheets");
      );
      ----------
  
    Returns:
      A Promise instance.
  */

  webappos.request_scopes = async function (driver_name, scopes) {
    if (driver_name == "webappos_scopes") {
      if (webappos.server_mode == -1) {
        throw new Error("\"webappos_scopes\" must be the first scope to request!");
      }
      webappos.set_server_mode(+1);
    } else
      webappos.set_server_mode(-1);
    return new Promise((resolve, reject) => {
      require(["/services/" + driver_name + "/" + driver_name + "_driver.js"], function (driver) {
        if (!webappos.drivers_set[driver_name]) {
          webappos.drivers_set[driver_name] = driver;
          webappos.drivers_stack.push(driver);
        }

        webappos.webcall("webappos.getAvailableWebCalls").then(function (result) {
          webappos.webcalls = result;
          driver.request_access(scopes, (webappos.server_mode == -1)).then(() => resolve(true)).catch((e) => reject(e));
        });
      });
    });
  };


  /**
   * 	Function: webappos.get_scopes_driver
   * 
   *  Returns the previously loaded scopes driver.
   *  Can be useful for scopes drivers themselves.
   * 
   *  Parameters:
   *    driver_name - the driver name (as passed to webappos.request_scopes)
   * 
   *  Returns:
   *    the previously loaded scopes driver (or undefined).
   */
  webappos.get_scopes_driver = function (driver_name) {
    return webappos.drivers_set[driver_name];
  };

  /**
   * 	Function: webappos.sign_out
   * 
   *  Calls revoke_serverless_access() for all loaded scopes drivers, including "webappos_scopes", if it was loaded (thus, releasing the webAppOS "login" scope and redirecting to the login page).
   *  The sign_out function DOES NOT release server-side scope tokens. This is done intentionally: tokens remain stored at the server side for future access (e.g., for re-mounting cloud drives on the next logon automatically).
   *  An exception is ws_token from the webAppOS "login" scope - it is revoked from the server as well.
   * 
   *  Returns:
   *    true (if not redirected to the login page)
   */
  window.webappos.sign_out = async function () {
    while (webappos.drivers_stack.length) {
      let driver = webappos.drivers_stack.pop();
      await driver.revoke_serverless_access();
    }

    webappos.set_server_mode(0);

    // executing "webappos.drivers_set = []" on the last drivers_set in the prototype chain:
    var props = Object.keys(drivers_set);
    for (var i = 0; i < props.length; i++) {
      delete drivers_set[props[i]];
    }
    return true;
  };

  if (window.webappos_defined_here) {

    ///// parent desktop <-> this child communication via HTML5 messages /////
    function receiveMessage(event) {
      if (webappos.in_desktop)
        return; // desktops must implement in their own way

      //console.log("RECEIVE MSG " + webappos.in_desktop + " webappos.js @" + webappos.caller_id, event);
      if (event.source == window.parent) {
        if (event.data.protocol == "webappos_desktop") {
          if (event.data.method == "i_am_desktop!") {
            window.webappos.parent_desktop = true;
          } else
          if (event.data.method == "callback") {
            var f = window.webappos.desktop.callback_map[event.data.callback_id];
            delete window.webappos.desktop.callback_map[event.data.callback_id];
            if (f)
              f(event.data.value);
          }
        }
      } else {
        // answer as if we are desktop, if our parent is desktop...
        if (event.data.protocol == "webappos_desktop") {
          if (event.data.method == "are_you_desktop?") {
            if (window.webappos.parent_desktop)
              event.source.postMessage({
                protocol: "webappos_desktop",
                method: "i_am_desktop!",
                caller_id: webappos.caller_id
              }, event.origin);
          } else
          if (window.webappos.parent_desktop) {
            if (event.data.callback_id) { // we act as relay iframe; store the callback function
              window.webappos.desktop.callback_map[event.data.callback_id] = function (data) {
                event.source.postMessage(event.data, event.origin);
              };
            }

            window.parent.postMessage(event.data, "*");
          }
        }
      }
    }
    window.addEventListener("message", receiveMessage, false);


    ///// webappos.js_util functions /////
    /** Group: JavaScript utility functions */

    webappos.js_util = {};

    /**
     * Function: webappos.js_util.wait (async)
     *   Waits the given mount of milliseconds.
     * 
     * Parameters:
     *   ms - how many milliseconds to wait
     * 
     * Returns:
     *   a Promise that resovles in ms milliseconds
     */
    webappos.js_util.wait = async function (ms) {
      return new Promise((accept, reject) => setTimeout(accept, ms));
    }

    /**
    Function: webappos.js_util.load_script (async)
  
    Loads the given script (asynchronously)
  
    Parameters:
      url - script url
  
    Returns:
      A Promise, which either resolves to true, or rejects with an error message.
    */
    webappos.js_util.load_script = async function (url) {
      let promise = new Promise((resolve, reject) => {
        var e = document.createElement('script');
        e.setAttribute('src', url);
        e.resolve = resolve;
        e.reject = reject;
        e.setAttribute("onload", "this.resolve(true);");
        e.setAttribute("onerror", "this.reject('Script '" + url + " could not be loaded.');");
        document.head.appendChild(e);
      });

      return promise;
    };

    /**
    Function: webappos.js_util.exec_script (async)
  
    Loads and executes the given script.
  
    Parameters:
      url - script url
  
    Returns:
      A Promise, which either resolves to true, or rejects with an error message.
    */
    webappos.js_util.exec_script = async function (url) {
      let promise = new Promise((resolve, reject) => {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", url, true);

        var retVal = null;
        xhr.onreadystatechange = function () {
          if (this.readyState == this.DONE) {
            if (xhr.status == 200) {
              try {
                eval(xhr.responseText);
                resolve(true);
              } catch (t) {
                resolve(false);
              }
            } else
              resolve(false);
          }
        };

        xhr.send();
      });

      return promise;
    };

    /**
     Function: webappos.js_util.show_failure
   
     Displays a failure error box.
   
     Parameters:
       message - an error message to display
     Returns:
        nothing (possible async execution)
     */
    webappos.js_util.show_failure = function (message) {
      if (typeof require == 'undefined') {
        alert(message + "\nYou can try to reload this page.");
      } else
        require(["dijit/Dialog", "dojo/domReady!"], function (Dialog) {
          myDialog = new Dialog({
            title: "Attention!",
            content: message + "<br>" +
              "<input type='button' value='Refresh' onclick='location.reload()'></input>&nbsp;",
            style: "width: 400px"
          });
          myDialog.show();
        });
    };

    /**
    Function: webappos.js_util.find_element_by_class_name
  
    Returns the first HTML element having the given class and the given substring within element's innerHTML.
  
    Parameters:
      className - the CSS class name of the HTML element to find
      htmlSubStr - (optional) substring within innerHTML
    Returns:
       the DOM element, or null, if not found
    */
    webappos.js_util.find_element_by_class_name = function (className, htmlSubStr) {
      var els = document.getElementsByClassName(className);
      if (!els)
        return null;
      var i;
      for (i = 0; i < els.length; i++) {
        if (!htmlSubStr)
          break;
        if ((els[i]) && (els[i].innerHTML) && (els[i].innerHTML.indexOf(htmlSubStr) >= 0))
          break;
      }
      if (i >= els.length)
        return null;
      else
        return els[i];
    };

    /**
    Function: webappos.js_util.await_condition
  
    Timeouts until the given condition function returns true.
  
    Parameters:
      fCondition - a function for checking the condition
    Returns:
      Promise, which resolves when fCondition() returns true
    */
    webappos.js_util.await_condition = async function (fCondition) {
      let f = function (resolve, reject) {
        (resolve, reject) => {
          if (!fCondition) {
            return reject('Invalid condition for webappos.js_util.await_condition');
          }
          var val = fCondition();
          if (val)
            return resolve(val);
          else {
            setTimeout(f, 10, resolve, reject);
            return;
          }
        }
      };
      return new Promise(f);
    };

    /**
    Function: webappos.js_util.fire_event
  
    Emits the given event on the given element.
  
    Parameters:
      element - the DOM element, for which to fire an event
      event_type - a string denoting the event type (e.g., "click")
    Returns:
       true, if all was OK, or false if the event was cancelled (by calling preventDefault)
    */
    webappos.js_util.fire_event = function (element, event_type) {
      if (document.createEvent) {
        // dispatch for firefox + others
        var evt = document.createEvent('HTMLEvents');
        evt.initEvent(event_type, true, true); // event type,bubbling,cancelable
        return !element.dispatchEvent(evt);
      } else {
        // dispatch for IE
        var evt = document.createEventObject();
        return element.fireEvent('on' + event_type, evt)
      }
    };

    /**
    Function: webappos.js_util.print_stack_trace
  
    Prints the current stack trace via console.log
  
    Returns:
      nothing
    */
    webappos.js_util.print_stack_trace = function () {
      var e = new Error('dummy');
      var stack = e.stack.replace(/^[^\(]+?[\n$]/gm, '')
        .replace(/^\s+at\s+/gm, '')
        .replace(/^Object.<anonymous>\s*\(/gm, '{anonymous}()@')
        .split('\n');
      console.log(stack);
    };

    /**
    Function: webappos.js_util.clone_object
  
    Calls the object constructor to create a clone of the given object and copies own properties (non-recursively).
  
    Parameters:
      obj - object to clone
    Returns:
      a cloned object of the same type
    */
    webappos.js_util.clone_object = function (obj) {
      if (null == obj || "object" != typeof obj) return obj;
      var copy = obj.constructor();
      for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
      }
      return copy;
    }

    /**
    Function: webappos.js_util.clone_object_properties
  
    Creates a new object {} and copies own properties of the given object (non-recursively).
  
    Parameters:
      obj - object, from which to get the properties
    Returns:
      an object with the same properties and values
    */
    webappos.js_util.clone_object_properties = function (obj) {
      if (null == obj || "object" != typeof obj) return obj;
      var copy = {}; //obj.constructor();
      for (var attr in obj) {
        if (obj.hasOwnProperty(attr) && (!webappos.js_util.is_function(obj[attr]))) copy[attr] = obj[attr];
      }
      return copy;
    }

    /**
    Function: webappos.js_util.slice
  
    Returns a sub-array of the given array. Correctly works with Float64Arrays.
  
    Parameters:
      arr - array, from which to extract a sub-array
      i - (optional) the first index; default=0; if negative, assume index arr.length+i
      j - (optional) the next-to-last index; default=arr.length; if negative, assume index arr.length+j
    Returns:
      an object with the same properties and values
    */
    webappos.js_util.slice = function (arr, i, j) {
      if (!arr)
        return [];
      /*  if (arr.slice)
          return arr.slice(i, j);*/
      if (!i)
        i = 0;
      if (i < 0)
        i = arr.length + i;
      if (!j)
        j = arr.length;
      if (j < 0)
        j = arr.length + j;

      var retVal = [];
      if (arr instanceof Float64Array) {
        retVal = new Float64Array(j - i);
        for (var k = i; k < j; k++)
          retVal[k - i] = arr[k];
      } else {
        for (var k = i; k < j; k++)
          retVal.push(arr[k]);
      }

      return retVal;
    };

    /**
    Function: webappos.js_util.inerit
  
    Implementing the inheritance relation via JavaScript prototypes.
  
    Parameters:
      proto - the super-class object (prototype)
    Returns:
      a new object, which acts a a subclass of the given proto
    */
    webappos.js_util.inherit = function (proto) {
      function F() {};
      F.prototype = proto;
      var object = new F;
      return object;
    };

    /**
    Function: webappos.js_util.is_true_object
  
    Checks whether the given JS variable is of type "object" AND not null AND not array.
  
    Parameters:
      item - the JS variable to check
    Returns:
      whether item is of type "object" AND not null AND not array
    */
    webappos.js_util.is_true_object = function (item) {
      return (typeof item === "object" && !Array.isArray(item) && item !== null);
    }

    /**
    Function: webappos.js_util.is_object
  
    Checks whether the given JS variable is an object or an array.
  
    Parameters:
      item - the JS variable to check
    Returns:
      whether item is of type "object" or "object smth."
    */
    webappos.js_util.is_object = function (item) {
      var s = typeof item;
      return (s.substr(0, 6) == "object");
    }

    /**
      Function: webappos.js_util.is_array
    
      Checks whether the given JS variable is an array.
    
      Parameters:
        obj - the JS variable to check
      Returns:
        whether obj is an array
      */
    webappos.js_util.is_array = function (obj) {
      return obj && Object.prototype.toString.call(obj) === "[object Array]";
    };

    /**
    Function: webappos.js_util.is_function
  
    Checks whether the given JS variable is a JS function.
  
    Parameters:
      functionToCheck - the JS variable to check
    Returns:
      whether functionToCheck is a JS function
    */
    webappos.js_util.is_function = function (functionToCheck) {
      return functionToCheck && Object.prototype.toString.call(functionToCheck) === '[object Function]';
    };

    /**
    Function: webappos.js_util.capitalize_first_letter
  
    Given a string, returns the same string with the first letter capitalized.
  
    Parameters:
      string - the string to process
    Returns:
      the same string with the first letter capitalized.
    */
    webappos.js_util.capitalize_first_letter = function (string) {
      return string.charAt(0).toUpperCase() + string.substring(1);
    };

    /**
    Function: webappos.js_util.starts_with
  
    Checks whether the given string s starts with the given prefix.
  
    Parameters:
      s - the string
      prefix - the prefix to check
    Returns:
      whether the given string s starts with the given prefix
    */
    webappos.js_util.starts_with = function (s, prefix) {
      if (!s)
        return false;
      if (s.length < prefix.length)
        return false;
      else
        return s.substr(0, prefix.length) == prefix;
    };

    /**
     * Function webappos.js_util.timestamp
     * 
     * Returns a number representing the current timestamp.
     * 
     * Returns:
     *   a number representing the current timestamp
     */
    webappos.js_util.timestamp = function () {
      var d = new Date();
      return d.getTime();
    };

    /**
    Function: webappos.js_util.find_element_by_tag_name
  
    Returns the first HTML element with the given tag name and the given substring within element's innerHTML.
  
    Parameters:
      tagName - HTML tag name
      htmlSubStr - (optional) substring within innerHTML
    Returns:
      the DOM element, or null, if not found
    */
    webappos.js_util.find_element_by_tag_name = function (tagName, htmlSubStr) {
      var els = document.getElementsByTagName(tagName);
      if (!els)
        return null;
      var i;
      for (i = 0; i < els.length; i++) {
        if (!htmlSubStr)
          break;
        if ((els[i]) && (els[i].innerHTML) && (els[i].innerHTML.indexOf(htmlSubStr) >= 0))
          break;
      }
      if (i >= els.length)
        return null;
      else
        return els[i];
    };


    /**
    Function: webappos.js_util.show_please_wait
  
    Shows a modal "Please, wait" window with the given message.
  
    Parameters:
      msg - the message to display
    Returns:
      nothing; the window wrap becomes visible
    */
   webappos.js_util.show_please_wait = function (msg) {
      thePleaseWaitDiv.innerHTML = msg;
      thePleaseWaitDivWrap.style.display = "block";
      if (!msg || (msg == ""))
        thePleaseWaitDiv.style.display = "none";
      else {
        thePleaseWaitDiv.style.display = "block";
        thePleaseWaitDiv.style.left = (thePleaseWaitDivWrap.clientWidth - thePleaseWaitDiv.clientWidth) / 2 + "px";
        thePleaseWaitDiv.style.top = (thePleaseWaitDivWrap.clientHeight - thePleaseWaitDiv.clientHeight) / 2 + "px";
      }

      thePleaseWaitDiv.offsetHeight;
      thePleaseWaitDivWrap.offsetHeight;
    };

    /**
    Function: webappos.js_util.hide_please_wait
  
    Hides the "Please, wait" window shown earlier
  
    Returns:
      nothing; the window wrap becomes invisible
    */
   webappos.js_util.hide_please_wait = function () {
      thePleaseWaitDivWrap.style.display = "none";
      thePleaseWaitDiv.style.display = "none";
    };



  }; // if (window.webappos_defined_here)

  (function () {
    require(["dojo/domReady!"], function () {
      if (window.thePleaseWaitDivWrap)
        return; // already exists
      // elements for the "please wait" window
      let wrap = document.createElement("div");
      wrap.id = "thePleaseWaitDivWrap";
      wrap.style = "z-index:9998; opacity:0.2; position:absolute; left:0; top:0; right:0; bottom:0; background-color:#cccccc; display:none;";
      let pwdiv = document.createElement("div");
      pwdiv.id = "thePleaseWaitDiv";
      pwdiv.style = "z-index:9999; position:absolute; left:200px; top:100px; background-color:#eeeeee; padding:20px; border-style: 2px /*ridge*/dotted; display:none;"; //opacity:0.99;
      pwdiv.innerHTML = "Please, wait...";
      document.body.appendChild(wrap);
      document.body.appendChild(pwdiv);
    });
  })();

  (function () {
    ///// webappos useful fields /////
    /** Group: Useful fields */
    /**
     * Variable: webappos.app_url_name
     * 
     * Stores the current application URL name
     */
    webappos.app_url_name = null;
    var i = location.pathname.indexOf("/apps/");
    if (i >= 0) {
      webappos.app_url_name = location.pathname.substring(i + 6);
      i = webappos.app_url_name.indexOf("/");
      if (i >= 0)
        webappos.app_url_name = webappos.app_url_name.substring(0, i);
    } else {
      webappos.app_url_name = location.origin;
      i = webappos.app_url_name.indexOf("://");
      if (i >= 0)
        webappos.app_url_name = webappos.app_url_name.substring(i + 3);
      i = webappos.app_url_name.indexOf(".");
      if (i >= 0)
        webappos.app_url_name = webappos.app_url_name.substring(0, i);
    }

    if (webappos.app_url_name) {
      webappos.webcall("webappos.getAppPropertiesByUrlName", webappos.app_url_name).then(function (props) {
        if (props)
          webappos.project_extension = props.projectExtension;
        webappos.app_full_name = props.fullName;
        webappos.app_displayed_name = props.displayedName;
        webappos.app_icon_url = props.iconURL;
      });
    }

    if (!webappos.project_id) {
      /**
       * Variable: webappos.project_id
       * 
       * Stores the current project_id (or null).
       * Initialized during requrest_scopes("webappos_scopes", "project_id").
       */
      webappos.project_id = null;
    }
  })();



  ///// The following webappos functions override the parent.webappos functions /////

  // Group: Functions for accessing URL query string (iframe-specific)

  /**
  Function: webappos.js_util.get_query_value
  
  Returns the argument value from the query string
  
  Parameters:
    key - the key name, for which to get the value
  Returns:
      the value for the given key, or null, if the value was not found
  */
  webappos.js_util.get_query_value = function (key) {
    return decodeURIComponent((new RegExp('[?|&]' + key + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || null;
  }; // [source: http://stackoverflow.com/questions/11582512/how-to-get-url-parameters-with-javascript]

  /**
  Function: webappos.js_util.update_query_value
  
  Updates (sets) the given value for the given key in the current query string.
  
  Parameters:
    key - the key name, for which to update/set the value
    value - the value to set
  Returns:
    nothing
  */
  webappos.js_util.update_query_value = function (key, value) {
    baseUrl = [location.protocol, '//', location.host, location.pathname].join('');
    urlQueryString = location.search;
    var newParam = key + '=' + value,
      params = '?' + newParam;

    // If the "search" string exists, then build params from it
    if (urlQueryString) {
      keyRegex = new RegExp('([\?&])' + key + '[^&]*');
      // If param exists already, update it
      if (urlQueryString.match(keyRegex) !== null) {
        params = urlQueryString.replace(keyRegex, "$1" + newParam);
      } else { // Otherwise, add it to end of query string
        params = urlQueryString + '&' + newParam;
      }
    }

    try {
      history.replaceState({}, "", baseUrl + params);
    } catch (t) {
      // can fail in iframes running within webAppOS Desktop
    }
  };

  /**
  Function: webappos.js_util.get_and_remove_query_value

  Given the key, removes the key=value fragment from the query string and returns the deleted value.

  Parameters:
    key - the key name, for which to remove the corresponding query fragment
  Returns:
    the previous value for the given key, or null, if the value was not found
  */
  webappos.js_util.get_and_remove_query_value = function (key) {
    var value = webappos.js_util.get_query_value(key);
    if (value == null)
      return null;
    var s =
      location.search.replace("?" + key + "=" + value + "&", "?")
      .replace("&" + key + "=" + value + "&", "&")
      .replace("&" + key + "=" + value, "")
      .replace("?" + key + "=" + value, "");
    if (s == "?")
      s = "";

    history.replaceState(history.state, document.title, location.pathname + s);
    return value;
  };

  /**
  Function: webappos.js_util.get_top_level_query_value
  
  Returns the argument value from the query string of the top-level frame.
  
  Parameters:
    key - the key name, for which to get the value
  Returns:
    the value for the given key, or null, if the value was not found
  */
  webappos.js_util.get_top_level_query_value = function (key) {
    return decodeURIComponent((new RegExp('[?|&]' + key + '=' + '([^&;]+?)(&|#|;|$)').exec(webappos.top_location.search) || [, ""])[1].replace(/\+/g, '%20')) || null
  }; // [source: http://stackoverflow.com/questions/11582512/how-to-get-url-parameters-with-javascript]





  /** Group: "Web memory"-related stuff */
  ///// tda object /////
  if (!window.tda) {
    try {
      if (window.parent) {
        window.tda = Object.create(window.parent["tda"]);
      }
    } catch (t) {}
  }

  /**
   * Function: webappos.init_web_memory (iframe-specific)
   * 
   * Performs the initial synchronization of web memory and keeps on synchronizing.
   * 
   * The function is called from the webappose_scopes web service. It uses login, ws_token, and project_id, and calls tda.model.init internally.
   * 
   * If the server closes the web socket connection (or if the connection is lost for some other reason), the corresponding error message will be displayed with the option to refresh the page.
   * 
   * Normally, the connection is active until the user closes the current browser tab (or desktop iframe), or 
   * webappos.close_current_project is called (which replaces the current page with some other).
   * 
   * Returns:
   *   nothing (possible async execution)
   * 
   */

  webappos.init_web_memory = async function () {

    if (!window.tda) {
      try {
        if (window.parent) {
          window.tda = Object.create(window.parent["tda"]);
        }
      } catch (t) {}
    }

    // If could not get from the parent, define window.tda here...
    if (!window.tda) {

      window.tda_defined_here = true;
      /**
       * Variable: webmem (tda)
       * 
       * Provides access to "web memory". This object becomes available only when web memory has been initialized by
       * calling <webappos.init_web_memory>. The object will be the same for the iframe that initialized web memory and all descendant iframes that will include webappos.js.
       * 
       * See "JavaScript Model Format" doc for more detail: http://webappos.org/dev/doc/index.html#File4:webappos.js_internals.txt:JavaScript_Model_Format
       * 
       */
      window.tda = {};

      window.webmem = window.tda;

      tda.synced = false;

      tda.prototypes = {
        // the metamodel level contains JS objects corresponding to repository classes;
        // inheritance = prototyping between these objects;
        // instanceof = prototyping between the model level objects and the metamodel level objects
        classPrototype: {
          delete: function () {
            tda.model.deleteObject(this.reference);
            var arr = new Float64Array(2);
            arr[0] = 0xF2;
            arr[1] = this.reference;
            tda.websocket.send(arr.buffer);
            tda.model.registerChange();
            // TODO: delete compositions
          },
          getClassName: function () { // if cls, returns this.className, else returns the name of the first object class
            if ((this.classes) && (this.classes.length > 0))
              return this.classes[0].className;
            else
              return null;
          },
          isTypeOf: function (t) {
            var cls = t;
            if (typeof t === 'string')
              cls = tda.model[t];
            for (var i = 0; i < this.classes.length; i++)
              if (this.classes[i] == cls)
                return true;
            return false;
          },
          isKindOf: function (t) {
            var cls = t;
            if (typeof t === 'string')
              cls = tda.model[t];
            if (this.isTypeOf(cls))
              return true;
            for (var i = 0; i < this.classes.length; i++)
              if (tda.model.isDerivedClass(this.classes[i], cls))
                return true;
            return false;
          }
        },

        // the following functions will be assigned to class constructors
        f_getFirstObject: function () {
          for (var i in this.directObjects) {
            return this.directObjects[i];
          }
          // no direct objects; check in subclasses...
          for (var subRef in this.subClasses) {
            var subCls = this.subClasses[subRef];
            var subClsObj = subCls.getFirstObject();
            if (subClsObj)
              return subClsObj;
            // else continue
          }
          return null;
        },
        f_getAllObjects: function () {
          var retVal = webappos.js_util.clone_object(this.directObjects);
          for (var subRef in this.subClasses) {
            var subCls = this.subClasses[subRef];
            var subClsObjs = subCls.getAllObjects();
            for (var objRef in subClsObjs) {
              retVal[objRef] = subClsObjs[objRef]; // adding the subclass object to the result
            }
          }
          return retVal;
        },
        f_getAllAttributes: function () {
          var retVal = webappos.js_util.clone_object(this.attributes);
          for (var superRef in this.superClasses) {
            var superCls = this.superClasses[superRef];
            var superAttrs = superCls.getAllAttributes();
            for (var ref in superAttrs) {
              retVal[ref] = superAttrs[ref]; // adding the superclass attr to the result
            }
          }
          return retVal;
        },
        f_getAllAssociations: function () {
          var retVal = webappos.js_util.clone_object(this.associations);
          for (var superRef in this.superClasses) {
            var superCls = this.superClasses[superRef];
            var superAssocs = superCls.getAllAssociations();
            for (var ref in superAssocs) {
              retVal[ref] = superAssocs[ref]; // adding the superclass assoc to the result
            }
          }
          return retVal;
        },
        f_getSubObjects: function () {
          var retVal = {};
          for (var subRef in this.subClasses) {
            var subCls = this.subClasses[subRef];
            var subClsObjs = subCls.getAllObjects();
            for (var objRef in subClsObjs) {
              retVal[objRef] = subClsObjs[objRef]; // adding the subclass object to the result
            }
          }
          return retVal;
        },
      }; // tda.prototypes

      /**
       * Variable: tda.model
       * 
       * Contains repository objects and classes, which are synchronized automatically with the server.
       * Given a reference, the corresponding element (object, class, attribute, association) can be accessed as tda.model[reference].
       * 
       * Classes can also be accessed as tda.model.<ClassName>. Class instances (objects) can be created via the "new" operator. New objects will be automatically
       * accessible as tda.model[reference]. They will also be automatically synchronized with the server.
       */
      tda.model = {
        SAVE_BALL_IDLE_TIME: 5000, // in ms
        saveBallLaunched: false,
        lastChangeTime: 0, // new Date().getTime()
        registerChange: function () { // resets the save timer...
          var d = new Date();
          tda.model.lastChangeTime = d.getTime();
          if (!tda.model.saveBallLaunched) {
            tda.model.saveBallLaunched = true;
            var f = function () {
              var d = new Date();
              curTime = d.getTime();
              if (curTime - tda.model.lastChangeTime >= tda.model.SAVE_BALL_IDLE_TIME) {
                //console.log("SEND SAVE_BALL");
                tda.model.saveBallLaunched = false;
                tda.model.lastChangeTime = curTime;
                var arr = new Float64Array(1);
                arr[0] = 0xBB;
                tda.websocket.send(arr.buffer);
                tda.model.lastChangeTime = curTime;
              } else {
                // wait more...
                setTimeout(f, tda.model.SAVE_BALL_IDLE_TIME - (curTime - tda.model.lastChangeTime));
              }
            };
            setTimeout(f, tda.model.SAVE_BALL_IDLE_TIME);
          }
        },

        maxReference: 0,
        predefinedBitsCount: 1,
        predefinedBitsValues: 1,


        webcallAsync: function (actionName, arg, callback) {
          if (webappos.js_util.is_object(arg)) {
            try {
              arg = JSON.stringify(arg);
            } catch (t) {
              return null;
            }
          }

          var action = webappos.webcalls[actionName];
          if (action && action.isClient) {

            if (action.callingConventions == "jsoncall") {
              webappos.client_webcall_jsoncall(actionName, arg).then(function (result) {
                if (callback)
                  callback(result);
              }).catch(function (t) {
                if (callback)
                  callback({
                    error: t + ""
                  });
              });
            } else
            if (action.callingConventions == "webmemcall") {
              webappos.client_webcall_webmemcall(actionName, tda.model[arg]);
              if (callback)
                callback({});
            }
            return;
          }

          var intr_obj = {
            type: "webcall",
            isClient: false,
            actionName: actionName,
            argument: arg
          };
          if (action)
            intr_obj.callingConventions = action.callingConventions;
    
          if ((webappos.interrupt)&& (webappos.interrupt(intr_obj))) {
            if (callback)
                callback({
                  error: "web call interrupted"
                });

            return;
          }


          if ((action && action.callingConventions == "webmemcall") || (!action && (typeof arg == 'number'))) {
            if (typeof arg == "undefined")
              arg = 0;
            if (!(typeof arg == 'number')) {
              if (callback)
                callback({
                  error: "webmemcall argument must be a number"
                });
              return;
            }
            // webmemcall
            var arr = new Float64Array(2);
            arr[0] = 0xC0;
            arr[1] = arg;
            tda.websocket.send(arr.buffer);
            tda.websocket.send(tda.model.sharpenString(actionName));
          } else {
            // jsoncall
            if (!tda.websocket.jsonsubmitID) {
              tda.websocket.jsonsubmitID = 1;
              tda.websocket.jsonsubmitCallbacks = {};
            } else
              tda.websocket.jsonsubmitID++;

            if (callback) {
              tda.websocket.jsonsubmitCallbacks[tda.websocket.jsonsubmitID] = callback;
            }

            if (typeof arg == "undefined")
              arg = "";
            var arr = new Float64Array(2);
            arr[0] = 0xC0;
            arr[1] = tda.websocket.jsonsubmitID;
            tda.websocket.send(arr.buffer);
            tda.websocket.send(tda.model.sharpenString(actionName) + "/" + tda.model.sharpenString(arg));
          }
        },

        checkReference: function (r) {
          if (!r) {
            this.maxReference = (((this.maxReference >> this.predefinedBitsCount) + 1) << this.predefinedBitsCount) | this.predefinedBitsValues;
            r = this.maxReference;
          } else {
            if (r > this.maxReference)
              this.maxReference = r;
          }
          return r;
        },
        createClass: function (className, r) {
          r = tda.model.checkReference(r);
          var mmcls = webappos.js_util.inherit(tda.prototypes.classPrototype);
          mmcls.reference = r;
          mmcls.className = className;

          tda.prototypes[className] = mmcls;
          tda.prototypes[r] = mmcls;

          var cls = function (rObj) { // just a constructor
            var needSync = false;

            // class constructor: creating an object
            if (rObj) {
              // constructor called during sync from the server (within tda.model.createObject)
            } else {
              // constructor called as: new tda.model.ClassName()...
              rObj = tda.model.checkReference();
              needSync = true;
            }

            var cls = tda.model[r];
            // the assigned cls == tda.model[r], but tda.model[r] will be initialized by that time

            this.reference = rObj;
            this.classes = [cls];

            cls.directObjects[rObj] = this;
            tda.model[rObj] = this;

            if (needSync) {
              // sync to the server...
              var arr = new Float64Array(3);
              arr[0] = 0x02;
              arr[1] = r;
              arr[2] = rObj;
              tda.websocket.send(arr.buffer);
              tda.model.registerChange();
            };
            return this;
          };
          cls.className = className;
          cls.prototype = mmcls;
          cls.reference = r;

          // setting initial properties:
          cls.directObjects = {};
          cls.superClasses = {};
          cls.subClasses = {};
          cls.associations = {};
          cls.attributes = {};

          cls.getFirstObject = tda.prototypes.f_getFirstObject;
          cls.getAllObjects = tda.prototypes.f_getAllObjects;
          cls.getAllAttributes = tda.prototypes.f_getAllAttributes;
          cls.getAllAssociations = tda.prototypes.f_getAllAssociations;
          cls.getSubObjects = tda.prototypes.f_getSubObjects;

          tda.model[className] = cls;
          tda.model[r] = cls;
          tda.model.registerChange();
        },

        createAttributeSetterGetter: function (cls /*=mmcls*/ , name, rAttr) {
          // check if the function for the given attr name exists...
          var setName = "set" + webappos.js_util.capitalize_first_letter(name);
          cls[setName] = function (val) {
            if (this[name] == val)
              return;
            var oldval = this[name];
            if ((val == null) || (typeof val === 'undefined')) {
              delete this[name];
              var arr = new Float64Array(3);
              arr[0] = 0xF4;
              arr[1] = this.reference;
              arr[2] = rAttr;
              tda.websocket.send(arr.buffer);
              tda.model.registerChange();
            } else {
              this[name] = val;
              var arr = new Float64Array(3);
              arr[0] = 0x04;
              arr[1] = this.reference;
              arr[2] = rAttr;
              tda.websocket.send(arr.buffer);
              if ((oldval == null) || (typeof oldval == "undefined"))
                tda.websocket.send(tda.model.sharpenString("" + val));
              else
                tda.websocket.send(tda.model.sharpenString("" + val) + "/" + tda.model.sharpenString("" + oldval));
              tda.model.registerChange();
            }
          };
          var getName = "get" + webappos.js_util.capitalize_first_letter(name);
          cls[getName] = function (val) {
            return this[name];
          };
        },

        deleteAttributeSetterGetter: function (cls /*=mmcls*/ , name) {
          delete cls["set" + webappos.js_util.capitalize_first_letter(name)];
          delete cls["get" + webappos.js_util.capitalize_first_letter(name)];
        },

        createAttribute: function (rClass, name, rType, r) {
          r = tda.model.checkReference(r);
          var cls = tda.model[rClass];
          var mmcls = tda.prototypes[rClass];
          var el = {
            reference: r,
            attributeName: name,
            domain: cls
          };
          switch (rType) {
            case 2:
              el.typeName = "Integer";
              break;
            case 3:
              el.typeName = "Real";
              break;
            case 4:
              el.typeName = "Boolean";
              break;
            default:
              el.typeName = "String";
          }
          cls.attributes[r] = el;
          tda.model[r] = el;
          tda.model.createAttributeSetterGetter(mmcls, name, r);
          tda.model.registerChange();
        },

        setAttributeValue: function (rObj, rAttr, val, oldval) {
          // oldval may be null of undefined
          var obj = tda.model[rObj];
          var attrObj = tda.model[rAttr];
          var attrName = tda.model[rAttr].attributeName;
          var curval = obj[attrName];

          var curnull = (curval == null) || (typeof curval == "undefined");
          var oldnull = (oldval == null) || (typeof oldval == "undefined");
          var valnull = (val == null) || (typeof val == "undefined");

          if ((curnull && oldnull) || (curval+"" == oldval+"") || (curval && (valnull || (val+"" <= curval + "")))) {
            if (valnull)
              delete obj[attrName];
            else {
              if (attrObj.typeName == "Integer")
                obj[attrName] = parseInt(val);
              else
              if (attrObj.typeName == "Real")
                obj[attrName] = parseFloat(val);
              else
              if (attrObj.typeName == "Boolean") {
                obj[attrName] = (val) && (val.toLowerCase() == "true");
              }
              else
                obj[attrName] = val;
            }
          }

          if ((curnull && oldnull) || (curval == oldval)) {
              // validating attribute value...
              setTimeout(function() {
                // we do not validate at once, since the server could change values sequentially; we just validate the last value obtained within 100ms...
                var arr = new Float64Array(3);
                arr[0] = 0xA4;
                arr[1] = rObj;
                arr[2] = rAttr;
                tda.websocket.send(arr.buffer);
                tda.websocket.send(tda.model.sharpenString("" + obj[attrName]));
                tda.model.registerChange();  
              }, 100);
          }
          tda.model.registerChange();
        },

        createAssociationSetterGetter: function (cls /*=mmcls*/ , roleName, rAssoc, inverseRoleName) {
          var linkName = "link" + webappos.js_util.capitalize_first_letter(roleName);
          cls[linkName] = function (obj2) {
            if (!this[roleName])
              this[roleName] = [];
            for (var i = 0; i < this[roleName].length; i++)
              if (this[roleName][i] == obj2)
                return;
            this[roleName].push(obj2);
            if (inverseRoleName) {
              if (!obj2[inverseRoleName])
                obj2[inverseRoleName] = [];
              obj2[inverseRoleName].push(this);
            }

            var arr = new Float64Array(4);
            arr[0] = 0x06;
            arr[1] = this.reference;
            arr[2] = obj2.reference;
            arr[3] = rAssoc;

            if (tda.model.checkEventOrCommand(this.reference, obj2.reference, rAssoc, false/*originated, not synced*/)) {
              arr[0] = 0xE6; // already handled
            }

            tda.websocket.send(arr.buffer);
            tda.model.registerChange();
          };

          var unlinkName = "unlink" + webappos.js_util.capitalize_first_letter(roleName);
          cls[unlinkName] = function (obj2) {
            if (inverseRoleName) {
              var arr = obj2[inverseRoleName];
              for (var i = 0; i < arr.length; i++)
                if (arr[i] == this) {
                  arr.splice(i, 1);
                }
            }
            var arr = this[roleName];
            for (var i = 0; i < arr.length; i++)
              if (arr[i] == obj2) {
                arr.splice(i, 1);
                var arr = new Float64Array(4);
                arr[0] = 0xF6;
                arr[1] = this.reference;
                arr[2] = obj2.reference;
                arr[3] = rAssoc;
                tda.websocket.send(arr.buffer);
                tda.model.registerChange();
                break;
              }
            if (this[roleName].length == 0)
              delete this[roleName];
          };

          var getName = "get" + webappos.js_util.capitalize_first_letter(roleName);
          cls[getName] = function () {
            if (!this[roleName])
              return [];
            else
              return webappos.js_util.slice(this[roleName]);
          };

          var setName = "set" + webappos.js_util.capitalize_first_letter(roleName);
          cls[setName] = function (newArr) {

            if (!webappos.js_util.is_array(newArr))
              newArr = [newArr];

            if (!this[roleName])
              this[roleName] = [];

            if (newArr.length == this[roleName].length) {
              // check elements and skip, if the same...
              var diff = false;
              for (var i = 0; i < newArr.length; i++) {
                if (newArr[i] != this[roleName][i]) {
                  diff = true;
                  break;
                }
              }
              if (!diff)
                return;
            }

            if (inverseRoleName) {
              for (var k = 0; k < newArr.length; k++) {
                var arr = newArr[k][inverseRoleName];
                if (!arr) {
                  arr = [];
                  newArr[k][inverseRoleName] = [];
                }
                for (var i = 0; i < arr.length; i++)
                  if (arr[i] == this) {
                    arr.splice(i, 1);
                  }
              }
            }

            var arr = this[roleName];
            for (var i = 0; i < arr.length; i++) {
              var arr2 = new Float64Array(4);
              arr2[0] = 0xF6;
              arr2[1] = this.reference;
              arr2[2] = arr[i].reference;
              arr2[3] = rAssoc;
              tda.websocket.send(arr2.buffer);
              tda.model.registerChange();
            }
            this[roleName] = [];
            for (var i = 0; i < newArr.length; i++) {
              this[roleName].push(newArr[i]);
              if (inverseRoleName) {
                if (newArr[i][inverseRoleName])
                  newArr[i][inverseRoleName].push(this);
                else
                  newArr[i][inverseRoleName] = [this];
              }

              var arr = new Float64Array(4);
              arr[0] = 0x06;
              arr[1] = this.reference;
              arr[2] = newArr[i].reference;
              arr[3] = rAssoc;
              if (tda.model.checkEventOrCommand(this.reference, newArr[i].reference, rAssoc, false/*originated, not synced*/)) {
                arr[0] = 0xE6; // already handled;
              }
              tda.websocket.send(arr.buffer);
              tda.model.registerChange();
            }
          };
        },

        deleteAssociationSetterGetter: function (cls /*=mmcls*/ , roleName) {
          delete cls["link" + webappos.js_util.capitalize_first_letter(roleName)];
          delete cls["unlink" + webappos.js_util.capitalize_first_letter(roleName)];
          delete cls["set" + webappos.js_util.capitalize_first_letter(roleName)];
          delete cls["get" + webappos.js_util.capitalize_first_letter(roleName)];
        },

        createAssociation: function (rSourceClass, rTargetClass, sourceRoleName, targetRoleName, isComposition, r1, r2) {
          r1 = tda.model.checkReference(r1);
          r2 = tda.model.checkReference(r2);
          var cls1 = tda.model[rSourceClass];
          var cls2 = tda.model[rTargetClass];
          var mmcls1 = tda.prototypes[rSourceClass];
          var mmcls2 = tda.prototypes[rTargetClass];
          var el1 = {
            reference: r1,
            roleName: targetRoleName,
            sourceClass: cls1,
            targetClass: cls2,
            isComposition: isComposition
          };
          var el2 = {
            reference: r2,
            roleName: sourceRoleName,
            sourceClass: cls2,
            targetClass: cls1,
            isComposition: false
          };
          el1.inverse = el2;
          el2.inverse = el1;

          tda.model.createAssociationSetterGetter(mmcls1, targetRoleName, r1, sourceRoleName);
          tda.model.createAssociationSetterGetter(mmcls2, sourceRoleName, r2, targetRoleName);

          cls1.associations[r1] = el1;
          cls2.associations[r2] = el2;
          tda.model[r1] = el1;
          tda.model[r2] = el2;
          tda.model.registerChange();
        },

        createDirectedAssociation: function (rSourceClass, rTargetClass, targetRoleName, isComposition, r) {
          r = tda.model.checkReference(r);
          var cls1 = tda.model[rSourceClass];
          var cls2 = tda.model[rTargetClass];
          var mmcls1 = tda.prototypes[rSourceClass];
          var el = {
            reference: r,
            roleName: targetRoleName,
            sourceClass: cls1,
            targetClass: cls2,
            isComposition: isComposition
          };

          tda.model.createAssociationSetterGetter(mmcls1, targetRoleName, r);
          cls1.associations[r] = el;
          tda.model[r] = el;
          tda.model.registerChange();
        },

        deleteClass: function (r) {
          var cls = tda.model[r];
          // deleting objects...
          var objs = cls.getAllObjects();
          for (var rObj in objs) {
            tda.model.deleteObject(rObj);
          }

          // deleting attributes...
          var attrs = webappos.js_util.slice(cls.attributes);
          for (var rAttr in attrs) {
            tda.model.deleteAttribute(rAttr);
          }
          // deleting associations...
          var assocs = webappos.js_util.slice(cls.associations);
          for (var rAssoc in assocs) {
            tda.model.deleteAssociation(rAssoc);
          }

          // deleting superclass relationships...
          var sup = webappos.js_util.slice(cls.superClasses);
          for (var rSup in sup) {
            tda.model.deleteGeneralization(r, rSup);
          }

          // deleting subclass relationships...
          var sub = webappos.js_util.slice(cls.subClasses);
          for (var rSub in sub) {
            tda.model.deleteGeneralization(rSub, r);
          }

          delete tda.model[cls.className];
          delete tda.model[r];
          tda.model.registerChange();
        },

        createGeneralization: function (rSub, rSuper) {
          // create attribute and association setters and getters for sub objects...
          var subCls = tda.model[rSub];
          var superCls = tda.model[rSuper];

          var mmsubCls = tda.prototypes[rSub];
          var mmsuperCls = tda.prototypes[rSuper];

          if (mmsubCls.prototype == tda.model.classPrototype) {
            // only one super-class using prototyping...
            Object.setPrototypeOf(mmsubCls, mmsuperCls); // this op may be slow!
          }

          subCls.superClasses[rSuper] = superCls;
          superCls.subClasses[rSub] = subCls;
          tda.model.registerChange();
        },

        deleteGeneralization: function (rSub, rSuper) {
          var superCls = tda.model[rSuper];
          var subCls = tda.model[rSub];
          var mmsubCls = tda.prototypes[rSub];
          var mmsuperCls = tda.prototypes[rSuper];

          Object.setPrototypeOf(mmsubCls, tda.prototypes.classPrototype);
          // this op may be slow!
          // TODO: assing some next class (if exists)

          delete subCls.superClasses[rSuper];
          delete superCls.subClasses[rSub];
          tda.model.registerChange();
        },

        isDerivedClass: function (subCls, superCls) {
          for (var rSuper in subCls.superClasses) {
            if (superCls == subCls.superClasses[rSuper])
              return true;
            else
              return tda.model.isDerivedClass(subCls.superClasses[rSuper], superCls);
          }
          return false;
        },

        createObject: function (rClass, rObj) {
          var cls = tda.model[rClass];
          var el = new cls(rObj);
        },

        deleteObject: function (rObj) {
          var obj = tda.model[rObj];
          if (!obj)
            return;
            // delete links...	
          for (var i = 0; i < obj.classes.length; i++) {
            var assocs = obj.classes[i].getAllAssociations();
            for (var assocId in assocs) {              
              var assoc = assocs[assocId];

              if (assoc.isComposition) {
                var arr = obj[assoc.roleName];
                if (arr)
                  arr = webappos.js_util.slice(arr);
                else
                  arr = [];

                for (var j = 0; j < arr.length; j++) { // go through linked objects...
                  tda.model.deleteObject(arr[j].reference);
                }
              }

              if (assoc.inverse) {
                var arr = obj[assoc.roleName];
                if (!arr)
                  arr = [];
                for (var j = 0; j < arr.length; j++) { // go through linked objects...
                  var arr2 = arr[j][assoc.inverse.roleName];
                  if (!arr2)
                    arr2 = [];
                  for (var k = 0; k < arr2.length; k++) // go through backward links...
                    if (arr2[k] == obj) {
                      arr2.splice(k, 1);
                      break;
                    }
                }
              }
            }
          }

          // delete from classes:
          for (var i = 0; i < obj.classes.length; i++) {
            delete obj.classes[i].directObjects[rObj];
          }


          delete tda.model[rObj];
          tda.model.registerChange();
        },
        deleteAttribute: function (rAttr) {
          var attr = tda.model[rAttr];
          // deleting the setter and getter for every class object...
          var objs = attr.domain.getAllObjects();
          for (var rObj in objs)
            tda.model.deleteAttributeSetterGetter(objs[rObj], attr.attributeName);
          delete tda.model[rAttr];
          tda.model.registerChange();
        },
        deleteAttributeValue: function (rObj, rAttr) {
          var obj = tda.model[rObj];
          var attrName = tda.model[rAttr].attributeName;
          if (obj)
            delete obj[attrName];
          tda.model.registerChange();
        },
        deleteAssociation: function (r) {
          var assoc = tda.model[r];
          if (!assoc)
            return; // may be already deleted, when deleting a class
          var invAssoc = assoc.inverse;

          // deleting the setter and getter for every class1 object...
          var objs = assoc.sourceClass.getAllObjects();
          for (var objRef in objs)
            tda.model.deleteAssociationSetterGetter(objs[objRef], assoc.roleName);

          if (invAssoc) {
            // deleting the setter and getter for every class2 object...
            objs = assoc.targetClass.getAllObjects();
            for (var objRef in objs)
              tda.model.deleteAssociationSetterGetter(objs[objRef], invAssoc.roleName);
          }

          delete assoc.sourceClass.associations[r];
          delete tda.model[r];
          if (invAssoc) {
            delete assoc.targetClass.associations[invAssoc.reference];
            delete tda.model[invAssoc.reference];
          }
          tda.model.registerChange();
        },

        checkEventOrCommand: function (r1, r2, rAssoc, synced) {
          if (!r1 || !r2 || !rAssoc)
            return false;
          var obj1 = tda.model[r1];
          var obj2 = tda.model[r2];
          var assoc = tda.model[rAssoc];
          if (!obj1 || !obj2) {
            console.log("checkEventOrCommand [" + r1 + "," + r2 + "," + rAssoc + " " + synced + "]: error in obj1 or obj2", obj1, obj2, assoc);
            return false;
          }
          if (!obj1.classes || !obj2.classes) {
            console.log("checkEventOrCommand [" + r1 + "," + r2 + "," + rAssoc + " " + synced + "]: error in obj1 or obj2 classes ", obj1, obj2);
            return false;
          }
          if (obj1.classes[0] == tda.model["Submitter"]) {
            var t = obj1;
            obj1 = obj2;
            obj2 = t;
          }
          if (obj2.classes[0] == tda.model["Submitter"]) {
            var className = obj1.getClassName();
            if (webappos.webcalls[className] && webappos.webcalls[className].isClient) {
              console.log("client-side webmemcall");
              webappos.client_webcall_webmemcall(className, obj1);
              return true; // event or command handled
            }

            if (synced)
              return true; // do not create the link at the client-side (assume the event or command handled)            
            else  {
              if ((webappos.interrupt)&& (webappos.interrupt({
                type: "submit",
                argument: obj1.reference,
                className: obj1.getClassName()
              }
              )))
                return true; // assume the event or command handled (interrupted)
            }
          }
          return false;
        },

        createLink: function (r1, r2, rAssoc) {
          var obj1 = tda.model[r1];
          var obj2 = tda.model[r2];
          var assoc = tda.model[rAssoc];
          if (tda.model.linkExists(r1, r2, rAssoc))
            return;
          if (!tda.model.checkEventOrCommand(r1, r2, rAssoc, true)) {
            if (!obj1[assoc.roleName])
              obj1[assoc.roleName] = [];
            obj1[assoc.roleName].push(obj2);
            if (assoc.inverse) {
              if (!obj2[assoc.inverse.roleName])
                obj2[assoc.inverse.roleName] = [];
              obj2[assoc.inverse.roleName].push(obj1);
            }

            // validate server-side link...
            var arr = new Float64Array(4);
            arr[0] = 0xA6;
            arr[1] = r1;
            arr[2] = r2;
            arr[3] = rAssoc;
            tda.websocket.send(arr.buffer);
            tda.model.registerChange();
          }
        },
        createOrderedLink: function (r1, r2, rAssoc, i) {
          var obj1 = tda.model[r1];
          var obj2 = tda.model[r2];
          var assoc = tda.model[rAssoc];
          if (tda.model.linkExists(r1, r2, rAssoc))
            return;
          if (!tda.model.checkEventOrCommand(r1, r2, rAssoc, true)) {
            if (!obj1[assoc.roleName])
              obj1[assoc.roleName] = [];
            obj1[assoc.roleName].splice(i, 0, obj2);
            if (assoc.inverse) {
              if (!obj2[assoc.inverse.roleName])
                obj2[assoc.inverse.roleName] = [];
              obj2[assoc.inverse.roleName].push(obj1);
            }
            // validate server-side link...
            var arr = new Float64Array(4);
            arr[0] = 0xA6;
            arr[1] = r1;
            arr[2] = r2;
            arr[3] = rAssoc;
            tda.websocket.send(arr.buffer);
            tda.model.registerChange();
          }
        },
        deleteLink: function (r1, r2, rAssoc) {
          var obj1 = tda.model[r1];
          var obj2 = tda.model[r2];
          var assoc = tda.model[rAssoc];
          var arr = obj1 ? obj1[assoc.roleName] : null;
          var deleted = false;
          if (arr)
            for (var i = 0; i < arr.length; i++)
              if (arr[i] == obj2) {
                deleted = true;
                arr.splice(i, 1);
                if (arr.length == 0)
                  delete obj1[assoc.roleName];
                break;
              }
          if (assoc.inverse) {
            var arr = obj2 ? obj2[assoc.inverse.roleName] : null;
            if (arr)
              for (var i = 0; i < arr.length; i++)
                if (arr[i] == obj1) {
                  deleted = true;
                  arr.splice(i, 1);
                  if (arr.length == 0)
                    delete obj2[assoc.inverse.roleName];
                  break;
                }
          }
          tda.model.registerChange();
        },

        linkExists: function (r1, r2, rAssoc) {
          var obj1 = tda.model[r1];
          var obj2 = tda.model[r2];
          var assoc = tda.model[rAssoc];
          var arr = obj1[assoc.roleName];
          if (!arr)
            return false;
          for (var i = 0; i < arr.length; i++)
            if (arr[i] == obj2) {
              return true;
            }
          if (assoc.inverse) {
            var arr = obj2[assoc.inverse.roleName];
            if (!arr)
              return false;

            for (var i = 0; i < arr.length; i++)
              if (arr[i] == obj1) {
                return true;
              }
          }
          return false;
        },

        sharpenString: function (s) {
          return s.split("#").join("#SHARP#").split("`").join("#GRAVE#").split("/").join("#SLASH#");
        },

        unsharpenString: function (s) {
          return s.split("#SLASH#").join("/").split("#GRAVE#").join("`").split("#SHARP#").join("#");
        },
      }; // tda.model


      tda.model.init = async function () {
        if (tda.websocket)
          return true; // already initialized

        var protocol;
        var port = window.location.port;

        if (window.location.protocol === 'https:') {
          protocol = 'wss://';
          if (!port)
            port = 443;
        } else {
          protocol = 'ws://';
          if (!port)
            port = 80;
        }


        let promise = new Promise((resolve, reject) => {




          console.log("connecting... " + port + " " + webappos.ws_token);
          var socket = new WebSocket(protocol + location.hostname + ":" + port + "/ws/");
          tda.websocket = socket;
          socket.binaryType = "arraybuffer";
          socket.onopen = function () {
            console.log("Connected.");
            var d = new Date();
            webappos.connect_started = d.getTime();
            if (webappos.project_id != null) {
              if (webappos.js_util.starts_with(webappos.project_id, "apptemplate:") ||
                webappos.js_util.starts_with(webappos.project_id, "publishedtemplate:") ||
                webappos.js_util.starts_with(webappos.project_id, "usertemplate:")) {
                socket.send("FROM_TEMPLATE" + " " + webappos.login + " " + webappos.ws_token + " " + webappos.app_url_name + " " + webappos.project_id.split(" ").join("\\ ") + " " + webappos.login + "/new." + webappos.project_extension);
                webappos.set_project_id(webappos.login + "/new." + webappos.project_extension); // ???
              } else {
                socket.send("OPEN " + webappos.login + " " + webappos.ws_token + " " + webappos.app_url_name + " " + webappos.project_id);
              }
            } else {
              socket.send("NEW" + " " + webappos.login + " " + webappos.ws_token + " " + webappos.app_url_name + " " + webappos.login + "/new." + webappos.project_extension);
              webappos.set_project_id(webappos.login + "/new." + webappos.project_extension); // ???
              tda.bootstrapped = true;
            }
            var d = new Date();
            console.log("ws_token sent. " + d.getTime());
          };
          socket.onclose = function (event) {
            if (event.wasClean) {
              console.log('Closed (OK)');
              webappos.js_util.show_failure("We closed the connection due to inactivity.<br>Did you enjoy your coffee?");
            } else {
              console.log('Closed (halt)');
              webappos.js_util.show_failure("The server has unexpectedly closed the socket connection.<br>Perhaps, some server-side exception has occured.");
              reject("The server has unexpectedly closed the socket connection.<br>Perhaps, some server-side exception has occured.");
            }
            console.log(event.code + ' ' + event.reason);
          };
          socket.onmessage = function (event) {
            var arr;
            if (typeof event.data === "string") {
              if (typeof tda.websocket.arr != 'undefined') {

                arr = tda.websocket.arr;
                tda.websocket.arr = null;
                delete tda.websocket.arr; // this tda.websocket.arr may be checked/assigned later for some other (inner) action

                switch (arr[0]) {
                  case 0x01:
                    tda.model.createClass(tda.model.unsharpenString(event.data), arr[1]);
                    break;
                  case 0x03:
                    tda.model.createAttribute(arr[1], tda.model.unsharpenString(event.data), arr[2], arr[3]);
                    break;
                  case 0x04:
                    var k = event.data.indexOf("/");
                    if (k >= 0) {
                      var parts = event.data.split("/");
                      tda.model.setAttributeValue(arr[1], arr[2], tda.model.unsharpenString(parts[0]), tda.model.unsharpenString(parts[1]));
                    } else
                      tda.model.setAttributeValue(arr[1], arr[2], tda.model.unsharpenString(event.data), null);
                    break;
                  case 0xA4:
                    var obj = tda.model[arr[1]];
                    var rAttr = arr[2];
                    var attrName = tda.model[arr[2]].attributeName;
                    var curval = obj[attrName];
                    var value = tda.model.unsharpenString(event.data);
                    if (curval != value) {
                      if (!curval || (curval < value)) {
                        // sync curval

                        if ((curval == null) || (typeof curval === 'undefined')) {
                          var arr = new Float64Array(3);
                          arr[0] = 0xF4;
                          arr[1] = obj.reference;
                          arr[2] = rAttr;
                          tda.websocket.send(arr.buffer);
                          tda.model.registerChange();
                        } else {
                          var arr = new Float64Array(3);
                          arr[0] = 0x04;
                          arr[1] = obj.reference;
                          arr[2] = rAttr;
                          tda.websocket.send(arr.buffer);
                          tda.websocket.send(tda.model.sharpenString(curval + "") + "/" + tda.model.sharpenString(value));
                          tda.model.registerChange();
                        }

                      } else
                        tda.model.setAttributeValue(arr[1], arr[2], value, curval);
                    }
                    break;
                  case 0x05:
                    var parts = event.data.split("/");
                    tda.model.createAssociation(arr[1], arr[2], tda.model.unsharpenString(parts[0]), tda.model.unsharpenString(parts[1]), arr[3]?true:false, arr[4], arr[5]);
                    break;
                  case 0x15:
                    tda.model.createDirectedAssociation(arr[1], arr[2], tda.model.unsharpenString(event.data), arr[3]?true:false, arr[4]);
                    break;
                  case 0xC1:
                    var callback = tda.websocket.jsonsubmitCallbacks[arr[1]];
                    if (callback) {
                      var json = null;
                      try {
                        if (event.data)
                          json = JSON.parse(tda.model.unsharpenString(event.data));
                      } catch (t) {
                        console.log("Error during tda.model.webcallAsync result parse: " + t);
                      }
                      try {
                        callback(json);
                      } catch (t) {
                        console.log("Error during tda.model.webcallAsync callback: " + t);
                      }
                      delete tda.websocket.jsonsubmitCallbacks[arr[1]];
                    }
                    break;
                  case 0xC0:
                    var aa = event.data.split("/"); // action/argument
                    if (aa.length == 1) {
                      webappos.client_webcall_webmemcall(tda.model.unsharpenString(aa[0]), tda.model[arr[1]]);
                    } else {
                      var id = arr[1];
                      webappos.client_webcall_jsoncall(tda.model.unsharpenString(aa[0]), tda.model.unsharpenString(aa[1])).then(function (result) {
                        if (id>0) {
                          var arr2 = new Float64Array(2);
                          arr2[0] = 0xC1;
                          arr2[1] = id;
                          tda.websocket.send(arr2.buffer);
                          tda.websocket.send(tda.model.sharpenString(JSON.stringify(result)));
                        }
                      });
                    }
                    break;
                  case 0xFC:
                    webappos.set_project_id(tda.model.unsharpenString(event.data));
                    break;
                    // TODO: 0x25
                  case 0xEE:
                    var i = 1;
                    var d = new Date();
                    var newEvent = {};
                    var strArr = event.data.split("`");
                    var j = 0;
                    while (i < arr.length) {
                      switch (arr[i]) {
                        case 0xF2:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 2);
                          tda.websocket.onmessage(newEvent);
                          i += 2;
                          break;
                        case 0x01:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 2);
                          tda.websocket.onmessage(newEvent);
                          i += 2;
                          newEvent.data = strArr[j];
                          tda.websocket.onmessage(newEvent);
                          j++;
                          break;
                        case 0x03:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 4);
                          tda.websocket.onmessage(newEvent);
                          i += 4;
                          newEvent.data = strArr[j];
                          tda.websocket.onmessage(newEvent);
                          j++;
                          break;
                        case 0x04:
                        case 0xA4:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 3);
                          tda.websocket.onmessage(newEvent);
                          i += 3;
                          newEvent.data = strArr[j];
                          tda.websocket.onmessage(newEvent);
                          j++;
                          break;
                        case 0x05:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 6);
                          tda.websocket.onmessage(newEvent);
                          i += 6;
                          newEvent.data = strArr[j];
                          tda.websocket.onmessage(newEvent);
                          j++;
                          break;
                        case 0x15:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 5);
                          tda.websocket.onmessage(newEvent);
                          i += 5;
                          newEvent.data = strArr[j];
                          tda.websocket.onmessage(newEvent);
                          j++;
                          break;
                          // TODO: 0x25
                        case 0xF1:
                        case 0xF3:
                        case 0xF5:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 2);
                          tda.websocket.onmessage(newEvent);
                          i += 2;
                          break;
                        case 0xFF:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 4);
                          tda.websocket.onmessage(newEvent);
                          i += 4;
                          break;
                        case 0x11:
                        case 0xE1:
                        case 0xF4:
                        case 0x02:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 3);
                          tda.websocket.onmessage(newEvent);
                          i += 3;
                          break;
                        case 0x06:
                        case 0xF6:
                        case 0xE6:
                        case 0xA6:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 4);
                          tda.websocket.onmessage(newEvent);
                          i += 4;
                          break;
                        case 0x16:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 5);
                          tda.websocket.onmessage(newEvent);
                          i += 5;
                          break;
                          // TODO: 0x12 0xE2 0x22
                        case 0xFC:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 1);
                          tda.websocket.onmessage(newEvent);
                          i += 1;
                          newEvent.data = strArr[j];
                          tda.websocket.onmessage(newEvent);
                          j++;
                          break;
                        case 0xC0:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 2);
                          tda.websocket.onmessage(newEvent);
                          i += 2;
                          newEvent.data = strArr[j];
                          tda.websocket.onmessage(newEvent);
                          j++;
                          break;
                        case 0xC1:
                          newEvent.data = webappos.js_util.slice(arr, i, i + 2);
                          tda.websocket.onmessage(newEvent);
                          i += 2;
                          newEvent.data = strArr[j];
                          tda.websocket.onmessage(newEvent);
                          j++;
                          break;
                        default:
                          console.log("ERROR in processing bulk actions, i=" + i);
                          for (var i = 0; i < arr.length; i++)
                            console.log("  arr[" + i + "]=" + arr[i]);
                      }
                    }
                    var d2 = new Date();
                    //			console.log("bulk string (and data) processed "+d2.getTime());
                    break;
                }
                delete tda.websocket.arr;
              } else {
                /*        var d = new Date();
                
                
                        console.log("socket strdata len: " + event.data.length+" "+d.getTime());
                        if (event.data.length<100) {
                          console.log("socket strdata: "+event.data);
                        }*/
              }
            } else
            if ((event.data instanceof ArrayBuffer) || (event.data instanceof Float64Array) || webappos.js_util.is_array(event.data)) {
              if (event.data instanceof ArrayBuffer)
                arr = new Float64Array(event.data);
              else
                arr = event.data;

              switch (arr[0]) {
                case 0x01:
                case 0x03:
                case 0x04:
                case 0xA4:
                case 0x05:
                case 0x15:
                case 0x25:
                case 0xC1:
                case 0xFC:
                case 0xC0:
                  // for string-containing ops, save the arr, and process the op on string message
                  tda.websocket.arr = arr;
                  break;
                case 0xEE:
                  var d = new Date();
                  tda.websocket.arr = arr;
                  break;
                case 0xF1:
                  tda.model.deleteClass(arr[1]);
                  break;
                case 0x11:
                  tda.model.createGeneralization(arr[1], arr[2]);
                  break;
                case 0xE1:
                  tda.model.deleteGeneralization(arr[1], arr[2]);
                  break;
                case 0x02:
                  tda.model.createObject(arr[1], arr[2]);
                  break;
                case 0xF2:
                  tda.model.deleteObject(arr[1]);
                  break;
                  // TODO: 0x12 0xE2 0x22
                case 0xF3:
                  tda.model.deleteAttribute(arr[1]);
                  break;
                case 0xF4:
                  tda.model.deleteAttributeValue(arr[1], arr[2]);
                  break;
                case 0xF5:
                  tda.model.deleteAssociation(arr[1]);
                  break;
                case 0x06:
                  tda.model.createLink(arr[1], arr[2], arr[3]);
                  break;
                case 0x16:
                  tda.model.createOrderedLink(arr[1], arr[2], arr[3], arr[4]);
                  break;
                case 0xA6:
                  // validate that link exists...
                  if (tda.model.linkExists(arr[1], arr[2], arr[3])) {} // ok
                  else {
                    // the link does not exist; 
                    // we force to delete the link at the server-side
                    var arr2 = new Float64Array(4);
                    arr2[0] = 0xF6;
                    arr2[1] = arr[1];
                    arr2[2] = arr[2];
                    arr2[3] = arr[3];
                    tda.websocket.send(arr2.buffer);
                    tda.model.registerChange();
                  };
                  break;
                case 0xF6:
                  tda.model.deleteLink(arr[1], arr[2], arr[3]);
                  break;
                case 0xFF:
                  tda.model.checkReference(arr[1]);
                  tda.model.predefinedBitsCount = arr[2];
                  tda.model.predefinedBitsValues = arr[3];
                  if (tda.standalone) {
                    console.log("standalone sync done!");
                    tda.synced = true;
                    console.log("tda.ee=" + tda.ee);
                  } else {
                    console.log("web sync done!");
                    // issuing a command...

                    tda.synced = true;
                    var d = new Date();
                    webappos.connect_finished = d.getTime();
                    console.log("Initial sync done in "+(webappos.connect_finished-webappos.connect_started)+" ms");

                    webappos.webcall("webappos.getAvailableWebCalls").then(function (result) {
                      webappos.webcalls = result;

                      tda.model.webcall("webappos.initializeProject", {bootstrapped: tda.bootstrapped}).then(()=>resolve(true));
                    });

                  }
                  break;
                case 0xBB:
                  tda.model.registerChange(); // start new saveBall
                  break;
                case 0xFE:
                  localStorage.removeItem("login");
                  localStorage.removeItem("ws_token");
                  var redirect = window.location.href;
                  window.location.href = "/apps/login?signout=true&redirect=" + redirect;
                  break;
                default:
                  console.log("bindata(" + arr[0] + "): " + event.data);
              }
            } else {
              webappos.js_util.print_stack_trace();
              console.log("wrong arr type!", typeof event.data, JSON.stringify(event.data));
              throw "wrong arr type!";
            }
          };
          socket.onerror = function (error) {
            console.log("error: " + JSON.stringify(error));
          };
          console.log("connecting socket done.");

        });

        return promise;
      }; // tda.model.init

      tda.model.disassemble_object = function (obj) {
        if (null == obj || "object" != typeof obj) return obj;
        var copy = {}; //obj.constructor();
        for (var attr in obj) {
          if (obj.hasOwnProperty(attr) && (!webappos.js_util.is_function(obj[attr])) && (attr != "classes")) copy[attr] = obj[attr] + "";
        }
        copy.className = obj.className;
        return copy;
      }

      /**
       * Function: tda.model.submit
       * 
       * Attaches the given command or event object to the submitter object to execute the given command or handle the given event.
       * 
       * Parameters:
       *   obj - a TDA command or event object within the tda.model scope
       * 
       * Returns:
       *   nothing (possible async execution)
       * 
       */
      tda.model.submit = function (obj) {
        tda.model.last_submitted = tda.model.disassemble_object(obj);
        var d = new Date();
        var dt = d.getTime()-webappos.connect_finished;

        console.log("submit" + obj.getClassName() + ", r=" + obj.reference+", time since connect finished="+dt);

        if (obj.linkSubmitter)
          obj.linkSubmitter(tda.model["Submitter"].getFirstObject());
        else {

          var rsubmitter = tda.model["Submitter"].getFirstObject().reference;
          var arr = new Float64Array(4);
          arr[0] = 0x06;
          arr[1] = obj.reference;
          arr[2] = rsubmitter;
          arr[3] = 0;

          if (tda.model.checkEventOrCommand(obj.reference, rsubmitter, 0, false/*originated, not synced*/)) {
            arr[0] = 0xE6; // already handled
          }

          tda.websocket.send(arr.buffer);
          tda.model.registerChange();
        }
      };

      /**
       * Function: tda.model.webcall (async)
       * 
       *   Executes the given web call asynchronously via the current webAppOS web socket.
       * 
       * Parameters:
       *   action - the name of the action to call (defined at the server side in *.webcalls files)
       *   arg - action argument; usually, a JSON object; sometimes - a string object; can be also an integer specifying a TDA argument
       * 
       * Returns:
       *   a Promise which resolves in a parsed resulting JSON of the web call as a JavaScript object;
       *   the promise is rejected if the returned object contains the "error" attribute, or if some other error occurs
       */
      tda.model.webcall = async function (action, arg) {
        let promise = new Promise((resolve, reject) => {

          tda.model.webcallAsync(action, arg, function (retVal) {
            return resolve(retVal);
          });

        });

        return promise;
      };

    }; // if (!window.tda)

    // launching...
    return await tda.model.init();

  }; // init_web_memory

} // script_label