package com.example.blast;

import java.io.IOException;

import com.hipmob.gifanimationdrawable.GifAnimationDrawable;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

public class myProgressBar extends Dialog {
	
	private Context context;
	
	public myProgressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		setContentView(R.layout.dlg_loading);
		
		try {
			GifAnimationDrawable gifDrawable = new GifAnimationDrawable(this.context.getResources().openRawResource(R.raw.earth));
			gifDrawable.setOneShot(false);
			
			ImageView imgView = (ImageView) findViewById(R.id.img_view);
			if(imgView.getDrawable() == null) {
				imgView.setImageDrawable(gifDrawable);
				gifDrawable.setVisible(true, true);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				myProgressBar.super.show();
			}
		});
	}
}
