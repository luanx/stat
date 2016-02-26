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
    <title>采购管理</title>
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



                <div class="btn-group">

                  <span class="btn btn-default fileinput-button">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span> 1688</span>
                        <input id="uploadAlibabaOrderExcel" type="file" name="uploadAlibabaOrderExcel" multiple/>
                    </span>
                </div>

                <div class="btn-group">
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

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>


<script>
    jQuery(document).ready(function () {

        jQuery(function(){
            var inputTable = jQuery("#inputTable").bootstrapTable({
                "url": "${lx}/api/v1/purchase",
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
                    "title": "1688订单号",
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
                    "field": "quantity",
                    "title": "数量",
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
                return "<nobr>" + value + "</nobr>";
            }

            function linkFormatter(value) {
                return "<a href='" + value + "' target='_blank'><nobr>" + value + "</nobr></a>";
            }

            function getIdSelections(){
                return jQuery.map(inputTable.bootstrapTable('getSelections'), function(row){
                   return row.id;
                });
            }

            jQuery("#btn_remove").click(function () {
                var ids = getIdSelections();
                jQuery.ajax({
                    "url": "${lx}/api/v1/purchase/" + ids,
                    "type": "DELETE",
                    "dataType": "application/json",
                    "success": function () {

                    }
                });

                inputTable.bootstrapTable('remove', {
                    field: 'id',
                    values: ids
                });
                jQuery(this).prop("disabled", true);
            });

            jQuery("#btn_edit").click(function(){
                layer.open({
                    "type": 1,
                    area: ['600px', '360px'],
                    "content": "test"
                })
            });

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
              $('#progress').fadeOut();
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
            url: '${lx}/purchase/normal/upload_exception',
            dataType: 'json',
            maxNumberOfFiles: 1,
            maxFileSize: 2000000,
            submit: function (e, data) {
                $('#progress').show();
            },
            done: function (e, data) {
                $('#progress').fadeOut();
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
            },
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                jQuery('#progress .progress-bar').css(
                        'width',
                        progress + '%'
                );
                $('#progress').fadeOut();
            }
        }).prop('disabled', !$.support.fileInput)
                .parent().addClass($.support.fileInput ? undefined : 'disabled');


    });


</script>

</body>
</html>
