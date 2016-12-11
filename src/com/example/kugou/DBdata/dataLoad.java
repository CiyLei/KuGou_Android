package com.example.kugou.DBdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.kugou.Home;
import com.example.kugou.Login;
import com.example.kugou.Registered;

import android.R.string;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;
/**
 * 加载网页信息的类
 * 
 * 可继承OnPostExecuteListener接口,回调得网页信息
 * 
 * @author Administrator
 *
 */
public class dataLoad extends AsyncTask<String, Void, String> {

	Context context;
	Fragment fragment;
	PopupWindow popupWindow;
	Dialog dialog;
	BaseAdapter adapter;
	public String dataString;
	
	public OnPostExecuteListener mOnPostExecuteListener;
	
	public interface OnPostExecuteListener{
		public void onPostExecute(String result);
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return getData(params[0]);
	}
	
	public dataLoad(Context context){
		this.context=context;
	}
	
	public dataLoad(Fragment fragment){
		this.fragment=fragment;
	}
	
	public dataLoad(PopupWindow popupWindow){
		this.popupWindow=popupWindow;
	}
	
	public dataLoad(Dialog dialog){
		this.dialog=dialog;
	}
	
	public dataLoad(BaseAdapter adapter){
		this.adapter=adapter;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(context != null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.context;
		if(fragment !=null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.fragment;
		if(popupWindow !=null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.popupWindow;
		if(dialog !=null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.dialog;
		if(adapter !=null)
			mOnPostExecuteListener = (OnPostExecuteListener) this.adapter;

		mOnPostExecuteListener.onPostExecute(result);
	}

	
	private String getData(String URL) {

		dataString = "";
		try {
			InputStream is=new java.net.URL(URL).openStream();
//			HttpURLConnection conn=(HttpURLConnection) (new URL(URL)).openConnection();
			dataString = readStream(is); //conn.getInputStream()
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataString;
	}

	private String readStream(InputStream is) {// 传入网页字节流 通过缓存以utf-8编码输出
		InputStreamReader isr;
		String result = "";
		try {
			String line = "";
			isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null){
				result += line;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
