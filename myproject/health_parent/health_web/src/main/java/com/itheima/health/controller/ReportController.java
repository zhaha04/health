package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 会员拆线图
     *
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        // 组装过去12个月的数据, 前端是一个数组
        List<String> months = new ArrayList<String>();
        // 使用java中的calendar来操作日期, 日历对象
        Calendar calendar = Calendar.getInstance();
        // 设置过去一年的时间  年-月-日 时:分:秒, 减去12个月
        calendar.add(Calendar.MONTH, -12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 构建12个月的数据
        for (int i = 0; i < 12; i++) {
            // 每次增加一个月就
            calendar.add(Calendar.MONTH, 1);
            // 过去的日期, 设置好的日期
            Date date = calendar.getTime();
            // 2020-06 前端只展示年-月
            months.add(sdf.format(date));
        }

        // 调用服务查询过去12个月会员数据 前端也是一数组 数值
        List<Integer> memberCount = memberService.getMemberReport(months);
        // 放入map
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("months", months);
        resultMap.put("memberCount", memberCount);

        // 再返回给前端
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, resultMap);
    }

    /**
     * 套餐预约占比
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        // 调用服务查询
        // 套餐数量
        List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();
        // 套餐名称集合
        List<String> setmealNames = new ArrayList<String>();
        // [{name:,value}]
        // 抽取套餐名称
        if (null != setmealCount) {
            for (Map<String, Object> map : setmealCount) {
                //map {name:,value}
                // 获取套餐的名称
                setmealNames.add((String) map.get("name"));
            }
        }
        // 封装返回的结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("setmealNames", setmealNames);
        resultMap.put("setmealCount", setmealCount);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
    }

    /**
     * 运营统计数据
     *
     * @return
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> businessReport = reportService.getBusinessReport();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, businessReport);

    }

    /**
     * 导出运营统计数据报表
     */
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res) {
        // 获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String template = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        // 创建工作簿(模板路径)
        try (// 写在try()里的对象，必须实现closable接口，try()cathc()中的finally
             OutputStream os = res.getOutputStream();
             XSSFWorkbook wk = new XSSFWorkbook(template);) {
            // 获取工作表
            XSSFSheet sht = wk.getSheetAt(0);
            // 获取运营统计数据
            Map<String, Object> reportData = reportService.getBusinessReport();
            // 日期 坐标 2,5
            sht.getRow(2).getCell(5).setCellValue(reportData.get("reportDate").toString());
            //======================== 会员 ===========================
            // 新增会员数 4,5
            sht.getRow(4).getCell(5).setCellValue((Integer) reportData.get("todayNewMember"));
            // 总会员数 4,7
            sht.getRow(4).getCell(7).setCellValue((Integer) reportData.get("totalMember"));
            // 本周新增会员数5,5
            sht.getRow(5).getCell(5).setCellValue((Integer) reportData.get("thisWeekNewMember"));
            // 本月新增会员数 5,7
            sht.getRow(5).getCell(7).setCellValue((Integer) reportData.get("thisMonthNewMember"));

            //=================== 预约 ============================
            sht.getRow(7).getCell(5).setCellValue((Integer) reportData.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((Integer) reportData.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue((Integer) reportData.get("thisWeekOrderNumber"));
            sht.getRow(8).getCell(7).setCellValue((Integer) reportData.get("thisWeekVisitsNumber"));
            sht.getRow(9).getCell(5).setCellValue((Integer) reportData.get("thisMonthOrderNumber"));
            sht.getRow(9).getCell(7).setCellValue((Integer) reportData.get("thisMonthVisitsNumber"));

            // 热门套餐
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) reportData.get("hotSetmeal");
            int row = 12;
            for (Map<String, Object> setmealMap : hotSetmeal) {
                sht.getRow(row).getCell(4).setCellValue((String) setmealMap.get("name"));
                sht.getRow(row).getCell(5).setCellValue((Long) setmealMap.get("setmeal_count"));
                BigDecimal proportion = (BigDecimal) setmealMap.get("proportion");
                sht.getRow(row).getCell(6).setCellValue(proportion.doubleValue());
                sht.getRow(row).getCell(7).setCellValue((String) setmealMap.get("remark"));
                row++;
            }

            // 工作簿写给reponse输出流
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            res.setHeader("Content-Disposition", "attachement;filename=" + filename);
            wk.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出运营统计数据报表
     */
    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest req, HttpServletResponse res) {
        // 获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String template = req.getSession().getServletContext().getRealPath("/template/report_template2.xlsx");
        // 创建工作簿(模板路径)
        try (// 写在try()里的对象，必须实现closable接口，try()cathc()中的finally
             OutputStream os = res.getOutputStream();
        ) {
            // 获取运营统计数据
            Map<String, Object> reportData = reportService.getBusinessReport();
            // 数据模型
            Context context = new PoiContext();
            context.putVar("obj", reportData);


            // 工作簿写给reponse输出流
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            res.setHeader("Content-Disposition", "attachement;filename=" + filename);
            // 把数据模型中的数据填充到文件中
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template), os, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出运营统计数据报表
     */
    @GetMapping("/exportBusinessReportPdf")
    public Result exportBusinessReportPdf(HttpServletRequest req, HttpServletResponse res) {
        // 获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String basePath = req.getSession().getServletContext().getRealPath("/template");
        // jrxml路径
        String jrxml = basePath + File.separator + "report_business.jrxml";
        // jasper路径
        String jasper = basePath + File.separator + "report_business.jasper";

        try {
            // 编译模板
            JasperCompileManager.compileReportToFile(jrxml, jasper);
            Map<String, Object> businessReport = reportService.getBusinessReport();
            // 热门套餐(list -> Detail1)
            List<Map<String, Object>> hotSetmeals = (List<Map<String, Object>>) businessReport.get("hotSetmeal");
            // 填充数据 JRBeanCollectionDataSource自定数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, businessReport, new JRBeanCollectionDataSource(hotSetmeals));
            res.setContentType("application/pdf");
            res.setHeader("Content-Disposition", "attachement;filename=businessReport.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, res.getOutputStream());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "导出运营数据统计pdf失败");
    }

    /**
     * 会员性别饼占图
     *
     * @return
     */
    @GetMapping("/getMemberSex")
    public Result getMemberSex() {
        // 调用服务查询
        List<Map<String, Object>> memberSexCount = memberService.findSexCount();
        // 性别集合
        List<String> memberNames = new ArrayList<>();
        // 抽取性别
        if (null != memberSexCount) {
            for (Map<String, Object> map : memberSexCount) {
                // 获取性别名称
                memberNames.add((String) map.get("name"));

            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("memberNames", memberNames);
        resultMap.put("memberSexCount", memberSexCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_SEX_SUCCESS, resultMap);
    }

    /**
     * 会员年龄段饼占图
     *
     * @return
     */
    @GetMapping("/getMemberAge")
    public Result getMemberAge() {
        List<Map<String, Object>> memberAgeCount = memberService.findBirthCount();
        // 年龄段集合
        List<String> memberNames = new ArrayList<>();
        if (null != memberAgeCount) {
            for (Map<String, Object> map : memberAgeCount) {
                memberNames.add((String) map.get("name"));
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("memberNames", memberNames);
        resultMap.put("memberAgeCount", memberAgeCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_Age_SUCCESS, resultMap);
    }

    @RequestMapping("/fixeTimefindMember")
    public Result fixeTimefindMember(String startTime, String endTime) {
//        System.out.println(startTime + "!!!!!!!!!!!!!!!!!!!!" + endTime);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date sDate = dateFormat.parse(startTime);
            Date eDate = dateFormat.parse(endTime);
            if (startTime == null) {
                return new Result(false, "开始时间不正确或为空");
            }
            if (endTime == null || endTime == "") {
                return new Result(false, "结束时间为空或者不正确");
            }

            if (sDate.after(eDate)) {
                return new Result(false, "不想让你倒着查  要多写好多行代码");
            }
            if (sDate.equals(eDate)) {
                return new Result(false, "小于一个月,真的没啥好看的");
            }
            if (eDate.after(new Date())) {
                return new Result(false, "不能大于今天");
            }

            Calendar scalendar = Calendar.getInstance();
            scalendar.setTime(sDate);

            List<String> months = new ArrayList<String>();
            List<Integer> memberCounts = new ArrayList<Integer>();


            while (true) {
                if (scalendar.getTime().before(eDate)) {
                    scalendar.add(Calendar.MONTH, 1);
                    months.add(dateFormat.format(scalendar.getTime()));
                    int memberCount = reportService.findMemberCount(scalendar.getTime());
                    memberCounts.add(memberCount);

                } else {

                    break;
                }
            }
//            long sDateTime = sDate.getTime();
//            long eDatetime = eDate.getTime();
//            long nd=1000*24*60*60;
//            Double stime = sDateTime / nd*1.0;
//            Double etime = eDatetime / nd * 1.0;
//            int smonth = (int) Math.ceil((stime / 30));
//            int emonth = (int) Math.ceil(etime);
//            List<String> months = new ArrayList<String>();
//            int Monthnum = smonth - emonth;
//            for (int i = 0; i < Monthnum; i++) {
//
//
//            } return    new Result(true, "ok");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("months", months);
            map.put("memberCount", memberCounts);

            return new Result(true, "查询成功", map);
        } catch (Exception e) {
            throw new HealthException("抛出了一条信息");
        }

    }
}
