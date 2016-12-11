package com.example.kugou.Controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kugou.MPService;
import com.example.kugou.R;
import com.example.kugou.SongPlayer;
import com.example.kugou.Adapter.SongList_Adapter;
import com.example.kugou.Adapter.SongList_Adapter.OnSongListDeleteClickListener;
import com.example.kugou.DBdata.Search_information;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 播放音乐列表的漂浮窗(这个是在播放音乐主界面的)
 * 
 * @author Administrator
 *
 */
public class SongPlayer_SongList_PopupWindow extends PopupWindow implements OnSongListDeleteClickListener,OnClickListener,OnItemClickListener {

	public static SongList_Adapter adapter;
	
	Context context;
	List<Search_information> search_informations;
	TextView tv_songplayer_songlist;
	ImageView img_songplayer_songlist_order;
	ImageView img_songplayer_songlist_delete;
	ListView ls_songplayer_songlist;

	public SongPlayer_SongList_PopupWindow(Context context,
			List<Search_information> search_informations,int Width) {

		this.context=context;
		this.search_informations = search_informations;

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.songplayer_songlist_layout, null);
		setContentView(view);
		
		setWidth(Width);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setAnimationStyle(R.animator.down_to_up);
        ColorDrawable dw = new ColorDrawable(0x00); 
		setBackgroundDrawable(dw);
		
		adapter = new SongList_Adapter(context, search_informations);
		adapter.SetOnSongListDeleteClickListener(this);
		
		ls_songplayer_songlist=(ListView) view.findViewById(R.id.ls_songplayer_songlist);
		tv_songplayer_songlist = (TextView) view.findViewById(R.id.tv_songplayer_songlist);
		img_songplayer_songlist_order = (ImageView) view.findViewById(R.id.img_songplayer_songlist_order);
		img_songplayer_songlist_delete = (ImageView) view.findViewById(R.id.img_songplayer_songlist_delete);

		tv_songplayer_songlist.setText("播放列表(" + MPService.SongList.size() + ")" );
		if(MPService.PlayerOrder == MPService.PlayerOrder_Sequentially)
			img_songplayer_songlist_order.setImageResource(R.drawable.pic_wxh_popwindow_sxbf);
		else if(MPService.PlayerOrder == MPService.PlayerOrder_Random)
			img_songplayer_songlist_order.setImageResource(R.drawable.pic_wxh_popwindow_sjbf);
		else if(MPService.PlayerOrder == MPService.PlayerOrder_Loop)
			img_songplayer_songlist_order.setImageResource(R.drawable.pic_wxh_popwindow_dqxh);
		
		ls_songplayer_songlist.setAdapter(adapter);
		
		img_songplayer_songlist_order.setOnClickListener(this);
		img_songplayer_songlist_delete.setOnClickListener(this);
		ls_songplayer_songlist.setOnItemClickListener(this);
		
	}

	@Override
	public void onSongListDeleteClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer) ((ImageView)v).getTag();
		MPService.SongList.remove(position);
		adapter.notifyDataSetChanged();
		tv_songplayer_songlist.setText("播放列表(" + MPService.SongList.size() + ")" );
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.img_songplayer_songlist_order:
			
			switch (MPService.PlayerOrder) {
			case MPService.PlayerOrder_Sequentially:
				MPService.PlayerOrder = MPService.PlayerOrder_Random;
				img_songplayer_songlist_order.setImageResource(R.drawable.pic_wxh_popwindow_sjbf);
				SongPlayer.img_songplayer_order.setImageResource(R.drawable.songplayer_sjbf);
				Toast.makeText(context, "随机播放", Toast.LENGTH_SHORT).show();
				break;
			case MPService.PlayerOrder_Random:
				MPService.PlayerOrder = MPService.PlayerOrder_Loop;
				img_songplayer_songlist_order.setImageResource(R.drawable.pic_wxh_popwindow_dqxh);
				SongPlayer.img_songplayer_order.setImageResource(R.drawable.songplayer_dqxh);
				Toast.makeText(context, "单曲循环", Toast.LENGTH_SHORT).show();
				break;
			case MPService.PlayerOrder_Loop:
				MPService.PlayerOrder = MPService.PlayerOrder_Sequentially;
				img_songplayer_songlist_order.setImageResource(R.drawable.pic_wxh_popwindow_sxbf);
				SongPlayer.img_songplayer_order.setImageResource(R.drawable.songplayer_sxbf);
				Toast.makeText(context, "顺序播放", Toast.LENGTH_SHORT).show();
				break;
			}
			
			break;

		case R.id.img_songplayer_songlist_delete:
			MPService.SongList.clear();
			MPService.SongPosition = 0;
			dismiss();
			break;
			
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		playMusic(MPService.MusicPlayer);
		MPService.SongPosition = position;
		MPService.isLoadImg = true;
		adapter.notifyDataSetChanged();
	}
	
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(context, MPService.class);  
        context.startService(intent);  
    }  

}
