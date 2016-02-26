<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-25
  Time: 17:12:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.wantdo.com/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>订单管理</title>
</head>
<body>
<div class="panel panel-default">
    <form:form id="inputForm" action="${lx}/market/normal/order_detail/${action}" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${orderDetail.id}" />
    <div class="panel-heading">
        <div class="panel-btns">
            <a href="" class="panel-close">&times;</a>
            <a href="" class="minimize">&minus;</a>
        </div>
        <h4 class="panel-title">Input Fields</h4>

        <p>Individual form controls automatically receive some global styling. All textual elements with <code>.form-control</code>
            are set to width: 100%; by default. Wrap labels and controls in <code>.form-group</code> for optimum
            spacing.</p>
    </div>
    <div class="panel-body panel-body-nopadding">

        <div class="form-group">
            <label class="col-sm-3 control-label">订单编号</label>

            <div class="col-sm-6">
                <input type="text" id="orderId" name="orderId" value="${orderDetail.orderId}" class="form-control"
                       required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">创建时间</label>

            <div class="col-sm-6">
                <input type="text" id="purDate" name="purDate" value="<joda:format value='${orderDetail.purDate}' pattern="yyyy-MM-dd HH:mm:ss"/>M" class="form-control"
                       disabled required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">付款时间</label>

            <div class="col-sm-6">
                <input type="text" id="payDate" name="payDate"
                       value="<joda:format value='${orderDetail.payDate}' pattern="yyyy-MM-dd HH:mm:ss"/>M"
                       class="form-control"
                       disabled required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">sku</label>

            <div class="col-sm-6">
                <input type="text" id="sku" name="sku" value="${orderDetail.sku}" class="form-control"
                       required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">产品名称</label>

            <div class="col-sm-6">
                <input type="text" id="productName" name="productName" value="${orderDetail.productName}" class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">数量</label>

            <div class="col-sm-6">
                <input type="text" id="quantityPurchased" name="quantityPurchased" value="${orderDetail.quantityPurchased}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">单价</label>

            <div class="col-sm-6">
                <input type="text" id="itemPrice" name="itemPrice"
                       value="${orderDetail.itemPrice}"
                       class="form-control"/>
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label">运费</label>

            <div class="col-sm-6">
                <input type="text" id="shippingPrice" name="shippingPrice"
                       value="${orderDetail.shippingPrice}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">配送方式</label>

            <div class="col-sm-6">
                <input type="text" id="shipServiceLevel" name="shipServiceLevel"
                       value="${orderDetail.shipServiceLevel}"
                       class="form-control"/>
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label">买家姓名</label>

            <div class="col-sm-6">
                <input type="text" id="buyerName" name="buyerName" value="${orderDetail.buyerName}" class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">买家邮箱</label>

            <div class="col-sm-6">
                <input type="text" id="buyerEmail" name="buyerEmail" value="${orderDetail.buyerEmail}" class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">买家电话号码</label>

            <div class="col-sm-6">
                <input type="text" id="buyerPhoneNumber" name="buyerPhoneNumber" value="${orderDetail.buyerPhoneNumber}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">收件人姓名</label>

            <div class="col-sm-6">
                <input type="text" id="recipientName" name="recipientName"
                       value="${orderDetail.recipientName}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">收件人地址</label>

            <div class="col-sm-6">
                <input type="text" id="shipAddress1" name="shipAddress1"
                       value="${orderDetail.shipAddress1}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">收件人电话</label>

            <div class="col-sm-6">
                <input type="text" id="shipPhoneNumber" name="shipPhoneNumber"
                       value="${orderDetail.shipPhoneNumber}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">城市</label>

            <div class="col-sm-6">
                <input type="text" id="shipCity" name="shipCity"
                       value="${orderDetail.shipCity}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">省/州</label>

            <div class="col-sm-6">
                <input type="text" id="shipState" name="shipState"
                       value="${orderDetail.shipState}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">邮编</label>

            <div class="col-sm-6">
                <input type="text" id="shipPostalCode" name="shipPostalCode"
                       value="${orderDetail.shipPostalCode}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">国家</label>

            <div class="col-sm-6">
                <input type="text" id="shipCountry" name="shipCountry"
                       value="${orderDetail.shipCountry}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">备注</label>

            <div class="col-sm-6">
                <input type="text" id="remark" name="remark"
                       value="${orderDetail.remark}"
                       class="form-control"/>
            </div>
        </div>

    </div>
    <!-- panel-body -->

    <div class="panel-footer">
        <div class="row">
            <div class="col-sm-6 col-sm-offset-3">
                <input class="btn btn-primary" type="submit" value="保存" />
                &nbsp;
                <input class="btn btn-default" type="button" value="取消" onclick="history.back();" />
            </div>
        </div>
    </div>

    </form:form>
    <!-- panel-footer -->

</div>
<!-- panel -->



</body>
</html>
