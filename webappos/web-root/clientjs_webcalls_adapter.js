define(function(){
  
    return {
        tdacall: async function(action, obj) {
          if (!obj)
            return;
          if (tda.ee) {
            if (obj.isKindOf("TDAKernel::Event")) {
              tda.ee.searchForFrameAndHandleEvent(obj, action);
            }
            else
            if (obj.isKindOf("TDAKernel::Command")) {
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
