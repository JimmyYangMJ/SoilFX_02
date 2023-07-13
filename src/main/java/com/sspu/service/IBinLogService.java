package com.sspu.service;

import com.sspu.pojo.BinLog;

import java.util.List;

/**
 * @author ymj
 * @Date： 2020/6/10 12:07
 */
public interface IBinLogService {

    /**
     * 查询当前日志信息
     * @return
     */
    List<BinLog> selectCurrentBinLog();

}

