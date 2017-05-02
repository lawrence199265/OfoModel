package com.zhuangbudong.ofo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by xxx on 17/2/24.
 */

public class BannerViewPager extends FrameLayout implements OnPageChangeListener {
    private LoopViewPager mViewPager;
    private PagerAdapter mAdapter;
    private AutoIndicator indicators;
    private Context mContext;
    private int currentItemPosition;


    private int mViewPagerScrollState;


    private boolean isAutoRolling = false;
    private int mAutoRollingTime = 4000;
    private int mReleaseTime = 0;
    private static final String TAG = "BannerViewPager";


    private static final int MESSAGE_AUTO_ROLLING = 0x1001;
    private static final int MESSAGE_AUTO_ROLLING_CANCEL = 0x1002;
    private int lastX;
    private int lastY;


    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mViewPager = new LoopViewPager(mContext);
        ViewPager.LayoutParams vpLayoutParams = new ViewPager.LayoutParams();
        vpLayoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
        vpLayoutParams.height = ViewPager.LayoutParams.MATCH_PARENT;
        mViewPager.setLayoutParams(vpLayoutParams);

        indicators = new AutoIndicator(mContext);
        LayoutParams indicatorLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        indicatorLp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        indicatorLp.bottomMargin = 10;
        indicators.setLayoutParams(indicatorLp);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d(TAG, "onPageScrolled: " + position);

    }

    @Override
    public void onPageSelected(int position) {

        setIndicators(position, 0);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            mViewPagerScrollState = ViewPager.SCROLL_STATE_DRAGGING;
        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            mReleaseTime = (int) System.currentTimeMillis();
            mViewPagerScrollState = ViewPager.SCROLL_STATE_IDLE;
        }
    }

    //是否支持自动滑动
    public void setAutoRolling(boolean autoRolling) {
        isAutoRolling = autoRolling;
    }


    public void setAutoRollingTime(int mAutoRollingTime) {
        this.mAutoRollingTime = mAutoRollingTime;
    }

    public void setAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0, false);
        mViewPager.setOnPageChangeListener(this);
        mAdapter = adapter;

        this.addView(mViewPager);
        this.addView(indicators);
        if (!isAutoRolling) {
            Log.d(TAG, "setAdapter: ");
            postDelayed(mAutoRollingTask, mAutoRollingTime);
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_AUTO_ROLLING:
                    int currentPosition = mViewPager.getCurrentItem();
                    Log.d(TAG, "handleMessage: " + currentPosition);
                    if (currentPosition == mViewPager.getAdapter().getCount() - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {

                        mViewPager.setCurrentItem(currentPosition + 1);

                    }
                    postDelayed(mAutoRollingTask, mAutoRollingTime);
                    break;
                case MESSAGE_AUTO_ROLLING_CANCEL:
                    postDelayed(mAutoRollingTask, mAutoRollingTime);
                    break;
            }
        }
    };
    private Runnable mAutoRollingTask = new Runnable() {
        @Override
        public void run() {
            int now = (int) System.currentTimeMillis();
            int timediff = mAutoRollingTime;
            if (mReleaseTime != 0) {
                timediff = now - mReleaseTime;
            }
            if (mViewPagerScrollState == ViewPager.SCROLL_STATE_IDLE) {
                if (timediff >= mAutoRollingTime * 0.8) {
                    mHandler.sendEmptyMessage(MESSAGE_AUTO_ROLLING);
                } else {
                    mHandler.sendEmptyMessage(MESSAGE_AUTO_ROLLING_CANCEL);
                }
            } else if (mViewPagerScrollState == ViewPager.SCROLL_STATE_DRAGGING) {
                mHandler.sendEmptyMessage(MESSAGE_AUTO_ROLLING_CANCEL);
            }
        }
    };

    public void setIndicators(int position, float offset) {
        indicators.setPositionAndOffset(position, offset);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                Log.i(TAG, "dealtX:=" + dealtX);
                Log.i(TAG, "dealtY:=" + dealtY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SaveState saveState = new SaveState(parcelable);
        saveState.currentPosition = currentItemPosition;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveSate = (SaveState) state;
        super.onRestoreInstanceState(saveSate.getSuperState());
        mViewPager.setCurrentItem(saveSate.currentPosition);
    }

    static class SaveState extends BaseSavedState {
        int currentPosition;

        public SaveState(Parcel source) {
            super(source);
            currentPosition = source.readInt();
        }

        public SaveState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentPosition);
        }

        public static final Creator<SaveState> CREATOR = new Creator<SaveState>() {
            @Override
            public SaveState createFromParcel(Parcel source) {
                return new SaveState(source);
            }

            @Override
            public SaveState[] newArray(int size) {
                return new SaveState[size];
            }
        };
    }
}
