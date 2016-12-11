package com.example.kugou;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.DB.songlist.ThreadOADImpl_songlist;
import com.example.download.DownLoadService;
import com.example.download.DownLoadTask;
import com.example.kugou.Home.OnBackHandlerInterface;
import com.example.kugou.Adapter.Down_Adapter;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.FragmentAnim.FragmentAnim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 下载管理的界面
 * 
 * onCreateView()					初始化,控件初始化和添加监听器,和查询数据库里面的下载任务
 * downSearch_informationsAdd()		将所有数据库的下载任务添加到下载任务
 * DownMusic()						下载和停止下载任务
 * receiver							接受歌曲下载进度更新个下载完毕的广播,进行UI刷新
 * onBackPressed()					退出动漫
 * 
 * @author Administrator
 *
 */
public class Fragment_Down extends Fragment implements OnBackHandlerInterface,OnClickListener {

	View view;
	ImageView img_homedown_close,img_homedown_Menu;
	ListView ls_down;
	Down_Adapter adapter;
	ThreadOADImpl_songlist impl;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.home_main_down, null);
		
		img_homedown_close = (ImageView) view.findViewById(R.id.img_homedown_close);
		img_homedown_Menu = (ImageView) view.findViewById(R.id.img_homedown_Menu);
		ls_down = (ListView) view.findViewById(R.id.ls_down);
		
		img_homedown_close.setOnClickListener(this);
		img_homedown_Menu.setOnClickListener(this);
		
		impl = new ThreadOADImpl_songlist(getActivity());
		//取出数据库中的下载任务
		List<Search_information> search_informations = impl.getAllSongInfo();
		//添加到下载任务中
		for (Search_information search_information : search_informations) {
			downSearch_informationsAdd(search_information);
		}
		adapter = new Down_Adapter(getActivity(),DownLoadService.downSearch_informations,ls_down);
		ls_down.setAdapter(adapter);
		//因为fragment无法监听返回按钮,所以做个回调,设置回调
		Home.SetonBackPressed=this;
		//载入动漫
		FragmentAnim.FragmentInAnimShow(getActivity(), view);
		//注册广播
		IntentFilter filter = new IntentFilter();
        filter.addAction(DownLoadService.ACTION_FINISH);
        filter.addAction(DownLoadService.ACTION_UPDATE);
        getActivity().registerReceiver(receiver, filter);
		
		return view;
	}
	
	public void downSearch_informationsAdd(Search_information search_information){
		//将所有数据库的下载任务添加到下载任务
		for (Search_information search_information2 : DownLoadService.downSearch_informations) {
			if(search_information2.songId.equals(search_information.songId))
				return;
		}
		DownLoadService.downSearch_informations.add(search_information);
	}
	
	public void DownMusic(String Action,Search_information search_information){
		//下载和停止下载任务
		Intent intent = new Intent(getActivity(), DownLoadService.class);
		intent.setAction(Action);
		intent.putExtra("search_information", search_information);
		getActivity().startService(intent);
		
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver(){
    	public void onReceive(Context context, Intent intent) {
    		//接收到下载进度更新广播
    		if(DownLoadService.ACTION_UPDATE.equals(intent.getAction())){
    			String id = intent.getStringExtra("id");
				int finished = intent.getIntExtra("finished", 0);
				//取出歌曲的ID和进度,进行UI刷新
				adapter.updateProgress(id, finished);
    		}else if (DownLoadService.ACTION_FINISH.equals(intent.getAction())) {
        		//接收到下载完毕广播
				Search_information search_information = (Search_information) intent.getSerializableExtra("search_information");
				//取出歌曲信息,进行UI刷新
				adapter.updateProgress(search_information.songId, 100);
				adapter.notifyDataSetChanged();
				//这句有时会导致出错,所以注释
//				Toast.makeText(getActivity(), search_information.songName + "下载完毕", Toast.LENGTH_SHORT).show();
			}
    	};
    };

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//退出动漫
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_Down.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_homedown_close:
			//退出fragment时,Home的返回接口手动清空
			Home.SetonBackPressed = null;
			//退出动漫
			FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_Down.this);
			break;
		case R.id.img_homedown_Menu:
			//侧滑菜单打开或关闭
			Home.mHorizontalsv.OpenOrCloseMenu();
			break;
		}
	}
}
