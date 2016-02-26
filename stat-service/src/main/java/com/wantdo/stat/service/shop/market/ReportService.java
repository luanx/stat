package com.wantdo.stat.service.shop.market;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.LineData;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wantdo.stat.dao.shop.AreaCountDao;
import com.wantdo.stat.dao.shop.OrderAreaDao;
import com.wantdo.stat.dao.shop.OrderDao;
import com.wantdo.stat.entity.shop.AreaCount;
import com.wantdo.stat.entity.shop.Order;
import com.wantdo.stat.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Date : 16/2/23
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class ReportService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderAreaDao orderAreaDao;

    @Autowired
    private AreaCountDao areaCountDao;

    private static final int[] days = new int[]{7, 14, 30, 90, 180, 365};

    public Option getTotalOption(String start, String end, Long organizationId) {
        Option option = new Option();
        DateTime startDate = new DateTime(start);
        DateTime endDate = new DateTime(end);

        option.title("订单销售汇总情况");
        option.tooltip().trigger(Trigger.axis);
        option.legend("订单数量");
        option.toolbox().show(true).feature(Tool.mark,
                Tool.dataView,
                new MagicType(Magic.line, Magic.bar, Magic.stack, Magic.tiled),
                Tool.restore,
                Tool.saveAsImage).padding(20);
        option.calculable(true);

        List<String> dates = Lists.newArrayList();
        List<Long> nums = Lists.newArrayList();
        String format = "yyyy-MM-dd";
        int days = Days.daysBetween(startDate, endDate).getDays();
        for(int i=0; i <= days; i++){
            DateTime date = startDate.plusDays(i);
            dates.add(date.toString(format));

            DateTime startOfDate = date.withTimeAtStartOfDay();
            DateTime endOfDate = date.millisOfDay().withMaximumValue();
            Long num = orderDao.countByDate(startOfDate.getMillis(), endOfDate.getMillis(), organizationId);
            nums.add(num);
        }

        option.xAxis(new CategoryAxis().boundaryGap(false).data(dates.toArray(new String[dates.size()])));
        option.yAxis(new ValueAxis());

        Line l1 = new Line("订单数量");
        l1.smooth(true).itemStyle().normal().areaStyle().typeDefault();
        l1.data(nums.toArray(new Long[nums.size()]));
        l1.markLine().data(new LineData().type(MarkType.average).name("平均值"));
        option.series(l1);

        return option;
    }

    public Option getAreaOption(String start, String end, Long organizationId) {
        Option option = new Option();
        Date startDate = new DateTime(start).withTimeAtStartOfDay().toDate();
        Date endDate = new DateTime(end).millisOfDay().withMaximumValue().toDate();

        option.title("购买区域汇总情况");
        option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
        option.legend("购买区域");
        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.calculable(true);

        String format = "yyyy-MM-dd";

        List<AreaCount> areaCounts = areaCountDao.findByDateAndOrganizationId(startDate, endDate, organizationId);
        List<String> areas = Lists.newArrayList();
        List<Long> nums = Lists.newArrayList();
        for(AreaCount areaCount: areaCounts){
            String stateCn = areaCount.getStateCn();
            if (StringUtils.isEmpty(stateCn)){
                stateCn = "其他";
            }
            areas.add(stateCn);
            Long num = areaCount.getNum();
            nums.add(num);
        }

        option.xAxis(new ValueAxis());
        option.yAxis(new CategoryAxis().boundaryGap(false).data(areas.toArray(new String[areas.size()])));

        Bar bar = new Bar("购买区域");
        bar.stack("订单数");
        bar.itemStyle().normal().label().show(true).position("insideRight");
        bar.data(nums.toArray(new Long[nums.size()]));

        option.series(bar);
        return option;
    }

    public Option getPurchaseOption(String start, String end, Long organizationId) {
        Option option = new Option();
        Date startDate = new DateTime(start).withTimeAtStartOfDay().toDate();
        Date endDate = new DateTime(end).millisOfDay().withMaximumValue().toDate();

        option.title("下单时间汇总情况(美国时间)");
        option.tooltip().trigger(Trigger.axis);
        option.legend("订单数量");
        option.toolbox().show(true).feature(Tool.mark,
                Tool.dataView,
                new MagicType(Magic.line, Magic.bar, Magic.stack, Magic.tiled),
                Tool.restore,
                Tool.saveAsImage).padding(20);
        option.calculable(true);

        List<Order> orderList = orderDao.findAllByPurchaseDateAndOrganization(startDate.getTime(), endDate.getTime(), organizationId);

        Map<String, Long> purchaseMap = Maps.newLinkedHashMap();
        for(int i=0 ; i<24; i++){
            String str = String.valueOf(i) + ":00";
            purchaseMap.put(str, 0L);
        }

        for(Order order: orderList){
            DateTime purDate = DateUtil.integralPointOfDate(order.getPurDate());
            String time = purDate.getHourOfDay() + ":00";
            if (purchaseMap.containsKey(time)){
                Long value = purchaseMap.get(time);
                value ++;
                purchaseMap.put(time, value);
            } else {
                purchaseMap.put(time, 1L);
            }
        }

        List<String> times = Lists.newArrayList();
        List<Long> nums = Lists.newArrayList();
        for (Map.Entry<String, Long> entry : purchaseMap.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            times.add(key);
            nums.add(value);
        }

        option.xAxis(new CategoryAxis().boundaryGap(false).data(times.toArray(new String[times.size()])));
        option.yAxis(new ValueAxis());

        Line l1 = new Line("订单数量");
        l1.smooth(true).itemStyle().normal().areaStyle().typeDefault();
        l1.data(nums.toArray(new Long[nums.size()]));
        l1.markLine().data(new LineData().type(MarkType.average).name("平均值"));
        option.series(l1);

        return option;
    }

    public Option getPaymentsOption(String start, String end, Long organizationId) {
        Option option = new Option();
        Date startDate = new DateTime(start).withTimeAtStartOfDay().toDate();
        Date endDate = new DateTime(end).millisOfDay().withMaximumValue().toDate();

        option.title("购买时间汇总情况(美国时间)");
        option.tooltip().trigger(Trigger.axis);
        option.legend("订单数量");
        option.toolbox().show(true).feature(Tool.mark,
                Tool.dataView,
                new MagicType(Magic.line, Magic.bar, Magic.stack, Magic.tiled),
                Tool.restore,
                Tool.saveAsImage).padding(20);
        option.calculable(true);

        List<Order> orderList = orderDao.findAllByPaymentsAndOrganization(startDate.getTime(), endDate.getTime(), organizationId);

        Map<String, Long> purchaseMap = Maps.newLinkedHashMap();
        for (int i = 0; i < 24; i++) {
            String str = String.valueOf(i) + ":00";
            purchaseMap.put(str, 0L);
        }

        for (Order order : orderList) {
            DateTime purDate = DateUtil.integralPointOfDate(order.getPurDate());
            String time = purDate.getHourOfDay() + ":00";
            if (purchaseMap.containsKey(time)) {
                Long value = purchaseMap.get(time);
                value++;
                purchaseMap.put(time, value);
            } else {
                purchaseMap.put(time, 1L);
            }
        }

        List<String> times = Lists.newArrayList();
        List<Long> nums = Lists.newArrayList();
        for (Map.Entry<String, Long> entry : purchaseMap.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            times.add(key);
            nums.add(value);
        }

        option.xAxis(new CategoryAxis().boundaryGap(false).data(times.toArray(new String[times.size()])));
        option.yAxis(new ValueAxis());

        Line l1 = new Line("订单数量");
        l1.smooth(true).itemStyle().normal().areaStyle().typeDefault();
        l1.data(nums.toArray(new Long[nums.size()]));
        l1.markLine().data(new LineData().type(MarkType.average).name("平均值"));
        option.series(l1);

        return option;
    }

    public Option getShipServiceLevelOption(String start, String end, Long organizationId) {

        Option option = new Option();
        DateTime startDate = new DateTime(start);
        DateTime endDate = new DateTime(end);

        option.title().text("快递占比情况汇总");
        option.tooltip().show(true).trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.legend().data("Standard", "Expedited").orient(Orient.vertical);
        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, Tool.restore, Tool.saveAsImage);
        option.calculable(true);

        List<String> shipservicelevels = Lists.newArrayList("Standard", "Expedited");
        Map<String, Long> shipServiceLevelMap = Maps.newLinkedHashMap();
        for(int i=0; i<shipservicelevels.size(); i++){
            Long num = orderDao.countByDateAndShipServiceLevel(startDate.getMillis(), endDate.getMillis(), organizationId, shipservicelevels.get(i));
            shipServiceLevelMap.put(shipservicelevels.get(i), num);
        }

        Pie pie = new Pie("快递占比");

        pie.clockWise(false).radius("55%");
        for(Map.Entry<String, Long> entry: shipServiceLevelMap.entrySet()){
            String key = entry.getKey();
            Long value = entry.getValue();
            pie.data(new PieData(key, value));
        }
        //pie.itemStyle(dataStyle);

        option.series(pie);
        return option;
    }

    public void areaSchedule(){
        for(int i=0; i<days.length; i++){
            Date startDate = new DateTime().minusDays(days[i]).withTimeAtStartOfDay().toDate();
            Date endDate = new DateTime().millisOfDay().withMaximumValue().toDate();
            List<Object[]> orderAreas = orderAreaDao.countByDate(startDate, endDate);

            for(Object[] orderArea: orderAreas){
                Long organizationId = (Long)orderArea[0];
                String state = (String) orderArea[1];
                String stateCn = (String) orderArea[2];
                Long num = (Long) orderArea[3];

                AreaCount areaCount = new AreaCount();
                areaCount.setStartDate(startDate);
                areaCount.setEndDate(endDate);
                areaCount.setOrganizationId(organizationId);
                areaCount.setState(state);
                areaCount.setStateCn(stateCn);
                areaCount.setNum(num);
                areaCount.setCreated(new Date());

                areaCountDao.save(areaCount);
            }
        }
    }


    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
