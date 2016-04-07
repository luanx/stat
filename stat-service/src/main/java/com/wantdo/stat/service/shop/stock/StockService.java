package com.wantdo.stat.service.shop.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wantdo.stat.constant.DateFormatConstant;
import com.wantdo.stat.constant.PathConstant;
import com.wantdo.stat.dao.shop.PlatformDao;
import com.wantdo.stat.dao.shop.StockOrderDao;
import com.wantdo.stat.dao.shop.StockOrderItemDao;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.front.response.ResponseVoResultCode;
import com.wantdo.stat.entity.front.vo.PDFVo;
import com.wantdo.stat.entity.front.vo.StockOrderVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.shop.Platform;
import com.wantdo.stat.entity.shop.StockOrder;
import com.wantdo.stat.entity.shop.StockOrderItem;
import com.wantdo.stat.excel.helper.ExcelReadHelper;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.service.account.ServiceException;
import com.wantdo.stat.template.HtmlHelper;
import com.wantdo.stat.template.PDFHelper;
import com.wantdo.stat.utils.DateUtil;
import com.wantdo.stat.utils.NumUtil;
import com.wantdo.stat.utils.Path;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ Date : 16/4/5
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class StockService {

    private static Logger logger = LoggerFactory.getLogger(StockService.class);

    private static final String DEFAULT_ENCODING = "utf-8";

    private static Map<Long, String> orderStatus = Maps.newLinkedHashMap();

    static {
        orderStatus.put(0L, "未出库");
        orderStatus.put(1L, "已出库");
    }

    @Autowired
    private StockOrderDao stockOrderDao;

    @Autowired
    private StockOrderItemDao stockOrderItemDao;

    @Autowired
    private PlatformDao platformDao;

    public StockOrder findLastRecord(){
        return stockOrderDao.findFirstByOrderByIdDesc();
    }

    public List<StockOrder> findAllStockOrders(){
        return (List<StockOrder>) stockOrderDao.findAll();
    }


    public TableDTO<StockOrderVo> getsStockOrderVo(Long platformId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                        String sortType, String orderType) {
        Page<StockOrder> stockOrders = SearchOrderDetail(platformId, searchParams, pageNumber, pageSize, sortType, orderType);
        TableDTO<StockOrderVo> tableDTO = new TableDTO<>();
        List<StockOrderVo> stockOrderVoList = Lists.newArrayList();
        tableDTO.setTotal(stockOrders.getTotalElements());
        for(StockOrder so: stockOrders.getContent()){
            StockOrderVo stockOrderVo = new StockOrderVo();
            stockOrderVo.setId(so.getId());
            stockOrderVo.setOrderId(so.getOrderId());
            stockOrderVo.setStockId(so.getStockId());
            stockOrderVo.setOutStock(so.getOutStock());
            stockOrderVo.setPlatform(so.getPlatform().getName());
            stockOrderVo.setWarehouse(so.getWarehouse());
            stockOrderVo.setStatus(orderStatus.get(so.getStatus()));
            stockOrderVo.setCreated(so.getCreated());
            stockOrderVo.setModified(so.getModified());
            stockOrderVoList.add(stockOrderVo);
        }
        tableDTO.setRows(stockOrderVoList);
        return tableDTO;
    }

    public Page<StockOrder> SearchOrderDetail(Long platformId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                               String sortType, String orderType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<StockOrder> spec = buildSpecification(platformId, searchParams, orderType);

        return stockOrderDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<StockOrder> buildSpecification(Long platformId, Map<String, Object> searchParams, String orderType) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("platform.id", new SearchFilter("platform.id", SearchFilter.Operator.EQ, platformId));
        Specification<StockOrder> spec = DynamicSpecifications.bySearchFilter(filters.values(), StockOrder.class);
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

            String relativeFold = "outstock" + File.separator + "upload" + File.separator;
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
            stockOrderExcelRead(fullPath);

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

    private void stockOrderExcelRead(String file) throws Exception {

        ArrayList<ArrayList<Object>> rowList = null;
        try {
            rowList = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException("读取Excel错误,请检查后重试或联系管理员");
        }
        for (ArrayList<Object> row : rowList) {

            String row0 = ExcelReadHelper.getCell(row.get(0));
            String orderId = "";
            if (row0 != null) {
                orderId = row0;
            }

            String row1 = ExcelReadHelper.getCell(row.get(1));
            Date outStock = null;
            if (row1 != null) {
                outStock = randomDate(row1);
            }

            String row2 = ExcelReadHelper.getCell(row.get(2));
            String sku = "";
            if (row2 != null) {
                sku = row2;
            }

            String row3 = ExcelReadHelper.getCell(row.get(3));
            String series = "";
            if (row3 != null) {
                series = row3;
            }

            String row4 = ExcelReadHelper.getCell(row.get(4));
            String category = "";
            if (row4 != null) {
                category = row4;
            }

            String row5 = ExcelReadHelper.getCell(row.get(5));
            Long num = 0L;
            if (row5 != null) {
                num = Long.valueOf(row5);
            }

            String row6 = ExcelReadHelper.getCell(row.get(6));
            String amount = "";
            if (row6 != null) {
                amount = row6;
            }

            String row7 = ExcelReadHelper.getCell(row.get(7));
            Platform platform = platformDao.findByName(row7);
            if (platform == null){
                throw new ServiceException("未找到订单" + orderId +"对应的平台");
            }

            String row8 = ExcelReadHelper.getCell(row.get(8));
            String warehouse = "";
            if (row8 != null) {
                warehouse = row8;
            }

            StockOrder stockOrderExist = stockOrderDao.findByOrderId(orderId);
            if (stockOrderExist == null) {
                StockOrder stockOrder = new StockOrder();
                StockOrderItem stockOrderItem = new StockOrderItem();

                StockOrder lastRecord = findLastRecord();
                String stockId = "";
                if (lastRecord == null) {
                    stockId = NumUtil.addOne(NumUtil.STR_FORMAT);
                } else {
                    stockId = NumUtil.addOne(lastRecord.getStockId());
                }

                stockOrder.setStockId(stockId);
                stockOrder.setOrderId(orderId);
                stockOrder.setOutStock(outStock);
                stockOrder.setPlatform(platform);
                stockOrder.setWarehouse(warehouse);
                stockOrder.setStatus(0L);
                stockOrderDao.save(stockOrder);

                stockOrderItem.setStockOrder(stockOrder);
                stockOrderItem.setSku(sku);
                stockOrderItem.setSeries(series);
                stockOrderItem.setCategory(category);
                stockOrderItem.setNum(num);
                stockOrderItem.setAmount(amount);
                stockOrderItemDao.save(stockOrderItem);

            } else {

                stockOrderExist.setOrderId(orderId);
                stockOrderExist.setOutStock(outStock);
                stockOrderExist.setPlatform(platform);
                stockOrderExist.setWarehouse(warehouse);
                stockOrderDao.save(stockOrderExist);

                StockOrderItem stockOrderItemExist = stockOrderItemDao.findByStockOrderIdAndSku(stockOrderExist.getId(), sku);
                if (stockOrderItemExist == null) {
                    StockOrderItem stockOrderItem = new StockOrderItem();
                    stockOrderItem.setStockOrder(stockOrderExist);
                    stockOrderItem.setSku(sku);
                    stockOrderItem.setSeries(series);
                    stockOrderItem.setCategory(category);
                    stockOrderItem.setNum(num);
                    stockOrderItem.setAmount(amount);
                    stockOrderItemDao.save(stockOrderItem);
                } else {
                    stockOrderItemExist.setStockOrder(stockOrderExist);
                    stockOrderItemExist.setSku(sku);
                    stockOrderItemExist.setSeries(series);
                    stockOrderItemExist.setCategory(category);
                    stockOrderItemExist.setNum(num);
                    stockOrderItemExist.setAmount(amount);
                    stockOrderItemDao.save(stockOrderItemExist);
                }

            }

        }
    }

    public PDFVo downloadPDF(){
        PDFVo pdfVo = new PDFVo();
        String now = new DateTime().toString(DateFormatConstant.FORMAT_yyyyMMdd);
        String relativeFold = "outstock" + File.separator + "download" +  File.separator;
        String fullRelativeFold = PathConstant.FILE_STORE_PATH + relativeFold;
        String fileName = System.currentTimeMillis() + ".pdf";
        String relativePath = relativeFold + fileName;
        String fullPath = PathConstant.FILE_STORE_PATH + relativePath;

        File fullFoldFile = new File(fullRelativeFold);
        if (!fullFoldFile.exists()) {
            fullFoldFile.mkdirs();
        }

        this.generatePDF(fullPath);
        pdfVo.setName(fileName);
        pdfVo.setPath(fullPath);
        return pdfVo;
    }

    private PDFVo generatePDF(String fullPath){
        PDFVo pdfVo = new PDFVo();
        try {
            Map<String, Object> variables = new HashMap<>();

            List<StockOrder> stockOrderList = stockOrderDao.findAllUnStocked();

            variables.put("stockOrderList", stockOrderList);

            String htmlStr = HtmlHelper.generate("template.ftl", variables);

            OutputStream out = new FileOutputStream(fullPath);
            PDFHelper.generate(htmlStr, out);
            for(StockOrder stockOrder : stockOrderList){
                stockOrder.setStatus(1L);
                stockOrderDao.save(stockOrder);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return pdfVo;

    }

    private Date randomDate(String dateStr) throws Exception{
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormat.format(new Date(dateStr));
            String start = date + " 08:30:00";
            String end = date + " 18:00:00";
            return DateUtil.randomDate(start, end);
        } catch (Exception e) {
            throw new ServiceException("日期转换错误");
        }
    }

}
