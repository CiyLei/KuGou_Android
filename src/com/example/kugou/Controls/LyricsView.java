package com.example.kugou.Controls;

import com.example.kugou.MPService;
import com.example.kugou.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
/**
 * 歌词滚动的TextView
 * @author Administrator
 *
 */
public class LyricsView extends TextView {

	Context context;
	//控件的高度
	float ViewWidth;
	//控件的宽度
	float ViewHeight;
	//歌词当前行的画笔
	Paint currentPaint;
	//歌词不是当前行的画笔
	Paint notCurrentPaint;
	//歌词每行的高度
	float LyricHeight = 60;
	//歌词每个字的大小(其实会从res里面获取)
	float LyricSize = 28;
	//歌词当前行
	int index = -1;
	//歌词每行的透明梯度
	int LyricAlphaGradient = 40;  
	//歌词滚到到下一句所要的时间	单位毫秒
	int LyricMoveTime = 500;  
	
	//是否处于歌词移动到下一句状态(程序自己判断用的,不可改)
	boolean isLyricMove = false;  
	//歌词移动的次数 (程序自己判断用的,不可改)
	int LyricMoveGradient = 1; 
	
	public LyricsView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public LyricsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public LyricsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
		// TODO Auto-generated constructor stub
	}
	
	private void init() {
		// TODO Auto-generated method stub
		setFocusable(true);
		
		LyricSize = getResources().getDimension(R.dimen.lyrics_font_size);
		LyricHeight = LyricSize * 2;
		currentPaint = new Paint();  
        currentPaint.setAntiAlias(true);    //设置抗锯齿，让文字美观饱满  
        currentPaint.setColor(context.getResources().getColor(R.color.lyricscolor_select));
        currentPaint.setTextSize(LyricSize);
        currentPaint.setTextAlign(Paint.Align.CENTER);//设置文本对齐方式  
          
        notCurrentPaint = new Paint();  
        notCurrentPaint.setAntiAlias(true);  
        notCurrentPaint.setColor(context.getResources().getColor(R.color.lyricscolor));
        notCurrentPaint.setTextSize(LyricSize);
        notCurrentPaint.setTextAlign(Paint.Align.CENTER);  
        
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		
		if(canvas == null || MPService.lyrics == null) {  
            return;  
        }  
		
		if(index == -1){
			getIndex(MPService.mp.getCurrentPosition());
			return;
		}

		if(isLyricMove){
	        float tempY = ViewHeight / 2 + LyricHeight;  
	        /**
	         * MPService.Refresh 为时钟时间
	         * LyricMoveTime / MPService.Refresh 得 总共要移动多少次
	         * LyricHeight / (LyricMoveTime / 100) 的 每次移动的距离
	         */
	        int MoveHeight = (int) (LyricHeight / (LyricMoveTime / MPService.Refresh));
	        tempY = tempY - MoveHeight * LyricMoveGradient;   //计算出移动后的Y轴位置
	        
			canvas.drawText(MPService.lyrics.LyricsStr.get(index), ViewWidth / 2, tempY, currentPaint);
			for(int i = index - 1; i >= 0; i--) {   //计算每行的透明值和Y轴
	            notCurrentPaint.setAlpha(255 - (index - i - 1) * LyricAlphaGradient); 
				canvas.drawText(MPService.lyrics.LyricsStr.get(i), ViewWidth / 2, tempY - (index - i) * LyricHeight, notCurrentPaint); 
																				//中间行的Y轴距离 - (与中间行相差多少行) * 每行的距离 = 每行要移动到的Y轴位置
			}
	        for(int i = index + 1; i < MPService.lyrics.LyricsStr.size(); i++){
	            notCurrentPaint.setAlpha(255 - (i - index - 1) * LyricAlphaGradient);
	        	canvas.drawText(MPService.lyrics.LyricsStr.get(i), ViewWidth / 2, tempY + (i - index) * LyricHeight, notCurrentPaint);  
			}
	            
	        if(LyricMoveGradient == (int)(LyricMoveTime / MPService.Refresh)){   //如果移动次数 = 要移动的次数  初始化
	        	isLyricMove = false;
	        	LyricMoveGradient = 1;
	        }
	        LyricMoveGradient++;
	        return;
		}
		//如果不是滚动歌词	一般刷新就好
		canvas.drawText(MPService.lyrics.LyricsStr.get(index), ViewWidth / 2, ViewHeight / 2, currentPaint);
		float tempY = ViewHeight / 2;  
		for(int i = index - 1; i >= 0; i--) {  
            tempY = tempY - LyricHeight;  
            notCurrentPaint.setAlpha(255 - (index - i - 1) * LyricAlphaGradient);
            canvas.drawText(MPService.lyrics.LyricsStr.get(i), ViewWidth / 2, tempY, notCurrentPaint);  
        }  
		
        tempY = ViewHeight / 2;  
		for(int i = index + 1; i < MPService.lyrics.LyricsStr.size(); i++) {  
            tempY = tempY + LyricHeight;  
            notCurrentPaint.setAlpha(255 - (i - index - 1) * LyricAlphaGradient);
            canvas.drawText(MPService.lyrics.LyricsStr.get(i), ViewWidth / 2, tempY, notCurrentPaint);  
        }  
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		this.ViewHeight = h;
		this.ViewWidth = w;
	}
	//获取歌词到哪行了
	public void getIndex(int time){
		if(MPService.lyrics != null){
			for (int i = 0; i < MPService.lyrics.LyricsTime.size() - 1; i++) {
				if(time > MPService.lyrics.LyricsTime.get(i) && time < MPService.lyrics.LyricsTime.get(i + 1)){
					
					if( i!= 0 && i != index)
						isLyricMove=true;
					
					index = i;
					return ;
				}
			}
		}
	}
	
}
