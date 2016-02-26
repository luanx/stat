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
            <div class="alert alert-info fade in" >
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
                            <span> 导入产品</span>
                            <input id="uploadExcel" type="file" name="uploadExcel" multiple/>
                        </span>
                    </div>

                    <div class="btn-group mr2">
                        <a href="${lx}/market/product/download_template" class="btn btn-default"><i
                                class="fa  fa-mail-forward"></i>产品模板</a>
                        <a href="${lx}/market/product/download_product/${organizations[0].id}" id="download_product" class="btn btn-default"><i
                                class="fa  fa-mail-forward"></i>产品</a>
                    </div>

                <div class="btn-group mr2">
                    <a id="btn_edit" href="javascript:void(0);" class="btn btn-info" data-toggle="modal"
                       data-target=".bs-example-modal-lg"><i
                            class="fa  fa-edit"></i> 编辑</a>
                    <a id="btn_remove" href="javascript:void(0);" class="btn btn-info"><i
                            class="fa  fa-times"></i> 删除</a>
                </div>

                <select id="organizationList" class="selectpicker" data-width="85px"
                        data-style="btn btn-default">
                    <c:forEach items="${organizations}" var="organization">
                        <option value="${organization.id}">${organization.name}</option>
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

    </div>
    <!-- tab-pane -->


</div>
<!-- tab-content -->

<%--订单修改modal--%>
<div id="order_detail" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="订单修改"
     aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <form id="orderDetailForm" action="${lx}/market/product/update" class="form-horizontal form-bordered"
              method="post">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
                    <h4 class="modal-title">产品修改</h4>
                </div>
                <div class="modal-body">

                    <div class="panel-body">
                        <input type="hidden" name="id" id="productId"/>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">SKU</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="sku" id="sku" required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">采购链接</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="link" id="link"
                                       required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">备用链接</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="alternateLink" id="alternateLink"/>
                            </div>
                        </div>

                        <table id="productDetailTable" class="table table-primary">
                        </table>

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
            <div class="modal-body">只能选择一个产品</div>
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
            <div class="modal-body">请选择一个产品</div>
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
            "url": "${lx}/api/v1/market/product/" + organizationId,
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
                "field": "status",
                "checkbox": true
            }, {
                "field": "id",
                "title": "ID",
                "visible": false
            }, {
                "field": "sku",
                "title": "SKU",
                "formatter": formatter
            }, {
                "field": "supplierName",
                "title": "供应商名称",
                "formatter": formatter
            }, {
                "field": "productName",
                "title": "产品名称",
                "formatter": formatter
            },{
                "field": "productNo",
                "title": "货号",
                "formatter": formatter
            }, {
                "field": "category",
                "title": "类目",
                "formatter": formatter
            },{
                "field": "origin",
                "title": "产地",
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

        function operateFormatter(value) {
            return "<nobr><a class='mr10'><i class='fa fa-edit'></i></a><a class='mr10'><i class='fa fa-times'></i></a><nobr>";
        }

        function getIdSelections() {
            return jQuery.map(inputTable.bootstrapTable('getSelections'), function (row) {
                return row.id;
            });
        }

        var productDetailTable = jQuery("#productDetailTable").bootstrapTable({
            "url": "${lx}/api/v1/market/product_detail/product/",
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
            "columns": [{
                "field": "id",
                "title": "ID",
                "visible": false
            }, {
                "field": "sku",
                "title": "SKU",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "supplierName",
                "title": "供应商名称",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "productName",
                "title": "产品名称",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "productNo",
                "title": "货号",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "color",
                "title": "颜色",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "size",
                "title": "尺码",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "feature",
                "title": "事物特征",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "price",
                "title": "单价",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "weight",
                "title": "重量",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "category",
                "title": "类目",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "origin",
                "title": "产地",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "link",
                "title": "采购链接",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "alternateLink",
                "title": "备用链接",
                "editable": {
                    "type": "text"
                }
            }, {
                "field": "created",
                "title": "创建时间",
                "formatter": formatter
            }, {
                "field": "remark",
                "title": "备注",
                "editable": {
                    "type": "text"
                }
            }],
            onCheck: function (row) {
            },
            onUncheck: function (row) {

            },
            onCheckAll: function () {

            },
            onUncheckAll: function () {

            },
            onEditableSave: function (field, row, oldValue, $el) {
                var data = {"id": row.id};
                //为参数动态绑定值
                var field_value = eval("row." + field);
                data[field] = field_value;
                jQuery.ajax({
                    "url": "${lx}/api/v1/market/product_detail",
                    "method": "PUT",
                    "data": data
                });
            }
        });

        jQuery("#btn_remove").click(function () {
            var ids = getIdSelections();
            if (ids != null && ids.length > 0) {
                jQuery.ajax({
                    "url": "${lx}/api/v1/market/product/" + ids,
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

                //为Product赋值
                jQuery('#productId').val(row.id);
                jQuery('#sku').val(row.sku);
                jQuery('#link').val(row.link);
                jQuery('#alternateLink').val(row.alternateLink);

                productDetailTable.bootstrapTable('refresh', {
                    "url": "${lx}/api/v1/market/product_detail/product/" + row.id
                });

                jQuery("#order_detail").modal();

            }
        });

        jQuery("#organizationList").change(function(){
            organizationId = jQuery(this).find("option:selected").val();
            inputTable.bootstrapTable('refresh', {url: '${lx}/api/v1/market/product/' + organizationId});
            jQuery("#download_product").prop("href", "${lx}/market/product/download_product/" + organizationId);
        });

       jQuery("#uploadExcel").click(function(){
           jQuery(this).fileupload({
               url: '${lx}/market/product/upload/' + organizationId,
               dataType: 'json',
               maxNumberOfFiles: 1,
               maxFileSize: 5000000,
               submit: function (e, data) {
                   $('#progress').show();
               },
               done: function (e, data) {
                   $('#progress').fadeOut();
                   $("#message").text(data.result.message);
                   $("#message_fade").show();
               },
               fail: function(e, data){
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
