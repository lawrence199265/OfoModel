package com.zhuangbudong.ofo.presenter;

import android.content.Context;

import com.zhuangbudong.ofo.activity.inter.IPersonActivity;
import com.zhuangbudong.ofo.model.User;
import com.zhuangbudong.ofo.net.SchedulersTransformer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xxx on 17/5/8.
 */

public class PersonInfoPresenter extends OfoBasePresenter<IPersonActivity> {


    public PersonInfoPresenter(IPersonActivity iView, Context context) {
        super(iView, context);
    }

    public void getUser() {
                userRepository.update().compose(new SchedulersTransformer<User>()).subscribe(new Observer<User>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(User user) {
                iView.showUser(user);
                disposable.dispose();


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
