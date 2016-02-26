<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-25
  Time: 11:32:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>资源管理</title>
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

        <div class="btn-group mr10">
            <a class="btn btn-default" href="${lx}/admin/resource/create"><i class="fa fa-arrows mr5"></i> 添加</a>
        </div>

        <div class="table-responsive">
            <table class="table table-primary">
                <thead>
                <tr>
                    <th><nobr>资源名</nobr></th>
                    <th>权限</th>
                    <th>路径</th>
                    <th>描述</th>
                    <th>图标</th>
                    <th>注册日期</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${resources.content}" var="resource">
                    <tr>
                        <td><nobr>${resource.name}</nobr></td>
                        <td>${resource.permission}</td>
                        <td>${resource.url}</td>
                        <td>${resource.description}</td>
                        <td>${resource.icon}</td>
                        <td>
                            <fmt:formatDate value="${resource.created}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />
                        </td>
                        <td>
                            <div class="btn-group">
                                <a data-toggle="dropdown" class="dropdown-toggle">
                                    <i class="fa fa-cog"></i>
                                </a>
                                <ul role="menu" class="dropdown-menu pull-right">
                                    <li><a href="${lx}/admin/resource/update/${resource.id}">Edit</a></li>
                                    <li><a href="${lx}/admin/resource/delete/${resource.id}">Delete</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- table-responsive -->

        <tags:pagination page="${resources}" paginationSize="5" />


    </div>
    <!-- tab-pane -->

    <div id="added" class="tab-pane">
        <p><strong>Note:</strong> Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim
            veniam, quis nostrud exercitatio.</p>
    </div>
    <!-- tab-pane -->

    <div id="assigned" class="tab-pane">
        Assigned To Me
    </div>
    <!-- tab-pane -->

    <div id="unresolved" class="tab-pane">
        Unresolved
    </div>
    <!-- tab-pane -->

    <div id="resolved" class="tab-pane">
        Resolved Recently
    </div>
    <!-- tab-pane -->

</div>
<!-- tab-content -->
</body>
</html>
