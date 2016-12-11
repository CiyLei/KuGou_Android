package com.example.kugou.Controls;

import java.net.URLEncoder;
import java.util.List;

import com.example.kugou.Home;
import com.example.kugou.R;
import com.example.kugou.Adapter.Login_Information_Adapter;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.DBdata.post_return_analysis;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
/**
 * 修改性别的漂浮窗
 * 
 * @author Administrator
 *
 */
public class Revise_Sex_PopupWindow extends PopupWindow implements OnClickListener,OnPostExecuteListener {
	
	Context context;
	Login_Information_Adapter adapter;
	RelativeLayout rela_sex_man,rela_sex_woman;
	ImageView img_sex_man,img_sex_woman;
	Button btn_sex;
	boolean sex;
	
	public Revise_Sex_PopupWindow(Context context,Login_Information_Adapter adapter){
		this.context = context;
		this.adapter=adapter;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.revise_sex_layout, null);
		setContentView(view);
		
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setAnimationStyle(R.animator.down_to_up);
        ColorDrawable dw = new ColorDrawable(0x00); 
		setBackgroundDrawable(dw);
		
		rela_sex_man = (RelativeLayout) view.findViewById(R.id.rela_sex_man);
		rela_sex_woman = (RelativeLayout) view.findViewById(R.id.rela_sex_woman);
		img_sex_man = (ImageView) view.findViewById(R.id.img_sex_man);
		img_sex_woman = (ImageView) view.findViewById(R.id.img_sex_woman);
		btn_sex = (Button) view.findViewById(R.id.btn_sex);
		
		if(Home.user.sex){
			img_sex_man.setVisibility(View.VISIBLE);
			img_sex_woman.setVisibility(View.INVISIBLE);
		}
		else {
			img_sex_man.setVisibility(View.INVISIBLE);
			img_sex_woman.setVisibility(View.VISIBLE);
		}
		
		rela_sex_man.setOnClickListener(this);
		rela_sex_woman.setOnClickListener(this);
		btn_sex.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rela_sex_man:
			
			sex=true;
			
			String url = Home.postURL
			+ "user="
			+ Home.user.userID
			+ "&pass="
			+ Home.user.pass
			+ "&operat=update&table=Users&bt=Sex&value='1'";
			
			new dataLoad(this).execute(url);
			
			break;
		case R.id.rela_sex_woman:

			sex=false;
			
			url = Home.postURL
			+ "user="
			+ Home.user.userID
			+ "&pass="
			+ Home.user.pass
			+ "&operat=update&table=Users&bt=Sex&value='0'";
			
			new dataLoad(this).execute(url);
			
			break;
			
		default:
			dismiss();
			break;
		}
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(post_return_analysis.getIsSuccess(result)){
			Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
			Home.user.sex=sex;
			adapter.notifyDataSetChanged();
			dismiss();
		}
		else {
			Log.e("失败原因", post_return_analysis.getMsg(result));
			Toast.makeText(context,"修改" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
		}
	}
	
}
