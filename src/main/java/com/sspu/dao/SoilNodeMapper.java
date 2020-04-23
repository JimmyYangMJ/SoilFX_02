package com.sspu.dao;


import com.sspu.pojo.SoilNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ymj
 * @Date： 2019/11/13 20:24
 */
public interface SoilNodeMapper {
    /**
     * 查询结点的 详细信息
     * @return 结点信息表
     */
    List<SoilNode> selectSoilNodeSet();

}
