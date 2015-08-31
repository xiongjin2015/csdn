package com.haha.csdn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.haha.csdn.R;
import com.haha.csdn.adapter.PersonalAdapter;
import com.haha.csdn.utils.Constiant;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class SetActivity extends HaBaseActionBarActivity implements OnItemClickListener{
    
    private final static String TAG = "PersonFragment";

    private String[] mItems;
    
    private ListView mListView;
    
    private int[] mImages = {
            R.drawable.ic_personal_feedback, R.drawable.ic_personal_upgrade,
            R.drawable.ic_personal_share
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        
        initActionBar();
        
        initView();
        mItems = getResources().getStringArray(R.array.personal_items);
        PersonalAdapter personalAdapter = new PersonalAdapter(mItems, mImages, this);
        mListView.setAdapter(personalAdapter);
    }
    
    private void initActionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setTitle(R.string.action_settings);
        bar.setIcon(R.drawable.channel_back);
    }
    
    private void initView() {
        mListView = (ListView) findViewById(R.id.view_main_pesonal_listview);
        mListView.setOnItemClickListener(this);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            back();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
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
        switch (position) {
            case 0:
                FeedbackAgent agent = new FeedbackAgent(this);
                agent.startFeedbackActivity();
                break;
            case 1:
                UmengUpdateAgent.forceUpdate(this);
                break;
            case 2:
                shareToFriends();
                break;

            default:
                break;
        }
    }
    
    private void shareToFriends() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,
                    getString(R.string.share_to_friends));
            intent.putExtra(Intent.EXTRA_TEXT, getString(
                    R.string.share_to_friends_content, Constiant.UPDATE_URL));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getTitle()));
        } catch (Exception e) {
            Log.e(TAG, "error:", e);
        }

    }

}
