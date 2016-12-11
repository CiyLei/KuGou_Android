package com.example.DB.user;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * �����û����ʺź�������Ϣ
 * @author Administrator
 *
 */
public class DB_user_Helper extends SQLiteOpenHelper {

	private static final String DB_NAME = "user.db";	//���ݿ�����
	private static DB_user_Helper helper;
	private static final int VERSION = 1;					//���ݿ�汾
	private static final String SQL_CREATE = "create table user_info(_id integer primary key autoincrement," +
			"user text,pass text)";
	private static final String SQL_DROP = "drop table if exists user_info";  //ɾ�����   �������thread_info��
	
	public DB_user_Helper(Context context) {				//���ݿ��ʼ��
		super(context, DB_NAME, null, VERSION);  
		// TODO Auto-generated constructor stub
	}
	
	public static DB_user_Helper getInstance(Context context){   //ʵ�ֵ���ģʽ ��ֹ����߳�ͬʱ�������ݿ�
		if(helper == null){
			helper = new DB_user_Helper(context);
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