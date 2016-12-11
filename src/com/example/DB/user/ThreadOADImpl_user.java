package com.example.DB.user;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.R.layout;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kugou.Home;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.download_entities.*;
/**
 * ���ݿ����ʵ��
 * 
 * ThreadOADImpl_user()			��ʼ��
 * insertUserInfo()				�����û���Ϣ(�Զ���ȡ��¼��Ϣ,����Ҫָ������)
 * deleteUserInfo()				ɾ���û���Ϣ(����)
 * getUser()					��ȡ���е��û���Ϣ(�ʺź�����),������
 * 
 * @author Administrator
 *
 */
public class ThreadOADImpl_user  {

	private DB_user_Helper helper = null;
	
	public ThreadOADImpl_user(Context context){
		helper = DB_user_Helper.getInstance(context);
	}
	/**
	 * synchronized ͬ��
	 * ��������ͬʱִ��
	 */
	public synchronized void insertUserInfo() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //���һ����д�����ݿ�
		db.execSQL("insert into user_info(user,pass) " +
				"values (?,?)",
				new Object[]{Home.user.userID,Home.user.pass});
		db.close();
	}

	public synchronized void deleteUserInfo() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //���һ����д�����ݿ�
		db.execSQL("delete from user_info",
				new Object[]{});
		db.close();
	}
	
	public List<String> getUser() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //���һ����д�����ݿ�
		List<String> User = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select * from user_info", new String[]{});
		while(cursor.moveToNext()){
			User.add(cursor.getString(cursor.getColumnIndex("user")));
			User.add(cursor.getString(cursor.getColumnIndex("pass")));
		}
		cursor.close();
		db.close();
		return User;
	}
}
