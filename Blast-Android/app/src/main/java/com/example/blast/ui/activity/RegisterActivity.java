package com.example.blast.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blast.AppConstants;
import com.example.blast.AppPreferences;
import com.example.blast.R;
import com.example.blast.http.Server;
import com.example.blast.model.UserModel.Login;
import com.example.blast.model.UserModel.Register;
import com.example.blast.utils.BaseTask;
import com.example.blast.utils.BaseTask.TaskListener;
import com.example.blast.utils.Validation;

public class RegisterActivity extends Activity implements OnClickListener{
	/*
	 * For UI
	 */
	private EditText edt_user_email;
	private EditText edt_password;
	private EditText edt_confirm_password;
	
	private ProgressDialog pDialog;
	
	/* 
	 * For Value
	 */
	private Login.User mUserInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		edt_user_email = findViewById(R.id.edt_user_email);
		edt_password = findViewById(R.id.edt_password);
		edt_confirm_password = findViewById(R.id.edt_confirm_password);

		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.btn_register).setOnClickListener(this);
		findViewById(R.id.btn_register_facebook).setOnClickListener(this);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Register to Blast...");
		pDialog.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			doBack();
			break;

		case R.id.btn_register:
			doRegister();
			break;
			
		case R.id.btn_register_facebook:
			doRegisterFacebook();
			break;

		default:
			break;
		}
	}
	
	private void doBack() {
		this.finish();
	}
	
	private void doRegister() {
		if (isValid()) {
			final String user_email = edt_user_email.getText().toString().trim();
			final String password = edt_password.getText().toString().trim();
			
			pDialog.show();

			BaseTask.run(new TaskListener() {
				@Override
				public Object onTaskRunning(int taskId, Object data) {
					Object result = Server.RegisterWithEmail(user_email, password);
					if (result instanceof Register.Result) {
						return null;
					}

					return result;
				}

				@Override
				public void onTaskResult(int taskId, Object result) {
					// Dismiss the progress dialog
					if (result == null) {
						Toast.makeText(RegisterActivity.this, "Register with Email Success", Toast.LENGTH_LONG).show();
						
						doLogin();

					} else {
						if (pDialog.isShowing())
							pDialog.dismiss();
						
						Toast.makeText(RegisterActivity.this, "Register with Email Fail: " + result, Toast.LENGTH_LONG).show();
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
	
	private void doRegisterFacebook() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/r.php?refid=9"));
		startActivity(intent);
	}
	
	private boolean isValid() {
		if (!Validation.EmptyValidation(edt_user_email, "Please input User Email"))
			return false;
		if (!Validation.EmailValid(edt_user_email, "Please check your Email Address is vaild"))
			return false;
		if (!Validation.EmptyValidation(edt_password, "Please input Password"))
			return false;
		if (!Validation.LengthMoreValidation(edt_password, 5, "You must input 5+ characters as Password"))
			return false;
		if (!Validation.EmptyValidation(edt_confirm_password, "Please confirm Password"))
			return false;
		if (!Validation.MatchValidation(edt_password, edt_confirm_password, "2 passwords not match"))
			return false;
		
		return true;
	}
	
	private void doLogin() {
		if (isValid()) {
			final String user_id = edt_user_email.getText().toString().trim();
			final String password = edt_password.getText().toString().trim();

			BaseTask.run(new TaskListener() {
				@Override
				public Object onTaskRunning(int taskId, Object data) {
					Object result = Server.LoginWithEmail(user_id, password);

					if (result instanceof Login.Result) {
						Login.Result loginResult = (Login.Result) result;
						mUserInfo = loginResult.user;
						return null;
					}
					
					return result;
				}

				@Override
				public void onTaskResult(int taskId, Object result) {
					// Dismiss the progress dialog
					if (pDialog.isShowing())
						pDialog.dismiss();

					if (result == null) {
						Toast.makeText(RegisterActivity.this, "Login Success", Toast.LENGTH_LONG).show();
						
						RegisterActivity.this.finish();
						AppPreferences.setInt(AppPreferences.KEY.LOGIN_MODE, AppConstants.LOGIN_TYPE_FACEBOOK);

						// save user information
						AppPreferences.setStr(AppPreferences.KEY.USER_ID, mUserInfo.mail);
						AppPreferences.setStr(AppPreferences.KEY.USER_AVATAR_URL, mUserInfo.picture);

					} else {
						Toast.makeText(RegisterActivity.this, "Login failed: "+result, Toast.LENGTH_LONG).show();
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
	
}
