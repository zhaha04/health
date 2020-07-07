package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReport() {
        Map<String, Object> reportData = new HashMap<String,Object>();
        //reportDate
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 星期一
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(today));
        // 星期天
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
        // 1号
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDay4ThisMonth());
        // 本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());


        String reportDate = sdf.format(today);
        //=======================会员数量===========================
        //todayNewMember 今日新增会员
        int todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //totalMember
        int totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember 本周新增会员数
        int thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember 本月新增会员数
        int thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);
        //==================================================

        //========================订单统计============================
        //todayOrderNumber 今日预约数
        int todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //todayVisitsNumber 今日到诊数
        int todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        //thisWeekOrderNumber 本周预约数
        int thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday, sunday);
        //thisWeekVisitsNumber 本周到诊数
        int thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber 本月预约数
        int thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber 本月到诊数
        int thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        //========================== 热门套餐========================
        //hotSetmeal
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);

        return reportData;
    }


    @Override
    public int findMemberCount(Date sDate)throws Exception{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String date = simpleDateFormat.format(sDate);
        String fristD = date + "-01";
        String lastD = date + "-31";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fristDay = format.parse(fristD);
        Date lastDay = format.parse(lastD);
            return memberDao.findByMonthMemberCount(fristDay,lastDay );



    }


}
