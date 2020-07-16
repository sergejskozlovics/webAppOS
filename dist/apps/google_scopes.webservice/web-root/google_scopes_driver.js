define(function(){


    var fff = async function() { // private function
      
      return true;
    };
  
    
  
    return {
        request_access: async function(scopes, serverless_only) {
          
          throw new Error("Only ... scopes are supported.");
          
          return true;
        },

        revoke_serverless_access: async function(withRedirect) {
          
          return new Promise((resolve,reject)=> {
            
                  return resolve(true);
          });

        }
    };
});


