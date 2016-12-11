package com.example.kugou;

import java.util.ArrayList;
import java.util.List;

import com.example.kugou.Adapter.ClassIfication_Adapter;
import com.example.kugou.Adapter.Signed_Singer_Adapter;
import com.example.kugou.download_entities.Signed_SingerInfo;
import com.example.kugou.download_entities.Singer_SongsInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Recommend_Singer extends Activity implements OnClickListener,OnItemClickListener {

	Signed_Singer_Adapter adapter;
	ClassIfication_Adapter adapter2;
	List<Signed_SingerInfo> signed_SingerInfos;
	List<String> typeStrings;
	List<Singer_SongsInfo> singer_SongsInfos;
	
	ImageView img_hide_gs_close;
	ListView ls_hide_gs;
	ListView ls_hide_num;
	TextView tv_hide_gs_title;
	
	int ls_hide_num_ON_num = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_hide_gs);
		
		signed_SingerInfos = new ArrayList<Signed_SingerInfo>();
		img_hide_gs_close = (ImageView) findViewById(R.id.img_hide_gs_close);
		ls_hide_gs = (ListView) findViewById(R.id.ls_hide_gs);
		ls_hide_num = (ListView) findViewById(R.id.ls_hide_num);
		tv_hide_gs_title = (TextView) findViewById(R.id.tv_hide_gs_title);
		
		Signed_SingerInfo info = new Signed_SingerInfo();
		info = new Signed_SingerInfo();
		
		info.ImgID = R.drawable.test_songer02;
		info.singerInfo="周杰伦";
		info.Singer_Profile="哎哟,不错哦!";
		info.Time="2016-12-08";
		signed_SingerInfos.add(info);
		info = new Signed_SingerInfo();
		info.ImgID = R.drawable.test_songer01;
		info.singerInfo="薛之谦";
		info.Singer_Profile="中国流行男歌手";
		info.Time="2016-12-08";
		signed_SingerInfos.add(info);
		typeStrings = new ArrayList<String>();
		typeStrings.add("热门");
		typeStrings.add("最近");
		typeStrings.add("主题");
		typeStrings.add("场景");
		typeStrings.add("心情");
		typeStrings.add("风格");
		typeStrings.add("人群");
		typeStrings.add("语言");
		typeStrings.add("DJ");
		singer_SongsInfos = new ArrayList<Singer_SongsInfo>();
		Singer_SongsInfo singer_SongsInfo = new Singer_SongsInfo();
		singer_SongsInfo.Singer_Pic = R.drawable.test_songer02;
		singer_SongsInfo.Singer_Name = "周杰伦";
		singer_SongsInfos.add(singer_SongsInfo);
		singer_SongsInfo = new Singer_SongsInfo();
		singer_SongsInfo.Singer_Pic = R.drawable.test_songer01;
		singer_SongsInfo.Singer_Name = "薛之谦";
		singer_SongsInfos.add(singer_SongsInfo);
		
		tv_hide_gs_title.setText("推荐歌手");
		ls_hide_num.setVisibility(View.VISIBLE);
		
		img_hide_gs_close.setOnClickListener(this);
		
		adapter = new Signed_Singer_Adapter(this, signed_SingerInfos);
		adapter2 = new ClassIfication_Adapter(typeStrings,Recommend_Singer.this);
		ls_hide_gs.setAdapter(adapter);
		ls_hide_num.setAdapter(adapter2);
		ls_hide_gs.setOnItemClickListener(this);
		ls_hide_num.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.ls_hide_num:
			View view_ON = ls_hide_num.getChildAt(ls_hide_num_ON_num);
			View view_xz = ls_hide_num.getChildAt(position);
			TextView tv_home_hide_gs_adapter_num = (TextView) view_ON.findViewById(R.id.tv_home_hide_gs_adapter_num);
			tv_home_hide_gs_adapter_num.setTextColor(getResources().getColor(R.color.black));
			tv_home_hide_gs_adapter_num.getPaint().setFakeBoldText(false);
			tv_home_hide_gs_adapter_num = (TextView) view_xz.findViewById(R.id.tv_home_hide_gs_adapter_num);
			tv_home_hide_gs_adapter_num.setTextColor(getResources().getColor(R.color.login_top_color));
			tv_home_hide_gs_adapter_num.getPaint().setFakeBoldText(true);
			
			ls_hide_num_ON_num = position;
			break;
		case R.id.ls_hide_gs:
			Intent intent = new Intent(Recommend_Singer.this, Singer_Songs.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("Singer_Songs", singer_SongsInfos.get(position));
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}
	
}
