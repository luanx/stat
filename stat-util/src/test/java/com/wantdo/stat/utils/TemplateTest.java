package com.wantdo.stat.utils;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.shop.Platform;
import com.wantdo.stat.entity.shop.StockOrder;
import com.wantdo.stat.entity.shop.StockOrderItem;
import com.wantdo.stat.entity.shop.StockProduct;
import com.wantdo.stat.template.HtmlHelper;
import com.wantdo.stat.template.PDFHelper;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Date : 16/4/5
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class TemplateTest {

    @Test
    public void generatePDF() throws Exception{

        try {
            String outputFile = "template.pdf";
            Map<String, Object> variables = new HashMap<String, Object>();

            List<StockOrder> stockOrderList = Lists.newArrayList();

            StockOrder stockOrder = new StockOrder();
            stockOrder.setStockId("aaa");
            //stockOrder.setOrderId("bbb");
            stockOrder.setOutStock(new Date());
            Platform platform = new Platform();
            platform.setName("安致");
            stockOrder.setPlatform(platform);

            List<StockOrderItem> stockOrderItemList = Lists.newArrayList();
            StockOrderItem stockOrderItem1 = new StockOrderItem();
            stockOrderItem1.setSku("ccc");
            stockOrderItem1.setNum(1L);
            StockProduct stockProduct1 = new StockProduct();
            stockProduct1.setName("xxx");
            stockOrderItem1.setStockProduct(stockProduct1);

            StockOrderItem stockOrderItem2 = new StockOrderItem();
            stockOrderItem2.setSku("ddd");
            stockOrderItem2.setNum(1L);
            StockProduct stockProduct2 = new StockProduct();
            stockProduct2.setName("yyy");
            stockOrderItem2.setStockProduct(stockProduct2);

            stockOrderItemList.add(stockOrderItem1);
            stockOrderItemList.add(stockOrderItem2);
            stockOrder.setStockOrderItemList(stockOrderItemList);
            stockOrderList.add(stockOrder);

            variables.put("stockOrderList", stockOrderList);

            String basePath = PDFHelper.class.getClassLoader().getResource("").getPath();
            System.out.println(basePath);

            String htmlStr = HtmlHelper.generate("template.ftl", variables);

            OutputStream out = new FileOutputStream(outputFile);
            PDFHelper.generate(htmlStr, out);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
