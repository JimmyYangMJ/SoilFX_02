package com.sspu.service;

import com.sspu.pojo.SoilNode;

import java.util.List;

/**
 * 查询结点信息
 * @author ymj
 * @Date： 2019/11/13 20:18
 */
public interface ISoilNodeService {

    /**
     * 查询结点相关的所有信息
     * @return 结点相关信息 List
     */
    List<SoilNode> selectSoilNodeSet();


}
