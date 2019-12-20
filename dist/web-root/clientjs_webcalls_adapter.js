define(function(){
  
    return {
        webmemcall: async function(actionName, arg) {
          let w = null;
          try {
            w = await webappos.get_client_webcall_window(actionName, arg);
          }catch(t){
          }
          if (!w)
            w = window;

          actionName = actionName.split("#").join("_");

          if (!w[actionName] && arg && arg.reference && arg.getClassName) {
            let className = arg.getClassName();
            if (className) {
              if (arg.isKindOf("Event")) {
                actionName = "handle"+className;
              }
              else
              if (arg.isKindOf("Command")) {
                actionName = "execute"+className;
              }
            }
          }

          actionName = actionName.split("#").join("_");

          try {
            if (w[actionName]) {
              return w[actionName](arg);
            }
            else {
              throw "Could not find client-side webmemcall "+actionName+".";
            }
          }
          catch(t) {
            console.log(t);
            throw "Error while executing client-side "+actionName+".";
          }
        },

        jsoncall: async function(actionName, arg) {
          let w = null;
          try {
            w = await webappos.get_client_webcall_window(actionName, arg);
          }catch(t){
          }
          if (!w)
            w = window;

          actionName = actionName.split("#").join("_");

          try {
            if (w[actionName]) {
              try {
                arg = eval("("+arg+")"); // better instead of JSON.parse, since arg can contain ' instead of "
              } catch(t){
              }
              return w[actionName](arg);
            }
            else {
              throw "Could not find client-side jsoncall "+actionName+".";
            }
          }
          catch(t) {
            console.log(t);
            throw "Error while executing client-side "+actionName+".";
          }
        }
    };
});
