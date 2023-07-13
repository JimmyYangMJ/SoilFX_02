package com.sspu.dao;

import com.sspu.pojo.BinLog;

import java.util.List;

/**
 * @author ymj
 * @Date： 2020/6/10 12:08
 */
public interface MysqlBinLogMapper {

    List<BinLog> selectCurrentBinLog();

}
