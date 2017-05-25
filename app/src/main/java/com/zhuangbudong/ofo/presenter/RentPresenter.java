package com.zhuangbudong.ofo.presenter;

import android.content.Context;

import com.lawrence.core.lib.core.mvp.BasePresenter;
import com.lawrence.core.lib.core.mvp.IBaseView;
import com.lawrence.core.lib.core.net.HttpResult;
import com.zhuangbudong.ofo.activity.inter.IRentActivity;
import com.zhuangbudong.ofo.model.Issue;
import com.zhuangbudong.ofo.net.SchedulersTransformer;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xxx on 17/5/21.
 */

public class RentPresenter extends OfoBasePresenter<IRentActivity> {

    public RentPresenter(IRentActivity iView, Context context) {
        super(iView, context);
    }

    public void submitRentInfo(Issue issue) {
        apiService.issue(issue).compose(new SchedulersTransformer<HttpResult<JSONObject>>()).subscribe(new Observer<HttpResult<JSONObject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<JSONObject> jsonObjectHttpResult) {
                iView.showToast(jsonObjectHttpResult.getMsg());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
