
package com.haha.csdn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.haha.csdn.R;

public class PersonalAdapter extends BaseAdapter {

    private String[] items;
    private int[] images;
    private LayoutInflater inflater;

    public PersonalAdapter(String[] items, int[] images, Context context) {
        this.items = items;
        this.images = images;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public String getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_personal_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.listitem_personal);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.tv.setText(items[position]);
        holder.tv.setCompoundDrawablesWithIntrinsicBounds(images[position], 0, 0, 0);
        return convertView;
    }

    private class ViewHolder{
        //ImageView iv;
        TextView tv;
    }

}
