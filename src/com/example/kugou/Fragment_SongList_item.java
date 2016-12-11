package com.example.kugou;

import java.util.List;

import com.example.kugou.Adapter.Wxh_Adapter;
import com.example.kugou.Controls.SongList_ListView;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.select_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.FragmentAnim.FragmentAnim;
import com.example.kugou.Home.OnBackHandlerInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * 歌单歌曲的界面
 * 
 * 都与前面大同小异,不注释了
 * 
 * @author Administrator
 *
 */
public class Fragment_SongList_item extends Fragment implements OnBackHandlerInterface,OnClickListener,OnPostExecuteListener,OnItemClickListener {

	View view;
	ListView ls_songlist_list;
	ImageView img_songlist_list_close,img_songlist_list_menu,img_songlist_heard;
	TextView tv_songlist_list;
	
	Wxh_Adapter adapter;
	List<Search_information> search_informations;
	float down_point = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.songlist_item_layout, null);

		ls_songlist_list = (ListView) view.findViewById(R.id.ls_songlist_list);
		img_songlist_list_close = (ImageView) view.findViewById(R.id.img_songlist_list_close);
		img_songlist_list_menu = (ImageView) view.findViewById(R.id.img_songlist_list_menu);
		img_songlist_heard=(ImageView) view.findViewById(R.id.img_songlist_heard);
		tv_songlist_list = (TextView) view.findViewById(R.id.tv_songlist_list);
		
		tv_songlist_list.setText(Fragment_songlist.Select_Name);
		
		Home.SetonBackPressed=this;
		img_songlist_list_close.setOnClickListener(this);
		img_songlist_list_menu.setOnClickListener(this);

		String url = Home.postURL + "operat=select&table=SongCollect"
			+ "&user=" + Home.user.userID + "&pass=" +  Home.user.pass;
		new dataLoad(Fragment_SongList_item.this).execute(url);
		
		FragmentAnim.FragmentInAnimShow(getActivity(), view,Fragment_songlist.view);
		
		return view;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_SongList_item.this,Fragment_songlist.view);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_songlist_list_close:
			Home.SetonBackPressed = (OnBackHandlerInterface) Fragment_songlist.fragment;
			FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_SongList_item.this,Fragment_songlist.view);
			break;
		case R.id.img_songlist_list_menu:
			Home.mHorizontalsv.OpenOrCloseMenu();
			break;
		}
	}
	
	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		search_informations = select_analysis.GetSongCollect(result);
		for (int i = 0; i < search_informations.size(); i++) {
			if(!search_informations.get(i).songList.equals(Fragment_songlist.Select_Name)){
				search_informations.remove(i);
				i--;
			}
		}
		if(search_informations.size() > 0)
			adapter = new Wxh_Adapter(getActivity(), search_informations, view, ls_songlist_list);
		else 
			adapter = null;
		ls_songlist_list.setAdapter(adapter);
		ls_songlist_list.setOnItemClickListener(this);
		if(Fragment_songlist.Select_Name.equals("我喜欢"))
			img_songlist_heard.setImageResource(R.drawable.love_yes);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		playMusic(MPService.MusicPlayer);
		MPService.SongPosition = MPService.SongListAdd(search_informations, position);
	}

	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(getActivity(), MPService.class);  
        getActivity().startService(intent);  
    }
}
