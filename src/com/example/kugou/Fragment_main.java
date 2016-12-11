package com.example.kugou;

import com.example.kugou.Controls.RoundImageView;

import android.animation.Animator.AnimatorListener;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
/**
 * 主界面的fragment界面
 * 
 * login_Refresh()		退出登录了,刷新UI
 * 
 * @author Administrator
 *
 */
public class Fragment_main extends Fragment implements OnClickListener {

	ImageView home_to_menu;
	public static RoundImageView pic_home_user;
	public static View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.home_main, container, false);
        home_to_menu=(ImageView) view.findViewById(R.id.home_to_menu);     //
        pic_home_user=(RoundImageView) view.findViewById(R.id.pic_home_user);  //
        
		home_to_menu.setOnClickListener(this);
		pic_home_user.setOnClickListener(this);

		Fragment_ting ting=new Fragment_ting();
		FragmentManager fragmentManager=getFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		beginTransaction.add(R.id.home_tab_fragm, ting);
		beginTransaction.commit();
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.home_to_menu:     
			//侧滑菜单打开或关闭
			Home.mHorizontalsv.OpenOrCloseMenu();
			break;

		case R.id.pic_home_user:
			//如果用户登录着,而且不是用户个人信息界面的话(当初一个BUG导致,我点击顶层的fragment界面,下面一层的fragment也被点击了
			//,所以加了后面这个判断,现在想想BUG已经解决了,应该不会出现这样的问题了,但懒的动了,反正不会有错)
			if(Home.user.isLogin && !Home.isShow_login_information){

				Fragment_login_information login_information=new Fragment_login_information();
				login_information.isHomeOpen = true;
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
				beginTransaction.add(R.id.home_fragm,login_information);
				beginTransaction.addToBackStack(null);
				beginTransaction.commit();
				Home.isShow_login_information=true;
				Home.mHorizontalsv.Enabled=false;
				
				ObjectAnimator animator=ObjectAnimator.ofFloat(Home.home_bottom, "translationY",  0 ,Home.home_bottom_height).setDuration(500);
				animator.start();
				
			}
			
			break;
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		login_Refresh();
	}
	
	public static void login_Refresh(){
		if(Home.user.isLogin){
			pic_home_user.setImageResource(R.drawable.pic_user);   //
		}
		else{
			pic_home_user.setImageResource(R.drawable.home_user_pic);   //
		}
	}
	
}
