package com.lawrence.core.lib.core.net;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * Created by wangxu on 17/2/10.
 */

public class UpLoadFunc {


    private File[] files;

    public UpLoadFunc(File... files) {
        this.files = files;
    }

    public Map<String, ResponseBody> upload(String type) {
        Map<String, ResponseBody> partMap = new HashMap<>();
        for (File file : files) {
            ResponseBody body = ResponseBody.create(MediaType.parse(type), String.valueOf(file));
            partMap.put("file\";filename=\"" + file.getName() + "\"", body);
        }

        return partMap;
    }

}
