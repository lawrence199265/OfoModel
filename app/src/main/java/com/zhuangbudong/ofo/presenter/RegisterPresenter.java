package com.zhuangbudong.ofo.presenter;

import android.content.Context;

import com.lawrence.core.lib.core.net.HttpResult;
import com.zhuangbudong.ofo.activity.inter.IRegisterActivity;
import com.zhuangbudong.ofo.net.SchedulersTransformer;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wangxu on 17/2/9.
 */
public class RegisterPresenter extends OfoBasePresenter<IRegisterActivity> {
    public RegisterPresenter(IRegisterActivity iView, Context context) {
        super(iView, context);
    }

    public void registerUser(String userName, String password) {
        apiService.register(userName, password)
                .compose(new SchedulersTransformer<HttpResult>())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult jsonObjectHttpResult) {
                        iView.showToast(jsonObjectHttpResult.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showToast(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
