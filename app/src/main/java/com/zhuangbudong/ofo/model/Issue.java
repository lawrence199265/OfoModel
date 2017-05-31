package com.zhuangbudong.ofo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangxu on 17/2/17.
 */

public class Issue implements Parcelable {

    private int userId;
    private String title;
    private String type;
    private String detail;
    private String phone;
    private String userName;
    private String[] image;

    public Issue() {
    }

    protected Issue(Parcel in) {
        userId = in.readInt();
        title = in.readString();
        type = in.readString();
        detail = in.readString();
        phone = in.readString();
        userName = in.readString();
        image = in.createStringArray();
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(detail);
        dest.writeString(phone);
        dest.writeString(userName);
        dest.writeStringArray(image);
    }
}
