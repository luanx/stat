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
    <form:form id="inputForm" action="${lx}/logistics/fba/${action}" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${fba.id}" />
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
            <label class="col-sm-3 control-label">FBA编号</label>

            <div class="col-sm-6">
                <input type="text" id="amazonId" name="amazonId" value="${fba.amazonId}" class="form-control"
                       required />
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label">快递公司</label>

            <div class="col-sm-6">
                <input type="text" id="shipMethod" name="shipMethod" value="${fba.shipMethod}" class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">快递单号</label>

            <div class="col-sm-6">
                <input type="text" id="trackno" name="trackno" value="${fba.trackno}" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">FBA地址</label>

            <div class="col-sm-6">
                <input type="text" id="shipAddress" name="shipAddress" value="${fba.shipAddress}" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">店铺</label>

            <div class="col-sm-6">
                <select class="form-control" name="organizationId">
                    <c:forEach items="${organizations}" var="organization">
                        <c:choose>
                            <c:when test="${fba.organization.id == organization.id}">
                                <option value="${organization.id}" selected>${organization.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${organization.id}">${organization.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
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
                        <th>箱子编号</th>
                        <th>快递单号</th>
                        <th>重量</th>
                        <th>预估价格</th>
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${fba.fbaDetailList}" var="fbaDetail">
                            <tr>
                                <td>${fbaDetail.shipId}</td>
                                <td>${fbaDetail.trackno}</td>
                                <td>${fbaDetail.weight}</td>
                                <td>${fbaDetail.price}</td>
                                <td>${fbaDetail.status}</td>
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
