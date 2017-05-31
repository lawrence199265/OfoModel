package com.zhuangbudong.ofo.presenter;

import android.content.Context;
import android.util.Log;

import com.lawrence.core.lib.core.net.HttpResult;
import com.lawrence.core.lib.utils.utils.Logger;
import com.zhuangbudong.ofo.fragment.INewsFragment;
import com.zhuangbudong.ofo.model.Issue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xxx on 17/5/21.
 */

public class NewsPresenter extends OfoBaseFragmentPresenter<INewsFragment> {

    private static final String TAG = "NewsPresenter";

    public NewsPresenter(INewsFragment iView, Context context) {
        super(iView, context);
    }

    public void updateListView() {

        apiService.allIssue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<Issue>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(HttpResult<List<Issue>> jsonArrayHttpResult) {
                        Log.d(TAG, "onNext: ");
//                        iView.notifyUi(issues2List(jsonArrayHttpResult.getData()));
                        iView.notifyUi(jsonArrayHttpResult.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.error(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });


//        apiService.allIssue().compose(new SchedulersTransformer<HttpResult<List<Issue>>>())
//                .subscribe(new Observer<HttpResult<List<Issue>>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(HttpResult<List<Issue>> listHttpResult) {
//                if (listHttpResult.getError() == 0&&listHttpResult.getData()!=null) {
//                    iView.notifyUi(listHttpResult.getData());
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

    }


    private List<Issue> issues2List(JSONArray json) {
        List<Issue> issueList = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = json.getJSONObject(i);
                Issue issue = new Issue();
                issue.setTitle(jsonObject.getString("title"));
                issue.setDetail(jsonObject.getString("detail"));
                issue.setImage((String[]) jsonObject.get("image"));
                issue.setPhone(jsonObject.getString("phone"));
                issue.setUserId(jsonObject.getInt("userId"));
                issueList.add(issue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return issueList;
    }


}
