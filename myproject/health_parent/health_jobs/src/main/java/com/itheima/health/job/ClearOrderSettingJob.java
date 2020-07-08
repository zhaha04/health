package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.ClearOrderSettingService;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ClearOrderSettingJob {
    @Reference
    private ClearOrderSettingService clearOrderSettingService;

    //定时清理预约设置历史数据
    public void clearOrderSetting() throws ParseException {

        //获取日历对象
        Calendar calendar = Calendar.getInstance();
        //设置当前日期
        calendar.setTime(new Date());
        //获取当前日期的月份
        int month = calendar.get(Calendar.MONTH) + 1;
        //获取当前日期的年份
        int year = calendar.get(Calendar.YEAR);
        //定义要删除月份的起始日
        String startDateStr =null;
        String endDateStr=null;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        if (month > 1){
            //如果月份大于1, 则删除(月份-1)月的数据
            startDateStr = year + "-"+(month-1) + "-01";
            Date startDate = sdf.parse(startDateStr);
            endDateStr = year+"-"+(month-1) + "-31";
            Date endDate = sdf.parse(endDateStr);
            clearOrderSettingService.clearOrderSettingBetweenDate(startDate,endDate);
        }
        if (month==1){
            //如果月份为1, 则删除当前日期上一年的12月份的数据
            startDateStr = (year-1) + "-" + "12" + "-01";
            Date startDate = sdf.parse(startDateStr);
            endDateStr =(year-1) + "-" + "12" +"-31";
            Date endDate = sdf.parse(endDateStr);
            clearOrderSettingService.clearOrderSettingByMonth(startDate,endDate);
        }
    }
}
