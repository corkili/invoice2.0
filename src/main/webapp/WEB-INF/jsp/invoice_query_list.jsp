<%--
  Created by IntelliJ IDEA.
  User: 李浩然
  Date: 2017/5/25
  Time: 13:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <title>列表查询</title>

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

    <script>
        function viewInvoice(index, invoice_id){
            var form = document.createElement("form");
            form.action = "view_invoice";
            form.method = "post";
            form.style.display = "none";
            var opt1 = document.createElement("input");
            opt1.name = "index";
            opt1.value = index;
            var opt2 = document.createElement("input");
            opt2.name = "invoice_id";
            opt2.value =  invoice_id;
            form.appendChild(opt1);
            form.appendChild(opt2);
            document.body.appendChild(form);
            form.submit();
            return form;
        }

        function delInvoice(index, invoice_id){
            var form = document.createElement("form");
            form.action = "del_invoice";
            form.method = "post";
            form.style.display = "none";
            var opt1 = document.createElement("input");
            opt1.name = "index";
            opt1.value = index;
            var opt2 = document.createElement("input");
            opt2.name = "invoice_id";
            opt2.value =  invoice_id;
            form.appendChild(opt1);
            form.appendChild(opt2);
            document.body.appendChild(form);
            form.submit();
            return form;
        }
    </script>

    <script src="../js/func/checkInvoice.js"></script>
    <script src="../js/func/validate.js"></script>
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
                        <h3>列表查询</h3>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${has_authority}">
                        <div class="clearfix"></div>

                        <!-- query form -->

                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>查询条件</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>

                                    <div class="x_content">
                                        <form action="queryForList" method="post" class="form-horizontal form-label-left" onsubmit="return checkQueryForm()">
                                            <input type="hidden" name="preAction" value="queryForList">
                                            <input type="hidden" name="action" value="query">
                                            <div class="row">
                                                <div class="form-group">
                                                    <label class="control-label col-md-2" for="selfName">
                                                        本方单位名称
                                                        <span class="required">*</span>
                                                    </label>
                                                    <div class="col-md-4">
                                                        <select class="form-control" name="selfName" id="selfName">
                                                            <option value="0" selected="selected">---请选择---</option>
                                                            <c:forEach var="company" items="${companys}">
                                                                <option value="${company.key}">${company.key}(${company.value})</option>
                                                            </c:forEach>
                                                        </select>
                                                        <%--<input class="form-control has-feedback-left"
                                                               id="selfName" name="selfName" placeholder="必填" required="required"/>--%>
                                                        <%--<span class="fa fa-user form-control-feedback left" aria-hidden="true"></span>--%>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label col-md-2" for="itName">
                                                        他方单位名称
                                                    </label>
                                                    <div class="col-md-4">
                                                        <%--<input class="form-control has-feedback-left"
                                                               id="itName" name="itName" placeholder="不填则表示全部"/>--%>
                                                            <select class="form-control" name="itName" id="itName">
                                                                <option value="" selected="selected">---全部---</option>
                                                                <c:forEach var="company" items="${companys}">
                                                                    <option value="${company.key}">${company.key}(${company.value})</option>
                                                                </c:forEach>
                                                            </select>
                                                        <%--<span class="fa fa-user form-control-feedback left" aria-hidden="true"></span>--%>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label col-md-2" for="reportrange_right">
                                                        日期范围
                                                        <span class="required">*</span>
                                                    </label>
                                                    <div class="col-md-6">
                                                        <div id="reportrange_right" class="pull-left" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc">
                                                            <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                                                            <span>请选择日期范围</span> <b class="caret"></b>
                                                        </div>
                                                        <input type="hidden" id="startDate" name="startDate" required  />
                                                        <input type="hidden" id="endDate" name="endDate" required  />
                                                        <script type="text/javascript">
                                                            function setDateRange(range) {
                                                                var dates = range.split('-');
                                                                if (dates.length != 2) {
                                                                    document.getElementById('startDate').value = '';
                                                                    document.getElementById('endDate').value = '';
                                                                } else {
                                                                    document.getElementById('startDate').value = trim(dates[0]);
                                                                    document.getElementById('endDate').value = trim(dates[1]);
                                                                }
                                                                alert(range + '=' + document.getElementById('startDate').value + ' - ' + document.getElementById('endDate').value);
                                                            }
                                                            function trim(str){ //删除左右两端的空格
                                                                return str.replace(/(^\s*)|(\s*$)/g, "");
                                                            }
                                                        </script>
                                                    </div>

                                                </div>

                                                <div class="ln_solid"></div>
                                                <div class="form-group">
                                                    <div class="col-md-5 col-md-offset-3">
                                                        <input type="reset" class="btn btn-primary" value="重置">
                                                        <input type="submit" class="btn btn-success" value="查询">
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <c:if test="${edit_invoice}">
                            <div class="row" id="editInvoiceRow">
                                <div class="col-md-12 col-sm-12 col-xs-12">
                                    <div class="x_panel">
                                        <div class="x_title">
                                            <h2>增值税专用发票</h2>
                                            <ul class="nav navbar-right panel_toolbox">
                                                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                                <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                                            </ul>
                                            <div class="clearfix"></div>
                                        </div>
                                        <div class="x_content">
                                            <form:form commandName="invoice" action="queryForList" method="post"
                                                       cssClass="form-horizontal form-label-left" onsubmit="return checkSubmitInvoice(${detail_num});">
                                                <form:hidden path="id" id="id" name="id"/>
                                                <input type="hidden" name="preAction" value="queryForList">
                                                <input type="hidden" name="action" value="save">
                                                <div class="row">
                                                    <c:if test="${has_errors}">
                                                        <div class="form-group">
                                                            <p style="color: red;text-align: left; padding-left: 5%">
                                                                <strong>发票信息有误，请更改后重新提交！</strong>
                                                                <c:forEach var="error_message" items="${error_messages}">
                                                                    <br/>${error_message}
                                                                </c:forEach>
                                                            </p>
                                                        </div>
                                                        <div class="ln_solid"></div>
                                                    </c:if>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-2" for="invoiceCode">
                                                            发票代码
                                                            <span class="required">*</span>
                                                        </label>
                                                        <div class="col-md-2">
                                                            <form:input path="invoiceCode" id="invoiceCode" name="invoiceCode" maxlength="10" minlength="10"
                                                                        cssClass="form-control col-md-2" required="required" placeholder="请输入10位发票代码"
                                                                        value="${invoice.invoiceCode}" onchange="checkInvoiceCode();"/>
                                                        </div>
                                                        <label class="control-label col-md-2" for="invoiceId">
                                                            发票号码
                                                            <span class="required">*</span>
                                                        </label>
                                                        <div class="col-md-2">
                                                            <form:input path="invoiceId" id="invoiceId" name="invoiceId" maxlength="8" minlength="8"
                                                                        cssClass="form-control col-md-2" required="required" placeholder="请输入8位发票号码"
                                                                        value="${invoice.invoiceId}" onchange="checkInvoiceId();"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-6" for="single_cal4">
                                                            发票日期
                                                            <span class="required">*</span>
                                                        </label>
                                                        <div class="col-md-2">
                                                            <form:hidden path="invoiceDate" id="invoiceDate" name="invoiceDate" cssClass="form-control col-md-2 has-feedback-left"
                                                                         aria-describedby="inputSuccess2Status4" required="required"  />
                                                            <fieldset>
                                                                <div class="control-group">
                                                                    <div class="controls">
                                                                        <div class="xdisplay_inputx form-group has-feedback">
                                                                            <input type="text" class="form-control has-feedback-left" onchange="formatDate(this.value);"
                                                                                   id="single_cal4" aria-describedby="inputSuccess2Status4"/>
                                                                            <span class="fa fa-calendar-o form-control-feedback left"></span>
                                                                            <span id="inputSuccess2Status4" class="sr-only">(success)</span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </fieldset>
                                                            <script>
                                                                function formatDate(value) {
                                                                    if (value.indexOf("-") < 0) {
                                                                        var tmp = value.split("/");
                                                                        var date = tmp[2] + "-" + tmp[0] + "-" + tmp[1];
                                                                        document.getElementById('invoiceDate').value = date;
                                                                        document.getElementById('single_cal4').value = date;
                                                                    } else {
                                                                        document.getElementById('invoiceDate').value = value;
                                                                    }
                                                                }
                                                            </script>
                                                        </div>
                                                    </div>
                                                    <div class="ln_solid"></div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-2" for="buyerName">
                                                            （购贷单位）名称
                                                            <span class="required">*</span>
                                                        </label>
                                                        <div class="col-md-6">
                                                            <form:input path="buyerName" id="buyerName" name="buyerName"
                                                                        cssClass="form-control col-md-6" required="required"
                                                                        value="${invoice.buyerName}" placeholder="请输入购贷方名称"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-2" for="buyerId">
                                                            （购贷单位）纳税人识别号
                                                        </label>
                                                        <div class="col-md-6">
                                                            <form:input path="buyerId" id="buyerId" name="buyerId"
                                                                        cssClass="form-control col-md-6"
                                                                        value="${invoice.buyerId}" placeholder="请输入购贷方纳税人识别号（可空）"/>
                                                        </div>
                                                    </div>
                                                    <div class="ln_solid"></div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-1">产品名称<span class="required">*</span></label>
                                                        <label class="control-label col-md-1">规格型号</label>
                                                        <label class="control-label col-md-1">单位</label>
                                                        <label class="control-label col-md-1">数量<span class="required">*</span></label>
                                                        <label class="control-label col-md-1">单价<span class="required">*</span></label>
                                                        <label class="control-label col-md-1">金额<span class="required">*</span></label>
                                                        <label class="control-label col-md-1">税率（小数）<span class="required">*</span></label>
                                                        <label class="control-label col-md-1">税额<span class="required">*</span></label>
                                                    </div>

                                                    <c:forEach var="i" begin="0" end="${detail_num-1}" step="1">
                                                        <div class="form-group">
                                                            <form:hidden path="details[${i}].detailId"/>
                                                            <form:hidden path="details[${i}].invoiceId"/>
                                                            <form:hidden path="details[${i}].invoiceCode"/>
                                                            <div class="col-md-1">
                                                                <form:input path="details[${i}].detailName" placeholder="产品名称"
                                                                            cssClass="form-control col-md-1" required="required"/>
                                                            </div>
                                                            <div class="col-md-1">
                                                                <form:input path="details[${i}].specification" placeholder="规格型号（可空）"
                                                                            cssClass="form-control col-md-1"/>
                                                            </div>
                                                            <div class="col-md-1">
                                                                <form:input path="details[${i}].unitName" placeholder="单位（可空）"
                                                                            cssClass="form-control col-md-1"/>
                                                            </div>
                                                            <div class="col-md-1">
                                                                <form:input path="details[${i}].quantity" placeholder="数量（整数）" name="quantity"
                                                                            id="quantity_${i}"
                                                                            cssClass="form-control col-md-1" required="required" onchange="checkQuantity(this);"/>
                                                            </div>
                                                            <div class="col-md-1">
                                                                <form:input path="details[${i}].unitPrice" placeholder="单价" onchange="checkMoney(this);" name="unitPrice"
                                                                            cssClass="form-control col-md-1" required="required" id="unitPrice_${i}"/>
                                                            </div>
                                                            <div class="col-md-1">
                                                                <form:input path="details[${i}].amount"  placeholder="金额" disabled="false" name="amount" id="amount_${i}"
                                                                            cssClass="form-control col-md-1" required="required" onchange="checkMoney(this);"/>
                                                            </div>
                                                            <div class="col-md-1">
                                                                <form:input path="details[${i}].taxRate" placeholder="税率" onchange="checkRate(this);" name="taxRate"
                                                                            cssClass="form-control col-md-1" required="required" id="taxRate_${i}"/>
                                                            </div>
                                                            <div class="col-md-1">
                                                                <form:input path="details[${i}].taxSum" placeholder="税额" disabled="false" name="taxSum" id="taxSum_${i}"
                                                                            cssClass="form-control col-md-1" required="required" onchange="checkMoney(this);"/>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                    <div class="ln_solid"></div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-5" for="totalAmount">
                                                            合计金额<span class="required">*</span>
                                                        </label>
                                                        <div class="col-md-1">
                                                            <form:input path="totalAmount" id="totalAmount" name="totalAmount" disabled="false"
                                                                        cssClass="form-control col-md-1" required="required" onchange="checkMoney(this);"
                                                                        value="${invoice.totalAmount}" placeholder="总金额"/>
                                                        </div>
                                                        <label class="control-label col-md-1" for="totalTax">
                                                            合计税额<span class="required">*</span>
                                                        </label>
                                                        <div class="col-md-1">
                                                            <form:input path="totalTax" id="totalTax" name="totalTax" disabled="false"
                                                                        cssClass="form-control col-md-1" required="required" onchange="checkMoney(this);"
                                                                        value="${invoice.totalTax}" placeholder="总税额"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-6" for="total">
                                                            税价合计<span class="required">*</span>
                                                        </label>
                                                        <div class="col-md-2">
                                                            <form:input path="total" id="total" name="total" disabled="false"
                                                                        cssClass="form-control col-md-2" required="required" onchange="checkMoney(this);"
                                                                        value="${invoice.total}" placeholder="税价合计金额"/>
                                                        </div>
                                                    </div>
                                                    <div class="ln_solid"></div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-2" for="sellerName">
                                                            （销贷单位）名称
                                                            <span class="required">*</span>
                                                        </label>
                                                        <div class="col-md-6">
                                                            <form:input path="sellerName" id="sellerName" name="sellerName"
                                                                        cssClass="form-control col-md-6" required="required"
                                                                        value="${invoice.sellerName}" placeholder="请输入销贷方名称"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-2" for="sellerId">
                                                            （销贷单位）纳税人识别号
                                                        </label>
                                                        <div class="col-md-6">
                                                            <form:input path="sellerId" id="sellerId" name="sellerId"
                                                                        cssClass="form-control col-md-6"
                                                                        value="${invoice.sellerId}" placeholder="请输入销贷方纳税人识别号（可空）"/>
                                                        </div>
                                                    </div>
                                                    <div class="ln_solid"></div>
                                                    <div class="form-group">
                                                        <label class="control-label col-md-2" for="remark">备注</label>
                                                        <div class="col-md-6">
                                                            <form:input path="remark" id="remark" name="remark"
                                                                        cssClass="form-control col-md-6"
                                                                        value="${invoice.remark}" placeholde="请输入备注（可空）"/>
                                                        </div>
                                                    </div>
                                                    <div class="ln_solid"></div>
                                                    <div class="form-group">
                                                        <div class="col-md-5 col-md-offset-3">
                                                            <input type="submit" class="btn btn-success" value="保存修改">
                                                            <input type="button" class="btn btn-dark" value="取消"
                                                                   onclick="document.getElementById('editInvoiceRow').style.display = 'none';">
                                                        </div>
                                                    </div>
                                                </div>
                                            </form:form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${view_invoice}">
                            <div class="row">
                                <div class="col-md-12 col-sm-12 col-xs-12">
                                    <div class="x_panel">
                                        <div class="x_title">
                                            <h2>增值税专用发票</h2>
                                            <ul class="nav navbar-right panel_toolbox">
                                                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                                </li>
                                                <li><a class="close-link"><i class="fa fa-close"></i></a>
                                                </li>
                                            </ul>
                                            <div class="clearfix"></div>
                                        </div>
                                        <div class="x_content">

                                            <section class="content invoice">
                                                <!-- title row -->

                                                <%@include file="invoice_element.jspf"%>

                                                <div class="row no-print">
                                                    <div class="col-md-12 col-sm-12 col-xs-12">
                                                        <div class="col-md-offset-10 col-sm-offset-10 col-xs-offset-10 col-md-1 col-sm-1 col-xs-1">
                                                            <form action="queryForList" method="post">
                                                                <input type="hidden" name="id" value="${invoice.id}">
                                                                <input type="hidden" name="preAction" value="queryForList">
                                                                <input type="hidden" name="action" value="edit">
                                                                <input type="submit" value="编辑"
                                                                       class="btn btn-round btn-primary pull-right">
                                                            </form>

                                                        </div>
                                                        <div class="col-md-1 col-sm-1 col-xs-1">
                                                            <form action="queryForList" method="post">
                                                                <input type="hidden" name="id" value="${invoice.id}">
                                                                <input type="hidden" name="preAction" value="queryForList">
                                                                <input type="hidden" name="action" value="delete">
                                                                <input type="submit" value="删除"
                                                                       class="btn btn-round btn-danger pull-right" onclick="return confirm('确定删除发票？');">
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </section>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <!-- result list -->
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>查询结果</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>

                                    <div class="x_content">

                                        <c:choose>
                                            <c:when test="${has_result}">
                                                <div class="table-responsive">
                                                    <table id="datatable" class="table table-striped jambo_table" style="white-space: nowrap;">
                                                        <thead>
                                                        <tr class="headings">
                                                            <th class="column-title">发票代码</th>
                                                            <th class="column-title">发票号码</th>
                                                            <th class="column-title">开票日期</th>
                                                            <th class="column-title">购贷方</th>
                                                            <th class="column-title">销贷方</th>
                                                            <th class="column-title">总金额</th>
                                                            <th class="column-title">总税额</th>
                                                            <th class="column-title"></th>
                                                            <th class="column-title"><span class="nobr">操作</span></th>
                                                            <th class="column-title no-link last"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="invoice" items="${invoice_list}" varStatus="status">
                                                            <c:choose>
                                                                <c:when test="${status.index % 2 == 0}">
                                                                    <tr class="even pointer">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <tr class="odd pointer">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <td class=" ">${invoice.invoiceCode}</td>
                                                            <td class=" ">${invoice.invoiceId}</td>
                                                            <td class=" ">${invoice.invoiceDate}</td>
                                                            <td class=" ">${invoice.buyerName}</td>
                                                            <td class=" ">${invoice.sellerName}</td>
                                                            <td class=" ">￥${invoice.totalAmount}</td>
                                                            <td class=" ">￥${invoice.totalTax}</td>
                                                            <td class=" ">
                                                                <form action="queryForList" method="post">
                                                                    <input type="hidden" name="id" value="${invoice.id}">
                                                                    <input type="hidden" name="preAction" value="queryForList">
                                                                    <input type="hidden" name="action" value="view">
                                                                    <input type="submit" value="查看"
                                                                           class="btn btn-round btn-success">
                                                                </form>
                                                            </td><td class=" ">
                                                            <form action="queryForList" method="post">
                                                                <input type="hidden" name="id" value="${invoice.id}">
                                                                <input type="hidden" name="preAction" value="queryForList">
                                                                <input type="hidden" name="action" value="edit">
                                                                <input type="submit" value="编辑"
                                                                       class="btn btn-round btn-primary">
                                                            </form>
                                                        </td>
                                                            <td class=" last">
                                                                <form action="queryForList" method="post">
                                                                    <input type="hidden" name="id" value="${invoice.id}">
                                                                    <input type="hidden" name="preAction" value="queryForList">
                                                                    <input type="hidden" name="action" value="delete">
                                                                    <input type="submit" value="删除" onclick="return confirm('确定删除发票？');"
                                                                           class="btn btn-round btn-danger">
                                                                </form>
                                                            </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <h3 style="text-align: center"><small>未查询到匹配的内容，请重新设置查询条件</small></h3>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%@ include file="no_authority.jspf"%>
                    </c:otherwise>
                </c:choose>
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
<!-- iCheck -->
<script src="../vendors/iCheck/icheck.min.js"></script>
<!-- bootstrap-daterangepicker -->
<script src="../vendors/moment/min/moment.min.js"></script>
<script src="../vendors/bootstrap-daterangepicker/daterangepicker.js"></script>
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
<script src="../build/js/custom.js"></script>
<script type="text/javascript">
    var ymd = document.getElementById('invoiceDate').value.split('-');
    document.getElementById('single_cal4').value = ymd[1] + "/" + ymd[2] + "/" + ymd[0];
</script>
<c:if test="${not empty auth_message}">
    <script>
        alert('${auth_message}');
    </script>
</c:if>
</body>
</html>
