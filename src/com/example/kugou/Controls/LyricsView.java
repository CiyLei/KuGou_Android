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
 * ��ʹ�����TextView
 * @author Administrator
 *
 */
public class LyricsView extends TextView {

	Context context;
	//�ؼ��ĸ߶�
	float ViewWidth;
	//�ؼ��Ŀ��
	float ViewHeight;
	//��ʵ�ǰ�еĻ���
	Paint currentPaint;
	//��ʲ��ǵ�ǰ�еĻ���
	Paint notCurrentPaint;
	//���ÿ�еĸ߶�
	float LyricHeight = 60;
	//���ÿ���ֵĴ�С(��ʵ���res�����ȡ)
	float LyricSize = 28;
	//��ʵ�ǰ��
	int index = -1;
	//���ÿ�е�͸���ݶ�
	int LyricAlphaGradient = 40;  
	//��ʹ�������һ����Ҫ��ʱ��	��λ����
	int LyricMoveTime = 500;  
	
	//�Ƿ��ڸ���ƶ�����һ��״̬(�����Լ��ж��õ�,���ɸ�)
	boolean isLyricMove = false;  
	//����ƶ��Ĵ��� (�����Լ��ж��õ�,���ɸ�)
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
        currentPaint.setAntiAlias(true);    //���ÿ���ݣ����������۱���  
        currentPaint.setColor(context.getResources().getColor(R.color.lyricscolor_select));
        currentPaint.setTextSize(LyricSize);
        currentPaint.setTextAlign(Paint.Align.CENTER);//�����ı����뷽ʽ  
          
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
	         * MPService.Refresh Ϊʱ��ʱ��
	         * LyricMoveTime / MPService.Refresh �� �ܹ�Ҫ�ƶ����ٴ�
	         * LyricHeight / (LyricMoveTime / 100) �� ÿ���ƶ��ľ���
	         */
	        int MoveHeight = (int) (LyricHeight / (LyricMoveTime / MPService.Refresh));
	        tempY = tempY - MoveHeight * LyricMoveGradient;   //������ƶ����Y��λ��
	        
			canvas.drawText(MPService.lyrics.LyricsStr.get(index), ViewWidth / 2, tempY, currentPaint);
			for(int i = index - 1; i >= 0; i--) {   //����ÿ�е�͸��ֵ��Y��
	            notCurrentPaint.setAlpha(255 - (index - i - 1) * LyricAlphaGradient); 
				canvas.drawText(MPService.lyrics.LyricsStr.get(i), ViewWidth / 2, tempY - (index - i) * LyricHeight, notCurrentPaint); 
																				//�м��е�Y����� - (���м�����������) * ÿ�еľ��� = ÿ��Ҫ�ƶ�����Y��λ��
			}
	        for(int i = index + 1; i < MPService.lyrics.LyricsStr.size(); i++){
	            notCurrentPaint.setAlpha(255 - (i - index - 1) * LyricAlphaGradient);
	        	canvas.drawText(MPService.lyrics.LyricsStr.get(i), ViewWidth / 2, tempY + (i - index) * LyricHeight, notCurrentPaint);  
			}
	            
	        if(LyricMoveGradient == (int)(LyricMoveTime / MPService.Refresh)){   //����ƶ����� = Ҫ�ƶ��Ĵ���  ��ʼ��
	        	isLyricMove = false;
	        	LyricMoveGradient = 1;
	        }
	        LyricMoveGradient++;
	        return;
		}
		//������ǹ������	һ��ˢ�¾ͺ�
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
	//��ȡ��ʵ�������
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
