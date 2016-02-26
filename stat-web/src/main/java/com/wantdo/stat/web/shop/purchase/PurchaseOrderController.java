package com.wantdo.stat.web.shop.purchase;

import com.google.common.collect.Maps;
import com.wantdo.stat.entity.front.vo.ExcelVo;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.shop.purchase.PurchaseService;
import com.wantdo.stat.utils.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 订单管理
 *
 * @ Date : 2015-9-17
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
@Controller
@RequestMapping(value = "/purchase/order")
public class PurchaseOrderController {

    private static Map<String, String> orderTypes = Maps.newLinkedHashMap();

    @Autowired
    private PurchaseService purchaseService;

    static {
        orderTypes.put("all", "所有订单");
        orderTypes.put("today", "今日订单");
        orderTypes.put("exception", "异常订单");
    }


    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("orderTypes", orderTypes);
        return "shop/purchase/purchaseOrderList";
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public
    @ResponseBody
    void download(HttpServletResponse response) {

        ServletOutputStream sos = null;
        BufferedInputStream bis = null;

        ExcelVo excelVo = purchaseService.download();

        String path = excelVo.getPath();

        try {
            File file = new File(path);
            response.addHeader("content-type", "application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=\"" + DateUtil.getStringDate() + ".xls" + "\"");


            sos = response.getOutputStream();

            bis = new BufferedInputStream(new FileInputStream(file));
            final int BUFF_SIZE = 1024 * 4;
            byte[] buff = new byte[BUFF_SIZE];
            int len = 0;
            while ((len = bis.read(buff)) != -1) {
                sos.write(buff, 0, len);
            }
            sos.flush();
            sos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @RequestMapping(value = "download_exception", method = RequestMethod.GET)
    public
    @ResponseBody
    void downloadExceptoin(HttpServletResponse response) {

        ServletOutputStream sos = null;
        BufferedInputStream bis = null;

        ExcelVo excelVo = purchaseService.downloadException();

        String path = excelVo.getPath();

        try {
            File file = new File(path);
            response.addHeader("content-type", "application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=\"" + DateUtil.getStringDate() + ".exception" + ".xls" + "\"");

            sos = response.getOutputStream();


            bis = new BufferedInputStream(new FileInputStream(file));
            final int BUFF_SIZE = 1024 * 4;
            byte[] buff = new byte[BUFF_SIZE];
            int len = 0;
            while ((len = bis.read(buff)) != -1) {
                sos.write(buff, 0, len);
            }
            sos.flush();
            sos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

}
