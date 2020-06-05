package com.sspu.service.impl;


import com.sspu.dao.SoilNodeMapper;
import com.sspu.pojo.SoilNode;
import com.sspu.service.ISoilNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据查询
 * @author ymj
 * @Date： 2019/11/13 20:21
 */
@Service("SoilNodeService")
public class SoilNodeServiceImpl implements ISoilNodeService {

    @Autowired
    private SoilNodeMapper soilNodeMapper;


    @Override
    public List<SoilNode> selectSoilNodeSet() {
        List<SoilNode> resultCount = soilNodeMapper.selectSoilNodeSet();
        return resultCount;
    }

}
