package com.example.myapp;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
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

    // scrollview不可滑动
    private boolean mDisable = false;

    // 开始进行scrollview嵌套listview的代码的开发工作
    private int mTouchSlop;

    private int mLastMotionY;

    private int mActivePointerId = -1;

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

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();


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

//    private OnTouchListener mOnTouchListener = new OnTouchListener() {
//        private int mLastY = 0;
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            final int action = event.getAction();
//            switch (action & MotionEvent.ACTION_MASK) {
//                case MotionEvent.ACTION_DOWN:
//                    mLastY = (int) event.getY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    final int y = (int) event.getY();
//                    int deltaY = mLastMotionY - y;
//                    boolean isSrollDown = false;
//                    if (Math.abs(deltaY) > mTouchSlop) {
//                        if (deltaY > 0) {
//                            isSrollDown = true;
//                        } else {
//                            isSrollDown = false;
//                        }
//                    }
//            }
//
//
//            return false;
//        }
//    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                int deltaY = mLastMotionY-y;
                boolean isSrollDown = false;
                if (Math.abs(deltaY) > mTouchSlop) {
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                        isSrollDown = true;
                    } else {
                        deltaY += mTouchSlop;
                        isSrollDown = false;
                    }

                    mLastMotionY = y;
                    if (!isSrollDown) {
                        // 从下往上滑，同时listview不能继续滑动
                        if (!mListView.canScrollList(-1)) {
                            mInterupt = true;
                            mDisable = false;
                        } else {
                            mDisable = true;
                            mListView.dispatchTouchEvent(ev);
                        }
                    } else {
                        // 从上往下滑，同时scrollview不能继续滑动
                        if (!canScrollDown(1)) {
                            if (mInterupt) {
                                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, ev.getX(), ev.getY(), 0);
                                mListView.dispatchTouchEvent(event);
                            }
                            mInterupt = false;
                            mListView.dispatchTouchEvent(ev);
                            mDisable = true;
                        } else {
                            mInterupt = true;
                            mDisable = false;
                        }
                    }
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mDisable) {
            return true;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    private boolean canScrollDown(int direction) {
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset >= 0;
        } else {
            return offset < range;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
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

                        overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, range,
                                0, mOverflingDistance, false);
                        onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);

                        if (getScrollY() >= range) {
                            isOver = true;
                            mScroller.forceFinished(true);
                            mScroller.abortAnimation();
//                            Log.v("xs", "isover--->" + true);
                        }
                    }

                    if (!awakenScrollBars()) {
                        postInvalidateOnAnimation();
                    }

                }

                if (mSelftScroller.computeScrollOffset() && isOver) {
                    int y = mSelftScroller.getCurrY();
                    if (mInterupt) {
                        mListView.setScroller(mSelftScroller);
                        mInterupt = false;
                        Log.v("xs", "I am going to break throw");
                    }
//                    Log.v("xs", "start to call listview---->" + y);
//                    mAdater.notifyDataSetChanged();
//                    Log.v("xs", "self--->" + y + "---->" + foucus);
//                    mListView.scrollTo(0, y - 600);
                    if (mScroller.isFinished()) {
                        //postInvalidateOnAnimation();
                    }

                    //mAdater.notifyDataSetChanged();
//                    mListView.setScroller(mSelftScroller);
                    //mListView.notifyChange();
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
