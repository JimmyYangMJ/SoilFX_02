package com.sspu.service;

import com.sspu.pojo.DataAD;
import com.sspu.pojo.SoilNode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询结点信息
 * @author ymj
 * @Date： 2019/11/13 20:18
 */

public interface ISoilNodeService {

    /**
     * 查询结点相关信息
     * node_id  状态， 间隔，更新时间
     * @return 结点相关信息 List
     */
    List<SoilNode> selectSoilNodeSet();

    /**
     * 插入水势数据
     * @param dataAD
     * @return
     */
    int insertSoilWater(DataAD dataAD);



}
