package com.zhuangbudong.ofo.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lawrence.core.lib.core.net.ApiException;
import com.lawrence.core.lib.core.net.HttpResult;
import com.zhuangbudong.ofo.activity.inter.ISignInActivity;
import com.zhuangbudong.ofo.application.OfoApplication;
import com.zhuangbudong.ofo.model.User;
import com.zhuangbudong.ofo.model.UserRepository;
import com.zhuangbudong.ofo.net.SchedulersTransformer;
import com.zhuangbudong.ofo.utils.AppUtil;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wangxu on 17/2/9.
 */
public class SignInPresenter extends OfoBasePresenter<ISignInActivity> {


    public SignInPresenter(ISignInActivity iView, Context context) {
        super(iView, context);
    }

    public void signIn(String userName, String pwd) {
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
        iView.showLoading();
        apiService.login(userName, pwd)
                .compose(new SchedulersTransformer<HttpResult<User>>())
                .subscribe(new Observer<HttpResult<User>>() {
                               Disposable disposable;

                               @Override
                               public void onSubscribe(Disposable d) {
                                   disposable = d;
                               }

                               @Override
                               public void onNext(HttpResult<User> userHttpResult) {

                                   if (userHttpResult.getError() == 0 && userHttpResult.getData() != null) {
                                       OfoApplication.getInstance().userId = Integer.parseInt(userHttpResult.getData().getId());
                                       userRepository.updateUser(userHttpResult.getData());
                                       iView.startIntent();
                                   } else {
                                       throw new ApiException(userHttpResult.getMsg());
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   iView.showToast(e.toString());
                                   disposable.dispose();
                                   iView.dismissLoading();
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );

    }
}
