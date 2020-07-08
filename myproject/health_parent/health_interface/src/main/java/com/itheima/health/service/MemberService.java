package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
public interface MemberService {
    /**
     * 通过手机号码查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member
     */
    void add(Member member);

    /**
     * 统计过去一年的会员总数
     * @param months
     * @return
     */
    List<Integer> getMemberReport(List<String> months);

    /**
     * 获取会员性别的数量
     * @return
     */
    List<Map<String,Object>> findSexCount();

    /**
     * 获取会员各年龄段的人数
     * @return
     */
    List<Map<String,Object>> findBirthCount();
}
