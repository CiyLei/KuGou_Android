package com.example.kugou;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Advertising extends Activity {

	TextView tv_advertising_time;
	int advertising_time = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_advertising);
		
		tv_advertising_time = (TextView) findViewById(R.id.tv_advertising_time);
		
		Timer timer = new Timer(true);
		timer.schedule(task, 1000,1000);
		
	}
	
	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(advertising_time > 0)
				advertising_time--;
			if (advertising_time == 0) {
				overridePendingTransition(R.animator.notanim,R.animator.yes_to_no);
				finish();
			}
			mHandler.sendEmptyMessage(advertising_time);
		}
	};
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			tv_advertising_time.setText("我是广告:"+msg.what);
		};
	};
	
}
