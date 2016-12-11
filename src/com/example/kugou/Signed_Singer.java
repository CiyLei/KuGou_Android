package com.example.kugou;

import java.util.ArrayList;
import java.util.List;

import com.example.kugou.Adapter.Signed_Singer_Adapter;
import com.example.kugou.download_entities.Signed_SingerInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class Signed_Singer extends Activity implements OnClickListener {

	Signed_Singer_Adapter adapter;
	List<Signed_SingerInfo> signed_SingerInfos;
	
	ImageView img_hide_gs_close;
	ListView ls_hide_gs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_hide_gs);
		
		signed_SingerInfos = new ArrayList<Signed_SingerInfo>();
		img_hide_gs_close = (ImageView) findViewById(R.id.img_hide_gs_close);
		ls_hide_gs = (ListView) findViewById(R.id.ls_hide_gs);
		
		Signed_SingerInfo info = new Signed_SingerInfo();
		info.ImgID = R.drawable.test_songer01;
		info.singerInfo="薛之谦";
		info.Singer_Profile="台湾歌手";
		info.Time="2016-12-08";
		signed_SingerInfos.add(info);
		info = new Signed_SingerInfo();
		info.ImgID = R.drawable.test_songer02;
		info.singerInfo="周杰伦";
		info.Singer_Profile="哎哟,不错哦!";
		info.Time="2016-12-08";
		signed_SingerInfos.add(info);
		info = new Signed_SingerInfo();
		info.ImgID = R.drawable.test_songer03;
		info.singerInfo="李荣浩";
		info.Singer_Profile="中国流行男歌手";
		info.Time="2016-12-08";
		signed_SingerInfos.add(info);
		
		img_hide_gs_close.setOnClickListener(this);
		
		adapter = new Signed_Singer_Adapter(this, signed_SingerInfos);
		ls_hide_gs.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
	
}
