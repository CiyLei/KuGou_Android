package com.example.kugou.Adapter;

import java.util.List;

import com.example.kugou.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Home_Hide_ZP_Adapter extends BaseAdapter {

	Context context;
	List<String> names,times;
	
	public Home_Hide_ZP_Adapter(Context context,List<String> names,List<String> times){
		this.context=context;
		this.names=names;
		this.times=times;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return names.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return names.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.home_hide_zp_adapter, null);
			viewContext.tv_home_hide_zp_name=(TextView) convertView.findViewById(R.id.tv_home_hide_zp_name);
			viewContext.tv_home_hide_zp_time=(TextView) convertView.findViewById(R.id.tv_home_hide_zp_time);
			viewContext.relative_zp=(RelativeLayout) convertView.findViewById(R.id.relative_zp);
			convertView.setTag(viewContext);
		}else{
			viewContext=(ViewContext) convertView.getTag();
		}
		
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		// 获取窗体
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		// 获取显示窗体的测量值给outMetrics
		viewContext.relative_zp.getLayoutParams().width = outMetrics.widthPixels + 200;
		
		viewContext.tv_home_hide_zp_name.setText(names.get(position));
		viewContext.tv_home_hide_zp_time.setText(times.get(position));
		return convertView;
	}
	
	public int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
	
	class ViewContext{
		TextView tv_home_hide_zp_name,tv_home_hide_zp_time;
		RelativeLayout relative_zp;
	}

}
