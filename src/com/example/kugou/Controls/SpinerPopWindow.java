package com.example.kugou.Controls;

import com.example.kugou.R;
import com.example.kugou.Adapter.Wxh_PopWindow_Adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
/**
 * 修改播放顺序的漂浮窗(这个是在主界面的)
 * 
 * @author Administrator
 *
 */
public class SpinerPopWindow extends PopupWindow implements OnItemClickListener {

	Context mContext;
    ListView mListView;  
    int mWidth;
    
    public OnItemClickListener_Pop SetOnItemClick;
    
    public interface OnItemClickListener_Pop{
    	public void onItemClick(AdapterView<?> parent, View view, int position,long id);
    }
	
	public SpinerPopWindow(Context context,int[] img,String[] txt, int select_index, int Width){
		mContext=context;
		mWidth = Width;
		init(img,txt,select_index);
	}

	public void init(int[] img,String[] txt, int select_index) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(mContext).inflate(R.layout.wxh_popwindow, null);
		setWidth(mWidth);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00); 
		setBackgroundDrawable(dw);
		
		mListView=(ListView) view.findViewById(R.id.ls_wxh_popwindow);
		Wxh_PopWindow_Adapter adapter=new Wxh_PopWindow_Adapter(mContext, img, txt, select_index);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		
		setContentView(view);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(SetOnItemClick!=null)
			SetOnItemClick.onItemClick(parent, view, position, id);
	}
}
