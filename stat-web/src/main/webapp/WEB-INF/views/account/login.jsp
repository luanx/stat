<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-18
  Time: 13:52:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="lx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="shortcut icon" href="${lx}/static/images/favicon.png" type="image/png">

  <title>Wantdo Admin 登录</title>

  <link href="${lx}/static/wro4j/css/all.css" rel="stylesheet">

</head>

<body class="signin">


<section>

  <div class="signinpanel">

    <div class="row">

      <div class="col-md-7">

        <div class="signin-info">
          <div class="logopanel">
            <h1><span>[</span> Wantdo <span>]</span></h1>
          </div>
          <!-- logopanel -->

          <div class="mb20"></div>

          <h5><strong>欢迎来到Wantdo Admin!</strong></h5>
          <ul>
            <li><i class="fa fa-arrow-circle-o-right mr5"></i> Fully Responsive Layout</li>
            <li><i class="fa fa-arrow-circle-o-right mr5"></i> HTML5/CSS3 Valid</li>
            <li><i class="fa fa-arrow-circle-o-right mr5"></i> Retina Ready</li>
            <li><i class="fa fa-arrow-circle-o-right mr5"></i> WYSIWYG CKEditor</li>
            <li><i class="fa fa-arrow-circle-o-right mr5"></i> and much more...</li>
          </ul>
          <div class="mb20"></div>
          <strong>没有账号? <a href="${lx}/register">注册</a></strong>
        </div>
        <!-- signin0-info -->

      </div>
      <!-- col-sm-7 -->

      <div class="col-md-5">

        <form id="inputForm" action="${lx}/login" method="post">
          <h4 class="nomargin">登录</h4>


          <input type="text" id="username" name="username" class="form-control uname" placeholder="Username" />
          <input type="password" id="password" name="password" class="form-control pword" placeholder="Password" />
          <a>
            <small>忘记密码?</small>
          </a>
          <button class="btn btn-success btn-block" type="submit">登录</button>

        </form>
      </div>
      <!-- col-sm-5 -->

    </div>
    <!-- row -->

    <div class="signup-footer">
      <div class="pull-left">
        Copyright &nbsp; 2008-2015 <a href="http://www.wantdo.com">wantdo.com</a>
      </div>
      <div class="pull-right">
        Created By: <a href="http://www.wantdo.com" target="_blank">wantdo</a>
      </div>
    </div>

  </div>
  <!-- signin -->

</section>


<script src="${lx}/static/wro4j/js/all.js"></script>

<script>
  jQuery(document).ready(function () {

    // Please do not use the code below
    // This is for demo purposes only
    var c = jQuery.cookie('change-skin');
    if (c && c == 'greyjoy') {
      jQuery('.btn-success').addClass('btn-orange').removeClass('btn-success');
    } else if (c && c == 'dodgerblue') {
      jQuery('.btn-success').addClass('btn-primary').removeClass('btn-success');
    } else if (c && c == 'katniss') {
      jQuery('.btn-success').addClass('btn-primary').removeClass('btn-success');
    }
  });
</script>

</body>
</html>

