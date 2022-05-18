const url = 'http://localhost:8080';
const header = 'Authorization';

function signup() {
  let username = document.getElementById("username").value;
  let password = document.getElementById("password").value;
  let email = document.getElementById("email").value;
  if (username == null || username === '') {
    alert("Please enter username!!");
  } else if (password == null || password === '') {
    alert("Please enter password!!");
  } else {
    $.ajax({
      url: url + "/auth/signup",
      type: 'POST',
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify({
        "username": username,
        "password": password,
        "email": email
      })})
      .done(function (data) {
        username = data.userName;
        alert("Signup Completed!\n Your username is: " + username);
        window.location.href = '/';
      }).fail(function (error) {
        alert(error.responseJSON.message);
      });
  }
}

function login() {
  let username = document.getElementById("username").value;
  let password = document.getElementById("password").value;
  if (username == null || username === '') {
    alert("Please enter username!!");
  } else if (password == null || password === '') {
    alert("Please enter password!!");
  } else {
    $.ajax({
      url: url + "/auth/login",
      type: 'POST',
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify({
        "username": username,
        "password": password
      })})
    .done(function (data) {
      window.localStorage.setItem(header, data.grantType + ' ' + data.accessToken);
      // 이제 대기실로 연결해 주자!!
      window.location.href = '/';
    }).fail(function (error) {
      console.log(error);
    });
  }
}


