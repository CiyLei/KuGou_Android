package com.example.kugou.Adapter;

import java.util.List;

import com.example.kugou.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClassIfication_Adapter extends BaseAdapter {

	List<String> typeStrings;
	Context context;
	
	public ClassIfication_Adapter(List<String> typeStrings,Context context){
		this.typeStrings= typeStrings;
		this.context =context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return typeStrings.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return typeStrings.get(position);
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
		if(convertView == null){
			viewContext = new ViewContext();
			convertView = LayoutInflater.from(context).inflate(R.layout.home_hide_gs__adapter_2, null);
			viewContext.tv_home_hide_gs_adapter_num = (TextView) convertView.findViewById(R.id.tv_home_hide_gs_adapter_num);
			convertView.setTag(viewContext);
		}
		else
			viewContext = (ViewContext) convertView.getTag();
		if(position == 0){
			viewContext.tv_home_hide_gs_adapter_num.setTextColor(context.getResources().getColor(R.color.login_top_color));
			viewContext.tv_home_hide_gs_adapter_num.getPaint().setFakeBoldText(true);
		}
		viewContext.tv_home_hide_gs_adapter_num.setText(typeStrings.get(position));
		return convertView;
	}

	class ViewContext{
		TextView tv_home_hide_gs_adapter_num;
	}
	
}
