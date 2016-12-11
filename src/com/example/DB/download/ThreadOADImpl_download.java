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
 * ���ݿ����ʵ��
 * �����ݿ�Ĳ�������װ�����������
 * Ϊ��ֹ���߳�ͬʱ���� ,Ҫ�õ�����ģʽ
 * 
 * ThreadOADImpl_download()			��ʼ��
 * insertThread()					ָ��һ���߳���Ϣ��,���б��浽���ݿ�Ĳ���
 * deleteThread()					ָ��һ��������URL,����ɾ�����ݿ��ڴ�URL�����ݲ���
 * updateThread()					ָ��һ��������url��id(��Ϊ���ж��߳�����,һ���������ж������ݿ���Ϣ,���Ը������������ſ�ȷ��)�ͽ���,���и������������ȵĲ���
 * getThread()						ָ��һ��url,�õ����URL������������Ϣ��
 * isExists()						ָ��һ��URL��id���ж��������Ƿ����
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
	 * synchronized ͬ��
	 * ��������ͬʱִ��
	 */
	@Override
	public synchronized void insertThread(ThreadInfo threadInfo) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //���һ����д�����ݿ�
		db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values (?,?,?,?,?)",
				new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),threadInfo.getEnd(),threadInfo.getFinished()});
		db.close();
	}

	@Override
	public synchronized void deleteThread(String url) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //���һ����д�����ݿ�
		db.execSQL("delete from thread_info where url = ? ",
				new Object[]{url});
		db.close();
	}

	@Override
	public synchronized void updateThread(String url, int thread_id, int finished) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getWritableDatabase();  //���һ����д�����ݿ�
		db.execSQL("update thread_info set finished = ? where url = ? and thread_id = ?",
				new Object[]{finished ,url , thread_id});
		db.close();
	}

	@Override
	public List<ThreadInfo> getThread(String url) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();  //���һ����д�����ݿ�
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
		SQLiteDatabase db = helper.getReadableDatabase();  //���һ����д�����ݿ�
		Cursor cursor = db.rawQuery("select * from thread_info where url = ? and thread_id = ?", new String[]{url,thread_id + ""});
		boolean exists = cursor.moveToNext();
		cursor.close();
		db.close();
		return exists;
	}

}
