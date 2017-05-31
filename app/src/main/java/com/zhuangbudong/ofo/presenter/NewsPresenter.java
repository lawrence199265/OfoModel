package com.zhuangbudong.ofo.presenter;

import android.content.Context;

import com.lawrence.core.lib.core.mvp.IBaseView;
import com.lawrence.core.lib.core.net.HttpResult;
import com.zhuangbudong.ofo.fragment.INewsFragment;
import com.zhuangbudong.ofo.model.Issue;
import com.zhuangbudong.ofo.net.SchedulersTransformer;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xxx on 17/5/21.
 */

public class NewsPresenter extends OfoBaseFragmentPresenter<INewsFragment> {


    public NewsPresenter(INewsFragment iView, Context context) {
        super(iView, context);
    }

    public void updateListView() {
        apiService.allIssue().compose(new SchedulersTransformer<HttpResult<List<Issue>>>())
                .subscribe(new Observer<HttpResult<List<Issue>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<Issue>> listHttpResult) {
                if (listHttpResult.getError() == 0&&listHttpResult.getData()!=null) {
                    iView.notifyUi(listHttpResult.getData());
                }
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
