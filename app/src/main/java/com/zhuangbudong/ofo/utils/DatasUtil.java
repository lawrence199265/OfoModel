package com.zhuangbudong.ofo.utils;

import com.zhuangbudong.ofo.model.ImageModel;
import com.zhuangbudong.ofo.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xxx on 17/3/17.
 */

public class DatasUtil {
    private static int itemID = 0;
    public static List<ImageModel> PHOTOS = new ArrayList<>();

    static {
        ImageModel p1 = new ImageModel();
        p1.url = "http://img.blog.163.com/photo/rkFWi0Un8nzSIfpPBGPXpA==/4528650900298350700.jpg";
        p1.w = 200;
        p1.h = 200;

        ImageModel p2 = new ImageModel();
        p2.url = "http://v1.qzone.cc/pic/201402/02/20/28/52ee39fa1c96a830.jpg%21600x600.jpg";
        p2.w = 200;
        p2.h = 200;

        ImageModel p3 = new ImageModel();
        p3.url = "http://imglf0.ph.126.net/fAcE0ioFGvFRvnpa4y_IzA==/2659094105003605284.jpg";
        p3.w = 200;
        p3.h = 200;


        PHOTOS.add(p1);
        PHOTOS.add(p2);
        PHOTOS.add(p3);

    }

    public static List<ImageModel> createPhotos() {
        List<ImageModel> photos = new ArrayList<ImageModel>();
        int size = getRandomNum(PHOTOS.size() + 1);
        if (size > 0) {
            if (size > 9) {
                size = 9;
            }
            for (int i = 0; i < size; i++) {
                ImageModel photo = PHOTOS.get(getRandomNum(PHOTOS.size()));
                if (!photos.contains(photo)) {
                    photos.add(photo);
                } else {
                    i--;
                }
            }
        }
        return photos;
    }

    public static int getRandomNum(int max) {
        Random random = new Random();
        int result = random.nextInt(max);
        return result;
    }

    public static List<Item> createCircleDatas() {
        List<Item> circleDatas = new ArrayList<Item>();
        for (int i = 0; i < 15; i++) {
            Item item = new Item();
            item.setId(String.valueOf(itemID++));
            item.setPhotos(createPhotos());
            circleDatas.add(item);
        }

        return circleDatas;
    }
}
