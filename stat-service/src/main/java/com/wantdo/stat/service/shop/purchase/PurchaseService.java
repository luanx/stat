package com.wantdo.stat.service.shop.purchase;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wantdo.stat.constant.DateFormatConstant;
import com.wantdo.stat.constant.PathConstant;
import com.wantdo.stat.dao.account.OrganizationDao;
import com.wantdo.stat.dao.shop.*;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.front.response.ResponseVoResultCode;
import com.wantdo.stat.entity.front.vo.ExcelVo;
import com.wantdo.stat.entity.front.vo.PurchaseVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.shop.*;
import com.wantdo.stat.excel.helper.ExcelExportHelper;
import com.wantdo.stat.excel.helper.ExcelReadHelper;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.security.utils.Digests;
import com.wantdo.stat.service.account.ServiceException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.utils.Clock;
import com.wantdo.stat.utils.DateUtil;
import com.wantdo.stat.utils.Encodes;
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
 * @ Date : 2015-9-17
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class PurchaseService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(PurchaseService.class);

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_ITERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    private PurchaseDao purchaseDao;
    private PurchaseDetailDao purchaseDetailDao;

    private OrderDao orderDao;
    private OrderDetailDao orderDetailDao;

    private AlibabaOrderDao alibabaOrderDao;
    private AlibabaOrderDetailDao alibabaOrderDetailDao;


    private OrganizationDao organizationDao;

    private Clock clock = Clock.DEFAULT;

    /**
     * 按Id获得Purchase对象
     */
    public Purchase getPurchase(Long id) {
        return purchaseDao.findOne(id);
    }

    public PurchaseDetail getPurchaseDetail(Long id) {
        return purchaseDetailDao.findOne(id);
    }

    public void savePurchase(Purchase purchase) {
        purchase.setCreated(clock.getCurrentDate());
        purchaseDao.save(purchase);
    }

    public void savePurchaseDetail(PurchaseDetail purchaseDetail) {
        purchaseDetail.setCreated(clock.getCurrentDate());
        purchaseDetailDao.save(purchaseDetail);
    }

    public void updatePurchase(Purchase purchase) {
        purchase.setModified(clock.getCurrentDate());
        purchaseDao.save(purchase);
    }

    public void updatePurchaseDetail(PurchaseDetail purchaseDetail) {
        purchaseDetail.setModified(clock.getCurrentDate());
        purchaseDetailDao.save(purchaseDetail);
    }

    public ResponseVo upload(MultipartFile uploadExcel) {
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

            String relativeFold = "purchase" + File.separator + now + File.separator + getCurrentUserId() + File
                    .separator;
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
            int len ;
            while ((len = bis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            bos.flush();
            bos.close();

            //解析Excel并写入数据库
            purchaseExcelRead(fullPath);

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

    private void purchaseExcelRead(String file) throws Exception{

        ArrayList<ArrayList<Object>> rowList ;
            try {
                rowList  = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
            }catch (Exception e){
                throw new ServiceException("读取Excel错误,请检查后重试或联系管理员");
            }
            for (ArrayList<Object> row : rowList) {

                String row0 =ExcelReadHelper.getCell(row.get(0));
                Date purchaseDate = new Date();
                if (row0 != null){
                    purchaseDate = DateUtil.stringToDate(row0);
                }


                String row1 = ExcelReadHelper.getCell(row.get(1));
                String purchaseOrderId = "";
                if (row1 != null) {
                    purchaseOrderId = row1;
                } else {
                    throw new ServiceException("采购订单号不能为空");
                }

                String row7 = ExcelReadHelper.getCell(row.get(7));
                String sku = "";
                if (row7 != null) {
                    sku = row7;
                } else {
                    throw new ServiceException("SKU不能为空");
                }

                String row20 = ExcelReadHelper.getCell(row.get(20));
                String orderItemId = "";
                if (row20 != null) {
                    orderItemId = row20;
                } else {
                    throw new ServiceException("订单号:" + purchaseOrderId + ", SKU:" + sku + "对应的orderItemId为空");
                }

                String row2 = ExcelReadHelper.getCell(row.get(2));
                String platformOrderId = "";
                if (row2 != null) {
                    platformOrderId = row2;
                } else {
                    platformOrderId = encryptPlatformOrderId(purchaseOrderId, orderItemId);
                }

                String row3 = ExcelReadHelper.getCell(row.get(3));
                String organizationName = "";
                if (row3 != null) {
                    organizationName = row3;
                } else {
                    throw new ServiceException("店铺名不能为空");
                }
                Organization organization = organizationDao.findByName(organizationName);
                if(organization == null){
                    throw new ServiceException("采购订单号:" + purchaseOrderId + ",平台订单号:"+platformOrderId +" 未找到店铺名:" + organizationName );
                }

                String row4 = ExcelReadHelper.getCell(row.get(4));
                String supplierName = "";
                if (row4 != null) {
                    supplierName = row4;
                }

                String row5 = ExcelReadHelper.getCell(row.get(5));
                String productName = "";
                if (row5 != null) {
                    productName = row5;
                }

                String row6 = ExcelReadHelper.getCell(row.get(6));
                String wwName = "";
                if (row6 != null) {
                    wwName = row6;
                }



                String row8 = ExcelReadHelper.getCell(row.get(8));
                String productNo = "";
                if (row8 != null) {
                    productNo = row8;
                }


                String row9 = ExcelReadHelper.getCell(row.get(9));
                String productFeature = "";
                if (row9 != null) {
                    productFeature = row9;
                }

                String row10 = ExcelReadHelper.getCell(row.get(10));
                Long quantity = 0L;
                if (row10 != null) {
                    quantity = Double.valueOf(row10).longValue();
                }

                String row11 = ExcelReadHelper.getCell(row.get(11));
                String price = "";
                if (row11 != null) {
                    price = row11;
                }

                String row12 = ExcelReadHelper.getCell(row.get(12));
                String shipDiscount = "";
                if (row12 != null) {
                    shipDiscount = row12;
                }

                String row13 = ExcelReadHelper.getCell(row.get(13));
                String total = "";
                if (row13 != null) {
                    total = row13;
                }

                String row14 = ExcelReadHelper.getCell(row.get(14));
                String category = "";
                if (row14 != null) {
                    category = row14;
                }

                String row15 = ExcelReadHelper.getCell(row.get(15));
                String origin = "";
                if (row15 != null) {
                    origin = row15;
                }

                String row16 = ExcelReadHelper.getCell(row.get(16));
                String productLink = "";
                if (row16 != null) {
                    productLink = row16;
                }


                String row17 = ExcelReadHelper.getCell(row.get(17));
                String alternateLink = "";
                if (row17 != null) {
                    alternateLink = row17;
                }

                String row18 = ExcelReadHelper.getCell(row.get(18));
                String remark = "";
                if (row18 != null) {
                    remark = row18;
                }



                Purchase purchaseExist = purchaseDao.findByPlatformOrderId(platformOrderId);

                if (purchaseExist == null) {

                    Purchase purchase = new Purchase();
                    purchase.setPlatformOrderId(platformOrderId);
                    purchase.setPurchaseDate(purchaseDate);
                    purchase.setSupplierName(supplierName);
                    purchase.setWwName(wwName);
                    purchase.setTotal(total);
                    purchase.setRemark(remark);
                    purchaseDao.save(purchase);

                    PurchaseDetail purchaseDetail = new PurchaseDetail();
                    purchaseDetail.setPurchaseOrderId(purchaseOrderId);
                    purchaseDetail.setPlatformOrderId(platformOrderId);
                    purchaseDetail.setPurchaseDate(purchaseDate);
                    purchaseDetail.setSupplierName(supplierName);
                    purchaseDetail.setProductName(productName);
                    purchaseDetail.setWwName(wwName);
                    purchaseDetail.setSku(sku);
                    purchaseDetail.setOrderItemId(orderItemId);
                    purchaseDetail.setProductNo(productNo);
                    purchaseDetail.setProductFeature(productFeature);
                    purchaseDetail.setQuantity(quantity);
                    purchaseDetail.setPrice(price);
                    purchaseDetail.setShipDiscount(shipDiscount);
                    purchaseDetail.setTotal(total);
                    purchaseDetail.setCategory(category);
                    purchaseDetail.setOrigin(origin);
                    purchaseDetail.setTotal(total);
                    purchaseDetail.setProductLink(productLink);
                    purchaseDetail.setAlternateLink(alternateLink);
                    purchaseDetail.setRemark(remark);
                    purchaseDetail.setOrganization(organization);
                    purchaseDetail.setPurchase(purchase);
                    purchaseDetail.setReceiveStatus(new ReceiveStatus(1L));
                    purchaseDetailDao.save(purchaseDetail);
                } else {
                    purchaseExist.setPlatformOrderId(platformOrderId);
                    purchaseExist.setPurchaseDate(purchaseDate);
                    purchaseExist.setSupplierName(supplierName);
                    purchaseExist.setWwName(wwName);
                    purchaseExist.setTotal(total);
                    purchaseExist.setRemark(remark);

                    purchaseDao.save(purchaseExist);

                    PurchaseDetail purchaseDetailExist = purchaseDetailDao.findByPlatformOrderIdAndPurchaseOrderIdAndOrderItemId(platformOrderId, purchaseOrderId, orderItemId);

                    if (purchaseDetailExist == null) {
                        PurchaseDetail purchaseDetail = new PurchaseDetail();
                        purchaseDetail.setPurchaseOrderId(purchaseOrderId);
                        purchaseDetail.setPlatformOrderId(platformOrderId);
                        purchaseDetail.setPurchaseDate(purchaseDate);
                        purchaseDetail.setSupplierName(supplierName);
                        purchaseDetail.setProductName(productName);
                        purchaseDetail.setWwName(wwName);
                        purchaseDetail.setSku(sku);
                        purchaseDetail.setOrderItemId(orderItemId);
                        purchaseDetail.setProductNo(productNo);
                        purchaseDetail.setProductFeature(productFeature);
                        purchaseDetail.setQuantity(quantity);
                        purchaseDetail.setPrice(price);
                        purchaseDetail.setShipDiscount(shipDiscount);
                        purchaseDetail.setTotal(total);
                        purchaseDetail.setCategory(category);
                        purchaseDetail.setOrigin(origin);
                        purchaseDetail.setProductLink(productLink);
                        purchaseDetail.setAlternateLink(alternateLink);
                        purchaseDetail.setRemark(remark);
                        purchaseDetail.setOrganization(organization);
                        purchaseDetail.setPurchase(purchaseExist);
                        purchaseDetail.setReceiveStatus(new ReceiveStatus(1L));

                        purchaseDetailDao.save(purchaseDetail);
                    } else {
                        purchaseDetailExist.setPurchaseOrderId(purchaseOrderId);
                        purchaseDetailExist.setPlatformOrderId(platformOrderId);
                        purchaseDetailExist.setPurchaseDate(purchaseDate);
                        purchaseDetailExist.setSupplierName(supplierName);
                        purchaseDetailExist.setProductName(productName);
                        purchaseDetailExist.setWwName(wwName);
                        purchaseDetailExist.setSku(sku);
                        purchaseDetailExist.setOrderItemId(orderItemId);
                        purchaseDetailExist.setProductNo(productNo);
                        purchaseDetailExist.setProductFeature(productFeature);
                        purchaseDetailExist.setQuantity(quantity);
                        purchaseDetailExist.setPrice(price);
                        purchaseDetailExist.setShipDiscount(shipDiscount);
                        purchaseDetailExist.setTotal(total);
                        purchaseDetailExist.setCategory(category);
                        purchaseDetailExist.setOrigin(origin);
                        purchaseDetailExist.setProductLink(productLink);
                        purchaseDetailExist.setRemark(remark);
                        purchaseDetailExist.setOrganization(organization);
                        purchaseDetailExist.setPurchase(purchaseExist);

                        purchaseDetailDao.save(purchaseDetailExist);
                    }

                }

                OrderDetail orderDetail = orderDetailDao.findByOrderIdAndOrderItemId(purchaseOrderId, orderItemId);

                if (orderDetail != null) {
                    OrderStatus orderStatus = orderDetail.getOrderStatus();
                    if(orderStatus != null && orderStatus.getId() != null){
                       if (orderStatus.getId() == 2L){
                           orderDetail.setOrderStatus(new OrderStatus(3L));
                           orderDetailDao.save(orderDetail);
                       }
                    }else {
                        throw new ServiceException("订单出现异常错误,请联系管理员!");
                    }
                } else {
                    throw new ServiceException("未找到订单号:" + purchaseOrderId + ", SKU:" + sku + ",OrderItemId:" + orderItemId);
                }

            }
    }

    public ResponseVo uploadException(MultipartFile uploadExceptionExcel) {
        ResponseVo responseVo = ResponseVo.newInstance();
        String fileName = uploadExceptionExcel.getOriginalFilename();
        String suffix = Path.getSuffixFromPath(fileName);
        if (!suffix.equals("xls") && !suffix.equals("xlsx")) {
            responseVo.setResult(ResponseVoResultCode.CODE_PARAM_ERROR);
            responseVo.setMessage("文件格式错误，不是excel文件");
            return responseVo;
        }

        BufferedOutputStream bos = null;

        try {
            String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);

            String relativeFold = "purchase" + File.separator + now + File.separator + getCurrentUserId() + File
                    .separator;
            String fullRelativeFold = PathConstant.FILE_STORE_PATH + relativeFold;
            String relativePath = relativeFold + System.currentTimeMillis() + "." + suffix;
            String fullPath = PathConstant.FILE_STORE_PATH + relativePath;

            File fullFoldFile = new File(fullRelativeFold);
            if (!fullFoldFile.exists()) {
                fullFoldFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(new File(fullPath));
            BufferedInputStream bis = new BufferedInputStream(uploadExceptionExcel.getInputStream());
            bos = new BufferedOutputStream(fos);
            final int BUFF_SIZE = 1024;
            byte[] buff = new byte[BUFF_SIZE];
            int len ;
            while ((len = bis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            bos.flush();
            bos.close();

            //解析ExcelException并写入数据库
            purchaseExcelExceptionRead(fullPath);

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

    private void purchaseExcelExceptionRead(String file) throws Exception{

             ArrayList<ArrayList<Object>> rowList = null;
            try{
                rowList = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
            } catch (Exception e){
                throw new ServiceException("读取Excel错误,请检查后重试或联系管理员");
            }
            for (ArrayList<Object> row : rowList) {


                String row1 = ExcelReadHelper.getCell(row.get(1));
                String orderId = "";
                if (row1 != null) {
                    orderId = row1;
                }

                String row7 = ExcelReadHelper.getCell(row.get(7));
                String sku = "";
                if (row7 != null) {
                    sku = row7;
                }

                String row18 = ExcelReadHelper.getCell(row.get(18));
                String remark = "";
                if (row18 != null) {
                    remark = row18;
                }

                String row20 = ExcelReadHelper.getCell(row.get(20));
                String orderItemId = "" ;
                if (row20 != null) {
                    orderItemId = row20;
                } else {
                    throw new ServiceException("订单号:" + orderId + ", SKU:" + sku + "对应的orderItemId为空");
                }

                OrderDetail orderDetail = orderDetailDao.findByOrderIdAndOrderItemId(orderId, orderItemId);
                if (orderDetail != null){
                    OrderStatus orderStatus = orderDetail.getOrderStatus();
                    if(orderStatus != null && orderStatus.getId() != null ){
                        if(orderStatus.getId() == 2L){
                            orderDetail.setOrderStatus(new OrderStatus(1L));
                            orderDetail.setRemark(remark);
                            orderDetailDao.save(orderDetail);
                        }
                    } else {
                        throw new ServiceException("订单出现异常错误,请联系管理员!");
                    }
                } else {
                    throw new ServiceException("未找到订单号:" + orderId + ", SKU:" + sku + ",OrderItemId: " + orderItemId);
                }
            }

    }


    public ResponseVo uploadAlibabaOrder(MultipartFile uploadExceptionExcel) {
        ResponseVo responseVo = ResponseVo.newInstance();
        String fileName = uploadExceptionExcel.getOriginalFilename();
        String suffix = Path.getSuffixFromPath(fileName);
        if (!suffix.equals("xls") && !suffix.equals("xlsx")) {
            responseVo.setResult(ResponseVoResultCode.CODE_PARAM_ERROR);
            responseVo.setMessage("文件格式错误，不是excel文件");
            return responseVo;
        }

        BufferedOutputStream bos = null;

        try {
            String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);

            String relativeFold = "purchase" + File.separator + now + File.separator + getCurrentUserId() + File
                    .separator;
            String fullRelativeFold = PathConstant.FILE_STORE_PATH + relativeFold;
            String relativePath = relativeFold + System.currentTimeMillis() + "." + suffix;
            String fullPath = PathConstant.FILE_STORE_PATH + relativePath;

            File fullFoldFile = new File(fullRelativeFold);
            if (!fullFoldFile.exists()) {
                fullFoldFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(new File(fullPath));
            BufferedInputStream bis = new BufferedInputStream(uploadExceptionExcel.getInputStream());
            bos = new BufferedOutputStream(fos);
            final int BUFF_SIZE = 1024;
            byte[] buff = new byte[BUFF_SIZE];
            int len ;
            while ((len = bis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            bos.flush();
            bos.close();

            //解析ExcelException并写入数据库
            purchaseExcelAlibabaOrderRead(fullPath);

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

    private void purchaseExcelAlibabaOrderRead(String file) throws Exception{
             ArrayList<ArrayList<Object>> rowList =null;
            try {
                rowList = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
            } catch (Exception e){
                throw new ServiceException("读取Excel错误,请检查后重试或联系管理员");
            }
            for (ArrayList<Object> row : rowList) {

                String row0 = ExcelReadHelper.getCell(row.get(0));
                String platformOrderId = "";
                if (row0 != null) {
                    platformOrderId = row0;
                }

                String row1 = ExcelReadHelper.getCell(row.get(1));
                String buyerCompany = "";
                if (row1 != null) {
                    buyerCompany = row1;
                }

                String row2 = ExcelReadHelper.getCell(row.get(2));
                String buyerName = "";
                if (row2 != null) {
                    buyerName = row2;
                }

                String row3 = ExcelReadHelper.getCell(row.get(3));
                String sellerCompany = "";
                if (row3 != null) {
                    sellerCompany = row3;
                }

                String row4 = ExcelReadHelper.getCell(row.get(4));
                String sellerName = "";
                if (row4 != null) {
                    sellerName = row4;
                }

                String row5 = ExcelReadHelper.getCell(row.get(5));
                Double total = 0.00;
                if (row5 != null) {
                    total = Double.valueOf(row5);
                }

                String row6 = ExcelReadHelper.getCell(row.get(6));
                Double shipFee = 0.00;
                if (row6 != null) {
                    shipFee = Double.valueOf(row6);
                }

                String row7 = ExcelReadHelper.getCell(row.get(7));
                Double discount = 0.00;
                if (row7 != null) {
                    discount = Double.valueOf(row7);
                }

                String row8 = ExcelReadHelper.getCell(row.get(8));
                Double ticket = 0.00;
                if (row8 != null) {
                    ticket = Double.valueOf(row8);
                }

                String row9 = ExcelReadHelper.getCell(row.get(9));
                Double totalPay = 0.00;
                if (row9 != null) {
                    totalPay = Double.valueOf(row9);
                }

                String row10 = ExcelReadHelper.getCell(row.get(10));
                String orderStatus = "";
                if (row10 != null) {
                    orderStatus = row10;
                }

                String row11 = ExcelReadHelper.getCell(row.get(11));
                Date purchaseDate = new Date();
                if (row11 != null) {
                    purchaseDate = new Date(row11);
                }

                String row12 = ExcelReadHelper.getCell(row.get(12));
                Date paymentsDate = new Date();
                if (row12 != null) {
                    paymentsDate = new Date(row12);
                }

                String row13 = ExcelReadHelper.getCell(row.get(13));
                String recipientName = "";
                if (row13 != null) {
                    recipientName = row13;
                }

                String row14 = ExcelReadHelper.getCell(row.get(14));
                String shipAddress = "";
                if (row14 != null) {
                    shipAddress = row14;
                }

                String row15 = ExcelReadHelper.getCell(row.get(15));
                String shipPostalCode = "";
                if (row15 != null) {
                    shipPostalCode = row15;
                }

                String row16 = ExcelReadHelper.getCell(row.get(16));
                String shipPhoneNumber = "";
                if (row16 != null) {
                    shipPhoneNumber = row16;
                }

                String row17 = ExcelReadHelper.getCell(row.get(17));
                String shipCallNumber = "";
                if (row17 != null) {
                    shipCallNumber = row17;
                }

                String row18 = ExcelReadHelper.getCell(row.get(18));
                String productTitle = "";
                if (row18 != null) {
                    productTitle = row18;
                }

                String row19 = ExcelReadHelper.getCell(row.get(19));
                Double productPrice = 0.00;
                if (row19 != null) {
                    productPrice = Double.valueOf(row19);
                }

                String row20 = ExcelReadHelper.getCell(row.get(20));
                Long quantity = 0L;
                if (row20 != null) {
                    quantity = Long.valueOf(row20);
                }

                String row21 = ExcelReadHelper.getCell(row.get(21));
                String unit = "";
                if (row21 != null) {
                    unit = row21;
                }

                String row22 = ExcelReadHelper.getCell(row.get(22));
                String productNo = "";
                if (row22 != null) {
                    productNo = row22;
                }

                String row23 = ExcelReadHelper.getCell(row.get(23));
                String productModel = "";
                if (row23 != null) {
                    productModel = row23;
                }

                String row24 = ExcelReadHelper.getCell(row.get(24));
                Long productType = 0L;
                if (row24 != null) {
                    productType = Long.valueOf(row24);
                }

                String row25 = ExcelReadHelper.getCell(row.get(25));
                String buyerMessage = "";
                if (row25 != null) {
                    buyerMessage = row25;
                }

                String row26 = ExcelReadHelper.getCell(row.get(26));
                String expressName = "";
                String trackno = "";
                if (row26 != null) {
                    expressName = row26.substring(0, row26.indexOf(':'));
                    trackno = row26.substring(row26.indexOf(':') + 1, row26.length() - 1);
                }

                String row27 = ExcelReadHelper.getCell(row.get(27));
                String invoiceBuyerCompany = "";
                if (row27 != null) {
                    invoiceBuyerCompany = row27;
                }

                String row28 = ExcelReadHelper.getCell(row.get(28));
                String invoiceTaxpayer = "";
                if (row28 != null) {
                    invoiceTaxpayer = row28;
                }

                String row29 = ExcelReadHelper.getCell(row.get(29));
                String invoiceAddressPhone = "";
                if (row29 != null) {
                    invoiceAddressPhone = row29;
                }

                String row30 = ExcelReadHelper.getCell(row.get(30));
                String invoiceBank = "";
                if (row30 != null) {
                    invoiceBank = row30;
                }

                String row31 = ExcelReadHelper.getCell(row.get(31));
                String invoiceAddress = "";
                if (row31 != null) {
                    invoiceAddress = row31;
                }

                String row32 = ExcelReadHelper.getCell(row.get(32));
                String associationNumber = "";
                if (row32 != null) {
                    invoiceAddress = row32;
                }

                String row33 = ExcelReadHelper.getCell(row.get(33));
                Double creditCharge = 0.00;
                if (row33 != null) {
                    invoiceAddress = row33;
                }

                AlibabaOrder alibabaOrderExist = alibabaOrderDao.findByPlatformOrderId(platformOrderId);
                if (alibabaOrderExist == null) {
                    AlibabaOrder alibabaOrder = new AlibabaOrder();
                    alibabaOrder.setPlatformOrderId(platformOrderId);
                    alibabaOrder.setBuyerCompany(buyerCompany);
                    alibabaOrder.setBuyerName(buyerName);
                    alibabaOrder.setSellerCompany(sellerCompany);
                    alibabaOrder.setSellerName(sellerName);
                    alibabaOrder.setTotal(total);
                    alibabaOrder.setShipFee(shipFee);
                    alibabaOrder.setDiscount(discount);
                    alibabaOrder.setTicket(ticket);
                    alibabaOrder.setTotalPay(totalPay);
                    alibabaOrder.setOrderStatus(orderStatus);
                    alibabaOrder.setPurchaseDate(purchaseDate);
                    alibabaOrder.setPaymentsDate(paymentsDate);
                    alibabaOrder.setRecipientName(recipientName);
                    alibabaOrder.setShipAddress(shipAddress);
                    alibabaOrder.setShipPostalCode(shipPostalCode);
                    alibabaOrder.setShipPhoneNumber(shipPhoneNumber);
                    alibabaOrder.setShipCallNumber(shipCallNumber);
                    alibabaOrder.setProductType(productType);
                    alibabaOrder.setBuyerMessage(buyerMessage);
                    alibabaOrder.setExpressName(expressName);
                    alibabaOrder.setTrackno(trackno);
                    alibabaOrder.setInvoiceBuyerCompany(invoiceBuyerCompany);
                    alibabaOrder.setInvoiceTaxpayer(invoiceTaxpayer);
                    alibabaOrder.setInvoiceAddressPhone(invoiceAddressPhone);
                    alibabaOrder.setInvoiceBank(invoiceBank);
                    alibabaOrder.setInvoiceAddress(invoiceAddress);
                    alibabaOrder.setAssociationNumber(associationNumber);
                    alibabaOrder.setCreditCharge(creditCharge);
                    alibabaOrderDao.save(alibabaOrder);

                    AlibabaOrderDetail alibabaOrderDetail = new AlibabaOrderDetail();
                    alibabaOrderDetail.setPlatformOrderId(platformOrderId);
                    alibabaOrderDetail.setBuyerCompany(buyerCompany);
                    alibabaOrderDetail.setBuyerName(buyerName);
                    alibabaOrderDetail.setSellerCompany(sellerCompany);
                    alibabaOrderDetail.setSellerName(sellerName);
                    alibabaOrderDetail.setTotal(total);
                    alibabaOrderDetail.setShipFee(shipFee);
                    alibabaOrderDetail.setDiscount(discount);
                    alibabaOrderDetail.setTicket(ticket);
                    alibabaOrderDetail.setTotalPay(totalPay);
                    alibabaOrderDetail.setOrderStatus(orderStatus);
                    alibabaOrderDetail.setPurchaseDate(purchaseDate);
                    alibabaOrderDetail.setPaymentsDate(paymentsDate);
                    alibabaOrderDetail.setRecipientName(recipientName);
                    alibabaOrderDetail.setShipAddress(shipAddress);
                    alibabaOrderDetail.setShipPostalCode(shipPostalCode);
                    alibabaOrderDetail.setShipPhoneNumber(shipPhoneNumber);
                    alibabaOrderDetail.setShipCallNumber(shipCallNumber);
                    alibabaOrderDetail.setProductTitle(productTitle);
                    alibabaOrderDetail.setProductPrice(productPrice);
                    alibabaOrderDetail.setQuantity(quantity);
                    alibabaOrderDetail.setUnit(unit);
                    alibabaOrderDetail.setProductNo(productNo);
                    alibabaOrderDetail.setProductModel(productModel);
                    alibabaOrderDetail.setProductType(productType);
                    alibabaOrderDetail.setBuyerMessage(buyerMessage);
                    alibabaOrderDetail.setExpressName(expressName);
                    alibabaOrderDetail.setTrackno(trackno);
                    alibabaOrderDetail.setInvoiceBuyerCompany(invoiceBuyerCompany);
                    alibabaOrderDetail.setInvoiceTaxpayer(invoiceTaxpayer);
                    alibabaOrderDetail.setInvoiceAddressPhone(invoiceAddressPhone);
                    alibabaOrderDetail.setInvoiceBank(invoiceBank);
                    alibabaOrderDetail.setInvoiceAddress(invoiceAddress);
                    alibabaOrderDetail.setAssociationNumber(associationNumber);
                    alibabaOrderDetail.setCreditCharge(creditCharge);
                    alibabaOrderDetail.setAlibabaOrder(alibabaOrder);

                    alibabaOrderDetailDao.save(alibabaOrderDetail);
                } else {
                    alibabaOrderExist.setModified(clock.getCurrentDate());
                    alibabaOrderExist.setPlatformOrderId(platformOrderId);
                    alibabaOrderExist.setBuyerCompany(buyerCompany);
                    alibabaOrderExist.setBuyerName(buyerName);
                    alibabaOrderExist.setSellerCompany(sellerCompany);
                    alibabaOrderExist.setSellerName(sellerName);
                    alibabaOrderExist.setTotal(total);
                    alibabaOrderExist.setShipFee(shipFee);
                    alibabaOrderExist.setDiscount(discount);
                    alibabaOrderExist.setTicket(ticket);
                    alibabaOrderExist.setTotalPay(totalPay);
                    alibabaOrderExist.setOrderStatus(orderStatus);
                    alibabaOrderExist.setPurchaseDate(purchaseDate);
                    alibabaOrderExist.setPaymentsDate(paymentsDate);
                    alibabaOrderExist.setRecipientName(recipientName);
                    alibabaOrderExist.setShipAddress(shipAddress);
                    alibabaOrderExist.setShipPostalCode(shipPostalCode);
                    alibabaOrderExist.setShipPhoneNumber(shipPhoneNumber);
                    alibabaOrderExist.setShipCallNumber(shipCallNumber);
                    alibabaOrderExist.setProductType(productType);
                    alibabaOrderExist.setBuyerMessage(buyerMessage);
                    alibabaOrderExist.setExpressName(expressName);
                    alibabaOrderExist.setTrackno(trackno);
                    alibabaOrderDao.save(alibabaOrderExist);

                    AlibabaOrderDetail alibabaOrderDetailExist = alibabaOrderDetailDao.findByPlatformOrderIdAndProductTitle(platformOrderId, productTitle);
                    if (alibabaOrderDetailExist == null) {
                        AlibabaOrderDetail alibabaOrderDetail = new AlibabaOrderDetail();
                        alibabaOrderDetail.setPlatformOrderId(platformOrderId);
                        alibabaOrderDetail.setBuyerCompany(buyerCompany);
                        alibabaOrderDetail.setBuyerName(buyerName);
                        alibabaOrderDetail.setSellerCompany(sellerCompany);
                        alibabaOrderDetail.setSellerName(sellerName);
                        alibabaOrderDetail.setTotal(total);
                        alibabaOrderDetail.setShipFee(shipFee);
                        alibabaOrderDetail.setDiscount(discount);
                        alibabaOrderDetail.setTicket(ticket);
                        alibabaOrderDetail.setTotalPay(totalPay);
                        alibabaOrderDetail.setOrderStatus(orderStatus);
                        alibabaOrderDetail.setPurchaseDate(purchaseDate);
                        alibabaOrderDetail.setPaymentsDate(paymentsDate);
                        alibabaOrderDetail.setRecipientName(recipientName);
                        alibabaOrderDetail.setShipAddress(shipAddress);
                        alibabaOrderDetail.setShipPostalCode(shipPostalCode);
                        alibabaOrderDetail.setShipPhoneNumber(shipPhoneNumber);
                        alibabaOrderDetail.setShipCallNumber(shipCallNumber);
                        alibabaOrderDetail.setProductTitle(productTitle);
                        alibabaOrderDetail.setProductPrice(productPrice);
                        alibabaOrderDetail.setQuantity(quantity);
                        alibabaOrderDetail.setUnit(unit);
                        alibabaOrderDetail.setProductNo(productNo);
                        alibabaOrderDetail.setProductModel(productModel);
                        alibabaOrderDetail.setProductType(productType);
                        alibabaOrderDetail.setBuyerMessage(buyerMessage);
                        alibabaOrderDetail.setExpressName(expressName);
                        alibabaOrderDetail.setTrackno(trackno);
                        alibabaOrderDetail.setInvoiceBuyerCompany(invoiceBuyerCompany);
                        alibabaOrderDetail.setInvoiceTaxpayer(invoiceTaxpayer);
                        alibabaOrderDetail.setInvoiceAddressPhone(invoiceAddressPhone);
                        alibabaOrderDetail.setInvoiceBank(invoiceBank);
                        alibabaOrderDetail.setInvoiceAddress(invoiceAddress);
                        alibabaOrderDetail.setAssociationNumber(associationNumber);
                        alibabaOrderDetail.setCreditCharge(creditCharge);
                        alibabaOrderDetail.setAlibabaOrder(alibabaOrderExist);

                        alibabaOrderDetailDao.save(alibabaOrderDetail);
                    } else {
                        alibabaOrderDetailExist.setPlatformOrderId(platformOrderId);
                        alibabaOrderDetailExist.setBuyerCompany(buyerCompany);
                        alibabaOrderDetailExist.setBuyerName(buyerName);
                        alibabaOrderDetailExist.setSellerCompany(sellerCompany);
                        alibabaOrderDetailExist.setSellerName(sellerName);
                        alibabaOrderDetailExist.setTotal(total);
                        alibabaOrderDetailExist.setShipFee(shipFee);
                        alibabaOrderDetailExist.setDiscount(discount);
                        alibabaOrderDetailExist.setTicket(ticket);
                        alibabaOrderDetailExist.setTotalPay(totalPay);
                        alibabaOrderDetailExist.setOrderStatus(orderStatus);
                        alibabaOrderDetailExist.setPurchaseDate(purchaseDate);
                        alibabaOrderDetailExist.setPaymentsDate(paymentsDate);
                        alibabaOrderDetailExist.setRecipientName(recipientName);
                        alibabaOrderDetailExist.setShipAddress(shipAddress);
                        alibabaOrderDetailExist.setShipPostalCode(shipPostalCode);
                        alibabaOrderDetailExist.setShipPhoneNumber(shipPhoneNumber);
                        alibabaOrderDetailExist.setShipCallNumber(shipCallNumber);
                        alibabaOrderDetailExist.setProductTitle(productTitle);
                        alibabaOrderDetailExist.setProductPrice(productPrice);
                        alibabaOrderDetailExist.setQuantity(quantity);
                        alibabaOrderDetailExist.setUnit(unit);
                        alibabaOrderDetailExist.setProductNo(productNo);
                        alibabaOrderDetailExist.setProductModel(productModel);
                        alibabaOrderDetailExist.setProductType(productType);
                        alibabaOrderDetailExist.setBuyerMessage(buyerMessage);
                        alibabaOrderDetailExist.setExpressName(expressName);
                        alibabaOrderDetailExist.setTrackno(trackno);
                        alibabaOrderDetailExist.setInvoiceBuyerCompany(invoiceBuyerCompany);
                        alibabaOrderDetailExist.setInvoiceTaxpayer(invoiceTaxpayer);
                        alibabaOrderDetailExist.setInvoiceAddressPhone(invoiceAddressPhone);
                        alibabaOrderDetailExist.setInvoiceBank(invoiceBank);
                        alibabaOrderDetailExist.setInvoiceAddress(invoiceAddress);
                        alibabaOrderDetailExist.setAssociationNumber(associationNumber);
                        alibabaOrderDetailExist.setCreditCharge(creditCharge);
                        alibabaOrderDetailExist.setAlibabaOrder(alibabaOrderExist);
                        alibabaOrderDetailExist.setModified(clock.getCurrentDate());

                        alibabaOrderDetailDao.save(alibabaOrderDetailExist);
                    }
                }

            }
    }


    public void savePurchaseList(List<Purchase> purchaseList) {
        for (Purchase purchase : purchaseList) {
            purchaseDao.save(purchase);
        }
    }



    public ExcelVo downloadException() {
        ExcelVo excelVo = new ExcelVo();

        List<OrderDetail> orderDetailList = orderDetailDao.findAllChanged();
        Map<String, List<OrderDetail>> shopOrderDetailMap = Maps.newHashMap();
        for(OrderDetail orderDetail: orderDetailList){
            String shopName = orderDetail.getOrganization().getName();
            List<OrderDetail> orderDetails = shopOrderDetailMap.get(shopName);
            if (orderDetails == null){
                List<OrderDetail> newOrderDetails = Lists.newArrayList();
                newOrderDetails.add(orderDetail);
                shopOrderDetailMap.put(shopName, newOrderDetails);
            } else {
                orderDetails.add(orderDetail);
                shopOrderDetailMap.put(shopName, orderDetails);
            }
            orderDetail.setChanged(false);
            orderDetailDao.save(orderDetail);
        }

        String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);
        String relativeFold = "purchase" + File.separator + "normal" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
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

    public ExcelVo download() {
        ExcelVo excelVo = new ExcelVo();

        Date startDate = new Date(DateUtil.startOfToday());
        Date endDate = new Date(DateUtil.endOfToday());
        List<OrderDetail> orderDetailList = orderDetailDao.findAllTodayNormal(startDate, endDate);
        Map<String, List<OrderDetail>> shopOrderDetailMap = Maps.newHashMap();
        for (OrderDetail orderDetail : orderDetailList) {
            String shopName = orderDetail.getOrganization().getName();
            List<OrderDetail> orderDetails = shopOrderDetailMap.get(shopName);
            if (orderDetails == null) {
                List<OrderDetail> newOrderDetails = Lists.newArrayList();
                newOrderDetails.add(orderDetail);
                shopOrderDetailMap.put(shopName, newOrderDetails);
            } else {
                orderDetails.add(orderDetail);
                shopOrderDetailMap.put(shopName, orderDetails);
            }
        }

        String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);
        String relativeFold = "purchase" + File.separator + "normal" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
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


    public void orderExcelExport(Map<String, List<OrderDetail>> shopOrderDetailMap, String fullPath) {

        List<String> shopList = Lists.newArrayList();
        for(Map.Entry<String, List<OrderDetail>> shopOrderDetail: shopOrderDetailMap.entrySet()){
            shopList.add(shopOrderDetail.getKey());
        }

        ExcelExportHelper excelExportHelper = new ExcelExportHelper(shopList);
        List<Sheet> sheetList = excelExportHelper.getSheets();
        for(Sheet sheet: sheetList){
            excelExportHelper.createRow(sheet);
            excelExportHelper.setCellHeader(0, "订货日期");
            excelExportHelper.setCellHeader(1, "采购订单号");
            excelExportHelper.setCellHeader(2, "平台订单号");
            excelExportHelper.setCellHeader(3, "店铺归属");
            excelExportHelper.setCellHeader(4, "供应商名称");
            excelExportHelper.setCellHeader(5, "商品名称");
            excelExportHelper.setCellHeader(6, "卖家旺旺号");
            excelExportHelper.setCellHeader(7, "SKU");
            excelExportHelper.setCellHeader(8, "货号");
            excelExportHelper.setCellHeader(9, "事物特性");
            excelExportHelper.setCellHeader(10, "数量");
            excelExportHelper.setCellHeader(11, "单价");
            excelExportHelper.setCellHeader(12, "运费");
            excelExportHelper.setCellHeader(13, "总价");
            excelExportHelper.setCellHeader(14, "类目");
            excelExportHelper.setCellHeader(15, "产地");
            excelExportHelper.setCellHeader(16, "链接");
            excelExportHelper.setCellHeader(17, "备用链接");
            excelExportHelper.setCellHeader(18, "备注");
            excelExportHelper.setCellHeader(19, "货源");
            excelExportHelper.setCellHeader(20,"orderItemId");

        }

        for (Map.Entry<String, List<OrderDetail>> shopOrderDetail : shopOrderDetailMap.entrySet()) {
            String shopName = shopOrderDetail.getKey();
            List<OrderDetail> orderDetailList = shopOrderDetail.getValue();
            Sheet sheet = null;
            for(int i=0; i<shopList.size(); i++){
                String shop = shopList.get(i);
                if (shop.equals(shopName)){
                    sheet = sheetList.get(i);
                }
            }
            for(OrderDetail orderDetail: orderDetailList){
                excelExportHelper.createRow(sheet);
                excelExportHelper.setCell(0, DateUtil.getDate());
                excelExportHelper.setCell(1, orderDetail.getOrderId());
                excelExportHelper.setCell(3, orderDetail.getOrganization().getName());
                excelExportHelper.setCell(7, orderDetail.getSku());
                excelExportHelper.setCell(10, orderDetail.getQuantityPurchased());
                excelExportHelper.setCell(20, orderDetail.getOrderItemId());
                if (orderDetail.getProductDetail() != null){
                    excelExportHelper.setCell(4, orderDetail.getProductDetail().getSupplierName());
                    excelExportHelper.setCell(8, orderDetail.getProductDetail().getProductNo());
                    excelExportHelper.setCell(9, orderDetail.getProductDetail().getFeature());
                    excelExportHelper.setCell(11, orderDetail.getProductDetail().getPrice());
                    excelExportHelper.setCell(14, orderDetail.getProductDetail().getCategory());
                    excelExportHelper.setCell(15, orderDetail.getProductDetail().getOrigin());
                    excelExportHelper.setCell(16, orderDetail.getProductDetail().getLink());
                    excelExportHelper.setCell(17, orderDetail.getProductDetail().getAlternateLink());
                    excelExportHelper.setCell(18, orderDetail.getProductDetail().getRemark());
                }

            }
        }

        excelExportHelper.export(fullPath);
    }


    public void deletePurchase(Long id) {
        purchaseDao.delete(id);
    }

    /**
     * 获取所有的Purchase对象
     */
    public List<Purchase> getAllPurchase() {
        return (List<Purchase>) purchaseDao.findAll();
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }

    public TableDTO<PurchaseVo> getPurchaseVo(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                        String sortType) {
        Page<PurchaseDetail> purchaseDetails = SearchPurchaseDetail(searchParams, pageNumber, pageSize, sortType);
        TableDTO<PurchaseVo> tableDTO = new TableDTO<>();
        List<PurchaseVo> purchaseVoList = Lists.newArrayList();
        tableDTO.setTotal(purchaseDetails.getTotalElements());
        for (PurchaseDetail purchaseDetail : purchaseDetails.getContent()) {
            PurchaseVo purchaseVo = new PurchaseVo();
            purchaseVo.setId(purchaseDetail.getId());
            purchaseVo.setPurchaseOrderId(purchaseDetail.getPurchaseOrderId());
            purchaseVo.setPlatformOrderId(purchaseDetail.getPlatformOrderId());
            purchaseVo.setPurchaseDate(purchaseDetail.getPurchaseDate());
            purchaseVo.setSku(purchaseDetail.getSku());
            purchaseVo.setOrderItemId(purchaseDetail.getOrderItemId());
            purchaseVo.setQuantity(purchaseDetail.getQuantity());
            purchaseVo.setOrganizationName(purchaseDetail.getOrganization().getName());
            purchaseVo.setCategory(purchaseDetail.getCategory());
            purchaseVo.setOrigin(purchaseDetail.getOrigin());
            purchaseVo.setProductLink(purchaseDetail.getProductLink());
            purchaseVo.setAlternateLink(purchaseDetail.getAlternateLink());
            purchaseVo.setProductName(purchaseDetail.getProductName());
            purchaseVo.setProductNo(purchaseDetail.getProductNo());
            purchaseVo.setProductFeature(purchaseDetail.getProductFeature());
            purchaseVo.setPrice(purchaseDetail.getPrice());
            purchaseVo.setShipDiscount(purchaseDetail.getShipDiscount());
            purchaseVo.setSupplierName(purchaseDetail.getSupplierName());
            purchaseVo.setWwName(purchaseDetail.getWwName());
            purchaseVo.setRemark(purchaseDetail.getRemark());

            purchaseVoList.add(purchaseVo);
        }
        tableDTO.setRows(purchaseVoList);
        return tableDTO;
    }

    /**
     * 按页面传来的查询条件查询订单
     */
    public Page<PurchaseDetail> SearchPurchaseDetail(Map<String, Object> searchParams,
                                         int pageNumber, int pageSize, String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<PurchaseDetail> spec = buildSpecification(searchParams);

        return purchaseDetailDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<PurchaseDetail> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<PurchaseDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), PurchaseDetail.class);
        return spec;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType) || "desc".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "id");
        } else if ("asc".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "id");
        }

        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }

    public void deletePurchaseDetailChecked(List<Long> purchaseDetailChecked){
        for(Long purchaseDetailId: purchaseDetailChecked){
            purchaseDetailDao.delete(purchaseDetailId);
        }
    }

    /**
     * 设定平台订单号
     */
    private String encryptPlatformOrderId(String purchaseOrderId, String orderItemId) {

        byte[] hashPlatformOrderId = Digests.sha1(purchaseOrderId.getBytes(), String.valueOf(orderItemId).getBytes(), HASH_ITERATIONS);
        return Encodes.encodeHex(hashPlatformOrderId);
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
    public void setPurchaseDao(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    @Autowired
    public void setPurchaseDetailDao(PurchaseDetailDao purchaseDetailDao) {
        this.purchaseDetailDao = purchaseDetailDao;
    }

    @Autowired
    public void setOrganizationDao(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
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
    public void setAlibabaOrderDao(AlibabaOrderDao alibabaOrderDao) {
        this.alibabaOrderDao = alibabaOrderDao;
    }

    @Autowired
    public void setAlibabaOrderDetailDao(AlibabaOrderDetailDao alibabaOrderDetailDao) {
        this.alibabaOrderDetailDao = alibabaOrderDetailDao;
    }
}
