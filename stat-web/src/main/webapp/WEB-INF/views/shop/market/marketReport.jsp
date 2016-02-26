<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-27
  Time: 16:46:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wantdo.stat.utils.DateUtil" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>数据报告</title>
</head>
<body>
<div class="row">
    <div class="col-sm-3 col-lg-2">
        <ul class="nav nav-pills nav-stacked nav-email">
            <li class="active"><a href="${lx}/market/report"><i class="glyphicon glyphicon-inbox"></i> 汇总情况</a></li>
            <li><a href="${lx}/market/report/area"><i class="glyphicon glyphicon-star"></i> 购买区域</a></li>
            <li><a href="${lx}/market/report/purchase"><i class="glyphicon glyphicon-send"></i> 下单情况</a></li>
            <li><a href="${lx}/market/report/payments"><i class="glyphicon glyphicon-pencil"></i> 付款情况</a></li>
            <li><a href="${lx}/market/report/shipservicelevel"><i class="glyphicon glyphicon-shopping-cart"></i> 快递选择</a></li>
        </ul>

    </div>
    <!-- col-sm-3 -->

    <div class="col-sm-9 col-lg-10">
        <div class="panel panel-default">
            <div class="panel-heading">
                <div id="zoom-controls">
                    <span>Zoom:</span>
                    <div class="btn-group">
                        <button type="button" class="btn btn-default">1W</button>
                        <button type="button" class="btn btn-default">2W</button>
                        <button type="button" class="btn btn-default">1M</button>
                        <button type="button" class="btn btn-default">3M</button>
                        <button type="button" class="btn btn-default">6M</button>
                        <button type="button" class="btn btn-default">1Y</button>
                    </div>
                    <select id="organizationList" class="selectpicker mr5" data-width="100px"
                            data-style="btn btn-default">
                        <c:forEach items="${organizations}" var="organization">
                            <option value="${organization.id}">${organization.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div id="date-picker">
                    <span>From:</span>
                    <input type="text" id="startDate" class="input-sm" pattern="yyyy/MM/dd"/>
                    <span>To:</span>
                    <input type="text" id="endDate" class="input-sm" pattern="yyyy/MM/dd"/>
                    <input type="button" id="submit_btn" class="btn btn-primary" value="查询"/>
                </div>
            </div>
            <div id="chart" class="panel-body" style="height:500px;">
            </div>
        </div>
    </div>

    <%--错误提示--%>
    <div id="error_prompt" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="错误"
         aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
                    <h4 class="modal-title">错误</h4>
                </div>
                <div class="modal-body">
                    <p id="error_msg"></p>
                </div>
                <div class="modal-footer">
                    <a type="button" class="btn btn-default" data-dismiss="modal">关闭</a>
                </div>
            </div>
        </div>
    </div>

</div>
<!-- row -->

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script src="${lx}/static/js/echarts.js"></script>


<script type="text/javascript">

    $(function(){
        $("#startDate").datepicker({
            dateFormat: 'yy-mm-dd'
        });
        $("#endDate").datepicker({
            dateFormat: 'yy-mm-dd'
        });

        init(new Date().addDays(-7).format("yyyy-MM-dd"), new Date().format("yyyy-MM-dd"));

        $("#zoom-controls button").click(function () {
            var _this = $(this);
            $("#zoom-controls button").removeClass("btn-primary").addClass("btn-default");
            _this.removeClass("btn-default").addClass("btn-primary");

            $("#date-picker input[type='text']").val("");

            var zoom = _this.text();
            var startDate;
            var endDate = new Date().format("yyyy-MM-dd");
            switch (zoom){
                case "1W":
                        startDate = new Date().addDays(-7).format("yyyy-MM-dd");
                        break;
                case "2W":
                        startDate = new Date().addDays(-14).format("yyyy-MM-dd");
                        break;
                case "1M":
                        startDate = new Date().addDays(-30).format("yyyy-MM-dd");
                        break;
                case "3M":
                        startDate = new Date().addDays(-90).format("yyyy-MM-dd");
                        break;
                case "6M":
                        startDate = new Date().addDays(-180).format("yyyy-MM-dd");
                        break;
                case "1Y":
                    startDate = new Date().addDays(-365).format("yyyy-MM-dd");
                    break;
                default:
                        startDate = new Date().addDays(-7).format("yyyy-MM-dd");
                        break;
            }

            console.log(startDate + " " + endDate);
            init(startDate, endDate);
        });

    });

    $("#submit_btn").click(function(){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        if(startDate == null || startDate == "" || typeof(startDate) == "undefined"){
            $("#error_msg").text("请选择起始时间");
            $("#error_prompt").modal();
        }
        if (endDate == null || endDate == "" || typeof(endDate) == "undefined") {
            $("#error_msg").text("请选择终止时间");
            $("#error_prompt").modal();
        }
        if(startDate > endDate){
            $("#error_msg").text("起始时间必须小于或等于终止时间");
            $("#error_prompt").modal();
        }
        init(startDate, endDate);
    });

    function init(startDate, endDate) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = echarts.init(document.getElementById('chart'));

        var option;

        var organizationId = ${organizations[0].id};

        if($("#organizationList option:selected").val() != null && typeof($("#organizationList option:selected").val()) != "undefined"){
            organizationId = $("#organizationList option:selected").val();
        }

        myChart.showLoading({
            text: "数据加载中",
            effect: "ring",
            textStyle: {
                fontSize: 20
            }
        });

        // ajax getting data
        $.ajax({
            "url": "${lx}/api/v1/market/report/total/" + organizationId,
            "method": "POST",
            "data": {
                startDate: startDate,
                endDate: endDate
            },
            "success": function (data) {
                option = data;
                // 为echarts对象加载数据
                myChart.setOption(option);
                myChart.hideLoading();
            }
        });

    }


</script>


</body>
</html>
