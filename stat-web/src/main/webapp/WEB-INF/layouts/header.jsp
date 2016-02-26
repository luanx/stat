<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-9-16
  Time: 16:52:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>


<div class="headerbar">

    <a class="menutoggle"><i class="fa fa-bars"></i></a>

    <form class="searchform" action="#" method="post">
        <input type="text" class="form-control" name="keyword" placeholder="Search here..." />
    </form>

    <shiro:user>
        <div class="header-right">
            <ul class="headermenu">
                <li>
                    <div class="btn-group">
                        <button class="btn btn-default dropdown-toggle tp-icon" data-toggle="dropdown" href="#">
                            <i class="glyphicon glyphicon-user"></i>
                            <span class="badge">2</span>
                        </button>
<%--                        <div class="dropdown-menu dropdown-menu-head pull-right">
                            <h5 class="title">2 Newly Registered Users</h5>
                            <ul class="dropdown-list user-list">
                                <li class="new">
                                    <div class="thumb"><a href=""><img src="${lx}/static/images/photos/user1.png"
                                                                       alt="" /></a>
                                    </div>
                                    <div class="desc">
                                        <h5><a href="">Draniem Daamul (@draniem)</a> <span
                                                class="badge badge-success">new</span></h5>
                                    </div>
                                </li>
                                <li class="new">
                                    <div class="thumb"><a href=""><img src="${lx}/static/images/photos/user2.png"
                                                                       alt="" /></a>
                                    </div>
                                    <div class="desc">
                                        <h5><a href="">Zaham Sindilmaca (@zaham)</a> <span
                                                class="badge badge-success">new</span></h5>
                                    </div>
                                </li>
                                <li>
                                    <div class="thumb"><a href=""><img src="${lx}/static/images/photos/user3.png"
                                                                       alt="" /></a>
                                    </div>
                                    <div class="desc">
                                        <h5><a href="">Weno Carasbong (@wenocar)</a></h5>
                                    </div>
                                </li>
                                <li>
                                    <div class="thumb"><a href=""><img src="${lx}/static/images/photos/user4.png"
                                                                       alt="" /></a>
                                    </div>
                                    <div class="desc">
                                        <h5><a href="">Nusja Nawancali (@nusja)</a></h5>
                                    </div>
                                </li>
                                <li>
                                    <div class="thumb"><a href=""><img src="${lx}/static/images/photos/user5.png"
                                                                       alt="" /></a>
                                    </div>
                                    <div class="desc">
                                        <h5><a href="">Lane Kitmari (@lane_kitmare)</a></h5>
                                    </div>
                                </li>
                                <li class="new"><a href="">See All Users</a></li>
                            </ul>
                        </div>--%>
                    </div>
                </li>
                <li>
                    <div class="btn-group">
                        <button class="btn btn-default dropdown-toggle tp-icon" data-toggle="dropdown">
                            <i class="glyphicon glyphicon-envelope"></i>
                            <span class="badge">1</span>
                        </button>
                        <%--<div class="dropdown-menu dropdown-menu-head pull-right">
                            <h5 class="title">You Have 1 New Message</h5>
                            <ul class="dropdown-list gen-list">
                                <li class="new">
                                    <a>
                                        <span class="thumb"><img src="${lx}/static/images/photos/user1.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Draniem Daamul <span class="badge badge-success">new</span></span>
                      <span class="msg">Lorem ipsum dolor sit amet...</span>
                    </span>
                                    </a>
                                </li>
                                <li>
                                    <a>
                                        <span class="thumb"><img src="${lx}/static/images/photos/user2.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Nusja Nawancali</span>
                      <span class="msg">Lorem ipsum dolor sit amet...</span>
                    </span>
                                    </a>
                                </li>
                                <li>
                                    <a>
                                        <span class="thumb"><img src="${lx}/static/images/photos/user3.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Weno Carasbong</span>
                      <span class="msg">Lorem ipsum dolor sit amet...</span>
                    </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="">
                                        <span class="thumb"><img src="${lx}/static/images/photos/user4.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Zaham Sindilmaca</span>
                      <span class="msg">Lorem ipsum dolor sit amet...</span>
                    </span>
                                    </a>
                                </li>
                                <li>
                                    <a>
                                        <span class="thumb"><img src="${lx}/static/images/photos/user5.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Veno Leongal</span>
                      <span class="msg">Lorem ipsum dolor sit amet...</span>
                    </span>
                                    </a>
                                </li>
                                <li class="new"><a href="">Read All Messages</a></li>
                            </ul>
                        </div>--%>
                    </div>
                </li>
                <li>
                    <div class="btn-group">
                        <button class="btn btn-default dropdown-toggle tp-icon" data-toggle="dropdown">
                            <i class="glyphicon glyphicon-globe"></i>
                            <span class="badge">5</span>
                        </button>
                        <%--<div class="dropdown-menu dropdown-menu-head pull-right">
                            <h5 class="title">You Have 5 New Notifications</h5>
                            <ul class="dropdown-list gen-list">
                                <li class="new">
                                    <a>
                                        <span class="thumb"><img src="${lx}/static/images/photos/user4.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Zaham Sindilmaca <span class="badge badge-success">new</span></span>
                      <span class="msg">is now following you</span>
                    </span>
                                    </a>
                                </li>
                                <li class="new">
                                    <a>
                                        <span class="thumb"><img src="${lx}/static/images/photos/user5.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Weno Carasbong <span class="badge badge-success">new</span></span>
                      <span class="msg">is now following you</span>
                    </span>
                                    </a>
                                </li>
                                <li class="new">
                                    <a>
                                        <span class="thumb"><img src="${lx}/static/images/photos/user3.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Veno Leongal <span class="badge badge-success">new</span></span>
                      <span class="msg">likes your recent status</span>
                    </span>
                                    </a>
                                </li>
                                <li class="new">
                                    <a href="">
                                        <span class="thumb"><img src="${lx}/static/images/photos/user3.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Nusja Nawancali <span class="badge badge-success">new</span></span>
                      <span class="msg">downloaded your work</span>
                    </span>
                                    </a>
                                </li>
                                <li class="new">
                                    <a>
                                        <span class="thumb"><img src="${lx}/static/images/photos/user3.png"
                                                                 alt="" /></span>
                    <span class="desc">
                      <span class="name">Nusja Nawancali <span class="badge badge-success">new</span></span>
                      <span class="msg">send you 2 messages</span>
                    </span>
                                    </a>
                                </li>
                                <li class="new"><a>See All Notifications</a></li>
                            </ul>
                        </div>--%>
                    </div>
                </li>
                <li>
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                            <img src="${lx}/static/images/photos/loggeduser.png" alt="" />
                            <shiro:principal property="loginName" />
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-usermenu pull-right">
                            <li><a href="${lx}/profile"><i class="glyphicon glyphicon-user"></i> 个人中心</a></li>
                            <li><a><i class="glyphicon glyphicon-question-sign"></i> 帮助</a></li>
                            <li><a href="${lx}/logout"><i class="glyphicon glyphicon-log-out"></i> 退出</a></li>
                        </ul>
                    </div>
                </li>
                <li>
                    <button id="chatview" class="btn btn-default tp-icon chat-icon">
                        <i class="glyphicon glyphicon-comment"></i>
                    </button>
                </li>
            </ul>
        </div>
    </shiro:user>
    <!-- header-right -->

</div>
<!-- headerbar -->

<div class="pageheader">
    <h2><i class="fa fa-home"><a href="${lx}"></a></i> <sitemesh:title/> </h2>

    <div class="breadcrumb-wrapper">
        <span class="label">You are here:</span>
        <ol class="breadcrumb">
            <li><a href="${lx}">Wantdo</a></li>
            <li class="active"><sitemesh:title /></li>
        </ol>
    </div>
</div>
