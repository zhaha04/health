package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.GoodsInSoldException;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;
import java.util.Set;

/**
 * Description: No Description
 * User: Eric
 */
public interface CheckItemService {

    /**
     * 查询所有的检查项
     *
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 新增检查项
     *
     * @param checkitem
     */
    void add(CheckItem checkitem);

    /**
     *
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 删除
     *
     * @param id
     */
    void deleteById(int id) throws GoodsInSoldException;

    /**
     * 通过id查询
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 更新检查项
     * @param checkitem
     */
    void update(CheckItem checkitem) throws GoodsInSoldException;

    /**
     * 获取被关联的检查项的所有id
     * @return
     */
    Set<String> findAllByCheckeitemToIds();
}
