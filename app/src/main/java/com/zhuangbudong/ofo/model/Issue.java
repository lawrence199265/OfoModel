package com.zhuangbudong.ofo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wangxu on 17/2/17.
 */

public class Issue implements Parcelable{

    private int id;
    private String title;
    private String type;
    private String memo;
    private ArrayList<byte[]> image;


    public Issue() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public ArrayList<byte[]> getImage() {
        return image;
    }

    public void setImage(ArrayList<byte[]> image) {
        this.image = image;
    }

    protected Issue(Parcel in) {
        id = in.readInt();
        title = in.readString();
        type = in.readString();
        memo = in.readString();
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(memo);
    }
}
