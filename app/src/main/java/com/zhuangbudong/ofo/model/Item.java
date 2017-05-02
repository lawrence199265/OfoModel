package com.zhuangbudong.ofo.model;

import java.util.List;

/**
 * Created by xxx on 17/3/17.
 */

public class Item extends BaseBean {
    private String id;
    private List<ImageModel> photos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ImageModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageModel> photos) {
        this.photos = photos;
    }
}
