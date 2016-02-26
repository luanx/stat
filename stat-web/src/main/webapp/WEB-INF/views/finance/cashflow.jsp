<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-27
  Time: 16:46:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>现金流</title>
</head>
<body>
<div class="row">
    <div class="col-sm-3 col-lg-2">
        <ul class="nav nav-pills nav-stacked nav-email">
            <li class="active">
                <a href="${lx}/cashflow">
                    <i class="glyphicon glyphicon-inbox"></i> 汇总情况
                </a>
            </li>
            <li><a href="${lx}/finance/income"><i class="glyphicon glyphicon-star"></i> 收入情况</a></li>
            <li><a href="${lx}/finance/expense"><i class="glyphicon glyphicon-send"></i> 支出情况</a></li>
        </ul>

    </div>
    <!-- col-sm-3 -->

    <div class="col-sm-9 col-lg-10">
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="col-sm-3">
                    <span class="text-muted">收入</span>
                    <h4>$630,201</h4>
                </div>
                <div class="col-sm-3">
                    <span class="text-muted">支出</span>
                    <h4>$630,201</h4>
                </div>
                <div class="col-sm-3">
                    <span class="text-muted">余额</span>
                    <h4>$630,201</h4>
                </div>
            </div>

            <div class="table-responsive">
                <table id="inputTable" class="table table-primary table-buglist">
                </table>
            </div>
        </div>
    </div>

</div>
<!-- row -->

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {

        var inputTable = jQuery("#inputTable").bootstrapTable({
            "url": "${lx}/api/v1/finance/account",
            "method": "get",
            "striped": true,
            "sortOrder": "desc",
            "dataType": "json",
            "pagination": true,
            "pageSize": 10,
            "sidePagination": "server",
            "pageList": [10, 20, 50, 100, 200],
            "minimumCountColumns": 2,
            "clickToSelect": true,
            "columns": [ {
                "field": "id",
                "title": "ID",
                "visible": false
            }, {
                "field": "date",
                "title": "日期",
                "formatter": formatter
            }, {
                "field": "organizationName",
                "title": "店铺",
                "formatter": formatter
            }, {
                "field": "balance",
                "title": "余额",
                "formatter": formatter
            }, {
                "field": "created",
                "title": "创建时间",
                "formatter": formatter
            }, {
                "field": "modified",
                "title": "更新时间",
                "formatter": linkFormatter
            }],
            onCheck: function (row) {
            },
            onUncheck: function (row) {

            },
            onCheckAll: function () {

            },
            onUncheckAll: function () {

            }
        });

        function formatter(value) {
            if(value == null){
                return "";
            }
            return "<nobr>" + value + "</nobr>";
        }

        function linkFormatter(value) {
            if(value == null){
                return "";
            }
            return "<a href='" + value + "' target='_blank'><nobr>" + value + "</nobr></a>";
        }


    });
</script>

</body>
</html>
