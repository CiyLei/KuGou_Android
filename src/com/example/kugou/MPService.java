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
 * �������ַ���
 * ����intent��Action()ֵ���ж��û�Ҫ������ô�Ĳ���(�粥������,ֹͣ����)
 * ����SongList���������б��PlayerOrder����λ�������в�������
 * ����һ�㴫��Action()֮��,��������һ��SongList��PlayerOrder
 * 
 * handler								ͨ��ʱ��,ÿ0.1���ȡһ�²��Ž���,���ص�,��һ�������е��¼�
 * SongListAdd()						��Ӳ��Ÿ���(����Ӳ��Ÿ����б�,���Ÿ����ڴ���Ӳ��Ÿ����б������λ��)
 * PlayMp								���ݵ�ַ��������
 * onPostExecute(Bitmap result)			�ص�������ͼƬ
 * onPostExecute(String result)			�ص����������
 * 
 * @author Administrator
 *
 */
public class MPService extends Service implements OnPostExecuteListener,com.example.kugou.DBdata.dataLoad.OnPostExecuteListener {
	//��������	����
	public static int MusicPlayer = 1;
	//ֹͣ����	����
	public static int MusicStop = 0;
	//������
	public static MediaPlayer mp;
	//���������б�
	public static List<Search_information>SongList = new ArrayList<Search_information>();
	//�����������б��λ��
	public static int SongPosition = -1;
	//������ͼƬ
	public static Bitmap SongBitmap;
	//�����ĸ��
	public static Lyrics lyrics;
	//ʱ��ˢ����
	public static int Refresh = 100;  
	
	public static boolean looping=false;
	public static boolean isPause=false;
	public static boolean isLoadImg = false;
	//����˳��
	public static int PlayerOrder = 0;
	public static final int PlayerOrder_Sequentially = 0;
	public static final int PlayerOrder_Random = 1;
	public static final int PlayerOrder_Loop = 2;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	//��ʼ��
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
					//��ȡ�����Ľ��Ⱥ���ʱ��
					int CurrentPosition=mp.getCurrentPosition();
					int Duration=mp.getDuration();
					//ִ�лص�
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

	//��Ӳ��Ÿ���(����Ӳ��Ÿ����б�,���Ÿ����ڴ���Ӳ��Ÿ����б������λ��)
	public static int SongListAdd(List<Search_information> search_informations,int position){
		int index = 0;		
		//Ҫ���Ÿ����ڲ��Ÿ赥��λ��	
		int songPo = SongList.size() + position;
		//ȡ��ÿ������ӵĸ���	
		for (int i = 0; i < search_informations.size(); i++) { 		
			//������� �Ƿ񲥷Ÿ赥�������׸���
			boolean flag = false;									
			//ȡ��ÿ�����Ÿ赥�ĸ���
			for (int j = 0; j < MPService.SongList.size(); j++) {	
				//������Ÿ赥�ĸ������ڴ���ӵĸ����Ļ�
				if(MPService.SongList.get(j).songId.equals(search_informations.get(i).songId)){	
					//��������Ѿ�����
					flag = true;				
					//������������û����Ҫ���ŵĸ����Ļ� ��¼�²��Ÿ赥��λ��
					if(position == i){								
						index = j;
					}
					//����Ѿ�����,��Ԥ�ڵ�λ���ϼ�һ
					songPo--;
				}
			}//������Ÿ赥����û�����׸�Ļ�
			if(!flag){										
				//��Ӹ���
				MPService.SongList.add(search_informations.get(i));	
				//������������û����Ҫ���ŵĸ����Ļ� ��¼�²��Ÿ赥��λ��
				if(MPService.SongList.size() - 1 == songPo){						
					index = MPService.SongList.size() - 1;
				}
			}
		}
		return index;
	}
	//��������
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
		//�ų�null
			if(MSG == MusicPlayer){	
				//��������
				PlayMp(SongList.get(SongPosition).songLink);
				if(Home.img_home_bottom_player!=null)
					Home.img_home_bottom_player.setImageResource(R.drawable.home_stop);
				if(SongPlayer.img_songplayer_player != null)
					SongPlayer.img_songplayer_player.setImageResource(R.drawable.songplayer_stop);
			}
			else{
				//ֹͣ����
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
	//��������
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
						if (MPService.SongList.get(MPService.SongPosition).songList.equals("��ϲ��"))
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
