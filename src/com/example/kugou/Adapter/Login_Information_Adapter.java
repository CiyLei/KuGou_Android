package com.example.kugou.Adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.kugou.Home;
import com.example.kugou.R;
import com.example.kugou.DBdata.User;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 用户信息的Adapter
 * 
 * getView判断点击了哪行,从而进行不同的操作
 * 
 * @author Administrator
 *
 */
public class Login_Information_Adapter extends BaseAdapter{

	LayoutInflater mInflater;
	TextView tv_login_information;
	ImageView img_login_information_sex;
	TextView tv_information_key;
	TextView tv_information_value;
	TextView tv_information_line_txt;
	
	public OnLogoutClickListener OnLogoutClick;
	
	String[] key=new String[]{"top","","帐号","乐龄","昵称","性别","修改密码","修改密码","退出登录"};
	String[] value;
	
	public interface OnLogoutClickListener{
		void onClick();
	}
	
	public Login_Information_Adapter(Context context){
		mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return key.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return key[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String sex="男";
		if(!Home.user.sex)
			sex="女";
		int age=0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			
			long to = df.parse(Home.user.registeredtime).getTime();
		    long from = df.parse(df.format(System.currentTimeMillis())).getTime();
		    age = (int) ((to - from) / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		value=new String[]{"","",Home.user.userID, Math.abs(age) + "天",Home.user.name,sex,"修改密码","","退出登录"};
		if(position == 0){
			
			convertView = mInflater.inflate(
					R.layout.login_information_adpter_top, null);

			tv_login_information = (TextView) convertView
					.findViewById(R.id.tv_login_information);
			img_login_information_sex = (ImageView) convertView
					.findViewById(R.id.img_login_information_sex);

			tv_login_information.setText(Home.user.name);
			if (Home.user.sex)
				img_login_information_sex
						.setImageResource(R.drawable.pic_registered_man_select);
			else
				img_login_information_sex
						.setImageResource(R.drawable.pic_registered_women_select);
			
		}
		else if(position == 1 || position ==6){
			
			convertView = mInflater.inflate(
					R.layout.login_information_adpter_line, null);
			
			tv_information_line_txt=(TextView) convertView.findViewById(R.id.tv_information_line_txt);
			
			if(key[position] != ""){
				tv_information_line_txt.setVisibility(View.VISIBLE);
				tv_information_line_txt.setText(value[position]);
			}
			else {
				tv_information_line_txt.setVisibility(View.VISIBLE);
				tv_information_line_txt.getLayoutParams().height=1;
			}
			
		}
		else if(position == 8){
			convertView = mInflater.inflate(
					R.layout.login_information_adpter_bottom, null);
			Button btn_information_close=(Button) convertView.findViewById(R.id.btn_information_close);
			btn_information_close.setText(value[position]);
			btn_information_close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					OnLogoutClick.onClick();
				}
			});
		}
		else{
			
			convertView = mInflater.inflate(
					R.layout.login_information_adpter, null);
			
			tv_information_key=(TextView) convertView.findViewById(R.id.tv_information_key);
			tv_information_value=(TextView) convertView.findViewById(R.id.tv_information_value);
			
			tv_information_key.setText(key[position]);
			tv_information_value.setText(value[position]);
			
		}
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		if(position == 0 || position == 1 || position ==6)
			return false;
		return super.isEnabled(position);
	}
}
