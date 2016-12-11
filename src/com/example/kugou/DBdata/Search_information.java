package com.example.kugou.DBdata;

import java.io.Serializable;
/**
 * 歌曲信息类
 * 
 * unicodeToString()		unicode编码转汉字
 * 
 * @author Administrator
 *
 */
public class Search_information implements Serializable {
	public String songId;				//歌曲id
	public String artistName;			//歌手
	public String songName;				//歌名
	public String albumName;			//专辑
	public String songPicSmall;			//歌曲小图片
	public String songPicBig;			//歌曲大图片
	public String songPicRadio;			//歌曲超大图片
	public String time;					//歌曲时间
	public String lrcLink;				//歌词路径
	public String songLink;				//歌曲路径
	public String format;				//歌曲类型	如mp3
	public String rate;					//歌曲清晰度
	public String size;					//歌曲大小
	public String songList;				//歌曲所在收藏歌单的名字
	public int songLength;				//歌曲的长度
	public int finished = 0;			//歌曲的下载完成读
	
	public static String unicodeToString(String unicode) {  
		
		StringBuffer string = new StringBuffer();
		  
	     String[] hex = unicode.split("\\\\u");
	     for (int i = 1; i < hex.length; i++) {
	    	 
	         // 转换出每一个代码点
	        int data = Integer.parseInt(hex[i].substring(0, 4), 16);
	  
	         // 追加成string
	         string.append((char) data + hex[i].substring(4));
	         
	     }
	  
	     return string.toString();
	}
}
