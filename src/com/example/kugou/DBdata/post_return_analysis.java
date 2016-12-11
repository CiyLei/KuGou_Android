package com.example.kugou.DBdata;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
/**
 * 网页信息返回分析类(针对自己的网页接口)
 * 
 * getIsSuccess()			指定网页内容,返回是否成功(如登录是否成功,添加是否成功...)
 * getIsSuccessTxt()		将成功是否转化为汉字
 * getMsg()					获取失败信息,并返回
 * getSearch_information()	将网页信息转化成功歌曲信息类
 * Patterns()				指定正则和内容,返回符合的信息
 * RateToTxt()				因为我的音乐是从百度偷的,百度标识音乐清晰度是用数字表示的,这里转化成通俗易懂的汉字
 * SizeToTxt()				同样百度音乐的大小单位是B,这里转换成MB
 * TimeToTxt()				同样百度音乐的音乐时长单位是秒,这里转换成3:10形式的
 * 
 * @author Administrator
 *
 */
public class post_return_analysis {

	public static boolean getIsSuccess(String post_return_information){
		String s =post_return_information.substring(0,1);
		if(s.equals("F")){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static String getIsSuccessTxt(String post_return_information){
		String s =post_return_information.substring(0,1);
		if(s.equals("F")){
			return "失败";
		}
		else if(s.equals("T")){
			return "成功";
		}
		else {
			return "未知";
		}
	}
	
	public static String getMsg(String post_return_information){
		if(post_return_information.indexOf("msg:")!=-1)
			return post_return_information.substring(post_return_information.indexOf("msg:") + 4);
		return "";
	}
	
	public static Search_information getSearch_information(String result){
		Search_information search_information = new Search_information();
		search_information.songId = Patterns("(?<=songId\":).+?(?=,)",result);
		search_information.songName = Search_information.unicodeToString(Patterns("(?<=songName\":\").+?(?=\")", result));
		search_information.artistName = Search_information.unicodeToString(Patterns("(?<=artistName\":\").+?(?=\")", result));
		search_information.albumName = Search_information.unicodeToString(Patterns("(?<=albumName\":\").+?(?=\")", result));
		search_information.songPicSmall = Patterns("(?<=songPicSmall\":\").+?(?=\")", result).replace("\\", "");
		search_information.songPicBig = Patterns("(?<=songPicBig\":\").+?(?=\")", result).replace("\\", "");
		search_information.songPicRadio = Patterns("(?<=songPicRadio\":\").+?(?=\")", result).replace("\\", "");
		search_information.time = TimeToTxt(Patterns("(?<=time\":).+?(?=,)", result));
		search_information.lrcLink = Patterns("(?<=lrcLink\":\").+?(?=\")", result).replace("\\", "");
		search_information.songLink = Patterns("(?<=songLink\":\").+?(?=\")", result).replace("\\", "");
		search_information.format = Patterns("(?<=format\":\").+?(?=\",)", result);
		search_information.rate = RateToTxt(Patterns("(?<=rate\":).+?(?=,)", result));
		search_information.size = SizeToTxt(Patterns("(?<=size\":).+?(?=,)", result));
		return search_information;
	}
	
	public static String Patterns(String patternString,String result){
		String valueString="";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(result);
		if(matcher.find())
			valueString=matcher.group();
		return valueString;
	}
	
	public static String RateToTxt(String rate){
		String ratetxt = "";
		switch (rate.trim()) {
		case "128":
			ratetxt = "标准音质";
			break;
		case "192":
			ratetxt = "高清音质";
			break;
		case "256":
			ratetxt = "无损音质";
			break;
		default:
			ratetxt = "流畅音质";
			break;
		}
		return ratetxt;
	}
	
	public static String SizeToTxt(String size){
		float sizetxt= Float.parseFloat(size.trim());
		BigDecimal bd = new BigDecimal(sizetxt / 1024 / 1024);
		sizetxt = bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		return (sizetxt + "MB");
	}
	
	public static String TimeToTxt(String time){
		float timetxt = Float.parseFloat(time.trim());
		int h = (int)timetxt / 60;
		int s = (int)timetxt % 60;
		return ( (h<10 ? "0" + h : h) + ":" + (s<10 ? "0" + s : s));
	}
}
