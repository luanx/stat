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
    <title>组织机构</title>
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

            <div class="col-sm-9 col-lg-10">
                <div class="panel panel-default">
                    <div id="organizationTree">

                    </div>
                </div>
            </div>

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

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>
<script src="${lx}/static/js/bootstrap-treeview.js"></script>

<script>
    jQuery(document).ready(function () {
        jQuery.ajax({
            "url": "${lx}/api/v1/organization",
            "type": "GET",
            "dataType": "json",
            "success": function (data) {
                console.log(data);
                jQuery('#organizationTree').treeview({
                    //color: '#428bca',
                    levels: 99,
                    enableLinks: true,
                    data: data
                })
            }
        });
    });
</script>

</body>
</html>
