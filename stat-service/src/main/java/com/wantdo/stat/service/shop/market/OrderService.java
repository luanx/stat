package com.wantdo.stat.service.shop.market;

import com.google.common.collect.Lists;
import com.wantdo.stat.constant.DateFormatConstant;
import com.wantdo.stat.constant.PathConstant;
import com.wantdo.stat.dao.shop.OrderDao;
import com.wantdo.stat.dao.shop.OrderDetailDao;
import com.wantdo.stat.dao.shop.ProductDetailDao;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.front.response.ResponseVoResultCode;
import com.wantdo.stat.entity.front.vo.OrderVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.shop.Order;
import com.wantdo.stat.entity.shop.OrderDetail;
import com.wantdo.stat.entity.shop.OrderStatus;
import com.wantdo.stat.entity.shop.ProductDetail;
import com.wantdo.stat.excel.helper.ExcelReadHelper;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.Hibernates;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.persistence.SearchFilter.Operator;
import com.wantdo.stat.service.account.ServiceException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.utils.Clock;
import com.wantdo.stat.utils.DateUtil;
import com.wantdo.stat.utils.JodaTime;
import com.wantdo.stat.utils.Path;
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
 * @ Date : 2015-9-17
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class OrderService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(OrderService.class);

    private OrderDao orderDao;

    private OrderDetailDao orderDetailDao;

    private ProductDetailDao productDetailDao;

    private Clock clock = Clock.DEFAULT;


    /**
     * 按Id获得订单
     */
    public Order getOrder(Long id) {
        return orderDao.findOne(id);
    }

    public OrderDetail getOrderDetail(Long id) {
        return orderDetailDao.findOne(id);
    }

    public void saveOrder(Order order) {
        order.setCreated(clock.getCurrentDate());
        orderDao.save(order);
    }

    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetail.setCreated(clock.getCurrentDate());
        orderDetailDao.save(orderDetail);
    }

    public void updateOrder(Order order) {
        order.setModified(clock.getCurrentDate());
        orderDao.save(order);
    }

    public void updateOrderDetail(OrderDetail orderDetail) {
        orderDetail.setModified(clock.getCurrentDate());
        orderDetailDao.save(orderDetail);
    }


    public ResponseVo upload(MultipartFile uploadExcel, Long organizationId) {
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

            String relativeFold = "market" + File.separator + "normal" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
            String fullRelativeFold = PathConstant.FILE_STORE_PATH + relativeFold;
            String relativePath = relativeFold + System.currentTimeMillis() + "." + suffix;
            String fullPath = PathConstant.FILE_STORE_PATH + relativePath;

            File fullFoldFile = new File(fullRelativeFold);
            if (!fullFoldFile.exists()) {
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
            orderExcelRead(fullPath, organizationId);

        } catch (Exception e) {
            logger.error(e.getMessage());
            responseVo.setResult(ResponseVoResultCode.CODE_SERVICE_ERROR);
            responseVo.setMessage(e.getMessage());
            return responseVo;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        responseVo.setMessage("上传成功");
        return responseVo;

    }

    private void orderExcelRead(String file, Long organizationId) throws Exception{

        ArrayList<ArrayList<Object>> rowList = null;
            try {
                 rowList = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
            } catch (Exception e){
                logger.error(e.getMessage());
                throw new ServiceException("读取Excel错误,请检查后重试或联系管理员");
            }
            for (ArrayList<Object> row : rowList) {

                String row0 = ExcelReadHelper.getCell(row.get(0));
                String orderId = "";
                if (row0 != null){
                    orderId = row0;
                }

                String row1 = ExcelReadHelper.getCell(row.get(1));
                String orderItemId = "";
                if (row1 != null){
                    orderItemId = row1;
                }

                String row2 = ExcelReadHelper.getCell(row.get(2));
                Long purchaseDate = 0L;
                if (row2 != null) {
                    purchaseDate = JodaTime.getTimeMillis(JodaTime.stringToDateTime(row2));
                }

                String row3 = ExcelReadHelper.getCell(row.get(3));
                Long paymentsDate = 0L;
                if (row3 != null) {
                    paymentsDate = JodaTime.getTimeMillis(JodaTime.stringToDateTime(row3));
                }

                String row4 = ExcelReadHelper.getCell(row.get(4));
                String buyerEmail = "";
                if (row4 != null) {
                    buyerEmail = row4;
                }

                String row5 = ExcelReadHelper.getCell(row.get(5));
                String buyerName = "";
                if (row5 != null) {
                    buyerName = row5;
                }

                String row6 = ExcelReadHelper.getCell(row.get(6));
                String buyerPhoneNumber = "";
                if (row6 != null) {
                    buyerPhoneNumber = row6;
                }

                String row7 = ExcelReadHelper.getCell(row.get(7));
                String sku = "";
                if (row7 != null) {
                    sku = row7;
                } else {
                    continue;
                }
                ProductDetail productDetail = productDetailDao.findBySku(sku);

                String row8 = ExcelReadHelper.getCell(row.get(8));
                String productName = "";
                if (row8 != null) {
                    productName = row8;
                }

                String row9 = ExcelReadHelper.getCell(row.get(9));
                Long quantityPurchased = 0L;
                if (row9 != null) {
                    quantityPurchased = Long.valueOf(row9).longValue();
                }

                String row10 = ExcelReadHelper.getCell(row.get(10));
                String currency = "";
                if (row10 != null) {
                    currency = row10;
                }

                String row11 = ExcelReadHelper.getCell(row.get(11));
                Double itemPrice = 0.00;
                if (row11 != null) {
                    itemPrice = Double.valueOf(row11);
                }

                String row12 = ExcelReadHelper.getCell(row.get(12));
                Double itemTax = 0.00;
                if (row12 != null) {
                    itemTax = Double.valueOf(row12);
                }

                String row13 = ExcelReadHelper.getCell(row.get(13));
                Double shippingPrice = 0.00;
                if (row13 != null) {
                    shippingPrice = Double.valueOf(row13);
                }

                String row14 = ExcelReadHelper.getCell(row.get(14));
                Double shippingTax = 0.00;
                if (row14 != null) {
                    shippingTax = Double.valueOf(row14);
                }

                String row15 = ExcelReadHelper.getCell(row.get(15));
                String shipServiceLevel = "";
                if (row15 != null) {
                    shipServiceLevel = row15;
                }

                String row16 = ExcelReadHelper.getCell(row.get(16));
                String recipientName = "";
                if (row16 != null) {
                    recipientName = row16;
                }

                String row17 = ExcelReadHelper.getCell(row.get(17));
                String shipAddress1 = "";
                if (row17 != null) {
                    shipAddress1 = row17;
                }

                String row18 = ExcelReadHelper.getCell(row.get(18));
                String shipAddress2 = "";
                if (row18 != null) {
                    shipAddress2 = row18;
                }

                String row19 = ExcelReadHelper.getCell(row.get(19));
                String shipAddress3 = "";
                if (row19 != null) {
                    shipAddress3 = row19;
                }

                String row20 = ExcelReadHelper.getCell(row.get(20));
                String shipCity = "";
                if (row20 != null) {
                    shipCity = row20;
                }

                String row21 = ExcelReadHelper.getCell(row.get(21));
                String shipState = "";
                if (row21 != null) {
                    shipState = row21;
                }


                String row22 = ExcelReadHelper.getCell(row.get(22));
                String shipPostalCode = "";
                if (row22 != null) {
                    shipPostalCode = row22;
                }

                String row23 = ExcelReadHelper.getCell(row.get(23));
                String shipCountry = "";
                if (row23 != null) {
                    shipCountry = row23;
                }

                String row24 = ExcelReadHelper.getCell(row.get(24));
                String shipPhoneNumber = "";
                if (row24 != null) {
                    shipPhoneNumber = row24;
                }


                String row25 = ExcelReadHelper.getCell(row.get(25));
                Double itemPromotionDiscount = 0.00;
                if (row25 != null) {
                    itemPromotionDiscount = Double.valueOf(row25);
                }

                String row26 = ExcelReadHelper.getCell(row.get(26));
                String itemPromotionId = "";
                if (row26 != null) {
                    itemPromotionId = row26;
                }

                String row27 = ExcelReadHelper.getCell(row.get(27));
                Double shipPromotionDiscount = 0.00;
                if (row27 != null) {
                    shipPromotionDiscount = Double.valueOf(row27);
                }


                String row28 = ExcelReadHelper.getCell(row.get(28));
                String shipPromotionId = "";
                if (row28 != null) {
                    shipPromotionId = row28;
                }

                String row29 = ExcelReadHelper.getCell(row.get(29));
                Long deliveryStartDate = 0L;
                if (row29 != null) {
                    deliveryStartDate = JodaTime.getTimeMillis(JodaTime.stringToDateTime(row29));
                }

                String row30 = ExcelReadHelper.getCell(row.get(30));
                Long deliveryEndDate = 0L;
                if (row30 != null) {
                    deliveryEndDate = JodaTime.getTimeMillis(JodaTime.stringToDateTime(row30));
                }

                String row31 = ExcelReadHelper.getCell(row.get(31));
                String deliveryTimeZone = "";
                if (row31 != null) {
                    deliveryTimeZone = row31;
                }

                String row32 = ExcelReadHelper.getCell(row.get(32));
                String deliveryInstructions = "";
                if (row32 != null) {
                    deliveryInstructions = row32;
                }

                String row33 = ExcelReadHelper.getCell(row.get(33));
                String salesChannel = "";
                if (row33 != null) {
                    salesChannel = row33;
                }


                Order orderExist = orderDao.findByOrderId(orderId);
                if (orderExist == null){
                    Order order = new Order();
                    OrderDetail orderDetail = new OrderDetail();

                    order.setOrderId(orderId);
                    order.setPurchaseDate(purchaseDate);
                    order.setPaymentsDate(paymentsDate);
                    order.setBuyerEmail(buyerEmail);
                    order.setBuyerName(buyerName);
                    order.setBuyerPhoneNumber(buyerPhoneNumber);
                    order.setOrganization(new Organization(organizationId));
                    order.setShipServiceLevel(shipServiceLevel);
                    order.setRecipientName(recipientName);
                    order.setShipAddress1(shipAddress1);
                    order.setShipAddress2(shipAddress2);
                    order.setShipAddress3(shipAddress3);
                    order.setShipCity(shipCity);
                    order.setShipState(shipState);
                    order.setShipPostalCode(shipPostalCode);
                    order.setShipCountry(shipCountry);
                    order.setShipPhoneNumber(shipPhoneNumber);
                    order.setDeliveryStartDate(deliveryStartDate);
                    order.setDeliveryEndDate(deliveryEndDate);
                    order.setDeliveryTimeZone(deliveryTimeZone);
                    order.setDeliveryInstructions(deliveryInstructions);
                    order.setSalesChannel(salesChannel);
                    if (productDetail != null){
                        order.setOrderStatus(new OrderStatus(2L));
                    } else {
                        order.setOrderStatus(new OrderStatus(1L));
                        order.setRemark("未找到该sku");
                    }
                    orderDao.save(order);

                    orderDetail.setOrderId(orderId);
                    orderDetail.setOrderItemId(orderItemId);
                    orderDetail.setPurchaseDate(purchaseDate);
                    orderDetail.setPaymentsDate(paymentsDate);
                    orderDetail.setBuyerEmail(buyerEmail);
                    orderDetail.setBuyerName(buyerName);
                    orderDetail.setBuyerPhoneNumber(buyerPhoneNumber);
                    orderDetail.setSku(sku);
                    if (productDetail != null){
                        orderDetail.setProductDetail(productDetail);
                        orderDetail.setOrderStatus(new OrderStatus(2L));
                    } else {
                        orderDetail.setOrderStatus(new OrderStatus(1L));
                        orderDetail.setRemark("未找到该sku");
                    }
                    orderDetail.setProductName(productName);
                    orderDetail.setQuantityPurchased(quantityPurchased);
                    orderDetail.setCurrency(currency);
                    orderDetail.setItemPrice(itemPrice);
                    orderDetail.setItemTax(itemTax);
                    orderDetail.setShippingPrice(shippingPrice);
                    orderDetail.setShippingTax(shippingTax);
                    orderDetail.setOrganization(new Organization(organizationId));
                    orderDetail.setShipServiceLevel(shipServiceLevel);
                    orderDetail.setRecipientName(recipientName);
                    orderDetail.setShipAddress1(shipAddress1);
                    orderDetail.setShipAddress2(shipAddress2);
                    orderDetail.setShipAddress3(shipAddress3);
                    orderDetail.setShipCity(shipCity);
                    orderDetail.setShipState(shipState);
                    orderDetail.setShipPostalCode(shipPostalCode);
                    orderDetail.setShipCountry(shipCountry);
                    orderDetail.setShipPhoneNumber(shipPhoneNumber);
                    orderDetail.setItemPromotionDiscount(itemPromotionDiscount);
                    orderDetail.setItemPromotionId(itemPromotionId);
                    orderDetail.setShipPromotionDiscount(shipPromotionDiscount);
                    orderDetail.setShipPromotionId(shipPromotionId);
                    orderDetail.setItemPromotionDiscount(itemPromotionDiscount);
                    orderDetail.setDeliveryStartDate(deliveryStartDate);
                    orderDetail.setDeliveryEndDate(deliveryEndDate);
                    orderDetail.setDeliveryTimeZone(deliveryTimeZone);
                    orderDetail.setDeliveryInstructions(deliveryInstructions);
                    orderDetail.setSalesChannel(salesChannel);
                    orderDetail.setOrder(order);
                    orderDetailDao.save(orderDetail);

                } else {

                    orderExist.setOrderId(orderId);
                    orderExist.setPurchaseDate(purchaseDate);
                    orderExist.setPaymentsDate(paymentsDate);
                    orderExist.setBuyerEmail(buyerEmail);
                    orderExist.setBuyerName(buyerName);
                    orderExist.setBuyerPhoneNumber(buyerPhoneNumber);
                    orderExist.setOrganization(new Organization(organizationId));
                    orderExist.setShipServiceLevel(shipServiceLevel);
                    orderExist.setRecipientName(recipientName);
                    orderExist.setShipAddress1(shipAddress1);
                    orderExist.setShipAddress2(shipAddress2);
                    orderExist.setShipAddress3(shipAddress3);
                    orderExist.setShipCity(shipCity);
                    orderExist.setShipState(shipState);
                    orderExist.setShipPostalCode(shipPostalCode);
                    orderExist.setShipCountry(shipCountry);
                    orderExist.setShipPhoneNumber(shipPhoneNumber);
                    orderExist.setDeliveryStartDate(deliveryStartDate);
                    orderExist.setDeliveryEndDate(deliveryEndDate);
                    orderExist.setDeliveryTimeZone(deliveryTimeZone);
                    orderExist.setDeliveryInstructions(deliveryInstructions);
                    orderExist.setSalesChannel(salesChannel);
                    orderExist.setModified(clock.getCurrentDate());
                    orderExist.setOrderStatus(new OrderStatus(2L));
                    orderDao.save(orderExist);

                    OrderDetail orderDetailExist = orderDetailDao.findByOrderIdAndOrderItemId(orderId, orderItemId);
                    if (orderDetailExist == null){
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setOrderId(orderId);
                        orderDetail.setOrderItemId(orderItemId);
                        orderDetail.setPurchaseDate(purchaseDate);
                        orderDetail.setPaymentsDate(paymentsDate);
                        orderDetail.setBuyerEmail(buyerEmail);
                        orderDetail.setBuyerName(buyerName);
                        orderDetail.setBuyerPhoneNumber(buyerPhoneNumber);
                        orderDetail.setSku(sku);
                        if (productDetail != null) {
                            orderDetail.setProductDetail(productDetail);
                            orderExist.setOrderStatus(new OrderStatus(2L));
                            orderDetail.setOrderStatus(new OrderStatus(2L));
                        } else {
                            orderExist.setOrderStatus(new OrderStatus(1L));
                            orderExist.setRemark("未找到该sku");
                            orderDetail.setOrderStatus(new OrderStatus(1L));
                            orderDetail.setRemark("未找到该sku");
                        }
                        orderDetail.setProductName(productName);
                        orderDetail.setQuantityPurchased(quantityPurchased);
                        orderDetail.setCurrency(currency);
                        orderDetail.setItemPrice(itemPrice);
                        orderDetail.setItemTax(itemTax);
                        orderDetail.setShippingPrice(shippingPrice);
                        orderDetail.setShippingTax(shippingTax);
                        orderDetail.setOrganization(new Organization(organizationId));
                        orderDetail.setShipServiceLevel(shipServiceLevel);
                        orderDetail.setRecipientName(recipientName);
                        orderDetail.setShipAddress1(shipAddress1);
                        orderDetail.setShipAddress2(shipAddress2);
                        orderDetail.setShipAddress3(shipAddress3);
                        orderDetail.setShipCity(shipCity);
                        orderDetail.setShipState(shipState);
                        orderDetail.setShipPostalCode(shipPostalCode);
                        orderDetail.setShipCountry(shipCountry);
                        orderDetail.setShipPhoneNumber(shipPhoneNumber);
                        orderDetail.setItemPromotionDiscount(itemPromotionDiscount);
                        orderDetail.setItemPromotionId(itemPromotionId);
                        orderDetail.setShipPromotionDiscount(shipPromotionDiscount);
                        orderDetail.setShipPromotionId(shipPromotionId);
                        orderDetail.setItemPromotionDiscount(itemPromotionDiscount);
                        orderDetail.setDeliveryStartDate(deliveryStartDate);
                        orderDetail.setDeliveryEndDate(deliveryEndDate);
                        orderDetail.setDeliveryTimeZone(deliveryTimeZone);
                        orderDetail.setDeliveryInstructions(deliveryInstructions);
                        orderDetail.setSalesChannel(salesChannel);
                        orderDetail.setOrder(orderExist);
                        orderDetailDao.save(orderDetail);
                    } else {
                        orderDetailExist.setOrderId(orderId);
                        orderDetailExist.setOrderItemId(orderItemId);
                        orderDetailExist.setPurchaseDate(purchaseDate);
                        orderDetailExist.setPaymentsDate(paymentsDate);
                        orderDetailExist.setBuyerEmail(buyerEmail);
                        orderDetailExist.setBuyerName(buyerName);
                        orderDetailExist.setBuyerPhoneNumber(buyerPhoneNumber);
                        orderDetailExist.setSku(sku);
                        orderDetailExist.setProductName(productName);
                        orderDetailExist.setQuantityPurchased(quantityPurchased);
                        orderDetailExist.setCurrency(currency);
                        orderDetailExist.setItemPrice(itemPrice);
                        orderDetailExist.setItemTax(itemTax);
                        orderDetailExist.setShippingPrice(shippingPrice);
                        orderDetailExist.setShippingTax(shippingTax);
                        orderDetailExist.setOrganization(new Organization(organizationId));
                        orderDetailExist.setShipServiceLevel(shipServiceLevel);
                        orderDetailExist.setRecipientName(recipientName);
                        orderDetailExist.setShipAddress1(shipAddress1);
                        orderDetailExist.setShipAddress2(shipAddress2);
                        orderDetailExist.setShipAddress3(shipAddress3);
                        orderDetailExist.setShipCity(shipCity);
                        orderDetailExist.setShipState(shipState);
                        orderDetailExist.setShipPostalCode(shipPostalCode);
                        orderDetailExist.setShipCountry(shipCountry);
                        orderDetailExist.setShipPhoneNumber(shipPhoneNumber);
                        orderDetailExist.setItemPromotionDiscount(itemPromotionDiscount);
                        orderDetailExist.setItemPromotionId(itemPromotionId);
                        orderDetailExist.setShipPromotionDiscount(shipPromotionDiscount);
                        orderDetailExist.setShipPromotionId(shipPromotionId);
                        orderDetailExist.setItemPromotionDiscount(itemPromotionDiscount);
                        orderDetailExist.setDeliveryStartDate(deliveryStartDate);
                        orderDetailExist.setDeliveryEndDate(deliveryEndDate);
                        orderDetailExist.setDeliveryTimeZone(deliveryTimeZone);
                        orderDetailExist.setDeliveryInstructions(deliveryInstructions);
                        orderDetailExist.setSalesChannel(salesChannel);
                        orderDetailExist.setOrder(orderExist);
                        orderDetailExist.setModified(clock.getCurrentDate());
                        if (productDetail != null) {
                            orderDetailExist.setProductDetail(productDetail);
                            orderExist.setOrderStatus(new OrderStatus(2L));
                            orderDetailExist.setOrderStatus(new OrderStatus(2L));
                        } else {
                            orderExist.setOrderStatus(new OrderStatus(1L));
                            orderExist.setRemark("未找到该sku");
                            orderDetailExist.setOrderStatus(new OrderStatus(1L));
                            orderDetailExist.setRemark("未找到该sku");
                        }
                        orderDetailDao.save(orderDetailExist);
                    }

                }

            }
    }

    public void saveOrderList(List<Order> orderList) {
        for (Order order : orderList) {
            orderDao.save(order);
        }
    }

    public void saveOrderDetailList(List<OrderDetail> orderDetailList) {
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetailDao.save(orderDetail);
        }
    }


    public void deleteOrder(Long id) {
        orderDao.delete(id);
        orderDetailDao.deleteByOrder(id);
    }

    public void endOrder(Long id) {
        Order order = orderDao.findOne(id);
        order.setOrderStatus(new OrderStatus(4L));
        for (OrderDetail orderDetail : order.getOrderDetailList()) {
            orderDetail.setOrderStatus(new OrderStatus(4L));
        }
        orderDao.save(order);
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

    public TableDTO<OrderVo> getOrderVo(Long organizationId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                                              String sortType, String orderType) {
        Page<OrderDetail> orderDetails = SearchOrderDetail(organizationId, searchParams, pageNumber, pageSize, sortType, orderType);
        TableDTO<OrderVo> tableDTO = new TableDTO<>();
        List<OrderVo> orderVoList = Lists.newArrayList();
        tableDTO.setTotal(orderDetails.getTotalElements());
        for(OrderDetail orderDetail: orderDetails.getContent()){
            OrderVo orderVo = new OrderVo();
            orderVo.setId(orderDetail.getId());
            orderVo.setOrderId(orderDetail.getOrderId());
            orderVo.setSku(orderDetail.getSku());
            orderVo.setCreated(orderDetail.getCreated());
            ProductDetail productDetail = orderDetail.getProductDetail();
            if (productDetail != null){
                orderVo.setProductLink(productDetail.getLink());
            } else {
                orderVo.setProductLink("");
            }
            orderVo.setProductName(orderDetail.getProductName());
            orderVo.setOrderItemId(orderDetail.getOrderItemId());
            orderVo.setPurDate(new Date(orderDetail.getPurchaseDate()));
            orderVo.setPayDate(new Date(orderDetail.getPaymentsDate()));
            orderVo.setBuyerName(orderDetail.getBuyerName());
            orderVo.setBuyerEmail(orderDetail.getBuyerEmail());
            orderVo.setBuyerPhoneNumber(orderDetail.getBuyerPhoneNumber());
            orderVo.setQuantityPurchased(orderDetail.getQuantityPurchased());
            orderVo.setCurrency(orderDetail.getCurrency());
            orderVo.setItemPrice(orderDetail.getItemPrice());
            orderVo.setShipServiceLevel(orderDetail.getShipServiceLevel());
            orderVo.setRecipientName(orderDetail.getRecipientName());
            orderVo.setShipPhoneNumber(orderDetail.getShipPhoneNumber());
            orderVo.setShipAddress(orderDetail.getShipAddress1());
            orderVo.setShipCity(orderDetail.getShipCity());
            orderVo.setShipState(orderDetail.getShipState());
            orderVo.setShipPostalCode(orderDetail.getShipPostalCode());
            orderVo.setShipCountry(orderDetail.getShipCountry());
            orderVo.setOrderStatus(orderDetail.getOrderStatus().getDescription());
            orderVo.setRemark(orderDetail.getRemark());

            orderVoList.add(orderVo);
        }
        tableDTO.setRows(orderVoList);
        return tableDTO;
    }

    public OrderVo getOrderVoById(Long id) {
        OrderDetail orderDetail = orderDetailDao.findOne(id);

        OrderVo orderVo = new OrderVo();
        orderVo.setId(orderDetail.getId());
        orderVo.setOrderId(orderDetail.getOrderId());
        orderVo.setSku(orderDetail.getSku());
        orderVo.setCreated(orderDetail.getCreated());
        ProductDetail productDetail = orderDetail.getProductDetail();
        if (productDetail != null) {
            orderVo.setProductLink(productDetail.getLink());
        } else {
            orderVo.setProductLink("");
        }
        orderVo.setProductName(orderDetail.getProductName());
        orderVo.setOrderItemId(orderDetail.getOrderItemId());
        orderVo.setPurDate(new Date(orderDetail.getPurchaseDate()));
        orderVo.setPayDate(new Date(orderDetail.getPaymentsDate()));
        orderVo.setBuyerName(orderDetail.getBuyerName());
        orderVo.setBuyerEmail(orderDetail.getBuyerEmail());
        orderVo.setBuyerPhoneNumber(orderDetail.getBuyerPhoneNumber());
        orderVo.setQuantityPurchased(orderDetail.getQuantityPurchased());
        orderVo.setCurrency(orderDetail.getCurrency());
        orderVo.setItemPrice(orderDetail.getItemPrice());
        orderVo.setShipServiceLevel(orderDetail.getShipServiceLevel());
        orderVo.setRecipientName(orderDetail.getRecipientName());
        orderVo.setShipPhoneNumber(orderDetail.getShipPhoneNumber());
        orderVo.setShipAddress(orderDetail.getShipAddress1());
        orderVo.setShipCity(orderDetail.getShipCity());
        orderVo.setShipState(orderDetail.getShipState());
        orderVo.setShipPostalCode(orderDetail.getShipPostalCode());
        orderVo.setShipCountry(orderDetail.getShipCountry());
        orderVo.setOrderStatus(orderDetail.getOrderStatus().getDescription());
        orderVo.setRemark(orderDetail.getRemark());

        return orderVo;

    }

    public TableDTO<OrderVo> getAllOrderVo(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                        String sortType, String orderType) {
        Page<OrderDetail> orderDetails = SearchAllOrderDetail(searchParams, pageNumber, pageSize, sortType, orderType);
        TableDTO<OrderVo> tableDTO = new TableDTO<>();
        List<OrderVo> orderVoList = Lists.newArrayList();
        tableDTO.setTotal(orderDetails.getTotalElements());
        for (OrderDetail orderDetail : orderDetails.getContent()) {
            OrderVo orderVo = new OrderVo();
            orderVo.setId(orderDetail.getId());
            orderVo.setOrderId(orderDetail.getOrderId());
            orderVo.setSku(orderDetail.getSku());
            orderVo.setCreated(orderDetail.getCreated());
            ProductDetail productDetail = orderDetail.getProductDetail();
            if (productDetail != null) {
                orderVo.setProductLink(productDetail.getLink());
            } else {
                orderVo.setProductLink("");
            }
            orderVo.setOrganizationName(orderDetail.getOrganization().getName());
            orderVo.setProductName(orderDetail.getProductName());
            orderVo.setOrderItemId(orderDetail.getOrderItemId());
            orderVo.setPurDate(new Date(orderDetail.getPurchaseDate()));
            orderVo.setPayDate(new Date(orderDetail.getPaymentsDate()));
            orderVo.setBuyerName(orderDetail.getBuyerName());
            orderVo.setBuyerEmail(orderDetail.getBuyerEmail());
            orderVo.setBuyerPhoneNumber(orderDetail.getBuyerPhoneNumber());
            orderVo.setQuantityPurchased(orderDetail.getQuantityPurchased());
            orderVo.setCurrency(orderDetail.getCurrency());
            orderVo.setItemPrice(orderDetail.getItemPrice());
            orderVo.setShipServiceLevel(orderDetail.getShipServiceLevel());
            orderVo.setRecipientName(orderDetail.getRecipientName());
            orderVo.setShipPhoneNumber(orderDetail.getShipPhoneNumber());
            orderVo.setShipAddress(orderDetail.getShipAddress1());
            orderVo.setShipCity(orderDetail.getShipCity());
            orderVo.setShipState(orderDetail.getShipState());
            orderVo.setShipPostalCode(orderDetail.getShipPostalCode());
            orderVo.setShipCountry(orderDetail.getShipCountry());
            orderVo.setOrderStatus(orderDetail.getOrderStatus().getDescription());
            orderVo.setRemark(orderDetail.getRemark());

            orderVoList.add(orderVo);
        }
        tableDTO.setRows(orderVoList);
        return tableDTO;
    }

    /**
     * 按页面传来的查询条件查询OrderDetail
     */
    public Page<OrderDetail> SearchOrderDetail(Long organizationId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                   String sortType, String orderType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<OrderDetail> spec = buildSpecification(organizationId, searchParams, orderType);

        return orderDetailDao.findAll(spec, pageRequest);
    }

    public Page<OrderDetail> SearchAllOrderDetail(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                               String sortType, String orderType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<OrderDetail> spec = buildAllSpecification(searchParams, orderType);

        return orderDetailDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<OrderDetail> buildSpecification(Long organizationId, Map<String, Object> searchParams, String orderType) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("organization.id", new SearchFilter("organization.id", Operator.EQ, organizationId));
        if (orderType.equals("today")){
            filters.put("created", new SearchFilter("created", Operator.GTE, new Date(DateUtil.startOfToday())));
        } else if (orderType.equals("exception")){
            filters.put("orderStatus.id", new SearchFilter("orderStatus.id", Operator.EQ, 1L));
        }
        Specification<OrderDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), OrderDetail.class);
        return spec;
    }

    private Specification<OrderDetail> buildAllSpecification(Map<String, Object> searchParams, String orderType) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        if (orderType.equals("today")) {
            filters.put("created", new SearchFilter("created", Operator.GTE, new Date(DateUtil.startOfToday())));
        } else if (orderType.equals("exception")) {
            filters.put("orderStatus.id", new SearchFilter("orderStatus.id", Operator.EQ, 1L));
        }
        Specification<OrderDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), OrderDetail.class);
        return spec;
    }


    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType) || "desc".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "id");
        } else if ("asc".equals(sortType)){
            sort = new Sort(Sort.Direction.ASC, "id");
        }

        return new PageRequest(pageNumber - 1, pageSize, sort);
    }

    public void deleteOrderList(List<OrderVo> orderVoList) {
        for(OrderVo orderVo: orderVoList){
            OrderDetail orderDetail = orderDetailDao.findOne(orderVo.getId());
            Order order = orderDetail.getOrder();
            List<OrderDetail> orderDetailList = order.getOrderDetailList();
            orderDetailDao.delete(orderDetail.getId());
            if (orderDetailList != null && orderDetailList.size() == 1){
                orderDao.delete(order.getId());
            }
        }
    }

    public void deleteOrderDetailChecked(List<Long> orderDetailChecked) {
        OrderDetail orderDetail0 = orderDetailDao.findOne(orderDetailChecked.get(0));
        Long orderId = orderDetail0.getOrder().getId();
        for (Long orderDetailId : orderDetailChecked) {
            orderDetailDao.delete(orderDetailId);
        }
        orderDao.delete(orderId);
    }

    public void endOrderDetailChecked(List<Long> orderDetailChecked) {
        for (Long orderDetailId : orderDetailChecked) {
            OrderDetail orderDetail = orderDetailDao.findOne(orderDetailId);
            orderDetail.setOrderStatus(new OrderStatus(4L));
            Order order = orderDetail.getOrder();
            order.setOrderStatus(new OrderStatus(4L));
            orderDetailDao.save(orderDetail);
            orderDao.save(order);
        }
    }

    public List<OrderDetail> findAllChanged() {
        return orderDetailDao.findAllChanged();
    }

    public boolean orderChanged(){
        return orderDetailDao.findAllChanged().size() > 0;
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
    public void setProductDetailDao(ProductDetailDao productDetailDao) {
        this.productDetailDao = productDetailDao;
    }
}
