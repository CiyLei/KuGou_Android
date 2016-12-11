package com.example.kugou.Controls;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.example.kugou.Home;
import com.example.kugou.Login;
import com.example.kugou.R;
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
 * 添加歌单的Dialog
 * @author Administrator
 *
 */
public class Add_SongList_Dialog extends Dialog implements OnClickListener,OnPostExecuteListener {

	Context context;
	EditText edit_addsonglist;
	
	public Add_SongList_Dialog(Context context) {
		super(context);
		this.context=context;
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
			+ "&operat=insert&table=SongList&values='"
			+ Home.user.userID
			+ "','"
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
			Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
			if(Home.user.songList == null)
				Home.user.songList = new ArrayList<String>();
			Home.user.songList.add(edit_addsonglist.getText().toString());
		}
		else{
			Log.e("失败原因", post_return_analysis.getMsg(result));
			Toast.makeText(context,"添加" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
		}
		this.dismiss();
	}
}
