package com.example.myapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 橡皮筋效果的scrollview
 *
 * Created by xs on 14/11/26.
 */
public class BounceScrollview extends ScrollView {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;

    private int mMaxYOverscrollDistance;

    private Scroller mScroller;

    private View mInner;

    private float y;// 点击时y坐标

    private int total = 0;

    private int mTop = 0;

    private int lastPos = 0;

    private boolean isCount = false;// 是否开始计算

    /**
     * Position of the last motion event.
     */
    private int mLastMotionY;

    private int mActivePointerId = -1;

    private boolean mIsBeingDragged = false;

    public BounceScrollview(Context context) {
        this(context, null);
    }

    public BounceScrollview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //initBounceListView();

        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mInner = getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isCount = false;
                break;
            case MotionEvent.ACTION_UP:
                mTop = mInner.getTop();
                lastPos = 0;
                mScroller.startScroll(0, 0, 0, mTop, 1500);
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;// 按下时的y坐标
                float nowY = ev.getY();// 时时y坐标
                int deltaY = (int) (preY - nowY);// 滑动距离
                if (!isCount) {
                    deltaY = 0; // 在这里要归0.
                    total = 0;
                }

                total += deltaY;
                y = nowY;

                if (isNeedMove()) {
                    int temp = (int)(Math.abs(total) / 1.5);
                    if (temp > 0) {
                        total = 0;
                        temp = deltaY < 0 ? -temp : temp;
                        mInner.offsetTopAndBottom(-temp);
                    }
                }
                isCount = true;
                break;

        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int temp = lastPos - mScroller.getCurrY();
            lastPos = mScroller.getCurrY();
            mInner.offsetTopAndBottom(temp);
            postInvalidate();
        }
        super.computeScroll();
    }

    public boolean isNeedMove() {
//        if (mInner != null) {
//            int offset = mInner.getMeasuredHeight() - getHeight();
//            int scrollY = getScrollY();
//            // 0是顶部，后面那个是底部
//            if (scrollY == 0 || scrollY >= offset) {
//                return true;
//            }
//        }
//        return false;
        if (getScrollY() == 0 || !canScrollVertically(-1) || !canScrollVertically(1)) {
            return true;
        }
        return false;
    }



    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
    {
        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    }

    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    };


}
