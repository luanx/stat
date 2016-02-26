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
    <form:form id="inputForm" action="${lx}/logistics/receive/${action}" method="post" modelAttribute="purchaseDetail"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${purchaseDetail.id}" />
    <div class="panel-heading">
        <div class="panel-btns">
            <a href="" class="panel-close">&times;</a>
            <a href="" class="minimize">&minus;</a>
        </div>
        <h4 class="panel-title">基本信息</h4>

        <p>Individual form controls automatically receive some global styling. All textual elements with <code>.form-control</code>
            are set to width: 100%; by default. Wrap labels and controls in <code>.form-group</code> for optimum
            spacing.</p>
    </div>
    <div class="panel-body panel-body-nopadding">

        <div class="form-group">
            <label class="col-sm-3 control-label">订单编号</label>

            <div class="col-sm-6">
                <input type="text" id="purchaseOrderId" name="purchaseOrderId" value="${purchaseDetail.purchaseOrderId}" class="form-control"
                       required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">采购订单号</label>

            <div class="col-sm-6">
                <input type="text" id="platformOrderId  " name="platformOrderId" value="${purchaseDetail.platformOrderId}" class="form-control"
                       required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">采购时间</label>

            <div class="col-sm-6">
                <input type="text" id="purchaseDate  " name="purchaseDate" value="<fmt:formatDate value="${purchaseDetail.purchaseDate}" pattern="yyyy-MM-dd" />" class="form-control"
                       disabled/>
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label">SKU</label>

            <div class="col-sm-6">
                <input type="text" id="sku  " name="sku" value="${purchaseDetail.sku}" class="form-control"
                       required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">商品名称</label>

            <div class="col-sm-6">
                <input type="text" id="productName  " name="productName" value="${purchaseDetail.productName}" class="form-control"
                       required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">事物特征</label>

            <div class="col-sm-6">
                <input type="text" id="productFeature" name="productFeature" value="${purchaseDetail.productFeature }"
                       class="form-control"
                       required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">数量</label>

            <div class="col-sm-6">
                <input type="text" id="quantity" name="quantity" value="${purchaseDetail.quantity }"
                       class="form-control"
                       required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">订单状态</label>

            <div class="col-sm-6">
                <div class="radio">
                    <form:bsradiobuttons path="receiveStatus.id" items="${allStatus}" labelCssClass="inline"/>
                </div>
            </div>

        </div>

    </div>
    <!-- panel-body -->

    <div class="panel-footer">
        <div class="row">
            <div class="col-sm-6 col-sm-offset-3">
                <input class="btn btn-primary" type="submit" value="保存"/>
                &nbsp;
                <input class="btn btn-default" type="button" value="取消" onclick="history.back();"/>
            </div>
        </div>
    </div>

    </form:form>
    <!-- panel-footer -->

</div>
<!-- panel -->

</body>
</html>
