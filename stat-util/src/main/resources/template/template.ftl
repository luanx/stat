<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <meta name="description" content="" />
    <meta name="author" content="" />

    <title>出库单</title>

    <link href="${basePath}/style.default.css" rel="stylesheet"/>
    <link href="${basePath}/bootstrap.min.css" rel="stylesheet"/>
    <style>
        @page {
            size: 8.5in 11in;
            @bottom-center {
                content: "page " counter(page) " of  " counter(pages);
            }
        }
    </style>

</head>
<body>

 <#list stockOrderList as stockOrder>
 <div class="contentpanel" style="page-break-after:always;">
     <div class="panel panel-default">
         <div class="panel-heading">
             <h2 style="text-align: center;font-family: SimSun;">出库单</h2>
         </div>
         <div class="panel-body">

             <div class="row">
                 <div class="col-sm-6 text-left">
                     <p><strong>销售单号:</strong> ${stockOrder.orderId?default("")}</p>

                     <p><strong>所属店铺:</strong> ${stockOrder.platform.name?default("")}</p>

                     <p><strong>出库时间:</strong> ${stockOrder.outStock?default("")}</p>

                     <p><strong>出库单号:</strong> ${stockOrder.stockId?default("")}</p>

                     <p><strong>目的地址:</strong> 美国</p>

                 </div>

             </div>
             <!-- row -->

             <div class="table-responsive">
                 <table class="table table-invoice">
                     <thead>
                     <tr>
                         <th>序号</th>
                         <th>SKU</th>
                         <th>商品名称</th>
                         <th>数量</th>
                     </tr>
                     </thead>
                     <tbody>
                         <#list stockOrder.stockOrderItemList as stockOrderItem>
                            <tr>
                                <td>${stockOrderItem_index + 1}</td>
                                <td>${stockOrderItem.sku?default("")}</td>
                                <td>${stockOrderItem.stockProduct.name?default("")}</td>
                                <td>${stockOrderItem.num?default("")}</td>
                            </tr>
                         </#list>
                     </tbody>
                 </table>

             </div>
             <!-- panel-body -->
         </div>
         <!-- panel -->
     </div>
 </div>
 </#list>
</body>
</html>