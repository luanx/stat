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
    <title>采购充值</title>
</head>
<body>
<div class="panel panel-default">
    <form:form id="inputForm" action="${lx}/shopbalance/recharge" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${recharge.id}" />
        <input type="hidden" name="organizationId" value="${organization.id}" />
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
            <label class="col-sm-3 control-label">日期 </label>

            <div class="col-sm-9">
                <input type="text" class="form-control" placeholder="yyyy/mm/dd" name="date"
                       value="<fmt:formatDate value="${recharge.date}" pattern="yyyy/MM/dd"/>" pattern="yyyy/MM/dd"
                       disabled required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">店铺 </label>

            <div class="col-sm-6">
                <input type="text" id="organizationName" name="organizationName" class="form-control sub-val"
                       value="${organization.name}" disabled required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">金额 </label>

            <div class="col-sm-6">
                <input type="text" id="account" name="account" class="form-control sub-val" />
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
