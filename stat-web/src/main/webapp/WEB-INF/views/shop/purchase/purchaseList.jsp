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
    <title>日常采购</title>
</head>
<body>
<div class="tab-content">
    <div id="all" class="tab-pane active">

        <form id="inputForm" action="${lx}/purchase/normal" class="form-horizontal form-bordered">

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
                <div class="btn-group mr2">
                    <span class="btn btn-success fileinput-button">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span> 采购表</span>
                        <input id="uploadExcel" type="file" name="uploadExcel" multiple/>
                    </span>
                    <span class="btn btn-success fileinput-button">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span> 异常表</span>
                        <input id="uploadExceptionExcel" type="file" name="uploadExceptionExcel" multiple/>
                    </span>

                </div>



                <div class="btn-group mr10">

                  <span class="btn btn-default fileinput-button">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span> 1688</span>
                        <input id="uploadAlibabaOrderExcel" type="file" name="uploadAlibabaOrderExcel" multiple/>
                    </span>
                </div>

                <div class="btn-group mr10">
                    <a id="btn_edit" href="javascript:void(0);" class="btn btn-info" data-toggle="modal" data-target=".bs-example-modal-lg"><i
                            class="fa  fa-edit"></i> 编辑</a>
                    <a id="btn_remove" href="javascript:void(0);" class="btn btn-info"><i
                            class="fa  fa-times"></i> 删除</a>
                </div>

            </div>
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


<!-- tab-content -->

<%--订单修改modal--%>
<div id="purchase_detail" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="订单修改"
     aria-hidden="true">
    <div class="modal-dialog">
        <form id="purchaseDetailForm" action="${lx}/purchase/detail/update" class="form-horizontal form-bordered"
              method="post">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
                    <h4 class="modal-title">订单修改</h4>
                </div>
                <div class="modal-body">

                    <div class="panel-body">
                        <input type="hidden" name="id" id="purchaseDetailId"/>

                        <div class="form-group">
                            <label class="col-sm-3 control-label">日期 </label>

                            <div class="col-sm-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="yyyy/mm/dd" id="purchaseDate"
                                           name="purchaseDate"
                                           value="<fmt:formatDate value="${company.date}" pattern="yyyy/MM/dd"/>" style="z-index:99999 !important;"
                                           required/>
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">amazon订单号</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="purchaseOrderId" id="purchaseOrderId"  required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">1688订单号</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="platformOrderId" id="platformOrderId" required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">SKU</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="sku" id="sku"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">数量</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="quantity" id="quantity"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">产品链接</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="productLink" id="productLink"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">备用链接</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="alternateLink" id="alternateLink"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">产品名称</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="productName" id="productName"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">货号</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="productNo" id="productNo"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">事物特征</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="productFeature" id="productFeature"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">价格</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="price" id="price"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">运费</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="shipDiscount" id="shipDiscount"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">供应商名称</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="supplierName" id="supplierName"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">旺旺</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="wwName" id="wwName"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">备注</label>

                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="remark" id="remark"/>
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

        var inputTable = jQuery("#inputTable").bootstrapTable({
            "url": "${lx}/api/v1/purchase",
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
                "field": "purchaseOrderId",
                "title": "amazon订单号",
                "formatter": formatter
            }, {
                "field": "platformOrderId",
                "title": "采购订单号",
                "formatter": formatter
            }, {
                "field": "purchaseDate",
                "title": "采购日期",
                "formatter": formatter
            }, {
                "field": "sku",
                "title": "SKU",
                "formatter": formatter
            }, {
                "field": "orderItemId",
                "title": "OrderItemId",
                "formatter": formatter
            },{
                "field": "quantity",
                "title": "数量",
                "formatter": formatter
            }, {
                "field": "organizationName",
                "title": "店铺名称",
                "formatter": formatter
            },{
                "field": "category",
                "title": "类目",
                "formatter": formatter
            }, {
                "field": "origin",
                "title": "产地",
                "formatter": formatter
            }, {
                "field": "productLink",
                "title": "产品链接",
                "formatter": linkFormatter
            }, {
                "field": "alternateLink",
                "title": "备用链接",
                "formatter": linkFormatter
            }, {
                "field": "productName",
                "title": "产品名称",
                "formatter": formatter
            }, {
                "field": "price",
                "title": "单价",
                "formatter": formatter
            }, {
                "field": "shipDiscount",
                "title": "运费",
                "formatter": formatter
            }, {
                "field": "productNo",
                "title": "货号",
                "formatter": formatter
            }, {
                "field": "supplierName",
                "title": "供应商名称",
                "formatter": formatter
            }, {
                "field": "wwName",
                "title": "旺旺",
                "formatter": formatter
            },{
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

        jQuery("#btn_remove").click(function () {
            var ids = getIdSelections();
            if (ids != null && ids.length > 0) {
                jQuery.ajax({
                    "url": "${lx}/api/v1/purchase/" + ids,
                    "type": "DELETE",
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
            if (ids == null || ids.length == 0) {
                jQuery("#no_checked").modal();
            } else if (ids.length > 1) {
                jQuery("#multi_checked").modal();
            } else {
                var row = inputTable.bootstrapTable('getSelections')[0];

                jQuery("#purchaseDate").datepicker({
                    dateFormat: 'yy/mm/dd'
                });


                //为PurchaseDetail赋值
                jQuery('#purchaseDetailId').val(row.id);
                jQuery('#purchaseDate').val(row.purchaseDate);
                jQuery('#purchaseOrderId').val(row.purchaseOrderId);
                jQuery('#platformOrderId').val(row.platformOrderId);
                jQuery('#sku').val(row.sku);
                jQuery('#quantity').val(row.quantity);
                jQuery('#category').val(row.category);
                jQuery('#origin').val(row.origin);
                jQuery('#productLink').val(row.productLink);
                jQuery('#alternateLink').val(row.alternateLink);
                jQuery("#productName").val(row.productName);
                jQuery("#productNo").val(row.productNo);
                jQuery("#productFeature").val(row.productFeature);
                jQuery("#price").val(row.price);
                jQuery("#shipDiscount").val(row.shipDiscount);
                jQuery("#supplierName").val(row.supplierName);
                jQuery("#wwName").val(row.wwName);
                jQuery("#remark").val(row.remark);

                jQuery("#purchase_detail").modal();
            }
        });


        $('#uploadExcel').fileupload({
            url: '${lx}/purchase/upload',
            dataType: 'json',
            maxNumberOfFiles: 1,
            maxFileSize: 2000000,
            submit: function (e, data){
              $('#progress').show();
            },
            done: function (e, data){
                console.log(data);
                $('#progress').fadeOut();
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


        jQuery('#uploadExceptionExcel').fileupload({
            url: '${lx}/purchase/upload_exception',
            dataType: 'json',
            maxNumberOfFiles: 1,
            maxFileSize: 2000000,
            submit: function (e, data) {
                $('#progress').show();
            },
            done: function (e, data) {
                $('#progress').fadeOut();
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

        jQuery('#uploadAlibabaOrderExcel').fileupload({
            url: '${lx}/purchase/upload_alibaba_order',
            dataType: 'json',
            maxNumberOfFiles: 1,
            maxFileSize: 2000000,
            submit: function (e, data) {
                $('#progress').show();
            },
            done: function (e, data) {
                $('#progress').fadeOut();
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

        jQuery.ajax({
            "url": "${lx}/api/v1/purchase/exception/changed",
            "type": "GET",
            "dataType": "json",
            "success": function (data) {
                if (data == true) {
                    jQuery('#exceptionIcon').addClass('shake-slow shake-constant');
                }
            }
        });

        var iter = setInterval(function(){
            jQuery.ajax({
                "url": "${lx}/api/v1/purchase/exception/changed",
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
