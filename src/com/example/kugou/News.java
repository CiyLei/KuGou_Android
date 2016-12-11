package com.example.kugou;

import java.util.ArrayList;
import java.util.List;

import com.example.kugou.Adapter.News_Adapter;
import com.example.kugou.download_entities.NewsInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class News extends Activity implements OnClickListener {

	ImageView img_hide_xx_close;
	ListView ls_hide_xx;
	News_Adapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_hide_xx);
		
		img_hide_xx_close = (ImageView) findViewById(R.id.img_hide_xx_close);
		ls_hide_xx = (ListView) findViewById(R.id.ls_hide_xx);
		
		img_hide_xx_close.setOnClickListener(this);
		
		List<NewsInfo> newsInfos = new ArrayList<NewsInfo>();
		NewsInfo newsInfo = new NewsInfo();
		newsInfo.ImgID=R.drawable.test_songer02;
		newsInfo.NewsTitle="我是Jay";
		newsInfo.NewsInfo="我为自己代言";
		newsInfo.NewsTime="2016-03-19 14:38";
		newsInfos.add(newsInfo);
		adapter=new News_Adapter(this, newsInfos);
		ls_hide_xx.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
	
}
