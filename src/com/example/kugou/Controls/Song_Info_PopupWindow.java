package com.example.kugou.Controls;

import com.example.kugou.MPService;
import com.example.kugou.R;
import com.example.kugou.DBdata.ImageLoader;
import com.example.kugou.DBdata.ImageLoader.OnPostExecuteListener;
import com.example.kugou.DBdata.Search_information;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
/**
 * 显示歌曲信息的漂浮窗
 * 
 * @author Administrator
 *
 */
public class Song_Info_PopupWindow extends PopupWindow implements OnPostExecuteListener{

	TextView tv_songinfopop_title,tv_songinfopop,tv_songinfopop_zj,tv_songinfopop_time,tv_songinfopop_rate,tv_songinfopop_format,tv_songinfopop_size;
	ImageView img_songinfopop;
	Button btn_songinfopop;
	
	public Song_Info_PopupWindow(Context context,Search_information search_information){
		
		View view = LayoutInflater.from(context).inflate(R.layout.song_info_pop_layout, null);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00); 
		setBackgroundDrawable(dw);
		setAnimationStyle(R.animator.down_to_up);
		
		tv_songinfopop_title= (TextView) view.findViewById(R.id.tv_songinfopop_title);
		tv_songinfopop= (TextView) view.findViewById(R.id.tv_songinfopop);
		tv_songinfopop_time = (TextView) view.findViewById(R.id.tv_songinfopop_time);
		tv_songinfopop_rate = (TextView) view.findViewById(R.id.tv_songinfopop_rate);
		tv_songinfopop_format = (TextView) view.findViewById(R.id.tv_songinfopop_format);
		tv_songinfopop_size = (TextView) view.findViewById(R.id.tv_songinfopop_size);
		tv_songinfopop_zj= (TextView) view.findViewById(R.id.tv_songinfopop_zj);
		img_songinfopop= (ImageView) view.findViewById(R.id.img_songinfopop);
		btn_songinfopop= (Button) view.findViewById(R.id.btn_songinfopop);
		
		tv_songinfopop_title.setText(search_information.artistName + " - " + search_information.songName);
		tv_songinfopop.setText("歌手: " + search_information.artistName);
		tv_songinfopop_time.setText("播放时长: " + search_information.time);
		tv_songinfopop_rate.setText("音质类型: " + search_information.rate);
		tv_songinfopop_format.setText("文件格式: " + search_information.format);
		tv_songinfopop_size.setText("文件大小: " + search_information.size);
		tv_songinfopop_zj.setText("歌曲专辑: " + search_information.albumName);
		btn_songinfopop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		new ImageLoader(Song_Info_PopupWindow.this).execute(search_information.songPicBig);
		
		setContentView(view);
	}

	@Override
	public void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		img_songinfopop.setImageBitmap(result);
	}
	
}
