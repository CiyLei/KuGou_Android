package com.example.kugou;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Upload extends Activity implements OnClickListener{

	ImageView img_hide_sc_close;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_hide_sc);
		
		img_hide_sc_close= (ImageView) findViewById(R.id.img_hide_sc_close);
		
		img_hide_sc_close.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
}
