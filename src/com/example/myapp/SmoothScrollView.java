package com.example.myapp;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.OverScroller;
import android.widget.ScrollView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xs on 14/12/5.
 */
public class SmoothScrollView extends ScrollView {

    private SmoothListView mListView;

    private GridListVIewAdater mAdater;

    private boolean mInterupt = true;

    private int mPaddingBottom;

    private int mPaddingTop;

    private OverScroller mScroller;

    private OverScroller mSelftScroller;

    private boolean isOver = false;

    public SmoothScrollView(Context context) {
        this(context, null);
    }

    public SmoothScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void setListView(SmoothListView view, GridListVIewAdater adater) {
        this.mListView = view;
        mAdater = adater;
    }

    private void initView() {

        mSelftScroller = new OverScroller(getContext());

        try {
            Class<ScrollView> cls = ScrollView.class;
            Field field = cls.getDeclaredField("mScroller");
            field.setAccessible(true);
            Object obj = field.get(this);
            mScroller = (OverScroller) obj;

            field = cls.getDeclaredField("mPaddingBottom");
            field.setAccessible(true);
            mPaddingBottom = (Integer) field.get(this);

            field = cls.getDeclaredField("mPaddingTop");
            field.setAccessible(true);
            mPaddingTop = (Integer) field.get(this);
        } catch (Exception e) {

        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v("xs", "top--->" + mListView.getTop());
        return mInterupt;
    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            int height = getHeight() - mPaddingBottom - mPaddingTop;
            int bottom = getChildAt(0).getHeight();

//            mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0,
//                    Math.max(0, bottom - height), 0, height/2);

            mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0,
                    Math.max(0, (bottom-height)*2), 0, height/2);

            mSelftScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0,
                    Math.max(0, (bottom-height)*2), 0, height/2);

            isOver = false;


            Log.v("xs", "fling---->" + mScroller.getFinalY() + "--->" + mSelftScroller.getFinalY());

//            if (mFlingStrictSpan == null) {
//                mFlingStrictSpan = StrictMode.enterCriticalSpan("ScrollView-fling");
//            }
            postInvalidateOnAnimation();
        }
    }


    @Override
    public void computeScroll() {

        try {
            Class<ScrollView> cls = ScrollView.class;
            Field field = cls.getDeclaredField("mScroller");
            field.setAccessible(true);
            Object obj = field.get(this);

            int mOverflingDistance = 0;
            field = cls.getDeclaredField("mOverflingDistance");
            field.setAccessible(true);
            mOverflingDistance = (Integer) field.get(this);

            if (obj instanceof OverScroller) {
                OverScroller mScroller = (OverScroller) obj;
                if (mScroller.computeScrollOffset()) {
                    mSelftScroller.computeScrollOffset();

                    int oldX = this.getScrollX();
                    int oldY = this.getScrollY();
                    int x = mScroller.getCurrX();
                    int y = mScroller.getCurrY();

                    if (oldX != x || oldY != y) {
                        Method method=cls.getDeclaredMethod("getScrollRange");
                        method.setAccessible(true);
                        final int range = (Integer) method.invoke(this);
                        final int overscrollMode = getOverScrollMode();
                        final boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                                (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                        Log.v("xs", "one---->" + getScrollX() + "---->" + getScrollY());

                        overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, range,
                                0, mOverflingDistance, false);

                        Log.v("xs", "tow---->" + getScrollX() + "---->" + getScrollY());

                        onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);

                        //Log.v("xs", "range--->" + range + "---->" + x + "---->" + y + "---->" + getHeight() + "---->" + mScroller.getFinalY() + "---->" + mSelftScroller.getCurrY());

                        if (y >= range) {
                            isOver = true;
                            Log.v("xs", "isover--->" + true);
                        }

                        if (y == range) {
                            //mInterupt = false;
                            Log.v("xs", "I am here");
                        } else {
                            mInterupt = true;
                        }


                    }

                    if (!awakenScrollBars()) {
                        postInvalidateOnAnimation();
                    }

                }

                if (mSelftScroller.computeScrollOffset() && isOver) {
                    int y = mSelftScroller.getCurrY();
                    mInterupt = false;
//                    mAdater.notifyDataSetChanged();
                    boolean foucus = mListView.requestFocusFromTouch();
//                    Log.v("xs", "self--->" + y + "---->" + foucus);
//                    mListView.scrollTo(0, y - 600);
                    if (mScroller.isFinished()) {
                        //postInvalidateOnAnimation();
                    }

                    //mAdater.notifyDataSetChanged();
                    //mListView.setScroller(mSelftScroller);
                    //mListView.notifyChange();

                    Log.v("xs", "I am start to listview");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
//        Log.v("xs", "---->" + System.currentTimeMillis());
        super.draw(canvas);
    }
}
