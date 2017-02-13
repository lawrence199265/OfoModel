package com.zhuangbudong.ofo.presenter;

import android.content.Context;
import android.os.Handler;

import com.lawrence.core.lib.utils.utils.AssetsUtil;
import com.zhuangbudong.ofo.activity.inter.ILoadingActivity;

/**
 * Created by wangxu on 17/2/8.
 */

public class LoadingPresenter extends OfoBasePresenter<ILoadingActivity> {

    public LoadingPresenter(ILoadingActivity iView, Context context) {
        super(iView, context);
    }


    public void copyResource() {
        if (AssetsUtil.copyFilesAssets(context, "", "")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iView.success();
                }
            }, 3000);
        } else {
            iView.showToast("数据异常");
        }
    }
}
