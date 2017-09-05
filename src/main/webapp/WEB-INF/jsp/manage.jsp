<%--
  Created by IntelliJ IDEA.
  User: 李浩然
  Date: 2017/8/16
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>用户管理</title>

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

    <script src="../js/func/validate.js"></script>
    <script type="text/javascript">
        function displayAuthForm() {
            document.getElementById('authDiv').style.display='';
            document.getElementById('authBtn').disabled = 'disabled';
        }

        function hideAuthForm() {
            document.getElementById('authDiv').style.display='none';
            document.getElementById('authBtn').disabled = '';
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
                        <h3>用户权限管理</h3>
                    </div>
                </div>


                <c:choose>
                    <c:when test="${has_authority}">
                        <form action="manage" method="post" onsubmit="return checkAuthForm();">
                            <div class="row" id="authDiv" style="display: none;">
                                <div class="col-md-12 col-sm-12 col-xs-12">
                                    <div class="x_panel">
                                        <div class="x_title">
                                            <h2>权限设置</h2>
                                            <ul class="nav navbar-right panel_toolbox">
                                                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                                </li>
                                            </ul>
                                            <div class="clearfix"></div>
                                        </div>
                                        <div class="x_content">
                                            <input type="checkbox" name="authority" value="addInvoice" class="flat">添加发票&nbsp;&nbsp;
                                            <input type="checkbox" name="authority" value="queryInvoice" class="flat">查询发票&nbsp;&nbsp;
                                            <input type="checkbox" name="authority" value="modifyInvoice" class="flat">修改发票&nbsp;&nbsp;
                                            <input type="checkbox" name="authority" value="removeInvoice" class="flat">删除发票&nbsp;&nbsp;
                                            <input type="checkbox" name="authority" value="queryReport" class="flat">查询报表&nbsp;&nbsp;
                                            <input type="checkbox" name="authority" value="queryRecord" class="flat">查看日志&nbsp;&nbsp;
                                            <c:if test="${user.isSuperManager}">
                                                <input type="checkbox" name="authority" value="manager" class="flat">设置为管理员
                                            </c:if>
                                            <br/>
                                            <div class="ln_solid"></div>
                                            <input type="submit" class="btn btn-primary" value="提交">
                                            <input type="button" class="btn btn-dark" value="取消" onclick="hideAuthForm();">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12 col-sm-12 col-xs-12">
                                    <div class="x_panel">
                                        <div class="x_title">
                                            <h2>用户列表</h2>
                                            <ul class="nav navbar-right panel_toolbox">
                                                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                                </li>
                                            </ul>
                                            <div class="clearfix"></div>
                                        </div>
                                        <div class="x_content">
                                            <div class="table-responsive">
                                                <div>
                                                    <button id="authBtn" class="btn btn-success" style="margin-right: 5px;" type="button"
                                                            onclick="displayAuthForm()">设置权限</button>
                                                    <br/>
                                                </div>
                                                <table id="datatable" class="table table-striped jambo_table" style="white-space: nowrap;">
                                                    <thead>
                                                    <tr class="headings">
                                                        <th></th>
                                                        <th class="column-title">姓名</th>
                                                        <th class="column-title">邮箱</th>
                                                        <th class="column-title">手机</th>
                                                        <th class="column-title">工号</th>
                                                        <th class="column-title">身份</th>
                                                        <th class="column-title">添加发票</th>
                                                        <th class="column-title">查询发票</th>
                                                        <th class="column-title">修改发票</th>
                                                        <th class="column-title">删除发票</th>
                                                        <th class="column-title">查询报表</th>
                                                        <th class="column-title">查看日志</th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="user" items="${user_list}" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${status.index % 2 == 0}">
                                                                <tr class="even pointer">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <tr class="odd pointer">
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <td class="a-center ">
                                                            <input type="checkbox" class="flat" name="table_records" value="${user.id}" checked>
                                                        </td>
                                                        <td class=" ">${user.name}</td>
                                                        <td class=" ">${user.email}</td>
                                                        <td class=" ">${user.phone}</td>
                                                        <td class=" ">${user.jobId}</td>
                                                        <td class=" ">
                                                            <c:choose>
                                                                <c:when test="${user.isSuperManager}">
                                                                    <span style="color: #a5d24a;">超级管理员</span>
                                                                </c:when>
                                                                <c:when test="${user.isManager}">
                                                                    <span style="color: #e9322d">管理员</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span style="color: #0e90d2">普通用户</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class=" ">
                                                            <c:choose>
                                                                <c:when test="${user.authority.addInvoice}">
                                                                    <span class="fa fa-star"></span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="fa fa-star-o"></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class=" ">
                                                            <c:choose>
                                                                <c:when test="${user.authority.queryInvoice}">
                                                                    <span class="fa fa-star"></span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="fa fa-star-o"></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class=" ">
                                                            <c:choose>
                                                                <c:when test="${user.authority.modifyInvoice}">
                                                                    <span class="fa fa-star"></span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="fa fa-star-o"></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class=" ">
                                                            <c:choose>
                                                                <c:when test="${user.authority.removeInvoice}">
                                                                    <span class="fa fa-star"></span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="fa fa-star-o"></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class=" ">
                                                            <c:choose>
                                                                <c:when test="${user.authority.queryReport}">
                                                                    <span class="fa fa-star"></span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="fa fa-star-o"></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class=" ">
                                                            <c:choose>
                                                                <c:when test="${user.authority.queryRecord}">
                                                                    <span class="fa fa-star"></span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="fa fa-star-o"></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </form>
                        <div class="clearfix"></div>

                    </c:when>
                    <c:otherwise>
                        <%@ include file="no_authority.jspf"%>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
        <!-- /page content -->

        <!-- footer content -->
        <footer>
            <div class="pull-right">

            </div>
            <div class="clearfix"></div>
        </footer>
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
<!-- iCheck -->
<script src="../vendors/iCheck/icheck.min.js"></script>
<!-- Datatables -->
<script src="../vendors/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="../vendors/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="../vendors/datatables.net-buttons/js/dataTables.buttons.min.js"></script>
<script src="../vendors/datatables.net-buttons-bs/js/buttons.bootstrap.min.js"></script>
<script src="../vendors/datatables.net-buttons/js/buttons.flash.min.js"></script>
<script src="../vendors/datatables.net-buttons/js/buttons.html5.min.js"></script>
<script src="../vendors/datatables.net-buttons/js/buttons.print.min.js"></script>
<script src="../vendors/datatables.net-fixedheader/js/dataTables.fixedHeader.min.js"></script>
<script src="../vendors/datatables.net-keytable/js/dataTables.keyTable.min.js"></script>
<script src="../vendors/datatables.net-responsive/js/dataTables.responsive.min.js"></script>
<script src="../vendors/datatables.net-responsive-bs/js/responsive.bootstrap.js"></script>
<script src="../vendors/datatables.net-scroller/js/dataTables.scroller.min.js"></script>
<script src="../vendors/jszip/dist/jszip.min.js"></script>
<script src="../vendors/pdfmake/build/pdfmake.min.js"></script>
<script src="../vendors/pdfmake/build/vfs_fonts.js"></script>

<!-- Custom Theme Scripts -->
<script src="../build/js/custom.min.js"></script>

<script type="text/javascript">
    var box = document.getElementsByName("table_records");
    for (var i = 0; i < box.length; i++) {
        box[i].checked = false;
    }
</script>

</body>
</html>
