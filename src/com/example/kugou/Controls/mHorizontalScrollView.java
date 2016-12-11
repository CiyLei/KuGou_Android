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
 * 侧滑菜单控件
 * 
 * 
 * 
 * @author Administrator
 *
 */
public class mHorizontalScrollView extends HorizontalScrollView {
	
	private LinearLayout mWapper;
	//主界面
	private ViewGroup home_View;
	//菜单界面
	private ViewGroup home_menu_View;
	//是否第一次加载(第一次加载的话初始化	主界面和菜单界面)
	private boolean once = false;
	//是否是打开状态
	public boolean isOpen = false;
	//设置是否可用
	public boolean Enabled=true;
	//自身的宽带
	private int mScreenWidth;
	//菜单的宽度
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
		// 获取窗体
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		// 获取显示窗体的测量值给outMetrics
		mScreenWidth = outMetrics.widthPixels;
		//差不多菜单的宽带是屏幕宽度的1/6
		mMenuLeftPadding=(int) (mScreenWidth/(float)6);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//如果第一次加载,初始化界面
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
		//判断滑动是否过半,再决定是否打开或关闭
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
	//移动的时候,设置两个都变大变小
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
	//打开或关闭
	public void OpenOrCloseMenu() {
		if (isOpen)
			this.smoothScrollTo(0, 0);
		else
			this.smoothScrollTo(mScreenWidth, 0);
		isOpen = !isOpen;
	}
}
