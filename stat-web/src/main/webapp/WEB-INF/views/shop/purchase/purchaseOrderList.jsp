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
    <title>订单管理</title>
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

        <div id="custom-toolbar">

            <div class="btn-group mr2">
                <a href="${lx}/purchase/order/download" class="btn btn-success"><i class="fa  fa-mail-forward"></i>下载订单</a>
                <a id="exception" href="${lx}/purchase/order/download_exception" class="btn btn-darkblue"><i
                        id="exceptionIcon" class="fa fa-flag mr5"></i>下载异常订单</a>
            </div>


            <select id="orderTypes" class="selectpicker mr5" data-width="100px"
                    data-style="btn btn-default">
                <c:forEach items="${orderTypes}" var="orderType">
                    <option value="${orderType.key}">${orderType.value}</option>
                </c:forEach>
            </select>

        </div>


        <div id="progress" class="progress" style="display:none;">
            <div class="progress-bar progress-bar-success"></div>
        </div>

        <div class="table-responsive">
            <table id="inputTable" class="table table-primary">
            </table>
        </div>
        <!-- table-responsive -->

    <!-- tab-pane -->
</div>

</div>
<!-- tab-content -->



<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {


        var inputTable = jQuery("#inputTable").bootstrapTable({
            "url": "${lx}/api/v1/purchase/order",
            "method": "get",
            "striped": true,
            "sortOrder": "desc",
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
            },{
                "field": "orderId",
                "title": "订单编号",
                "formatter": formatter
            }, {
                "field": "sku",
                "title": "SKU",
                "formatter": formatter
            },{
                "field": "orderItemId",
                "title": "OrderItemId",
                "formatter": formatter
            },{
                "field": "created",
                "title": "上传时间",
                "formatter": formatter
            }, {
                "field": "orderStatus",
                "title": "订单状态",
                "formatter": formatter
            }, {
                "field": "organizationName",
                "title": "店铺名称",
                "formatter": formatter
            },{
                "field": "productLink",
                "title": "产品链接",
                "formatter": linkFormatter
            },{
                "field": "productName",
                "title": "商品名称",
                "formatter": formatter
            }, {
                "field": "orderItemId",
                "title": "OrderItemId",
                "formatter": formatter
            },{
                "field": "purDate",
                "title": "创建时间(M)",
                "formatter": formatter
            }, {
                "field": "payDate",
                "title": "付款时间(M)",
                "formatter": formatter
            }, {
                "field": "buyerName",
                "title": "买家姓名",
                "formatter": formatter
            }, {
                "field": "buyerEmail",
                "title": "买家邮箱",
                "formatter": formatter
            }, {
                "field": "buyerPhoneNumber",
                "title": "买家电话号码",
                "formatter": formatter
            }, {
                "field": "quantityPurchased",
                "title": "数量",
                "formatter": formatter
            }, {
                "field": "currency",
                "title": "货币",
                "formatter": formatter
            }, {
                "field": "itemPrice",
                "title": "商品价格",
                "formatter": formatter
            }, {
                "field": "shipServiceLevel",
                "title": "配送方式",
                "formatter": formatter
            }, {
                "field": "recipientName",
                "title": "收件人姓名",
                "formatter": formatter
            }, {
                "field": "shipPhoneNumber",
                "title": "收件人电话",
                "formatter": formatter
            }, {
                "field": "shipAddress",
                "title": "配送地址",
                "formatter": formatter
            }, {
                "field": "shipCity",
                "title": "城市",
                "formatter": formatter
            }, {
                "field": "shipState",
                "title": "省/州",
                "formatter": formatter
            } , {
                "field": "shipPostalCode",
                "title": "邮编",
                "formatter": formatter
            }, {
                "field": "shipCountry",
                "title": "国家",
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


        jQuery("#orderTypes").change(function () {
            var orderType = jQuery(this).find("option:selected").val();
            inputTable.bootstrapTable('refresh', {url: '${lx}/api/v1/purchase/order/?orderType=' + orderType});
        });

        jQuery('#exception').click(function () {
            jQuery('#exceptionIcon').removeClass('shake-slow shake-constant');
            clearInterval(iter);
        });


        jQuery.ajax({
            "url": "${lx}/api/v1/purchase/order/exception/changed",
            "type": "GET",
            "dataType": "json",
            "success": function (data) {
                if (data == true) {
                    jQuery('#exceptionIcon').addClass('shake-slow shake-constant');
                }
            }
        });

        var iter = setInterval(function () {
            jQuery.ajax({
                "url": "${lx}/api/v1/purchase/order/exception/changed",
                "type": "GET",
                "dataType": "json",
                "success": function (data) {
                    if (data == true) {
                        jQuery('#exceptionIcon').addClass('shake-slow shake-constant');
                    }
                }
            });
        }, 600000);

    });
</script>
</body>
</html>
