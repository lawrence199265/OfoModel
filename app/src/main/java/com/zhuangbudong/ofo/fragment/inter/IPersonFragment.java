package com.zhuangbudong.ofo.fragment.inter;

import com.lawrence.core.lib.core.mvp.IBaseFragmentView;
import com.zhuangbudong.ofo.model.User;

/**
 * Created by xxx on 17/5/12.
 */

public interface IPersonFragment extends IBaseFragmentView {
    void updateUser(User user);
}
