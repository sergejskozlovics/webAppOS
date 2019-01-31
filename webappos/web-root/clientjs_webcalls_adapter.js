define(function(){
  
    return {
        tdacall: async function(action, obj) {
          if (!obj)
            return;
          if (obj.isKindOf("TDAKernel::Event")) {
            tda.ee.searchForFrameAndHandleEvent(obj, action);
          }
          else
          if (obj.isKindOf("TDAKernel::Command")) {
            tda.ee.searchForFrameAndExecuteCommand(obj, action);
          }
        },
        jsoncall: async function(action, arg) {
          if (window[action])
             return window[action](arg);
          else
             throw "Function "+action+" not found.";
        }
    };
});


