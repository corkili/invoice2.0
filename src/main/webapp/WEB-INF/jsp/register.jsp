<%--
  Created by IntelliJ IDEA.
  User: 李浩然
  Date: 2017/8/13
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>注册</title>

    <!-- Bootstrap -->
    <link href="../vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="../vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="../vendors/nprogress/nprogress.css" rel="stylesheet">
    <!-- Animate.css -->
    <link href="../vendors/animate.css/animate.min.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="../build/css/custom.min.css" rel="stylesheet">

    <script src="../js/func/validate.js"></script>
    <script src="../js/func/md5.js"></script>
</head>
<body class="login">
<div>
    <div class="login_wrapper">
        <div class="animate form login_form">
            <section class="login_content">
                <form:form commandName="user" action="register" method="post" id="register_form"
                           name="register_form" onsubmit="return checkRegisterForm();">
                    <h1>
                        注册<br/><br/>
                        <strong>企业增值税发票数据分析系统</strong>
                    </h1>
                    <input type="hidden" id="action" name="action" value="register">
                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <div class="col-md-3 col-sm-3 col-xs-3" style="text-align: right">
                            <label class="control-label right" for="email" style="text-align: right">
                                邮箱<span class="required">*</span>
                            </label>
                        </div>
                        <div class="col-md-9 col-sm-9 col-xs-9">
                            <form:input path="email" id="email" name="email" cssErrorClass="error" onchange="checkEmail();"
                                        type="text" cssClass="form-control" placeholder="邮箱：example@example.com" required="required"/>
                        </div>
                    </div>
                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <div class="col-md-3 col-sm-3 col-xs-3" style="text-align: right">
                            <label class="control-label right" for="password" style="text-align: right">
                                密码<span class="required">*</span>
                            </label>
                        </div>
                        <div class="col-md-9 col-sm-9 col-xs-9">
                            <form:input path="password" id="password" name="password" cssErrorClass="error" onchange="checkPassword();"
                                        type="password" cssClass="form-control" placeholder="密码：8-12位，包含数字和字母" required="required"/>
                        </div>
                    </div>
                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <div class="col-md-3 col-sm-3 col-xs-3" style="text-align: right">
                            <label class="control-label right" for="confirmPassword" style="text-align: right">
                                重复密码<span class="required">*</span>
                            </label>
                        </div>
                        <div class="col-md-9 col-sm-9 col-xs-9">
                            <input id="confirmPassword" type="password" onchange="checkConfirmPassword();"
                                   class="form-control" placeholder="再次输入密码" required/>
                        </div>
                    </div>

                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <div class="col-md-3 col-sm-3 col-xs-3" style="text-align: right">
                            <label class="control-label right" for="name" style="text-align: right">
                                真实姓名<span class="required">*</span>
                            </label>
                        </div>
                        <div class="col-md-9 col-sm-9 col-xs-9">
                            <form:input path="name" id="name" name="name" cssErrorClass="error" onchange="checkName();"
                                        type="text" cssClass="form-control" placeholder="请输入姓名" required="required"/>
                        </div>
                    </div>

                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <div class="col-md-3 col-sm-3 col-xs-3" style="text-align: right">
                            <label class="control-label right" for="jobId" style="text-align: right">
                                工号<span class="required">*</span>
                            </label>
                        </div>
                        <div class="col-md-9 col-sm-9 col-xs-9">
                            <form:input path="jobId" id="jobId" name="jobId" cssErrorClass="error"
                                        type="text" cssClass="form-control" placeholder="请输入工号" required="required"/>
                        </div>
                    </div>

                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <div class="col-md-3 col-sm-3 col-xs-3" style="text-align: right">
                            <label class="control-label right" for="phone" style="text-align: right">
                                手机号<span class="required">*</span>
                            </label>
                        </div>
                        <div class="col-md-9 col-sm-9 col-xs-9">
                            <form:input path="phone" id="phone" name="phone" cssErrorClass="error" onchange="checkPhone();"
                                        type="text" cssClass="form-control" placeholder="请输入11位手机号" required="required"/>
                        </div>
                    </div>

                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <div class="col-md-4 col-sm-4 col-xs-4">
                            <div class="col-md-4 col-sm-4 col-xs-4">
                                <img id="captchaImage" src="captcha" onclick="reImg()"/>
                            </div>
                        </div>
                        <div class="col-md-1 col-sm-1 col-xs-1"></div>
                        <div class="col-md-7 col-sm-7 col-xs-7">
                            <input id="captcha" name="captcha" class="form-control" onchange="checkVerificationCode();"
                                   maxlength="6" placeholder="请输入验证码" required/>
                        </div>
                    </div>
                    <script type="text/javascript">
                        function reImg(){
                            var img = document.getElementById("captchaImage");
                            img.src = "captcha?timestamp=" + (new Date()).valueOf();
                        }
                    </script>
                    <div class="form-group col-md-12 col-sm-12 col-xs-12">
                        <input id="submit" type="submit" class="btn btn-default submit"
                               value="注册">
                        <input id="reset" type="reset" class="btn btn-default submit"
                               value="重置">
                    </div>
                    <c:if test="${has_error}">
                        <div class="col-md-12 col-sm-12 col-xs-12 has-error">
                            <p style="color: red">${error_message}</p>
                        </div>
                    </c:if>
                    <div class="clearfix"></div>

                    <div class="separator">
                        <p class="change_link">已有账号？
                            <a href="login" class="to_register">点此登录</a>
                        </p>
                        <div class="clearfix"></div>
                        <br />
                        <div>
                            <h1><i class="fa fa-paw"></i> SCU-H.L.D.</h1>
                            <p>©2017 All Rights Reserved. SCU-H.L.D.!</p>
                        </div>
                    </div>
                </form:form>
            </section>
        </div>
    </div>
</div>
</body>
</html>


