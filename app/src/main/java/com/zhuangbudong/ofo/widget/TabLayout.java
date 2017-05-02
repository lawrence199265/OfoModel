package com.zhuangbudong.ofo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxx on 17/3/2.
 */

public class TabLayout extends LinearLayout {
    private List<TabEntity> tabEntities;
    private OnTabClickListener listener;
    private int mTabCount;
    private int mIndicatorColor = Color.BLUE;
    private int mTitleSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
    private int mTitleColor = Color.parseColor("#a3a3a5");
    private int mTitleGravity = Gravity.BOTTOM;
    private Context mContext;
    private int currentPosition = 0;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        obtainStyledAttributes(mContext, attrs);
        tabEntities = new ArrayList<>();
    }

    private void obtainStyledAttributes(Context mContext, AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.MyTabLayout);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.MyTabLayout_indicatorColor) {
                mIndicatorColor = array.getColor(attr, getResources().getColor(R.color.main_color));
            } else if (attr == R.styleable.MyTabLayout_textSize) {
                mTitleSize = (int) array.getDimension(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.MyTabLayout_textGravity) {
                mTitleGravity = array.getInt(attr, Gravity.BOTTOM);
            }
        }
        array.recycle();
    }

    private void addTab(final int position, final TabItem tabItem) {
        tabItem.setIndicatorColor(mIndicatorColor);

        tabItem.setIconBitmap(tabEntities.get(position).getIconResId());
        tabItem.setTitle(tabEntities.get(position).getTitle());


        tabItem.setTitleColor(mTitleColor);
        tabItem.setTitleSize(mTitleSize);
        tabItem.setTitleGravity(mTitleGravity);

        if (position == 0) {
            tabItem.setIndicatorAlpha(1.0f);
        }
        tabItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                resetTab();
                tabItem.setIndicatorAlpha(1.0f);
                if (listener != null) {
                    listener.onClick(tabItem, position);
                }
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getScreenWidth() / mTabCount, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(tabItem, position, lp);
    }


    public void setCurrentPosition(int position) {
        resetTab();
        TabItem tabItem = (TabItem) getChildAt(position);
        tabItem.setIndicatorAlpha(1.0f);

    }

    public void resetTab() {
        for (int i = 0; i < getChildCount(); i++) {
            TabItem tabView = (TabItem) getChildAt(i);
            tabView.setIndicatorAlpha(0.0f);
        }
    }

    public void setTabData(List<TabEntity> entitys) {
        if (entitys == null || entitys.size() <= 0) {
            throw new IllegalStateException("entitys can not be NULL or EMPTY !");
        }
        tabEntities = entitys;
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        removeAllViews();
        this.mTabCount = tabEntities.size();

        TabItem tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = new TabItem(mContext, tabEntities.get(i));
            tabView.setTag(i);
            addTab(i, tabView);
        }
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        this.listener = listener;
    }


    public interface OnTabClickListener {
        void onClick(View tab, int position);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


}
