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
 * �������ֵĽ���
 * 
 * onCreateView()					��ʼ��,�ؼ���ʼ������Ӽ�����,�Ͳ�ѯ���ݿ�����ı���������Ϣ
 * PlayerOrder()					�ı䲥�����ֵ�˳��,������UIˢ��
 * playMusic()						���ź�ֹͣ����
 * onBackPressed()					�˳�����
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
	String[] txt=new String[]{"˳�򲥷�","�������","����ѭ��"};
	
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
		//��һ�μ���,ͼ���벥��˳���Ӧһ��
		PlayerOrder(MPService.PlayerOrder);
		//��Ϊfragment�޷��������ذ�ť,���������ص�,���ûص�
		Home.SetonBackPressed=this;
		//���붯��
		FragmentAnim.FragmentInAnimShow(getActivity(), view);
		
		ThreadOADImpl_songlist impl = new ThreadOADImpl_songlist(getActivity());
		//ȡ������������ϵĸ�����Ϣ
		search_informations = impl.getAllFinishSongInfo();
		
		adapter = new Wxh_Adapter(getActivity(), search_informations, view, ls_local);
		//��ʶһ��,adapter�����������Ķ���
		adapter.isLocal = true;
		ls_local.setAdapter(adapter);
		
		return view;
	}
	
	public void PlayerOrder(int position){
		//ͼ���벥��˳���Ӧһ��
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
		//�˳�����
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_Local.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.radio_local_pop_show :
			//��ʼ��Ư������
			popWindow=new SpinerPopWindow(getActivity(), img, txt, select_index,radio_local_pop_show.getWidth() + 100);
			//���ûص�,������������ʲô����˳��
			popWindow.SetOnItemClick=Fragment_Local.this;
			//��ʾλ��
			popWindow.showAsDropDown(radio_local_pop_show,10 ,-5);
			//ͼƬ��ת
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
			//�˳�fragmentʱ,Home�ķ��ؽӿ��ֶ����
			Home.SetonBackPressed = null;
			//�˳�����
			FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_Local.this);
			break;
		case R.id.img_local_Menu:
			//�໬�˵��򿪻�ر�
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
			//������Ư�������ѡ�񲥷�˳��
			popWindow.dismiss();
			//����UI
			PlayerOrder(position);
			break;

		case R.id.ls_local:
			//��������
			playMusic(MPService.MusicPlayer);
			//��Ӳ��Ÿ���
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
