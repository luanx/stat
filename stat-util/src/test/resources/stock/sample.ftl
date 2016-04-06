<html>
<head>
    <title></title>
    <style>
        table {
            width: 100%;
            border: green dotted;
            border-width: 2 0 0 2
        }

        td {
            border: green dotted;
            border-width: 0 2 2 0
        }

        @page {
            size: 8.5in 11in;
            @bottom-center {
                content: "page " counter(page) " of  " counter(pages);
            }
        }
    </style>
</head>
<body>

<#list userList as user>
<div style="page-break-after:always;">
    <div align="center">
        <h1>${user.name}</h1>
    </div>
</div>
</#list>

</body>
</html>