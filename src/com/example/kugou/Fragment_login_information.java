package com.example.kugou;

import com.example.kugou.Home.OnBackHandlerInterface;
import com.example.kugou.Adapter.Login_Information_Adapter;
import com.example.kugou.Adapter.Login_Information_Adapter.OnLogoutClickListener;
import com.example.kugou.Controls.Revise_Sex_PopupWindow;
import com.example.kugou.Controls.Revise_UserName_Dialog;
import com.example.kugou.DBdata.User;
import com.example.kugou.FragmentAnim.FragmentAnim;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * �û�������Ϣ����
 * 
 * onClick()			�ص�������,�ǵ������˳���¼
 * onBackPressed()		�˳���������,�ײ����ϻ�
 * onItemClick()		�ֱ𵥻���	�޸�����	�޸��Ա�	�޸�����
 * 
 * @author Administrator
 *
 */
public class Fragment_login_information extends Fragment implements OnClickListener,OnLogoutClickListener,OnBackHandlerInterface,OnItemClickListener {

	View view;
	ImageView img_info_close;
	RadioButton radio_information_ting;
	RadioButton radio_information_kan;
	RadioButton radio_information_chan;
	ListView lv_login_information;
	Login_Information_Adapter adapter;
	
	boolean mHandledPress=false;
	
	public boolean isHomeOpen = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.login_information, container,false);
		
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		
		img_info_close=(ImageView) view.findViewById(R.id.img_info_close);
		radio_information_ting=(RadioButton) view.findViewById(R.id.radio_information_ting);
		radio_information_kan=(RadioButton) view.findViewById(R.id.radio_information_kan);
		radio_information_chan=(RadioButton) view.findViewById(R.id.radio_information_chan);
		lv_login_information=(ListView) view.findViewById(R.id.lv_login_information);
		
		img_info_close.setOnClickListener(this);
		//��Ϊfragment�޷��������ذ�ť,���������ص�,���ûص�
		Home.SetonBackPressed=this;
		
		adapter = new Login_Information_Adapter(getActivity());
		//�������˳���¼�Ļص�
		adapter.OnLogoutClick=Fragment_login_information.this;
		lv_login_information.setAdapter(adapter);
		lv_login_information.setOnItemClickListener(this);
		//����Ǵ�Home������صĻ�,�м��ض���
		if(isHomeOpen)
			FragmentAnim.FragmentInAnimShow(getActivity(), view);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_info_close:

			//�˳�fragmentʱ,Home�ķ��ؽӿ��ֶ����
			Home.SetonBackPressed = null;
			onBackPressed();
			
			break;
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		//�������˳���¼,�û���Ϣˢ��,�����˳�
		Home.user=new User();
		Home.login_Refresh();
		Fragment_main.login_Refresh();
		Home.SetonBackPressed = null;
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Home.mHorizontalsv.Enabled=true;
		Home.isShow_login_information=false;
		ObjectAnimator animator=ObjectAnimator.ofFloat(Home.home_bottom, "translationY", Home.home_bottom_height , 0).setDuration(500);
		animator.start();
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_login_information.this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 4:	//�޸�����
			Revise_UserName_Dialog dialog = new Revise_UserName_Dialog(getActivity(), adapter);
			dialog.show();
			break;
		case 5:	//�޸��Ա�
			Revise_Sex_PopupWindow popupWindow = new Revise_Sex_PopupWindow(getActivity(), adapter);
			popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case 7:	//�޸�����
			Intent intent = new Intent(getActivity(), Revise_UserPass.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.animator.down_to_up, R.animator.notanim);
			break;
		}
	} 
	
}
