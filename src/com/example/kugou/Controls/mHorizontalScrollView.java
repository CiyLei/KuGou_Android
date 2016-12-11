package com.example.kugou.Controls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
/**
 * �໬�˵��ؼ�
 * 
 * 
 * 
 * @author Administrator
 *
 */
public class mHorizontalScrollView extends HorizontalScrollView {
	
	private LinearLayout mWapper;
	//������
	private ViewGroup home_View;
	//�˵�����
	private ViewGroup home_menu_View;
	//�Ƿ��һ�μ���(��һ�μ��صĻ���ʼ��	������Ͳ˵�����)
	private boolean once = false;
	//�Ƿ��Ǵ�״̬
	public boolean isOpen = false;
	//�����Ƿ����
	public boolean Enabled=true;
	//����Ŀ��
	private int mScreenWidth;
	//�˵��Ŀ��
	private int mMenuLeftPadding;

	public mHorizontalScrollView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public mHorizontalScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public mHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		// ��ȡ����
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		// ��ȡ��ʾ����Ĳ���ֵ��outMetrics
		mScreenWidth = outMetrics.widthPixels;
		//���˵��Ŀ������Ļ��ȵ�1/6
		mMenuLeftPadding=(int) (mScreenWidth/(float)6);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//�����һ�μ���,��ʼ������
		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			home_View = (ViewGroup) mWapper.getChildAt(0);
			home_menu_View = (ViewGroup) mWapper.getChildAt(1);

			home_View.getLayoutParams().width = mScreenWidth;
			home_menu_View.getLayoutParams().width = mScreenWidth- mMenuLeftPadding;
			once = true;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		
		if(!Enabled)
			return true;
		
		switch (ev.getAction()) {
		//�жϻ����Ƿ����,�پ����Ƿ�򿪻�ر�
		case MotionEvent.ACTION_UP:

			int scrlloX = getScrollX();
			if (scrlloX > mScreenWidth / 2) {
				this.smoothScrollTo(mScreenWidth - mMenuLeftPadding, 0);
				isOpen = true;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = false;
			}

			return true;
		}
		
		return super.onTouchEvent(ev);
	}
	//�ƶ���ʱ��,��������������С
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		
		float scale=l*1.0f/mScreenWidth;
		
		float rightScale=0.8f+0.2f*scale;
		float leftScale=1.0f-scale*0.3f;
		
		home_View.setPivotX(home_View.getWidth());
		home_View.setPivotY(home_View.getHeight()/2);
		home_View.setScaleX(leftScale);
		home_View.setScaleY(leftScale);
		home_View.setAlpha(leftScale);

		home_menu_View.setPivotX(0);
		home_menu_View.setPivotY(home_menu_View.getHeight()/2);
		home_menu_View.setScaleX(rightScale);
		home_menu_View.setScaleY(rightScale);
		
	}
	//�򿪻�ر�
	public void OpenOrCloseMenu() {
		if (isOpen)
			this.smoothScrollTo(0, 0);
		else
			this.smoothScrollTo(mScreenWidth, 0);
		isOpen = !isOpen;
	}
}
