package com.itheima.health.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface ClearOrderSettingDao {
    //月份大于1, 则删除(月份-1)预约设置历史数据
    void clearOrderSettingBetweenDate(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    //月份等于1, 则删除上一年12月份预约设置历史数据
    void clearOrderSettingByMonth(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
}
