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

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
  <title>出库管理</title>
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
        <a id="btn_out" class="btn btn-info" href="${lx}/stock/outstock/download"><i
                class="fa  fa-camera-retro"></i> 出库</a>
      </div>

      <select id="platformList" class="selectpicker mr5" data-width="100px"
              data-style="btn btn-default">
        <c:forEach items="${platforms}" var="platform">
          <option value="${platform.id}">${platform.name}</option>
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
              <label class="col-sm-3 control-label">销售订单号</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" name="orderId" id="orderId" disabled required/>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-3 control-label">出库单号</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" name="stockId" id="stockId" disabled required/>
              </div>
            </div>

            <div class="form-group">

              <label class="col-sm-3 control-label">出库时间</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" name="created" id="created" disabled/>
              </div>
            </div>

            <div class="form-group">
              <label class="col-sm-3 control-label">仓库</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" name="quantityPurchased" id="quantityPurchased"
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

    var platformId = ${platforms[0].id};

    var inputTable = jQuery("#inputTable").bootstrapTable({
      "url": "${lx}/api/v1/stock/outstock/" + platformId + "?stockType=1",
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
        "field": "ostatus",
        "checkbox": true
      }, {
        "field": "id",
        "title": "ID",
        "visible": false
      }, {
        "field": "orderId",
        "title": "销售订单号",
        "formatter": formatter
      }, {
        "field": "stockId",
        "title": "出库单号",
        "formatter": formatter
      }, {
        "field": "outStock",
        "title": "出库时间",
        "formatter": formatter
      }, {
        "field": "warehouse",
        "title": "仓库",
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


    jQuery("#btn_edit").click(function () {
      var ids = getIdSelections();
      if (ids == null || ids.length == 0) {
        jQuery("#no_checked").modal();
      } else if (ids.length > 1) {
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



    jQuery("#platformList").change(function () {
      platformId = jQuery(this).find("option:selected").val();
      var orderType = jQuery("#orderTypes").val();
      inputTable.bootstrapTable('refresh', {url: '${lx}/api/v1/stock/outstock/' + platformId + "?stockType=1&orderType=" + orderType});
    });

    jQuery("#orderTypes").change(function () {
      var orderType = jQuery(this).find("option:selected").val();
      inputTable.bootstrapTable('refresh', {url: '${lx}/api/v1/stock/outstock/' + platformId + "?stockType=1&orderType=" + orderType});
    });

    jQuery("#uploadExcel").click(function () {
      jQuery(this).fileupload({
        url: '${lx}/stock/outstock/upload',
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
