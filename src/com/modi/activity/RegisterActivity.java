package com.modi.activity;

import util.Affair;
import util.DialogUtil;
import util.T;
import activityLoc.xwt.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends Activity {

	@ViewInject(R.id.phoneNumber_register)
	private EditText telEditText;
	@ViewInject(R.id.verificationCode_register)
	private EditText codeEditText;
	@ViewInject(R.id.userName)
	private EditText userEditText;
	@ViewInject(R.id.passward)
	private EditText passwardEditText;
	@ViewInject(R.id.passward2)
	private EditText passward2EditText;
	@ViewInject(R.id.finish_register)
	private Button finishButton;
	private String phone = "";
	private String code = "";
	private String user = "";
	private String passward = "";
	private String passward2 = "";
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		/*phone = telEditText.getText().toString();
		code = codeEditText.getText().toString();
		passward = passwardEditText.getText().toString();
		passward2 = passward2EditText.getText().toString();
		user = userEditText.getText().toString();*/
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					DialogUtil.dismissDialog();
					T.showShortCenter(RegisterActivity.this, "注册成功!");
					finish();
					Intent intent = new Intent(RegisterActivity.this,
							MainModi.class);
					startActivity(intent);

				} else if (msg.what == 0) {
					DialogUtil.dismissDialog();
					T.showShortCenter(RegisterActivity.this, "注册失败!");
				}
			}
		};
		

	}

	@OnClick(R.id.finish_register)
	private void postInformation(View view) {
		
		phone = telEditText.getText().toString().trim();
		code = codeEditText.getText().toString();
		passward = passwardEditText.getText().toString();
		passward2 = passward2EditText.getText().toString();
		user = userEditText.getText().toString();
		if(phone.equals("")||user.equals("")||passward.equals("")||passward2.equals("")||code.equals(""))  
			{
			T.showLongCenter(RegisterActivity.this, "请填写完整信息!");
			return;
			}
		else if(passward.length()<6)  {
			T.showLongCenter(RegisterActivity.this, "密码位数不能小于6位!");	return;}
		else if(!passward.equals(passward2)){
			 T.showLongCenter(RegisterActivity.this, "密码输入不一致!请重新输入!");
			 passwardEditText.setText("");
			 passward2EditText.setText("");
				return;
		 }
		
		DialogUtil.showProgressDialog(RegisterActivity.this, "", "正在注册...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					phone = telEditText.getText().toString().trim();
					code = codeEditText.getText().toString();
					passward = passwardEditText.getText().toString();
					passward2 = passward2EditText.getText().toString();
					user = userEditText.getText().toString();				
					Affair.registerUser(user, passward, phone, "");
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					Log.i("1", e.toString());
					handler.sendEmptyMessage(0);
				}

			}
		}).start();

	}

	private void checkEmpty() {
		
		 

	}

	/*
	 * @OnClick(R.id.registerByPhoneNumber) public void register(){ finish(); }
	 */

}
