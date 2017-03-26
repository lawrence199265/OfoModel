package com.zhuangbudong.ofo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.adpter.BannerAdapter;
import com.zhuangbudong.ofo.adpter.NewsAdapter;
import com.zhuangbudong.ofo.model.Item;
import com.zhuangbudong.ofo.utils.DatasUtil;
import com.zhuangbudong.ofo.utils.DensityUtils;
import com.zhuangbudong.ofo.widget.BannerViewPager;
import com.zhuangbudong.ofo.widget.CustomDividerDecoration;

import java.util.List;


public class NewsFragment extends Fragment {

    private int[] imageList = new int[]{R.drawable.ic_img04, R.drawable.a, R.drawable.c};
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private BannerViewPager loopViewPager;
    private RecyclerView rlList;
    private List<Item> circleDatas;


    public NewsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewsFragment newInstance() {

        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();


    }

    private void initData() {
        circleDatas = DatasUtil.createCircleDatas();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
        loopViewPager = (BannerViewPager) rootView.findViewById(R.id.news_banner_vp);
        loopViewPager.setFocusable(true);
        loopViewPager.setFocusableInTouchMode(true);
        loopViewPager.requestFocus();
        loopViewPager.setAdapter(new BannerAdapter(imageList));
        rlList = (RecyclerView) rootView.findViewById(R.id.news_rl_list);
        rlList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        CustomDividerDecoration customDividerDecoration = new CustomDividerDecoration(getContext(), DividerItemDecoration.VERTICAL);
        customDividerDecoration.setLineHeight(DensityUtils.dip2px(getContext(), 16));
        rlList.addItemDecoration(customDividerDecoration);
        NewsAdapter newsAdapter = new NewsAdapter(getContext());
        newsAdapter.setDatas(circleDatas);
        rlList.setAdapter(newsAdapter);
        return rootView;
    }

}