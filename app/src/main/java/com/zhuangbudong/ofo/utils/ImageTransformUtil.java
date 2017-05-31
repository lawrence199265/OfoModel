package com.zhuangbudong.ofo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by xxx on 17/5/16.
 */

public class ImageTransformUtil {
    public static String convertIconToString(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appIcon = baos.toByteArray();
        return Base64.encodeToString(appIcon, Base64.DEFAULT);
    }

    public static Bitmap convertStringToIcon(String st) {
        byte[] bitmapByte = Base64.decode(st, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        return bitmap;
    }
}
