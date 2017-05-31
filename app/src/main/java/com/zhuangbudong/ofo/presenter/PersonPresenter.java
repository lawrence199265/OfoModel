package com.zhuangbudong.ofo.presenter;

import android.content.Context;

import com.lawrence.core.lib.core.mvp.BaseFragmentPresenter;
import com.zhuangbudong.ofo.application.OfoApplication;
import com.zhuangbudong.ofo.fragment.inter.IPersonFragment;
import com.zhuangbudong.ofo.model.User;
import com.zhuangbudong.ofo.model.UserRepository;
import com.zhuangbudong.ofo.net.SchedulersTransformer;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xxx on 17/5/12.
 */

public class PersonPresenter extends BaseFragmentPresenter<IPersonFragment> {
    private UserRepository repository;
    public PersonPresenter(IPersonFragment iView, Context context) {
        super(iView, context);
        repository= OfoApplication.getInstance().getUserRepository();
    }

    public void updateUser() {
        repository.update().compose(new SchedulersTransformer<User>()).subscribe(new Consumer<User>() {
            @Override
            public void accept(@NonNull User user) throws Exception {
                iView.updateUser(user);
            }
        });
    }
}
