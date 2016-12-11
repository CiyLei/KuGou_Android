package com.example.kugou;

import com.example.kugou.DBdata.dataLoad;
import com.example.kugou.DBdata.dataLoad.OnPostExecuteListener;
import com.example.kugou.DBdata.User;
import com.example.kugou.DBdata.post_return_analysis;
import com.example.kugou.DBdata.select_analysis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 修改密码界面
 * 
 * 没什么说明的
 * 
 * @author Administrator
 *
 */
public class Revise_UserPass extends Activity implements OnClickListener,OnCheckedChangeListener,OnPostExecuteListener {

	ImageView revise_userpass_close;
	EditText edit_RevisePass_old,edit_RevisePass_new;
	CheckBox check_ShowPass;
	Button btn_RevisePass;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.revise_pass_layout);
		
		revise_userpass_close=(ImageView) findViewById(R.id.revise_userpass_close);
		edit_RevisePass_old=(EditText) findViewById(R.id.edit_RevisePass_old);
		edit_RevisePass_new=(EditText) findViewById(R.id.edit_RevisePass_new);
		check_ShowPass=(CheckBox) findViewById(R.id.check_ShowPass);
		btn_RevisePass=(Button) findViewById(R.id.btn_RevisePass);
		
		revise_userpass_close.setOnClickListener(this);
		btn_RevisePass.setOnClickListener(this);
		check_ShowPass.setOnCheckedChangeListener(this);
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(R.animator.notanim, R.animator.up_to_down);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.revise_userpass_close:
			finish();
			break;
			
		case R.id.btn_RevisePass:
			if(edit_RevisePass_new.getText().toString().isEmpty() || edit_RevisePass_old.getText().toString().isEmpty())
				Toast.makeText(Revise_UserPass.this, "请填写完整", Toast.LENGTH_SHORT).show();
			else{
				
				if(User.MD5(edit_RevisePass_old.getText().toString()).equals(Home.user.pass)){
					pd = ProgressDialog.show(Revise_UserPass.this, null, "正在修改");
					String URL = Home.postURL + 
						"user=" + Home.user.userID +
						"&pass=" + Home.user.pass +
						"&operat=update&table=Users&bt=Pass&value='" +
						edit_RevisePass_new.getText().toString().trim() + "'";
					new dataLoad(Revise_UserPass.this).execute(URL);
				}
				else{
					Toast.makeText(Revise_UserPass.this, "密码错误", Toast.LENGTH_SHORT).show();
				}
				
			}
			break;

		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			edit_RevisePass_old.setInputType(InputType.TYPE_CLASS_TEXT);
			edit_RevisePass_new.setInputType(InputType.TYPE_CLASS_TEXT);
		}
		else{
			edit_RevisePass_old.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			edit_RevisePass_new.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		edit_RevisePass_old.setSelection(edit_RevisePass_new.getText().length());
		edit_RevisePass_new.setSelection(edit_RevisePass_new.getText().length());
	}
	@Override
	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		pd.dismiss();
		if(post_return_analysis.getIsSuccess(result)){
			Home.user.pass = User.MD5(edit_RevisePass_new.getText().toString());
			finish();
			Toast.makeText(Revise_UserPass.this, "修改成功", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(Revise_UserPass.this,"修改" + post_return_analysis.getIsSuccessTxt(result),Toast.LENGTH_SHORT).show();
			Log.e("失败原因", post_return_analysis.getMsg(result));
		}
	}
}
