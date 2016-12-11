package com.example.kugou;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import com.example.DB.user.ThreadOADImpl_user;
import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 注册界面
 * 
 * 没什么说明的
 * 
 * @author Administrator
 *
 */
public class Registered extends Activity implements OnClickListener,OnPostExecuteListener{

	ImageView img_registered_close;
	EditText edit_registered_user;
	EditText edit_registered_pass;
	Button btn_registered_xsmm;
	Button btn_registered;
	RadioButton radio_man;
	RadioButton radio_women;
	
	public static boolean registered_close=false;
	
	public ProgressDialog pd;
	public SimpleDateFormat sdf;
	public String user;
	public String pass;
	public String sex;
	public String time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registered);
		
		img_registered_close=(ImageView) findViewById(R.id.img_registered_close);
		edit_registered_user=(EditText) findViewById(R.id.edit_registered_user);
		edit_registered_pass=(EditText) findViewById(R.id.edit_registered_pass);
		btn_registered_xsmm=(Button) findViewById(R.id.btn_registered_xsmm);
		btn_registered=(Button) findViewById(R.id.btn_registered);
		radio_man=(RadioButton) findViewById(R.id.radio_man);
		radio_women=(RadioButton) findViewById(R.id.radio_women);
		
		img_registered_close.setOnClickListener(this);
		edit_registered_pass.setOnClickListener(this);
		btn_registered_xsmm.setOnClickListener(this);
		btn_registered.setOnClickListener(this);
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!registered_close){
			overridePendingTransition(R.animator.small_to_big, R.animator.left_to_right);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_registered_close:
			finish();
			break;
			
		case R.id.btn_registered_xsmm:
			if(btn_registered_xsmm.getText().equals("显示密码")){
				btn_registered_xsmm.setText("隐藏密码");
				edit_registered_pass.setInputType(InputType.TYPE_CLASS_TEXT);
				edit_registered_pass.setSelection(edit_registered_pass.getText().length());
			}
			else {
				btn_registered_xsmm.setText("显示密码");
				edit_registered_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				edit_registered_pass.setSelection(edit_registered_pass.getText().length());
			}
			break;
			
		case R.id.btn_registered:
			sdf = new SimpleDateFormat("yyyy-MM-dd yy:mm:ss");
			user = edit_registered_user.getText().toString();
			pass = edit_registered_pass.getText().toString();
			sex = "-1";
			if (radio_man.isChecked())
				sex = "1";
			else if (radio_women.isChecked())
				sex = "0";
			time = sdf.format(System.currentTimeMillis()).replace(" ", "%20");

			if (user.isEmpty() || pass.isEmpty() || sex.equals("-1")
					|| time.isEmpty()) {
				Toast.makeText(Registered.this, "请填写完整！", Toast.LENGTH_SHORT)
						.show();
			} else {
				pd = ProgressDialog.show(Registered.this, null, "正在注册");
				String url = Home.postURL + "operat=insert&table=Users&values=";
				url += "'" + user + "','" + pass + "','" + user + "'," + sex
						+ ",'" + time + "'";
				
				new dataLoad(Registered.this).execute(url);

			}

			break;
		}
	}
	
	public void Registered_end(){
		Home.user.isLogin=true;
		Home.user.userID=user;
		Home.user.pass=Home.user.MD5(pass);
		Home.user.name=user;
		Home.user.sex=false;
		if(sex.equals("1"))
			Home.user.sex=true;
		Home.user.registeredtime= time;
		registered_close = true;
		ThreadOADImpl_user impl = new ThreadOADImpl_user(Registered.this);
		impl.deleteUserInfo();
		impl.insertUserInfo();
		finish();
	}
	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(post_return_analysis.getIsSuccess(result)){
			Registered_end();
		}
		else {
			Log.e("失败原因", post_return_analysis.getMsg(result));
			Toast.makeText(Registered.this,"注册" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
		}
		pd.dismiss();
	}
	
}
