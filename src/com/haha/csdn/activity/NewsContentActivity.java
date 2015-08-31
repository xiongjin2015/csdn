
package com.haha.csdn.activity;

import java.util.List;

import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.haha.csdn.R;
import com.haha.csdn.adapter.NewsContentAdapter;
import com.haha.csdn.utils.Constiant;
import com.haha.splider.bean.News;
import com.haha.splider.manager.NewsManager;

public class NewsContentActivity extends HaBaseActionBarActivity implements OnItemClickListener {
    
    private final static String TAG = "NewsContentActivity";
    private XListView mListView;

    // 该页面的url
    private String url;
    private String title;
    private NewsManager mNewsManager;
    private List<News> mDatas;

    private ProgressBar mProgressBar;
    private NewsContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        
        initActionBar();

        mNewsManager = new NewsManager();

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        mAdapter = new NewsContentAdapter(this);

        mListView = (XListView) findViewById(R.id.id_listview);
        mProgressBar = (ProgressBar) findViewById(R.id.id_newsContentPro);

        mListView.setAdapter(mAdapter);
        mListView.disablePullRefreash();

        mListView.setOnItemClickListener(this);

        mProgressBar.setVisibility(View.VISIBLE);
        new LoadDataTask().execute();
    }
    
    private void initActionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        bar.setIcon(R.drawable.channel_back);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_content, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            back();
            break;
        case R.id.action_share:
            shareToFriends();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void shareToFriends() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,
                    getString(R.string.share_to_friends));
            intent.putExtra(Intent.EXTRA_TEXT, "来自CSDN:"+title+":"+url+"["+getString(
                    R.string.share_to_friends_content, Constiant.UPDATE_URL)+"]");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getTitle()));
        } catch (Exception e) {
            Log.e(TAG, "error:", e);
        }

    }


    /**
     * 点击返回按钮
     * 
     * @param view
     */
    public void back() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = mDatas.get(position - 1);
        String imageLink = news.getImgUrl();
        // Toast.makeText(NewContentActivity.this, imageLink, 1).show();
        Intent intent = new Intent(NewsContentActivity.this, ImageShowActivity.class);
        intent.putExtra("url", imageLink);
        startActivity(intent);
    }

    class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mDatas = mNewsManager.getNews(url).getNewses();
            } catch (Exception e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mDatas == null)
                return;
            mAdapter.addList(mDatas);
            mAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }

    }

}
