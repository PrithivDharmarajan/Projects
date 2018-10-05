/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.bridgellc.bridge.stickygridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Tonic Artos
 *
 * @param <T>
 */
public class StickyGridHeadersSimpleArrayAdapter<T> extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private int mHeaderResId;
    private LayoutInflater mInflater;
    private int mItemResId;
    private ArrayList<HashMap<String, String>> DataArray;

    public StickyGridHeadersSimpleArrayAdapter(Context context, int headerResId, int itemResId, ArrayList<HashMap<String, String>> data) {
        init(context, headerResId, itemResId, data);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return DataArray.size();
    }

    public CharSequence getHeaderId(int position) {
    	//Change Item Type from "T" to "HashMap"
    	HashMap<String, String> item  = getItem(position);
        CharSequence value;
        //get the string that you want to group by.
        if (item.get("Name") instanceof CharSequence) {
        	//Insert it inside value variable
            value = (CharSequence) item.get("Name");
        } else {
        	//Insert it inside value variable
            value = item.get("Name").toString();
        }

        return value.subSequence(0, 1);
    }

    @SuppressWarnings("unchecked")
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        HashMap<String, String> item = getItem(position);
        CharSequence string;
        if (item.get("Name") instanceof CharSequence) {
            string = (CharSequence) item.get("Name");
        } else {
            string = item.get("Name").toString();
        }

        // set home_screen_grid_header text as first char in string
        holder.textView.setText(string.subSequence(0, 1));

        return convertView;
    }












    @Override
    public HashMap<String, String> getItem(int position) {
        return DataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mItemResId, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> item = getItem(position);
        if (item.get("Name") instanceof CharSequence) {
            holder.textView.setText((CharSequence) item.get("Name"));
        } else {
            holder.textView.setText(item.get("Name").toString());
        }

        return convertView;
    }

    private void init(Context context, int headerResId, int itemResId, ArrayList<HashMap<String, String>> data) {
        this.mHeaderResId = headerResId;
        this.mItemResId = itemResId;
        this.DataArray = data;
        mInflater = LayoutInflater.from(context);
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
        public TextView textView;
    }
}
