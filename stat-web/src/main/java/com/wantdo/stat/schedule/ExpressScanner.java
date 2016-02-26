package com.wantdo.stat.schedule;

import com.wantdo.stat.entity.shop.Express;
import com.wantdo.stat.entity.shop.Order;
import com.wantdo.stat.service.account.ExpressService;
import com.wantdo.stat.web.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ Date : 15/10/8
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
public class ExpressScanner {

    @Autowired
    private ExpressService expressService;

    public void executeByJdk() {
        execute("jdk timer job");
    }

    public void executeBySpringCronByJava(){
        execute("spring cron job by java");
    }

    //被Spring的quartz MethodInvokingjobDetailFactoryBean反射执行
    public void executeByQuartzLocalJob(){
        execute("quartz local job");
    }

    //被Spring的Scheduler namespace发射构造成ScheduleMethodRunnable
    public void executeBySpringCronByXml(){
        execute("spring cron job by xml");
    }

    //被Spring的Scheduler namespace发射构造成ScheduleMethodRunnable
    public void executeBySpringTimerByXml(){
        execute("spring timer job by xml");
    }

    /**
     * 定时查询快递状态
     */
    private void execute(String by) {
        Logger logger = LoggerFactory.getLogger(ExpressScanner.class.getName() + "." + by);
        List<Order> orderList = expressService.getAllOrderWithinAMonth();

        for (Order order: orderList){
            String url = "http://www.17track.net/r/handlertrack" +
                    ".ashx?callback=express";
            String trackno = order.getTrackno();
            Express express = order.getExpress();
            if (express != null){
                String expressCode = express.getCode();
                url += "&num=" + trackno + "&et=" + expressCode + "&_=" + System.currentTimeMillis();
                String str = HttpClientUtil.get(url);
                logger.info("{}", url);
            }
        }
    }

}
