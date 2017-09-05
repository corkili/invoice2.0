<%--
  Created by IntelliJ IDEA.
  User: 李浩然
  Date: 2017/6/9
  Time: 22:35
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

    <title>图表查询 </title>

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
                        <h3>图表查询</h3>
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
                                        <form action="queryForChart" method="post" class="form-horizontal form-label-left"  onsubmit="return checkQueryForm()">
                                            <input type="hidden" name="preAction" value="queryForChart">
                                            <input type="hidden" name="action" value="query">
                                            <input type="hidden" name="pattern" value="yyyy-MM">
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

                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>折线图</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <c:choose>
                                            <c:when test="${has_result}">
                                                <div id="chart_line" style="height:350px;"></div>
                                            </c:when>
                                            <c:otherwise>
                                                <h3 style="text-align: center"><small>未查询到匹配的内容，请重新设置查询条件</small></h3>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>柱状图</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <c:choose>
                                            <c:when test="${has_result}">
                                                <div id="chart_bar" style="height:350px;"></div>
                                            </c:when>
                                            <c:otherwise>
                                                <h3 style="text-align: center"><small>未查询到匹配的内容，请重新设置查询条件</small></h3>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>雷达图</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <c:choose>
                                            <c:when test="${has_result}">
                                                <div id="chart_radar" style="height:400px;"></div>
                                            </c:when>
                                            <c:otherwise>
                                                <h3 style="text-align: center"><small>未查询到匹配的内容，请重新设置查询条件</small></h3>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                        	<div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>饼状图</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <c:choose>
                                            <c:when test="${has_result}">
                                                <div id="chart_pie" style="height:400px;"></div>
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

        <%@ include file="footer.jspf"%>
        <!-- /footer content -->
    </div>
</div>
</body>


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
<!-- Custom Theme Scripts -->
<script src="../build/js/custom.js"></script>

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
        var pieIncomes = [];
        var pieOutcomes = [];
        var size = 0;

        size = ${dates.size()};

        // 填充数据
        <c:forEach var="date" items="${dates}" varStatus="status">
        dates.push('${date}');
        incomes.push(${incomes.get(status.index)});
        outcomes.push(${outcomes.get(status.index)});
        if (incomes[${status.index}] > outcomes[${status.index}])
            max.push(incomes[${status.index}]);
        else
            max.push(outcomes[${status.index}]);
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

        var subTitle = '${dates.get(0)}' + '~' + '${dates.get(dates.size()-1)}';

        // chart_line
        var lineChart = echarts.init(document.getElementById("chart_line"), theme);


        lineChart.setOption({
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
                    dataView: {show: true, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
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
            }]
        });
        // chart_line

        // chart_bar
        var barChart = echarts.init(document.getElementById("chart_bar"), theme);

        barChart.setOption({
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
                    dataView: {show: true, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
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
            ]
        });

        // chart_bar

        // chart_radar
        var  radarChart = echarts.init(document.getElementById("chart_radar"), theme);

        radarChart.setOption({
            tooltip: {
                trigger: 'item'
            },
            toolbox: {
                feature: {
                    dataView: {show: true, readOnly: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                orient: 'vertical',
                x:'left',
                data: ['进项','销项']
            },
            polar: [{
                indicator: indicator
            }],
            calculable: true,
            series: [{
                type: 'radar',
                data: [{
                    value: incomes,
                    name: '进项'
                }, {
                    value: outcomes,
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
                data: dates
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