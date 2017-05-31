package com.zhuangbudong.ofo.activity.inter;

import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.lawrence.core.lib.core.mvp.IBaseView;
import com.zhuangbudong.ofo.model.User;

/**
 * Created by xxx on 17/5/8.
 */

public interface IPersonActivity extends IBaseView {

    void showUser(User user);
}
