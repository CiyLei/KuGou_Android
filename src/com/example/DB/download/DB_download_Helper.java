package com.example.DB.download;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 *	保存下载信息数据库帮助雷
 *
 *	DB_download_Helper()		初始化(设置私有,进行同步)
 *	getInstance()				获取数据库实类
 *
 * @author Administrator
 */
public class DB_download_Helper extends SQLiteOpenHelper {

	private static final String DB_NAME = "download.db";	//数据库名称
	private static DB_download_Helper helper;
	private static final int VERSION = 1;					//数据库版本
	private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement," +
			"thread_id integer, url text,start integer,end integer,finished integer)";
	private static final String SQL_DROP = "drop table if exists thread_info";  //删表语句   如果存在thread_info表
	
	private DB_download_Helper(Context context) {				//数据库初始化
		super(context, DB_NAME, null, VERSION);  
		// TODO Auto-generated constructor stub
	}
	
	public static DB_download_Helper getInstance(Context context){   //实现单例模式 防止多个线程同时访问数据库
		if(helper == null){
			helper = new DB_download_Helper(context);
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
