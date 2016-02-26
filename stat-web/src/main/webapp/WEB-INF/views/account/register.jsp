<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-18
  Time: 14:07:05
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

    <title>Wantdo Admin 注册</title>

    <link href="${lx}/static/wro4j/css/all.css" rel="stylesheet">


</head>

<body class="signin">


<section>

  <div class="signuppanel">

    <div class="row">

      <div class="col-md-6">

        <div class="signup-info">
          <div class="logopanel">
            <h1><span>[</span> Wantdo <span>]</span></h1>
          </div>
          <!-- logopanel -->

          <div class="mb20"></div>

          <h5><strong>Wantdo Admin 注册</strong></h5>

          <p>Bracket is a theme that is perfect if you want to create your own content management, monitoring or any
            other system for your project.</p>

          <p>Below are some of the benefits you can have when purchasing this template.</p>

          <div class="mb20"></div>

          <div class="feat-list">
            <i class="fa fa-wrench"></i>
            <h4 class="text-success">Easy to Customize</h4>

            <p>Bracket is made using Bootstrap 3 so you can easily customize any element of this template following the
              structure of Bootstrap 3.</p>
          </div>

          <div class="feat-list">
            <i class="fa fa-compress"></i>
            <h4 class="text-success">Fully Responsive Layout</h4>

            <p>Bracket is design to fit on all browser widths and all resolutions on all mobile devices. Try to scale
              your browser and see the results.</p>
          </div>


          <h4 class="mb20">and much more...</h4>

        </div>
        <!-- signup-info -->

      </div>
      <!-- col-sm-6 -->

      <div class="col-md-6">

        <form id="inputForm" action="${lx}/register" method="post">

          <h3 class="nomargin">注册</h3>

          <p class="mt5 mb20">已经注册? <a href="${lx}/login"><strong>登录</strong></a></p>


            <div class="mb10">
                <label class="control-label">姓名 <span class="asterisk">*</span></label>
                <input type="text" id="name" name="name" class="form-control" required />
            </div>

          <div class="mb10">
            <label class="control-label">登录名 <span class="asterisk">*</span></label>
            <input type="text" id="loginName" name="loginName" class="form-control" required/>
          </div>

          <div class="mb10">
            <label class="control-label">密码 <span class="asterisk">*</span></label>
            <input type="password" id="plainPassword" name="plainPassword" class="form-control" required/>
          </div>

          <div class="mb10">
            <label class="control-label">确认密码 <span class="asterisk">*</span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required/>
          </div>

          <div class="mb10">
            <label class="control-label">邮箱</label>
            <input type="text" id="email" name="email" class="form-control" />
          </div>

            <div class="mb10">
                <label class="control-label">电话</label>
                <input type="text" id="phone" name="phone" class="form-control" />
            </div>
          <br />

          <button class="btn btn-success btn-block" type="submit">注册</button>
        </form>
      </div>
      <!-- col-sm-6 -->

    </div>
    <!-- row -->

    <div class="signup-footer">
      <div class="pull-left">
        &copy; 2014. All Rights Reserved. Bracket Bootstrap 3 Admin Template
      </div>
      <div class="pull-right">
        Created By: <a href="http://themepixels.com/" target="_blank">ThemePixels</a>
      </div>
    </div>

  </div>
  <!-- signuppanel -->

</section>


<script src="${lx}/static/wro4j/js/all.js"></script>

<script>
  jQuery(document).ready(function () {

      jQuery(".select2").select2({
          width: '100%',
          minimumResultsForSearch: -1
      });

      jQuery(".select2-2").select2({
          width: '100%'
      });

      //聚焦第一个输入框
      jQuery('#name').focus();

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

      //为inputForm注册validate函数
      $("#inputForm").validate({
          rules: {
              loginName: {
                  remote: "${lx}/register/checkLoginName"
              }
          },
          messages: {
              loginName: {
                  remote: "用户登录名已存在"
              }
          }
      });

  });
</script>

</body>
</html>
