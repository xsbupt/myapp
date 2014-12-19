package com.example.myapp;

/**
 * Created by xs on 14/11/25.
 */
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class TestScrollerActivity extends Activity {
    private static final String TAG = "TestScrollerActivity";
    LinearLayout lay1,lay2,lay0;
    private Scroller mScroller;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScroller = new Scroller(this);
        lay1 = new MyLinearLayout(this);
        lay2 = new MyLinearLayout(this);

        lay1.setBackgroundColor(this.getResources().getColor(android.R.color.darker_gray));
        lay2.setBackgroundColor(this.getResources().getColor(android.R.color.white));
        lay0 = new ContentLinearLayout(this);
        lay0.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams p0 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
        this.setContentView(lay0, p0);

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
        p1.weight=1;
        lay0.addView(lay1,p1);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
        p2.weight=1;
        lay0.addView(lay2,p2);
        MyButton btn1 = new MyButton(this);
        MyButton btn2 = new MyButton(this);
        btn1.setText("btn in layout1");
        btn2.setText("btn in layout2");
        btn1.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                mScroller.startScroll(0, 0, 500, 500, 1000);
            }
        });
        btn2.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                mScroller.startScroll(20, 20, -50, -50, 50);
            }
        });
        lay1.addView(btn1);
        lay2.addView(btn2);
    }
    class MyButton extends Button
    {
        public MyButton(Context ctx)
        {
            super(ctx);
        }
        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            Log.d("MyButton", this.toString() + " onDraw------");
        }
    }

    class MyLinearLayout extends LinearLayout
    {
        public MyLinearLayout(Context ctx)
        {
            super(ctx);
        }

        @Override
        /**
         * Called by a parent to request that a child update its values for mScrollX
         * and mScrollY if necessary. This will typically be done if the child is
         * animating a scroll using a {@link android.widget.Scroller Scrol eler}
         * object.
         */
        public void computeScroll()
        {
            Log.d(TAG, this.toString() + " computeScroll-----------");
            if (mScroller.computeScrollOffset())//如果mScroller没有调用startScroll，这里将会返回false。
            {
                //因为调用computeScroll函数的是MyLinearLayout实例，
                //所以调用scrollTo移动的将是该实例的孩子，也就是MyButton实例
                scrollTo(mScroller.getCurrX(), 0);
                Log.d(TAG, "getCurrX = " +  mScroller.getCurrX());

                //继续让系统重绘
                getChildAt(0).invalidate();
            }
        }
    }

    class ContentLinearLayout extends LinearLayout
    {
        public ContentLinearLayout(Context ctx)
        {
            super(ctx);
        }

        @Override
        protected void dispatchDraw(Canvas canvas)
        {
            Log.d("ContentLinearLayout", "contentview dispatchDraw");
            super.dispatchDraw(canvas);
        }
    }
}
