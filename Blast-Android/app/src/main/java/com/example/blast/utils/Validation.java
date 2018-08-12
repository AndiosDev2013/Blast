package com.example.blast.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Validation {
	private static Context context;

	public static void initialize(Context context) {
		Validation.context = context;
	}

	public static boolean EmptyValidation(TextView control, String warning_msg) {
		if (control == null)
			return false;

		String str = control.getText().toString().trim();
		if (str.length() <= 0) {
			Toast.makeText(context, warning_msg, Toast.LENGTH_LONG).show();
			control.requestFocus();
			return false;
		}

		return true;
	}

	public static boolean EmptyValidation(EditText control, int resId) {
		return EmptyValidation(control, control.getResources().getString(resId));
	}

	public static boolean EmptyValidation(EditText control, String warning_msg) {
		if (control == null)
			return false;

		String str = control.getText().toString().trim();
		if (str.length() <= 0) {
			Toast.makeText(context, warning_msg, Toast.LENGTH_LONG).show();
			control.requestFocus();
			return false;
		}

		return true;
	}

	public static boolean LengthValidation(EditText control, int length, String warning_msg) {
		if (control == null)
			return false;

		String str = control.getText().toString().trim();
		if (str.length() != length) {
			Toast.makeText(context, warning_msg, Toast.LENGTH_LONG).show();
			control.requestFocus();
			return false;
		}

		return true;
	}

	public static boolean LengthMoreValidation(EditText control, int length, String warning_msg) {
		if (control == null)
			return false;

		String str = control.getText().toString().trim();
		if (str.length() < length) {
			Toast.makeText(context, warning_msg, Toast.LENGTH_LONG).show();
			control.requestFocus();
			return false;
		}

		return true;
	}

	public static boolean LengthLessValidation(EditText control, int length, String warning_msg) {
		if (control == null)
			return false;

		String str = control.getText().toString().trim();
		if (str.length() > length) {
			Toast.makeText(context, warning_msg, Toast.LENGTH_LONG).show();
			control.requestFocus();
			return false;
		}

		return true;
	}

	public static boolean MatchValidation(EditText control0, EditText control1, String warning_msg) {
		if (control0 == null || control1 == null)
			return false;

		String str0 = control0.getText().toString().trim();
		String str1 = control1.getText().toString().trim();
		if (!str0.equals(str1)) {
			Toast.makeText(context, warning_msg, Toast.LENGTH_LONG).show();
			control0.requestFocus();
			return false;
		}

		return true;
	}

	public static boolean EmailValid(EditText control0, String warning_msg) {
		String regExpn =
				"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
						+"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
						+"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
						+"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = control0.getText().toString().trim();

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if(!matcher.matches()) {
			Toast.makeText(context, warning_msg, Toast.LENGTH_LONG).show();
			control0.requestFocus();
			return false;
		}
		
		return true;
	}
}
