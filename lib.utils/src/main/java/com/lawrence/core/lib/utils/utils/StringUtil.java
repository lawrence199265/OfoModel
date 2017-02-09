package com.lawrence.core.lib.utils.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by wangxu on 16/12/5.
 */

public class StringUtil {


    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    public static String compress(String str) {
        String retStr = str;

        if (TextUtils.isEmpty(str) || str.length() == 0) {
            retStr = "";
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
                gzipOutputStream.write(str.getBytes());
                gzipOutputStream.close();
                retStr = out.toString("ISO-8859-1");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return retStr;
    }

    public static String unCompress(String str) {
        String retStr = "";
        if (isEmpty(str)) {
            return retStr;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
            GZIPInputStream gUnZip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];

            int n;
            while ((n = gUnZip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            retStr = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retStr;
    }

}
