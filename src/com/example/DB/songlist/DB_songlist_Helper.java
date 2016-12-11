package com.example.DB.songlist;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * ���ص����صĸ�����ID������,��������songlist���ݿ���ձ�,��ŵ������ID��������ϸ��Ϣ
 * @author Administrator
 *
 */
public class DB_songlist_Helper extends SQLiteOpenHelper {

	private static final String DB_NAME = "songlist.db";	//���ݿ�����
	private static DB_songlist_Helper helper;
	private static final int VERSION = 3;					//���ݿ�汾
	private static final String SQL_CREATE = "create table songlist_info(_id integer primary key autoincrement," +
			"name text, url text,picurl text,author text,albums text,lyricsurl text," +
			"time text,format text,rate text,size text,songid text,songlist text,songLength integer,finished integer)";
	private static final String SQL_DROP = "drop table if exists songlist_info";  //ɾ�����   �������thread_info��
	
	public DB_songlist_Helper(Context context) {				//���ݿ��ʼ��
		super(context, DB_NAME, null, VERSION);  
		// TODO Auto-generated constructor stub
	}
	
	public static DB_songlist_Helper getInstance(Context context){   //ʵ�ֵ���ģʽ ��ֹ����߳�ͬʱ�������ݿ�
		if(helper == null){
			helper = new DB_songlist_Helper(context);
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		//���ݿⴴ��
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		//���ݿ����
		// TODO Auto-generated method stub
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_CREATE);
	}

}
