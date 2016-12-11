package com.example.kugou.Controls;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kugou.Home;
import com.example.kugou.MPService;
import com.example.kugou.R;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.DBdata.select_analysis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 添加歌曲收藏的漂浮窗
 * 
 * @author Administrator
 *
 */
public class Song_Add_PopupWindow extends PopupWindow implements
		OnPostExecuteListener, OnItemClickListener {

	ListView ls_songaddpop;
	Button btn_songaddpop, btn_songaddpop_xj;
	SimpleAdapter adapter;

	Context context;
	Search_information search_information;

	int operating = -1;
	final int selete_songlist = 0;
	final int songlist_add = 1;

	List<Map<String, String>> data;

	public Song_Add_PopupWindow(Context context,
			Search_information search_information) {

		this.context = context;
		this.search_information = search_information;

		View view = LayoutInflater.from(context).inflate(
				R.layout.song_add_pop_layout, null);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
		setAnimationStyle(R.animator.down_to_up);

		ls_songaddpop = (ListView) view.findViewById(R.id.ls_songaddpop);
		btn_songaddpop = (Button) view.findViewById(R.id.btn_songaddpop);
		btn_songaddpop_xj = (Button) view.findViewById(R.id.btn_songaddpop_xj);
		
		btn_songaddpop_xj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Add_SongList_Dialog dialog = new Add_SongList_Dialog(Song_Add_PopupWindow.this.context);
				dialog.show();
			}
		});

		btn_songaddpop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		if (Home.user.songList == null) {
			String url = Home.postURL + "user=" + Home.user.userID + "&pass="
					+ Home.user.pass + "&operat=select&table=SongList";
			new dataLoad(Song_Add_PopupWindow.this).execute(url);
			operating = selete_songlist;
		} else {
			adapter = new SimpleAdapter(context, getdata(Home.user.songList),
					R.layout.song_add_pop_adapter, new String[] { "gd" },
					new int[] { R.id.tv_songaddpop_adapter });
			ls_songaddpop.setAdapter(adapter);
		}

		ls_songaddpop.setOnItemClickListener(this);

		setContentView(view);
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		switch (operating) {

		case selete_songlist:
			Home.user.songList = new ArrayList<String>();
			Home.user.songList = select_analysis.GetSongList(result);
			adapter = new SimpleAdapter(context, getdata(Home.user.songList),
					R.layout.song_add_pop_adapter, new String[] { "gd" },
					new int[] { R.id.tv_songaddpop_adapter });
			ls_songaddpop.setAdapter(adapter);
			break;

		case songlist_add:
			if (post_return_analysis.getIsSuccess(result)) {
				Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
			} 
			else {
				Log.e("失败原因", post_return_analysis.getMsg(result));
				Toast.makeText(context,"添加" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
			}
			dismiss();
			break;

		}
	}

	public List<Map<String, String>> getdata(List<String> SongList) {
		data = new ArrayList<Map<String, String>>();
		for (String s : SongList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("gd", s);
			data.add(map);
		}
		return data;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String songLink = search_information.songLink;
		if(search_information.songLink.indexOf("&") > -1)
			songLink = search_information.songLink.substring(0,search_information.songLink.indexOf("&"));
		String url = Home.postURL
				+ "user="
				+ Home.user.userID
				+ "&pass="
				+ Home.user.pass
				+ "&operat=insert&table=SongCollect&values='"
				+ Home.user.userID
				+ "','"
				+ URLEncoder.encode(search_information.songName)
				+ "','"
				+ URLEncoder.encode(songLink + "','")
				+ search_information.songPicBig + "','"
				+ URLEncoder.encode(search_information.artistName) + "','"
				+ URLEncoder.encode(search_information.albumName) + "','"
				+ search_information.lrcLink + "','"
				+ search_information.time + "','"
				+ search_information.format + "','"
				+ URLEncoder.encode(search_information.rate) + "','"
				+ search_information.size + "','"
				+ search_information.songId + "','"
				+ URLEncoder.encode(Home.user.songList.get(position)) + "'";
		new dataLoad(Song_Add_PopupWindow.this).execute(url);
		operating = songlist_add;
	}
}
