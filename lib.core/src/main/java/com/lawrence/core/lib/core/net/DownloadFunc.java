package com.lawrence.core.lib.core.net;

import android.text.TextUtils;
import android.util.Log;

import com.lawrence.core.lib.utils.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * Created by wangxu on 16/12/16.
 */

public class DownloadFunc implements Func1<ResponseBody, Boolean> {


    private static final String TAG = "DownloadFunc";
    private String savePath = "";

    public DownloadFunc(String savePath) {
        this.savePath = savePath;
    }

    @Override
    public Boolean call(ResponseBody responseBody) {
        byte[] b = null;
        try {
            b = responseBody.bytes();
            saveFile(b);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException("下载失败    " + e.getMessage());
        }

        return true;
    }


    private void saveFile(byte[] b) throws IOException {
        if (TextUtils.isEmpty(savePath)) {
            throw new IllegalArgumentException(savePath + " 非法路径!");
        } else if (savePath == null) {
            throw new NullPointerException("写入非法数据！");
        } else {
//            File file = new File(savePath);
            File file = FileUtil.createFile(savePath);
//            if (!file.exists()) {
//                if (!file.getParentFile().exists()) {
//                    file.getParentFile().mkdirs();
//                }
//                file.createNewFile();
//            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(b);
            fileOutputStream.close();
            Log.d(TAG, "saveFile: finished");
        }
    }
}
