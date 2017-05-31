package com.zhuangbudong.ofo.fragment;

import com.lawrence.core.lib.core.mvp.IBaseFragmentView;
import com.lawrence.core.lib.core.net.HttpResult;
import com.zhuangbudong.ofo.model.Issue;

import java.util.List;

/**
 * Created by xxx on 17/5/21.
 */

public interface INewsFragment extends IBaseFragmentView {

    void notifyUi(List<Issue> data);
}
