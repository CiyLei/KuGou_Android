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
 * 数据库帮助实类
 * 
 * ThreadOADImpl_user()			初始化
 * insertUserInfo()				插入用户信息(自动获取登录信息,不需要指定参数)
 * deleteUserInfo()				删除用户信息(所有)
 * getUser()					获取所有的用户信息(帐号和密码),并返回
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
	 * synchronized 同步
	 * 方法不会同时执行
	 */
	public synchronized void insertUserInfo() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("insert into user_info(user,pass) " +
				"values (?,?)",
				new Object[]{Home.user.userID,Home.user.pass});
		db.close();
	}

	public synchronized void deleteUserInfo() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("delete from user_info",
				new Object[]{});
		db.close();
	}
	
	public List<String> getUser() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //获得一个可写的数据库
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
