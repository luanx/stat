package com.wantdo.stat.web.shop.stock;

import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.front.vo.PDFVo;
import com.wantdo.stat.entity.shop.Platform;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.stock.PlatformService;
import com.wantdo.stat.service.shop.stock.StockService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 日常订单
 *
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Controller
@RequestMapping(value = "/stock/outstock")
public class OutStockController {

    @Autowired
    private UserService userService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private StockService stockService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Platform> platforms = platformService.getAllPlatform();
        model.addAttribute("platforms", platforms);
        return "shop/stock/outstockOrderList";
    }

    /**
     * 上传excel文件
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVo upload(MultipartFile uploadExcel) {
        return stockService.upload(uploadExcel);
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public
    @ResponseBody
    void outstock(HttpServletResponse response) {
        ServletOutputStream sos = null;
        BufferedInputStream bis = null;

        PDFVo pdfVo = stockService.downloadPDF();

        String path = pdfVo.getPath();

        try {
            File file = new File(path);
            response.addHeader("content-type", "application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=\"" + "stock" + ".pdf" + "\"");

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
