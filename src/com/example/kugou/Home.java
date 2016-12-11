package com.example.kugou;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.kugou.Controls.SongList_PopupWindow;
import com.example.kugou.Controls.mHorizontalScrollView;
import com.example.kugou.DBdata.User;
import com.example.kugou.FragmentAnim.FragmentAnim;
import com.example.kugou.R.drawable;
import com.example.download.DownLoadService;

import android.R.bool;
import android.R.integer;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * 主Activity
 * 
 * ProgressChanged()		获取歌曲进度,刷新UI
 * playMusic()				播放和停止音乐
 * login_Refresh()			根据是否登录,刷新UI
 * onBackPressed()			监听返回按钮,做fragment的回调或侧滑菜单的收回
 * Music_Next()				设置播放下一个的歌曲的位置
 * 
 * @author Administrator
 *
 */
public class Home extends FragmentActivity implements OnClickListener {

	public static ImageView img_home_bottom;
	public static ProgressBar pb_home_bottom;
	public static TextView tv_home_bottom_song;
	public static TextView tv_home_bottom_singer;
	public static ImageView img_home_bottom_player;
	public static Button home_menu_login;
	public static ImageView pic_user;
	public static TextView tv_user;
	public static RelativeLayout home_bottom;
	ImageView img_home_bottom_menu;
	ImageView img_home_bottom_next;
	Button btn_home_menu_close;
	
	public static mHorizontalScrollView mHorizontalsv;
	
	public static User user=new User();
	
	public static String postURL="http://115.28.105.161/kugou.aspx?";
	//存放底部导航的高度
	public static float home_bottom_height = 0;
	//存放屏幕的高度
	public static float mheight;
	Fragment_wxh wxh=new Fragment_wxh();
	Fragment_login_information login_information=new Fragment_login_information();
	public static boolean isShow_login_information;
	//线程池
	public static ExecutorService executorService = Executors.newCachedThreadPool();
	//点击了返回按钮的回调
	public interface OnBackHandlerInterface {  
        public void onBackPressed();
    }
	
	public static OnBackHandlerInterface SetonBackPressed;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fragment_main main=new Fragment_main();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
    	beginTransaction.add(R.id.home_fragm, main);
		beginTransaction.commit();
        
        mHorizontalsv=(mHorizontalScrollView) findViewById(R.id.mHorizontalsv);
        home_menu_login=(Button) findViewById(R.id.home_menu_login);
        pic_user=(ImageView) findViewById(R.id.pic_user);
        tv_user=(TextView) findViewById(R.id.tv_user);
        btn_home_menu_close=(Button) findViewById(R.id.btn_home_menu_close);	
        img_home_bottom=(ImageView) findViewById(R.id.img_home_bottom);
        pb_home_bottom=(ProgressBar) findViewById(R.id.pb_home_bottom);
        tv_home_bottom_song=(TextView) findViewById(R.id.tv_home_bottom_song);
        tv_home_bottom_singer=(TextView) findViewById(R.id.tv_home_bottom_singer);
        img_home_bottom_menu=(ImageView) findViewById(R.id.img_home_bottom_menu);
        img_home_bottom_next=(ImageView) findViewById(R.id.img_home_bottom_next);
        img_home_bottom_player=(ImageView) findViewById(R.id.img_home_bottom_player);
        
        img_home_bottom.setOnClickListener(this);
        img_home_bottom_menu.setOnClickListener(this);	
        img_home_bottom_next.setOnClickListener(this);	
        img_home_bottom_player.setOnClickListener(this);
		home_menu_login.setOnClickListener(this);
		btn_home_menu_close.setOnClickListener(this);
		pic_user.setOnClickListener(this);
		
		//获取屏幕高度和底部播放歌曲的高度
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 获取窗体
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mheight=outMetrics.heightPixels;
		home_bottom= (RelativeLayout) findViewById(R.id.home_bottom);
		if(home_bottom_height == 0)
			home_bottom_height = mheight - home_bottom.getY();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		//登录按钮
		case R.id.home_menu_login:
			Intent intent=new Intent(Home.this, Login.class);
			startActivity(intent);
			overridePendingTransition(R.animator.down_to_up, R.animator.notanim);
			break;
		//打开或关闭侧滑菜单按钮
		case R.id.btn_home_menu_close:
			mHorizontalsv.OpenOrCloseMenu();
			break;
		//用户头像
		case R.id.pic_user:

			login_information=new Fragment_login_information();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
	    	beginTransaction.add(R.id.home_fragm, login_information);
			beginTransaction.addToBackStack(null);
			beginTransaction.commit();
	    	isShow_login_information=true;
			mHorizontalsv.Enabled=false;
			
			mHorizontalsv.OpenOrCloseMenu();

			ObjectAnimator animator=ObjectAnimator.ofFloat(home_bottom, "translationY", 0F , home_bottom_height).setDuration(500);
			animator.start();
			
			break;
		//底部的音乐播放按钮
		case R.id.img_home_bottom_player:
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
		//底部的播放音乐的图片
		case R.id.img_home_bottom:
			
			intent = new Intent(Home.this, SongPlayer.class);
			startActivity(intent);
			overridePendingTransition(R.animator.right_to_left_rotate, R.animator.notanim);
			
			break;
		//底部的下一首图片
		case R.id.img_home_bottom_next:
			
			Music_Next();
			
			break;
		//底部的打开播放娶个列表图片
		case R.id.img_home_bottom_menu:
			home_bottom = (RelativeLayout) findViewById(R.id.home_bottom);
			home_bottom_height = home_bottom.getHeight();
			SongList_PopupWindow popupWindow = new SongList_PopupWindow(Home.this, MPService.SongList);
			View view=findViewById(R.layout.activity_home);
			popupWindow.showAtLocation(home_bottom, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, (int) home_bottom_height);
			break;
			
		}
		
	}
	//获取音乐播放进度,刷新UI 
	public static void ProgressChanged(int CurrentPosition,int Duration){
		pb_home_bottom.setMax(Duration);
		pb_home_bottom.setProgress(CurrentPosition);
	}
	
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(Home.this, MPService.class);  
        startService(intent);  
    }  
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		login_Refresh();
	}
	//根据是否登录,刷新UI
	public static void login_Refresh(){
		if(user.isLogin){
			
			home_menu_login.setVisibility(View.GONE);
			pic_user.setVisibility(View.VISIBLE);
			tv_user.setVisibility(View.VISIBLE);
			
			tv_user.setText(user.name);
			
		}
		else {
			home_menu_login.setVisibility(View.VISIBLE);
			pic_user.setVisibility(View.GONE);
			tv_user.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//如果不等于null(说明现在的界面是某个fragment)
		if(SetonBackPressed != null){
			//一般是退出动漫
			SetonBackPressed.onBackPressed();
			//如果是歌单歌曲界面的话,不是清空,而是设置为歌单界面
			if(!(SetonBackPressed instanceof Fragment_SongList_item))
				SetonBackPressed=null;
			else
				SetonBackPressed = (OnBackHandlerInterface) Fragment_songlist.fragment;
			
			return ;
		}
		//如果侧滑菜单是打开的话,关闭侧滑菜单
		if(mHorizontalsv.isOpen){
			mHorizontalsv.OpenOrCloseMenu();
			return ;
		}
		
		
		super.onBackPressed();
	}
	//播放下一首
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
	
}
