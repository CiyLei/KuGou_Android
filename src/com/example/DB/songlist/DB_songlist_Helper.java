package com.example.DB.songlist;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 下载到本地的歌是已ID命名的,所以来个songlist数据库对照表,存放的是这个ID歌曲的详细信息
 * @author Administrator
 *
 */
public class DB_songlist_Helper extends SQLiteOpenHelper {

	private static final String DB_NAME = "songlist.db";	//数据库名称
	private static DB_songlist_Helper helper;
	private static final int VERSION = 3;					//数据库版本
	private static final String SQL_CREATE = "create table songlist_info(_id integer primary key autoincrement," +
			"name text, url text,picurl text,author text,albums text,lyricsurl text," +
			"time text,format text,rate text,size text,songid text,songlist text,songLength integer,finished integer)";
	private static final String SQL_DROP = "drop table if exists songlist_info";  //删表语句   如果存在thread_info表
	
	public DB_songlist_Helper(Context context) {				//数据库初始化
		super(context, DB_NAME, null, VERSION);  
		// TODO Auto-generated constructor stub
	}
	
	public static DB_songlist_Helper getInstance(Context context){   //实现单例模式 防止多个线程同时访问数据库
		if(helper == null){
			helper = new DB_songlist_Helper(context);
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		//数据库创建
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		//数据库更新
		// TODO Auto-generated method stub
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_CREATE);
	}

}
