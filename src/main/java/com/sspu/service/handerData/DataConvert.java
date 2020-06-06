package com.sspu.service.handerData;

import com.sspu.pojo.DataAD;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据转换
 * @author ymj
 * @Date： 2020/6/6 10:34
 */
public class DataConvert {

    /**
     * 将 两个 16 进制 对应的 10 进制 转换为 电压值
     * @param ad1 ad 前两个16 进制对应的 10进制
     * @param ad2 ad 后两个16 进制对应的 10进制
     * @return
     */
    public static double adConvert(int ad1, int ad2) {
        double ad = Double.parseDouble(String.valueOf(ad1))*16*16;
        ad += Double.parseDouble(String.valueOf(ad2));  // 合并
        //System.out.println("ad = " + ad);
        return Double.parseDouble(String.format("%.2f", ad / 4098 * 3.3));
    }

    /**
     * 电压值 转换为 水势值
     * Todo 需要压力膜仪滴定，这里是模拟值
     * 水势值: AD*4 取模 14 ：模拟得到水势
     * @param ad
     * @param ad_base
     * @return
     */
    public static double adConvertWater(double ad, double ad_base) {

        int ad_int = (int) (ad * 4);
        double soilWater =  ad_int % 14 + (ad - (int)ad);
        return Double.parseDouble(String.format("%.2f", soilWater));
    }

    /**
     * 转换为 socket 协议数据
     * <[参数个数],[参数类型],[AD],[AD_base],[水势值],[时间],>
     * @return
     */
    public static String socketFormat(DataAD dataAD) {
        String massage = "<5,1,";
        massage += String.valueOf(dataAD.getAd()) + ",";
        massage += String.valueOf(dataAD.getAd_base()) + ",";
        massage += String.valueOf(dataAD.getHumidity()) + ",";
        massage += String.valueOf(dataAD.getHumidity()) + ",";
        return massage + ">";
    }

    public static DataAD createDataAD(int[] message){
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String time = ft.format(date);
        System.out.println("time:" + time);
        /**
         * 绑定数据
         * 在表格显示
         */
        DataAD dataAD = new DataAD();

        /** 接收端口数据  2个字节 = 4个16进制
         *  例如 原始16进制：Ox09 Ox02 == 0902 （16）
         *  message ：9 2
         *  转换 9*16*16 + 2 = （10）
         */
        double ad = DataConvert.adConvert(message[6], message[7]);

        double ad_base = DataConvert.adConvert(message[8], message[9]);

        /** 水势值: AD*4 取模 16 ：模拟得到水势*/
        double soilWater = DataConvert.adConvertWater(ad, ad_base);


        dataAD.setNode(message[3]);
        dataAD.setAd(ad);
        dataAD.setAd_base(ad_base);
        dataAD.setHumidity(soilWater);
        dataAD.setTime(time);

        return dataAD;

    }

}
