package com.example.kugou;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.kugou.Home.OnBackHandlerInterface;
import com.example.kugou.Adapter.Wxh_Adapter;
import com.example.kugou.Controls.Song_Add_PopupWindow;
import com.example.kugou.Controls.Song_Info_PopupWindow;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.select_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.FragmentAnim.FragmentAnim;

import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
/**
 * 搜索歌曲界面
 * 
 * onPostExecute()		判断一下是第一次加载音乐还是加载第N页的音乐还是要加载播放音乐的信息
 * 
 * @author Administrator
 *
 */
public class Search extends Fragment implements OnClickListener,
		OnPostExecuteListener, OnScrollListener, OnFocusChangeListener,
		OnItemClickListener,OnBackHandlerInterface {

	View view;
	EditText edit_search_title;
	Button btn_search_title;
	ListView ls_search_nr;
	
	String URL="";
	Wxh_Adapter adapter;
	List<Search_information> search_informations;
	Integer page=1;
	int ls_scrollState;
	
	ProgressDialog pd;
	
	int Post_Operating = 0;
	final int Post_SongList = 0;
	final int Post_SongList_Add = 1;
	final int Post_PlayerMusic = 2;
	
	boolean isSongList_Add = false;
	
	public Search_information search_information;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.search,container, false);
		
//		show();
		pd=ProgressDialog.show(getActivity(), null, "加载中");
		
		edit_search_title=(EditText) view.findViewById(R.id.edit_search_title);
		btn_search_title=(Button) view.findViewById(R.id.btn_search_title);
		ls_search_nr=(ListView) view.findViewById(R.id.ls_search_nr);
		
		edit_search_title.setText(Fragment_ting.search_value.trim());
		edit_search_title.setOnFocusChangeListener(this);
		btn_search_title.setOnClickListener(this);
		ls_search_nr.setOnScrollListener(this);
		ls_search_nr.setOnItemClickListener(this);
		Home.SetonBackPressed=this;
		
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);   //关闭输入法
		
		edit_search_title.clearFocus();
		String txt=edit_search_title.getText().toString();
		if(!txt.isEmpty()){
			URL="http://musicmini.baidu.com/app/search/searchList.php?qword=" + URLEncoder.encode(txt);
			new dataLoad(Search.this).execute(URL);
		}
		
		FragmentAnim.FragmentInAnimShow(getActivity(), view);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search_title:
			if(btn_search_title.getText().toString().equals("取消"))
				FragmentAnim.FragmentInAnimEnd(getActivity(), view, Search.this);
			else {
				page = 1;
				Post_Operating = Post_SongList;
				pd=ProgressDialog.show(getActivity(), null, "加载中");
				btn_search_title.setText("取消");
				
				String txt=edit_search_title.getText().toString();
				if(!txt.isEmpty()){
					URL="http://musicmini.baidu.com/app/search/searchList.php?qword=" + URLEncoder.encode(txt);
					new dataLoad(Search.this).execute(URL);
				}
				
			}
			break;
		}
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		
		switch (Post_Operating) {
		case Post_SongList:
			
			search_informations = new ArrayList<Search_information>();
			pd.dismiss();
			
			Pattern pattern = Pattern.compile("(?<=downloadSong\\().+?(?=\\)\")");
			Matcher matcher = pattern.matcher(result);
			while (matcher.find()) {
				String[] ss = matcher.group().replace("&#039;", "").split(",");
				Search_information search_information = new Search_information();
				search_information.songId = ss[0];
				search_information.artistName = ss[1];
				if(ss[3].trim().isEmpty())
					search_information.songName = ss[2];
				else
					search_information.songName = ss[2] + " " + ss[3];
				search_informations.add(search_information);
			}
			adapter = new Wxh_Adapter(getActivity(), search_informations, view, ls_search_nr);
			adapter.isSearch = true;
			ls_search_nr.setAdapter(adapter);
			
			break;
		case Post_SongList_Add:
			
			pattern = Pattern.compile("(?<=downloadSong\\().+?(?=\\)\")");
			matcher = pattern.matcher(result);
			while (matcher.find()) {
				String[] ss = matcher.group().replace("&#039;", "").split(",");
				Search_information search_information = new Search_information();
				search_information.songId = ss[0];
				search_information.artistName = ss[1];
				if(ss[3].trim().isEmpty())
					search_information.songName = ss[2];
				else
					search_information.songName = ss[2] + " " + ss[3];
				search_informations.add(search_information);
			}
			isSongList_Add = false;
			adapter.notifyDataSetChanged();
			
			break;
		case Post_PlayerMusic:
			
			search_information = post_return_analysis.getSearch_information(result);

			playMusic(MPService.MusicPlayer);
			List<Search_information> search_informations = new ArrayList<Search_information>();
			search_informations.add(search_information);
			MPService.SongPosition = MPService.SongListAdd(search_informations,0);
//			MPService.SongList.add(search_information);
//			MPService.SongPosition = MPService.SongList.size() - 1;
			
			break;
		}
	}
	
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(getActivity(), MPService.class);  
        getActivity().startService(intent);  
    }  

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		ls_scrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount >= 1 
				&& ls_scrollState ==OnScrollListener.SCROLL_STATE_IDLE && !isSongList_Add){
			Post_Operating = Post_SongList_Add;
			page++;
			new dataLoad(Search.this).execute(URL + "&page=" + page.toString());
			isSongList_Add = true;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus)
			btn_search_title.setText("搜索");
		else
			btn_search_title.setText("取消");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
			Post_Operating = Post_PlayerMusic;
			new dataLoad(Search.this).execute("http://music.baidu.com/data/music/links?songIds="+search_informations.get(position).songId);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Search.this);
	}
}
