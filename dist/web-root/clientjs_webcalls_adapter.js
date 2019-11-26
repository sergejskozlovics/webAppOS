define(function(){
  
    return {
        webmemcall: async function(action, obj) {
          if (tda.ee && obj && (obj.reference!=0)) {
            if (obj.isKindOf("Event")) {
              tda.ee.searchForFrameAndHandleEvent(obj, action);
            }
            else
            if (obj.isKindOf("Command")) {
              tda.ee.searchForFrameAndExecuteCommand(obj, action);
            }
          }
          else
          if (window[action]) {
             try {
               obj = JSON.parse(obj);
             } catch(t){}
             return window[action](obj);
          }
          else {
             throw "Function "+action+" not found.";
          }
        },
        jsoncall: async function(action, arg) {
          if (window[action]) {
             try {
               arg = JSON.parse(arg);
             } catch(t){}
             return window[action](arg);
          }
          else
             throw "Function "+action+" not found.";
        }
    };
});
