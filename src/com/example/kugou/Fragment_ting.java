package com.example.kugou;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.DB.songlist.ThreadOADImpl_songlist;
import com.example.download.DownLoadService;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.FragmentAnim.FragmentAnim;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 主界面的中间界面
 * 
 * 都与前面大同小异,不注释了
 * 
 * @author Administrator
 *
 */
public class Fragment_ting extends Fragment implements OnClickListener,TextWatcher,OnPostExecuteListener,OnFocusChangeListener,OnKeyListener,OnItemClickListener {


	AutoCompleteTextView home_search_edit;
	RadioButton radio_wxh;    //
	RadioButton radio_songlist;    //
	RadioButton radio_down;
	ArrayAdapter<String> arrayAdapter;
	ImageView img_search,img_local_player;
	Button btn_search;
	public static TextView tv_songlist_num;
	LinearLayout linear_frag_local;
	
	LinearLayout home_main_main_bottom_Line;  //底部填充
	boolean flag;
	boolean flag_hc;
	
	List<Search_information> search_informations = new ArrayList<Search_information>();
	
	public static String search_value="";
	ThreadOADImpl_songlist impl;
	
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view =inflater.inflate(R.layout.home_main_main, container, false);

        home_search_edit=(AutoCompleteTextView) view.findViewById(R.id.home_search_edit);  //
        radio_wxh=(RadioButton) view.findViewById(R.id.radio_wxh);    //
        radio_songlist=(RadioButton) view.findViewById(R.id.radio_songlist);
        radio_down = (RadioButton) view.findViewById(R.id.radio_down);
        home_main_main_bottom_Line=(LinearLayout) view.findViewById(R.id.home_main_main_bottom_Line);
        img_search=(ImageView) view.findViewById(R.id.img_search);
        img_local_player=(ImageView) view.findViewById(R.id.img_local_player);
        btn_search=(Button) view.findViewById(R.id.btn_search);
        tv_songlist_num=(TextView) view.findViewById(R.id.tv_songlist_num);
        linear_frag_local=(LinearLayout) view.findViewById(R.id.linear_frag_local);

		home_search_edit.clearFocus();
		home_main_main_bottom_Line.getLayoutParams().height=80;
        
		radio_wxh.setOnClickListener(this);    //
		radio_songlist.setOnClickListener(this);
		radio_down.setOnClickListener(this);
		home_search_edit.addTextChangedListener(this);
		img_search.setOnClickListener(this);
		img_local_player.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		home_search_edit.setOnFocusChangeListener(this);
		home_search_edit.setOnKeyListener(this);
		home_search_edit.setOnItemClickListener(this);
		linear_frag_local.setOnClickListener(this);
		
		impl = new ThreadOADImpl_songlist(getActivity());
		search_informations = impl.getAllFinishSongInfo();
		tv_songlist_num.setText(search_informations.size() + "首");
        
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		
		switch (v.getId()) {

		case R.id.radio_wxh:   //
		
			Fragment_wxh wxh=new Fragment_wxh();
			beginTransaction.add(R.id.home_fragm,wxh);
			beginTransaction.addToBackStack(null);
			beginTransaction.commit();
			
			break;
			
		case R.id.radio_songlist:
			
			Fragment_songlist songlist = new Fragment_songlist();
			beginTransaction.add(R.id.home_fragm,songlist);
			beginTransaction.addToBackStack(null);
			beginTransaction.commit();
			
			break;
			
		case R.id.radio_down:

			Fragment_Down down = new Fragment_Down();
			beginTransaction.add(R.id.home_fragm,down);
			beginTransaction.addToBackStack(null);
			beginTransaction.commit();
			
			break;
			
		case R.id.img_search:
			
			
			
			break;
			
		case R.id.btn_search:
			
			home_search_edit.setText("");
			home_search_edit.clearFocus();
			
			break;
			
		case R.id.img_local_player:
			
			if(search_informations.size()>0){
				playMusic(MPService.MusicPlayer);
				Random random = new Random();
				int index = random.nextInt(search_informations.size());
				MPService.SongListAdd(search_informations, index);
			}else{
				search_informations = impl.getAllFinishSongInfo();
			}
			
			break;
			
		case R.id.linear_frag_local:
			
			Fragment_Local local = new Fragment_Local();
			beginTransaction.add(R.id.home_fragm,local);
			beginTransaction.addToBackStack(null);
			beginTransaction.commit();
			
			break;
		}
		
		if(v.getId()!=R.id.home_search_edit)
			home_search_edit.clearFocus();
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	
	public void playMusic(int action) {  
        Intent intent = new Intent();  
        intent.putExtra("MSG", action);  
        intent.setClass(getActivity(), MPService.class);  
        getActivity().startService(intent);  
    }  

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

		String txt=home_search_edit.getText().toString();
		if (!txt.isEmpty() && !flag) {
			
			txt = URLEncoder.encode(txt);
			String url = "http://sug.music.baidu.com/info/suggestion?format=json&word="
					+ txt + "&version=2&from=0";
			new dataLoad(Fragment_ting.this).execute(url);
		}
		flag=false;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		
		List<String> arr=new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("(?<=songname\":\").+?(?=\")");
		Matcher matcher =pattern.matcher(result);
		while (matcher.find()) {
			String s = Search_information.unicodeToString(matcher.group());
			arr.add(s);
		}

		arrayAdapter =new ArrayAdapter<String>(getActivity(), R.layout.ting_search_prompt, R.id.tv_prompt, arr);
		home_search_edit.setAdapter(arrayAdapter);
		flag=true;
		home_search_edit.setText(home_search_edit.getText().toString());
		home_search_edit.setSelection(home_search_edit.getText().toString().length());
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			btn_search.setVisibility(View.VISIBLE);
			img_search.setVisibility(View.GONE);
		}
		else{
			btn_search.setVisibility(View.GONE);
			img_search.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == 66 && !flag_hc){
			ToSearch();
		}
		flag_hc = !flag_hc;
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		ToSearch();
	}  
	
	public void ToSearch(){
		search_value=home_search_edit.getText().toString();
		home_search_edit.setText("");
		home_search_edit.clearFocus();
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		Search search=new Search();
    	beginTransaction.add(R.id.home_fragm, search);
		beginTransaction.addToBackStack(null);
		beginTransaction.commit();
	}
}
