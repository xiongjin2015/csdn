
package com.haha.csdn.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.haha.csdn.R;
import com.haha.csdn.utils.FileUtil;
import com.haha.csdn.utils.Http;
import com.polites.android.GestureImageView;

public class ImageShowActivity extends HaBaseActionBarActivity {

    private String url;
    private ProgressBar mLoading;
    /** 开源的图片缩放插件进行图片缩放 */
    private GestureImageView mGestureImageView;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        
        ActionBar bar = getSupportActionBar();
        bar.hide();

        url = getIntent().getStringExtra("url");
        mLoading = (ProgressBar) findViewById(R.id.loading);
        mGestureImageView = (GestureImageView) findViewById(R.id.image);

        new DownloadImgTask().execute();
    }

    /**
     * 点击返回按钮
     * 
     * @param view
     */
    public void back(View view) {
        finish();
    }

    public void downloadImg(View view){
        mGestureImageView.setDrawingCacheEnabled(true);
        if (FileUtil.writeSDcard(url, mGestureImageView.getDrawingCache())){
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
        mGestureImageView.setDrawingCacheEnabled(false);
    }

    class DownloadImgTask extends AsyncTask<Void, Void, Void>{
        
        @Override
        protected Void doInBackground(Void... params){
            mBitmap = Http.HttpGetBmp(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            mGestureImageView.setImageBitmap(mBitmap);
            mLoading.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }

}
