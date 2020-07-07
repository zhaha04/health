package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.ClearOrderSettingDao;
import com.itheima.health.service.ClearOrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service(interfaceClass = ClearOrderSettingService.class)
public class ClearOrderSettingServiceImpl implements ClearOrderSettingService {
   @Autowired
    private ClearOrderSettingDao clearOrderSettingDao;


    @Override
    public void clearOrderSettingBetweenDate(Date startDate,Date endDate) {
        clearOrderSettingDao.clearOrderSettingBetweenDate(startDate,endDate);
    }

    @Override
    public void clearOrderSettingByMonth(Date startDate, Date endDate) {
        clearOrderSettingDao.clearOrderSettingByMonth(startDate,endDate);
    }

}
