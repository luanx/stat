<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-17
  Time: 15:36:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<c:set var="lx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="${lx}/static/images/favicon.png" type="image/png">

    <title>Wantdo Admin</title>

    <link href="${lx}/static/wro4j/css/all.css" rel="stylesheet">

  <sitemesh:head />
</head>

<body>

<!-- Preloader -->
<div id="preloader">
  <div id="status"><i class="fa fa-spinner fa-spin"></i></div>
</div>

<section>

  <%@ include file="/WEB-INF/layouts/left.jsp"%>

  <div class="mainpanel">

    <%@ include file="/WEB-INF/layouts/header.jsp"%>

    <div class="contentpanel">
        <sitemesh:body />
    </div>
    <!-- contentpanel -->

  </div>
  <!-- mainpanel -->

  <%--<%@ include file="/WEB-INF/layouts/right.jsp"%>--%>

</section>

    <script src="${lx}/static/wro4j/js/all.js"></script>
    <%--<script src="${lx}/static/js/layer/layer.js"></script>--%>

  <script>
      jQuery(document).ready(function(){
         jQuery.ajax({
             "url": "${lx}/api/v1/user",
             "type": "GET",
             "dataType": "json",
             "success": function(data){
                 if (data != null){
                     $.each(data, function() {
                         var resource = jQuery(this)[0];
                         var append = "";
                         if (resource.children != null && resource.children.length != 0){
                             append += '<li class="nav-parent"><a><i class="fa ' + resource.icon + '"></i><span>' + resource.name + '</span></a>';
                             append += '<ul class="children">';
                             jQuery.each(resource.children, function(){
                                var children = jQuery(this)[0];
                                append += '<li><a href="${lx}' + children.url + '"><i class="fa ' + children.icon + '"></i> ' + children.name + '</a></li>';
                             });
                             append += '</ul>';
                             append += '</li>';
                         } else {
                             append += '<li><a><i class="fa ' + resource.icon + '"></i><span>' + resource.name + '</span></a></li>';
                         }
                         jQuery('#navigation').append(append);
                     });
                 }
             }
         });
      });
  </script>

</body>

</html>
