package com.example.myapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.ScrollView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xs on 14/12/8.
 */
public class SmoothListView extends ListView {

    private ScrollView mScrollView;

    private OverScroller mScroller;

    private OverScroller mCurScroller;

    private OverScroller mSelfScroller;

    private int mLastY = 0;

    private boolean mFlag = false;

    private boolean mFind = false;

    private Class<?> mInnerClass = null;

    private int mLastScrollY = 0;

    public SmoothListView(Context context) {
        this(context, null);
    }

    public SmoothListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCurScroller = new OverScroller(context);
    }

    public void setScrollView(ScrollView scrollView) {
        mScrollView = scrollView;
    }

    private void initView() {
        if (mSelfScroller != null && mSelfScroller != null) {
            return;
        }
        try {
            Class<AbsListView> cls = AbsListView.class;
            Field field = cls.getDeclaredField("mFlingRunnable");
            field.setAccessible(true);
            Object mFlingRunnable = field.get(this);
            if (mFlingRunnable != null) {
                if (!mFind) {
                    Class<?>[] allClass = AbsListView.class.getDeclaredClasses();
                    for (Class<?> temClass : allClass) {
                        if (temClass.getSimpleName().equals("FlingRunnable")) {
                            mInnerClass = temClass;
                            break;
                        }
                    }
                    mFind = true;
                }
                if (mInnerClass != null) {
                    Field tempField = mInnerClass.getDeclaredField("mScroller");
                    tempField.setAccessible(true);
                    mSelfScroller = (OverScroller)tempField.get(mFlingRunnable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScroller(OverScroller scroller) {
        this.mScroller = scroller;
        mLastY = scroller.getCurrY();
//        final ViewParent parent = getParent();
//        if (parent != null) {
//            parent.requestDisallowInterceptTouchEvent(true);
//        }
        mFlag = true;
        postInvalidateOnAnimation();
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (mCurScroller.computeScrollOffset()) {
                int curY = mCurScroller.getCurrY();
                if (curY == 0) {
                    mCurScroller.abortAnimation();
                    return;
                }
                int temp = mCurScroller.getCurrY() - mLastScrollY;
                if (temp !=0) {
                    if (!canScrollVertically(-1)) {
                            mScrollView.scrollBy(0, -temp);
                            Log.v("xs", "xxxxx---->" + temp + "--->" + mCurScroller.getCurrY() + "--->" + mLastScrollY);
                    }
                    Log.v("xs", "here--->" + (temp));
                }

                mLastScrollY = mCurScroller.getCurrY();
                post(this);
            }
//            post(this);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean flag = super.onTouchEvent(ev);

        initView();
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (mSelfScroller != null && mInnerClass != null) {
                mLastScrollY = mSelfScroller.getStartY();

                mCurScroller.fling(0,  mSelfScroller.getStartY(), 0, (int)mSelfScroller.getCurrVelocity(),
                        0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

                Log.v("xs", "mCurScroller--->" + mSelfScroller.getCurrY() + "--->" + mSelfScroller.getFinalY());
                post(mRunnable);
            }
        }

        return flag;
    }



    public void onTouchUp(MotionEvent event) {
        Log.v("xs", "onTouchUp--->" + event.getAction());
        try {
            Class<ScrollView> cls = ScrollView.class;
            Method method = cls.getDeclaredMethod("onTouchUp");
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                int curY = mScroller.getCurrY();
                if (curY - mLastY > 0) {
                    scrollListBy(curY - mLastY);
                    mLastY = curY;
                    postInvalidateOnAnimation();
                }
            }
        }
    }
}
