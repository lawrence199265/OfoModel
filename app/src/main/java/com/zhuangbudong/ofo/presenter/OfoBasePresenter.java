package com.zhuangbudong.ofo.presenter;

import android.content.Context;

import com.lawrence.core.lib.core.mvp.IBaseView;
import com.lawrence.core.lib.core.mvp.BasePresenter;
import com.zhuangbudong.ofo.net.ApiService;
import com.zhuangbudong.ofo.net.RetrofitNetApi;

/**
 * Created by wangxu on 17/2/9.
 */

class OfoBasePresenter<T extends IBaseView> extends BasePresenter<T> {

    private ApiService apiService;

    OfoBasePresenter(T iView, Context context) {
        super(iView, context);
        apiService = RetrofitNetApi.getApiServiceInstance();
    }
}
