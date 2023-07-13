package com.sspu.pojo;

/**
 * Mysql 二进制文件
 * @author ymj
 * @Date： 2020/6/10 12:00
 */
public class BinLog {
    private String Log_name;
    private String File_size;
    private String createTime;

    public String getLog_name() {
        return Log_name;
    }

    public void setLog_name(String log_name) {
        Log_name = log_name;
    }

    public String getFile_size() {
        return File_size;
    }

    public void setFile_size(String file_size) {
        this.File_size = file_size;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
