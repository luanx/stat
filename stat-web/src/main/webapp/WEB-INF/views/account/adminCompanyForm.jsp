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

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>公司管理</title>
</head>
<body>
<div class="panel panel-default">
    <form:form id="inputForm" action="${lx}/admin/company/${action}" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${company.id}" />
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
            <label class="col-sm-3 control-label">名称</label>

            <div class="col-sm-6">
                <input type="text" id="name" name="name" value="${company.name}" class="form-control"
                       required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">营业执照号码</label>

            <div class="col-sm-6">
                <input type="text" id="businessNo" name="businessNo" value="${company.businessNo}" class="form-control"
                       required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">税务登记证号码</label>

            <div class="col-sm-6">
                <input type="text" id="taxNo" name="taxNo" value="${company.taxNo}" class="form-control"
                       required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="organizationNo">组织机构代码证号码</label>

            <div class="col-sm-6">
                <input type="text" id="organizationNo" name="organizationNo"
                       class="form-control" value="${company.organizationNo}" required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">经营范围</label>

            <div class="col-sm-6">
                <textarea id="businesses" name="businesses" class="form-control" rows="5">${company.businesses}</textarea>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">地址</label>

            <div class="col-sm-6">
                <input type="text" id="address" name="address" value="${company.address}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">日期 </label>

            <div class="col-sm-6">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="yyyy/mm/dd" id="orderstart" name="orderstart"/>
                    <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
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

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {
        jQuery("#datepicker").datepicker({
            dateFormat: 'yy/mm/dd'
        });
    });
</script>
</body>
</html>
