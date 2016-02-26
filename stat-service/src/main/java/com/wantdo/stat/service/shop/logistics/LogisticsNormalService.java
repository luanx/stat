package com.wantdo.stat.service.shop.logistics;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wantdo.stat.constant.DateFormatConstant;
import com.wantdo.stat.constant.PathConstant;
import com.wantdo.stat.dao.account.OrganizationDao;
import com.wantdo.stat.dao.shop.ExpressDao;
import com.wantdo.stat.dao.shop.OrderDao;
import com.wantdo.stat.dao.shop.OrderDetailDao;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.front.response.ResponseVoResultCode;
import com.wantdo.stat.entity.front.vo.ExcelVo;
import com.wantdo.stat.entity.shop.Express;
import com.wantdo.stat.entity.shop.Order;
import com.wantdo.stat.entity.shop.OrderDetail;
import com.wantdo.stat.excel.helper.ExcelExportHelper;
import com.wantdo.stat.excel.helper.ExcelReadHelper;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.Hibernates;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.persistence.SearchFilter.Operator;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.utils.Clock;
import com.wantdo.stat.utils.DateUtil;
import com.wantdo.stat.utils.Path;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日常打单
 *
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Component
@Transactional
public class LogisticsNormalService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(LogisticsNormalService.class);

    private OrderDao orderDao;

    private OrderDetailDao orderDetailDao;

    private ExpressDao expressDao;

    private OrganizationDao organizationDao;

    private Clock clock = Clock.DEFAULT;


    /**
     * 按Id获得订单
     */
    public Order getOrder(Long id) {
        return orderDao.findOne(id);
    }

    public void saveOrder(Order order) {
        order.setCreated(clock.getCurrentDate());
        orderDao.save(order);
    }

    public void updateOrder(Order order) {
        order.setModified(clock.getCurrentDate());
        orderDao.save(order);
    }

    public ResponseVo upload(MultipartFile uploadExcel){
        ResponseVo responseVo = ResponseVo.newInstance();
        String fileName = uploadExcel.getOriginalFilename();
        String suffix = Path.getSuffixFromPath(fileName);
        if (!suffix.equals("xls") && !suffix.equals("xlsx")) {
            responseVo.setResult(ResponseVoResultCode.CODE_PARAM_ERROR);
            responseVo.setMessage("文件格式错误，不是excel文件");
            return responseVo;
        }

        BufferedOutputStream bos = null;

        try {
            String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);

            String relativeFold = "logistics" + File.separator + "normal" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
            String fullRelativeFold = PathConstant.FILE_STORE_PATH + relativeFold;
            String relativePath = relativeFold + System.currentTimeMillis() + "." + suffix;
            String fullPath = PathConstant.FILE_STORE_PATH + relativePath;

            File fullFoldFile = new File(fullRelativeFold);
            if (!fullFoldFile.exists()){
                fullFoldFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(new File(fullPath));
            BufferedInputStream bis = new BufferedInputStream(uploadExcel.getInputStream());
            bos = new BufferedOutputStream(fos);
            final int BUFF_SIZE = 1024;
            byte[] buff = new byte[BUFF_SIZE];
            int len = 0;
            while ((len = bis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            bos.flush();
            bos.close();

            //解析Excel并写入数据库
            orderExcelRead(fullPath);

        } catch (Exception e) {

        } finally {

        }

        return responseVo;

    }

    private void orderExcelRead(String file) {

        try{
            ArrayList<ArrayList<Object>> rowList = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
            for(ArrayList<Object> row: rowList){

                String row0 = ExcelReadHelper.getCell(row.get(0));
                String orderId = "";
                if (row0 != null) {
                    orderId = row0;
                }

                String row1 = ExcelReadHelper.getCell(row.get(1));
                String orderItemId = "";
                if (row1 != null) {
                    orderItemId = row1;
                }


                Order orderExist = orderDao.findByOrderId(orderId);
                OrderDetail orderDetailExist = orderDetailDao.findByOrderIdAndOrderItemId(orderId, orderItemId);

                String row35 = ExcelReadHelper.getCell(row.get(35));
                String expressName = "";
                if (row35 != null){
                    expressName = row35;
                }

                Express express = expressDao.findByName(expressName);

                String row36 = ExcelReadHelper.getCell(row.get(36));
                String trackno = "";
                if (row36 != null) {
                    trackno = row36;
                }

                if (orderExist != null){
                    if (express != null){
                        orderExist.setExpress(express);
                    }
                    orderExist.setTrackno(trackno);
                    orderExist.setModified(clock.getCurrentDate());
                    orderDao.save(orderExist);
                }

                if (orderDetailExist != null){
                    if (express != null){
                        orderDetailExist.setExpress(express);
                    }
                    orderDetailExist.setTrackno(trackno);
                    orderDetailExist.setModified(clock.getCurrentDate());
                    orderDetailDao.save(orderDetailExist);
                }

            }
        } catch (Exception e){

        }
    }

    public void saveOrderList(List<Order> orderList){
        for(Order order: orderList){
            orderDao.save(order);
        }
    }

    public void saveOrderDetailList(List<OrderDetail> orderDetailList) {
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetailDao.save(orderDetail);
        }
    }

    public ExcelVo download(){
        ExcelVo excelVo = new ExcelVo();

        Date startDate = new Date(DateUtil.startOfToday());
        Date endDate = new Date(DateUtil.endOfToday());
        List<OrderDetail> orderDetailList = orderDetailDao.findAllToday(startDate, endDate);
        Map<String, List<OrderDetail>> shopOrderDetailMap = Maps.newHashMap();
        for (OrderDetail orderDetail : orderDetailList) {
            String shipServiceLevel = orderDetail.getShipServiceLevel();
            String expressName = "";
            if (shipServiceLevel.equals("Standard")){
                expressName = "E邮宝";
            } else if (shipServiceLevel.equals("Expedited")){
                expressName = "DHL";
            }
            List<OrderDetail> orderDetails = shopOrderDetailMap.get(expressName);
            if (orderDetails == null) {
                List<OrderDetail> newOrderDetails = Lists.newArrayList();
                newOrderDetails.add(orderDetail);
                shopOrderDetailMap.put(expressName, newOrderDetails);
            } else {
                orderDetails.add(orderDetail);
                shopOrderDetailMap.put(expressName, orderDetails);
            }
        }

        String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);
        String relativeFold = "logistics" + File.separator + "normal" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
        String fullRelativeFold = PathConstant.FILE_STORE_PATH + relativeFold;
        String fileName = System.currentTimeMillis() + ".xls";
        String relativePath = relativeFold + fileName;
        String fullPath = PathConstant.FILE_STORE_PATH + relativePath;

        File fullFoldFile = new File(fullRelativeFold);
        if (!fullFoldFile.exists()) {
            fullFoldFile.mkdirs();
        }

        this.orderExcelExport(shopOrderDetailMap, fullPath);
        excelVo.setName(fileName);
        excelVo.setPath(fullPath);

        return excelVo;
    }

    public void orderExcelExport(Map<String, List<OrderDetail>> shopOrderDetailMap, String fullPath){
        List<String> shopList = Lists.newArrayList();
        for (Map.Entry<String, List<OrderDetail>> shopOrderDetail : shopOrderDetailMap.entrySet()) {
            shopList.add(shopOrderDetail.getKey());
        }

        ExcelExportHelper excelExportHelper = new ExcelExportHelper(shopList);
        List<Sheet> sheetList = excelExportHelper.getSheets();
        for (Sheet sheet : sheetList) {
            excelExportHelper.createRow(sheet);
            excelExportHelper.setCellHeader(0, "order-id");
            excelExportHelper.setCellHeader(1, "order-item-id");
            excelExportHelper.setCellHeader(2, "purchase-date");
            excelExportHelper.setCellHeader(3, "payments-date");
            excelExportHelper.setCellHeader(4, "buyer-email");
            excelExportHelper.setCellHeader(5, "buyer-name");
            excelExportHelper.setCellHeader(6, "buyer-phone-number");
            excelExportHelper.setCellHeader(7, "sku");
            excelExportHelper.setCellHeader(8, "product-name");
            excelExportHelper.setCellHeader(9, "quantity-purchased");
            excelExportHelper.setCellHeader(10, "currency");
            excelExportHelper.setCellHeader(11, "item-price");
            excelExportHelper.setCellHeader(12, "item-tax");
            excelExportHelper.setCellHeader(13, "shipping-price");
            excelExportHelper.setCellHeader(14, "shipping-tax");
            excelExportHelper.setCellHeader(15, "ship-service-level");
            excelExportHelper.setCellHeader(16, "recipient-name");
            excelExportHelper.setCellHeader(17, "ship-address-1");
            excelExportHelper.setCellHeader(18, "ship-address-2");
            excelExportHelper.setCellHeader(19, "ship-address-3");
            excelExportHelper.setCellHeader(20, "ship-city");
            excelExportHelper.setCellHeader(21, "ship-state");
            excelExportHelper.setCellHeader(22, "ship-postal-code");
            excelExportHelper.setCellHeader(23, "ship-country");
            excelExportHelper.setCellHeader(24, "ship-phone-number");
            excelExportHelper.setCellHeader(25, "item-promotion-discount");
            excelExportHelper.setCellHeader(26, "item-promotion-id");
            excelExportHelper.setCellHeader(27, "ship-promotion-discount");
            excelExportHelper.setCellHeader(28, "ship-promotion-id");
            excelExportHelper.setCellHeader(29, "delivery-start-date");
            excelExportHelper.setCellHeader(30, "delivery-end-date");
            excelExportHelper.setCellHeader(31, "delivery-time-zone");
            excelExportHelper.setCellHeader(32, "delivery-Instructions");
            excelExportHelper.setCellHeader(33, "sales-channel");
            excelExportHelper.setCellHeader(34, "shop-name");
        }


        for (Map.Entry<String, List<OrderDetail>> shopOrderDetail : shopOrderDetailMap.entrySet()) {
            String shopName = shopOrderDetail.getKey();
            List<OrderDetail> orderDetailList = shopOrderDetail.getValue();
            Sheet sheet = null;
            for (int i = 0; i < shopList.size(); i++) {
                String shop = shopList.get(i);
                if (shop.equals(shopName)) {
                    sheet = sheetList.get(i);
                }
            }
            for (OrderDetail orderDetail : orderDetailList) {
                excelExportHelper.createRow(sheet);
                excelExportHelper.setCell(0, orderDetail.getOrderId());
                excelExportHelper.setCell(1, orderDetail.getOrderItemId());
                excelExportHelper.setCell(2, orderDetail.getPurDate().toString());
                excelExportHelper.setCell(3, orderDetail.getPayDate().toString());
                excelExportHelper.setCell(4, orderDetail.getBuyerEmail());
                excelExportHelper.setCell(5, orderDetail.getBuyerName());
                excelExportHelper.setCell(6, orderDetail.getBuyerPhoneNumber());
                excelExportHelper.setCell(7, orderDetail.getSku());
                excelExportHelper.setCell(8, orderDetail.getProductName());
                excelExportHelper.setCell(9, orderDetail.getQuantityPurchased());
                excelExportHelper.setCell(10, orderDetail.getCurrency());
                excelExportHelper.setCell(11, orderDetail.getItemPrice());
                excelExportHelper.setCell(12, orderDetail.getItemTax());
                excelExportHelper.setCell(13, orderDetail.getShippingPrice());
                excelExportHelper.setCell(14, orderDetail.getShippingTax());
                excelExportHelper.setCell(15, orderDetail.getShipServiceLevel());
                excelExportHelper.setCell(16, orderDetail.getRecipientName());
                excelExportHelper.setCell(17, orderDetail.getShipAddress1());
                excelExportHelper.setCell(18, orderDetail.getShipAddress2());
                excelExportHelper.setCell(19, orderDetail.getShipAddress3());
                excelExportHelper.setCell(20, orderDetail.getShipCity());
                excelExportHelper.setCell(21, orderDetail.getShipState());
                excelExportHelper.setCell(22, orderDetail.getShipPostalCode());
                excelExportHelper.setCell(23, orderDetail.getShipCountry());
                excelExportHelper.setCell(24, orderDetail.getShipPhoneNumber());
                excelExportHelper.setCell(25, orderDetail.getItemPromotionDiscount());
                excelExportHelper.setCell(26, orderDetail.getItemPromotionId());
                excelExportHelper.setCell(27, orderDetail.getShipPromotionDiscount());
                excelExportHelper.setCell(28, orderDetail.getShipPromotionId());
                excelExportHelper.setCell(29, orderDetail.getDeliveryStartDate());
                excelExportHelper.setCell(30, orderDetail.getDeliveryEndDate());
                excelExportHelper.setCell(31, orderDetail.getDeliveryTimeZone());
                excelExportHelper.setCell(32, orderDetail.getDeliveryInstructions());
                excelExportHelper.setCell(33, orderDetail.getSalesChannel());
                excelExportHelper.setCell(34, orderDetail.getOrganization().getName());
            }
        }

        excelExportHelper.export(fullPath);
    }


    public void deleteOrder(Long id) {
        orderDao.delete(id);
    }

    /**
     * 获取所有的订单
     */
    public List<Order> getAllOrder() {
        List<Order> orderList = (List<Order>) orderDao.findAll();
        for (Order order : orderList) {
            Hibernates.initLazyProperty(order.getOrderDetailList());
        }
        return orderList;
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }

    /**
     * 按页面传来的查询条件查询订单
     */
    public Page<Order> SearchOrder( Map<String, Object> searchParams, int pageNumber, int pageSize,
                                       String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Order> spec = buildSpecification(searchParams);

        return orderDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Order> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("purchaseDate", new SearchFilter("created", Operator.GTE, new Date(DateUtil.startOfToday())));
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        return spec;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "id");
        }

        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }


    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(User user) {
        return ((user.getId() != null) && (user.getId() == 1L));
    }


    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.loginName;
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Autowired
    public void setOrderDetailDao(OrderDetailDao orderDetailDao) {
        this.orderDetailDao = orderDetailDao;
    }

    @Autowired
    public void setExpressDao(ExpressDao expressDao) {
        this.expressDao = expressDao;
    }

    @Autowired
    public void setOrganizationDao(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }
}
