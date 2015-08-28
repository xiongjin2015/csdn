package com.haha.csdn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;

import com.haha.csdn.R;

public class StartActivity extends HaBaseActionBarActivity {
    
    private Handler handler = new Handler();
    private Runnable runnable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_start);
        
        ActionBar bar = getSupportActionBar();
        bar.hide();
        
        handler.postDelayed(runnable = new Runnable() {
            
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);  
                startActivity(intent); 
                finish();
            }
        }, 1000);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);  
            startActivity(intent); 
            finish();
            
            if(runnable != null)
                handler.removeCallbacks(runnable);
        }
        return super.onTouchEvent(event);
    }
    
    @Override
    protected void onDestroy() {
        runnable = null;
        handler = null;       
        super.onDestroy();
    }

}
