package com.itheima.health.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * @author 张鹏
 * @date 2020/7/6 9:42
 */

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:applicationContext-service.xml")
public class JsonTest {

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void test01() throws Exception {

        HashMap<String, Integer> map = new HashMap<>();
        map.put(null, 4);
        map.put("1", 6);
        map.put("0", 3);


        HashMap<String, Integer> newMap = new HashMap<>();

        for (String s : map.keySet()) {
            Integer nmber = map.get(s);
            if (s == null) {
                newMap.put("性别不详", nmber);
            } else if (s.equals("0")) {
                newMap.put("男", nmber);
            } else if (s.equals("1")) {
                newMap.put("女", nmber);
            }

        }
        System.out.println(newMap);


    }

    @Test
    public void test02() throws Exception {

        ArrayList<Integer> arrayList = new ArrayList<>();


        Collections.addAll(arrayList, 1, 5, 8, 9, 2, 3, 6);

        System.out.println(arrayList.contains(1));
        System.out.println(arrayList.contains(7));

        ArrayList<Integer> arrayList1 = new ArrayList<>();
        Collections.addAll(arrayList1, 8, 5, 7, 9, 2, 3, 6);


        HashMap<Integer, Object> hashMap = new HashMap<>();
        hashMap.put(15, arrayList);
        hashMap.put(17, arrayList1);

        System.out.println(hashMap);


    }

    @Test
    public void test03() throws Exception {
        Jedis jedis = jedisPool.getResource();
        String itemId = jedis.hget("connected_to_all_id", "checkitem_id");

        System.out.println(itemId);
        if (!itemId.contains("1")) {  // 查看是否被关联，没有被关联则可以修改
            System.out.printf("dasdas");
        }


        jedis.close();
    }
}
