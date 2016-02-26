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
    <title>采购管理</title>
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

        <div class="table-responsive">
            <table class="table table-primary">
                <thead>
                <tr>
                    <th>店铺</th>
                    <th>日期</th>
                    <th>结余</th>
                    <th>更新时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${accounts}" var="account">
                    <tr>
                        <td>${account.organization.name}</td>
                        <td>
                            <fmt:formatDate value="${account.date}" pattern="yyyy/MM/dd" />
                        </td>
                        <td>${account.balance}</td>
                        <td>
                            <c:choose>
                                <c:when test="${account.modified == null}">
                                    <fmt:formatDate value="${account.created}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatDate value="${account.modified}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="btn-group">
                                <a data-toggle="dropdown" class="dropdown-toggle">
                                    <i class="fa fa-cog"></i>
                                </a>
                                <ul role="menu" class="dropdown-menu pull-right">
                                    <li><a href="${lx}/shopbalance/recharge/${account.organization.id}">recharge</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- table-responsive -->


    </div>
    <!-- tab-pane -->


</div>
<!-- tab-content -->

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {
        jQuery('#uploadExcel').fileupload({
            url: '${lx}/purchase/upload',
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
