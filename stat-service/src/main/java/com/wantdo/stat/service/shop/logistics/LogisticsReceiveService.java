package com.wantdo.stat.service.shop.logistics;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wantdo.stat.dao.shop.*;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.shop.AlibabaOrder;
import com.wantdo.stat.entity.shop.PurchaseDetail;
import com.wantdo.stat.entity.shop.ReceiveStatus;
import com.wantdo.stat.entity.front.vo.LogisticsReceiveVo;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.Hibernates;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.utils.Clock;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * 日常打单
 *
 * @ Date : 2015-9-17
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class LogisticsReceiveService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(LogisticsReceiveService.class);

    private Clock clock = Clock.DEFAULT;

    private AlibabaOrderDao alibabaOrderDao;

    private AlibabaOrderDetailDao alibabaOrderDetailDao;

    private PurchaseDao purchaseDao;

    private PurchaseDetailDao purchaseDetailDao;

    private ReceiveStatusDao receiveStatusDao;

    public Map<Long, String> getAllReceiveStatus(){
        List<ReceiveStatus> receiveStatusList = (List<ReceiveStatus>)receiveStatusDao.findAll();
        Map<Long, String> allStatus = Maps.newLinkedHashMap();
        for(ReceiveStatus receiveStatus: receiveStatusList){
            allStatus.put(receiveStatus.getId(), receiveStatus.getDescription());
        }
        return allStatus;
    }

    /**
     * 按Id获得alibaba订单
     */
    public AlibabaOrder getAlibabaOrder(Long id) {
        return alibabaOrderDao.findOne(id);
    }

    public PurchaseDetail getAllOrder(Long id){
        return purchaseDetailDao.findOne(id);
    }

    public ReceiveStatus getReceiveStatus(Long id){
        return receiveStatusDao.findOne(id);
    }


    public AlibabaOrder getAlibabaOrderByPlatformOrderId(String platformOrderId) {
        return alibabaOrderDao.findByPlatformOrderId(platformOrderId);
    }

    public AlibabaOrder getAlibabaOrderByTrackno(String trackno) {
        return alibabaOrderDao.findByTrackno(trackno);
    }

    public PurchaseDetail getPurchaseDetail(Long id){
        return purchaseDetailDao.findOne(id);
    }

    public void saveAlibabaOrder(AlibabaOrder alibabaOrder) {
        alibabaOrder.setCreated(clock.getCurrentDate());
        alibabaOrderDao.save(alibabaOrder);
    }

    public void updateAlibabaOrder(AlibabaOrder alibabaOrder) {
        alibabaOrder.setModified(clock.getCurrentDate());
        alibabaOrderDao.save(alibabaOrder);
    }

    public void savePurchaseDetail(PurchaseDetail purchaseDetail) {
        purchaseDetail.setCreated(clock.getCurrentDate());
        purchaseDetailDao.save(purchaseDetail);
    }

    public void updatePurchaseDetail(PurchaseDetail purchaseDetail) {
        purchaseDetail.setModified(clock.getCurrentDate());
        purchaseDetailDao.save(purchaseDetail);
    }

    public void deleteAlibabaOrder(Long id) {
        alibabaOrderDao.delete(id);
    }

    public void deletePurchaseDetail(Long id) {
        purchaseDetailDao.delete(id);
    }


    /**
     * 获取所有的订单
     */
    public List<AlibabaOrder> getAllAlibabaOrder() {
        List<AlibabaOrder> alibabaOrderList = (List<AlibabaOrder>) alibabaOrderDao.findAll();
        for (AlibabaOrder alibabaOrder : alibabaOrderList) {
            Hibernates.initLazyProperty(alibabaOrder.getAlibabaOrderDetailList());
        }
        return alibabaOrderList;
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }

    public TableDTO<LogisticsReceiveVo> getLogisticsReceiveVo(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                                              String sortType){
        Page<PurchaseDetail> purchaseDetails = SearchPurchaseDetail(searchParams, pageNumber, pageSize, sortType);
        TableDTO<LogisticsReceiveVo> tableDTO = new TableDTO<>();
        List<LogisticsReceiveVo> logisticsReceiveVoList = Lists.newArrayList();
        tableDTO.setTotal(purchaseDetails.getTotalElements());
        for (PurchaseDetail purchaseDetail : purchaseDetails.getContent()) {
            LogisticsReceiveVo logisticsReceiveVo = new LogisticsReceiveVo();
            logisticsReceiveVo.setId(purchaseDetail.getId());
            logisticsReceiveVo.setPurchaseDate(purchaseDetail.getPurchaseDate());
            logisticsReceiveVo.setPurchaseOrderId(purchaseDetail.getPurchaseOrderId());
            logisticsReceiveVo.setPlatformOrderId(purchaseDetail.getPlatformOrderId());
            logisticsReceiveVo.setSku(purchaseDetail.getSku());
            logisticsReceiveVo.setProductName(purchaseDetail.getProductName());
            logisticsReceiveVo.setProductFeature(purchaseDetail.getProductFeature());
            logisticsReceiveVo.setQuantity(purchaseDetail.getQuantity());
            logisticsReceiveVo.setPrice(purchaseDetail.getPrice());
            logisticsReceiveVo.setProductLink(purchaseDetail.getProductLink());
            logisticsReceiveVo.setSupplierName(purchaseDetail.getSupplierName());
            logisticsReceiveVo.setWwName(purchaseDetail.getWwName());
            logisticsReceiveVo.setTrackno(purchaseDetail.getPurchase().getTrackno());
            if (purchaseDetail.getReceiveStatus() != null){
                logisticsReceiveVo.setReceiveStatus(purchaseDetail.getReceiveStatus().getDescription());
            } else {
                logisticsReceiveVo.setReceiveStatus("");
            }

            logisticsReceiveVo.setRemark(purchaseDetail.getRemark());
            logisticsReceiveVoList.add(logisticsReceiveVo);
        }
        tableDTO.setRows(logisticsReceiveVoList);
        return tableDTO;
    }

    /**
     * 按页面传来的查询条件查询订单
     */
    public Page<AlibabaOrder> SearchAlibabaOrder( Map<String, Object> searchParams, int pageNumber, int pageSize,
                                       String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<AlibabaOrder> spec = buildSpecification(searchParams);

        return alibabaOrderDao.findAll(spec, pageRequest);
    }

    public Page<PurchaseDetail> SearchPurchaseDetail(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                               String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<PurchaseDetail> spec = buildOrderSpecification(searchParams);

        return purchaseDetailDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<AlibabaOrder> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<AlibabaOrder> spec = DynamicSpecifications.bySearchFilter(filters.values(), AlibabaOrder.class);
        return spec;
    }

    private Specification<PurchaseDetail> buildOrderSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<PurchaseDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), PurchaseDetail.class);
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

    public void confirmPurchase(List<LogisticsReceiveVo> logisticsReceiveVoList){
        if (logisticsReceiveVoList != null && logisticsReceiveVoList.size() >0){
            for(LogisticsReceiveVo logisticsReceiveVo: logisticsReceiveVoList){
                PurchaseDetail purchaseDetail = purchaseDetailDao.findByPlatformOrderIdAndPurchaseOrderIdAndOrderItemId(logisticsReceiveVo.getPlatformOrderId(),logisticsReceiveVo.getPurchaseOrderId(), logisticsReceiveVo.getOrderItemId());
                if (purchaseDetail != null){
                    purchaseDetail.setReceiveStatus(new ReceiveStatus(2L));
                    purchaseDetailDao.save(purchaseDetail);
                }
            }
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
    public void setAlibabaOrderDao(AlibabaOrderDao alibabaOrderDao) {
        this.alibabaOrderDao = alibabaOrderDao;
    }

    @Autowired
    public void setAlibabaOrderDetailDao(AlibabaOrderDetailDao alibabaOrderDetailDao) {
        this.alibabaOrderDetailDao = alibabaOrderDetailDao;
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
    public void setReceiveStatusDao(ReceiveStatusDao receiveStatusDao) {
        this.receiveStatusDao = receiveStatusDao;
    }
}
