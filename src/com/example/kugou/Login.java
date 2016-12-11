package com.example.kugou;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.DB.user.ThreadOADImpl_user;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.DBdata.select_analysis;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 登录界面
 * 
 * 用dataLoad类发送帐号和加密的密码,onPostExecute()回调内容,判断是否登录成功
 * 
 * @author Administrator
 *
 */
public class Login extends Activity implements OnClickListener,OnPostExecuteListener {

	ImageView login_close;
	Button btn_login_registered;
	Button btn_login;
	EditText edit_login_user;
	EditText edit_login_pass;
	boolean start_egistered = false;
	ThreadOADImpl_user impl;
	public ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		login_close = (ImageView) findViewById(R.id.login_close);
		btn_login_registered = (Button) findViewById(R.id.btn_login_registered);
		btn_login = (Button) findViewById(R.id.btn_login);
		edit_login_user = (EditText) findViewById(R.id.edit_login_user);
		edit_login_pass = (EditText) findViewById(R.id.edit_login_pass);

		login_close.setOnClickListener(this);
		btn_login_registered.setOnClickListener(this);
		btn_login.setOnClickListener(this);

		impl = new ThreadOADImpl_user(Login.this);
		List<String> user = impl.getUser();
		if(user.size() > 0){
			edit_login_user.setText(user.get(0));
			edit_login_user.clearFocus();
			edit_login_pass.requestFocus();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_close:
			finish();
			break;

		case R.id.btn_login_registered:

			Intent intent = new Intent(Login.this, Registered.class);
			startActivity(intent);
			overridePendingTransition(R.animator.right_to_left,
					R.animator.big_to_small);
			start_egistered = true;

			break;

		case R.id.btn_login:

			String user = edit_login_user.getText().toString();
			String pass = edit_login_pass.getText().toString();
			if (user.isEmpty() || pass.isEmpty()) {
				Toast.makeText(Login.this, "请填写完整!", Toast.LENGTH_SHORT).show();
			} else {
				
				pd = ProgressDialog.show(Login.this, null, "正在登录");
				String passMD5 = Home.user.MD5(pass);
				String URL = Home.postURL + "operat=select&table=Users";
				URL += "&user=" + user + "&pass=" + passMD5;

				new dataLoad(Login.this).execute(URL);

			}
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (!start_egistered)
			overridePendingTransition(R.animator.notanim, R.animator.up_to_down);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (Registered.registered_close) {
			finish();
			overridePendingTransition(R.animator.notanim, R.animator.up_to_down);
		}
	}

	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		pd.dismiss();
		//如果登录成功
		if (post_return_analysis.getIsSuccess(result)) {
			//更新用户信息
			Home.user = select_analysis.getUser(result);
			//更新用户数据库
			impl.deleteUserInfo();
			impl.insertUserInfo();
			finish();
			// overridePendingTransition(R.animator.notanim,
			// R.animator.up_to_down);
		} else {
			Log.e("失败原因", post_return_analysis.getMsg(result));
			Toast.makeText(Login.this,"登录" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
		}
	}

}
