package com.example.kugou.DBdata;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.PopupWindow;
/**
 * 加载网页图片的类
 * 
 * 可继承OnPostExecuteListener接口,回调得图片
 * 
 * @author Administrator
 *
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

	Context context;
	Fragment fragment;
	Service service;
	PopupWindow popupWindow;
	OnPostExecuteListener mOnPostExecuteListener;
	
	public ImageLoader(Context context){
		this.context=context;
	}
	public ImageLoader(Fragment fragment){
		this.fragment=fragment;
	}
	public ImageLoader(Service service){
		this.service=service;
	}
	public ImageLoader(PopupWindow popupWindow){
		this.popupWindow=popupWindow;
	}
	
	public interface OnPostExecuteListener{
		void onPostExecute(Bitmap result);
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url=params[0];
		Bitmap bitmap=getBitmapFormURL(url);
		return bitmap;
	}
	public Bitmap getBitmapFormURL(String urlString){
		Bitmap bitmap;
		InputStream is;
		try {
			URL url=new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			is=new BufferedInputStream(connection.getInputStream());
			bitmap=BitmapFactory.decodeStream(is);
			connection.disconnect();
			is.close();
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(context != null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.context;
		if(fragment !=null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.fragment;
		if(service !=null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.service;
		if(popupWindow !=null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.popupWindow;

		mOnPostExecuteListener.onPostExecute(result);
	}
	
}
