package com.example.kugou.Controls;

import com.example.kugou.MPService;
import com.example.kugou.R;
import com.example.kugou.SongPlayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
/**
 * 修改播放顺序的漂浮窗(这个是在播放音乐主界面的)
 * 
 * @author Administrator
 *
 */
public class SongPlayer_Order_PopupWindow extends PopupWindow implements OnClickListener {

	Context context;
	int Width;
	
	public SongPlayer_Order_PopupWindow(Context context,int Width){
		this.context = context;
		this.Width = Width;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.songplayer_order_layout, null);
		setWidth(this.Width);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		
        ColorDrawable dw = new ColorDrawable(0x00); 
		setBackgroundDrawable(dw);
		setContentView(view);
		
		LinearLayout line_songplayeroledr_sxbf = (LinearLayout) view.findViewById(R.id.line_songplayeroledr_sxbf);
		LinearLayout line_songplayeroledr_sjbf = (LinearLayout) view.findViewById(R.id.line_songplayeroledr_sjbf);
		LinearLayout line_songplayeroledr_dqxh = (LinearLayout) view.findViewById(R.id.line_songplayeroledr_dqxh);
		
		line_songplayeroledr_sxbf.setOnClickListener(this);
		line_songplayeroledr_sjbf.setOnClickListener(this);
		line_songplayeroledr_dqxh.setOnClickListener(this);
		
		switch (MPService.PlayerOrder) {
		case MPService.PlayerOrder_Sequentially:
			line_songplayeroledr_sxbf.setBackgroundResource(R.color.blur_line_select);
			break;
		case MPService.PlayerOrder_Random:
			line_songplayeroledr_sjbf.setBackgroundResource(R.color.blur_line_select);
			break;
		case MPService.PlayerOrder_Loop:
			line_songplayeroledr_dqxh.setBackgroundResource(R.color.blur_line_select);
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.line_songplayeroledr_sxbf:
			MPService.PlayerOrder = MPService.PlayerOrder_Sequentially;
			SongPlayer.img_songplayer_order.setImageResource(R.drawable.songplayer_sxbf);
			Toast.makeText(context, "顺序播放", Toast.LENGTH_SHORT).show();
			break;
		case R.id.line_songplayeroledr_sjbf:
			MPService.PlayerOrder = MPService.PlayerOrder_Random;
			SongPlayer.img_songplayer_order.setImageResource(R.drawable.songplayer_sjbf);
			Toast.makeText(context, "随机播放", Toast.LENGTH_SHORT).show();
			break;
		case R.id.line_songplayeroledr_dqxh:
			MPService.PlayerOrder = MPService.PlayerOrder_Loop;
			SongPlayer.img_songplayer_order.setImageResource(R.drawable.songplayer_dqxh);
			Toast.makeText(context, "单曲循环", Toast.LENGTH_SHORT).show();
			break;
		}
		dismiss();
	}
	
}
