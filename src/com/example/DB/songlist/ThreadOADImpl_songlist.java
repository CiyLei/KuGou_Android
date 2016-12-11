package com.example.DB.songlist;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.R.layout;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kugou.DBdata.Search_information;
import com.example.kugou.download_entities.*;
/**
 * 数据库帮助实类
 * 对数据库的操作都封装在这个类里面
 * 为防止多线程同时访问 ,要用到单例模式
 * 
 * ThreadOADImpl_songlist()			初始化
 * insertSongInfo()					指定一个歌曲信息类,进行保存到数据库的操作
 * deleteSongInfo()					指定一个歌曲的ID,进行删除数据库内此ID的数据操作
 * updateSongInfo()					指定一个歌曲的ID和完成进度(防止下到一般程序退出的话,完成进度丢失的情况),进行更新数据条进度的操作
 * updateSongInfo_URL()				指定一个歌曲的ID和URL,更新歌曲URL信息(完成下载时会调用,将url变成本地路径)
 * getSongInfo()					指定一个歌曲ID,得到这个ID的所有歌曲信息类
 * isExists()						指点一个id来判断数据条是否存在
 * getAllSongInfo()					获取所有已下载的歌曲信息(无论是否下载完成)
 * getAllFinishSongInfo()			获取所有已下载的歌曲信息(要下载完成的)
 * 
 * @author Administrator
 *
 */
public class ThreadOADImpl_songlist implements ThreadDAO_songlist {

	private DB_songlist_Helper helper = null;
	
	public ThreadOADImpl_songlist(Context context){
		helper = DB_songlist_Helper.getInstance(context);
	}
	/**
	 * synchronized 同步
	 * 方法不会同时执行
	 */
	@Override
	public synchronized void insertSongInfo(Search_information  search_information) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("insert into songlist_info(name,url,picurl,author,albums,lyricsurl,time,format,rate,size,songid,songlist,songLength,finished) " +
				"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[]{search_information.songName,search_information.songLink,search_information.songPicBig,
				search_information.artistName,search_information.albumName,search_information.lrcLink,
				search_information.time,search_information.format,search_information.rate,
				search_information.size,search_information.songId,search_information.songList,search_information.songLength,
				search_information.finished});
		db.close();
	}

	@Override
	public synchronized void deleteSongInfo(String songid) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("delete from songlist_info where songid = ? ",
				new Object[]{songid});
		db.close();
	}
	
	@Override
	public synchronized void updateSongInfo(String songid,int finished) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("update songlist_info set finished = ? where songid = ?",
				new Object[]{finished,songid});
		db.close();
	}
	
	@Override
	public synchronized void updateSongInfo_URL(String songid,String URL) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("update songlist_info set url = ? where songid = ?",
				new Object[]{URL,songid});
		db.close();
	}

	@Override
	public Search_information getSongInfo(String songid) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //获得一个可写的数据库
		Search_information search_information = new Search_information();
		Cursor cursor = db.rawQuery("select * from songlist_info where songid = ?", new String[]{songid});
		while(cursor.moveToNext()){
			search_information.songName = cursor.getString(cursor.getColumnIndex("name"));
			search_information.songLink = cursor.getString(cursor.getColumnIndex("url"));
			search_information.songPicBig = cursor.getString(cursor.getColumnIndex("picurl"));
			search_information.artistName = cursor.getString(cursor.getColumnIndex("author"));
			search_information.albumName = cursor.getString(cursor.getColumnIndex("albums"));
			search_information.lrcLink = cursor.getString(cursor.getColumnIndex("lyricsurl"));
			search_information.time = cursor.getString(cursor.getColumnIndex("time"));
			search_information.format = cursor.getString(cursor.getColumnIndex("format"));
			search_information.rate = cursor.getString(cursor.getColumnIndex("rate"));
			search_information.size = cursor.getString(cursor.getColumnIndex("size"));
			search_information.songId = cursor.getString(cursor.getColumnIndex("songid"));
			search_information.songList = cursor.getString(cursor.getColumnIndex("songlist"));
			search_information.songLength = cursor.getInt(cursor.getColumnIndex("songLength"));
			search_information.finished = cursor.getInt(cursor.getColumnIndex("finished"));
		}
		cursor.close();
		db.close();
		return search_information;
	}

	@Override
	public boolean isExists(String songid) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //获得一个可写的数据库
		Cursor cursor = db.rawQuery("select * from songlist_info where songid = ? ", new String[]{songid});
		boolean exists = cursor.moveToNext();
		cursor.close();
		db.close();
		return exists;
	}
	@Override
	public List<Search_information> getAllSongInfo() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //获得一个可写的数据库
		List<Search_information> search_informations = new ArrayList<Search_information>();
		Cursor cursor = db.rawQuery("select * from songlist_info order by finished", new String[]{});
		while(cursor.moveToNext()){
			Search_information search_information = new Search_information();
			search_information.songName = cursor.getString(cursor.getColumnIndex("name"));
			search_information.songLink = cursor.getString(cursor.getColumnIndex("url"));
			search_information.songPicBig = cursor.getString(cursor.getColumnIndex("picurl"));
			search_information.artistName = cursor.getString(cursor.getColumnIndex("author"));
			search_information.albumName = cursor.getString(cursor.getColumnIndex("albums"));
			search_information.lrcLink = cursor.getString(cursor.getColumnIndex("lyricsurl"));
			search_information.time = cursor.getString(cursor.getColumnIndex("time"));
			search_information.format = cursor.getString(cursor.getColumnIndex("format"));
			search_information.rate = cursor.getString(cursor.getColumnIndex("rate"));
			search_information.size = cursor.getString(cursor.getColumnIndex("size"));
			search_information.songId = cursor.getString(cursor.getColumnIndex("songid"));
			search_information.songList = cursor.getString(cursor.getColumnIndex("songlist"));
			search_information.songLength = cursor.getInt(cursor.getColumnIndex("songLength"));
			search_information.finished = cursor.getInt(cursor.getColumnIndex("finished"));
			search_informations.add(search_information);
		}
		cursor.close();
		db.close();
		return search_informations;
	}
	
	public List<Search_information> getAllFinishSongInfo() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //获得一个可写的数据库
		List<Search_information> search_informations = new ArrayList<Search_information>();
		Cursor cursor = db.rawQuery("select * from songlist_info where finished = 100", new String[]{});
		while(cursor.moveToNext()){
			Search_information search_information = new Search_information();
			search_information.songName = cursor.getString(cursor.getColumnIndex("name"));
			search_information.songLink = cursor.getString(cursor.getColumnIndex("url"));
			search_information.songPicBig = cursor.getString(cursor.getColumnIndex("picurl"));
			search_information.artistName = cursor.getString(cursor.getColumnIndex("author"));
			search_information.albumName = cursor.getString(cursor.getColumnIndex("albums"));
			search_information.lrcLink = cursor.getString(cursor.getColumnIndex("lyricsurl"));
			search_information.time = cursor.getString(cursor.getColumnIndex("time"));
			search_information.format = cursor.getString(cursor.getColumnIndex("format"));
			search_information.rate = cursor.getString(cursor.getColumnIndex("rate"));
			search_information.size = cursor.getString(cursor.getColumnIndex("size"));
			search_information.songId = cursor.getString(cursor.getColumnIndex("songid"));
			search_information.songList = cursor.getString(cursor.getColumnIndex("songlist"));
			search_information.songLength = cursor.getInt(cursor.getColumnIndex("songLength"));
			search_information.finished = cursor.getInt(cursor.getColumnIndex("finished"));
			search_informations.add(search_information);
		}
		cursor.close();
		db.close();
		return search_informations;
	}

}
