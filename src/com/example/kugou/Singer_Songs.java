package com.example.kugou;

import java.util.ArrayList;
import java.util.List;

import com.example.kugou.Adapter.Signed_Singer_Adapter;
import com.example.kugou.Adapter.Singer_Songs_Adapter;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.download_entities.Signed_SingerInfo;
import com.example.kugou.download_entities.Singer_SongsInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Singer_Songs extends Activity implements OnClickListener,OnItemClickListener,OnPostExecuteListener{

	Singer_Songs_Adapter adapter;
	List<Signed_SingerInfo> signed_SingerInfos;
	
	ImageView img_singer_close,img_home_singer;
	TextView tv_singer_title;
	ListView ls_home_singer;
	Singer_SongsInfo singer_SongsInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_singer);
		img_singer_close = (ImageView) findViewById(R.id.img_singer_close);
		tv_singer_title = (TextView) findViewById(R.id.tv_singer_title);
		ls_home_singer = (ListView) findViewById(R.id.ls_home_singer);
		img_home_singer = (ImageView) findViewById(R.id.img_home_singer);
		singer_SongsInfo = (Singer_SongsInfo) getIntent().getSerializableExtra("Singer_Songs");
		
		img_singer_close.setOnClickListener(this);
		ls_home_singer.setOnItemClickListener(this);
		
		tv_singer_title.setText(singer_SongsInfo.Singer_Name);
		img_home_singer.setImageResource(singer_SongsInfo.Singer_Pic);
		
		signed_SingerInfos = new ArrayList<Signed_SingerInfo>();
		Signed_SingerInfo info = new Signed_SingerInfo();
		if(tv_singer_title.getText().equals("ÖÜ½ÜÂ×")){
			info.ImgID = R.drawable.test_songer02;
			info.singerInfo="°®ÔÚÎ÷ÔªÇ°";
			info.Singer_Profile="ÖÜ½ÜÂ×";
			info.Time="2016-12-08";
			signed_SingerInfos.add(info);
		}else if(tv_singer_title.getText().equals("Ñ¦Ö®Ç«")){
			info.ImgID = R.drawable.test_songer01;
			info.singerInfo="³ó°Ë¹Ö";
			info.Singer_Profile="Ñ¦Ö®Ç«";
			info.Time="2016-12-08";
			signed_SingerInfos.add(info);
		}
		adapter = new Singer_Songs_Adapter(this, signed_SingerInfos);
		ls_home_singer.setAdapter(adapter);
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
		if(tv_singer_title.getText().equals("ÖÜ½ÜÂ×"))
			new dataLoad(Singer_Songs.this).execute("http://music.baidu.com/data/music/links?songIds=17780102");
		else if(tv_singer_title.getText().equals("Ñ¦Ö®Ç«"))
			new dataLoad(Singer_Songs.this).execute("http://music.baidu.com/data/music/links?songIds=87967607");
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		Search_information search_information = new Search_information();
		search_information = post_return_analysis.getSearch_information(result);
		playMusic(MPService.MusicPlayer);
		List<Search_information> search_informations = new ArrayList<Search_information>();
		search_informations.add(search_information);
		MPService.SongPosition = MPService.SongListAdd(search_informations,0);
	}
	
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(Singer_Songs.this, MPService.class);  
        startService(intent);  
    }  
}
