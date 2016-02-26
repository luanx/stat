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

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>组织机构管理</title>
</head>
<body>
<div class="panel panel-default">
    <form:form id="inputForm" modelAttribute="organization" action="${lx}/admin/organization/${action}" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${organization.id}" />
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
                    <input type="text" id="name" name="name" class="form-control" value="${organization.name}" required/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label">描述</label>

                <div class="col-sm-6">
                    <input type="text" id="description" name="description" class="form-control" value="${organization.description}" required />
                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-3 control-label">上级组织机构</label>
                <div class="col-sm-6">
                    <select class="form-control" name="parentId">
                        <c:forEach items="${organizations}" var="org">
                            <option value="${org.id}">${org.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label">层级</label>

                <div class="col-sm-6">
                    <input type="text" id="level" name="level" class="form-control"
                           value="${organization.level}" />
                </div>
            </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">是否叶子节点</label>

            <div class="col-sm-6">
                <div class="radio">
                    <form:bsradiobuttons path="isLeaf" items="${leafStatus}" labelCssClass="inline" />
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
        jQuery('#level').spinner();
    });
</script>
</body>
</html>
