package com.wantdo.stat.service.shop.market;

import com.google.common.collect.Lists;
import com.wantdo.stat.constant.DateFormatConstant;
import com.wantdo.stat.constant.PathConstant;
import com.wantdo.stat.dao.shop.OrderDetailDao;
import com.wantdo.stat.dao.shop.ProductDao;
import com.wantdo.stat.dao.shop.ProductDetailDao;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.front.response.ResponseVoResultCode;
import com.wantdo.stat.entity.front.vo.ExcelVo;
import com.wantdo.stat.entity.front.vo.ProductDetailVo;
import com.wantdo.stat.entity.front.vo.ProductVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.shop.*;
import com.wantdo.stat.excel.helper.ExcelExportHelper;
import com.wantdo.stat.excel.helper.ExcelReadHelper;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.Hibernates;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.persistence.SearchFilter.Operator;
import com.wantdo.stat.service.account.ServiceException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.utils.Clock;
import com.wantdo.stat.utils.DateUtil;
import com.wantdo.stat.utils.Path;
import org.apache.commons.lang3.StringUtils;
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
 * 产品业务类
 *
 * @ Date : 2015-9-17
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class ProductService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(ProductService.class);

    private ProductDao productDao;

    private ProductDetailDao productDetailDao;

    private OrderDetailDao orderDetailDao;

    private Clock clock = Clock.DEFAULT;

    /**
     * 按Id获得产品
     */
    public Product getProduct(Long id) {
        return productDao.findOne(id);
    }

    public ProductDetail getProductDetail(Long id) {
        return productDetailDao.findOne(id);
    }

    public void saveProduct(Product product) {
        product.setCreated(clock.getCurrentDate());
        productDao.save(product);
    }

    public void saveProductDetail(ProductDetail productDetail) {
        productDetail.setCreated(clock.getCurrentDate());
        productDetailDao.save(productDetail);
    }

    public void updateProduct(Product product) {
        product.setModified(clock.getCurrentDate());
        productDao.save(product);
    }

    public void updateProductDetail(ProductDetail productDetail) {
        productDetail.setModified(clock.getCurrentDate());
        productDetailDao.save(productDetail);

        //14点至15点为上传订单时间
        long purchaseStartTime = DateUtil.selectOfToday(14, 0, 0, 0);
        long purchaseEndTime = DateUtil.selectOfToday(15, 0, 0, 0);
        long currentTime = new Date().getTime();

        List<OrderDetail> orderDetailList = orderDetailDao.findAllOrderDetailExceptionBySku(productDetail.getSku());
        if (orderDetailList != null && orderDetailList.size() > 0) {
            Order order = orderDetailList.get(0).getOrder();
            order.setOrderStatus(new OrderStatus(2L));
            order.setRemark("");
            if (currentTime < purchaseStartTime || currentTime > purchaseEndTime) {
                order.setChanged(true);
            }
            for (OrderDetail orderDetail : orderDetailList) {
                orderDetail.setOrderStatus(new OrderStatus(2L));
                order.setRemark("");
                if (currentTime < purchaseStartTime || currentTime > purchaseEndTime) {
                    orderDetail.setChanged(true);
                }
                orderDetail.setOrder(order);
                orderDetailDao.save(orderDetail);
            }
        }
    }

    /**
     * 上传产品
     */
    public ResponseVo upload(MultipartFile uploadExcel, Long organizationId){
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

            String relativeFold = "market" + File.separator + "product" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
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
            productExcelRead(fullPath, organizationId);

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

    private void productExcelRead(String file, Long organizationId) throws  Exception{

            ArrayList<ArrayList<Object>> rowList = null;
            try{
                rowList = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
            } catch (Exception e){
                logger.error(e.getMessage());
                throw new ServiceException("读取Excel错误(可能包含公式或数据填写错误),请检查后重试或联系管理员");
            }
            for (ArrayList<Object> row : rowList) {

                String row0 = ExcelReadHelper.getCell(row.get(0));
                String sku = "";
                if (row0 != null) {
                    sku = row0;
                } else {
                    continue;
                }

                String row1 = ExcelReadHelper.getCell(row.get(1));
                String parentChild = "";
                if (row1 != null) {
                    parentChild = row1;
                } else {
                    throw new ServiceException("SKU:"+ sku + ", parent_child为必填项");
                }

                String row2 = ExcelReadHelper.getCell(row.get(2));
                String parentSku = "";
                if (row2 != null) {
                    parentSku = row2;
                }

                String row3 = ExcelReadHelper.getCell(row.get(3));
                String supplierName = "";
                if (row3 != null) {
                    supplierName = row3;
                }

                String row4 = ExcelReadHelper.getCell(row.get(4));
                String productName = "";
                if (row4 != null) {
                    productName = row4;
                }

                String row5 = ExcelReadHelper.getCell(row.get(5));
                String productNo = "";
                if (row5 != null) {
                    productNo = row5;
                }

                String row6 = ExcelReadHelper.getCell(row.get(6));
                String color = "";
                if (row6 != null) {
                    color = row6;
                }

                String row7 = ExcelReadHelper.getCell(row.get(7));
                String size = "";
                if (row7 != null) {
                    size = row7;
                }

                String row8 = ExcelReadHelper.getCell(row.get(8));
                String feature = "";
                if (row8 != null) {
                    feature = row8;
                }

                String row9 = ExcelReadHelper.getCell(row.get(9));
                String price = "";
                if (row9 != null) {
                    price = row9;
                }

                String row10 = ExcelReadHelper.getCell(row.get(10));
                String weight = "";
                if (row10 != null) {
                    weight = row10;
                }

                String row11 = ExcelReadHelper.getCell(row.get(11));
                String category = "";
                if (row11 != null) {
                    category = row11;
                }

                String row12 = ExcelReadHelper.getCell(row.get(12));
                String origin = "";
                if (row12 != null) {
                    origin = row12;
                }

                String row13 = ExcelReadHelper.getCell(row.get(13));
                String link = "";
                if (row13 != null) {
                    link = row13;
                }

                String row14 = ExcelReadHelper.getCell(row.get(14));
                String alternateLink = "";
                if (row14 != null) {
                    alternateLink = row14;
                }

                String row15 = ExcelReadHelper.getCell(row.get(15));
                String remark = "";
                if (row15 != null) {
                    remark = row15;
                }

                if (StringUtils.isNotEmpty(parentChild)){
                    if (parentChild.equalsIgnoreCase("parent")) {
                        Product productExist= productDao.findBySku(sku);
                        if (productExist == null){
                            Product product = new Product();
                            product.setSku(sku);
                            product.setSupplierName(supplierName);
                            product.setProductNo(productNo);
                            product.setProductName(productName);
                            product.setCategory(category);
                            product.setOrigin(origin);
                            product.setLink(link);
                            product.setAlternateLink(alternateLink);
                            product.setRemark(remark);
                            product.setOrganization(new Organization(organizationId));
                            productDao.save(product);
                        } else {
                            productExist.setSku(sku);
                            productExist.setSupplierName(supplierName);
                            productExist.setProductNo(productNo);
                            productExist.setProductName(productName);
                            productExist.setCategory(category);
                            productExist.setOrigin(origin);
                            productExist.setLink(link);
                            productExist.setAlternateLink(alternateLink);
                            productExist.setRemark(remark);
                            productExist.setOrganization(new Organization(organizationId));
                            productDao.save(productExist);
                        }
                    } else if (parentChild.equalsIgnoreCase("child")) {
                        ProductDetail productDetailExist = productDetailDao.findBySku(sku);
                        if (productDetailExist == null){
                            ProductDetail productDetail = new ProductDetail();

                            if (StringUtils.isNoneEmpty(parentSku)) {
                                Product parentProduct = productDao.findBySku(parentSku);
                                if (parentProduct != null) {
                                    productDetail.setProduct(parentProduct);
                                }else{
                                    throw new ServiceException("SKU:" + sku + "  未找到父体" + parentSku + ",请重试");
                                }
                            }else{
                                throw new ServiceException("SKU:" + sku +" 父体不能为空,请重试");
                            }

                            if (StringUtils.isEmpty(link)){
                                throw new ServiceException("SKU:" + sku + " 采购链接不能为空,请重试");
                            }

                            productDetail.setSku(sku);
                            productDetail.setSupplierName(supplierName);
                            productDetail.setProductNo(productNo);
                            productDetail.setProductName(productName);
                            productDetail.setColor(color);
                            productDetail.setSize(size);
                            productDetail.setFeature(feature);
                            productDetail.setPrice(price);
                            productDetail.setWeight(weight);
                            productDetail.setCategory(category);
                            productDetail.setOrigin(origin);
                            productDetail.setLink(link);
                            productDetail.setAlternateLink(alternateLink);
                            productDetail.setRemark(remark);
                            productDetail.setOrganization(new Organization(organizationId));

                            productDetailDao.save(productDetail);

                        } else {
                            if (StringUtils.isNoneEmpty(parentSku)) {
                                Product parentProduct = productDao.findBySku(parentSku);
                                if (parentProduct != null) {
                                    productDetailExist.setProduct(parentProduct);
                                } else {
                                    throw new ServiceException("SKU:" + sku + "  未找到父体" + parentSku + ",请重试");
                                }
                            } else {
                                throw new ServiceException("SKU:" + sku + " 父体不能为空,请重试");
                            }

                            if (StringUtils.isEmpty(link)) {
                                throw new ServiceException("SKU:" + sku + " 采购链接不能为空,请重试");
                            }

                            productDetailExist.setSku(sku);
                            productDetailExist.setSupplierName(supplierName);
                            productDetailExist.setProductNo(productNo);
                            productDetailExist.setProductName(productName);
                            productDetailExist.setColor(color);
                            productDetailExist.setSize(size);
                            productDetailExist.setFeature(feature);
                            productDetailExist.setPrice(price);
                            productDetailExist.setWeight(weight);
                            productDetailExist.setCategory(category);
                            productDetailExist.setOrigin(origin);
                            productDetailExist.setLink(link);
                            productDetailExist.setAlternateLink(alternateLink);
                            productDetailExist.setRemark(remark);
                            productDetailExist.setOrganization(new Organization(organizationId));

                            productDetailDao.save(productDetailExist);

                        }

                    } else {
                        throw new ServiceException("parent_child填写错误:只能为parent或child");
                    }
                }

                //14点至15点为上传订单时间
                long purchaseStartTime = DateUtil.selectOfToday(14, 0, 0, 0);
                long purchaseEndTime = DateUtil.selectOfToday(15, 0, 0, 0);
                long currentTime = new Date().getTime();

                List<OrderDetail> orderDetailList = orderDetailDao.findAllOrderDetailExceptionBySku(sku);
                if (orderDetailList != null && orderDetailList.size() > 0) {
                    Order order = orderDetailList.get(0).getOrder();
                    order.setOrderStatus(new OrderStatus(2L));
                    order.setRemark("");
                    if (currentTime < purchaseStartTime || currentTime > purchaseEndTime) {
                        order.setChanged(true);
                    }
                    for (OrderDetail orderDetail : orderDetailList) {
                        orderDetail.setOrderStatus(new OrderStatus(2L));
                        order.setRemark("");
                        if (currentTime < purchaseStartTime || currentTime > purchaseEndTime) {
                            orderDetail.setChanged(true);
                        }
                        orderDetail.setOrder(order);
                        orderDetailDao.save(orderDetail);
                    }
                }

            }
    }

    public ExcelVo downloadTemplate() {
        ExcelVo excelVo = new ExcelVo();

        String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);
        String relativeFold = "market" + File.separator + "product" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
        String fullRelativeFold = PathConstant.FILE_STORE_PATH + relativeFold;
        String fileName = System.currentTimeMillis() + ".xls";
        String relativePath = relativeFold + fileName;
        String fullPath = PathConstant.FILE_STORE_PATH + relativePath;

        File fullFoldFile = new File(fullRelativeFold);
        if (!fullFoldFile.exists()) {
            fullFoldFile.mkdirs();
        }

        this.productTemplateExcelExport(fullPath);
        excelVo.setName(fileName);
        excelVo.setPath(fullPath);

        return excelVo;
    }

    public void productTemplateExcelExport( String fullPath) {
        ExcelExportHelper excelExportHelper = new ExcelExportHelper();
        excelExportHelper.createRow();
        excelExportHelper.setCellHeader(0, "SKU");
        excelExportHelper.setCellHeader(1, "parent_child");
        excelExportHelper.setCellHeader(2, "parent_sku");
        excelExportHelper.setCellHeader(3, "供应商名称");
        excelExportHelper.setCellHeader(4, "产品名称");
        excelExportHelper.setCellHeader(5, "货号");
        excelExportHelper.setCellHeader(6, "颜色");
        excelExportHelper.setCellHeader(7, "尺码");
        excelExportHelper.setCellHeader(8, "事物特性");
        excelExportHelper.setCellHeader(9, "单价");
        excelExportHelper.setCellHeader(10, "重量");
        excelExportHelper.setCellHeader(11, "类目");
        excelExportHelper.setCellHeader(12, "产地");
        excelExportHelper.setCellHeader(13, "采购链接");
        excelExportHelper.setCellHeader(14, "备用链接");
        excelExportHelper.setCellHeader(15, "备注");

        excelExportHelper.export(fullPath);
    }

    public ExcelVo downloadProduct( Long organizationId) {
        ExcelVo excelVo = new ExcelVo();

        String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);
        String relativeFold = "market" + File.separator + "product" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
        String fullRelativeFold = PathConstant.FILE_STORE_PATH + relativeFold;
        String fileName = System.currentTimeMillis() + ".xls";
        String relativePath = relativeFold + fileName;
        String fullPath = PathConstant.FILE_STORE_PATH + relativePath;

        File fullFoldFile = new File(fullRelativeFold);
        if (!fullFoldFile.exists()) {
            fullFoldFile.mkdirs();
        }

        this.productExcelExport(organizationId, fullPath);
        excelVo.setName(fileName);
        excelVo.setPath(fullPath);

        return excelVo;
    }

    public void productExcelExport(Long organizationId, String fullPath) {
        ExcelExportHelper excelExportHelper = new ExcelExportHelper();
        excelExportHelper.createRow();
        excelExportHelper.setCellHeader(0, "SKU");
        excelExportHelper.setCellHeader(1, "parent_child");
        excelExportHelper.setCellHeader(2, "parent_sku");
        excelExportHelper.setCellHeader(3, "供应商名称");
        excelExportHelper.setCellHeader(4, "产品名称");
        excelExportHelper.setCellHeader(5, "货号");
        excelExportHelper.setCellHeader(6, "颜色");
        excelExportHelper.setCellHeader(7, "尺码");
        excelExportHelper.setCellHeader(8, "事物特性");
        excelExportHelper.setCellHeader(9, "单价");
        excelExportHelper.setCellHeader(10, "重量");
        excelExportHelper.setCellHeader(11, "类目");
        excelExportHelper.setCellHeader(12, "产地");
        excelExportHelper.setCellHeader(13, "采购链接");
        excelExportHelper.setCellHeader(14, "备用链接");
        excelExportHelper.setCellHeader(15, "备注");

        List<Product> productList = productDao.findByOrganizationId(organizationId);
        for(Product product: productList){
            excelExportHelper.createRow();
            excelExportHelper.setCell(0, product.getSku());
            excelExportHelper.setCell(1, "parent");
            excelExportHelper.setCell(3, product.getSupplierName());
            excelExportHelper.setCell(4, product.getProductName());
            excelExportHelper.setCell(5, product.getProductNo());
            excelExportHelper.setCell(11, product.getCategory());
            excelExportHelper.setCell(12, product.getOrigin());
            excelExportHelper.setCell(13, product.getLink());
            excelExportHelper.setCell(14, product.getAlternateLink());
            excelExportHelper.setCell(15, product.getRemark());

            List<ProductDetail> productDetailList = product.getProductDetailList();
            for(ProductDetail productDetail: productDetailList){
                excelExportHelper.createRow();
                excelExportHelper.setCell(0, productDetail.getSku());
                excelExportHelper.setCell(1, "child");
                excelExportHelper.setCell(2, product.getSku());
                excelExportHelper.setCell(3, productDetail.getSupplierName());
                excelExportHelper.setCell(4, productDetail.getProductName());
                excelExportHelper.setCell(5, productDetail.getProductNo());
                excelExportHelper.setCell(6, productDetail.getColor());
                excelExportHelper.setCell(7, productDetail.getSize());
                excelExportHelper.setCell(8, productDetail.getFeature());
                excelExportHelper.setCell(9, productDetail.getPrice());
                excelExportHelper.setCell(10, productDetail.getWeight());
                excelExportHelper.setCell(11, productDetail.getCategory());
                excelExportHelper.setCell(12, productDetail.getOrigin());
                excelExportHelper.setCell(13, productDetail.getLink());
                excelExportHelper.setCell(14, productDetail.getAlternateLink());
                excelExportHelper.setCell(15, productDetail.getRemark());
            }
        }

        excelExportHelper.export(fullPath);
    }


    public void saveProductList(List<Product> productList) {
        for (Product product : productList) {
            productDao.save(product);
        }
    }

    public void saveProductDetailList(List<ProductDetail> productDetailList) {
        for (ProductDetail productDetail : productDetailList) {
            productDetailDao.save(productDetail);
        }
    }


    public void deleteProduct(Long id) {
        productDao.delete(id);
        productDetailDao.deleteByProduct(id);
    }

    public void deleteProductDetail(Long id) {
        productDetailDao.delete(id);
    }

    /**
     * 获取所有的产品
     */
    public List<Product> getAllOrder() {
        List<Product> productList = (List<Product>) productDao.findAll();
        for (Product product : productList) {
            Hibernates.initLazyProperty(product.getProductDetailList());
        }
        return productList;
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }


    public TableDTO<ProductVo> getProductVo(Long organizationId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                                      String sortType) {
        Page<Product> products = SearchProduct(organizationId, searchParams, pageNumber, pageSize, sortType);
        TableDTO<ProductVo> tableDTO = new TableDTO<>();
        List<ProductVo> productVoList = Lists.newArrayList();
        tableDTO.setTotal(products.getTotalElements());
        for(Product product: products.getContent()){
            ProductVo productVo = new ProductVo();
            productVo.setId(product.getId());
            productVo.setSku(product.getSku());
            productVo.setSupplierName(product.getSupplierName());
            productVo.setProductNo(product.getProductNo());
            productVo.setProductName(product.getProductName());
            productVo.setCategory(product.getCategory());
            productVo.setOrigin(product.getOrigin());
            productVo.setLink(product.getLink());
            productVo.setAlternateLink(product.getAlternateLink());
            productVo.setCreated(product.getCreated());
            productVo.setRemark(product.getRemark());
            productVo.setOrganizationName(product.getOrganization().getName());

            productVoList.add(productVo);
        }
        tableDTO.setRows(productVoList);
        return tableDTO;
    }

    public TableDTO<ProductDetailVo> getProductDetailVo(Long productId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                            String sortType) {
        Page<ProductDetail> productDetails = SearchProductDetail(productId, searchParams, pageNumber, pageSize, sortType);
        TableDTO<ProductDetailVo> tableDTO = new TableDTO<>();
        List<ProductDetailVo> productDetailVoList = Lists.newArrayList();
        tableDTO.setTotal(productDetails.getTotalElements());
        for (ProductDetail productDetail : productDetails.getContent()) {
            ProductDetailVo productDetailVo = new ProductDetailVo();
            productDetailVo.setId(productDetail.getId());
            productDetailVo.setSku(productDetail.getSku());
            productDetailVo.setSupplierName(productDetail.getSupplierName());
            productDetailVo.setProductNo(productDetail.getProductNo());
            productDetailVo.setProductName(productDetail.getProductName());
            productDetailVo.setColor(productDetail.getColor());
            productDetailVo.setSize(productDetail.getSize());
            productDetailVo.setFeature(productDetail.getFeature());
            productDetailVo.setPrice(productDetail.getPrice());
            productDetailVo.setWeight(productDetail.getWeight());
            productDetailVo.setCategory(productDetail.getCategory());
            productDetailVo.setOrigin(productDetail.getOrigin());
            productDetailVo.setLink(productDetail.getLink());
            productDetailVo.setAlternateLink(productDetail.getAlternateLink());
            productDetailVo.setCreated(productDetail.getCreated());
            productDetailVo.setRemark(productDetail.getRemark());
            productDetailVo.setOrganizationName(productDetail.getOrganization().getName());

            productDetailVoList.add(productDetailVo);
        }
        tableDTO.setRows(productDetailVoList);
        return tableDTO;
    }

    /**
     * 按页面传来的查询条件查询产品
     */
    public Page<Product> SearchProduct(Long organizationId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                   String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Product> spec = buildSpecification(organizationId, searchParams);

        return productDao.findAll(spec, pageRequest);
    }

    public Page<ProductDetail> SearchProductDetail(Long productId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                       String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<ProductDetail> spec = buildDetailSpecification(productId, searchParams);

        return productDetailDao.findAll(spec, pageRequest);
    }


    /**
     * 创建动态查询条件组合.
     */
    private Specification<Product> buildSpecification(Long organizationId, Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        //如果organizationId为-1L,则查询所有的产品
        if (organizationId != -1L){
            filters.put("organization.id", new SearchFilter("organization.id", Operator.EQ, organizationId));
        }
        Specification<Product> spec = DynamicSpecifications.bySearchFilter(filters.values(), Product.class);
        return spec;
    }

    private Specification<ProductDetail> buildDetailSpecification(Long productId, Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        if (productId != -1L) {
            filters.put("product.id", new SearchFilter("product.id", Operator.EQ, productId));
        }
        Specification<ProductDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), ProductDetail.class);
        return spec;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType) || "desc".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "id");
        } else if ("asc".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "id");
        }

        return new PageRequest(pageNumber - 1, pageSize, sort);
    }

    public void deleteProductChecked(List<Long> productChecked) {
        for (Long productId : productChecked) {
            productDetailDao.deleteByProduct(productId);
            productDao.delete(productId);
        }
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
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }


    @Autowired
    public void setProductDetailDao(ProductDetailDao productDetailDao) {
        this.productDetailDao = productDetailDao;
    }

    @Autowired
    public void setOrderDetailDao(OrderDetailDao orderDetailDao) {
        this.orderDetailDao = orderDetailDao;
    }
}
