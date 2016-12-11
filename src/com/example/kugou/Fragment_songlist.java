package com.example.kugou;

import java.util.List;

import com.example.kugou.Adapter.Home_SonList_Adapter;
import com.example.kugou.Controls.Add_SongList_Dialog;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.DBdata.select_analysis;
import com.example.kugou.FragmentAnim.FragmentAnim;
import com.example.kugou.Home.OnBackHandlerInterface;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 歌单界面
 * 
 * 都与前面大同小异,不注释了
 * 
 * @author Administrator
 *
 */
public class Fragment_songlist extends Fragment implements OnClickListener,OnBackHandlerInterface,OnPostExecuteListener,OnItemClickListener {

	public static View view;
	public static Fragment fragment;
	ImageView img_homesonglist_close,img_homesonglist_Menu,img_homesonglist_Refresh;
	Button btn_homesonglist_add;
	TextView tv_homesonglist_num,tv_homesonglist_menu_num;
	ListView ls_songlist;
	
	int Operating = -1;
	final int Selct_SongList = 0;
	final int Selct_SongCollect = 1;
	
	public static String Select_Name = "";
	
	List<String> songListsList;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.home_main_songlist, container, false);
		
		fragment = this;
		SetViewById();
		SetListener();
		SetAttributes();
		
		return view;
	}

	private void SetViewById() {
		// TODO Auto-generated method stub
		img_homesonglist_close = (ImageView) view.findViewById(R.id.img_homesonglist_close);
		img_homesonglist_Menu = (ImageView) view.findViewById(R.id.img_homesonglist_Menu);
		img_homesonglist_Refresh = (ImageView) view.findViewById(R.id.img_homesonglist_Refresh);
		btn_homesonglist_add = (Button) view.findViewById(R.id.btn_homesonglist_add);
		tv_homesonglist_num = (TextView) view.findViewById(R.id.tv_homesonglist_num);
		tv_homesonglist_menu_num = (TextView) view.findViewById(R.id.tv_homesonglist_menu_num);
		ls_songlist = (ListView) view.findViewById(R.id.ls_songlist);
	}

	private void SetListener() {
		// TODO Auto-generated method stub
		img_homesonglist_close.setOnClickListener(this);
		img_homesonglist_Menu.setOnClickListener(this);
		img_homesonglist_Refresh.setOnClickListener(this);
		btn_homesonglist_add.setOnClickListener(this);
	}

	private void SetAttributes() {
		// TODO Auto-generated method stub
		Home.SetonBackPressed=this;
		FragmentAnim.FragmentInAnimShow(getActivity(), view);
		if(Home.user.isLogin){
			Operating = Selct_SongList;
			String url = Home.postURL + "user=" + Home.user.userID 
				+ "&pass=" + Home.user.pass + "&operat=select&table=SongList";
			new dataLoad(Fragment_songlist.this).execute(url);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.img_homesonglist_close:
			Home.SetonBackPressed = null;
			FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_songlist.this);
			break;

		case R.id.img_homesonglist_Menu:
			Home.mHorizontalsv.OpenOrCloseMenu();
			break;

		case R.id.img_homesonglist_Refresh:
			if(Home.user.isLogin){
				Operating = Selct_SongList;
				String url = Home.postURL + "user=" + Home.user.userID 
						+ "&pass=" + Home.user.pass + "&operat=select&table=SongList";
				new dataLoad(Fragment_songlist.this).execute(url);
			}
			break;

		case R.id.btn_homesonglist_add:
			if(Home.user.isLogin){
				Add_SongList_Dialog dialog = new Add_SongList_Dialog(getActivity());
				dialog.show();
			}
			break;
			
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_songlist.this);
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		switch (Operating) {
		case Selct_SongList:

			songListsList = select_analysis.GetSongList(result);
			tv_homesonglist_menu_num.setText("自建歌单(" + songListsList.size() + ")");
			Operating = Selct_SongCollect;
			String url = Home.postURL + "user=" + Home.user.userID 
					+ "&pass=" + Home.user.pass + "&operat=select&table=SongCollect";
			new dataLoad(Fragment_songlist.this).execute(url);
			
			break;
		case Selct_SongCollect:
			
			List<Search_information> songCollects = select_analysis.GetSongCollect(result);
			tv_homesonglist_num.setText("云空间歌曲数:(" + songCollects.size() + ")");
			Home_SonList_Adapter adapter = new Home_SonList_Adapter(getActivity(), songListsList,songCollects, ls_songlist);
			ls_songlist.setAdapter(adapter);
			ls_songlist.setOnItemClickListener(this);
			
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		Select_Name = songListsList.get(position);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		Fragment_SongList_item songList_item=new Fragment_SongList_item();
		beginTransaction.add(R.id.home_fragm,songList_item);
		beginTransaction.addToBackStack(null);
		beginTransaction.commit();
		
	}
	
}
