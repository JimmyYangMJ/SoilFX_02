package com.sspu.service.cloud;

import com.sspu.service.send.SendInterval;
import javafx.application.Platform;

import static com.sspu.controller.port.PortListener.realTimeInfo;

/**
 * @author ymj
 * @Date： 2020/6/6 16:01
 */
public class Execution {

    /**
     * 分析并执行指令
     * <[参数个数],[参数类型],[client_id],[0/1],>
     *     休眠/唤醒
     *          <3,2,1,1,>
     * @param MessageReceived
     */
    public static boolean handleInfo(String MessageReceived){

        String[] message = MessageReceived.split(",");
        int type = Integer.parseInt(message[1]);
        System.out.println("参数类型 = " + type);
        System.out.println(type);

        if (type == 2) { /** 休眠指令 */
            /** 休眠唤醒指令 */
            int instruct = Integer.parseInt(message[3]);
            if (instruct == 1) { // 唤醒
                SendInterval.resumeThread();
                realTimeInfoPrint("实时采集已被服务器开启");
            } else  {
                SendInterval.pauseThread();
                realTimeInfoPrint("实时采集已被服务器暂停");
            }
        }
        return true;
    }

    /** 输出 提示框 */
    public static void realTimeInfoPrint(String message) {
        Platform.runLater(()->{
            realTimeInfo.log(message);
        });
    }
}
