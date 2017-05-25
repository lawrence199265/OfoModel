package com.zhuangbudong.ofo.utils;

import java.text.DecimalFormat;

/**
 * Created by xxx on 17/5/11.
 */

public class PriceFormatUtil {
    // 将float转换成浮点型保留两位小数的字符串
    public static String toTwoString(String str) {
        try {
            Double v = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("0.00");
            return String.valueOf(df.format(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 去掉double类型尾部的0
     */
    public static String reduceDouble(double price) {
        int i = (int) price;
        if (i == price) {
            return i + "";
        } else {
            return toTwoString(price + "");
        }
    }
}
