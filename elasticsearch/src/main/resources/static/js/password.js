function password_show_hide() {
  var pwd = document.getElementById("password");
  var show_eye = document.getElementById("show_eye");
  var hide_eye = document.getElementById("hide_eye");
  hide_eye.classList.remove("d-none");
  if (pwd.type === "password") {
    pwd.type = "text";
    show_eye.style.display = "none";
    hide_eye.style.display = "block";
  } else if(pwd.type === "text"){
    pwd.type = "password";
    show_eye.style.display = "block";
    hide_eye.style.display = "none";
  }
}

function confirm_password_show_hide() {
  var cfm_pwd = document.getElementById("confirmPassword");
  var cfm_show_eye = document.getElementById("cfm_show_eye");
  var cfm_hide_eye = document.getElementById("cfm_hide_eye");
  cfm_hide_eye.classList.remove("d-none");
  if (cfm_pwd.type === "password") {
    cfm_pwd.type = "text";
    cfm_show_eye.style.display = "none";
    cfm_hide_eye.style.display = "block";
  } else if(cfm_pwd.type === "text"){
    cfm_pwd.type = "password";
    cfm_show_eye.style.display = "block";
    cfm_hide_eye.style.display = "none";
  }
}

function login_password_show_hide() {
  var lg_pwd = document.getElementById("loginPassword");
  var lg_show_eye = document.getElementById("lg_show_eye");
  var lg_hide_eye = document.getElementById("lg_hide_eye");
  lg_hide_eye.classList.remove("d-none");
  if (lg_pwd.type === "password") {
    lg_pwd.type = "text";
    lg_show_eye.style.display = "none";
    lg_hide_eye.style.display = "block";
  } else if(lg_pwd.type === "text"){
    lg_pwd.type = "password";
    lg_show_eye.style.display = "block";
    lg_hide_eye.style.display = "none";
  }
}

addEventListener("DOMContentLoaded", (event) => {
    const password = document.getElementById("password");
    const passwordAlert = document.getElementById("password-alert");
    const requirements = document.querySelectorAll(".requirements");
    let lengBoolean, bigLetterBoolean, numBoolean, specialCharBoolean;
    let leng = document.querySelector(".leng");
    let bigLetter = document.querySelector(".big-letter");
    let num = document.querySelector(".num");
    let specialChar = document.querySelector(".special-char");
    const specialChars = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?`~";
    const numbers = "0123456789";

    requirements.forEach((element) => element.classList.add("wrong"));

    password.addEventListener("focus", () => {
        passwordAlert.classList.remove("d-none");
        if (!password.classList.contains("is-valid")) {
            password.classList.add("is-invalid");
        }
    });

    password.addEventListener("input", () => {
        let value = password.value;
        if (value.length < 8) {
            lengBoolean = false;
        } else if (value.length > 7) {
            lengBoolean = true;
        }

        if (value.toLowerCase() == value) {
            bigLetterBoolean = false;
        } else {
            bigLetterBoolean = true;
        }

        numBoolean = false;
        for (let i = 0; i < value.length; i++) {
            for (let j = 0; j < numbers.length; j++) {
                if (value[i] == numbers[j]) {
                    numBoolean = true;
                }
            }
        }

        specialCharBoolean = false;
        for (let i = 0; i < value.length; i++) {
            for (let j = 0; j < specialChars.length; j++) {
                if (value[i] == specialChars[j]) {
                    specialCharBoolean = true;
                }
            }
        }

        if (lengBoolean == true && bigLetterBoolean == true && numBoolean == true && specialCharBoolean == true) {
            password.classList.remove("is-invalid");
            password.classList.add("is-valid");

            requirements.forEach((element) => {
                element.classList.remove("wrong");
                element.classList.add("good");
            });
            passwordAlert.classList.remove("alert-warning");
            passwordAlert.classList.add("alert-success");
        } else {
            password.classList.remove("is-valid");
            password.classList.add("is-invalid");

            passwordAlert.classList.add("alert-warning");
            passwordAlert.classList.remove("alert-success");

            if (lengBoolean == false) {
                leng.classList.add("wrong");
                leng.classList.remove("good");
            } else {
                leng.classList.add("good");
                leng.classList.remove("wrong");
            }

            if (bigLetterBoolean == false) {
                bigLetter.classList.add("wrong");
                bigLetter.classList.remove("good");
            } else {
                bigLetter.classList.add("good");
                bigLetter.classList.remove("wrong");
            }

            if (numBoolean == false) {
                num.classList.add("wrong");
                num.classList.remove("good");
            } else {
                num.classList.add("good");
                num.classList.remove("wrong");
            }

            if (specialCharBoolean == false) {
                specialChar.classList.add("wrong");
                specialChar.classList.remove("good");
            } else {
                specialChar.classList.add("good");
                specialChar.classList.remove("wrong");
            }
        }
    });

    password.addEventListener("blur", () => {
        passwordAlert.classList.add("d-none");
    });
});