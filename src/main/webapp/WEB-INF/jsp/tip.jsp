<%--
  Created by IntelliJ IDEA.
  User: 李浩然
  Date: 2017/8/14
  Time: 14:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <title>提示</title>

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

    <script src="../js/func/update.js"></script>
</head>

<body class="login">
<div>
    <div class="login_wrapper">
        <div class="animate form login_form">
            <section class="login_content">
                <h1>
                    提示<br/><br/>
                    <strong>企业增值税发票数据分析系统</strong>
                </h1>
                <div class="col-md-12 col-sm-12 col-xs-12 has-error" style="font-size: 20px;">
                    <br/>
                    <p>${message}</p>
                    <span id="show"></span><a href="${url}" style="color: #0e90d2"><u>点此立即跳转</u></a>
                    <script>
                        Load('${url}', "秒后自动跳转...");
                    </script>
                </div>
                <div class="separator">
                    <div class="clearfix"></div>
                    <br />
                    <div>
                        <h1><i class="fa fa-paw"></i> SCU-H.L.D.</h1>
                        <p>©2017 All Rights Reserved. SCU-H.L.D.!</p>
                    </div>
                </div>
            </section>
        </div>
    </div>
</div>
</body>
</html>
