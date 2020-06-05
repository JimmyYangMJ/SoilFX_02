package com.sspu.constants;

/**
 * 结点状态
 * @author ymj
 * @Date： 2020/4/28 18:43
 */
public enum NodeStatus {

    status01("01","正常"),
    status02("02","休眠"),
    status03("03","低电压"),
    status00("00","未定义");

    private final String stateID;

    private final String stateMessage;

    public String getStateMessage() {
        return stateMessage;
    }

    NodeStatus(String stateID, String stateMessage){
        this.stateID = stateID;
        this.stateMessage = stateMessage;
    }

}
