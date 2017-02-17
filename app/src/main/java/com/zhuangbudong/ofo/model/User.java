package com.zhuangbudong.ofo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangxu on 17/2/17.
 */

public class User implements Parcelable{

    private String nickName;
    private String id;
    private String address;
    private String email;
    private String phone;


    public User() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    protected User(Parcel in) {
        nickName = in.readString();
        id = in.readString();
        address = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickName);
        dest.writeString(id);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(phone);
    }
}
