package com.zhuangbudong.ofo.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lawrence.core.lib.core.mvp.BaseFragment;
import com.lawrence.core.lib.utils.utils.Logger;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.DetailActivity;
import com.zhuangbudong.ofo.adpter.NewsAdapter;
import com.zhuangbudong.ofo.model.Issue;
import com.zhuangbudong.ofo.presenter.NewsPresenter;
import com.zhuangbudong.ofo.widget.BannerViewPager;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends BaseFragment<NewsPresenter> implements INewsFragment {

    private int[] imageList = new int[]{R.drawable.ic_img04, R.drawable.a, R.drawable.c};
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private BannerViewPager loopViewPager;
    private XRecyclerView rlList;
    private List<Issue> circleDatas = new ArrayList<>();
    private int refreshTime = 0;
    private int times = 0;
    private NewsAdapter newsAdapter;


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


    }


    @Override
    protected void bindData() {
        presenter.updateListView();
    }

    @Override
    protected void initPresenter() {
        presenter = new NewsPresenter(this, getContext());
    }

    @Override
    protected void destroyViewAndThings() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main2;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initView(View view) {
        rlList = (XRecyclerView) view.findViewById(R.id.news_rl_list);
        rlList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.divider);
        rlList.addItemDecoration(rlList.new DividerItemDecoration(dividerDrawable));
        rlList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rlList.setLoadingMoreEnabled(true);
        rlList.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        newsAdapter = new NewsAdapter(getContext(), imageList);
        rlList.setAdapter(newsAdapter);
        newsAdapter.setDatas(circleDatas);

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
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Logger.debug("点击选择:", position + "");
                intent.putExtra("data", newsAdapter.getItemData(position - 2));

                startActivity(intent);
            }
        });
    }

    @Override
    public void notifyUi(List<Issue> data) {
        newsAdapter.setDatas(data);
    }
}