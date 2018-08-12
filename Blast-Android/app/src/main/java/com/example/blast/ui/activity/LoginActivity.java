package com.example.blast.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blast.AppConstants;
import com.example.blast.AppPreferences;
import com.example.blast.R;
import com.example.blast.http.Server;
import com.example.blast.model.UserModel.Login;
import com.example.blast.utils.BaseTask;
import com.example.blast.utils.BaseTask.TaskListener;
import com.example.blast.utils.FontUtil;
import com.example.blast.utils.Validation;

public class LoginActivity extends Activity implements View.OnClickListener{
	/*
	 * For UI
	 */
	private View btn_back;
	private View btn_login;
	private View btn_login_facebook;
	private Button btn_register;
	private EditText edt_user_email;
	private EditText edt_password;

	private ProgressDialog pDialog;

	/*
	 * For Values
	 */
	private Login.User mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		btn_back = findViewById(R.id.btn_back);
		btn_login = findViewById(R.id.btn_login);
		btn_login_facebook = findViewById(R.id.btn_login_facebook);
		btn_register = (Button) findViewById(R.id.btn_register);

		btn_back.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_login_facebook.setOnClickListener(this);
		btn_register.setOnClickListener(this);

		edt_user_email = (EditText) findViewById(R.id.edt_user_email);
		edt_password = (EditText) findViewById(R.id.edt_password);

		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		setfont();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			doBack();
			break;

		case R.id.btn_login:
			doLoginUsingEmail();
			break;

		case R.id.btn_login_facebook:
			doLoginUsingFacebook();
			break;

		case R.id.btn_register:
			doRegister();
			break;

		default:
			break;
		}
	}

	private void doBack() {
		this.finish();
	}

	private void doLoginUsingEmail() {
		if (isValid()) {
			final String user_id = edt_user_email.getText().toString().trim();
			final String password = edt_password.getText().toString().trim();
			
			pDialog.setMessage("Login with Email to Blast...");
			pDialog.show();
			
			BaseTask.run(new TaskListener() {
				@Override
				public Object onTaskRunning(int taskId, Object data) {
					Object result = Server.LoginWithEmail(user_id, password);

					if (result instanceof Login.Result) {
						Login.Result loginResult = (Login.Result) result;
						mUserInfo = loginResult.user;
						return null;
					}
					
					return ((String) result);
				}

				@Override
				public void onTaskResult(int taskId, Object result) {
					// Dismiss the progress dialog
					if (pDialog.isShowing())
						pDialog.dismiss();

					if (result == null) {
						Toast.makeText(LoginActivity.this, "Login with Email Success", Toast.LENGTH_LONG).show();
						
						LoginActivity.this.finish();
						AppPreferences.setInt(AppPreferences.KEY.LOGIN_MODE, AppConstants.LOGIN_TYPE_EMAIL);

						// save user information
						ConfigInfo.setUserId(mUserInfo.mail);
						ConfigInfo.setUserName(mUserInfo.mail);
						ConfigInfo.setUserAvatarUrl(mUserInfo.picture);

					} else {
						Toast.makeText(LoginActivity.this, "Login with Email Fail: " + (String)result, Toast.LENGTH_LONG).show();
					}
				}

				@Override
				public void onTaskProgress(int taskId, Object... values) {
				}

				@Override
				public void onTaskPrepare(int taskId, Object data) {
				}

				@Override
				public void onTaskCancelled(int taskId) {
				}
			});
		}
	}

	@SuppressWarnings("deprecation")
	private void doLoginUsingFacebook() {
		// for facebook login
		//		String facebook_app_id = getApplicationContext().getResources().getString(R.string.facebook_app_id);
		//		Facebook mFacebook = new Facebook(facebook_app_id);
		//		mFacebook.authorize(this, new String[] {"email", "read_stream"}, Facebook.FORCE_DIALOG_AUTH, new DialogListener() {
		//			@Override
		//			public void onComplete(Bundle values) {
		//				Toast.makeText(LoginActivity.this, "Facebook login successed", Toast.LENGTH_LONG).show();
		//
		//				LoginActivity.this.finish();
		//				ConfigInfo.setUserLoginMode(Constants.LOGIN_TYPE_FACEBOOK);
		//
		//				//save user information
		////				ConfigInfo.setUserId(user.getId());
		////				ConfigInfo.setUserName(user.getName());
		//			}
		//			@Override
		//			public void onFacebookError(FacebookError error) {
		//			}
		//			@Override
		//			public void onError(DialogError e) {
		//			}
		//			@Override
		//			public void onCancel() {
		//
		//			}
		//		});
		
		pDialog.setMessage("Login with Facebook Account to Blast...");
		pDialog.show();

//		Session.openActiveSession(this, true, new StatusCallback() {
//			@Override
//			public void call(Session session, SessionState state, Exception exception) {
//				// TODO Auto-generated method stub
//				if (session.isOpened()) {
//					// make request to the /me API
//					Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//						// callback after Graph API response with user object
//						@Override
//						public void onCompleted(GraphUser user, Response response) {
//							pDialog.dismiss();
//
//							if (user != null) {
//								Toast.makeText(LoginActivity.this, "Login with Facebook Account Success", Toast.LENGTH_LONG).show();
//
//								// finish login activity
//								LoginActivity.this.finish();
//								ConfigInfo.setUserLoginMode(Constants.LOGIN_TYPE_FACEBOOK);
//
//								// save user information
//								ConfigInfo.setUserId(user.getId());
//								ConfigInfo.setUserName(user.getName());
//
//							} else {
//								Toast.makeText(LoginActivity.this, "Login with Facebook Account Fail", Toast.LENGTH_LONG).show();
//							}
//						}
//
//					}).executeAsync();
//				}
//
//				// if user cancel login
//				if (exception != null) {
//					Toast.makeText(LoginActivity.this, "Login with Facebook Account Fail", Toast.LENGTH_LONG).show();
//					pDialog.dismiss();
//				}
//			}
//		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	private void doRegister() {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	private boolean isValid() {
		if (!Validation.EmptyValidation(edt_user_email, "Please input Your Email"))
			return false;
		if (!Validation.EmailValid(edt_user_email, "Please check your Email Address is valid"))
			return false;
		if (!Validation.EmptyValidation(edt_password, "Please input Password"))
			return false;
		if (!Validation.LengthMoreValidation(edt_password, 5, "You must input 5+ characters as Password"))
			return false;

		return true;
	}

	private void setfont() {
		btn_register.setTypeface(FontUtil.GetDeJaFont(this));
	}
}
