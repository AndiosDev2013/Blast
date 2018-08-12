package com.example.blast.ui.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import com.example.blast.AppCommonInfo;
import com.example.blast.utils.BaseTask;
import com.example.blast.utils.BaseTask.TaskListener;
import com.example.blast.Constants;
import com.example.blast.R;
import com.example.blast.VideoControllerView;
import com.example.blast.VideoControllerView.MediaPlayerControl;
import com.example.blast.database.FavoriteDatabase;
import com.example.blast.http.Server;
import com.example.blast.model.ErrorModel;
import com.example.blast.model.VideoModel;
import com.example.blast.utils.myImageLoader;
import com.example.blast.utils.YoutubeExtractor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.VideoView;

public class myMediaPlayer2 extends Activity
implements 	OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnErrorListener, OnClickListener, OnInfoListener, OnSeekBarChangeListener, MediaPlayerControl {

	private static final String TAG = myMediaPlayer2.class.getName();
	public static myMediaPlayer2 instance = null;

	/*
	 * UI
	 */
	// top layout
	private LinearLayout		layout_title;
	private ImageButton			btn_back;

	// left layout
	private LinearLayout		layout_left;
	private ImageButton			btn_vote_down, btn_vote_up;
	private TextView			txt_vote_down_count, txt_vote_up_count;
	private TextView			txt_video_title, txt_video_desc;

	// right layout
	private ListView 			lst_video;

	// bottom layout
	private LinearLayout		layout_control;
	private SeekBar        		mProgress;
	private ImageView         	btn_share, btn_fullscreen, btn_backward, btn_play, btn_forward;
	private TextView            txt_current_time, txt_end_time;

	// center layout
	private TextView 			txt_download_speed;
	private TextView			txt_buffering;
	private VideoView			android_videoview;

	// anchor view of full screen mode
	private VideoControllerView controller;

	// progress dialog
	private ProgressDialog		pDialog;

	// facebook share
	//private UiLifecycleHelper	uiHelper;

	/*
	 * Data
	 */
	public static int			RQ_SEND_MAIL = 0;

	private String 				mChannelID;

	StringBuilder               mFormatBuilder;
	Formatter                   mFormatter;

	private Handler 			mUpdateProgresshandler = new Handler();
	public int					current_video_index = 0;

	private boolean				mFullscreenMode = false;

	public FavoriteDatabase 	mFavDatabase;


	/*
	 * Activity's functions
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;

		setContentView(R.layout.activity_my_media_player2);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Intent intent = getIntent();
		if (intent != null) {
			mChannelID = intent.getStringExtra("tid"); 
		}

		// android video view
		android_videoview = (VideoView) findViewById(R.id.android_videoview);

		// for facebook share
//		uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
//			@Override
//			public void call(Session session, SessionState state, Exception exception) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		uiHelper.onCreate(savedInstanceState);

		// get data from server
		getVideoInfoFromServer();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

//		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

//		uiHelper.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

//		uiHelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// TODO Auto-generated method stub
		if (requestCode == RQ_SEND_MAIL) {
			current_video_index++;
			playNewVideo();
		}

		super.onActivityResult(requestCode, resultCode, data);

//		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
//
//			@Override
//			public void onError(PendingCall pendingCall, Exception error, Bundle data) {
//				// TODO Auto-generated method stub
//				Log.e("Facebook", String.format("Fail: %s", error.toString()));
//			}
//
//			@Override
//			public void onComplete(PendingCall pendingCall, Bundle data) {
//				// TODO Auto-generated method stub
//				Log.i("Facebook", "Success");
//			}
//		});
	}

	private void initControllerView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		// buttons for Thumb down/up
		btn_vote_down = (ImageButton) findViewById(R.id.btn_vote_down);
		btn_vote_down.setOnClickListener(this);
		btn_vote_up = (ImageButton) findViewById(R.id.btn_vote_up);
		btn_vote_up.setOnClickListener(this);

		// text for thumb down / up count
		txt_vote_down_count = (TextView) findViewById(R.id.txt_vote_down_count);
		txt_vote_up_count = (TextView) findViewById(R.id.txt_vote_up_count);

		// text for video
		txt_video_title = (TextView) findViewById(R.id.txt_video_title);
		txt_video_desc = (TextView) findViewById(R.id.txt_video_desc);

		// progress bar
		txt_end_time = (TextView) findViewById(R.id.txt_end_time);
		txt_current_time = (TextView) findViewById(R.id.txt_current_time);
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		mProgress = (SeekBar) findViewById(R.id.mediacontroller_progress);
		mProgress.setOnSeekBarChangeListener(this);

		// player
		txt_download_speed = (TextView) findViewById(R.id.txt_download_speed);
		txt_buffering = (TextView) findViewById(R.id.txt_buffering);
		btn_share = (ImageView) findViewById(R.id.btn_share);
		btn_fullscreen = (ImageView) findViewById(R.id.btn_fullscreen);
		btn_backward = (ImageView) findViewById(R.id.btn_backward);
		btn_play = (ImageView) findViewById(R.id.btn_play);
		btn_forward = (ImageView) findViewById(R.id.btn_forward);
		btn_share.setOnClickListener(this);
		btn_fullscreen.setOnClickListener(this);
		btn_backward.setOnClickListener(this);
		btn_play.setOnClickListener(this);
		btn_forward.setOnClickListener(this);

		// video ListView
		lst_video = (ListView) findViewById(R.id.lst_video);
		LazyAdapter adapter = new LazyAdapter(this, AppCommonInfo.VideoList);
		lst_video.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		// layout for full screen
		layout_title = (LinearLayout) findViewById(R.id.layout_title);
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_control = (LinearLayout) findViewById(R.id.layout_control);

		// anchor view
		controller = new VideoControllerView(this);
		controller.setPrevNextListeners(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				forwardPlay();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backwardPlay();
			}
		});

		current_video_index = 0;
		playNewVideo();
	}

	private void setEnableUI(boolean flag) {
		btn_vote_down.setEnabled(flag);
		btn_vote_up.setEnabled(flag);

		btn_share.setEnabled(flag);
		btn_fullscreen.setEnabled(flag);
		btn_backward.setEnabled(flag);
		btn_play.setEnabled(flag);
		btn_backward.setEnabled(flag);
		mProgress.setEnabled(flag);

		lst_video.setEnabled(flag);
	}

	@Override
	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");

		// add listener to media player
		mediaplayer.setOnBufferingUpdateListener(this);
		mediaplayer.setOnInfoListener(this);

		// set controller's media player
		controller.setMediaPlayer(this);
		controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));

		hideProgressDialog();
		android_videoview.start();

		mProgress.setMax((int)android_videoview.getDuration());
		updatePosition();

		btn_play.setImageResource(R.drawable.bg_btn_media_pause);

		// enable UI
		setEnableUI(true);
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		txt_buffering.setText("Buffering: " + percent + "%");
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			break;

		case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
			//Display video download speed
			txt_download_speed.setText("" + extra + " kb/s");
			break;
		}
		return true;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		// hide progress
		hideProgressDialog();

		// show alert dialog
		new AlertDialog.Builder(this)
		.setTitle("Hi !!!")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setMessage("There is some error on the video file or URL, please Notice that to Blast Site Administrator.")
		.setPositiveButton("Yes, I will send", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (AppCommonInfo.VideoList.size() > current_video_index && current_video_index >= 0) {
					VideoModel.DetailInfo info = AppCommonInfo.VideoList.get(current_video_index);

					// send email to administrator
					String strbody = "PLEASE CHECK THIS VIDEO FILE OR URL\n\n"
							+ "**********************\n\n" 
							+ "Category:= " + info.category_name + "\n\n"
							+ "Title:= " + info.title + "\n\n"
							+ "Url:= " + info.uri.substring(0, 9) + "..." + info.uri.substring(info.uri.length()-10, info.uri.length()) + "\n\n"
							+ "**********************\n\n"
							+ "  When I play this video, i got some error, please check this video file.\n\nthanks."; 


					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] { Constants.ADMIN_EMAIL_ADDRESS });
					intent.putExtra(Intent.EXTRA_SUBJECT, "Please check Video URL");
					intent.putExtra(Intent.EXTRA_TEXT, strbody);
					intent.setType("html/text");

					startActivityForResult(Intent.createChooser(intent, "Send mail..."), RQ_SEND_MAIL);
				}

			}
		})
		.setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// if we donot check current_video_index is last index, the app will enter the endless looping.
				if (current_video_index != AppCommonInfo.VideoList.size()-1) {
					current_video_index++;
					playNewVideo();
				}
			}
		})
		.show();

		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			myOnbackPressed();
			break;

		case R.id.btn_vote_down:
			Toast.makeText(this, "clicked thumb down", Toast.LENGTH_LONG).show();
			doVoteDown();
			break;

		case R.id.btn_vote_up:
			Toast.makeText(this, "clicked thumb up", Toast.LENGTH_LONG).show();
			doVoteUp();
			break;

		case R.id.btn_share:
			shareVideo();
			break;

		case R.id.btn_fullscreen:
			showFullscreen();
			break;

		case R.id.btn_backward:
			backwardPlay();
			break;

		case R.id.btn_play:
			doPauseResume();
			break;

		case R.id.btn_forward:
			forwardPlay();
			break;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// hide progress
		hideProgressDialog();

		// set button pause
		btn_play.setImageResource(R.drawable.btn_media_play_normal);

		current_video_index ++;
		playNewVideo();
	}

	/**
	 * Show the controller on screen. It will go away
	 * automatically after 'timeout' milliseconds of inactivity.
	 * @param timeout The timeout in milliseconds. Use 0 to show
	 * the controller until hide() is called.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mFullscreenMode) {
			controller.show();
		}

		return false;
	}

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours   = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	private void doPauseResume() {
		if (android_videoview.isPlaying()) {
			android_videoview.pause();
		} else {
			android_videoview.start();
		}

		// set play or pause button
		if (android_videoview.isPlaying()) {
			btn_play.setImageResource(R.drawable.bg_btn_media_pause);
		} else {
			btn_play.setImageResource(R.drawable.bg_btn_media_play);
		}
	}

	public void playNewVideo() {
		Toast.makeText(this, "Play Next Video", Toast.LENGTH_LONG).show();

		ArrayList<VideoModel.DetailInfo> list = AppCommonInfo.VideoList;
		if (current_video_index < 0) {
			current_video_index = 0;
			return;
		}
		if (current_video_index >= list.size()) {
			current_video_index = list.size() - 1;
			return;
		}

		// play new video with URL info
		String video_url = list.get(current_video_index).uri;
		if (TextUtils.isEmpty(video_url))
			return;

		// set vote_up count and vote_down count
		txt_vote_up_count.setText(list.get(current_video_index).vote_up_count);
		txt_vote_down_count.setText(list.get(current_video_index).vote_down_count);

		// set video title and description
		if (list.get(current_video_index).title != null) {
			txt_video_title.setText(list.get(current_video_index).title);
		} else  {
			txt_video_title.setText("");
		}
		if (list.get(current_video_index).description != null) {
			txt_video_desc.setText(list.get(current_video_index).description);
		} else  {
			txt_video_desc.setText("");
		}

		// disable all UI
		setEnableUI(false);

		// play
		new StreamVideo().execute();
	}

	// StreamVideo AsyncTask
	private class StreamVideo extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressbar
			showProgressDialog("Buffering...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				// Start the MediaController
				Uri video = Uri.parse(AppCommonInfo.VideoList.get(current_video_index).uri);
				android_videoview.setVideoURI(video);

				android_videoview.requestFocus();
				android_videoview.setOnPreparedListener(myMediaPlayer2.this);
				android_videoview.setOnErrorListener(myMediaPlayer2.this);
				android_videoview.setOnCompletionListener(myMediaPlayer2.this);

			} catch (Exception e) {
				hideProgressDialog();
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void shareVideo() {
		String items[] = {"Share via Email", "Share via Facebook"};
		new AlertDialog.Builder(this)
		.setSingleChoiceItems(items, 0, null)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
				// Do something useful withe the position of the selected radio button
				if (selectedPosition == 0) {
					shareVideoViaEmail();
				} else if (selectedPosition == 1) {
					shareVideoViaFacebook();
				}
			}
		})
		.show();
	}

	private void shareVideoViaEmail() {
		String strbody = "Good Video\n\n"
				+ "**********************\n\n" 
				+ "Title: " + AppCommonInfo.VideoList.get(current_video_index).title + "\n\n"
				+ "Url: " + AppCommonInfo.VideoList.get(current_video_index).uri + "\n\n"
				+ "**********************\n\n"
				+ "Please Enjoy. :)";

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
		intent.putExtra(Intent.EXTRA_SUBJECT, "Share video of Blast");
		intent.putExtra(Intent.EXTRA_TEXT, strbody);
		intent.setType("html/text");

		startActivity(Intent.createChooser(intent, "Send mail..."));
	}

	private void shareVideoViaFacebook() {

//		FacebookDialog shareDlg = null;
//		try {
//			shareDlg = new FacebookDialog.ShareDialogBuilder(this)
//			.setName("Blast Video")
//			.setCaption("There is good video here, please enjoy.")
//			.setPicture(AppCommonInfo.VideoList.get(current_video_index).thumbnail_img)
//			.setDescription("Title: " + AppCommonInfo.VideoList.get(current_video_index).title + "</br>"
//					+ "Description: " + AppCommonInfo.VideoList.get(current_video_index).description + "</br>"
//					+ "Url: " + AppCommonInfo.VideoList.get(current_video_index).uri)
//					.setLink(ServerConfig.HOST)
//					.build();
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			Toast.makeText(this, "It seems the Facebook App is not installed. Please install Facebook App First for Sharing.", Toast.LENGTH_LONG).show();
//		}
//
//		if (shareDlg != null) {
//			uiHelper.trackPendingDialogCall(shareDlg.present());
//		}
	}

	private void showFullscreen() {
		Animation slide_out_up = AnimationUtils.loadAnimation(this, R.anim.slide_out_up);
		layout_title.startAnimation(slide_out_up);

		Animation slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
		layout_left.startAnimation(slide_out_left);

		Animation slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
		lst_video.startAnimation(slide_out_right);

		Animation slide_out_down = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
		layout_control.startAnimation(slide_out_down);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				layout_title.setVisibility(View.GONE);
				layout_left.setVisibility(View.GONE);
				lst_video.setVisibility(View.GONE);
				layout_control.setVisibility(View.GONE);
			}
		}, getResources().getInteger(R.integer.slide_animation_time));

		// set fullscreen mode flag
		mFullscreenMode = true;

		// play video
		//playNewVideo();
	}

	private void doVoteDown() {
		forwardPlay();
	}

	private void doVoteUp() {
		mFavDatabase = new FavoriteDatabase(this);
		mFavDatabase.open();

		Cursor cur = mFavDatabase.getSameURL(AppCommonInfo.VideoList.get(current_video_index).uri);
		if (cur != null && cur.getCount() > 0) {
			Toast.makeText(this, "Already in Fav List", Toast.LENGTH_LONG).show();

		} else {
			VideoModel.DetailInfo item = AppCommonInfo.VideoList.get(current_video_index);
			long dd = mFavDatabase.InsertOne(
					item.title,
					item.description, 
					item.category_name, 
					item.thumbnail_img,
					item.uri,
					item.vote_up_count,
					item.vote_down_count
					);

			if (dd > 0) {
				Toast.makeText(this, "Current video added as Favorite", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void showNormalScreen() {
		controller.setVisibility(View.GONE);

		Animation slide_in_down = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
		layout_title.startAnimation(slide_in_down);

		Animation slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
		layout_left.startAnimation(slide_in_right);

		Animation slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		lst_video.startAnimation(slide_in_left);

		Animation slide_in_up = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
		layout_control.startAnimation(slide_in_up);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				layout_title.setVisibility(View.VISIBLE);
				layout_left.setVisibility(View.VISIBLE);
				lst_video.setVisibility(View.VISIBLE);
				layout_control.setVisibility(View.VISIBLE);
			}
		}, getResources().getInteger(R.integer.slide_animation_time));

		// set fullscreen mode flag
		mFullscreenMode = false;

		// play video
		//playNewVideo();
	}

	private void backwardPlay() {
		current_video_index--;
		playNewVideo();
	}

	private void forwardPlay() {
		current_video_index++;
		playNewVideo();
	}

	private final Runnable updatePositionRunnable = new Runnable() {
		public void run() {
			updatePosition();
		}
	};

	private void updatePosition() {
		if (updatePositionRunnable == null || mUpdateProgresshandler == null)
			return;

		mUpdateProgresshandler.removeCallbacks(updatePositionRunnable);

		int duration = (int)android_videoview.getDuration();
		int position = (int)android_videoview.getCurrentPosition();

		mProgress.setProgress(position);

		if (txt_end_time != null)
			txt_end_time.setText(stringForTime(duration));
		if (txt_current_time != null)
			txt_current_time.setText(stringForTime(position));

		mUpdateProgresshandler.postDelayed(updatePositionRunnable, 500);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		if (fromUser) {
			android_videoview.seekTo(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	private void getVideoInfoFromServer() {
		if (TextUtils.isEmpty(mChannelID))
			return;

		showProgressDialog("Getting Video List by Channel, Please wait...");

		BaseTask.run(new TaskListener() {
			@Override
			public Object onTaskRunning(int taskId, Object data) {
				Object result = Server.GetVideoListByChannel(mChannelID);

				if (result instanceof VideoModel.Result) {
					VideoModel.Result info_list = (VideoModel.Result) result;
					saveToGlovalVideoList(info_list.result);
					return null;
				}

				return ((String) result);
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				// Dismiss the progress dialog
				hideProgressDialog();

				if (result == null) {
					Toast.makeText(myMediaPlayer2.this, "Get Video List Success", Toast.LENGTH_LONG).show();

					if (AppCommonInfo.VideoList.size() > 0) {
						GetVoteInfoFromServer(AppCommonInfo.VideoList.get(0).id);
					}

					initControllerView();

				} else {
					Toast.makeText(myMediaPlayer2.this, "Get Video List Fail: " + (String)result, Toast.LENGTH_LONG).show();
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

	private void GetVoteInfoFromServer(final String video_id) {
		if (TextUtils.isEmpty(video_id))
			return;

		showProgressDialog("Please wait. Get Vote Status of Video...");

		BaseTask.run(new TaskListener() {
			@Override
			public Object onTaskRunning(int taskId, Object data) {
				Object result = Server.GetVoteStatusOfVideo(video_id);

				if (result instanceof VideoModel.Result) {
					VideoModel.Result info_list = (VideoModel.Result) result;

					//					if (info_list.status == ServerConfig.STATUS_SUCCESS) {
					//						return null;
					//
					//					} else {
					//						return info_list.result;
					//					}

				} else if (result instanceof String) {
					return ErrorModel.GetErrorString((String)result);
				}

				return ((String) result);
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				// hide the progress dialog
				hideProgressDialog();

				if (result == null) {
					Toast.makeText(myMediaPlayer2.this, "Get Vote Status Success", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(myMediaPlayer2.this, "Get Vote Status Fail: " + (String)result, Toast.LENGTH_LONG).show();
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

	/*
	 * Save video infomation to App Common Video information list
	 */

	private void saveToGlovalVideoList(ArrayList<VideoModel.Info> info_list) {
		AppCommonInfo.VideoList.clear();

		for (int i = 0; i < info_list.size(); i++) {
			VideoModel.Info item = info_list.get(i);

			VideoModel.DetailInfo detail_item = new VideoModel.DetailInfo();
			detail_item.id = item.nid;
			detail_item.category_name = item.Channel;
			detail_item.title = item.node_title;
			detail_item.description = item.Description;
			detail_item.thumbnail_img = "";
			detail_item.vote_up_count = "";
			detail_item.vote_down_count = "";

			if (YoutubeExtractor.isYoutubeURL(item.Video)) {
				String video_id = YoutubeExtractor.parseYoutubeVideoId(item.Video);
				YoutubeExtractor.video_url_map.clear();
				try {
					YoutubeExtractor.extractVideobyId(video_id);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (YoutubeExtractor.video_url_map.containsKey(YoutubeExtractor.VideoQuality.Medium360)) {
					URL url = (URL)YoutubeExtractor.video_url_map.get(YoutubeExtractor.VideoQuality.Medium360);
					detail_item.uri = url.toString();
				} else if (YoutubeExtractor.video_url_map.containsKey(YoutubeExtractor.VideoQuality.HD720)) {
					URL url = (URL)YoutubeExtractor.video_url_map.get(YoutubeExtractor.VideoQuality.HD720);
					detail_item.uri = url.toString();
				} else 	if (YoutubeExtractor.video_url_map.containsKey(YoutubeExtractor.VideoQuality.Small240)) {
					URL url = (URL)YoutubeExtractor.video_url_map.get(YoutubeExtractor.VideoQuality.Small240);
					detail_item.uri = url.toString();
				} else if (YoutubeExtractor.video_url_map.containsKey(YoutubeExtractor.VideoQuality.HD1080)) {
					URL url = (URL)YoutubeExtractor.video_url_map.get(YoutubeExtractor.VideoQuality.HD1080);
					detail_item.uri = url.toString();
				}

			} else {
				detail_item.uri = item.Video;
			}

			AppCommonInfo.VideoList.add(detail_item);
		}
	}

	public class LazyAdapter extends BaseAdapter {
		Activity act;
		ArrayList<VideoModel.DetailInfo> mArrayList;
		String mitem[];
		private LayoutInflater inflater = null;

		public LazyAdapter(Activity a, ArrayList<VideoModel.DetailInfo> category) {
			act = a;
			if (category == null) {
				this.mArrayList = new ArrayList<VideoModel.DetailInfo>();
			} else {
				this.mArrayList = category;
			}

			inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return mArrayList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			public RelativeLayout item_layout;
			public ImageView img_thumbnail;
			public ImageButton btn_play;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_video_vertical, null);
				holder = new ViewHolder();
				holder.item_layout = (RelativeLayout) convertView.findViewById(R.id.item_layout);
				holder.img_thumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);
				holder.btn_play = (ImageButton) convertView.findViewById(R.id.btn_play);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			try {
				ViewGroup.LayoutParams params = holder.item_layout.getLayoutParams();
				params.height = (int)(Constants.SCREEN_WIDTH * 0.12);

				myImageLoader.showImage(holder.img_thumbnail, mArrayList.get(position).thumbnail_img);
				holder.btn_play.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						current_video_index = position;
						playNewVideo();
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	private void hideProgressDialog() {
		if (pDialog == null)
			return;

		pDialog.dismiss();
	}

	private void showProgressDialog(String message) {
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
		}

		pDialog.setMessage(message);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void seekTo(int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFullScreen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showFullScreen() {
		// TODO Auto-generated method stub
		showNormalScreen();
	}

	private void myOnbackPressed() {
		if (mFullscreenMode) {
			showNormalScreen();

		} else {
			new AlertDialog.Builder(this)
			.setTitle("Hi")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage("Are you stop to play video?")
			.setPositiveButton("Yes, Stop", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					myMediaPlayer2.super.onBackPressed();
				}
			})
			.setNegativeButton("No, Continue", null)
			.show();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		myOnbackPressed();
	}
}
