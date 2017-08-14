function $$(id) {
    return  document.getElementById(id);
}

var defaultTextColor = "#888888";

function checkRegisterForm() {
    if (checkEmail() && checkPassword() && checkConfirmPassword()
            && checkName() && checkPhone() && checkVerificationCode()) {
        return true;
    } else {
        alert("某些信息输入不正确，请检查红字部分！");
        return false;
    }
}

function checkResetPasswordForm() {
    if (checkEmail() && checkCode() && checkPassword() && checkConfirmPassword() && checkVerificationCode()) {
        return true;
    } else {
        alert("某些信息输入不正确，请检查红字部分！");
        return false;
    }
}

function checkModifyInformationForm() {
    if (checkName() && checkPhone() && checkProvince() && checkCity() && checkArea()) {
        return true;
    } else {
        alert("某些信息输入不正确，请检查红字部分！");
        return false;
    }
}

function checkEmail() {
    var reg = /^\s*\w+(?:\.{0,1}[\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\.[a-zA-Z]+\s*$/;
    var emailStr = $$('email').value;
    if (emailStr != null && emailStr != '' && reg.test(emailStr)) {
        $$('email').style.color=defaultTextColor;
        return true;
    } else {
        $$('email').style.color="#FF0000";
        return false;
    }
}

function checkPassword() {
    var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,24}$/;
    var first = $$('password').value;
    var second = $$('confirmPassword').value;
    if (reg.test(second) && first == second) {
        $$('password').style.color = defaultTextColor;
        $$('confirmPassword').style.color = defaultTextColor;
        return true;
    } else {
        $$('password').style.color="#FF0000";
        return false;
    }
}

function checkConfirmPassword() {
    var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,24}$/;
    var first = $$('password').value;
    var second = $$('confirmPassword').value;
    if (reg.test(second) && first == second) {
        $$('password').style.color = defaultTextColor;
        $$('confirmPassword').style.color = defaultTextColor;
        return true;
    } else {
        $$('confirmPassword').style.color="#FF0000";
        return false;
    }
}

function checkName() {
    var nameStr = $$('name').value;
    if (nameStr.length > 0) {
        $$('name').style.color=defaultTextColor;
        return true;
    } else {
        $$('name').style.color="#FF0000";
        return false;
    }
}

function checkPhone() {
    var phoneStr = $$('phone').value;
    var reg = /^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\d{8}$/;
    if (phoneStr.length == 11 && reg.test(phoneStr)) {
        $$('phone').style.color=defaultTextColor;
        return true;
    } else {
        $$('phone').style.color="#FF0000";
        return false;
    }
}

function checkVerificationCode() {
    var reg = /^[0-9a-zA-Z]*$/;
    var captchaStr =  $$('captcha').value;
    if (captchaStr.length == 6 && reg.test(captchaStr)) {
        $$('captcha').style.color=defaultTextColor;
        return true;
    } else {
        $$('captcha').style.color="#FF0000";
        return false;
    }
}
