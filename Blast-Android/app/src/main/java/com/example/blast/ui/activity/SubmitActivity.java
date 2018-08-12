package com.example.blast.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.blast.AppConstants;
import com.example.blast.R;
import com.example.blast.utils.Validation;

public class SubmitActivity extends Activity implements OnClickListener{
	private View btn_back;
	private ImageView btn_channel;
	private boolean channel_audio_flag = true;
	private View btn_send;
	private EditText edt_email;
	private EditText edt_url;
	private EditText edt_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_url);

		btn_back = findViewById(R.id.btn_back);
		btn_channel = findViewById(R.id.btn_channel);
		btn_send = findViewById(R.id.btn_send);
		
		edt_email = findViewById(R.id.edt_email);
		edt_email.setText(AppConstants.ADMIN_EMAIL_ADDRESS);
		edt_url = findViewById(R.id.edt_url);
		edt_message = findViewById(R.id.edt_message);
		
		btn_send.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_channel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			doBack();
			break;
			
		case R.id.btn_send:
			doSend();
			break;
			
		case R.id.btn_channel:
			selectChannelAudio();
			break;

		default:
			break;
		}
	}
	
	private void doBack() {
		this.finish();
	}
	
	private void doSend() {
		if (isVaild()) {
			String strbody = "Please check my URL\n\n"
					+ "**********************\n\n" 
					+ edt_url.getText().toString() + "\n\n"
					+ "**********************\n\n"
					+ edt_message.getText().toString().trim();

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { edt_email.getText().toString().trim() });
			intent.putExtra(Intent.EXTRA_SUBJECT, "Submit My Media URL to Blast");
			intent.putExtra(Intent.EXTRA_TEXT, strbody);
			intent.setType("html/text");

			startActivity(Intent.createChooser(intent, "Send mail..."));
		}
	}
	
	private void selectChannelAudio() {
		channel_audio_flag = !channel_audio_flag;
		if (channel_audio_flag) {
			btn_channel.setImageResource(R.drawable.btn_tog_on);
		} else {
			btn_channel.setImageResource(R.drawable.btn_tog_off);
		}
	}
	
	private boolean isVaild() {
		if (!Validation.EmptyValidation(edt_email, "Please Enter Email Address"))
			return false;
		if (!Validation.EmptyValidation(edt_url, "Please Enter Your Url"))
			return false;
		
		return true;
	}
}
