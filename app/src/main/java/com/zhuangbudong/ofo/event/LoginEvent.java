package com.zhuangbudong.ofo.event;

import android.support.annotation.IntDef;

/**
 * Created by xxx on 17/5/9.
 */

public class LoginEvent {
    public static final int LOGIN = 1;
    public static final int LOGOUT = 2;

    private int type;

    public LoginEvent() {
    }

    public LoginEvent(@AccountEventType int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @IntDef({
            LOGIN, LOGOUT
    })
    @interface AccountEventType {

    }
}
