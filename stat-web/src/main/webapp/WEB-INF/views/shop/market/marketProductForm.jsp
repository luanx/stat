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
    <form:form id="inputForm" action="${lx}/market/product/${action}" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${product.id}" />
        <input type="hidden" name="organizationId" value="${product.organization.id}" />
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
                <input type="text" id="sku" name="sku" value="${product.sku}" class="form-control"
                       required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">sku</label>

            <div class="col-sm-6">
                <input type="text" id="supplierName" name="supplierName" value="${product.supplierName}" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">productNo</label>

            <div class="col-sm-6">
                <input type="text" id="productNo" name="productNo" value="${product.productNo}"
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

<div class="panel panel-default">
    <div class="panel-heading">
        <div class="panel-btns">
            <a href="" class="panel-close">&times;</a>
            <a href="" class="minimize">&minus;</a>
        </div>
        <h4 class="panel-title">详情</h4>

        <p>Individual form controls automatically receive some global styling. All textual elements with <code>.form-control</code>
            are set to width: 100%; by default. Wrap labels and controls in <code>.form-group</code> for optimum
            spacing.</p>
    </div>

    <div class="tab-content">
        <div id="all" class="tab-pane active">
            <div class="table-responsive">
                <table class="table table-primary">
                    <thead>
                    <tr>
                        <th><nobr>sku</nobr></th>
                        <th><nobr>颜色</nobr></th>
                        <th><nobr>尺码</nobr></th>
                        <th><nobr>事物特征</nobr></th>
                        <th><nobr>单价</nobr></th>
                        <th><nobr>重量</nobr></th>
                        <th><nobr>采购链接</nobr></th>
                        <th><nobr>操作</nobr></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${product.productDetailList}" var="productDetail">
                        <tr>
                            <td><a href="${lx}/market/product_detail/update/${productDetail.id}">${productDetail.sku}</a></td>
                            <td>${productDetail.color}</td>
                            <td>${productDetail.size}</td>
                            <td>${productDetail.feature}</td>
                            <td>${productDetail.price}</td>
                            <td>${productDetail.weight}</td>
                            <td><a href="${productDetail.link}" target="_blank">${productDetail.link}</a></td>
                            <td>
                                <div class="btn-group">
                                    <a data-toggle="dropdown" class="dropdown-toggle">
                                        <i class="fa fa-cog"></i>
                                    </a>
                                    <ul role="menu" class="dropdown-menu pull-right">
                                        <li><a href="${lx}/market/product_detail/update/${productDetail.id}">Edit</a></li>
                                        <li><a href="${lx}/market/product_detail/delete/${productDetail.id}">Delete</a></li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

</body>
</html>
