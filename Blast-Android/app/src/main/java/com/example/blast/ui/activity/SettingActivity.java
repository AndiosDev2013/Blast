package com.example.blast.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blast.AppConstants;
import com.example.blast.AppPreferences;
import com.example.blast.R;
import com.example.blast.http.Server;
import com.example.blast.model.UserModel.Login;
import com.example.blast.utils.BaseTask;
import com.example.blast.utils.BaseTask.TaskListener;
import com.example.blast.utils.Validation;
import com.example.blast.utils.myImageLoader;

public class SettingActivity extends Activity implements OnClickListener{
	public SettingActivity instance = null;

	private View btn_back;
	//private ProfilePictureView fbpic_avatar;
	private ImageView img_avatar;
	private TextView txt_user_id;
	private View btn_login;
	private View btn_reset_password;
	private LinearLayout layout_reset_password_item_bottom_line;
	private View btn_submit_url;
	private View btn_clear_cache;

	private ProgressDialog pDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		instance = this;

		btn_back = findViewById(R.id.btn_back);

		//fbpic_avatar = (ProfilePictureView) findViewById(R.id.fbpic_avatar);
		img_avatar = findViewById(R.id.img_avatar);
		txt_user_id = findViewById(R.id.txt_user_id);

		btn_login = findViewById(R.id.btn_login);
		btn_reset_password = findViewById(R.id.btn_reset_password);
		layout_reset_password_item_bottom_line = findViewById(R.id.layout_reset_password_item_bottom_line);
		btn_submit_url = findViewById(R.id.btn_submit_url);
		btn_clear_cache = findViewById(R.id.btn_clear_cache);

		btn_back.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_reset_password.setOnClickListener(this);
		btn_submit_url.setOnClickListener(this);
		btn_clear_cache.setOnClickListener(this);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait, showing page to reset your password");
		pDialog.setCancelable(false);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		int login_type = AppPreferences.getInt(AppPreferences.KEY.LOGIN_MODE, AppConstants.LOGIN_TYPE_UNKNOWN);
		if (login_type == AppConstants.LOGIN_TYPE_UNKNOWN || login_type == AppConstants.LOGIN_TYPE_EMAIL) {
			img_avatar.setVisibility(View.VISIBLE);
			//fbpic_avatar.setVisibility(View.GONE);

			if (login_type ==  AppConstants.LOGIN_TYPE_EMAIL) {
				myImageLoader.showImage(img_avatar, AppPreferences.getStr(AppPreferences.KEY.USER_AVATAR_URL, null));
				txt_user_id.setText(AppPreferences.getStr(AppPreferences.KEY.USER_ID, null));
				setResetPasswordLayoutVisible(true);

			} else {
				txt_user_id.setText(R.string.Unknown_User);
				setResetPasswordLayoutVisible(false);
			}

		} else {
			img_avatar.setVisibility(View.GONE);
			//fbpic_avatar.setVisibility(View.VISIBLE);

			//fbpic_avatar.setProfileId(ConfigInfo.getUserId());
			txt_user_id.setText(AppPreferences.getStr(AppPreferences.KEY.USER_ID, null));

			setResetPasswordLayoutVisible(false);
		}

		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			doBack();
			break;

		case R.id.btn_login:
			doLogin();
			break;

		case R.id.btn_reset_password:
			doResetPassword();
			break;

		case R.id.btn_submit_url:
			doSubmitYourURL();
			break;

		case R.id.btn_clear_cache:
			clearCache();
			break;

		default:
			break;
		}
	}

	private void doBack() {
		this.finish();
	}

	private void doLogin() {
		Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
		startActivity(intent);
	}

	private void doResetPassword() {
		Dialog dlg = new Dialog(this);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(R.layout.dlg_check_user);
		dlg.show();

		final EditText edt_user_email = dlg.findViewById(R.id.edt_user_email);
		final EditText edt_password = dlg.findViewById(R.id.edt_password);
		Button btn_check = dlg.findViewById(R.id.btn_check);

		btn_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Validation.EmptyValidation(edt_user_email, "Please input your email."))
					return;
				if (!Validation.EmailValid(edt_user_email, "Please check your email is valid"))
					return;
				if (!Validation.EmptyValidation(edt_password, "Please input password"))
					return;
				if (!Validation.LengthMoreValidation(edt_password, 5, "You must input 5+ charators as password"))
					return;

				final String user_email = edt_user_email.getText().toString().trim();
				final String password = edt_password.getText().toString().trim();
				
				BaseTask.run(new TaskListener() {
					@Override
					public Object onTaskRunning(int taskId, Object data) {
						Object result = Server.LoginWithEmail(user_email, password);
						if (result instanceof Login.Result) {
							return null;
						}
						
						return result;
					}

					@Override
					public void onTaskResult(int taskId, Object result) {
						// Dismiss the progress dialog
						if (pDialog.isShowing())
							pDialog.dismiss();

//						if (result == null) {
//							Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
//
//							LoginActivity.this.finish();
//							ConfigInfo.setUserLoginMode(Constants.LOGIN_TYPE_FACEBOOK);
//
//							// save user information
//							ConfigInfo.setUserId(mUserInfo.mail);
//							ConfigInfo.setUserName(mUserInfo.mail);
//							ConfigInfo.setUserAvatarUrl(mUserInfo.picture);
//
//						} else {
//							Toast.makeText(LoginActivity.this, "Login failed: " + (String)result, Toast.LENGTH_LONG).show();
//						}
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
		});


	}

	private void doSubmitYourURL() {
		Intent intent = new Intent(SettingActivity.this, SubmitActivity.class);
		startActivity(intent);
	}

	private void clearCache() {
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("Hi !!!")
		.setMessage("Do you want to clear all cache data?")
		.setPositiveButton("Yes, Clear", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				myImageLoader.clearCache();
				Toast.makeText(SettingActivity.this, "Cache was cleared", Toast.LENGTH_LONG).show();
			}
		})
		.setNegativeButton("Cancel", null)
		.show();
	}

	private void setResetPasswordLayoutVisible(boolean flag) {
		if (flag) {
			btn_reset_password.setVisibility(View.VISIBLE);
			layout_reset_password_item_bottom_line.setVisibility(View.VISIBLE);

		} else {
			btn_reset_password.setVisibility(View.GONE);
			layout_reset_password_item_bottom_line.setVisibility(View.GONE);
		}
	}
}
