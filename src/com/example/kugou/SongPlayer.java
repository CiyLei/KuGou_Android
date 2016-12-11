package com.example.kugou;

import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

import com.example.DB.songlist.ThreadOADImpl_songlist;
import com.example.download.DownLoadService;
import com.example.kugou.Adapter.Wxh_Adapter;
import com.example.kugou.Controls.LyricsView;
import com.example.kugou.Controls.SongPlayer_Order_PopupWindow;
import com.example.kugou.Controls.SongPlayer_SongList_PopupWindow;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.select_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.DBdata.post_return_analysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 播放音乐主界面
 * 
 * 没什么说明的
 * 
 * @author Administrator
 *
 */
public class SongPlayer extends Activity implements OnClickListener,OnSeekBarChangeListener,OnPostExecuteListener {

	public static ImageView img_songplayer_player,img_songplayer_love,img_songplayer_order;
	ImageView img_songplayer_close,img_songplayer_down,img_songplayer_last
		,img_songplayer_next,img_songplayer_menu;
	public static TextView tv_songplayer_song,tv_songplayer_singer,tv_songplayer_time,tv_songplayer_time_all;
	public static LyricsView lyricsView;
	public static SeekBar seekbar_songplayer;
	public static boolean islove;
	
	int Operating = -1;
	final int Post_Love = 0;
	final int Post_DelLove = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songplayer_layout);
		
		SetViewById();
		SetListener();
		SetAttributes();  //设置控件属性
		
	}
	
	private void SetAttributes() {
		// TODO Auto-generated method stub
		if(MPService.mp != null){
			if(MPService.mp.isPlaying())
				img_songplayer_player.setImageResource(R.drawable.songplayer_stop);
			SeekBarChanged(MPService.mp.getCurrentPosition(),MPService.mp.getDuration());
		}
		switch (MPService.PlayerOrder) {
		case MPService.PlayerOrder_Sequentially:
			img_songplayer_order.setImageResource(R.drawable.songplayer_sxbf);
			break;
		case MPService.PlayerOrder_Random:
			img_songplayer_order.setImageResource(R.drawable.songplayer_sjbf);
			break;
		case MPService.PlayerOrder_Loop:
			img_songplayer_order.setImageResource(R.drawable.songplayer_dqxh);
			break;
		}
	}

	private void SetListener() {
		// TODO Auto-generated method stub
		img_songplayer_close.setOnClickListener(this);
		img_songplayer_down.setOnClickListener(this);
		img_songplayer_love.setOnClickListener(this);
		img_songplayer_order.setOnClickListener(this);
		img_songplayer_last.setOnClickListener(this);
		img_songplayer_player.setOnClickListener(this);
		img_songplayer_next.setOnClickListener(this);
		img_songplayer_menu.setOnClickListener(this);
		seekbar_songplayer.setOnSeekBarChangeListener(this);
	}

	private void SetViewById() {
		// TODO Auto-generated method stub
		img_songplayer_close = (ImageView) findViewById(R.id.img_songplayer_close);
		img_songplayer_down = (ImageView) findViewById(R.id.img_songplayer_down);
		img_songplayer_love = (ImageView) findViewById(R.id.img_songplayer_love);
		img_songplayer_order = (ImageView) findViewById(R.id.img_songplayer_order);
		img_songplayer_last = (ImageView) findViewById(R.id.img_songplayer_last);
		img_songplayer_player = (ImageView) findViewById(R.id.img_songplayer_player);
		img_songplayer_next = (ImageView) findViewById(R.id.img_songplayer_next);
		img_songplayer_menu = (ImageView) findViewById(R.id.img_songplayer_menu);
		tv_songplayer_song = (TextView) findViewById(R.id.tv_songplayer_song);
		tv_songplayer_singer = (TextView) findViewById(R.id.tv_songplayer_singer);
		tv_songplayer_time = (TextView) findViewById(R.id.tv_songplayer_time);
		tv_songplayer_time_all = (TextView) findViewById(R.id.tv_songplayer_time_all);
		seekbar_songplayer = (SeekBar) findViewById(R.id.seekbar_songplayer);
		lyricsView=(LyricsView) findViewById(R.id.tv_lyrics);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(R.animator.notanim, R.animator.left_to_right_rotate);
	}

	@Override
	public void onClick(View v) {
		WindowManager wm = (WindowManager) SongPlayer.this
				.getSystemService(Context.WINDOW_SERVICE);
		// 获取窗体
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int width_p = outMetrics.widthPixels;

		switch (v.getId()) {
		case R.id.img_songplayer_close:
			finish();
			break;
		case R.id.img_songplayer_player:
			
			if (MPService.mp != null) {
				if (MPService.mp.isPlaying()) {
					playMusic(MPService.MusicStop);
				} else {
					playMusic(MPService.MusicPlayer);
				}
			}
			else if(MPService.SongList.size()!=0){
				playMusic(MPService.MusicPlayer);
				MPService.SongPosition=0;
			}
			
			break;
		case R.id.img_songplayer_last:
			Music_Last();
			break;
		case R.id.img_songplayer_next:
			Music_Next();
			break;
		case R.id.img_songplayer_order:
			SongPlayer_Order_PopupWindow popupWindow = new SongPlayer_Order_PopupWindow(SongPlayer.this, (int)(width_p * 0.4));
			popupWindow.showAtLocation(lyricsView, Gravity.BOTTOM|Gravity.LEFT, (int)(width_p * 0.01), (int)(width_p * 0.22));
			break;
		case R.id.img_songplayer_menu:
			SongPlayer_SongList_PopupWindow popupWindow2 = new SongPlayer_SongList_PopupWindow(SongPlayer.this, MPService.SongList, (int)(width_p * 0.8));
			 popupWindow2.showAtLocation(lyricsView, Gravity.BOTTOM|Gravity.RIGHT, (int)(width_p * 0.01), (int)(width_p * 0.22));
			break;
		case R.id.img_songplayer_love:
			String url = "";
			if(islove){
				Operating = Post_DelLove;
				url = Home.postURL + "user=" + Home.user.userID + "&pass=" + Home.user.pass + "&operat=del" +
				"&table=SongCollect&tj=SongID&tj_value='" + MPService.SongList.get(MPService.SongPosition).songId + 
				"'%20and%20SongList='" + URLEncoder.encode("我喜欢") + "'";
			} 
			else {
				
				if(!Home.user.isLogin || MPService.SongList.size() == 0)
					return;

				Operating = Post_Love;
				
				url = Home.postURL
				+ "user="
				+ Home.user.userID
				+ "&pass="
				+ Home.user.pass
				+ "&operat=insert&table=SongCollect&values='"
				+ Home.user.userID
				+ "','"
				+ URLEncoder.encode(MPService.SongList.get(MPService.SongPosition).songName)
				+ "','"
				+ URLEncoder.encode(MPService.SongList.get(MPService.SongPosition).songLink + "','")
				+ MPService.SongList.get(MPService.SongPosition).songPicBig
			    + "','"
				+ URLEncoder.encode(MPService.SongList.get(MPService.SongPosition).artistName)
				+ "','"
				+ URLEncoder.encode(MPService.SongList.get(MPService.SongPosition).albumName)
				+ "','"
				+ MPService.SongList.get(MPService.SongPosition).lrcLink
				+ "','"
				+ MPService.SongList.get(MPService.SongPosition).time
				+ "','"
				+ MPService.SongList.get(MPService.SongPosition).format
				+ "','"
				+ URLEncoder.encode(MPService.SongList.get(MPService.SongPosition).rate) + "','"
				+ MPService.SongList.get(MPService.SongPosition).size
				+ "','"
				+ MPService.SongList.get(MPService.SongPosition).songId
				+ "','" + URLEncoder.encode("我喜欢") + "'";
			}
			new dataLoad(SongPlayer.this).execute(url);
			
			break;
		case R.id.img_songplayer_down:
			
			DownMusic(SongPlayer.this,MPService.SongList.get(MPService.SongPosition));
			
			break;
		}
	}
	
	public static void DownMusic(Context context,Search_information search_information){
		
		ThreadOADImpl_songlist impl = new ThreadOADImpl_songlist(context);
		List<Search_information> search_informations = impl.getAllSongInfo();
		for (Search_information search_information2 : search_informations) {
			if(search_information2.songId.equals(search_information.songId)){
				Toast.makeText(context, "已下载", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if(search_information.songLink.indexOf("http") == -1){
			Toast.makeText(context, "无法下载", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(context, DownLoadService.class);
		intent.setAction(DownLoadService.ACTION_START);
		intent.putExtra("search_information", search_information);
		context.startService(intent);
		Toast.makeText(context, search_information.songName + "开始下载", Toast.LENGTH_SHORT).show();
		
	}
	
	public static void SeekBarChanged(int CurrentPosition,int Duration){
		
		tv_songplayer_song.setText(MPService.SongList.get(MPService.SongPosition).songName);
		tv_songplayer_singer.setText(MPService.SongList.get(MPService.SongPosition).artistName);
		
		if(MPService.SongList.get(MPService.SongPosition).songList != null){
			if (MPService.SongList.get(MPService.SongPosition).songList.equals("我喜欢")){
				SongPlayer.img_songplayer_love.setImageResource(R.drawable.songplayer_love_yes);
				islove = true;
			}
		}
		else{ 
			SongPlayer.img_songplayer_love.setImageResource(R.drawable.songplayer_love_no);
			islove = false;
		}
		
		SongPlayer.seekbar_songplayer.setMax(Duration);
		SongPlayer.seekbar_songplayer.setProgress(CurrentPosition);
		SongPlayer.tv_songplayer_time.setText(CurrentPosition / 1000 /60 + ":" 
				+ (CurrentPosition / 1000 % 60 < 10 ? "0" + CurrentPosition / 1000 % 60 : CurrentPosition / 1000 % 60));
		SongPlayer.tv_songplayer_time_all.setText(Duration / 1000 / 60 + ":" 
				+ (Duration / 1000 % 60 < 10 ? "0" + Duration / 1000 % 60 : Duration / 1000 % 60));
		lyricsView.getIndex(CurrentPosition);
		lyricsView.invalidate();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if(MPService.mp != null)
			MPService.mp.seekTo(seekBar.getProgress());
	}
	
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(SongPlayer.this, MPService.class);  
        startService(intent);  
    }  
	
	public void Music_Next() {
		
		if(MPService.SongList.size() == 0)
			return;
		
		switch (MPService.PlayerOrder) {

		case MPService.PlayerOrder_Sequentially:
			playMusic(MPService.MusicPlayer);
			if (MPService.SongPosition == MPService.SongList.size() - 1)
				MPService.SongPosition = 0;
			else
				MPService.SongPosition++;
			break;

		case MPService.PlayerOrder_Random:
			playMusic(MPService.MusicPlayer);
			Random random = new Random();
			MPService.SongPosition = random
					.nextInt(MPService.SongList.size());
			break;

		case MPService.PlayerOrder_Loop:
			playMusic(MPService.MusicPlayer);
			break;

		}
	}
	
	public void Music_Last(){
		if(MPService.SongList.size() == 0)
			return;
		playMusic(MPService.MusicPlayer);
		if (MPService.SongPosition == 0)
			MPService.SongPosition = MPService.SongList.size() - 1;
		else
			MPService.SongPosition--;
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(post_return_analysis.getIsSuccess(result)){
			switch (Operating) {
			case Post_Love:
				Toast.makeText(SongPlayer.this, "添加" + post_return_analysis.getIsSuccessTxt(result), Toast.LENGTH_SHORT).show();
				MPService.SongList.get(MPService.SongPosition).songList = "我喜欢";
				break;
			case Post_DelLove:
				Toast.makeText(SongPlayer.this, "取消" + post_return_analysis.getIsSuccessTxt(result), Toast.LENGTH_SHORT).show();
				MPService.SongList.get(MPService.SongPosition).songList = null;
				break;
			}
		}
		else {
			Toast.makeText(SongPlayer.this, "操作失败", Toast.LENGTH_SHORT).show();
			Log.e("失败原因:", post_return_analysis.getMsg(result));
		}
	}
}
