package com.example.kugou.FragmentAnim;

import com.example.kugou.Fragment_main;
import com.example.kugou.Home;
import com.example.kugou.R;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
/**
 * fragment���ɶ���������
 * 
 * FragmentInAnimShow()		���ص�fragment�ļ��ض���
 * FragmentInAnimEnd()		���ص�fragment���˳�����
 * FragmentOutAnimShow()	�����ص�fragment�ļ��ض���
 * FragmentOutAnimEnd()		�����ص�fragment�ļ��ض���
 * 
 * @author Administrator
 *
 */
public class FragmentAnim {
	public static void FragmentInAnimShow(Context context,View view){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// ��ȡ����
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		// ��ȡ��ʾ����Ĳ���ֵ��outMetrics
		ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view, "TranslationX", outMetrics.widthPixels, 0f).setDuration(500);
		objectAnimator.start();
		FragmentAnim.FragmentOutAnimEnd(Fragment_main.view);
	}
	public static void FragmentInAnimShow(Context context,View view,View outView){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// ��ȡ����
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		// ��ȡ��ʾ����Ĳ���ֵ��outMetrics
		ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view, "TranslationX", outMetrics.widthPixels, 0f).setDuration(500);
		objectAnimator.start();
		FragmentAnim.FragmentOutAnimEnd(outView);
	}
	public static void FragmentInAnimEnd(Context context,View view,final Fragment fragment){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// ��ȡ����
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		// ��ȡ��ʾ����Ĳ���ֵ��outMetrics
		ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view, "TranslationX", 0f, outMetrics.widthPixels).setDuration(500);
		objectAnimator.start();
    	FragmentAnim.FragmentOutAnimShow(Fragment_main.view);
		objectAnimator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				fragment.getFragmentManager().popBackStack();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public static void FragmentInAnimEnd(Context context,View view,final Fragment fragment,View outView){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// ��ȡ����
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		// ��ȡ��ʾ����Ĳ���ֵ��outMetrics
		ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view, "TranslationX", 0f, outMetrics.widthPixels).setDuration(500);
		objectAnimator.start();
    	FragmentAnim.FragmentOutAnimShow(outView);
		objectAnimator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				fragment.getFragmentManager().popBackStack();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public static void FragmentOutAnimShow(View view){
		ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f).setDuration(500);
		ObjectAnimator objectAnimator1=ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f).setDuration(500);
		objectAnimator.start();
		objectAnimator1.start();
	}
	public static void FragmentOutAnimEnd(View view){
		ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f).setDuration(500);
		ObjectAnimator objectAnimator1=ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f).setDuration(500);
		objectAnimator.start();
		objectAnimator1.start();
	}
}
