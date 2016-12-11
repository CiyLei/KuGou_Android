package com.example.kugou.DBdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.example.kugou.Home;
/**
 * 用户登录信息分析类
 * 
 * getUser()			根据网页内容,转换成User类
 * GetSongList()		根据网页内容,返回歌单列表
 * GetSongCollect()		根据网页内容,歌曲信息列表
 * 
 * @author Administrator
 *
 */
public class select_analysis {

	public static User getUser(String data) {
		User user = new User();
		String[] ss = data.replace("[", "").replace("]", "").split("'");
		user.isLogin = true;
		user.userID = ss[0];
		user.pass = user.MD5(ss[1]);
		user.name = ss[2];
		user.sex = Boolean.parseBoolean(ss[3]);
		user.registeredtime=ss[4].replace("/", "-");
		return user;
	}
	
	public static List<String> GetSongList(String data){
		 List<String> SongList = new ArrayList<String>();
		 Pattern pattern= Pattern.compile("(?<=\\[).+?(?=\\])");
		 Matcher matcher=pattern.matcher(data);
		 while(matcher.find()){
			 String s= matcher.group();
			 String[] ss= s.split("'");
			 SongList.add(ss[1]);
		 }
		 return SongList;
	}
	
	public static List<Search_information> GetSongCollect(String data){
		 List<Search_information> SongCollect = new ArrayList<Search_information>();
		 Pattern pattern= Pattern.compile("(?<=\\[).+?(?=\\])");
		 Matcher matcher=pattern.matcher(data);
		 Search_information search_information;
		 while(matcher.find()){
			 search_information = new Search_information();
			 String s= matcher.group();
			 String[] ss= s.split("'");
			 if(ss[0].equals(Home.user.userID)){
				 search_information.songName = ss[1].trim();
				 search_information.songLink = ss[2].trim();
				 search_information.songPicBig = ss[3].trim();
				 search_information.artistName = ss[4].trim();
				 search_information.albumName = ss[5].trim();
				 search_information.lrcLink = ss[6].trim();
				 search_information.time = ss[7].trim();
				 search_information.format = ss[8].trim();
				 search_information.rate = ss[9].trim();
				 search_information.size = ss[10].trim();
				 search_information.songId = ss[11].trim();
				 search_information.songList = ss[12].trim();
				 SongCollect.add(search_information);
			 }
		 }
		 return SongCollect;
	}

}
