package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;

public class MyActivity extends Activity {

    private SmoothScrollView mScrollView;

    private SmoothListView mListView;

    private TextView mTextView;

    private ViewPager mViewPage;

    static ArrayList<String> data = new ArrayList<String>();

    static {
        for (int i = 0; i < 30; i++) {
            data.add(String.valueOf(i));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_scrollview);

        mScrollView = (SmoothScrollView) findViewById(R.id.scroll_view);
        mTextView = (TextView) findViewById(R.id.test1);
        mTextView.setText(R.string.gradle);

        mListView = (SmoothListView) findViewById(R.id.listview);
        mListView.setAdapter(mAdater);
        mAdater.notifyDataSetChanged();

        mScrollView.setListView(mListView, mAdater);

        ViewTreeObserver viewTreeObserver = mScrollView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int viewWidth = mScrollView.getWidth();
                    int viewHeight = mScrollView.getHeight();
                    mListView.getLayoutParams().height = viewHeight;
                    Log.v("xs", "view---->" + viewHeight + "--->" + viewWidth);
//                    mListView.scrollBy(0, 100);
                    mListView.setSelection(5);
                }
            });
        }

        Log.v("xs", "madter---->" + mAdater.hasStableIds());
    }

    class Holder extends GridListVIewAdater.ViewHolder<String> implements Cloneable {

        private TextView title;

        @Override
        public GridListVIewAdater.ViewHolder newInstance() {
            return new Holder();
        }

        @Override
        public View createView(int index, LayoutInflater inflater) {
            View tempView = inflater.inflate(R.layout.list_item, null);
            title = (TextView) tempView.findViewById(R.id.title);
            return tempView;
        }

        @Override
        public void showData(int index, String data) {
            title.setText("--->" + data);
        }
    }

    private GridListVIewAdater mAdater = new GridListVIewAdater(1, data, new Holder()) {
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    };
}
