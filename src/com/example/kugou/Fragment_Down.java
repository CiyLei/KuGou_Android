package com.example.kugou;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.DB.songlist.ThreadOADImpl_songlist;
import com.example.download.DownLoadService;
import com.example.download.DownLoadTask;
import com.example.kugou.Home.OnBackHandlerInterface;
import com.example.kugou.Adapter.Down_Adapter;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.FragmentAnim.FragmentAnim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
/**
 * ���ع���Ľ���
 * 
 * onCreateView()					��ʼ��,�ؼ���ʼ������Ӽ�����,�Ͳ�ѯ���ݿ��������������
 * downSearch_informationsAdd()		���������ݿ������������ӵ���������
 * DownMusic()						���غ�ֹͣ��������
 * receiver							���ܸ������ؽ��ȸ��¸�������ϵĹ㲥,����UIˢ��
 * onBackPressed()					�˳�����
 * 
 * @author Administrator
 *
 */
public class Fragment_Down extends Fragment implements OnBackHandlerInterface,OnClickListener {

	View view;
	ImageView img_homedown_close,img_homedown_Menu;
	ListView ls_down;
	Down_Adapter adapter;
	ThreadOADImpl_songlist impl;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.home_main_down, null);
		
		img_homedown_close = (ImageView) view.findViewById(R.id.img_homedown_close);
		img_homedown_Menu = (ImageView) view.findViewById(R.id.img_homedown_Menu);
		ls_down = (ListView) view.findViewById(R.id.ls_down);
		
		img_homedown_close.setOnClickListener(this);
		img_homedown_Menu.setOnClickListener(this);
		
		impl = new ThreadOADImpl_songlist(getActivity());
		//ȡ�����ݿ��е���������
		List<Search_information> search_informations = impl.getAllSongInfo();
		//��ӵ�����������
		for (Search_information search_information : search_informations) {
			downSearch_informationsAdd(search_information);
		}
		adapter = new Down_Adapter(getActivity(),DownLoadService.downSearch_informations,ls_down);
		ls_down.setAdapter(adapter);
		//��Ϊfragment�޷��������ذ�ť,���������ص�,���ûص�
		Home.SetonBackPressed=this;
		//���붯��
		FragmentAnim.FragmentInAnimShow(getActivity(), view);
		//ע��㲥
		IntentFilter filter = new IntentFilter();
        filter.addAction(DownLoadService.ACTION_FINISH);
        filter.addAction(DownLoadService.ACTION_UPDATE);
        getActivity().registerReceiver(receiver, filter);
		
		return view;
	}
	
	public void downSearch_informationsAdd(Search_information search_information){
		//���������ݿ������������ӵ���������
		for (Search_information search_information2 : DownLoadService.downSearch_informations) {
			if(search_information2.songId.equals(search_information.songId))
				return;
		}
		DownLoadService.downSearch_informations.add(search_information);
	}
	
	public void DownMusic(String Action,Search_information search_information){
		//���غ�ֹͣ��������
		Intent intent = new Intent(getActivity(), DownLoadService.class);
		intent.setAction(Action);
		intent.putExtra("search_information", search_information);
		getActivity().startService(intent);
		
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver(){
    	public void onReceive(Context context, Intent intent) {
    		//���յ����ؽ��ȸ��¹㲥
    		if(DownLoadService.ACTION_UPDATE.equals(intent.getAction())){
    			String id = intent.getStringExtra("id");
				int finished = intent.getIntExtra("finished", 0);
				//ȡ��������ID�ͽ���,����UIˢ��
				adapter.updateProgress(id, finished);
    		}else if (DownLoadService.ACTION_FINISH.equals(intent.getAction())) {
        		//���յ�������Ϲ㲥
				Search_information search_information = (Search_information) intent.getSerializableExtra("search_information");
				//ȡ��������Ϣ,����UIˢ��
				adapter.updateProgress(search_information.songId, 100);
				adapter.notifyDataSetChanged();
				//�����ʱ�ᵼ�³���,����ע��
//				Toast.makeText(getActivity(), search_information.songName + "�������", Toast.LENGTH_SHORT).show();
			}
    	};
    };

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//�˳�����
		FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_Down.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_homedown_close:
			//�˳�fragmentʱ,Home�ķ��ؽӿ��ֶ����
			Home.SetonBackPressed = null;
			//�˳�����
			FragmentAnim.FragmentInAnimEnd(getActivity(), view, Fragment_Down.this);
			break;
		case R.id.img_homedown_Menu:
			//�໬�˵��򿪻�ر�
			Home.mHorizontalsv.OpenOrCloseMenu();
			break;
		}
	}
}
