package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MemberDao {
    List<Member> findAll();
    Page<Member> selectByCondition(String queryString);
    void add(Member member);
    void deleteById(Integer id);
    Member findById(Integer id);
    Member findByTelephone(String telephone);
    void edit(Member member);
    Integer findMemberCountBeforeDate(String date);
    Integer findMemberCountByDate(String date);
    Integer findMemberCountAfterDate(String date);
    Integer findMemberTotalCount();

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
    int findByMonthMemberCount(@Param("fristDay") Date fristDay, @Param("lastDay") Date lastDay);
}
