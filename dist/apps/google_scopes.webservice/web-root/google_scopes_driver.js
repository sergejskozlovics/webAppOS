define(function(){

    var gapi;
  
    var init = function() {
      gapi = window.gapi;
      gapi.load('auth2', function () {
        console.log(gapi.auth2);
        /* Ready. Make a call to gapi.auth2.init or some other API */
      });
    }
   
    //Google OAuth2.0
    var onSignIn = function() {
      var googleDriveCheckbox = document.querySelector('input[value="google-drive"]');

      var xhr = new XMLHttpRequest();
      xhr.open("GET", "/services/google_scopes/oauth_client_id", false);
      var google_oauth_client_id = "";
      xhr.onreadystatechange = function () {
        if (this.readyState == this.DONE) {
          google_oauth_client_id = xhr.responseText.trim(); // assign to the upper variable
        }
      };

      xhr.send();
      console.log("Google oauth: " + google_oauth_client_id);
      gapi.auth2.init({ client_id: google_oauth_client_id }).then(
        function () {
          let auth2 = gapi.auth2.getAuthInstance({
            client_id: google_oauth_client_id,
            cookiepolicy: 'single_host_origin',
            // scope: 'profile' + (googleDriveCheckbox.checked ? " https://www.googleapis.com/auth/drive" : ""),
            scope: 'profile',
            ux_mode: 'redirect',
          });
          googleUser = auth2.currentUser.get();

          if(googleDriveCheckbox.checked){
            let option = new gapi.auth2.SigninOptionsBuilder();
            option.setScope('email https://www.googleapis.com/auth/drive');

            googleUser = auth2.currentUser.get();
            googleUser.grant(option).then(
              function(success){
                console.log(JSON.stringify({message: "success", value: success}));
              },
              function(fail){
                alert(JSON.stringify({message: "fail", value: fail}));
              });
          }

          /* if (googleDriveCheckbox.checked) {
             let options = new gapi.auth2.SigninOptionsBuilder(
               { 'scope': 'email https://www.googleapis.com/auth/drive' });
   
             googleUser = auth2.currentUser.get();
             googleUser.grant(options).then(
               function (success) {
                 console.log(JSON.stringify({ message: "success", value: success }));
                 console.log("Success Google drive permission");
               });
           }*/

          // The ID token you need to pass to your backend:
          let resp = googleUser.getAuthResponse();
          console.log("RESP",resp);
          var id_token = resp.id_token;
          console.log("1");
          var access_token = resp.access_token;
          console.log("2");
          var xhr = new XMLHttpRequest();

          console.log("3");
          xhr.open('POST', '/services/google_scopes/', true);
          console.log("4");
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          console.log(xhr.status);
          xhr.onload = function () {
            if (xhr.status == 200) {
              console.log("6");
              console.log('Signed in as: ' + xhr.responseText);
              try {
                let json = JSON.parse(xhr.responseText);
                console.log(json);
                window.location.href=json.redirect;
              } catch (e) {
                console.trace();
              }
            }
          };

          xhr.send('id_token=' + id_token+"&access_token=" + access_token);
        }
        );
    }

    var signOut = function() {
      var auth2 = gapi.auth2.getAuthInstance();
      auth2.signOut().then(function () {
        console.log('User signed out.');
      });
    }
  
    return {
        request_access: async function(scopes, serverless_only) {

          if (scopes !== "button")
            throw new Error("Only 'button' scopes are supported.");
          
          window.init = init;
          window.onSignIn = onSignIn;
          window.signOut = signOut;
          let el = document.getElementById("google_scopes_driver");
          el.innerHTML = 
            '  <script src="https://apis.google.com/js/platform.js?onload=init" async defer></script>\
               <link rel="stylesheet" type="text/css" href="css/zocial.css">\
               <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css">\
               <div class="zocial google" <input type="button" value="clk" onclick="onSignIn()">Google login</input></div>\
               <input type="checkbox" id="google-drive" name="google-drive" value="google-drive" checked>\
               <label for="google-drive">Connect Google Drive</label>\
               <a href="#" onclick="signOut();">Sign out</a>\
            ';
          return true;
        },

        revoke_serverless_access: async function(withRedirect) {
          
          return new Promise((resolve,reject)=> {
            
                  return resolve(true);
          });

        }
    };
});


