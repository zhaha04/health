package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.GoodsInSoldException;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.CheckGroupService;
import com.itheima.health.service.CheckItemService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.*;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private CheckItemService checkItemService;

    @Autowired
    private CheckGroupService checkGroupService;


    @Autowired
    private JedisPool jedisPool;

    /**
     * 存放生成静态化页面的目录，开发时，存到health_mobile/webapp/pages下，
     * 上线时，要存放到health_mobile的tomcat目录下
     */
    @Value("${out_put_path}")
    private String out_put_path;


    // 删除、修改、添加新的套餐，需要把保存在redis中的套餐删除,并且把套餐详情删除
    private void setmealListToJsonByRedis(int id) {
        try {
            String setmealJson = new ObjectMapper().writeValueAsString(setmealDao.findAll());
            Jedis jedis = jedisPool.getResource();
            jedis.del("setmealJson");
            jedis.del("setmeal+" + id);
            jedis.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 判断商品是否已上架，否则不能修改 删除
    private void isSetmealByGoods(String strid) {
        Jedis jedis = jedisPool.getResource();
        // 查看是否被关联，没有被关联则可以修改
        String id = jedis.hget("setmeal_id", strid);
        if (id != null) {
            jedis.close();
            // 如果被关联，不能被修改，抛商品在售异常
            throw new GoodsInSoldException();
        }
    }

    /**
     * 添加套餐------------------------------
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 添加套餐信息
        setmealDao.add(setmeal);
        // 获取套餐的id
        Integer setmealId = setmeal.getId();
        // 添加套餐与检查组的关系
        if (null != checkgroupIds) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
        // 添加套餐之后，需要重新把套餐添加到redis中
        this.setmealListToJsonByRedis(setmealId);
        //新增套餐后需要重新生成静态页面
        //  generateMobileStaticHtml();
    }

    /**
     * 生成 列表及详情静态页面
     */
    private void generateMobileStaticHtml() {
        try {
            // 套餐列表静态页面
            generateSetmealListHtml();
            // 套餐详情静态页面 生成单就行了，为了测试方便，生成所有的
            generateSetmealDetailHtml();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成套餐详情静态页面
     */
    private void generateSetmealDetailHtml() throws Exception {
        // 把所有套餐都生成详情页面 方便测试
        List<Setmeal> setmealList = setmealDao.findAll();
        // setmealList中的套餐是没有详情信息，即没有检查组也没有检查项的信息，要查询一遍
        for (Setmeal setmeal : setmealList) {
            // 获取套餐详情
            Setmeal setmealDetail = setmealDao.findDetailById(setmeal.getId());
            // 设置套餐的图片路径
            setmealDetail.setImg(QiNiuUtils.DOMAIN + setmealDetail.getImg());
            // 生成详情页面
            generateDetailHtml(setmealDetail);
        }
    }

    /**
     * 生成套餐详情页面
     *
     * @param setmealDetail
     */
    private void generateDetailHtml(Setmeal setmealDetail) throws Exception {
        // 获取模板 套餐列表的模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal_detail.ftl");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmeal", setmealDetail);
        File setmealDetailFile = new File(out_put_path, "setmeal_" + setmealDetail.getId() + ".html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealDetailFile),"utf-8")));
    }

    /**
     * 生成 套餐列表静态页面
     */
    private void generateSetmealListHtml() throws Exception {
        // 获取模板 套餐列表的模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal.ftl");
        // 获取数据模型
        List<Setmeal> setmealList = setmealDao.findAll();
        // 图片地址
        setmealList.forEach(s -> {
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList", setmealList);
        // 给模板填充数据 new OutputStreamWriter要指定编码格式，否则中文乱码
        // 生成的文件 c:/sz89/health_parent/health_mobile/src/main/webapp/pages/m_setmeal.html
        File setmealListFile = new File(out_put_path, "m_setmeal.html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealListFile),"utf-8")));
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            // 模糊查询 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 条件查询，这个查询语句会被分页
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 通过id查询选中的检查组ids
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐-----------------------------
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 判断商品是否已上架，否则不能修改 删除
        this.isSetmealByGoods(setmeal.getId().toString());

        // 先更新套餐信息
        setmealDao.update(setmeal);
        // 删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        // 添加新关系
        if (null != checkgroupIds) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
        // 修改套餐之后，需要重新把套餐添加到redis中
        this.setmealListToJsonByRedis(setmeal.getId());
    }

    /**
     * 通过id删除套餐----------------------
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteById(int id) {

        // 判断商品是否已上架，否则不能修改 删除
        this.isSetmealByGoods(String.valueOf(id));

        // 统计使用了这个套餐的订单个数，判断套餐是否被使用了
        int count = setmealDao.findOrderCountBySetmealId(id);
        if (count > 0) {
            // 被使用了
            throw new HealthException(MessageConstant.SETMEAL_IN_USE);
        }
        // 没有使用
        // 先删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        // 删除套餐数据
        setmealDao.deleteById(id);
        //删除套餐之后，需要重新把套餐添加到redis中
        this.setmealListToJsonByRedis(id);

    }

    /**
     * 查询所有的套餐----------------------------
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() throws Exception {
        //新增套餐后需要重新生成静态页面
        //generateMobileStaticHtml();

        Jedis jedis = jedisPool.getResource();
        String setmealListJson = jedis.get("setmealJson");

        ObjectMapper mapper = new ObjectMapper();


        if (!jedis.exists("checkitem_id")) {
            // 获取上架的检查项的所有id
            Set<String> checkitem_id = checkItemService.findAllByCheckeitemToIds();
            for (String id : checkitem_id) {
                jedis.hset("checkitem_id", id, id);
            }
        }

        if (!jedis.exists("checkgroup_id")) {
            // 获取上架的检查项的所有id
            Set<String> checkgroup_id = checkGroupService.findAllByCheckegroupToIds();
            for (String id : checkgroup_id) {
                jedis.hset("checkgroup_id", id, id);
            }
        }

        if (!jedis.exists("setmeal_id")) {
            // 获取上架的检查项的所有id
            Set<String> setmeal_id = setmealDao.findAllBySetmealToIds();
            for (String id : setmeal_id) {
                jedis.hset("setmeal_id", id, id);
            }
        }


        if (setmealListJson == null) {  // 如果redis查询不到，则需要去数据库查询数据
            List<Setmeal> setmealList = setmealDao.findAll();
            // 然后转化为json数据保存到redis中
            setmealListJson = mapper.writeValueAsString(setmealList);
            jedis.set("setmealJson", setmealListJson);
            // 关闭连接
            jedis.close();
            return setmealList;
        }

        // 将json数据转化为实体对象
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Setmeal.class);
        List<Setmeal> setmealList = mapper.readValue(setmealListJson, javaType);
        // 关闭连接
        jedis.close();
        return setmealList;
    }

    /**
     * 查询套餐详情---------------------------
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) throws Exception {

        // Setmeal setmeal = setmealDao.findDetailById(id);

        Jedis jedis = jedisPool.getResource();
        String setmealJson = jedis.get("setmeal_" + id);

        if (setmealJson == null) {  // 如果redis查询不到，则需要去数据库查询数据
            Setmeal setmeal = setmealDao.findDetailById(id);
            // 然后转化为json数据保存到redis中
            setmealJson = new ObjectMapper().writeValueAsString(setmeal);
            jedis.set("setmeal_" + id, setmealJson);
            // 关闭连接
            jedis.close();
            return setmeal;
        }

        // 将json数据转化为实体对象
        Setmeal setmeal = new ObjectMapper().readValue(setmealJson, Setmeal.class);
        // 关闭连接
        jedis.close();
        return setmeal;

    }

    /**
     * 查询套餐详情
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById2(int id) {
        return setmealDao.findDetailById2(id);
    }

    @Override
    public Setmeal findDetailById3(int id) {
        // 查询套餐信息
        Setmeal setmeal = setmealDao.findById(id);
        // 查询套餐下的检查组
        List<CheckGroup> checkGroups = setmealDao.findCheckGroupListBySetmealId(id);
        if (null != checkGroups) {
            for (CheckGroup checkGroup : checkGroups) {
                // 通过检查组id检查检查项列表
                List<CheckItem> checkItems = setmealDao.findCheckItemByCheckGroupId(checkGroup.getId());
                // 设置这个检查组下所拥有的检查项
                checkGroup.setCheckItems(checkItems);
            }
            //设置套餐下的所拥有的检查组
            setmeal.setCheckGroups(checkGroups);
        }
        return setmeal;
    }

    /**
     * 获取套餐的预约数量
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
