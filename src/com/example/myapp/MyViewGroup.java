package com.example.myapp;

/**
 * Created by xs on 14/11/25.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class MyViewGroup extends LinearLayout {
    private boolean s1=true;
    Scroller mScroller=null;
    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller=new Scroller(context);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }
    public void beginScroll(){
        if (!s1) {
            mScroller.startScroll(-800, 0, 800, 0, 1000);
            s1 = true;
        } else {
            mScroller.startScroll(0, 0, -800, 0, 1000);
            s1 = false;
        }
        invalidate();
    }
}

