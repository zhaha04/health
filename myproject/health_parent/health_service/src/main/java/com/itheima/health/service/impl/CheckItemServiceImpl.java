package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.GoodsInSoldException;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

/**
 * Description: No Description
 * 解决 dubbo 2.6.0 【注意，注意，注意】
 * interfaceClass 发布出去服务的接口为这个CheckItemService.class
 * 没加interfaceClass, 调用No Provider 的异常
 * User: Eric
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private JedisPool jedisPool;


    /**
     * 查询 所有检查项
     *
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 新增检查项
     *
     * @param checkitem
     */
    @Override
    public void add(CheckItem checkitem) {
        checkItemDao.add(checkitem);
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //第二种，Mapper接口方式的调用，推荐这种使用方式。
        //PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 模糊查询 拼接 %
        // 判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            // 有查询条件，拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 紧接着的查询语句会被分页
        //Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        //PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());
        List<CheckItem> list = checkItemDao.findByCondition2(queryPageBean.getQueryString());
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>((long) list.size(), list);
        return pageResult;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void deleteById(int id) {

        // 判断商品是否被关联
        this.isCheckItemByGoods(String.valueOf(id));


        //先判断这个检查项是否被检查组使用了
        //调用dao查询检查项的id是否在t_checkgroup_checkitem表中存在记录
        int cnt = checkItemDao.findCountByCheckItemId(id);
        //被使用了则不能删除
        if (cnt > 0) {
            //??? health_web能捕获到这个异常吗？
            throw new HealthException(MessageConstant.CHECKITEM_IN_USE);
        }
        //没使用就可以调用dao删除
        checkItemDao.deleteById(id);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    /**
     * 更新
     *
     * @param checkitem
     */
    @Override
    public void update(CheckItem checkitem) {

        // 判断商品是否被关联
        this.isCheckItemByGoods(checkitem.getId().toString());

        checkItemDao.update(checkitem);


    }


    /**
     * 获取被关联的检查项的所有id
     *
     * @return
     */
    @Override
    public Set<String> findAllByCheckeitemToIds() {
        return checkItemDao.findAllByCheckeitemToIds();
    }


    /**
     * 判断商品是否被关联
     */
    private void isCheckItemByGoods(String strid) {
        Jedis jedis = jedisPool.getResource();
        // 查看是否被关联，没有被关联则可以修改
        String id = jedis.hget("checkitem_id", strid);
        if (id != null) {
            jedis.close();
            // 如果被关联，不能被修改，抛商品在售异常
            throw new GoodsInSoldException();
        }
    }
}
