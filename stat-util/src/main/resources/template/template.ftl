<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <meta name="description" content="" />
    <meta name="author" content="" />

    <title>出库单</title>

    <link href="file:///usr/local/ideaIU_workspace/stat/stat-util/src/main/resources/template/style.default.css" rel="stylesheet"/>
    <link href="file:///usr/local/ideaIU_workspace/stat/stat-util/src/main/resources/template/bootstrap.min.css" rel="stylesheet"/>
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
<div class="contentpanel">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h2 style="text-align: center;font-family: SimSun;">出库单</h2>
        </div>
        <div class="panel-body">

            <div class="row">
                <div class="col-sm-6 text-left">
                    <p><strong>销售单号:</strong> January 20, 2014</p>
                    <p><strong>所属店铺:</strong> January 22, 2014</p>
                    <p><strong>出库时间:</strong> January 22, 2014</p>
                    <p><strong>出库单号:</strong> INV-000464F4-00</p>
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
                    <tr>
                        <td>1</td>
                        <td>aaa</td>
                        <td>bbbb</td>
                        <td>1</td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>aaa</td>
                        <td>bbbb</td>
                        <td>1</td>
                    </tr>

                </tbody>
            </table>

        </div>
        <!-- panel-body -->
    </div>
    <!-- panel -->
    </div>
</div>
</body>
</html>