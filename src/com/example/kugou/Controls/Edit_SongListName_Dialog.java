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
 * �༭�û����Ƶ�Dialog
 * @author Administrator
 *
 */
public class Edit_SongListName_Dialog extends Dialog implements OnClickListener,OnPostExecuteListener {

	Context context;
	EditText edit_addsonglist;
	String OldName;
	
	int Operating = -1;
	final int Post_EditSongListName = 0;
	final int Post_EditSongCollectName = 1;
	
	public Edit_SongListName_Dialog(Context context,String OldName) {
		super(context);
		this.context=context;
		this.OldName = OldName;
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
		
		TextView tv_addsonglist = (TextView) view.findViewById(R.id.tv_addsonglist);
		Button btn_addsonglist_no=(Button) view.findViewById(R.id.btn_addsonglist_no);
		Button btn_addsonglist_yes=(Button) view.findViewById(R.id.btn_addsonglist_yes);
		edit_addsonglist=(EditText) view.findViewById(R.id.edit_addsonglist);
		
		btn_addsonglist_no.setOnClickListener(this);
		btn_addsonglist_yes.setOnClickListener(this);
		
		tv_addsonglist.setText("�༭�赥����");
		edit_addsonglist.setText(OldName);
		edit_addsonglist.setSelection(edit_addsonglist.getText().toString().length());
		
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // ��ȡ��Ļ������
		lp.width = (int) (d.widthPixels * 0.9); // �߶�����Ϊ��Ļ��0.6
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
			
			if(edit_addsonglist.getText().toString().equals(OldName)){
				Toast.makeText(context, "�����벻ͬ������", Toast.LENGTH_SHORT).show();
				return;
			}
			Operating = Post_EditSongListName;
			String url = Home.postURL
					+ "user="
					+ Home.user.userID
					+ "&pass="
					+ Home.user.pass
					+ "&operat=update&table=SongList&bt=Name&value='"
					+ URLEncoder.encode(edit_addsonglist.getText().toString())
					+ "'&tj=Name&tj_value='"
					+ URLEncoder.encode(OldName) +"'";
			
			new dataLoad(this).execute(url);
			
			break;
			
		}
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		switch (Operating) {
		case Post_EditSongListName:
			if(post_return_analysis.getIsSuccess(result)){
				Operating = Post_EditSongCollectName;
				String url = Home.postURL
					+ "user="
					+ Home.user.userID
					+ "&pass="
					+ Home.user.pass
					+ "&operat=update&table=SongCollect&bt=SongList&value='"
					+ URLEncoder.encode(edit_addsonglist.getText().toString())
					+ "'&tj=SongList&tj_value='"
					+ URLEncoder.encode(OldName) +"'";
				new dataLoad(this).execute(url);
			}
			else{
				Log.e("ʧ��ԭ��", post_return_analysis.getMsg(result));
				Toast.makeText(context,"�޸�" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
			}
			break;
		case Post_EditSongCollectName:
			Operating = -1;
			if(post_return_analysis.getIsSuccess(result)){
				Toast.makeText(context, "�޸�"+ post_return_analysis.getIsSuccessTxt(result), Toast.LENGTH_SHORT).show();
				for (int i = 0; i < Home.user.songList.size(); i++) {
					if(Home.user.songList.get(i).equals(OldName))
						Home.user.songList.set(i, edit_addsonglist.getText().toString());
				}
			}
			else{
				Log.e("ʧ��ԭ��", post_return_analysis.getMsg(result));
				Toast.makeText(context,"�޸�" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
			}
			this.dismiss();
			break;
		}
	}
}
