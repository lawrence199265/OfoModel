package com.zhuangbudong.ofo.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.lawrence.core.lib.core.net.HttpResult;
import com.lawrence.core.lib.utils.utils.Logger;
import com.zhuangbudong.ofo.activity.inter.ISignInActivity;
import com.zhuangbudong.ofo.model.User;
import com.zhuangbudong.ofo.utils.AppUtil;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangxu on 17/2/9.
 */
public class SignInPresenter extends OfoBasePresenter<ISignInActivity> {


    public SignInPresenter(ISignInActivity iView, Context context) {
        super(iView, context);
    }

    public void signIn(final String userName, String pwd) {
        if (!AppUtil.isNetWorkConnected(context)) {
            iView.showToast("网络连接不可用，请检查");
        }

        if (TextUtils.isEmpty(userName)) {
            iView.showToast("用户名不能为空!");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            iView.showToast("密码不能为空!");
            return;
        }
//        iView.showLoading();


        apiService.login(userName, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<User>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.error(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult<User> userHttpResult) {
                        Logger.debug(TAG, userHttpResult.getData().toString());
                    }
                });

    }
}
