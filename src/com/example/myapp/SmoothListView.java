package com.example.myapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.OverScroller;

/**
 * Created by xs on 14/12/8.
 */
public class SmoothListView extends ListView {

    private OverScroller mScroller;

    public SmoothListView(Context context) {
        this(context, null);
    }

    public SmoothListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
//        mScroller = new OverScroller(getContext());
    }

    public void setScroller(OverScroller scroller) {
        this.mScroller = scroller;
    }

    public void notifyChange() {
        //postInvalidateOnAnimation();
        //this.performClick();
        //this.setPressed(true);
        //this.requestFocusFromTouch();
        //Log.v("xs", "mlist--->" + this.isInTouchMode());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                int curY = mScroller.getCurrY();
                scrollBy(0, curY-600);
//                Log.v("xs", "start to scroll listview--->" + curY);
            }
        }
    }
}
