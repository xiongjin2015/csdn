
package com.haha.csdn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.haha.csdn.R;
import com.haha.csdn.adapter.TabAdapter;
import com.haha.csdn.widget.TabPageIndicator;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends HaBaseActionBarActivity {

    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private TabAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        initView();
        UmengUpdateAgent.update(this);
    }
    
    private void initView() {
        mIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_pager);
        mAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initActionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(false);
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setIcon(R.drawable.icon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SetActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
