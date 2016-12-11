package com.example.kugou;

import java.util.ArrayList;
import java.util.List;

import com.example.kugou.Adapter.Home_Hide_ZP_Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class Works extends Activity implements OnClickListener {

	ImageView img_hide_zp_close;
	ListView ls_hide_zp;
	Home_Hide_ZP_Adapter adapter;
	
	List<String> names,times;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_hide_zp);
		
		img_hide_zp_close=(ImageView) findViewById(R.id.img_hide_zp_close);
		ls_hide_zp=(ListView) findViewById(R.id.ls_hide_zp);
		
		img_hide_zp_close.setOnClickListener(this);

		names=new ArrayList<String>();
		times=new ArrayList<String>();
		names.add("Ï²»¶Äã");
		times.add("2015-03--19 14:32");
		adapter = new Home_Hide_ZP_Adapter(this, names, times);
		ls_hide_zp.setAdapter(adapter);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
	
}
