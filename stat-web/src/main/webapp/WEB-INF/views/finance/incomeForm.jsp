<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-27
  Time: 19:02:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title></title>
</head>
<body>
<div class="row">
    <div class="col-sm-3 col-lg-2">
        <ul class="nav nav-pills nav-stacked nav-email">
            <li class="active">
                <a href="${lx}/finance/cashflow">
                    <i class="glyphicon glyphicon-inbox"></i> 汇总情况
                </a>
            </li>
            <li><a href="${lx}/finance/income"><i class="glyphicon glyphicon-star"></i> 收入情况</a></li>
            <li><a href="${lx}/finance/expense"><i class="glyphicon glyphicon-send"></i> 支出情况</a></li>
        </ul>

        <div class="mb30"></div>

    </div>

    <div class="col-sm-9 col-lg-10">
        <div class="panel panel-default">
            <form id="inputForm" action="${lx}/finance/income/create" method="post"
                  class="form-horizontal form-bordered">
            <div class="panel-heading">
                <div class="panel-btns">
                    <a href="" class="panel-close">&times;</a>
                    <a href="" class="minimize">&minus;</a>
                </div>
                <h4 class="panel-title">Basic Form Validation</h4>

                <p>Please provide your name, email address (won't be published) and a comment.</p>
            </div>
            <div class="panel-body ">
                <input type="hidden" name="id" value="${income.id}"/>

                <div class="form-group">
                    <label class="col-sm-3 control-label">日期 </label>

                    <div class="col-sm-9">
                        <input type="text" class="form-control" placeholder="yyyy/mm/dd"  name="date"
                               value="<fmt:formatDate value="${income.date}" pattern="yyyy/MM/dd"/>" pattern="yyyy/MM/dd" disabled required/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">现金 </label>

                    <div class="col-sm-9">
                        <input type="text" id="cash" name="cash" class="form-control sub-val"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">营业收入 </label>

                    <div class="col-sm-9">
                        <input type="text" id="busMain" name="busMain" class="form-control input-sm mb15 sub-val" placeholder="主营收入"/>
                        <input type="text" id="busOther" name="busOther" class="form-control input-sm mb15 sub-val" placeholder="其他收入"/>
                    </div>

                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">应付款 </label>

                    <div class="col-sm-9">
                        <input type="text" id="payBorrowAccount" name="payBorrowAccount" class="form-control input-sm mb15 sub-val" placeholder="借款" value="${income.payBorrowAccount}"/>
                        <input type="text" id="payLoan" name="payLoan" class="form-control input-sm mb15 sub-val" placeholder="贷款" />
                    </div>

                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">应收款 </label>

                    <div class="col-sm-9">
                        <input type="text" id="receivable" name="receivable" class="form-control sub-val" />
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">其他 </label>

                    <div class="col-sm-9">
                        <input type="text" id="other" name="other" class="form-control sub-val"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">组织机构</label>

                    <div class="col-sm-9">
                        <select class="form-control" name="organizationId">
                            <c:forEach items="${organizations}" var="org" varStatus="orgStatus">
                                <c:choose>
                                    <c:when test="${orgStatus.index==0}">
                                        <option value="${org.id}" selected>${org.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${org.id}">${org.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">备注 </label>

                    <div class="col-sm-9">
                        <input type="text" id="remark" name="remark" class="form-control input-sm mb15"
                               value="${income.remark}"/>
                    </div>
                </div>

            </div>



            <!-- panel-body -->
            <div class="panel-footer">
                <div class="row">
                    <div class="col-sm-9 col-sm-offset-3">
                        <input class="btn btn-primary" type="submit" value="保存"/>
                        &nbsp;
                        <input class="btn btn-default" type="button" value="取消" onclick="history.back();"/>
                    </div>
                </div>
            </div>
            </form>

        </div>
        <!-- panel -->

    </div>
    <!-- col-sm-3 -->


</div>
<!-- row -->

<script src="${lx}/static/js/jquery-1.11.1.min.js"></script>

<script>
    jQuery(document).ready(function () {
        jQuery("#inputForm").submit(function(){
            jQuery(".sub-val").each(function(){
                var _this = $(this);
                console.log(_this.val() == "");
                if(_this.val() == ""){
                    _this.val("0.00");
                }
            });
        });
    });
</script>

</body>
</html>
