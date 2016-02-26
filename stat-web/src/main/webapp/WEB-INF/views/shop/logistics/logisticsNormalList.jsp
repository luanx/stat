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

        <form id="inputForm" action="${lx}/logistics/normal" class="form-horizontal form-bordered">


        <c:if test="${not empty message}">
            <div class="alert alert-info fade in">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <p>${message}</p>

            </div>
        </c:if>

        <div class="btn-group mr10">
            <span class="btn btn-success fileinput-button">
                <i class="glyphicon glyphicon-upload"></i>
                <span> 回填订单</span>
                <input id="uploadExcel" type="file" name="uploadExcel" multiple />
            </span>

        </div>

        <div class="btn-group mr10">
            <a href="${lx}/logistics/normal" class="btn btn-default"><i class="fa fa-comments mr5"></i> 刷新</a>
            <a href="${lx}/logistics/normal/download" class="btn btn-default"><i class="fa  fa-mail-forward"></i> 下载订单</a>
        </div>

            <div class="pull-right">
                <div class="col-sm-8">
                    <input type="text" class="form-control" name="search_LIKE_orderId" />
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
                    <th><nobr>订单编号</nobr></th>
                    <th><nobr>店铺</nobr></th>
                    <th><nobr>创建时间</nobr></th>
                    <th><nobr>付款时间</nobr></th>
                    <th><nobr>买家名称</nobr></th>
                    <th><nobr>快递公司</nobr></th>
                    <th><nobr>快递单号</nobr></th>
                    <th><nobr>状态</nobr></th>
                    <th><nobr>操作</nobr></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders.content}" var="order">
                    <tr>
                        <td>
                            <nobr>${order.orderId}</nobr>
                        </td>
                        <td><nobr>${order.organization.name}</nobr></td>
                        <td>
                            <joda:format value="${order.purDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                        </td>
                        <td>
                            <joda:format value="${order.payDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                        </td>
                        <td>${order.buyerName}</td>
                        <td>${order.express.name}</td>
                        <td>${order.trackno}</td>
                        <td><nobr>${order.orderStatus.description}</nobr></td>
                        <td>
                            <div class="btn-group">
                                <a data-toggle="dropdown" class="dropdown-toggle">
                                    <i class="fa fa-cog"></i>
                                </a>
                                <ul role="menu" class="dropdown-menu pull-right">
                                    <li><a href="${lx}/logistics/normal/update/${order.id}">Edit</a></li>
                                    <li><a href="${lx}/logistics/normal/delete${order.id}">Delete</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- table-responsive -->

        <tags:pagination page="${orders}" paginationSize="5" />

        </form>

    </div>
    <!-- tab-pane -->


</div>
<!-- tab-content -->

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {
        jQuery('#uploadExcel').fileupload({
            url: '${lx}/logistics/normal/upload',
            dataType: 'json',
            maxNumberOfFiles: 1,
            maxFileSize: 2000000,
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
