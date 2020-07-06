package com.itheima.health.service;

import java.util.Date;

public interface ClearOrderSettingService {
    //月份大于1, 则删除(月份-1)预约设置历史数据
    void clearOrderSettingBetweenDate(Date startDate, Date endDate);

    //月份等于1, 则删除上一年12月份预约设置历史数据
    void clearOrderSettingByMonth(Date startDate, Date endDate);
}