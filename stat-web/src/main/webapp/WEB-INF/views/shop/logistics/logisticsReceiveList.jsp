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
        <form id="inputForm" action="${lx}/logistics/receive" class="form-horizontal form-bordered">

        <c:if test="${not empty message}">
            <div class="alert alert-info fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <p>${message}</p>

            </div>
        </c:if>

            <div id="custom-toolbar">
                <div class="btn-group mr2">
                    <a type="button" class="btn btn-success" id="btn_confirm"><i class="fa fa-thumbs-up mr5"></i> 确认完好</a>
                    <a type="button" class="btn btn-success" id="btn_edit"><i class="fa fa-edit mr5"></i> 编辑</a>
                </div>

                <div class="btn-group m2">
                    <a href="${lx}/logistics/receive" class="btn btn-default"><i class="fa fa-comments mr5"></i> 刷新</a>
                </div>
            </div>


        <div class="table-responsive">
            <table id="inputTable" class="table table-primary">
            </table>
        </div>
        <!-- table-responsive -->

            <!-- 模态框（Modal） -->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="myModalLabel">
                                请确认
                            </h4>
                        </div>
                        <div class="modal-body">
                            确认货物完好吗?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="btn_complete">
                                确定
                            </button>
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal">关闭
                            </button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal -->
                </div>


            </form>
    </div>
    <!-- tab-pane -->


</div>
<!-- tab-content -->

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function(){
        var inputTable = jQuery("#inputTable").bootstrapTable({
            "url": "${lx}/api/v1/logistics/receive",
            "method": "get",
            "striped": true,
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
            },{
                "field": "id",
                "title": "ID",
                "visible": false
            },{
                "field": "purchaseDate",
                "title": "订货日期",
                formatter: formatter
            }, {
                "field": "purchaseOrderId",
                "title": "采购订单号",
                formatter: formatter
            },{
                "field": "platformOrderId",
                "title": "平台订单号",
                formatter: formatter
            }, {
                "field": "sku",
                "title": "SKU",
                formatter: formatter
            }, {
                "field": "productName",
                "title": "商品名称",
                formatter: formatter
            }, {
                "field": "productFeature",
                "title": "事物特性",
                formatter: formatter
            }, {
                "field": "quantity",
                "title": "数量",
                formatter: formatter
            }, {
                "field": "price",
                "title": "价格",
                formatter: formatter
            }, {
                "field": "productLink",
                "title": "采购链接",
                formatter: linkFormatter
            }, {
                "field": "supplierName",
                "title": "供应商名称",
                formatter: formatter
            }, {
                "field": "wwName",
                "title": "卖家旺旺",
                formatter: formatter
            },{
                "field": "trackno",
                "title": "快递单号",
                formatter: formatter
            },{
                "field": "receiveStatus",
                "title": "订单状态",
                formatter: formatter
            },{
                "field": "remark",
                "title": "备注",
                formatter: formatter
            }],
            onCheck: function(row){
            },
            onUncheck: function(row){

            },
            onCheckAll: function(){

            },
            onUncheckAll: function(){

            }
        });

        function formatter(value){
            return "<nobr>" + value + "</nobr>";
        }

        function linkFormatter(value){
            return "<a href='" + value +"' target='_blank'><nobr>" + value + "</nobr></a>";
        }

        jQuery("#btn_confirm").click(function(){
            var btSelectItemArr = inputTable.bootstrapTable('getSelections');
            if(btSelectItemArr.length >0){
                jQuery("#myModal").modal();
                jQuery('#btn_complete').click(function () {
                    jQuery.ajax({
                       "url": "${lx}/api/v1/logistics/receive/confirm",
                        "contentType": "application/json",
                        "method": "put",
                        "data": jQuery.toJSON(btSelectItemArr),
                        "success": function(){
                            jQuery("#myModal").modal("hide");
                            window.location.href = "${lx}/logistics/receive";
                        }
                    });
                });
            } else {
                alert("请选择需要修改的订单");
            }
        });

        jQuery("#btn_edit").click(function(){
            var btSelectItemArr = inputTable.bootstrapTable('getSelections');
            if(btSelectItemArr.length > 0){
                if(btSelectItemArr.length >1){
                    alert("只能选择一条订单进行修改");
                } else {
                    var btSelectItem = btSelectItemArr[0];
                    window.location.href = "${lx}/logistics/receive/update/" + btSelectItem.id;
                }
            } else {
                alert("请选择需要修改的订单");
            }
        });
    });

</script>

</body>
</html>
