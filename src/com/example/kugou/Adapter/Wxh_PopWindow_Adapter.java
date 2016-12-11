package com.example.kugou.Adapter;

import com.example.kugou.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 更新播放顺序的Adapter
 * 
 * @author Administrator
 *
 */
public class Wxh_PopWindow_Adapter extends BaseAdapter {

	int[] img;
	String[] txt;
	int select_index;
	LayoutInflater mInflater;
	
	public Wxh_PopWindow_Adapter(Context context,int[] img,String[] txt, int select_index){
		mInflater=LayoutInflater.from(context);
		this.txt=txt;
		this.img=img;
		this.select_index=select_index;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return txt.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return txt[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		convertView=mInflater.inflate(R.layout.wxh_popwindow_adapter, null);
		ImageView img_wxh_pop_select = (ImageView) convertView.findViewById(R.id.img_wxh_pop_select);
		ImageView img_wxh_pop_img = (ImageView) convertView.findViewById(R.id.img_wxh_pop_img);
		TextView tv_wxh_pop_txt = (TextView) convertView.findViewById(R.id.tv_wxh_pop_txt);
		
		if(position == select_index)
			img_wxh_pop_select.setVisibility(View.VISIBLE);
		else {
			img_wxh_pop_select.setVisibility(View.INVISIBLE);
		}
		img_wxh_pop_img.setImageResource(img[position]);
		tv_wxh_pop_txt.setText(txt[position]);
		
		return convertView;
	}

}
