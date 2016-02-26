package com.wantdo.stat.service.shop.fba;

import com.google.common.collect.Lists;
import com.wantdo.stat.constant.DateFormatConstant;
import com.wantdo.stat.constant.PathConstant;
import com.wantdo.stat.dao.account.OrganizationDao;
import com.wantdo.stat.dao.shop.FBADao;
import com.wantdo.stat.dao.shop.FBADetailDao;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.front.response.ResponseVoResultCode;
import com.wantdo.stat.entity.shop.FBA;
import com.wantdo.stat.entity.shop.FBADetail;
import com.wantdo.stat.excel.helper.ExcelReadHelper;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.Hibernates;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.utils.Clock;
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
import java.util.List;
import java.util.Map;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Component
@Transactional
public class FBAService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(FBAService.class);

    private FBADao fbaDao;

    private FBADetailDao fbaDetailDao;

    private OrganizationDao organizationDao;

    private Clock clock = Clock.DEFAULT;


    /**
     * 按Id获得FBA对象
     */
    public FBA getFBA(Long id) {
        return fbaDao.findOne(id);
    }

    public void saveFBA(FBA fba) {
        fba.setCreated(clock.getCurrentDate());
        fbaDao.save(fba);
    }

    public void updateFBA(FBA fba) {
        fba.setModified(clock.getCurrentDate());
        fbaDao.save(fba);
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

            String relativeFold = "logistics" + File.separator + "fba" + File.separator + now + File.separator + getCurrentUserId() + File.separator;
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
            orderExcelRead(fullPath);

        } catch (Exception e) {

        } finally {

        }

        return responseVo;

    }

    private void orderExcelRead(String file) {
        List<FBADetail> fbaDetailList = Lists.newArrayList();
        try {
            ArrayList<ArrayList<Object>> rowList = ExcelReadHelper.readRows(file, 1, ExcelReadHelper.getRowNum(file));
            for (ArrayList<Object> row : rowList) {

                String amazonId = String.valueOf(Double.valueOf(String.valueOf(row.get(0))).longValue());
                String shipId = String.valueOf(String.valueOf(row.get(1)));
                Double weight = Double.valueOf(String.valueOf(row.get(2)));
                String shipMethod = String.valueOf(row.get(3));
                String shipAddress = String.valueOf(row.get(4));
                String organizationName = String.valueOf(row.get(5));
                Organization organization = organizationDao.findByName(organizationName);

                FBA fbaExist = fbaDao.findByAmazonId(amazonId);
                if (fbaExist == null){
                    FBA fba = new FBA();
                    FBADetail fbaDetail = new FBADetail();
                    fba.setAmazonId(amazonId);
                    fba.setShipId(shipId);
                    fba.setShipMethod(shipMethod);
                    fba.setShipAddress(shipAddress);
                    fba.setOrganization(organization);
                    fbaDao.save(fba);

                    fbaDetail.setAmazonId(amazonId);
                    fbaDetail.setShipId(shipId);
                    fbaDetail.setWeight(weight);
                    fbaDetail.setShipMethod(shipMethod);
                    fbaDetail.setShipAddress(shipAddress);
                    fbaDetail.setOrganization(organization);
                    fbaDetail.setFba(fba);

                    fbaDetailDao.save(fbaDetail);
                } else {
                    fbaExist.setAmazonId(amazonId);
                    fbaExist.setShipMethod(shipMethod);
                    fbaExist.setShipAddress(shipAddress);
                    fbaExist.setOrganization(organization);
                    fbaDao.save(fbaExist);

                    FBADetail fbaDetailExist = fbaDetailDao.findByAmazonIdAndShipId(amazonId, shipId);
                    if (fbaDetailExist == null){
                        FBADetail fbaDetail = new FBADetail();
                        fbaDetail.setAmazonId(amazonId);
                        fbaDetail.setShipId(shipId);
                        fbaDetail.setWeight(weight);
                        fbaDetail.setShipMethod(shipMethod);
                        fbaDetail.setShipAddress(shipAddress);
                        fbaDetail.setOrganization(organization);
                        fbaDetail.setFba(fbaExist);
                        fbaDetailDao.save(fbaDetail);
                    }else{
                        fbaDetailExist.setAmazonId(amazonId);
                        fbaDetailExist.setShipId(shipId);
                        fbaDetailExist.setShipMethod(shipMethod);
                        fbaDetailExist.setWeight(weight);
                        fbaDetailExist.setShipAddress(shipAddress);
                        fbaDetailExist.setOrganization(organization);
                        fbaDetailExist.setFba(fbaExist);
                        fbaDetailDao.save(fbaDetailExist);
                    }

                }

            }
        } catch (Exception e) {

        }
    }

    public void saveFBAList(List<FBA> fbaList) {
        for (FBA fba : fbaList) {
            fbaDao.save(fba);
        }
    }


    public void deleteFBA(Long id) {
        fbaDao.delete(id);
    }

    /**
     * 获取所有的FBA对象
     */
    public List<FBA> getAllFBA() {
        List<FBA> fbaList = (List<FBA>) fbaDao.findAll();
        for(FBA fba: fbaList){
            Hibernates.initLazyProperty(fba.getFbaDetailList());
        }
        return fbaList;
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }

    /**
     * 按页面传来的查询条件查询FBA对象
     */
    public Page<FBA> SearchOrder(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                   String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<FBA> spec = buildSpecification(searchParams);

        return fbaDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<FBA> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FBA> spec = DynamicSpecifications.bySearchFilter(filters.values(), FBA.class);
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
    public void setFbaDao(FBADao fbaDao) {
        this.fbaDao = fbaDao;
    }

    @Autowired
    public void setOrganizationDao(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }

    @Autowired
    public void setFbaDetailDao(FBADetailDao fbaDetailDao) {
        this.fbaDetailDao = fbaDetailDao;
    }
}