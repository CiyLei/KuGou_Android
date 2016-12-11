package com.example.kugou.DBdata;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Paint;
/**
 * 歌词类
 * 
 * init()		分析歌词
 * 
 * @author Administrator
 *
 */
public class Lyrics {

	String LyricsTxt;
	public List<Float> LyricsTime;
	public List<String> LyricsStr;
	List<Integer> isnull = new ArrayList<Integer>();
	
	public Lyrics(String LyricsTxt){
		this.LyricsTxt = LyricsTxt;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		
		LyricsTime = new ArrayList<Float>();
		LyricsStr = new ArrayList<String>();
		
		Pattern p = Pattern.compile("(?<=\\d{2}:\\d{2}.\\d{2}\\]).*?(?=\\[)");
		//取出每行歌词内存
		Matcher m = p.matcher(LyricsTxt);
		int index = 0;
		while(m.find()){
			if(m.group().trim().isEmpty())	//如果是空内容的话,不进行添加,并记录下位置
				isnull.add(index);
			else
				LyricsStr.add(m.group());
			index++;
		}
		
		index = 0;
		p = Pattern.compile("(?<=\\[)\\d{2}:\\d{2}.\\d{2}(?=\\])");
		//取出每行歌词时间
		m =p.matcher(LyricsTxt);
		while(m.find()){
			
			boolean flag = false;
			for (Integer i : isnull) {    //内容是空白的行数,不进行添加
				if(i == index)
					flag=true;
			}
			if(!flag){
				Float mm = Float.parseFloat(m.group().substring(0, 2));
				Float ss = Float.parseFloat(m.group().substring(3, 5));
				Float ff = Float.parseFloat(m.group().substring(6, 8));
				LyricsTime.add(ff*10 + ss*1000 + mm*60*1000);
			}
			
			index++;
			
		}
		
		
	}
	
}
