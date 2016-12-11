package com.example.kugou;

import java.util.ArrayList;
import java.util.List;

import com.example.kugou.Home.OnBackHandlerInterface;
import com.example.kugou.Adapter.Wxh_Adapter;
import com.example.kugou.Adapter.Wxh_PopWindow_Adapter;
import com.example.kugou.Controls.SpinerPopWindow;
import com.example.kugou.Controls.SpinerPopWindow.OnItemClickListener_Pop;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.select_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.FragmentAnim.FragmentAnim;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
/**
 * 我喜欢界面
 * 
 * 都与前面大同小异,不注释了
 * 
 * @author Administrator
 *
 */
public class Fragment_wxh extends Fragment implements OnBackHandlerInterface,OnItemClickListener_Pop,OnClickListener,OnPostExecuteListener,OnItemClickListener{

	ImageView img_wxh_close;
	ImageView img_wxh_Menu;
	View view;
	RadioButton radio_wxh_pop_show;
	SpinerPopWindow popWindow;
	ImageView img_wxh_pop_img;
	int select_index=0;
	List<String> values;
	List<Search_information> search_informations;
	Wxh_Adapter adapter;
	public static ListView ls_wxh;

	int[] img=new int[]{R.drawable.pic_wxh_popwindow_sxbf,R.drawable.pic_wxh_popwindow_sjbf,R.drawable.pic_wxh_popwindow_dqxh};
	String[] txt=new String[]{"顺序播放","随机播放","单曲循环"};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.home_main_wxh, container, false);
		
		img_wxh_close=(ImageView) view.findViewById(R.id.img_wxh_close);
		img_wxh_Menu=(ImageView) view.findViewById(R.id.img_wxh_Menu);
		radio_wxh_pop_show=(RadioButton) view.findViewById(R.id.radio_wxh_pop_show);
		img_wxh_pop_img=(ImageView) view.findViewById(R.id.img_wxh_pop_img);
		ls_wxh=(ListView) view.findViewById(R.id.ls_wxh);
		
		
		if(Home.user.isLogin){
			String URL = Home.postURL + "user=" + Home.user.userID + "&pass=" + Home.user.pass + "&operat=select&table=SongCollect";
			new dataLoad(Fragment_wxh.this).execute(URL);
		}

		PlayerOrder(MPService.PlayerOrder);
		
		radio_wxh_pop_show.setOnClickListener(this);
		img_wxh_pop_img.setOnClickListener(this);
		img_wxh_close.setOnClickListener(this);
		img_wxh_Menu.setOnClickListener(this);
		
		Home.SetonBackPressed=this;
		FragmentAnim.FragmentInAnimShow(getActivity(), view);
    	
		return view;
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_wxh.this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		
		case R.id.ls_wxh_popwindow:
			popWindow.dismiss();
			PlayerOrder(position);
			break;

		case R.id.ls_wxh:
			playMusic(MPService.MusicPlayer);
			MPService.SongPosition = MPService.SongListAdd(search_informations, position);
//			MPService.SongList = search_informations;
			break;
		}
	}
	
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(getActivity(), MPService.class);  
        getActivity().startService(intent);  
    } 
	
	public void PlayerOrder(int position){
		Drawable drawable= getResources().getDrawable(img[position]);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		radio_wxh_pop_show.setCompoundDrawables(drawable, null, null, null);
		radio_wxh_pop_show.setText(txt[position]);
		select_index=position;
		MPService.PlayerOrder=position;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.radio_wxh_pop_show :
//		case R.id.img_wxh_pop_img:
			
			popWindow=new SpinerPopWindow(getActivity(), img, txt, select_index,radio_wxh_pop_show.getWidth() + 100);
			popWindow.SetOnItemClick=Fragment_wxh.this;
			popWindow.showAsDropDown(radio_wxh_pop_show,10 ,-5);
			img_wxh_pop_img.setRotation(180);
			popWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					img_wxh_pop_img.setRotation(0);
				}
			});
			
			break;
		case R.id.img_wxh_close:
			Home.SetonBackPressed = null;
			FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_wxh.this);
			break;
		case R.id.img_wxh_Menu:
			Home.mHorizontalsv.OpenOrCloseMenu();
			break;
		}
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		search_informations = select_analysis.GetSongCollect(result);
		for (int i = 0; i < search_informations.size(); i++) {
			if(!search_informations.get(i).songList.equals("我喜欢")){
				search_informations.remove(i);
				i--;
			}
		}
		adapter = new Wxh_Adapter(getActivity(), search_informations ,view ,ls_wxh);
		adapter.SongList="我喜欢";
		ls_wxh.setAdapter(adapter);
		ls_wxh.setOnItemClickListener(this);
	}
}
