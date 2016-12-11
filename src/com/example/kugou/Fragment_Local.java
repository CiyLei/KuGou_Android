package com.example.kugou;

import java.util.ArrayList;
import java.util.List;

import com.example.DB.songlist.ThreadOADImpl_songlist;
import com.example.kugou.Home.OnBackHandlerInterface;
import com.example.kugou.Adapter.Wxh_Adapter;
import com.example.kugou.Controls.SpinerPopWindow;
import com.example.kugou.Controls.SpinerPopWindow.OnItemClickListener_Pop;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.FragmentAnim.FragmentAnim;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
/**
 * 本地音乐的界面
 * 
 * onCreateView()					初始化,控件初始化和添加监听器,和查询数据库里面的本地音乐信息
 * PlayerOrder()					改变播放音乐的顺序,并进行UI刷新
 * playMusic()						播放和停止音乐
 * onBackPressed()					退出动漫
 * 
 * @author Administrator
 *
 */
public class Fragment_Local extends Fragment implements OnBackHandlerInterface,OnClickListener,OnItemClickListener_Pop,OnItemClickListener{

	View view;
	ImageView img_local_close,img_local_Menu,img_local_pop_img;
	RadioButton radio_local_pop_show;
	ListView ls_local;
	SpinerPopWindow popWindow;
	Wxh_Adapter adapter;
	
	List<Search_information> search_informations = new ArrayList<Search_information>();

	int select_index=0;
	int[] img=new int[]{R.drawable.pic_wxh_popwindow_sxbf,R.drawable.pic_wxh_popwindow_sjbf,R.drawable.pic_wxh_popwindow_dqxh};
	String[] txt=new String[]{"顺序播放","随机播放","单曲循环"};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.home_main_local,container, false);
		
		img_local_close = (ImageView) view.findViewById(R.id.img_local_close);
		img_local_Menu = (ImageView) view.findViewById(R.id.img_local_Menu);
		img_local_pop_img=(ImageView) view.findViewById(R.id.img_local_pop_img);
		radio_local_pop_show = (RadioButton) view.findViewById(R.id.radio_local_pop_show);
		ls_local = (ListView) view.findViewById(R.id.ls_local);
		
		img_local_close.setOnClickListener(this);
		img_local_Menu.setOnClickListener(this);
		radio_local_pop_show.setOnClickListener(this);
		ls_local.setOnItemClickListener(this);
		//第一次加载,图标与播放顺序对应一下
		PlayerOrder(MPService.PlayerOrder);
		//因为fragment无法监听返回按钮,所以做个回调,设置回调
		Home.SetonBackPressed=this;
		//载入动漫
		FragmentAnim.FragmentInAnimShow(getActivity(), view);
		
		ThreadOADImpl_songlist impl = new ThreadOADImpl_songlist(getActivity());
		//取出所有下载完毕的歌曲信息
		search_informations = impl.getAllFinishSongInfo();
		
		adapter = new Wxh_Adapter(getActivity(), search_informations, view, ls_local);
		//标识一下,adapter你是这个界面的东东
		adapter.isLocal = true;
		ls_local.setAdapter(adapter);
		
		return view;
	}
	
	public void PlayerOrder(int position){
		//图标与播放顺序对应一下
		Drawable drawable= getResources().getDrawable(img[position]);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		radio_local_pop_show.setCompoundDrawables(drawable, null, null, null);
		radio_local_pop_show.setText(txt[position]);
		select_index = position;
		MPService.PlayerOrder=position;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//退出动漫
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_Local.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.radio_local_pop_show :
			//初始化漂浮窗体
			popWindow=new SpinerPopWindow(getActivity(), img, txt, select_index,radio_local_pop_show.getWidth() + 100);
			//设置回调,看看是设置了什么播放顺序
			popWindow.SetOnItemClick=Fragment_Local.this;
			//显示位置
			popWindow.showAsDropDown(radio_local_pop_show,10 ,-5);
			//图片旋转
			img_local_pop_img.setRotation(180);
			popWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					img_local_pop_img.setRotation(0);
				}
			});
			
			break;
		case R.id.img_local_close:
			//退出fragment时,Home的返回接口手动清空
			Home.SetonBackPressed = null;
			//退出动漫
			FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_Local.this);
			break;
		case R.id.img_local_Menu:
			//侧滑菜单打开或关闭
			Home.mHorizontalsv.OpenOrCloseMenu();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		
		case R.id.ls_wxh_popwindow:
			//单击了漂浮窗体的选择播放顺序
			popWindow.dismiss();
			//更新UI
			PlayerOrder(position);
			break;

		case R.id.ls_local:
			//播放音乐
			playMusic(MPService.MusicPlayer);
			//添加播放歌曲
			MPService.SongPosition = MPService.SongListAdd(search_informations, position);
			break;
		}
	}
	
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(getActivity(), MPService.class);  
        getActivity().startService(intent);  
    } 
	
}
