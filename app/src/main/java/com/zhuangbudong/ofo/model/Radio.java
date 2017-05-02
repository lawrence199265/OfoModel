package com.zhuangbudong.ofo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xxx on 17/4/7.
 */

public class Radio implements Parcelable {
    private String label;
    private boolean isChecked;

    protected Radio(Parcel in) {
        label = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<Radio> CREATOR = new Creator<Radio>() {
        @Override
        public Radio createFromParcel(Parcel in) {
            return new Radio(in);
        }

        @Override
        public Radio[] newArray(int size) {
            return new Radio[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
