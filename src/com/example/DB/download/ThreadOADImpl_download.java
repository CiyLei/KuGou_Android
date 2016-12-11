package com.example.DB.download;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.R.layout;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kugou.download_entities.*;
/**
 * 数据库帮助实类
 * 对数据库的操作都封装在这个类里面
 * 为防止多线程同时访问 ,要用到单例模式
 * 
 * ThreadOADImpl_download()			初始化
 * insertThread()					指定一个线程信息类,进行保存到数据库的操作
 * deleteThread()					指定一个歌曲的URL,进行删除数据库内此URL的数据操作
 * updateThread()					指定一个歌曲的url和id(因为进行多线程下载,一个歌曲会有多条数据库信息,所以给定两个参数才可确定)和进度,进行更新数据条进度的操作
 * getThread()						指定一个url,得到这个URL的所有下载信息类
 * isExists()						指点一个URL和id来判断数据条是否存在
 * 
 * @author Administrator
 *
 */
public class ThreadOADImpl_download implements ThreadDAO_download {

	private DB_download_Helper helper = null;
	
	public ThreadOADImpl_download(Context context){
		helper = DB_download_Helper.getInstance(context);
	}
	/**
	 * synchronized 同步
	 * 方法不会同时执行
	 */
	@Override
	public synchronized void insertThread(ThreadInfo threadInfo) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values (?,?,?,?,?)",
				new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),threadInfo.getEnd(),threadInfo.getFinished()});
		db.close();
	}

	@Override
	public synchronized void deleteThread(String url) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("delete from thread_info where url = ? ",
				new Object[]{url});
		db.close();
	}

	@Override
	public synchronized void updateThread(String url, int thread_id, int finished) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //获得一个可写的数据库
		db.execSQL("update thread_info set finished = ? where url = ? and thread_id = ?",
				new Object[]{finished ,url , thread_id});
		db.close();
	}

	@Override
	public List<ThreadInfo> getThread(String url) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //获得一个可写的数据库
		List<ThreadInfo> threadInfos = new ArrayList<ThreadInfo>();
		Cursor cursor = db.rawQuery("select * from thread_info where url = ?", new String[]{url});
		while(cursor.moveToNext()){
			ThreadInfo threadInfo = new ThreadInfo();
			threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
			threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
			threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
			threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
			threadInfos.add(threadInfo);
		}
		cursor.close();
		db.close();
		return threadInfos;
	}

	@Override
	public boolean isExists(String url, int thread_id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //获得一个可写的数据库
		Cursor cursor = db.rawQuery("select * from thread_info where url = ? and thread_id = ?", new String[]{url,thread_id + ""});
		boolean exists = cursor.moveToNext();
		cursor.close();
		db.close();
		return exists;
	}

}
