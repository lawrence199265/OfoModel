package com.lawrence.core.lib.core.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by wangxu on 17/2/6.
 */

public abstract class BaseFragment<T extends BaseFragmentPresenter> extends Fragment implements IBaseFragmentView {

    private Activity mActivity;
    protected T presenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initPresenter();
        init(view);
        bindData();
    }

    protected abstract void bindData();

    private void init(View view) {
        if (presenter!=null){
            presenter.init(view);
        }
    }


    @Override
    public void onDestroy() {
        destroyViewAndThings();
        super.onDestroy();

    }

    protected abstract void initPresenter();

    protected abstract void destroyViewAndThings();

    protected abstract int getContentViewLayoutID();



    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz,boolean isCloseCurrentActivity){
        gotoActivity(clz,isCloseCurrentActivity,null);
    }

    private void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle bundle) {
        Intent intent = new Intent(mActivity, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (isCloseCurrentActivity) {
            mActivity.finish();
        }
    }
}
