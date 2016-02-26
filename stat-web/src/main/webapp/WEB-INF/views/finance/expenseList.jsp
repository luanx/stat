<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-27
  Time: 19:02:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>支出</title>
</head>
<body>
<div class="row">
    <div class="col-sm-3 col-lg-2">
        <ul class="nav nav-pills nav-stacked nav-email">
            <li>
                <a href="${lx}/finance/cashflow">
                    <i class="glyphicon glyphicon-inbox"></i> 汇总情况
                </a>
            </li>
            <li><a href="${lx}/finance/income"><i class="glyphicon glyphicon-star"></i> 收入情况</a></li>
            <li class="active"><a href="${lx}/finance/expense"><i class="glyphicon glyphicon-send"></i> 支出情况</a></li>
        </ul>

        <div class="mb30"></div>

    </div>
    <!-- col-sm-3 -->

    <div class="col-sm-9 col-lg-10">

        <div class="panel panel-default">
            <div class="panel-body">

                <c:if test="${not empty message}">
                    <div class="alert alert-info fade in">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <p>${message}</p>

                    </div>
                </c:if>

                <div id="custom-toolbar">

                    <div class="btn-group mr10">
                        <a class="btn btn-default" href="${lx}/finance/expense/create"><i class="fa fa-arrows mr5"></i>
                            添加</a>
                    </div>

                    <select id="organizationList" class="selectpicker mr5" data-width="100px"
                            data-style="btn btn-default">
                        <c:forEach items="${organizations}" var="organization">
                            <option value="${organization.id}">${organization.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="table-responsive">
                    <table id="inputTable" class="table table-primary table-buglist">
                        </tbody>
                    </table>
                </div>
                <!-- table-responsive -->

            </div>
            <!-- panel-body -->
        </div>
        <!-- panel -->

    </div>
    <!-- col-sm-9 -->

</div>
<!-- row -->

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {

        var organizationId = ${organizations[0].id};

        var inputTable = jQuery("#inputTable").bootstrapTable({
            "url": "${lx}/api/v1/finance/expense/" + organizationId,
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
                "field": "status",
                "checkbox": true
            }, {
                "field": "id",
                "title": "ID",
                "visible": false
            }, {
                "field": "date",
                "title": "日期",
                "formatter": formatter
            }, {
                "field": "busPurchase",
                "title": "采购成本",
                "formatter": formatter
            }, {
                "field": "busLogistics",
                "title": "物流成本",
                "formatter": formatter
            }, {
                "field": "busAdvertisement",
                "title": "广告费",
                "formatter": formatter
            }, {
                "field": "busRefund",
                "title": "退款",
                "formatter": linkFormatter
            }, {
                "field": "salesTax",
                "title": "营业税",
                "formatter": formatter
            }, {
                "field": "adminSalary",
                "title": "人员薪酬",
                "formatter": formatter
            }, {
                "field": "adminOffice",
                "title": "办公成本",
                "formatter": formatter
            }, {
                "field": "adminTravel",
                "title": "差旅",
                "formatter": formatter
            }, {
                "field": "finInterest",
                "title": "利息",
                "formatter": formatter
            }, {
                "field": "finCharge",
                "title": "银行手续费",
                "formatter": formatter
            }, {
                "field": "other",
                "title": "其他",
                "formatter": formatter
            }, {
                "field": "balance",
                "title": "余额",
                "formatter": formatter
            }, {
                "field": "organizationName",
                "title": "店铺",
                "formatter": formatter
            }, {
                "field": "userName",
                "title": "提交人",
                "formatter": formatter
            }, {
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

        function getIdSelections() {
            return jQuery.map(inputTable.bootstrapTable('getSelections'), function (row) {
                return row.id;
            });
        }

        jQuery("#organizationList").change(function () {
            organizationId = jQuery(this).find("option:selected").val();
            inputTable.bootstrapTable('refresh', {url: "${lx}/api/v1/finance/expense/" + organizationId});
        });

    });
</script>


</body>
</html>
