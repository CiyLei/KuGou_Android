package com.example.DB.download;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 *	����������Ϣ���ݿ������
 *
 *	DB_download_Helper()		��ʼ��(����˽��,����ͬ��)
 *	getInstance()				��ȡ���ݿ�ʵ��
 *
 * @author Administrator
 */
public class DB_download_Helper extends SQLiteOpenHelper {

	private static final String DB_NAME = "download.db";	//���ݿ�����
	private static DB_download_Helper helper;
	private static final int VERSION = 1;					//���ݿ�汾
	private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement," +
			"thread_id integer, url text,start integer,end integer,finished integer)";
	private static final String SQL_DROP = "drop table if exists thread_info";  //ɾ�����   �������thread_info��
	
	private DB_download_Helper(Context context) {				//���ݿ��ʼ��
		super(context, DB_NAME, null, VERSION);  
		// TODO Auto-generated constructor stub
	}
	
	public static DB_download_Helper getInstance(Context context){   //ʵ�ֵ���ģʽ ��ֹ����߳�ͬʱ�������ݿ�
		if(helper == null){
			helper = new DB_download_Helper(context);
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
