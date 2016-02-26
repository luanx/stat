<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-25
  Time: 15:30:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="lx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title></title>
</head>
<body>

    <div class="row">
        <div class="col-sm-3">
            <img src="${lx}/static/images/photos/profile-1.png" class="thumbnail img-responsive" alt="" />

            <div class="mb30"></div>

            <h5 class="subtitle">About</h5>

            <p class="mb30">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt
                ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitat... <a href="">Show
                    More</a></p>

            <h5 class="subtitle">Connect</h5>
            <ul class="profile-social-list">
                <li><i class="fa fa-twitter"></i> <a href="">twitter.com/eileensideways</a></li>
                <li><i class="fa fa-facebook"></i> <a href="">facebook.com/eileen</a></li>
                <li><i class="fa fa-youtube"></i> <a href="">youtube.com/eileen22</a></li>
                <li><i class="fa fa-linkedin"></i> <a href="">linkedin.com/4ever-eileen</a></li>
                <li><i class="fa fa-pinterest"></i> <a href="">pinterest.com/eileen</a></li>
                <li><i class="fa fa-instagram"></i> <a href="">instagram.com/eiside</a></li>
            </ul>

            <div class="mb30"></div>

            <h5 class="subtitle">Address</h5>
            <address>
                795 Folsom Ave, Suite 600<br>
                San Francisco, CA 94107<br>
                <abbr title="Phone">P:</abbr> (123) 456-7890
            </address>

        </div>
        <!-- col-sm-3 -->
        <div class="col-sm-9">

            <div class="profile-header">
                <h2 class="profile-name">${user.name}</h2>

                <div class="profile-location"><i class="fa fa-map-marker"></i> ${user.loginName}</div>
                <div class="profile-position"><i class="fa fa-briefcase"></i> ${user.email}</div>

                <div class="mb20"></div>

                <button class="btn btn-success mr5"><i class="fa fa-user"></i> Follow</button>
                <button class="btn btn-white"><i class="fa fa-envelope-o"></i> Message</button>
            </div>
            <!-- profile-header -->

            <!-- Nav tabs -->
            <ul class="nav nav-tabs nav-justified nav-profile">
                <li class="active"><a href="#activities" data-toggle="tab"><strong>今日收益</strong></a></li>
                <li><a href="#events" data-toggle="tab"><strong>个人权益</strong></a></li>
                <li><a href="#basic" data-toggle="tab"><strong>基本信息</strong></a></li>
                <li><a href="#account" data-toggle="tab"><strong>账号设置</strong></a></li>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content">
                <div class="tab-pane active" id="activities">

                </div>
                <div class="tab-pane" id="followers">

                </div>
                <div class="tab-pane" id="basic">
                    <div class="panel panel-default">
                        <form id="basicForm" action="${lx}/profile/basic" method="post"
                              class="form-horizontal form-bordered">
                            <input type="hidden" name="id" value="${user.id}" />

                            <div class="panel-heading">
                                <div class="panel-btns">
                                    <a href="" class="panel-close">&times;</a>
                                    <a href="" class="minimize">&minus;</a>
                                </div>
                                <h4 class="panel-title">Input Fields</h4>

                                <p>Individual form controls automatically receive some global styling. All textual
                                    elements
                                    with <code>.form-control</code> are set to width: 100%; by default. Wrap labels and
                                    controls in <code>.form-group</code> for optimum spacing.</p>
                            </div>
                            <div class="panel-body panel-body-nopadding">

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">上级组织机构</label>

                                    <div class="col-sm-6">
                                        <select class="form-control" name="parentId">
                                            <c:forEach items="${user.organizationList}" var="org">
                                                <option value="${org.id}">${org.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                            </div>
                            <!-- panel-body -->

                            <div class="panel-footer">
                                <div class="row">
                                    <div class="col-sm-6 col-sm-offset-3">
                                        <button class="btn btn-primary" type="submit">保存</button>
                                        &nbsp;
                                        <button class="btn btn-default">取消</button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="tab-pane" id="account">
                    <div class="panel panel-default">
                        <form id="accountForm" action="${lx}/profile/account" method="post"
                              class="form-horizontal form-bordered">
                            <input type="hidden" name="id" value="${user.id}"/>
                        <div class="panel-heading">
                            <div class="panel-btns">
                                <a href="" class="panel-close">&times;</a>
                                <a href="" class="minimize">&minus;</a>
                            </div>
                            <h4 class="panel-title">Input Fields</h4>

                            <p>Individual form controls automatically receive some global styling. All textual elements
                                with <code>.form-control</code> are set to width: 100%; by default. Wrap labels and
                                controls in <code>.form-group</code> for optimum spacing.</p>
                        </div>
                        <div class="panel-body panel-body-nopadding">

                            <div class="form-group">
                                <label class="col-sm-3 control-label">姓名</label>

                                <div class="col-sm-6">
                                    <input type="text" id="name" name="name" value="${user.name}" class="form-control"
                                           required />
                                </div>
                            </div>


                                <div class="form-group">
                                    <label class="col-sm-3 control-label" for="loginName">登录名</label>

                                    <div class="col-sm-6">
                                        <input type="text" id="loginName" name="loginName" value="${user.loginName}"
                                               class="form-control" readonly="readonly" required/>
                                    </div>
                                </div>



                                <div class="form-group">
                                    <label class="col-sm-3 control-label">密码</label>

                                    <div class="col-sm-6">
                                        <input type="password" id="plainPassword" name="plainPassword" class="form-control"
                                               placeholder="...Leave it blank if no change"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">确认密码</label>

                                    <div class="col-sm-6">
                                        <input type="password" id="confirmPassword" name="confirmPassword"
                                               class="form-control" placeholder="...Leave it blank if no change"/>
                                    </div>
                                </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">邮箱</label>

                                <div class="col-sm-6">
                                    <input type="text" id="email" name="email" value="${user.email}"
                                           class="form-control" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">电话</label>

                                <div class="col-sm-6">
                                    <input type="text" id="phone" name="phone" value="${user.phone}"
                                           class="form-control" />
                                </div>
                            </div>

                        </div>
                        <!-- panel-body -->

                        <div class="panel-footer">
                            <div class="row">
                                <div class="col-sm-6 col-sm-offset-3">
                                    <input class="btn btn-primary" type="submit" value="保存"/>
                                    &nbsp;
                                    <input class="btn btn-default" type="button" value="取消" onclick="history.back();"/>
                                </div>
                            </div>
                        </div>
                        <!-- panel-footer -->

                        </form>

                    </div>
                    <!-- panel -->
                </div>
            </div>
            <!-- tab-content -->

        </div>
        <!-- col-sm-9 -->
    </div>
    <!-- row -->

</div>
<!-- contentpanel -->
</body>
</html>
