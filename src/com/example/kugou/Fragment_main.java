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
 * �������fragment����
 * 
 * login_Refresh()		�˳���¼��,ˢ��UI
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
			//�໬�˵��򿪻�ر�
			Home.mHorizontalsv.OpenOrCloseMenu();
			break;

		case R.id.pic_home_user:
			//����û���¼��,���Ҳ����û�������Ϣ����Ļ�(����һ��BUG����,�ҵ�������fragment����,����һ���fragmentҲ�������
			//,���Լ��˺�������ж�,��������BUG�Ѿ������,Ӧ�ò������������������,�����Ķ���,���������д�)
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
