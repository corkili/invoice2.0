<%--
  Created by IntelliJ IDEA.
  User: 李浩然
  Date: 2017/8/16
  Time: 19:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>企业增值税发票数据分析系统</title>

    <!-- Bootstrap -->
    <link href="../vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="../vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="../vendors/nprogress/nprogress.css" rel="stylesheet">
    <!-- iCheck -->
    <link href="../vendors/iCheck/skins/flat/green.css" rel="stylesheet">
    <!-- bootstrap-wysiwyg -->
    <link href="../vendors/google-code-prettify/bin/prettify.min.css" rel="stylesheet">
    <!-- Select2 -->
    <link href="../vendors/select2/dist/css/select2.min.css" rel="stylesheet">
    <!-- Switchery -->
    <link href="../vendors/switchery/dist/switchery.min.css" rel="stylesheet">
    <!-- starrr -->
    <link href="../vendors/starrr/dist/starrr.css" rel="stylesheet">
    <!-- bootstrap-daterangepicker -->
    <link href="../vendors/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="../build/css/custom.min.css" rel="stylesheet">

    <style>
        .file {
            position: relative;
            display: inline-block;
            background: #D0EEFF;
            border: 1px solid #99D3F5;
            border-radius: 4px;
            padding: 4px 12px;
            overflow: hidden;
            color: #1E88C7;
            text-decoration: none;
            text-indent: 0;
            line-height: 20px;
        }
        .file input {
            position: absolute;
            font-size: 100px;
            right: 0;
            top: 0;
            opacity: 0;
        }
        .file:hover {
            background: #AADFFD;
            border-color: #78C3F3;
            color: #004974;
            text-decoration: none;
        }
    </style>

    <script type="text/javascript">
        //定义id选择器
        function Id(id){
            return document.getElementById(id);
        }
        //入口函数，两个参数分别为<input type='file'/>的id，还有一个就是图片的id，然后会自动根据文件id得到图片，然后把图片放到指定id的图片标签中
        function changeToop(fileid,imgid){
            var file = Id(fileid);
            if(file.value==''){
                //设置默认图片
                Id("myimg").src='';
            }else{
                preImg(fileid,imgid);
            }
        }
        //获取input[file]图片的url Important
        function getFileUrl(fileId) {
            var url;
            var file = Id(fileId);
            var agent = navigator.userAgent;
            if (agent.indexOf("MSIE")>=1) {
                url = file.value;
            } else if(agent.indexOf("Firefox")>0) {
                url = window.URL.createObjectURL(file.files.item(0));
            } else if(agent.indexOf("Chrome")>0) {
                url = window.URL.createObjectURL(file.files.item(0));
            }
            var extIndex = file.value.lastIndexOf(".");
            var ext = file.value.substring(extIndex,file.value.length).toUpperCase();
            if (ext != ".PNG") {
                alert("只允许上传PNG格式的图片！");
                Id('upload_file').disabled = true;
                return "";
            }
            Id('upload_file').disabled = false;
            return url;
        }
        //读取图片后预览
        function preImg(fileId,imgId) {
            var imgPre =Id(imgId);
            imgPre.src = getFileUrl(fileId);
        }
    </script>
</head>

<body class="nav-md">
<div class="container body">
    <div class="main_container">
        <%@ include file="left_top.jspf"%>

        <!-- top navigation -->
        <%@ include file="right_top.jspf"%>
        <!-- /top navigation -->

        <!-- page content -->
        <div class="right_col" role="main">
            <div class="">
                <div class="page-title">
                    <div class="title_left">
                        <h3>企业增值税发票数据分析系统</h3>
                    </div>
                </div>

                <div class="clearfix"></div>

                <div class="row">
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <div class="x_panel">
                            <div class="x_title">
                                <h2>个人信息修改<small>${user.name}</small></h2>
                                <ul class="nav navbar-right panel_toolbox">
                                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                    </li>
                                </ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">

                                <!-- start form for validation -->
                                <form action="modifyInformation" method="post" id="modify_info_form"
                                      name="modify_info_form" onsubmit="return checkModifyInformationForm();">
                                    <label for="email">邮箱 <span class="required">*</span> :</label>
                                    <input id="email" class="form-control" name="email" value="${user.email}"
                                           onchange="checkEmail()" required disabled />

                                    <label for="name">姓名 <span class="required">*</span> :</label>
                                    <input id="name" class="form-control" name="name" value="${user.name}"
                                           onchange="checkName()" required />

                                    <label for="jobId">工号 <span class="required">*</span> :</label>
                                    <input id="jobId" class="form-control" name="jobId" value="${user.jobId}" required />

                                    <label for="phone">手机号 <span class="required">*</span> :</label>
                                    <input id="phone" class="form-control" name="phone" value="${user.phone}"
                                                data-parsley-trigger="change" onchange="checkPhone()" required/>

                                    <br/>
                                    <input type="submit" class="btn btn-primary" value="保存"/>
                                    <c:if test="${has_error}">
                                        <br/>
                                        <p style="color: red">${error_message}</p>
                                    </c:if>
                                </form>
                                <!-- end form for validations -->

                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <div class="x_panel">
                            <div class="x_title">
                                <h2>头像修改<small>${user.name}</small></h2>
                                <ul class="nav navbar-right panel_toolbox">
                                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                    </li>
                                </ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">

                                <!-- start form for validation -->
                                <form action="modifyHeadImage" method="post"
                                      enctype="multipart/form-data" class="form-horizontal">
                                    <div class="ln_solid"></div>
                                    <div class="form-group">
                                        <div class="col-md-5 col-sm-5 col-xs-5">
                                            <a class="file">点此上传一张图片
                                                <input type="file" name="image" id="file_selector" class="btn btn-round"
                                                       placeholder="点此上传一张图片" required
                                                       accept=".png" onchange="changeToop('file_selector', 'imagePreview');">
                                            </a>
                                        </div>
                                        <div class="col-md-1  col-sm-1 col-xs-1">
                                            <input type="submit" value="保存"
                                                   class="btn btn-round btn-success" id="upload_file" onclick="return checkInput(this.form);">
                                            <script>
                                                function checkInput(form) {
                                                    if (form.image.value == null || form.image.value == '') {
                                                        alert("请选择一个文件");
                                                        return false;
                                                    }
                                                    return true;
                                                }
                                            </script>
                                        </div>
                                    </div>
                                    <div class="ln_solid"></div>
                                    <div id="preview" class="col-sm-5 col-md-5 col-xs-5">
                                        <img id="imagePreview" src="" alt="" class="img-circle img-responsive">
                                    </div>
                                </form>
                                <c:if test="${image_error}">
                                    <script>
                                        alert('${error_message}');
                                    </script>
                                </c:if>
                                <!-- end form for validations -->

                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <!-- /page content -->

        <!-- footer content -->
        <%@ include file="footer.jspf"%>
        <!-- /footer content -->
    </div>
</div>

<!-- jQuery -->
<script src="../vendors/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap -->
<script src="../vendors/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- FastClick -->
<script src="../vendors/fastclick/lib/fastclick.js"></script>
<!-- NProgress -->
<script src="../vendors/nprogress/nprogress.js"></script>
<!-- bootstrap-progressbar -->
<script src="../vendors/bootstrap-progressbar/bootstrap-progressbar.min.js"></script>
<!-- iCheck -->
<script src="../vendors/iCheck/icheck.min.js"></script>
<!-- bootstrap-wysiwyg -->
<script src="../vendors/bootstrap-wysiwyg/js/bootstrap-wysiwyg.min.js"></script>
<script src="../vendors/jquery.hotkeys/jquery.hotkeys.js"></script>
<script src="../vendors/google-code-prettify/src/prettify.js"></script>
<!-- jQuery Tags Input -->
<script src="../vendors/jquery.tagsinput/src/jquery.tagsinput.js"></script>
<!-- Switchery -->
<script src="../vendors/switchery/dist/switchery.min.js"></script>
<!-- Select2 -->
<script src="../vendors/select2/dist/js/select2.full.min.js"></script>
<!-- Parsley -->
<script src="../vendors/parsleyjs/dist/parsley.min.js"></script>
<!-- Autosize -->
<script src="../vendors/autosize/dist/autosize.min.js"></script>
<!-- jQuery autocomplete -->
<script src="../vendors/devbridge-autocomplete/dist/jquery.autocomplete.min.js"></script>
<!-- starrr -->
<script src="../vendors/starrr/dist/starrr.js"></script>
<!-- Custom Theme Scripts -->
<script src="../build/js/custom.min.js"></script>
<script src="../js/func/validate.js"></script>
</body>
</html>
