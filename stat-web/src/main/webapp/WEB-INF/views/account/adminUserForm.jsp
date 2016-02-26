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
    <title>用户管理</title>
</head>
<body>
<div class="panel panel-default">
    <form:form id="inputForm" modelAttribute="user" action="${lx}/admin/user/update" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${user.id}" />
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
            <label class="col-sm-3 control-label">姓名</label>

            <div class="col-sm-6">
                <input type="text" id="name" name="name" value="${user.name}" class="form-control"
                       required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="loginName">登录名</label>

            <div class="col-sm-6">
                <input type="text" id="loginName" name="loginName" value="${user.loginName}"
                       class="form-control" readonly="readonly" required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="loginName">密码</label>

            <div class="col-sm-6">
                <input type="password" id="plainPassword" name="plainPassword"
                       class="form-control" placeholder="Leave it blank if no change..."/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="loginName">密码</label>

            <div class="col-sm-6">
                <input type="password" id="confirmPassword" name="confirmPassword"
                       class="form-control" placeholder="Leave it blank if no change..."/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">邮箱</label>

            <div class="col-sm-6">
                <input type="text" id="email" name="email" value="${user.email}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">电话</label>

            <div class="col-sm-6">
                <input type="text" id="phone" name="phone" value="${user.phone}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">状态</label>

            <div class="col-sm-6">
                <div class="radio">
                    <form:bsradiobuttons path="status" items="${allStatus}" labelCssClass="inline" />
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
