
package com.haha.csdn.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haha.csdn.R;
import com.haha.splider.bean.NewsItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class NewsItemAdapter extends BaseAdapter {

    private List<NewsItem> mDatas;
    private LayoutInflater mInflater;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public NewsItemAdapter(Context context, List<NewsItem> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
        
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.images)
                .showImageForEmptyUri(R.drawable.images)
                .showImageOnFail(R.drawable.images)
                .cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }
    
    public void addAll(List<NewsItem> datas){  
        mDatas.addAll(datas);  
    } 
    
    public void setDatas(List<NewsItem> datas){
        this.mDatas.clear();
        this.mDatas.addAll(datas);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public NewsItem getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.news_item, null);
            holder = new ViewHolder();
            holder.mContent = (TextView) convertView.findViewById(R.id.content);
            holder.mTitle = (TextView) convertView.findViewById(R.id.title);
            holder.mDate = (TextView) convertView.findViewById(R.id.date);
            holder.mImage = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        
        NewsItem newsItem = mDatas.get(position);
        holder.mTitle.setText(newsItem.getTitle());
        holder.mContent.setText(newsItem.getContent());
        holder.mDate.setText(newsItem.getDate());
        if(TextUtils.isEmpty(newsItem.getImgUrl())){
            holder.mImage.setVisibility(View.GONE);
        }else{
            holder.mImage.setVisibility(View.VISIBLE);
            imageLoader.displayImage(newsItem.getImgUrl(), holder.mImage,options);
        }
        
        return convertView;
    }
    
    private final class ViewHolder{
        TextView mTitle;
        TextView mContent;
        ImageView mImage;
        TextView mDate;
    }

}
