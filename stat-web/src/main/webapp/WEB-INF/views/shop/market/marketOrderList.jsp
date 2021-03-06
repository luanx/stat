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
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

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

        <div class="alert alert-info fade in" id="message_fade" style="display:none;">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            <p id="message"></p>

        </div>

        <div id="custom-toolbar">
            <div class="btn-group mr10">
                <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-upload"></i>
                    <span> 导入订单</span>
                    <input id="uploadExcel" type="file" name="uploadExcel" multiple/>
                </span>
            </div>

            <div class="btn-group mr10">
                <a id="btn_edit" href="javascript:void(0);" class="btn btn-info" data-toggle="modal"
                   data-target=".bs-example-modal-lg"><i
                        class="fa  fa-edit"></i> 编辑</a>
                <a id="btn_end" href="javascript:void(0);" class="btn btn-info"><i
                        class="fa  fa-times"></i> 结束</a>
                <shiro:hasRole name="admin">
                    <a id="btn_del" href="javascript:void(0);" class="btn btn-info"><i
                            class="fa  fa-times"></i> 删除</a>
                </shiro:hasRole>
            </div>

            <select id="organizationList" class="selectpicker mr5" data-width="100px"
                             data-style="btn btn-default">
                <c:forEach items="${organizations}" var="organization">
                    <option value="${organization.id}">${organization.name}</option>
                </c:forEach>
            </select>

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

            </form>
    </div>
    <!-- tab-pane -->

</div>
<!-- tab-content -->

<%--订单修改modal--%>
<div id="order_detail" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="订单修改"
     aria-hidden="true">
    <div class="modal-dialog">
        <form id="orderDetailForm" action="${lx}/market/order_detail/update" class="form-horizontal form-bordered"
              method="post">
        <div class="modal-content">
            <div class="modal-header">
                <button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
                <h4 class="modal-title">订单修改</h4>
            </div>
            <div class="modal-body">

                    <div class="panel-body">
                        <input type="hidden" name="id" id="orderDetailId"/>
                        <div class="form-group">

                            <label class="col-sm-3 control-label">上传时间</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="created" id="created" disabled />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">订单编号</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="orderId" id="orderId" disabled required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">SKU</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="sku" id="sku" disabled required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">数量</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="quantityPurchased" id="quantityPurchased" required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">配送方式</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="shipServiceLevel" id="shipServiceLevel" required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">收件人姓名</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="recipientName" id="recipientName"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">收件人电话</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="shipPhoneNumber" id="shipPhoneNumber"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">国家</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="shipCountry" id="shipCountry"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">省/州</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="shipState" id="shipState"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">城市</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="shipCity" id="shipCity"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">配送地址</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="shipAddress" id="shipAddress"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">邮编</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="shipPostalCode" id="shipPostalCode"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">备注</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="remark" id="remark"
                                       required/>
                            </div>
                        </div>

                    </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="submit" class="btn btn-primary">保存</button>
            </div>
        </div>
        </form>
    </div>
</div>

<%--选中多个时提示错误--%>
<div id="multi_checked" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="错误"
     aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
                <h4 class="modal-title">错误</h4>
            </div>
            <div class="modal-body">只能选择一个订单</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<%--未选中订单时提示错误--%>
<div id="no_checked" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="错误"
     aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
                <h4 class="modal-title">错误</h4>
            </div>
            <div class="modal-body">请选择一个订单</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {

        var organizationId = ${organizations[0].id};

        var inputTable = jQuery("#inputTable").bootstrapTable({
            "url": "${lx}/api/v1/market/order/" + organizationId,
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
                "field": "orderId",
                "title": "订单编号",
                "formatter": formatter
            }, {
                "field": "sku",
                "title": "SKU",
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
                "field": "remark",
                "title": "备注",
                "formatter": formatter
            },{
                "field": "productLink",
                "title": "产品链接",
                "formatter": linkFormatter
            }, {
                "field": "purDate",
                "title": "创建时间(M)",
                "formatter": formatter
            }, {
                "field": "payDate",
                "title": "付款时间(M)",
                "formatter": formatter
            },{
                "field": "productName",
                "title": "商品名称",
                "formatter": formatter
            }, {
                "field": "orderItemId",
                "title": "OrderItemId",
                "formatter": formatter
            }, {
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
            }, {
                "field": "shipPostalCode",
                "title": "邮编",
                "formatter": formatter
            }, {
                "field": "shipCountry",
                "title": "国家",
                "formatter": formatter
            }, {
                "field": "orderItemId",
                "title": "OrderItemId",
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

        jQuery("#btn_end").click(function () {
            var ids = getIdSelections();
            if (ids != null && ids.length > 0) {
                jQuery.ajax({
                    "url": "${lx}/api/v1/market/order/end/" + ids,
                    "type": "PUT",
                    "contentType": "application/json",
                    "dataType": "application/json",
                    "success": function () {

                    }
                });
            }

            inputTable.bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
            jQuery(this).prop("disabled", true);
        });


        jQuery("#btn_del").click(function () {
            var ids = getIdSelections();
            if (ids != null && ids.length > 0) {
                jQuery.ajax({
                    "url": "${lx}/api/v1/market/order/delete/" + ids,
                    "type": "DELETE",
                    "contentType": "application/json",
                    "dataType": "application/json",
                    "success": function () {

                    }
                });
            }

            inputTable.bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
            jQuery(this).prop("disabled", true);
        });


        jQuery("#btn_edit").click(function () {
            var ids = getIdSelections();
            if (ids == null || ids.length == 0){
                jQuery("#no_checked").modal();
            } else if (ids.length > 1){
                jQuery("#multi_checked").modal();
            } else {
                var row = inputTable.bootstrapTable('getSelections')[0];

                //为OrderDetail赋值
                jQuery('#orderDetailId').val(row.id);
                jQuery('#created').val(row.created);
                jQuery('#orderId').val(row.orderId);
                jQuery('#sku').val(row.sku);
                jQuery('#quantityPurchased').val(row.quantityPurchased);
                jQuery('#shipServiceLevel').val(row.shipServiceLevel);
                jQuery('#recipientName').val(row.recipientName);
                jQuery("#shipPhoneNumber").val(row.shipPhoneNumber);
                jQuery("#shipCountry").val(row.shipCountry);
                jQuery("#shipState").val(row.shipState);
                jQuery("#shipCity").val(row.shipCity);
                jQuery("#shipAddress").val(row.shipAddress);
                jQuery("#shipPostalCode").val(row.shipPostalCode);
                jQuery("#remark").val(row.remark);

                jQuery("#order_detail").modal();
            }
        });

        jQuery("#organizationList").change(function () {
            organizationId = jQuery(this).find("option:selected").val();
            var orderType = jQuery("#orderTypes").val();
            inputTable.bootstrapTable('refresh', {url: '${lx}/api/v1/market/order/' + organizationId +"?orderType=" + orderType});
        });

        jQuery("#orderTypes").change(function () {
            var orderType = jQuery(this).find("option:selected").val();
            inputTable.bootstrapTable('refresh', {url: '${lx}/api/v1/market/order/' + organizationId + "?orderType=" + orderType});
        });

        jQuery("#uploadExcel").click(function(){
            jQuery(this).fileupload({
                url: '${lx}/market/order/upload/' + organizationId,
                dataType: 'json',
                maxNumberOfFiles: 1,
                maxFileSize: 5000000,
                submit: function (e, data) {
                    $('#progress').show();
                },
                done: function (e, data) {
                    $('#progress').fadeOut();
                    console.log(data.result);
                    $("#message").text(data.result.message);
                    $("#message_fade").show();
                },
                fail: function (e, data) {
                    $('#progress').fadeOut();
                    $("#message").text("上传失败,请重试或联系管理员");
                    $("#message_fade").show();
                },
                progressall: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    jQuery('#progress .progress-bar').css(
                            'width',
                            progress + '%'
                    );

                }
            }).prop('disabled', !$.support.fileInput)
                    .parent().addClass($.support.fileInput ? undefined : 'disabled');
        });


    });
</script>
</body>
</html>
