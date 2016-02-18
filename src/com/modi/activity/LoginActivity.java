package com.modi.activity;

import org.json.JSONException;
import org.json.JSONObject;

import util.Affair;
import util.AppConstants;
import util.DialogUtil;
import util.T;
import util.Util;
import activityLoc.xwt.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.platform.comapi.map.t;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

@ContentView(R.layout.activity_login)
public class LoginActivity extends Activity {

	private Tencent mTencent;
	public static QQAuth mQQAuth;
	public static String mAppid;
	public static String openidString;
	public static String nicknameString;
	public static String TAG = "MainActivity";
	public static String sex;
	public static String city;
	public static String province;
	@ViewInject(R.id.phoneNum)
	private EditText phoneNumText;
	@ViewInject(R.id.button_clear_one)
	private Button button_clear_one;
	@ViewInject(R.id.button_clear_two)
	private Button button_clear_two;
	@ViewInject(R.id.password)
	private EditText passwardText;
	private Handler handler;
	private String telephone;
	private String password;

	Dialog progressDialog;
	Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		telephone=phoneNumText.getText().toString();
		password=passwardText.getText().toString();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
					DialogUtil.dismissDialog();
					//LoginActivity.this.finish();
					if(msg.obj.toString()=="") {
						T.showShortCenter(LoginActivity.this, "登录成功!");
						Intent intent = new Intent(LoginActivity.this,
							Activity_MainUI.class);
					startActivity(intent);	
					finish();
					}
					else T.showShortCenter(LoginActivity.this, msg.obj.toString());
					
			}
		};
		phoneNumText.addTextChangedListener(new textwatch(button_clear_one));
		passwardText.addTextChangedListener(new textwatch(button_clear_two));

	}

	@OnClick(R.id.registerByPhoneNumber)
	public void register(View view) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		// finish();
		startActivity(intent);
	}

	@OnClick(R.id.login)
	public void login(View v) {
		DialogUtil.showProgressDialog(LoginActivity.this, "", "正在登录...");		
		Context context = v.getContext();
		Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
		v.startAnimation(shake);
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
				String string=	Affair.loginUser(telephone, password);
				Message msgMessage=new Message();
				msgMessage.obj=string;
					handler.sendMessage(msgMessage);
				} catch (Exception e) {
					Log.i("1", e.toString());
					handler.sendEmptyMessage(0);
				}

			}
		}).start();		
		/*Intent intent = new Intent(LoginActivity.this,
				Activity_MainUI.class);

		 finish();
		startActivity(intent);	*/
	
	}

	@OnClick(R.id.qqlogin)
	public void QQClick(View v) {
		// Toast.makeText(getApplicationContext(), "正在登录...",
		// Toast.LENGTH_LONG).show();
		progressDialog = ProgressDialog.show(LoginActivity.this, "请稍候...",
				"正在为你QQ登录...");
		Context context = v.getContext();
		Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
		LoginQQ();
		v.startAnimation(shake);
	}
	@OnClick(value={R.id.button_clear_one,R.id.button_clear_two})
	public void buttonClear(View v) {
		switch (v.getId()) {
		case R.id.button_clear_one:
			phoneNumText.setText("");
			button_clear_one.setVisibility(View.INVISIBLE);
			break;
		case R.id.button_clear_two:
			passwardText.setText("");
			button_clear_two.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
		
	}
	
	public void LoginQQ() {
		// 这里的APP_ID请换成你应用申请的APP_ID，我这里使用的是DEMO中官方提供的测试APP_ID 222222
		mAppid = AppConstants.APP_ID;
		// 第一个参数就是上面所说的申请的APPID，第二个是全局的Context上下文，这句话实现了调用QQ登录
		mTencent = Tencent.createInstance(mAppid, getApplicationContext());
		/**
		 * 通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO
		 * 是一个String类型的字符串，表示一些权限 官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE =
		 * “get_user_info,add_t”；所有权限用“all”
		 * 第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类
		 */
		mTencent.login(LoginActivity.this, "all", new BaseUiListener());

	}

	/*
	 * private void updateLoginButton() { if (mTencent != null &&
	 * mTencent.isSessionValid()) { loginButton.setTextColor(Color.RED);
	 * loginButton.setText("登出帐号"); } else {
	 * loginButton.setTextColor(Color.BLUE); loginButton.setText("登录"); } }
	 */

	/**
	 * 当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法， onComplete onCancel onError
	 * 分别表示第三方登录成功，取消 ，错误。
	 */
	private class BaseUiListener implements IUiListener {

		public void onCancel() {
			// TODO Auto-generated method stub

		}

		public void onComplete(Object response) {
			// TODO Auto-generated method stub

			progressDialog.dismiss();

			Toast.makeText(getApplicationContext(), "登录成功", 0).show();

			try {
				// 获得的数据是JSON格式的，获得你想获得的内容
				// 如果你不知道你能获得什么，看一下下面的LOG
				Log.e(TAG, "-------------" + response.toString());
				openidString = ((JSONObject) response).getString("openid");

				Log.e(openidString, "-------------" + openidString);
				// access_token= ((JSONObject)
				// response).getString("access_token"); //expires_in =
				// ((JSONObject) response).getString("expires_in");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * 到此已经获得OpneID以及其他你想获得的内容了
			 * QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
			 * sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
			 * 如何得到这个UserInfo类呢？
			 */
			QQToken qqToken = mTencent.getQQToken();
			UserInfo info = new UserInfo(getApplicationContext(), qqToken);
			// 这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON
			info.getUserInfo(new IUiListener() {

				public void onComplete(final Object response) {
					// TODO Auto-generated method stub
					Log.e(TAG, "---------------111111");
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					try {
						sex = ((JSONObject) response).getString("gender");
						province = ((JSONObject) response)
								.getString("province");
						city = ((JSONObject) response).getString("city");
						// openidTextView.setText(openidString+" "+sex+" "+province+" "+city);
					} catch (JSONException e1) {

						e1.printStackTrace();
					}
					Log.e(TAG, "-----111---" + response.toString());
					/**
					 * 由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接 在mHandler里进行操作
					 * 
					 */
					new Thread() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							JSONObject json = (JSONObject) response;
							try {
								bitmap = Util.getbitmap(json
										.getString("figureurl_qq_2"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message msg = new Message();
							msg.obj = bitmap;
							msg.what = 1;
							mHandler.sendMessage(msg);
						}
					}.start();
				}

				public void onCancel() {
					Log.e(TAG, "--------------111112");
					// TODO Auto-generated method stub
				}

				public void onError(UiError arg0) {
					// TODO Auto-generated method stub
					Log.e(TAG, "-111113" + ":" + arg0);
				}

			});
			Intent intent = new Intent(LoginActivity.this, Activity_MainUI.class);
			startActivity(intent);
			finish();
		}

		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						nicknameString = response.getString("nickname");

						// nicknameTextView.setText(nicknameString);
						Log.e(TAG, "--" + nicknameString);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (msg.what == 1) {
				Bitmap bitmap = (Bitmap) msg.obj;
				// userlogo.setImageBitmap(bitmap);

			}
		}

	};

	@Override
	protected void onDestroy() {
		mTencent.logout(LoginActivity.this);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		progressDialog.dismiss();
		super.onBackPressed();
	
	}
class textwatch implements TextWatcher{
		protected Button clearButton;
	
	public textwatch() {
		super();
		// TODO Auto-generated constructor stub
	}
	public textwatch(Button clearButton) {
		super();
	
		this.clearButton = clearButton;
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {		
		if(s.toString().equals("")) {clearButton.setVisibility(View.INVISIBLE);Log.e("Q",s.toString()+count);	}
		else clearButton.setVisibility(View.VISIBLE);		
	}
	
}
}
