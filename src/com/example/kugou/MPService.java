package com.example.kugou;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import com.example.kugou.Adapter.SongList_Adapter;
import com.example.kugou.Controls.SongList_PopupWindow;
import com.example.kugou.Controls.SongPlayer_SongList_PopupWindow;
import com.example.kugou.DBdata.ImageLoader;
import com.example.kugou.DBdata.Lyrics;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.ImageLoader.OnPostExecuteListener;
import com.example.kugou.DBdata.Search_information;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
/**
 * 播放音乐服务
 * 根据intent的Action()值来判断用户要进行怎么的操作(如播放音乐,停止音乐)
 * 根据SongList播放音乐列表和PlayerOrder播放位置来进行播放音乐
 * 所以一般传个Action()之外,还会设置一下SongList和PlayerOrder
 * 
 * handler								通过时钟,每0.1秒获取一下播放进度,并回调,做一个播放中的事件
 * SongListAdd()						添加播放歌曲(呆添加播放歌曲列表,播放歌曲在呆添加播放歌曲列表里面的位置)
 * PlayMp								根据地址播放音乐
 * onPostExecute(Bitmap result)			回调来歌曲图片
 * onPostExecute(String result)			回调来歌曲歌词
 * 
 * @author Administrator
 *
 */
public class MPService extends Service implements OnPostExecuteListener,com.example.kugou.DBdata.dataLoad.OnPostExecuteListener {
	//播放音乐	命令
	public static int MusicPlayer = 1;
	//停止音乐	命令
	public static int MusicStop = 0;
	//播放器
	public static MediaPlayer mp;
	//播放音乐列表
	public static List<Search_information>SongList = new ArrayList<Search_information>();
	//播放音乐在列表的位置
	public static int SongPosition = -1;
	//歌曲的图片
	public static Bitmap SongBitmap;
	//歌曲的歌词
	public static Lyrics lyrics;
	//时钟刷新率
	public static int Refresh = 100;  
	
	public static boolean looping=false;
	public static boolean isPause=false;
	public static boolean isLoadImg = false;
	//播放顺序
	public static int PlayerOrder = 0;
	public static final int PlayerOrder_Sequentially = 0;
	public static final int PlayerOrder_Random = 1;
	public static final int PlayerOrder_Loop = 2;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	//初始化
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(SongPosition == -1)
			SongPosition=0;
		if (mp == null){
			mp = new MediaPlayer();
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
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
						MPService.SongPosition = random.nextInt(MPService.SongList.size() - 1);
						break;

					case MPService.PlayerOrder_Loop:
						playMusic(MPService.MusicPlayer);
						break;
						

					}
				}
			});
		}
		
		final Handler handler =new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				if(mp.isPlaying()){
					//获取歌曲的进度和总时间
					int CurrentPosition=mp.getCurrentPosition();
					int Duration=mp.getDuration();
					//执行回调
					if(Home.pb_home_bottom != null)
						Home.ProgressChanged(CurrentPosition,Duration);
					if(SongPlayer.seekbar_songplayer!=null)
						SongPlayer.SeekBarChanged(CurrentPosition,Duration);
				}
				
			}
		};
		
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg= new Message();
				handler.sendMessage(msg);
			}
		}, 0,Refresh);
		
	}

	//添加播放歌曲(呆添加播放歌曲列表,播放歌曲在呆添加播放歌曲列表里面的位置)
	public static int SongListAdd(List<Search_information> search_informations,int position){
		int index = 0;		
		//要播放歌曲在播放歌单的位置	
		int songPo = SongList.size() + position;
		//取出每个待添加的歌曲	
		for (int i = 0; i < search_informations.size(); i++) { 		
			//做个标记 是否播放歌单里有这首个了
			boolean flag = false;									
			//取出每个播放歌单的歌曲
			for (int j = 0; j < MPService.SongList.size(); j++) {	
				//如果播放歌单的歌曲等于待添加的歌曲的话
				if(MPService.SongList.get(j).songId.equals(search_informations.get(i).songId)){	
					//标记这首已经有了
					flag = true;				
					//但如果这首是用户点击要播放的歌曲的话 记录下播放歌单的位置
					if(position == i){								
						index = j;
					}
					//如果已经有了,再预期的位置上减一
					songPo--;
				}
			}//如果播放歌单里面没有这首歌的话
			if(!flag){										
				//添加歌曲
				MPService.SongList.add(search_informations.get(i));	
				//但如果这首是用户点击要播放的歌曲的话 记录下播放歌单的位置
				if(MPService.SongList.size() - 1 == songPo){						
					index = MPService.SongList.size() - 1;
				}
			}
		}
		return index;
	}
	//播放音乐
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(MPService.this, MPService.class);  
        startService(intent);  
    }  
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent != null){
		int MSG=intent.getIntExtra("MSG", MusicStop);
		//排除null
			if(MSG == MusicPlayer){	
				//播放音乐
				PlayMp(SongList.get(SongPosition).songLink);
				if(Home.img_home_bottom_player!=null)
					Home.img_home_bottom_player.setImageResource(R.drawable.home_stop);
				if(SongPlayer.img_songplayer_player != null)
					SongPlayer.img_songplayer_player.setImageResource(R.drawable.songplayer_stop);
			}
			else{
				//停止音乐
				mp.pause();
				isPause=true;
				if(Home.img_home_bottom_player!=null)
					Home.img_home_bottom_player.setImageResource(R.drawable.home_player);
				if(SongPlayer.img_songplayer_player != null)
					SongPlayer.img_songplayer_player.setImageResource(R.drawable.songplayer_player);
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	//播放音乐
	public void PlayMp(String uri) {
		try {
			if (!isPause) {
				mp.reset();
				mp.setDataSource(MPService.this, Uri.parse(uri));
				mp.setLooping(looping);
				mp.prepare();

				lyrics = null;
				if(!MPService.SongList.get(MPService.SongPosition).lrcLink.isEmpty()){
					String url = MPService.SongList.get(MPService.SongPosition).lrcLink;
					new dataLoad(MPService.this).execute(url);
				}
				if(Home.tv_home_bottom_song != null){
					Home.tv_home_bottom_song.setText(SongList.get(SongPosition).songName);
					Home.tv_home_bottom_singer.setText(SongList.get(SongPosition).artistName);
					new ImageLoader(MPService.this).execute(SongList.get(SongPosition).songPicBig);
				}
				if(SongPlayer.tv_songplayer_song != null){
					SongPlayer.tv_songplayer_song.setText(SongList.get(SongPosition).songName);
					SongPlayer.tv_songplayer_singer.setText(SongList.get(SongPosition).artistName);
					if(MPService.SongList.get(MPService.SongPosition).songList != null)
						if (MPService.SongList.get(MPService.SongPosition).songList.equals("我喜欢"))
							SongPlayer.img_songplayer_love.setImageDrawable(getResources().getDrawable(R.drawable.songplayer_love_yes));
				}
				
			}
			mp.start();
			isPause=false;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mp.release();
	}

	@Override
	public void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		Home.img_home_bottom.setImageBitmap(result);
		if(SongList_PopupWindow.adapter != null){
			isLoadImg = false;
			SongList_PopupWindow.adapter.notifyDataSetChanged();
		}
		if(SongPlayer_SongList_PopupWindow.adapter != null){
			isLoadImg = false;
			SongPlayer_SongList_PopupWindow.adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(!result.isEmpty()){
			lyrics = new Lyrics(result);
		}
	}

}
