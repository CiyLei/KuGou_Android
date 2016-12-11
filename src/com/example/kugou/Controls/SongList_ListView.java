package com.example.kugou.Controls;

import com.example.kugou.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * ܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳܳ
 * addHeaderView֮�� λ��ȫ���������һλ adapterҲ��Ӱ�� ���Է���
 * @author Administrator
 *
 */
public class SongList_ListView extends ListView {

	View heardView;
	int heardHeight;
	public static ImageView img_songlist_heard;
	
	public SongList_ListView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	public SongList_ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	public SongList_ListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public void init(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		heardView = inflater.inflate(R.layout.songlist_listview_heard, null);
		
		img_songlist_heard = (ImageView) heardView.findViewById(R.id.img_songlist_heard);
		
		measureView(heardView);
		heardHeight = heardView.getMeasuredHeight();
		heardView.invalidate();
		this.addHeaderView(heardView);
	}
	
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}
	
}
