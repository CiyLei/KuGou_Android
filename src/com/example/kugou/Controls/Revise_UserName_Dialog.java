package com.example.kugou.Controls;

import java.net.URLEncoder;

import com.example.kugou.Home;
import com.example.kugou.R;
import com.example.kugou.Adapter.Login_Information_Adapter;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.DBdata.select_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 修改名称的Dialog
 * 
 * @author Administrator
 *
 */
public class Revise_UserName_Dialog extends Dialog implements OnClickListener,OnPostExecuteListener {

	Context context;
	Login_Information_Adapter adapter;
	EditText edit_addsonglist;
	TextView tv_addsonglist;
	
	public Revise_UserName_Dialog(Context context,Login_Information_Adapter adapter) {
		super(context);
		this.context=context;
		this.adapter=adapter;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater= LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.add_songlist_dialog, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		
		Button btn_addsonglist_no=(Button) view.findViewById(R.id.btn_addsonglist_no);
		Button btn_addsonglist_yes=(Button) view.findViewById(R.id.btn_addsonglist_yes);
		edit_addsonglist=(EditText) view.findViewById(R.id.edit_addsonglist);
		tv_addsonglist = (TextView) view.findViewById(R.id.tv_addsonglist);
		
		edit_addsonglist.setText(Home.user.name);
		edit_addsonglist.setSelection(edit_addsonglist.getText().toString().length());
		tv_addsonglist.setText("修改昵称");
		tv_addsonglist.setTextColor(context.getResources().getColor(R.color.login_top_color));
		
		btn_addsonglist_no.setOnClickListener(this);
		btn_addsonglist_yes.setOnClickListener(this);
		
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
		dialogWindow.setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.btn_addsonglist_no:
			this.dismiss();
			break;

		case R.id.btn_addsonglist_yes:
			
			String url = Home.postURL
			+ "user="
			+ Home.user.userID
			+ "&pass="
			+ Home.user.pass
			+ "&operat=update&table=Users&bt=Name&value='"
			+ URLEncoder.encode(edit_addsonglist.getText().toString())
			+ "'";
			
			new dataLoad(this).execute(url);
			
			break;
			
		}
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(post_return_analysis.getIsSuccess(result)){
			Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
			Home.user.name = edit_addsonglist.getText().toString();
			adapter.notifyDataSetChanged();
		}
		else{
			Log.e("失败原因", post_return_analysis.getMsg(result));
			Toast.makeText(context,"修改" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
		}
		this.dismiss();
	}
}
