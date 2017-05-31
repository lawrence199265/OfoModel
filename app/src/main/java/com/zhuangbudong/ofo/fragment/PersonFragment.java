package com.zhuangbudong.ofo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.lawrence.core.lib.core.mvp.BaseFragment;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.Main2Activity;
import com.zhuangbudong.ofo.activity.PersonInfoActivity;
import com.zhuangbudong.ofo.activity.inter.IMainActivity;
import com.zhuangbudong.ofo.application.OfoApplication;
import com.zhuangbudong.ofo.event.LoginEvent;
import com.zhuangbudong.ofo.fragment.inter.IPersonFragment;
import com.zhuangbudong.ofo.model.User;
import com.zhuangbudong.ofo.presenter.PersonPresenter;
import com.zhuangbudong.ofo.utils.PrefsUtils;

import static com.zhuangbudong.ofo.activity.SignInActivity.EXTRA_SIGN_OK;


public class PersonFragment extends BaseFragment<PersonPresenter> implements View.OnClickListener, IPersonFragment {

    private Button btnQuit;

    public static final int REQUEST_QUIT = -2;

    private FrameLayout flHead;


    private TextView tvUserName;

    public PersonFragment() {
    }


    public static PersonFragment newInstance() {

        Bundle args = new Bundle();

        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.updateUser();
    }

    @Override
    protected void destroyViewAndThings() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_person;
    }


    @Override
    public void showToast(String msg) {

    }

    public void initView(View view) {
        btnQuit = (Button) view.findViewById(R.id.fg_btn_quit);
        flHead = (FrameLayout) view.findViewById(R.id.personal_fl_head);
        tvUserName = (TextView) view.findViewById(R.id.my_user_name);
        btnQuit.setOnClickListener(this);
        flHead.setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    protected void bindData() {

    }

    @Override
    protected void initPresenter() {
        presenter = new PersonPresenter(this, getContext());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fg_btn_quit:
                PrefsUtils.savePrefBoolean(getContext(), EXTRA_SIGN_OK, false);
                RxBus.get().post(new LoginEvent(LoginEvent.LOGOUT));
                OfoApplication.getInstance().userId = -1;
                Intent intent = new Intent(getContext(), Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.personal_fl_head:
                Intent personIntent = new Intent(getContext(), PersonInfoActivity.class);
                startActivity(personIntent);
                break;
        }
    }


    @Override
    public void updateUser(User user) {
        tvUserName.setText(user.getUserName());
    }
}
