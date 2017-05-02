package com.zhuangbudong.ofo.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.DetailActivity;
import com.zhuangbudong.ofo.activity.PersonInfoActivity;
import com.zhuangbudong.ofo.adpter.BannerAdapter;
import com.zhuangbudong.ofo.adpter.NewsAdapter;
import com.zhuangbudong.ofo.model.Item;
import com.zhuangbudong.ofo.utils.DatasUtil;
import com.zhuangbudong.ofo.utils.DensityUtils;
import com.zhuangbudong.ofo.widget.BannerViewPager;
import com.zhuangbudong.ofo.widget.CustomDividerDecoration;
import com.zhuangbudong.ofo.widget.listener.RecyclerItemClickListener;

import java.util.List;


public class NewsFragment extends Fragment {

    private int[] imageList = new int[]{R.drawable.ic_img04, R.drawable.a, R.drawable.c};
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private BannerViewPager loopViewPager;
    private XRecyclerView rlList;
    private List<Item> circleDatas;
    private int refreshTime = 0;
    private int times = 0;


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
        rlList = (XRecyclerView) rootView.findViewById(R.id.news_rl_list);
        rlList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.divider);
        rlList.addItemDecoration(rlList.new DividerItemDecoration(dividerDrawable));
        rlList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rlList.setLoadingMoreEnabled(true);
        rlList.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        final NewsAdapter newsAdapter = new NewsAdapter(getContext(), imageList);
        newsAdapter.setDatas(circleDatas);
        rlList.setAdapter(newsAdapter);

        rlList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {


                        newsAdapter.notifyDataSetChanged();
                        rlList.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                if (times < 2) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            rlList.loadMoreComplete();
                            newsAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            rlList.setNoMore(true);
                            newsAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
                times++;
            }
        });

        newsAdapter.setOnRecyclerItemListener(new NewsAdapter.onRecyclerItemListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(getActivity(), DetailActivity.class));
            }
        });

        return rootView;
    }

}