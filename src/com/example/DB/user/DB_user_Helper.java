package com.example.DB.user;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 保存用户的帐号和密码信息
 * @author Administrator
 *
 */
public class DB_user_Helper extends SQLiteOpenHelper {

	private static final String DB_NAME = "user.db";	//数据库名称
	private static DB_user_Helper helper;
	private static final int VERSION = 1;					//数据库版本
	private static final String SQL_CREATE = "create table user_info(_id integer primary key autoincrement," +
			"user text,pass text)";
	private static final String SQL_DROP = "drop table if exists user_info";  //删表语句   如果存在thread_info表
	
	public DB_user_Helper(Context context) {				//数据库初始化
		super(context, DB_NAME, null, VERSION);  
		// TODO Auto-generated constructor stub
	}
	
	public static DB_user_Helper getInstance(Context context){   //实现单例模式 防止多个线程同时访问数据库
		if(helper == null){
			helper = new DB_user_Helper(context);
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
