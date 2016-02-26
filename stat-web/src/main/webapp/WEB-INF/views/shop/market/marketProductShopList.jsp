<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-27
  Time: 19:02:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="lx" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title>店铺管理</title>
</head>
<body>

    <div class="mb30"></div>

    <div class="people-list">
        <div class="row">
            <c:forEach items="${organizations}" var="organization">
                <div class="col-md-6">
                    <div class="people-item">
                        <div class="media">
                            <a href="#" class="pull-left">
                                <img alt="" src="${lx}/static/images/photos/shop.jpg" class="thumbnail media-object">
                            </a>

                            <div class="media-body">
                                <h4 class="person-name"><a href="${lx}/market/product/${organization.id}">${organization.name}</a></h4>

                                <div class="text-muted"><i class="fa fa-map-marker"></i> Cebu City, Philippines</div>
                                <div class="text-muted"><i class="fa fa-briefcase"></i> Software Engineer at <a href="">SomeCompany,
                                    Inc.</a></div>
                                <ul class="social-list">
                                    <li><a href="" class="tooltips" data-toggle="tooltip" data-placement="top"
                                           title="Email"><i class="fa fa-envelope-o"></i></a></li>
                                    <li><a href="" class="tooltips" data-toggle="tooltip" data-placement="top"
                                           title="Facebook"><i class="fa fa-facebook"></i></a></li>
                                    <li><a href="" class="tooltips" data-toggle="tooltip" data-placement="top"
                                           title="Twitter"><i class="fa fa-twitter"></i></a></li>
                                    <li><a href="" class="tooltips" data-toggle="tooltip" data-placement="top"
                                           title="LinkedIn"><i class="fa fa-linkedin"></i></a></li>
                                    <li><a href="" class="tooltips" data-toggle="tooltip" data-placement="top"
                                           title="Skype"><i class="fa fa-skype"></i></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- col-md-6 -->
            </c:forEach>

        </div>
        <!-- row -->
    </div>
    <!-- people-list -->


</body>
</html>
