<%@ page import="com.wantdo.stat.service.account.ShiroDbRealm" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-17
  Time: 21:41:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="lx" value="${pageContext.request.contextPath}" />
<div class="leftpanel">

  <div class="logopanel">
    <h1><span>[</span> Wantdo <span>]</span></h1>
  </div>
  <!-- logopanel -->

  <div class="leftpanelinner">

    <!-- This is only visible to small devices -->
    <%--<div class="visible-xs hidden-sm hidden-md hidden-lg">
      <div class="media userlogged">
        <img alt="" src="${lx}/static/images/photos/loggeduser.png" class="media-object">

        <div class="media-body">
          <h4>John Doe</h4>
          <span>"Life is so..."</span>
        </div>
      </div>

      <h5 class="sidebartitle actitle">Account</h5>
      <ul class="nav nav-pills nav-stacked nav-bracket mb30">
        <li><a href="profile.html"><i class="fa fa-user"></i> <span>Profile</span></a></li>
        <li><a href=""><i class="fa fa-cog"></i> <span>Account Settings</span></a></li>
        <li><a href=""><i class="fa fa-question-circle"></i> <span>Help</span></a></li>
        <li><a href="signout.html"><i class="fa fa-sign-out"></i> <span>Sign Out</span></a></li>
      </ul>
    </div>--%>

    <h5 class="sidebartitle">Navigation</h5>
    <ul class="nav nav-pills nav-stacked nav-bracket" id="navigation">
        <li class="active"><a href="${lx}"><i class="fa fa-home"></i> <span>Dashboard</span></a></li>
    </ul>


    <%--<div class="infosummary">
      <h5 class="sidebartitle">Information Summary</h5>
      <ul>
        <li>
          <div class="datainfo">
            <span class="text-muted">Daily Traffic</span>
            <h4>630, 201</h4>
          </div>
          <div id="sidebar-chart" class="chart"></div>
        </li>
        <li>
          <div class="datainfo">
            <span class="text-muted">Average Users</span>
            <h4>1, 332, 801</h4>
          </div>
          <div id="sidebar-chart2" class="chart"></div>
        </li>
        <li>
          <div class="datainfo">
            <span class="text-muted">Disk Usage</span>
            <h4>82.2%</h4>
          </div>
          <div id="sidebar-chart3" class="chart"></div>
        </li>
        <li>
          <div class="datainfo">
            <span class="text-muted">CPU Usage</span>
            <h4>140.05 - 32</h4>
          </div>
          <div id="sidebar-chart4" class="chart"></div>
        </li>
        <li>
          <div class="datainfo">
            <span class="text-muted">Memory Usage</span>
            <h4>32.2%</h4>
          </div>
          <div id="sidebar-chart5" class="chart"></div>
        </li>
      </ul>
    </div>--%>
    <!-- infosummary -->

  </div>
  <!-- leftpanelinner -->
</div>
<!-- leftpanel -->


