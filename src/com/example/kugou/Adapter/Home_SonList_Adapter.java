package com.example.kugou.Adapter;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kugou.Home;
import com.example.kugou.R;
import com.example.kugou.Controls.Edit_SongListName_Dialog;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;

import dalvik.bytecode.Opcodes;

import android.R.integer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 获取歌单的Adapter
 * 
 * @author Administrator
 *
 */
public class Home_SonList_Adapter extends BaseAdapter implements OnClickListener,OnPostExecuteListener{

	Context context;
	List<String> SongLists;
	List<Search_information> songCollects;
	ListView listView;
	
	Map<String, Integer> SongNum;
	
	int Operating = -1;
	final int Post_Edit = 0;
	final int Post_Del = 1;
	
	String DelName = "";
	
	public Home_SonList_Adapter(Context context,List<String> SongLists,List<Search_information> songCollects,ListView listView){
		this.context=context;
		this.SongLists = SongLists;
		this.songCollects = songCollects;
		this.listView=listView;
		SongNum = new HashMap<String, Integer>();
		for (String SongList : SongLists) {
			SongNum.put(SongList, 0);
		}
		for (Search_information Search_information : songCollects) {
			Integer num = SongNum.get(Search_information.songList);
			if(SongNum.get(Search_information.songList) != null)
				SongNum.put(Search_information.songList, ++num);
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return SongLists.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position == SongLists.size())
			position--;
		return SongLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(position == SongLists.size()){
			convertView = LayoutInflater.from(context).inflate(R.layout.black_layout, null);
			return convertView;
		}
		
		ViewHolder viewHolder = new ViewHolder();
		
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.home_main_songlist_adapter, null);
			viewHolder.img_songlist_adapter = (ImageView) convertView.findViewById(R.id.img_songlist_adapter);
			viewHolder.img_songlist_adapter_right = (ImageView) convertView.findViewById(R.id.img_songlist_adapter_right);
			viewHolder.tv_songlist_adapter = (TextView) convertView.findViewById(R.id.tv_songlist_adapter);
			viewHolder.tv_songlist_adapter_item_num = (TextView) convertView.findViewById(R.id.tv_songlist_adapter_item_num);
			viewHolder.line_songlist_hide = (LinearLayout) convertView.findViewById(R.id.line_songlist_hide);
			viewHolder.radio_songlist_bj = (RadioButton) convertView.findViewById(R.id.radio_songlist_bj);
			viewHolder.radio_songlist_sc = (RadioButton) convertView.findViewById(R.id.radio_songlist_sc);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(SongLists.get(position).equals("我喜欢")){
			viewHolder.img_songlist_adapter.setImageResource(R.drawable.songlist_wxh);
		}
		viewHolder.tv_songlist_adapter.setText(SongLists.get(position));
		viewHolder.img_songlist_adapter_right.setTag(position);
		viewHolder.radio_songlist_bj.setTag(position);
		viewHolder.radio_songlist_sc.setTag(position);
		viewHolder.tv_songlist_adapter_item_num.setText(SongNum.get(SongLists.get(position)) + "首");
		
		viewHolder.img_songlist_adapter_right.setOnClickListener(this);
		viewHolder.radio_songlist_bj.setOnClickListener(this);
		viewHolder.radio_songlist_sc.setOnClickListener(this);
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView img_songlist_adapter,img_songlist_adapter_right;
		TextView tv_songlist_adapter,tv_songlist_adapter_item_num;
		LinearLayout line_songlist_hide;
		RadioButton radio_songlist_bj,radio_songlist_sc;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		int position = (int) v.getTag();
		
		switch (v.getId()) {
		case R.id.img_songlist_adapter_right:

			View view = listView.getChildAt(position - listView.getFirstVisiblePosition());
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			
			if(viewHolder.line_songlist_hide.getVisibility() == View.VISIBLE){
				viewHolder.img_songlist_adapter_right.setRotation(0);
				viewHolder.line_songlist_hide.setVisibility(View.GONE);
			}
			else{
				viewHolder.line_songlist_hide.setVisibility(View.VISIBLE);
				viewHolder.img_songlist_adapter_right.setRotation(180);
			}
			
			break;

		case R.id.radio_songlist_bj:
			if(SongLists.get(position).equals("我喜欢")){
				Toast.makeText(context, "不能编辑", Toast.LENGTH_SHORT).show();
				return;
			}
			Edit_SongListName_Dialog dialog = new Edit_SongListName_Dialog(context, SongLists.get(position));
			dialog.show();
			break;
			
		case R.id.radio_songlist_sc:
			if(SongLists.get(position).equals("我喜欢")){
				Toast.makeText(context, "不能删除", Toast.LENGTH_SHORT).show();
				return;
			}
			Operating = Post_Del;
			DelName = SongLists.get(position);
			String url = Home.postURL + "user=" + Home.user.userID 
					+ "&pass=" + Home.user.pass + "&operat=del&table=SongList&tj=Name&tj_value='" 
					+ URLEncoder.encode(SongLists.get(position)) + "'";
			new dataLoad(Home_SonList_Adapter.this).execute(url);
			break;
			
		}
		
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		switch (Operating) {
		case Post_Del:
			
			if(post_return_analysis.getIsSuccess(result)){
				Toast.makeText(context, "删除" + post_return_analysis.getIsSuccessTxt(result), Toast.LENGTH_SHORT).show();
				if(!DelName.isEmpty()){
					Home.user.songList.remove(DelName);
					SongLists.remove(DelName);
					DelName = "";
				}
				notifyDataSetChanged();
			}
			else{
				Log.e("失败原因", post_return_analysis.getMsg(result));
				Toast.makeText(context,"删除" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
			}
			
			break;
		case Post_Edit:
			
			break;
		}
	}
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		if(position == SongLists.size())
			return false;
		return super.isEnabled(position);
	}

}
