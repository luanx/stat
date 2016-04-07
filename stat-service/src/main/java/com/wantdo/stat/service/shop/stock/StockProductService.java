package com.wantdo.stat.service.shop.stock;

import com.google.common.collect.Lists;
import com.wantdo.stat.constant.DateFormatConstant;
import com.wantdo.stat.constant.PathConstant;
import com.wantdo.stat.dao.shop.PlatformDao;
import com.wantdo.stat.dao.shop.StockProductDao;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.front.response.ResponseVoResultCode;
import com.wantdo.stat.entity.front.vo.StockProductVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.shop.Platform;
import com.wantdo.stat.entity.shop.StockProduct;
import com.wantdo.stat.excel.helper.ExcelReadHelper;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.service.account.ServiceException;
import com.wantdo.stat.utils.Clock;
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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ Date : 16/4/5
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class StockProductService {

    private static Logger logger = LoggerFactory.getLogger(StockProductService.class);

    private Clock clock = Clock.DEFAULT;


    @Autowired
    private StockProductDao stockProductDao;

    @Autowired
    private PlatformDao platformDao;

    public List<StockProduct> findAll(){
        return (List<StockProduct>) stockProductDao.findAll();
    }

    /**
     * 按Id获得产品
     */
    public StockProduct getStockProduct(Long id) {
        return stockProductDao.findOne(id);
    }

    public void updateStockProduct(StockProduct stockProduct) {
        stockProduct.setModified(clock.getCurrentDate());
        stockProductDao.save(stockProduct);
    }

    public TableDTO<StockProductVo> getsStockProductVo(Long platformId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                                   String sortType, String orderType) {
        Page<StockProduct> stockProducts = SearchProduct(platformId, searchParams, pageNumber, pageSize, sortType, orderType);
        TableDTO<StockProductVo> tableDTO = new TableDTO<>();
        List<StockProductVo> stockProductVoList = Lists.newArrayList();
        tableDTO.setTotal(stockProducts.getTotalElements());
        for (StockProduct sp : stockProducts.getContent()) {
            StockProductVo stockProductVo = new StockProductVo();
            stockProductVo.setId(sp.getId());
            stockProductVo.setSku(sp.getSku());
            stockProductVo.setPtype(sp.getPtype());
            stockProductVo.setPlatform(sp.getPlatform().getName());
            stockProductVo.setName(sp.getName());
            stockProductVo.setSeries(sp.getSeries());
            stockProductVo.setCategory(sp.getCategory());
            stockProductVo.setPrice(sp.getPrice());
            stockProductVoList.add(stockProductVo);
        }
        tableDTO.setRows(stockProductVoList);
        return tableDTO;
    }

    public Page<StockProduct> SearchProduct(Long platformId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                              String sortType, String orderType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<StockProduct> spec = buildSpecification(platformId, searchParams, orderType);

        return stockProductDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<StockProduct> buildSpecification(Long platformId, Map<String, Object> searchParams, String orderType) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("platform.id", new SearchFilter("platform.id", SearchFilter.Operator.EQ, platformId));
        Specification<StockProduct> spec = DynamicSpecifications.bySearchFilter(filters.values(), StockProduct.class);
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

            String relativeFold = "stock_product" + File.separator + "upload" + File.separator;
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
            stockProductExcelRead(fullPath);

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

    private void stockProductExcelRead(String file) throws Exception {

        ArrayList<ArrayList<Object>> rowList = null;
        try {
            rowList = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException("读取Excel错误,请检查后重试或联系管理员");
        }
        for (ArrayList<Object> row : rowList) {

            String row0 = ExcelReadHelper.getCell(row.get(0));
            String sku = "";
            if (row0 != null) {
                sku = row0;
            }

            String row1 = ExcelReadHelper.getCell(row.get(1));
            String ptype = "";
            if (row1 != null) {
                ptype = row1;
            }

            String row2 = ExcelReadHelper.getCell(row.get(2));
            Platform platform = platformDao.findByName(row2);
            if (platform == null) {
                throw new ServiceException("未找到产品" + sku + "对应的平台");
            }

            String row3 = ExcelReadHelper.getCell(row.get(3));
            String name = "";
            if (row3 != null) {
                name = row3;
            }

            String row4 = ExcelReadHelper.getCell(row.get(4));
            String series = "";
            if (row4 != null) {
                series = row4;
            }

            String row5 = ExcelReadHelper.getCell(row.get(5));
            String category = "";
            if (row5 != null) {
                category = row5;
            }

            String row6 = ExcelReadHelper.getCell(row.get(6));
            String price = "";
            if (row6 != null) {
                price = row6;
            }

            StockProduct stockProductExist = stockProductDao.findBySku(sku);
            if (stockProductExist == null) {
                StockProduct stockProduct = new StockProduct();

                stockProduct.setSku(sku);
                stockProduct.setPtype(ptype);
                stockProduct.setPlatform(platform);
                stockProduct.setName(name);
                stockProduct.setSeries(series);
                stockProduct.setCategory(category);
                stockProduct.setPrice(price);
                stockProductDao.save(stockProduct);

            } else {

                stockProductExist.setSku(sku);
                stockProductExist.setPtype(ptype);
                stockProductExist.setPlatform(platform);
                stockProductExist.setName(name);
                stockProductExist.setSeries(series);
                stockProductExist.setCategory(category);
                stockProductExist.setPrice(price);
                stockProductDao.save(stockProductExist);
            }

        }
    }
}
