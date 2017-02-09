package com.lawrence.core.lib.utils.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;

/**
 * SDCard 工具类
 * <p/>
 * Created by wangxu on 16/8/9.
 */
public class SDCardUtil {


    /**
     * 检测SDCard是否可用
     *
     * @return
     */
    public static boolean isAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SDCard路径
     *
     * @return sdcard 路径
     */
    public static String getSDCardPath() {
        if (isAvailable()) {
            return Environment.getExternalStorageDirectory().getPath() + "/";
        } else {
            return "SDCard is broken";
        }
    }

    /**
     * 获取路径剩余存储空间
     *
     * @param path 目标路径
     * @return 剩余存储空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String[] getAvailableBlocks(String path) {
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        return fileSize(blockSize * availableBlocks);
    }


    /**
     * 获取总容量数量
     *
     * @param path
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String[] getBlockCount(String path) {

        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSizeLong();
        long totalCount = statFs.getBlockCountLong();
        return fileSize(blockSize * totalCount);
    }


    //返回数组，下标1代表大小，下标2代表单位 KB/MB
    private static String[] fileSize(long size) {
        String str = "";
        if (size >= 1024) {
            str = "KB";
            size /= 1024;
            if (size >= 1024) {
                str = "MB";
                size /= 1024;
                if (size >= 1024) {
                    str = "GB";
                    size/=1024;
                }
            }
        }
        DecimalFormat formatter = new DecimalFormat();
        formatter.setGroupingSize(3);
        String result[] = new String[2];
        result[0] = formatter.format(size);
        result[1] = str;
        return result;
    }

}