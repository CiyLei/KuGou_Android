package com.example.kugou.Adapter;

import java.util.List;

import com.example.kugou.R;
import com.example.kugou.download_entities.NewsInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class News_Adapter extends BaseAdapter {

	Context context;
	List<NewsInfo> newsInfos;
	
	public News_Adapter(Context context,List<NewsInfo> newsInfos){
		this.context=context;
		this.newsInfos=newsInfos;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return newsInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewContext viewContext=null;
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.home_hide_xx_adapter, null);
			viewContext=new ViewContext();
			viewContext.img_home_hide_xx = (ImageView) convertView.findViewById(R.id.img_home_hide_xx);
			viewContext.tv_home_hide_xx_title = (TextView) convertView.findViewById(R.id.tv_home_hide_xx_title);
			viewContext.tv_home_hide_xx_info = (TextView) convertView.findViewById(R.id.tv_home_hide_xx_info);
			viewContext.tv_home_hide_xx_time = (TextView) convertView.findViewById(R.id.tv_home_hide_xx_time);
			convertView.setTag(viewContext);
		}else{
			viewContext=(ViewContext) convertView.getTag();
		}
		viewContext.img_home_hide_xx.setImageResource(newsInfos.get(position).ImgID);
		viewContext.tv_home_hide_xx_title.setText(newsInfos.get(position).NewsTitle);
		viewContext.tv_home_hide_xx_info.setText(newsInfos.get(position).NewsInfo);
		viewContext.tv_home_hide_xx_time.setText(newsInfos.get(position).NewsTime);
		return convertView;
	}

	class ViewContext{
		ImageView img_home_hide_xx;
		TextView tv_home_hide_xx_title,tv_home_hide_xx_info,tv_home_hide_xx_time;
	}
}
