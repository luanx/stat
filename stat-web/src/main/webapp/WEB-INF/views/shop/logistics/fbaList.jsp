<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-25
  Time: 11:32:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<c:set var="lx" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title>订单管理</title>
</head>
<body>
<div class="tab-content">
    <div id="all" class="tab-pane active">

        <form id="inputForm" action="${lx}/logistics/fba" class="form-horizontal form-bordered">

        <c:if test="${not empty message}">
            <div class="alert alert-info fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <p>${message}</p>

            </div>
        </c:if>

        <div class="btn-group mr10">
            <span class="btn btn-success fileinput-button">
                <i class="glyphicon glyphicon-upload"></i>
                <span> 上传</span>
                <input id="uploadExcel" type="file" name="uploadExcel" multiple />
            </span>

        </div>

        <div class="btn-group mr10">
            <a href="${lx}/logistics/fba/create" class="btn btn-default"><i class="fa fa-arrows mr5"></i> 新建</a>
            <a href="${lx}/logistics/fba" class="btn btn-default"><i class="fa fa-comments mr5"></i> 刷新</a>
        </div>

        <div class="pull-right">
            <div class="col-sm-8">
                <input type="text" class="form-control" name="search_LIKE_amazonId" />
            </div>
            <div class="col-sm-4">
                <input type="submit" class="btn btn-info" value="查询" />
            </div>
        </div>

        <div id="progress" class="progress" style="display:none;">
            <div class="progress-bar progress-bar-success"></div>
        </div>

        <div class="table-responsive">
            <table class="table table-primary">
                <thead>
                <tr>
                    <th><nobr>FBA编号</nobr></th>
                    <th><nobr>快递公司</nobr></th>
                    <th><nobr>快递单号</nobr></th>
                    <th><nobr>FBA地址</nobr></th>
                    <th><nobr>店铺</nobr></th>
                    <th><nobr>操作</nobr></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${fbas.content}" var="fba">
                    <tr>
                        <td>
                            <nobr>${fba.amazonId}</nobr>
                        </td>
                        <td>${fba.shipMethod}</td>
                        <td>${fba.trackno}</td>
                        <td>${fba.shipAddress}</td>
                        <td>${fba.organization.name}</td>
                        <td>
                            <div class="btn-group">
                                <a data-toggle="dropdown" class="dropdown-toggle">
                                    <i class="fa fa-cog"></i>
                                </a>
                                <ul role="menu" class="dropdown-menu pull-right">
                                    <li><a href="${lx}/logistics/fba/update/${fba.id}">Edit</a></li>
                                    <li><a href="${lx}/logistics/fba/delete/${fba.id}">Delete</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- table-responsive -->

        <tags:pagination page="${fbas}" paginationSize="5" />


    </div>
    <!-- tab-pane -->


</div>
<!-- tab-content -->

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {
        jQuery('#uploadExcel').fileupload({
            url: '${lx}/logistics/fba/upload',
            dataType: 'json',
            maxNumberOfFiles: 1,
            maxFileSize: 10485760,
            submit: function (e, data){
              $('#progress').show();
            },
            done: function (e, data){
              $('#progress').fadeOut();
            },
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                jQuery('#progress .progress-bar').css(
                        'width',
                        progress + '%'
                );
            }
        }).prop('disabled', !$.support.fileInput)
                .parent().addClass($.support.fileInput ? undefined : 'disabled');
    });
</script>
</body>
</html>
