<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-8-25
  Time: 17:12:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.wantdo.com/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<c:set var="lx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>订单管理</title>
</head>
<body>
<div class="panel panel-default">
    <form:form id="inputForm" action="${lx}/logistics/normal/${action}" method="post"
          class="form-horizontal form-bordered">
        <input type="hidden" name="id" value="${order.id}" />
        <input type="hidden" name="organizationId" value="${order.organization.id}" />
    <div class="panel-heading">
        <div class="panel-btns">
            <a href="" class="panel-close">&times;</a>
            <a href="" class="minimize">&minus;</a>
        </div>
        <h4 class="panel-title">Input Fields</h4>

        <p>Individual form controls automatically receive some global styling. All textual elements with <code>.form-control</code>
            are set to width: 100%; by default. Wrap labels and controls in <code>.form-group</code> for optimum
            spacing.</p>
    </div>
    <div class="panel-body panel-body-nopadding">

        <div class="form-group">
            <label class="col-sm-3 control-label">订单编号</label>

            <div class="col-sm-6">
                <input type="text" id="orderId" name="orderId" value="${order.orderId}" class="form-control"
                       required />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">创建时间</label>

            <div class="col-sm-6">
                <input type="text" id="purDate" name="purDate" value="<joda:format value="${order.purDate}" pattern="yyyy-MM-dd HH:mm:ss" />" class="form-control" disabled />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">付款时间</label>

            <div class="col-sm-6">
                <input type="text" id="payDate" name="payDate" value="<joda:format value="${order.payDate}" pattern="yyyy-MM-dd HH:mm:ss" />" class="form-control" disabled/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">买家邮箱</label>

            <div class="col-sm-6">
                <input type="text" id="buyerEmail" name="buyerEmail" value="${order.buyerEmail}" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">买家名称</label>

            <div class="col-sm-6">
                <input type="text" id="buyerName" name="buyerName" value="${order.buyerName}" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">买家联系电话</label>

            <div class="col-sm-6">
                <input type="text" id="buyerPhoneNumber" name="buyerPhoneNumber" value="${order.buyerPhoneNumber}" class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">配送方式</label>

            <div class="col-sm-6">
                <input type="text" id="shipServiceLevel" name="shipServiceLevel" value="${order.shipServiceLevel}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">收件人姓名</label>

            <div class="col-sm-6">
                <input type="text" id="recipientName" name="recipientName" value="${order.recipientName}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">配送地址1</label>

            <div class="col-sm-6">
                <input type="text" id="shipAddress1" name="shipAddress1" value="${order.shipAddress1}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">配送地址2</label>

            <div class="col-sm-6">
                <input type="text" id="shipAddress2" name="shipAddress2" value="${order.shipAddress2}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">配送地址3</label>

            <div class="col-sm-6">
                <input type="text" id="shipAddress3" name="shipAddress3" value="${order.shipAddress3}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">配送地址：城市</label>

            <div class="col-sm-6">
                <input type="text" id="shipCity" name="shipCity" value="${order.shipCity}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">配送地址：省/州</label>

            <div class="col-sm-6">
                <input type="text" id="shipState" name="shipState" value="${order.shipState}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">邮编</label>

            <div class="col-sm-6">
                <input type="text" id="shipPostalCode" name="shipPostalCode" value="${order.shipPostalCode}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">配送地址：国家</label>

            <div class="col-sm-6">
                <input type="text" id="shipCountry" name="shipCountry" value="${order.shipCountry}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">收件人电话</label>

            <div class="col-sm-6">
                <input type="text" id="shipPhoneNumber" name="shipPhoneNumber" value="${order.shipPhoneNumber}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">快递公司</label>

            <div class="col-sm-6">
                <input type="text" id="expressName" name="expressName" value="${order.express.name}"
                       class="form-control" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">快递单号</label>

            <div class="col-sm-6">
                <input type="text" id="trackno" name="trackno" value="${order.trackno}"
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

    </form:form>
    <!-- panel-footer -->

</div>
<!-- panel -->


<div class="panel panel-default">
    <div class="panel-heading">
        <div class="panel-btns">
            <a href="" class="panel-close">&times;</a>
            <a href="" class="minimize">&minus;</a>
        </div>
        <h4 class="panel-title">详情</h4>

        <p>Individual form controls automatically receive some global styling. All textual elements with <code>.form-control</code>
            are set to width: 100%; by default. Wrap labels and controls in <code>.form-group</code> for optimum
            spacing.</p>
    </div>

    <div class="tab-content">
        <div id="all" class="tab-pane active">
            <div class="table-responsive">
                <table class="table table-primary">
                    <thead>
                    <tr>
                        <th>商品Id</th>
                        <th>sku</th>
                        <th>商品名称</th>
                        <th>商品数量</th>
                        <th>价格</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${order.orderDetailList}" var="orderDetail">
                        <tr>
                            <td>${orderDetail.orderItemId}</td>
                            <td>${orderDetail.sku}</td>
                            <td>${orderDetail.productName}</td>
                            <td>${orderDetail.quantityPurchased}</td>
                            <td>${orderDetail.itemPrice}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

</body>
</html>
