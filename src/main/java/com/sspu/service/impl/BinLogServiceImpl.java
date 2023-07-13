package com.sspu.service.impl;

import com.sspu.dao.MysqlBinLogMapper;
import com.sspu.pojo.BinLog;
import com.sspu.service.IBinLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ymj
 * @Date： 2020/6/10 12:11
 */
@Service("BinLogService")
public class BinLogServiceImpl implements IBinLogService {

    @Autowired
    MysqlBinLogMapper BinLogMapper;

    @Override
    public List<BinLog> selectCurrentBinLog() {
        return BinLogMapper.selectCurrentBinLog();
    }
}
