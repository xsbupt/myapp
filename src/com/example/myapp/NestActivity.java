package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xs on 14/12/18.
 */
public class NestActivity extends Activity {

    private NestScrollView mScrollView;

    private ListView mListView;

    private TextView mStickView;

    static ArrayList<String> data = new ArrayList<String>();

    static {
        for (int i = 0; i < 30; i++) {
            data.add("here" + String.valueOf(i));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nest);
        mScrollView = (NestScrollView) findViewById(R.id.scroll_view);
        mListView = (ListView) findViewById(R.id.listview);
        mStickView = (TextView) findViewById(R.id.stick_header);
        ViewTreeObserver viewTreeObserver = mScrollView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int viewHeight = mScrollView.getHeight();
                    Log.v("xs", "viewheight--->" + viewHeight);
                    mListView.getLayoutParams().height = viewHeight - mStickView.getHeight();
                }
            });
        }
        mListView.setAdapter(mAdater);
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
