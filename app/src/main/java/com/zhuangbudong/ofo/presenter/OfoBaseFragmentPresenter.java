package com.zhuangbudong.ofo.presenter;

import android.content.Context;

import com.lawrence.core.lib.core.mvp.BaseFragmentPresenter;
import com.lawrence.core.lib.core.mvp.IBaseFragmentView;
import com.zhuangbudong.ofo.application.OfoApplication;
import com.zhuangbudong.ofo.model.UserRepository;
import com.zhuangbudong.ofo.net.ApiService;
import com.zhuangbudong.ofo.net.RetrofitNetApi;

/**
 * Created by xxx on 17/5/21.
 */

public class OfoBaseFragmentPresenter<T extends IBaseFragmentView> extends BaseFragmentPresenter<T> {
    public ApiService apiService;
    public UserRepository userRepository;

    public OfoBaseFragmentPresenter(T iView, Context context) {
        super(iView, context);
        apiService = RetrofitNetApi.getApiServiceInstance();
        userRepository = OfoApplication.getInstance().getUserRepository();
    }
}
