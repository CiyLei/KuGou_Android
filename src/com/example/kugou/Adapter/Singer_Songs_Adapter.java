package com.example.kugou.Adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kugou.R;
import com.example.kugou.Adapter.Signed_Singer_Adapter.ViewContext;
import com.example.kugou.download_entities.Signed_SingerInfo;

public class Singer_Songs_Adapter extends BaseAdapter {
	Context context;
	List<Signed_SingerInfo> signed_SingerInfos;
	
	public Singer_Songs_Adapter(Context context,List<Signed_SingerInfo> signed_SingerInfos){
		this.context=context;
		this.signed_SingerInfos=signed_SingerInfos;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return signed_SingerInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return signed_SingerInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewContext viewContext = null;
		if(convertView==null){
			viewContext = new ViewContext();
			convertView=LayoutInflater.from(context).inflate(R.layout.home_singer_songs_adapter, null);
			viewContext.img_singer_songs_gs = (ImageView) convertView.findViewById(R.id.img_singer_songs_gs);
			viewContext.tv_singer_songs_gs_user = (TextView) convertView.findViewById(R.id.tv_singer_songs_gs_user);
			viewContext.tv_singer_songs_gs_jj = (TextView) convertView.findViewById(R.id.tv_singer_songs_gs_jj);
			viewContext.tv_home_singer_songs_gs_time = (TextView) convertView.findViewById(R.id.tv_home_singer_songs_gs_time);
			convertView.setTag(viewContext);
		}else {
			viewContext = (ViewContext) convertView.getTag();
		}
		viewContext.img_singer_songs_gs.setImageResource(R.drawable.home_hide_zp_play);
		viewContext.tv_singer_songs_gs_user.setText(signed_SingerInfos.get(position).singerInfo);
		viewContext.tv_singer_songs_gs_jj.setText(signed_SingerInfos.get(position).Singer_Profile);
		viewContext.tv_home_singer_songs_gs_time.setText(signed_SingerInfos.get(position).Time);
		return convertView;
	}
	
	class ViewContext{
		ImageView img_singer_songs_gs;
		TextView tv_singer_songs_gs_user,tv_singer_songs_gs_jj,tv_home_singer_songs_gs_time;
	}
}
