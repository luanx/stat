<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-25
  Time: 11:32:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<c:set var="lx" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title>产品管理</title>
</head>
<body>

<div class="tab-content">
    <div id="all" class="tab-pane active">

        <c:if test="${not empty message}">
            <div class="alert alert-info fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <p>${message}</p>

            </div>
        </c:if>

        <div class="table-responsive">
            <table id="inputTable" class="table table-primary">
            </table>
        </div>
        <!-- table-responsive -->

    </div>
    <!-- tab-pane -->


</div>
<!-- tab-content -->


<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {

        var inputTable = jQuery("#inputTable").bootstrapTable({
            "url": "${lx}/api/v1/purchase/product_detail",
            "method": "get",
            "striped": true,
            "sortOrder": "asc",
            "dataType": "json",
            "pagination": true,
            "pageSize": 10,
            "sidePagination": "server",
            "pageList": [10, 20, 50, 100, 200],
            "search": true,
            "showColumns": true,
            "showToggle": true,
            "showRefresh": true,
            "minimumCountColumns": 2,
            "clickToSelect": true,
            "toolbar": "#custom-toolbar",
            "columns": [{
                "field": "id",
                "title": "ID",
                "visible": false
            }, {
                "field": "sku",
                "title": "SKU",
                "formatter": formatter
            }, {
                "field": "organizationName",
                "title": "店铺名称",
                "formatter": formatter
            },{
                "field": "supplierName",
                "title": "供应商名称",
                "formatter": formatter
            },{
                "field": "productName",
                "title": "产品名称",
                "formatter": formatter
            }, {
                "field": "productNo",
                "title": "货号",
                "formatter": formatter
            }, {
                "field": "color",
                "title": "颜色",
                "formatter": formatter
            }, {
                "field": "size",
                "title": "尺码",
                "formatter": formatter
            }, {
                "field": "feature",
                "title": "事物特征",
                "formatter": formatter
            },{
                "field": "link",
                "title": "采购链接",
                formatter: linkFormatter
            }, {
                "field": "alternateLink",
                "title": "备用链接",
                formatter: linkFormatter
            }, {
                "field": "price",
                "title": "单价",
                formatter: formatter
            }, {
                "field": "weight",
                "title": "重量",
                formatter: formatter
            }, {
                "field": "category",
                "title": "类目",
                formatter: formatter
            },{
                "field": "origin",
                "title": "产地",
                "formatter": formatter
            },{
                "field": "created",
                "title": "创建时间",
                "formatter": formatter
            }, {
                "field": "remark",
                "title": "备注",
                "formatter": formatter
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
            if (value == null) {
                return "";
            }
            return "<nobr>" + value + "</nobr>";
        }

        function linkFormatter(value) {
            if (value == null) {
                return "";
            }
            return "<a href='" + value + "' target='_blank'><nobr>" + value + "</nobr></a>";
        }


    });
</script>
</body>
</html>
