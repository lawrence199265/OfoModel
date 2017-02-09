package com.lawrence.core.lib.utils.error;


import com.lawrence.core.lib.utils.utils.DateUtil;
import com.lawrence.core.lib.utils.utils.FileUtil;
import com.lawrence.core.lib.utils.utils.SDCardUtil;

import java.io.File;
import java.io.IOException;


/**
 * Leo Crash and Exception Catch Class
 * <p>
 * 全局异常捕获
 * <p>
 * <p/>
 * Signal Instance Class
 * <p/>
 * Created by wangxu on 16/8/9.
 */

public class LeoCrashException {

    private static String EXPORT_PATH = SDCardUtil.getSDCardPath() + "/Leo/error/";

    private static LeoCrashException leoCrashException;

    /**
     * 获取单例对象
     *
     * @return 当前类的实例对象
     */
    public static LeoCrashException getInstance() {
        if (leoCrashException == null) {
            leoCrashException = new LeoCrashException();
        }
        return leoCrashException;
    }

    private LeoCrashException() {
    }

    private String getExportFilePath() {
        return EXPORT_PATH + DateUtil.getDate() + File.separator + DateUtil.getDate(DateUtil.DateStyle.HHMMSS) + ".error";
    }

    public static void setExportPath(String path) {
        EXPORT_PATH = path;
    }


    /**
     * 设置全局异常捕获
     */
    public void setCaughtException() {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler());
    }


    private final class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            reportException(ex);
        }
    }


    /**
     * 导出异常信息到指定文件
     *
     * @param throwable exception
     */
    public void reportException( Throwable throwable) {
        try {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            stringBuilder.append(throwable.getMessage());

            for (StackTraceElement stackTraceElement : stackTrace) {
                String className = stackTraceElement.getClassName();
                String fileName = stackTraceElement.getFileName();
                int lineNumber = stackTraceElement.getLineNumber();
                String methodName = stackTraceElement.getMethodName();
                stringBuilder.append("\n")
                        .append(fileName)
                        .append(" ")
                        .append(className)
                        .append(" ")
                        .append(lineNumber)
                        .append(" ")
                        .append(methodName);
            }
            stringBuilder.append("\n");
            FileUtil.writeFile(getExportFilePath(), stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
