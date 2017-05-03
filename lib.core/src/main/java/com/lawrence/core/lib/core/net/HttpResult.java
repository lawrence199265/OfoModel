package com.lawrence.core.lib.core.net;

/**
 * Created by wangxu on 16/11/24.
 */

public class HttpResult<T> {


    /**
     * 用来封装相同格式的 http 请求
     * <p>
     * isSuccess : true
     * msg : 成功
     * data : {}
     */

    private int error;
    private String msg;
    private T data;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

}
