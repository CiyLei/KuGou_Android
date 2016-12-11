package com.example.kugou.Adapter;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.kugou.Fragment_ting;
import com.example.kugou.Fragment_wxh;
import com.example.kugou.Home;
import com.example.kugou.MPService;
import com.example.kugou.R;
import com.example.kugou.Search;
import com.example.kugou.SongPlayer;
import com.example.kugou.Controls.Song_Add_PopupWindow;
import com.example.kugou.Controls.Song_Info_PopupWindow;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.DB.songlist.ThreadOADImpl_songlist;
import com.example.download.DownLoadService;

import android.R.integer;
import android.R.mipmap;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 我喜欢,搜索,歌单歌曲的Adapter
 * 
 * onClick() ----  onPostExecute()获取歌曲信息  ----  on_Post_Operating具体操作
 * 
 * @author Administrator
 *
 */
public class Wxh_Adapter extends BaseAdapter implements OnClickListener,OnPostExecuteListener {

	Map<Integer, Boolean> Open=new HashMap<Integer, Boolean>();

	Context mContext;
	LayoutInflater mInflater;
	List<Search_information> msearch_informations;
	ListView ls;
	View view;
	
	public boolean isSearch;
	public boolean isLocal;
	public String SongList;
	
	int Post_Operating = 0;
	final int Post_Add = 1;
	final int Post_Info = 2;
	final int Post_Del = 3;
	final int Post_AddList = 4;
	final int Post_Down = 5;
	
	public Wxh_Adapter(Context context,List<Search_information> search_informations,View view,ListView ls){
		this.mContext = context;
		this.msearch_informations = search_informations;
		this.view = view;
		this.ls = ls;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return msearch_informations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return msearch_informations.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		
		if(convertView == null){
			viewHolder=new ViewHolder();
			mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.wxh_adpater, null);
			viewHolder.tv_wxh_adpater = (TextView) convertView.findViewById(R.id.tv_wxh_adpater);
			viewHolder.hidelLayout=(RelativeLayout) convertView.findViewById(R.id.wxh_hide);
			viewHolder.img_wxh_adpater_left=(ImageView) convertView.findViewById(R.id.img_wxh_adpater_left);
			viewHolder.img_wxh_adpater_right = (ImageView) convertView.findViewById(R.id.img_wxh_adpater_right);
			viewHolder.radio_wxh_hide_down=(RadioButton) convertView.findViewById(R.id.radio_wxh_hide_down);
			viewHolder.radio_wxh_hide_add=(RadioButton) convertView.findViewById(R.id.radio_wxh_hide_add);
			viewHolder.radio_wxh_hide_songinfo=(RadioButton) convertView.findViewById(R.id.radio_wxh_hide_songinfo);
			viewHolder.radio_wxh_hide_delete=(RadioButton) convertView.findViewById(R.id.radio_wxh_hide_delete);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		//在歌手不等于空的情况下	歌名=歌名-歌手
		if(msearch_informations.get(position).artistName == null || msearch_informations.get(position).artistName.isEmpty())
			viewHolder.tv_wxh_adpater.setText(msearch_informations.get(position).songName);
		else{
			viewHolder.tv_wxh_adpater.setText(msearch_informations.get(position).artistName + "-" + msearch_informations.get(position).songName);
		}
		
		viewHolder.img_wxh_adpater_left.setTag(position);
		viewHolder.img_wxh_adpater_right.setTag(position);
		viewHolder.radio_wxh_hide_down.setTag(position);
		viewHolder.radio_wxh_hide_add.setTag(position);
		viewHolder.radio_wxh_hide_songinfo.setTag(position);
		viewHolder.radio_wxh_hide_delete.setTag(position);
		viewHolder.hidelLayout.setVisibility(View.GONE);
		
		viewHolder.img_wxh_adpater_left.setOnClickListener(this);
		viewHolder.img_wxh_adpater_right.setOnClickListener(this);
		viewHolder.radio_wxh_hide_down.setOnClickListener(this);
		viewHolder.radio_wxh_hide_add.setOnClickListener(this);
		viewHolder.radio_wxh_hide_songinfo.setOnClickListener(this);
		viewHolder.radio_wxh_hide_delete.setOnClickListener(this);
		
		if(Open.get(position) == null)
			Open.put(position, false);
		
		return convertView;
	}

	class ViewHolder{
		public TextView tv_wxh_adpater;
		public RelativeLayout hidelLayout;
		public ImageView img_wxh_adpater_left;
		public ImageView img_wxh_adpater_right;
		public RadioButton radio_wxh_hide_down;
		public RadioButton radio_wxh_hide_add;
		public RadioButton radio_wxh_hide_songinfo;
		public RadioButton radio_wxh_hide_delete;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		
		case R.id.img_wxh_adpater_right:
			//获取点击了哪行的控件
			Integer position = (Integer) ((ImageView)v).getTag();
			//获取那行的View
			View view = ls.getChildAt(position - ls.getFirstVisiblePosition());
			
			ViewHolder holder = (ViewHolder) view.getTag(); 
			//绑定控件
			holder.hidelLayout=(RelativeLayout) view.findViewById(R.id.wxh_hide);
			holder.img_wxh_adpater_right = (ImageView) view.findViewById(R.id.img_wxh_adpater_right);
			
			if(Open.get(position)){
				holder.hidelLayout.setVisibility(View.GONE);
				holder.img_wxh_adpater_right.setRotation(0);
			}
			else{
				holder.hidelLayout.setVisibility(View.VISIBLE);
				holder.img_wxh_adpater_right.setRotation(180);
			}
			
			Open.put(position, !Open.get(position));
			break;
			
		case R.id.radio_wxh_hide_down:
			
			Integer index = (Integer) ((RadioButton)v).getTag();
			Post_Operating = Post_Down;
			if(isSearch)
				new dataLoad(Wxh_Adapter.this).execute("http://music.baidu.com/data/music/links?songIds="+msearch_informations.get(index).songId);
			else if(isLocal)
				Toast.makeText(mContext, "已下载", Toast.LENGTH_SHORT).show();
			else
				SongPlayer.DownMusic(mContext, msearch_informations.get(index));
//				DownMusic(msearch_informations.get(index));
			break;

		case R.id.radio_wxh_hide_add:
			index = (Integer) ((RadioButton)v).getTag();
			Post_Operating = Post_Add;
			if(isSearch)
				new dataLoad(Wxh_Adapter.this).execute("http://music.baidu.com/data/music/links?songIds="+msearch_informations.get(index).songId);
			else
				on_Post_Operating(msearch_informations.get(index));
			break;

		case R.id.radio_wxh_hide_songinfo:
			index = (Integer) ((RadioButton)v).getTag();
			Post_Operating = Post_Info;
			if(isSearch)
				new dataLoad(Wxh_Adapter.this).execute("http://music.baidu.com/data/music/links?songIds="+msearch_informations.get(index).songId);
			else
				on_Post_Operating(msearch_informations.get(index));
			break;

		case R.id.radio_wxh_hide_delete:
			if(isSearch){
				//如果是搜索界面点击了删除的话,就只是隐藏而已
				index = (Integer) ((RadioButton)v).getTag();
				RelativeLayout hidelLayout=(RelativeLayout) ls.getChildAt(index).findViewById(R.id.rela_wxh_adapter);
				hidelLayout.setVisibility(View.GONE);
			}
			else if (isLocal) {
				//如果是本地音乐界面点击了删除的话,删除本地音乐
				index = (Integer) ((RadioButton)v).getTag();
				Search_information search_information = msearch_informations.get(index);
				File path = new File(DownLoadService.DOWNLOAD_PATH);
				File[] files = path.listFiles();
				for (File file : files) {
					String s =file.getName();
					String s1 =search_information.songId + "." + search_information.format;
					if(file.getName().equals(search_information.songId + "." + search_information.format)){
						file.delete();
						ThreadOADImpl_songlist impl = new ThreadOADImpl_songlist(mContext);
						impl.deleteSongInfo(search_information.songId);
						msearch_informations.remove(search_information);
						Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
						if(Fragment_ting.tv_songlist_num != null){
							List<Search_information> search_informations = impl.getAllFinishSongInfo();
							Fragment_ting.tv_songlist_num.setText(search_informations.size() + "首");
						}
						notifyDataSetChanged();
					}
				}
			}
			else {
				//如果是其他界面点击了删除的话,就删除收藏
				index = (Integer) ((RadioButton)v).getTag();
				Post_Operating = Post_Del;
				String URL = Home.postURL + "user=" + Home.user.userID + "&pass=" + Home.user.pass + "&operat=del" +
						"&table=SongCollect&tj=SongID&tj_value='" + msearch_informations.get(index).songId + 
						"'%20and%20SongList='" + URLEncoder.encode(msearch_informations.get(index).songList) + "'";
				new dataLoad(Wxh_Adapter.this).execute(URL);
				
				RelativeLayout hidelLayout=(RelativeLayout) ls.getChildAt(index).findViewById(R.id.rela_wxh_adapter);
				hidelLayout.setVisibility(View.GONE);
			}
			break;
			
		case R.id.img_wxh_adpater_left:
			index = (Integer) ((ImageView)v).getTag();
			Post_Operating = Post_AddList;
			if(isSearch)
				new dataLoad(Wxh_Adapter.this).execute("http://music.baidu.com/data/music/links?songIds="+msearch_informations.get(index).songId);
			else
				on_Post_Operating(msearch_informations.get(index));
			Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(Post_Operating == Post_Del){
			//如果是删除操作的话,提醒一下
			if (post_return_analysis.getIsSuccess(result)) {
				Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
			}
		}
		else{	//如果不是删除操作的话,获取歌曲信息,继续操作
			Search_information search_information = post_return_analysis.getSearch_information(result);
			on_Post_Operating(search_information);
		}
	}
	
	public void on_Post_Operating(Search_information search_information){
		switch (Post_Operating) {
		
		case Post_Add:			//添加收藏
			if(Home.user.isLogin){
				Song_Add_PopupWindow popupWindow2=new Song_Add_PopupWindow(mContext, search_information);
				popupWindow2.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			}
			break;
		case Post_Info:			//查看歌曲信息
			Song_Info_PopupWindow popupWindow=new Song_Info_PopupWindow(mContext, search_information);
			popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case Post_AddList:		//添加到播放列表
			MPService.SongList.add(search_information);
			break;
		case Post_Down:			//下载歌曲
			SongPlayer.DownMusic(mContext, search_information);
			break;
		}
	}
	
}
