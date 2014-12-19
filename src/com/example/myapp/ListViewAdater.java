package com.example.myapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 14/11/23.
 */
public class ListViewAdater extends BaseAdapter {

    private int mCol = 1;

    private List<String> mData = new ArrayList<String>();

    public ListViewAdater(int col) {
        for (int i = 0; i < 30; i++) {
            mData.add(String.valueOf(i));
        }
        mCol = col;
    }


    @Override
    public int getCount() {
        int size = mData.size() / mCol + (mData.size() % mCol == 0 ? 0 : 1);
        Log.v("xs", "size--->" + size);
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridHolder holder = new GridHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            LinearLayout row = new LinearLayout(parent.getContext());
            convertView = row;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setLayoutDirection(LinearLayout.HORIZONTAL);
            params.weight = 1;
            for (int i = 0; i < mCol; i++) {
                View tempView = inflater.inflate(R.layout.list_item, null);
                row.addView(tempView, params);
                ViewHolder itemHolder = new ViewHolder();
                itemHolder.title = (TextView) tempView.findViewById(R.id.title);
                holder.gridHolder.add(itemHolder);
            }
            convertView.setTag(holder);
        } else {
            holder = (GridHolder) convertView.getTag();
        }

        for (int index = 0; index < mCol; index++) {
            if ((position*mCol+index) < mData.size()) {
                ViewHolder item = holder.gridHolder.get(index);
                item.title.setText("---->" + mData.get(position * mCol + index));
            }
        }
        return convertView;
    }

    static class GridHolder {
        List<ViewHolder> gridHolder = new ArrayList<ViewHolder>();
    }

    static class ViewHolder {
        public TextView title;
    }
}
