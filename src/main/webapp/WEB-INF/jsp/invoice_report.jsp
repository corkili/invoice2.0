<%--
  Created by IntelliJ IDEA.
  User: 李浩然
  Date: 2017/6/11
  Time: 21:44
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

    <title>分析报表</title>


    <!-- Bootstrap -->
    <link href="../vendors/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="../vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="../vendors/nprogress/nprogress.css" rel="stylesheet">
    <!-- iCheck -->
    <link href="../vendors/iCheck/skins/flat/green.css" rel="stylesheet">

    <!-- bootstrap-progressbar -->
    <link href="../vendors/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet">
    <!-- JQVMap -->
    <link href="../vendors/jqvmap/dist/jqvmap.min.css" rel="stylesheet"/>
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
                        <h3>分析报表</h3>
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
                                            <li><a class="collapse-link" id="drop_panel"><i class="fa fa-chevron-up"></i></a>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form action="report" method="post" class="form-horizontal form-label-left">
                                            <input type="hidden" name="preAction" value="report">
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
                                                    <label class="control-label col-md-2" for="itName">
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
                                                <div class="form-group">
                                                    <label class="control-label col-md-2" for="pattern">
                                                        统计方式<span class="required">*</span>
                                                    </label>
                                                    <br/>
                                                    <div class="col-md-6">
                                                        <div id="pattern" class="btn-group" data-toggle="buttons">
                                                            年度：<input type="radio" class="flat" name="pattern" id="patternrY" value="yyyy" checked="" required />
                                                            月度：<input type="radio" class="flat" name="pattern" id="patternM" value="yyyy-MM" />
                                                            日度：<input type="radio" class="flat" name="pattern" id="patternD" value="yyyy-MM-dd" />
                                                        </div>
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

                        <!-- tab main panel -->
                        <div class="">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>
                                            <c:if test="${has_result}">${dates.get(0)}至${dates.get(dates.size()-1)}分析报表</c:if>
                                        </h2>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <c:choose>
                                            <c:when test="${has_result}">
                                                <div class="" role="tabpanel" data-example-id="togglable-tabs">
                                                    <ul id="" class="nav nav-tabs bar_tabs" role="tablist">
                                                        <li role="presentation" class="active" style="background-color: #F4F4F4">
                                                            <a href="#income_data_view" id="income_data_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="true" style="background-color: #F4F4F4;">
                                                                <strong>进项数据表</strong>
                                                            </a>
                                                        </li>
                                                        <%--<li role="presentation" class="" style="background-color: #F4F4F4">
                                                            <a href="#income_bar_view" id="income_bar_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #F4F4F4">
                                                                <strong>进项柱状图</strong>
                                                            </a>
                                                        </li>
                                                        <li role="presentation" class="" style="background-color: #F4F4F4">
                                                            <a href="#income_line_view" id="income_line_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #F4F4F4">
                                                                <strong>进项折线图</strong>
                                                            </a>
                                                        </li>--%>
                                                        <li role="presentation" class="" style="background-color: #B7B7B7">
                                                            <a href="#outcome_data_view" id="outcome_data_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #B7B7B7; color: #4A708B;">
                                                                <strong>销项数据表</strong>
                                                            </a>
                                                        </li>
                                                        <%--<li role="presentation" class="" style="background-color: #B7B7B7">
                                                            <a href="#outcome_bar_view" id="outcome_bar_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #B7B7B7; color: #4A708B;">
                                                                <strong>销项柱状图</strong>
                                                            </a>
                                                        </li>
                                                        <li role="presentation" class="" style="background-color: #B7B7B7">
                                                            <a href="#outcome_line_view" id="outcome_line_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #B7B7B7; color: #4A708B;">
                                                                <strong>销项折线图</strong>
                                                            </a>
                                                        </li>--%>
                                                        <li role="presentation" class="" style="background-color: #8E8E8E ">
                                                            <a href="#compare_data_view" id="compare_data_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #8E8E8E; color: #F5F5F5;">
                                                                <strong>对比数据表</strong>
                                                            </a>
                                                        </li>
                                                        <%--<li role="presentation" class="" style="background-color: #8E8E8E ">
                                                            <a href="#compare_bar_view" id="compare_bar_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #8E8E8E; color: #F5F5F5;">
                                                                <strong>对比柱状图</strong>
                                                            </a>
                                                        </li>
                                                        <li role="presentation" class="" style="background-color: #8E8E8E ">
                                                            <a href="#compare_line_view" id="compare_line_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #8E8E8E; color: #F5F5F5;">
                                                                <strong>对比折线图</strong>
                                                            </a>
                                                        </li>
                                                        <li role="presentation" class="" style="background-color: #8E8E8E ">
                                                            <a href="#compare_radar_view" id="compare_radar_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #8E8E8E; color: #F5F5F5;">
                                                                <strong>对比雷达图</strong>
                                                            </a>
                                                        </li>
                                                        <li role="presentation" class="" style="background-color: #8E8E8E ">
                                                            <a href="#compare_pie_view" id="compare_pie_tab" role="tab" data-toggle="tab"
                                                               aria-expanded="false" style="background-color: #8E8E8E; color: #F5F5F5;">
                                                                <strong>对比饼状图</strong>
                                                            </a>
                                                        </li>--%>
                                                    </ul>
                                                    <div id="myTabContent" class="tab-content">
                                                        <div role="tabpanel" class="tab-pane fade active in" id="income_data_view" aria-labelledby="income_data_tab">
                                                            <div>
                                                                <button class="btn btn-primary pull-right" style="margin-right: 5px;"
                                                                        onclick="table2excel('income_data_table')">
                                                                    <i class="fa fa-download"></i>导出Excel
                                                                </button>
                                                                <h4 style="text-align: center">
                                                                    企业进项数据表
                                                                    <small>（单位：元）</small>
                                                                </h4><br/>
                                                            </div>
                                                            <div class="table-responsive">
                                                                <table id="income_data_table" class="table table-striped jambo_table bulk_action" style="white-space: nowrap;">
                                                                    <thead>
                                                                    <tr class="headings">
                                                                        <th class="column-title">月份/项目</th>
                                                                        <c:forEach var="n" items="${income_names}" varStatus="status" >
                                                                            <th class="column-title" style="text-align: center">${n}</th>
                                                                        </c:forEach>
                                                                        <th class="column-title" style="text-align: center">合计</th>
                                                                    </tr>
                                                                    </thead>

                                                                    <tbody>
                                                                    <c:forEach var="line" items="${income_amounts}" varStatus="status">
                                                                        <c:choose>
                                                                            <c:when test="${status.index % 2 == 0}">
                                                                                <tr class="even pointer">
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <tr class="odd pointer">
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        <td class=" " style="text-align: center"><b>${dates.get(status.index)}</b></td>
                                                                        <c:forEach var="price" items="${line}">
                                                                            <td class=" " style="text-align: center">
                                                                                <c:choose>
                                                                                    <c:when test="${price!=0}">${price}</c:when>
                                                                                    <c:otherwise>-</c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                        </c:forEach>
                                                                        <td class=" " style="text-align: center">${incomes.get(status.index)}</td>
                                                                        </tr>
                                                                    </c:forEach>

                                                                    <c:choose>
                                                                    <c:when test="${dates.size() % 2 == 0}">
                                                                    <tr class="even pointer">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                    <tr class="odd pointer">
                                                                        </c:otherwise>
                                                                        </c:choose>

                                                                        <td class=" " style="text-align: center"><b>合计</b></td>
                                                                        <c:forEach var="total" items="${income_product_totals}">
                                                                            <td class=" " style="text-align: center">
                                                                                <c:choose>
                                                                                    <c:when test="${total!=0}">${total}</c:when>
                                                                                    <c:otherwise>-</c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                        </c:forEach>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                            <div class="ln_solid"></div>
                                                            <%--<p>--%>
                                                                <%--进项数据分析：${income_comments}--%>
                                                            <%--</p>--%>
                                                            <div id="income_chart_bar" style="height:450px;"></div>
                                                            <div class="ln_solid"></div>
                                                            <div id="income_chart_line" style="height:450px;"></div>
                                                            <div class="ln_solid"></div>
                                                        </div>
                                                        <%--<div role="tabpanel" class="tab-pane fade" id="income_bar_view" aria-labelledby="income_bar_tab">
                                                            <div id="income_chart_bar" style="height:450px;"></div>
                                                        </div>
                                                        <div role="tabpanel" class="tab-pane fade" id="income_line_view" aria-labelledby="income_line_tab">
                                                            <div id="income_chart_line" style="height:450px;"></div>
                                                        </div>--%>
                                                        <div role="tabpanel" class="tab-pane fade" id="outcome_data_view" aria-labelledby="outcome_data_tab">
                                                            <div>
                                                                <button class="btn btn-primary pull-right" style="margin-right: 5px;"
                                                                        onclick="table2excel('outcome_data_table')">
                                                                    <i class="fa fa-download"></i>导出Excel
                                                                </button>
                                                                <h4 style="text-align: center">
                                                                    企业销项数据表
                                                                    <small>（单位：元）</small>
                                                                </h4><br/>
                                                            </div>
                                                            <div class="table-responsive">
                                                                <table id="outcome_data_table" class="table table-striped jambo_table bulk_action" style="white-space: nowrap;">
                                                                    <thead>
                                                                    <tr class="headings">
                                                                        <th class="column-title">月份/项目</th>
                                                                        <c:forEach var="n" items="${outcome_names}" varStatus="status" >
                                                                            <th class="column-title" style="text-align: center">${n}</th>
                                                                        </c:forEach>
                                                                        <th class="column-title" style="text-align: center">合计</th>
                                                                    </tr>
                                                                    </thead>

                                                                    <tbody>
                                                                    <c:forEach var="line" items="${outcome_amounts}" varStatus="status">
                                                                        <c:choose>
                                                                            <c:when test="${status.index % 2 == 0}">
                                                                                <tr class="even pointer">
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <tr class="odd pointer">
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        <td class=" " style="text-align: center"><b>${dates.get(status.index)}</b></td>
                                                                        <c:forEach var="price" items="${line}">
                                                                            <td class=" " style="text-align: center">
                                                                                <c:choose>
                                                                                    <c:when test="${price!=0}">${price}</c:when>
                                                                                    <c:otherwise>-</c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                        </c:forEach>
                                                                        <td class=" " style="text-align: center">${outcomes.get(status.index)}</td>
                                                                        </tr>
                                                                    </c:forEach>

                                                                    <c:choose>
                                                                    <c:when test="${dates.size() % 2 == 0}">
                                                                    <tr class="even pointer">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                    <tr class="odd pointer">
                                                                        </c:otherwise>
                                                                        </c:choose>

                                                                        <td class=" " style="text-align: center"><b>合计</b></td>
                                                                        <c:forEach var="total" items="${outcome_product_totals}">
                                                                            <td class=" " style="text-align: center">
                                                                                <c:choose>
                                                                                    <c:when test="${total!=0}">${total}</c:when>
                                                                                    <c:otherwise>-</c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                        </c:forEach>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                            <div class="ln_solid"></div>
                                                            <%--<p>--%>
                                                                <%--销项数据分析：${outcome_comments}--%>
                                                            <%--</p>--%>
                                                            <div id="outcome_chart_bar" style="height:450px;"></div>
                                                            <div class="ln_solid"></div>
                                                            <div id="outcome_chart_line" style="height:450px;"></div>
                                                            <div class="ln_solid"></div>
                                                        </div>
                                                        <%--<div role="tabpanel" class="tab-pane fade" id="outcome_bar_view" aria-labelledby="outcome_bar_tab">
                                                            <div id="outcome_chart_bar" style="height:450px;"></div>
                                                        </div>
                                                        <div role="tabpanel" class="tab-pane fade" id="outcome_line_view" aria-labelledby="outcome_line_tab">
                                                            <div id="outcome_chart_line" style="height:450px;"></div>
                                                        </div>--%>
                                                        <div role="tabpanel" class="tab-pane fade" id="compare_data_view" aria-labelledby="compare_data_tab">
                                                            <div>
                                                                <button class="btn btn-primary pull-right" style="margin-right: 5px;"
                                                                        onclick="table2excel('compare_data_table')">
                                                                    <i class="fa fa-download"></i>导出Excel
                                                                </button>
                                                                <h4 style="text-align: center">
                                                                    企业进销项对比数据表
                                                                    <small>（单位：元）</small>
                                                                </h4><br/>
                                                            </div>
                                                            <div class="table-responsive">
                                                                <table id="compare_data_table" class="table table-striped jambo_table bulk_action" style="white-space: nowrap;">
                                                                    <thead>
                                                                    <tr class="headings">
                                                                        <th class="column-title"></th>
                                                                        <c:forEach var="date" items="${dates}" varStatus="status" >
                                                                            <th class="column-title" style="text-align: center">${date}</th>
                                                                        </c:forEach>
                                                                        <th class="column-title" style="text-align: center">合计</th>
                                                                        <c:if test="${not empty pre_date}">
                                                                            <th class="column-title" style="text-align: center">${pre_date}</th>
                                                                        </c:if>
                                                                    </tr>
                                                                    </thead>

                                                                    <tbody>
                                                                    <tr class="even pointer">
                                                                        <td class=" " style="text-align: center">
                                                                            <b>进项</b>
                                                                        </td>
                                                                        <c:forEach var="amount" items="${incomes}">
                                                                            <td class=" " style="text-align: center">${amount}</td>
                                                                        </c:forEach>
                                                                        <td class=" " style="text-align: center">
                                                                                ${income_product_totals.get(income_product_totals.size() - 1)}
                                                                        </td>
                                                                        <c:if test="${not empty pre_date}">
                                                                            <td class=" " style="text-align: center">${pre_income}</td>
                                                                        </c:if>
                                                                    </tr>
                                                                    <tr class="odd pointer">
                                                                        <td class=" " style="text-align: center">
                                                                            <b>销项</b>
                                                                        </td>
                                                                        <c:forEach var="amount" items="${outcomes}">
                                                                            <td class=" " style="text-align: center">
                                                                                <c:choose>
                                                                                    <c:when test="${amount!=0}">${amount}</c:when>
                                                                                    <c:otherwise>-</c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                        </c:forEach>
                                                                        <td class=" " style="text-align: center">
                                                                                ${outcome_product_totals.get(outcome_product_totals.size() - 1)}
                                                                        </td>
                                                                        <c:if test="${not empty pre_date}">
                                                                            <td class=" " style="text-align: center">${pre_outcome}</td>
                                                                        </c:if>
                                                                    </tr>
                                                                    <tr class="even pointer">
                                                                        <td class=" " style="text-align: center">
                                                                            <b>差额</b>
                                                                        </td>
                                                                        <c:forEach var="amount" items="${balances}">
                                                                            <td class=" " style="text-align: center">
                                                                                    ${amount}
                                                                            </td>
                                                                        </c:forEach>
                                                                        <c:if test="${not empty pre_date}">
                                                                            <td class=" " style="text-align: center">${pre_balance}</td>
                                                                        </c:if>
                                                                    </tr>
                                                                    <tr class="odd pointer">
                                                                        <td class=" " style="text-align: center">
                                                                            <b>盈亏</b>
                                                                        </td>
                                                                        <c:forEach var="amount" items="${balances}">
                                                                            <c:choose>
                                                                                <c:when test="${amount > 0}">
                                                                                    <td class=" " style="text-align: center; color: green">
                                                                                        盈利
                                                                                    </td>
                                                                                </c:when>
                                                                                <c:when test="${amount < 0}">
                                                                                    <td class=" " style="text-align: center; color: red">
                                                                                        亏损
                                                                                    </td>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <td class=" " style="text-align: center; color: yellow">
                                                                                        平衡
                                                                                    </td>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:forEach>
                                                                        <c:if test="${not empty pre_date}">
                                                                            <c:choose>
                                                                                <c:when test="${pre_balance > 0}">
                                                                                    <td class=" " style="text-align: center; color: green">
                                                                                        盈利
                                                                                    </td>
                                                                                </c:when>
                                                                                <c:when test="${pre_balance < 0}">
                                                                                    <td class=" " style="text-align: center; color: red">
                                                                                        亏损
                                                                                    </td>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <td class=" " style="text-align: center; color: yellow">
                                                                                        平衡
                                                                                    </td>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:if>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                            <div class="ln_solid"></div>
                                                            <p>
                                                                进销项数据对比分析：${compare_comments}
                                                            </p>
                                                            <div class="ln_solid"></div>
                                                            <div id="chart_bar" style="height:350px;"></div>
                                                            <div class="ln_solid"></div>
                                                            <div id="chart_line" style="height:350px;"></div>
                                                            <div class="ln_solid"></div>
                                                            <div id="chart_radar" style="height:600px;"></div>
                                                            <div class="ln_solid"></div>
                                                            <div id="chart_pie" style="height:600px;"></div>
                                                            <div class="ln_solid"></div>
                                                        </div>
                                                        <%--<div role="tabpanel" class="tab-pane fade" id="compare_bar_view" aria-labelledby="compare_bar_tab">
                                                            <div id="chart_bar" style="height:350px;"></div>
                                                        </div>
                                                        <div role="tabpanel" class="tab-pane fade" id="compare_line_view" aria-labelledby="compare_line_tab">
                                                            <div id="chart_line" style="height:350px;"></div>
                                                        </div>
                                                        <div role="tabpanel" class="tab-pane fade" id="compare_radar_view" aria-labelledby="compare_radar_tab">
                                                            <div id="chart_radar" style="height:400px;"></div>
                                                        </div>
                                                        <div role="tabpanel" class="tab-pane fade" id="compare_pie_view" aria-labelledby="compare_pie_tab">
                                                            <div id="chart_pie" style="height:400px;"></div>
                                                        </div>--%>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <h3 style="text-align: center"><small>无结果，请在上方输入查询条件</small></h3>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /tab main panel -->
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
<!-- bootstrap-progressbar -->
<script src="../vendors/bootstrap-progressbar/bootstrap-progressbar.min.js"></script>
<!-- ECharts -->
<script src="../js/func/echarts.min.js"></script>
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

<!-- table2excel -->
<script src="../js/func/table2excel.js" type="text/javascript" language="javascript"></script>
<!-- generate charts -->
<c:if test="${has_result}">
    <script type="text/javascript">
        var theme = {
            color: [
                '#26B99A', '#34495E', '#BDC3C7', '#3498DB',
                '#9B59B6', '#8abb6f', '#759c6a', '#bfd3b7'
            ],

            title: {
                itemGap: 8,
                textStyle: {
                    fontWeight: 'normal',
                    color: '#408829'
                }
            },

            dataRange: {
                color: ['#1f610a', '#97b58d']
            },

            toolbox: {
                color: ['#408829', '#408829', '#408829', '#408829']
            },

            tooltip: {
                backgroundColor: 'rgba(0,0,0,0.5)',
                axisPointer: {
                    type: 'line',
                    lineStyle: {
                        color: '#408829',
                        type: 'dashed'
                    },
                    crossStyle: {
                        color: '#408829'
                    },
                    shadowStyle: {
                        color: 'rgba(200,200,200,0.3)'
                    }
                }
            },

            dataZoom: {
                dataBackgroundColor: '#eee',
                fillerColor: 'rgba(64,136,41,0.2)',
                handleColor: '#408829'
            },
            grid: {
                borderWidth: 0
            },

            categoryAxis: {
                axisLine: {
                    lineStyle: {
                        color: '#408829'
                    }
                },
                splitLine: {
                    lineStyle: {
                        color: ['#eee']
                    }
                }
            },

            valueAxis: {
                axisLine: {
                    lineStyle: {
                        color: '#408829'
                    }
                },
                splitArea: {
                    show: true,
                    areaStyle: {
                        color: ['rgba(250,250,250,0.1)', 'rgba(200,200,200,0.1)']
                    }
                },
                splitLine: {
                    lineStyle: {
                        color: ['#eee']
                    }
                }
            },
            timeline: {
                lineStyle: {
                    color: '#408829'
                },
                controlStyle: {
                    normal: {color: '#408829'},
                    emphasis: {color: '#408829'}
                }
            },

            k: {
                itemStyle: {
                    normal: {
                        color: '#68a54a',
                        color0: '#a9cba2',
                        lineStyle: {
                            width: 1,
                            color: '#408829',
                            color0: '#86b379'
                        }
                    }
                }
            },
            map: {
                itemStyle: {
                    normal: {
                        areaStyle: {
                            color: '#ddd'
                        },
                        label: {
                            textStyle: {
                                color: '#c12e34'
                            }
                        }
                    },
                    emphasis: {
                        areaStyle: {
                            color: '#99d2dd'
                        },
                        label: {
                            textStyle: {
                                color: '#c12e34'
                            }
                        }
                    }
                }
            },
            force: {
                itemStyle: {
                    normal: {
                        linkStyle: {
                            strokeColor: '#408829'
                        }
                    }
                }
            },
            chord: {
                padding: 4,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1,
                            color: 'rgba(128, 128, 128, 0.5)'
                        },
                        chordStyle: {
                            lineStyle: {
                                width: 1,
                                color: 'rgba(128, 128, 128, 0.5)'
                            }
                        }
                    },
                    emphasis: {
                        lineStyle: {
                            width: 1,
                            color: 'rgba(128, 128, 128, 0.5)'
                        },
                        chordStyle: {
                            lineStyle: {
                                width: 1,
                                color: 'rgba(128, 128, 128, 0.5)'
                            }
                        }
                    }
                }
            },
            gauge: {
                startAngle: 225,
                endAngle: -45,
                axisLine: {
                    show: true,
                    lineStyle: {
                        color: [[0.2, '#86b379'], [0.8, '#68a54a'], [1, '#408829']],
                        width: 8
                    }
                },
                axisTick: {
                    splitNumber: 10,
                    length: 12,
                    lineStyle: {
                        color: 'auto'
                    }
                },
                axisLabel: {
                    textStyle: {
                        color: 'auto'
                    }
                },
                splitLine: {
                    length: 18,
                    lineStyle: {
                        color: 'auto'
                    }
                },
                pointer: {
                    length: '90%',
                    color: 'auto'
                },
                title: {
                    textStyle: {
                        color: '#333'
                    }
                },
                detail: {
                    textStyle: {
                        color: 'auto'
                    }
                }
            },
            textStyle: {
                fontFamily: 'Arial, Verdana, sans-serif'
            }
        };

        var dates = [];
        var incomes = [];
        var outcomes = [];
        var max = [];
        var indicator = [];
        var radarIncomes = [];
        var radarOutcomes = [];
        var pieIncomes = [];
        var pieOutcomes = [];
        var pieDates = [];
        var size = 0;
        size = ${dates.size()};

        var incomeNames = [];
        var outcomeNames = [];
        var incomeLine = [];
        var incomeBar = [];
        var outcomeLine = [];
        var outcomeBar = [];

        var incomeSelected = {};
        var outcomeSelected = {};

        // 预测相关
        var preIncomesLine = [];
        var preOutcomesLine = [];
        var preIncomesBar = [];
        var preOutcomesBar = [];

        var tmp;

        // 填充数据--进销项对比
        <c:forEach var="date" items="${dates}" varStatus="status">
        dates.push('${date}');
        pieDates.push('${date}');
        incomes.push(${incomes.get(status.index)});
        outcomes.push(${outcomes.get(status.index)});
        radarIncomes.push(${incomes.get(status.index)});
        radarOutcomes.push(${outcomes.get(status.index)});
        if (radarIncomes[${status.index}] > radarOutcomes[${status.index}])
            max.push(radarIncomes[${status.index}]);
        else
            max.push(radarOutcomes[${status.index}]);
        indicator.push({
            text: dates[${status.index}],
            max: max[${status.index}]
        });
        pieIncomes.push({
            value: incomes[${status.index}],
            name: ''+dates[${status.index}]
        });
        pieOutcomes.push({
            value: outcomes[${status.index}],
            name: ''+dates[${status.index}]
        });
        </c:forEach>

        <c:if test="${not empty pre_date}">
        dates.push('${pre_date}');
        preIncomesLine = [<c:forEach begin="0" end="${dates.size() - 2}" step="1">,</c:forEach> ${incomes.get(incomes.size()-1)}, ${pre_income}];
        preOutcomesLine = [<c:forEach begin="0" end="${dates.size() - 2}" step="1">,</c:forEach> ${outcomes.get(outcomes.size()-1)}, ${pre_outcome}];
        preIncomesBar = [<c:forEach begin="0" end="${dates.size() - 1}" step="1">,</c:forEach> ${pre_income}];
        preOutcomesBar = [<c:forEach begin="0" end="${dates.size() - 1}" step="1">,</c:forEach> ${pre_outcome}];
        radarIncomes.push(${pre_income});
        radarOutcomes.push(${pre_outcome});
        if (${pre_income} > ${pre_outcome})
            max.push(${pre_income});
        else
            max.push(${pre_outcome});
        indicator.push({
            text: '${pre_date}',
            max: max[max.length - 1]
        });
        pieIncomes.push({
            value: ${pre_income},
            name: '${pre_date}'
        });
        pieOutcomes.push({
            value: ${pre_outcome},
            name: '${pre_date}'
        });
        pieDates.push('${pre_date}');
        </c:if>

        // 填充数据--进项
        <c:forEach var="i" begin="0" end="${income_names.size() - 1}" step="1">
        incomeNames.push('${income_names.get(i)}');
        incomeSelected['${income_names.get(i)}'] = false;
        tmp = [];
        <c:forEach var="j" begin="0" end="${income_amounts.size() - 1}" step="1">
        tmp.push(${income_amounts.get(j).get(i)});
        </c:forEach>
        incomeLine.push({
            name: ''+incomeNames[${i}],
            type: 'line',
            smooth: true,
            itemStyle: {
                normal: {
                    label:{
                        show: false
                    }
                }
            },
            data: tmp
        });
        incomeBar.push({
            name:''+incomeNames[${i}],
            type:'bar',
            data:tmp
        });
        </c:forEach>

        // 填充数据--销项
        <c:forEach var="i" begin="0" end="${outcome_names.size() - 1}" step="1">
        outcomeNames.push('${outcome_names.get(i)}');
        outcomeSelected['${outcome_names.get(i)}'] = false;
        tmp = [];
        <c:forEach var="j" begin="0" end="${outcome_amounts.size() - 1}" step="1">
        tmp.push(${outcome_amounts.get(j).get(i)});
        </c:forEach>
        outcomeLine.push({
            name: outcomeNames[${i}],
            type: 'line',
            smooth: true,
            itemStyle: {
                normal: {
                    label:{
                        show: false
                    }
                }
            },
            data: tmp
        });
        outcomeBar.push({
            name:outcomeNames[${i}],
            type:'bar',
            data:tmp
        });
        </c:forEach>

        var subTitle = '${dates.get(0)}' + '~' + '${dates.get(dates.size()-1)}';

        // income_chart_line
        var incomeLineChart = echarts.init(document.getElementById("income_chart_line"), theme);

        incomeLineChart.setOption({
            title: {
                text: '进项数据折线图',
                subtext: subTitle,
                x: 'center',
                y: 'top',
                padding: [0,5,20,5]
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: false, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                type: 'scroll',
                x: 'center',
                y: 'bottom',
                padding: 0,
                data: incomeNames,
                selected: incomeSelected
            },
            calculable: true,
            xAxis: [{
                type: 'category',
                boundaryGap: false,
                name: '日期',
                data: dates
            }],
            yAxis: [{
                type: 'value',
                name: '金额（元）'
            }],
            series: incomeLine
        });
        // income_chart_line

        // income_chart_bar
        var incomeBarChart = echarts.init(document.getElementById("income_chart_bar"), theme);

        incomeBarChart.setOption({
            title: {
                text: '进项数据柱状图',
                subtext: subTitle,
                x: 'center',
                y: 'top',
                padding: [0,5,20,5]
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: false, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                type: 'scroll',
                x: 'center',
                y: 'bottom',
                padding: 0,
                data:incomeNames,
                selected: incomeSelected
            },
            xAxis: [
                {
                    type: 'category',
                    name: '日期',
                    data: dates,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '金额（元）'
                }
            ],
            series: incomeBar
        });
        // income_chart_bar

        // outcome_chart_line
        var outcomeLineChart = echarts.init(document.getElementById("outcome_chart_line"), theme);

        outcomeLineChart.setOption({
            title: {
                text: '销项数据折线图',
                subtext: subTitle,
                x: 'center',
                y: 'top',
                padding: [0,5,20,5]
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: false, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                type: 'scroll',
                x: 'center',
                y: 'bottom',
                padding: 0,
                data: outcomeNames,
                selected: outcomeSelected
            },
            calculable: true,
            xAxis: [{
                type: 'category',
                boundaryGap: false,
                name: '日期',
                data: dates
            }],
            yAxis: [{
                type: 'value',
                name: '金额（元）'
            }],
            series: outcomeLine
        });
        // outcome_chart_line

        // outcome_chart_bar
        var outcomeBarChart = echarts.init(document.getElementById("outcome_chart_bar"), theme);

        outcomeBarChart.setOption({
            title: {
                text: '销项数据柱状图',
                subtext: subTitle,
                x: 'center',
                y: 'top',
                padding: [0,5,20,5]
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: false, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                type: 'scroll',
                x: 'center',
                y: 'bottom',
                padding: 0,
                data:outcomeNames,
                selected: outcomeSelected
            },
            xAxis: [
                {
                    type: 'category',
                    name: '日期',
                    data: dates,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '金额（元）'
                }
            ],
            series: outcomeBar
        });
        // outcome_chart_bar


        // chart_line
        var lineChart = echarts.init(document.getElementById("chart_line"), theme);

        lineChart.setOption({
            title: {
                text: '企业进销项数据对比折线图',
                subtext: subTitle,
                x: 'center',
                y: 'top',
                padding: [0,5,20,5]
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: false, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data: ['进项','销项']
            },
            calculable: true,
            xAxis: [{
                type: 'category',
                boundaryGap: false,
                name: '日期',
                data: dates
            }],
            yAxis: [{
                type: 'value',
                name: '金额（元）'
            }],
            series: [{
                name: '进项',
                type: 'line',
                smooth: true,
                itemStyle: {
                    normal: {
                        label:{
                            show: false
                        }
                    }
                },
                data: incomes
            }, {
                name: '销项',
                type: 'line',
                smooth: true,
                itemStyle: {
                    normal: {
                        label:{
                            show: false
                        }
                    }
                },
                data: outcomes
            }<c:if test="${not empty pre_date}">
                , {
                    name: '进项',
                    type: 'line',
                    smooth: true,
                    itemStyle: {
                        normal: {
                            label:{
                                show: false
                            }
                        }
                    },
                    lineStyle: {
                        normal: {
                            type: 'dotted'
                        }
                    },
                    data: preIncomesLine
                }, {
                    name: '销项',
                    type: 'line',
                    smooth: true,
                    itemStyle: {
                        normal: {
                            label:{
                                show: false
                            }
                        }
                    },
                    lineStyle: {
                        normal: {
                            type: 'dotted'
                        }
                    },
                    data: preOutcomesLine
                }
                </c:if> ]
        });
        // chart_line

        // chart_bar
        var barChart = echarts.init(document.getElementById("chart_bar"), theme);

        barChart.setOption({
            title: {
                text: '企业进销项数据对比柱状图',
                subtext: subTitle,
                x: 'center',
                y: 'top',
                padding: [0,5,20,5]
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: false, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data:['进项','销项']
            },
            xAxis: [
                {
                    type: 'category',
                    name: '日期',
                    data: dates,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '金额（元）'
                }
            ],
            series: [
                {
                    name:'进项',
                    type:'bar',
                    data:incomes
                },
                {
                    name:'销项',
                    type:'bar',
                    data:outcomes
                }
                <c:if test="${not empty pre_date}">
                , {
                    name: '进项',
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            opacity: 0.5
                        }
                    },
                    data: preIncomesBar
                }, {
                    name: '销项',
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            opacity: 0.5
                        }
                    },
                    data: preOutcomesBar
                }
                </c:if>
            ]
        });

        // chart_bar

        // chart_radar
        var  radarChart = echarts.init(document.getElementById("chart_radar"), theme);

        radarChart.setOption({
            title: {
                text: '企业进销项数据对比雷达图',
                subtext: subTitle,
                x: 'left',
                y: 'top',
                padding: [0,5,20,5]
            },
            tooltip: {
                trigger: 'item'
            },
            toolbox: {
                feature: {
                    dataView: {show: false, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                orient: 'vertical',
                x:'left',
                y:'bottom',
                data: ['进项','销项']
            },
            polar: [{
                indicator: indicator
            }],
            calculable: true,
            series: [{
                type: 'radar',
                data: [{
                    value: radarIncomes,
                    name: '进项'
                }, {
                    value: radarOutcomes,
                    name: '销项'
                }]
            }]
        });
        // chart_radar

        // chart_pie
        var  pieChart = echarts.init(document.getElementById("chart_pie"), theme);

        pieChart.setOption({
            title: {
                text: '企业进项（左图）与销项（右图）数据饼状图',
                subtext: subTitle,
                x: 'center',
                y: 'top',
                padding: [0,5,20,5]
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c}元 ({d}%)"
            },
            legend: {
                x : 'center',
                y : 'bottom',
                data: pieDates
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: true},
                    magicType : {
                        show: true,
                        type: ['pie', 'funnel']
                    },
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            series : [
                {
                    name:'进项',
                    type:'pie',
                    radius : [30, 110],
                    center : ['25%', '50%'],
                    roseType : 'area',
                    data: pieIncomes
                },
                {
                    name:'销项',
                    type:'pie',
                    radius : [30, 110],
                    center : ['75%', '50%'],
                    roseType : 'area',
                    data: pieOutcomes
                }
            ]
        });
        // chart_pie

    </script>
</c:if>
</body>
</html>
