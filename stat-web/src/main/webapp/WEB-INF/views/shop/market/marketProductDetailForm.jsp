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
    <form:form id="inputForm" action="${lx}/market/product_detail/${action}" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${productDetail.id}" />
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
            <label class="col-sm-3 control-label">sku</label>

            <div class="col-sm-6">
                <input type="text" id="sku" name="sku" value="${productDetail.sku}" class="form-control"
                       required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">供应商名称</label>

            <div class="col-sm-6">
                <input type="text" id="supplierName" name="supplierName" value="${productDetail.supplierName}" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">货号</label>

            <div class="col-sm-6">
                <input type="text" id="productNo" name="productNo" value="${productDetail.productNo}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">颜色</label>

            <div class="col-sm-6">
                <input type="text" id="color" name="color" value="${productDetail.color}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">尺码</label>

            <div class="col-sm-6">
                <input type="text" id="size" name="size" value="${productDetail.size}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">事物特征</label>

            <div class="col-sm-6">
                <input type="text" id="feature" name="feature" value="${productDetail.feature}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">单价</label>

            <div class="col-sm-6">
                <input type="text" id="price" name="price" value="${productDetail.price}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">重量</label>

            <div class="col-sm-6">
                <input type="text" id="weight" name="weight" value="${productDetail.weight}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">采购链接</label>

            <div class="col-sm-6">
                <input type="text" id="link" name="link" value="${productDetail.link}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">备用链接</label>

            <div class="col-sm-6">
                <input type="text" id="alternateLink" name="alternateLink" value="${productDetail.alternateLink}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">备注</label>

            <div class="col-sm-6">
                <input type="text" id="remark" name="remark" value="${productDetail.remark}"
                       class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">父体sku</label>

            <div class="col-sm-6">
                <input type="text" id="parentSku" name="parentSku" value="${productDetail.product.sku}"
                       class="form-control" disabled/>
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
