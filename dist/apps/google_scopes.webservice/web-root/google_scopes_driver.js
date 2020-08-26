define(function () {

  var gapi;

  var init = function () {
    console.log("in google_scopes init");
    gapi = window.gapi;
    gapi.load('auth2', function () {
      // console.log(gapi.auth2);
      /* Ready. Make a call to gapi.auth2.init or some other API */
    });
  }

  //Google OAuth2.0
  var onSignIn = function () {
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
    console.log("Google OAuth Client ID: " + google_oauth_client_id);
    gapi.auth2.init({ client_id: google_oauth_client_id }).then(
      function () {
        let auth2 = gapi.auth2.getAuthInstance({
          client_id: google_oauth_client_id,
          cookiepolicy: 'single_host_origin',
          // scope: 'profile' + (googleDriveCheckbox.checked ? " https://www.googleapis.com/auth/drive" : ""),
          scope: 'profile',
          ux_mode: 'redirect',
        });

        let afterSuccess = function () {
          // The ID token we need to pass to our backend:
          let googleUser = auth2.currentUser.get();
          let resp = googleUser.getAuthResponse();
          var id_token = resp.id_token;
          console.log("id_token is " + id_token, resp.access_token);
          var access_token = resp.access_token;
          var xhr = new XMLHttpRequest();

          xhr.open('POST', '/services/google_scopes/', true);
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          xhr.onload = function () {
            console.log(xhr.status);
            if (xhr.status == 200) {
              try {
                let json = JSON.parse(xhr.responseText);
                console.log(json);
                window.location.href = json.redirect;
              } catch (e) {
                console.trace();
              }
            }
          };

          xhr.send('id_token=' + id_token + "&access_token=" + access_token);
        };

        if (googleDriveCheckbox.checked) {
          // sign in with google drive
          let googleUser = auth2.currentUser.get();
          let option = new gapi.auth2.SigninOptionsBuilder();
          option.setScope('email https://www.googleapis.com/auth/drive');

          googleUser.grant(option).then(
            function (successValue) {
              console.log("Google drive connected successfully.");
              afterSuccess();
            },
            function (failValue) {
              console.log(failValue);
              alert("Google drive not connected.");
              afterSuccess();
            });
        }
        else {
          // just sign in
          auth2.signIn().then(function (res) {
            let googleUser = auth2.currentUser.get();
            let resp = googleUser.getAuthResponse();
            var id_token = resp.id_token;
            console.log("id_token signIn is " + id_token, resp.access_token);
            afterSuccess();
          });
        }


      }
    );
  }

  var signOut = async function () {
    alert("Google sign out");
    console.log("Signing out");

    return new Promise(function (resolve, reject) {
      var auth2 = gapi.auth2.getAuthInstance();
      if (auth2) {
        auth2.signOut().then(function () {
          console.log('User signed out.');
          resolve(true);
        }).catch(function () {
          resolve(false);
        });
      }
      else
        resolve(true);
    });
  }

  if (!window.onSignIn) {
    window.onSignIn = onSignIn;
    window.signOut = signOut;
    let sc = document.createElement("script");
    sc.src = "https://apis.google.com/js/platform.js";
    sc.onload = function () { init(); };
    document.head.appendChild(sc);
  }

  return {
    request_access: async function (scopes, serverless_only) {

      if (scopes !== "button")
        throw new Error("Only 'button' scopes are supported.");

      let el = document.getElementById("google_scopes_driver");
      el.innerHTML =
        '\
               <link rel="stylesheet" type="text/css" href="css/zocial.css">\
               <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css">\
               <div style="width:100%" class="zocial google" onclick="onSignIn()">Google login</div>\
               <input type="checkbox" id="google-drive" name="google-drive" value="google-drive" checked>\
               <label for="google-drive">&nbsp;&nbsp;with Google drive</label>\
               <a href="#" onclick="signOut();">Sign out</a>\
            ';
      return true;
    },

    revoke_serverless_access: async function (withRedirect) {
      return signOut();
    }
  };
});


