package com.example.blast;

public class myMediaPlayer1 {
	
}
//public class myMediaPlayer1 extends YouTubeBaseActivity
//implements 	OnBufferingUpdateListener, OnCompletionListener, Callback, OnPreparedListener, OnErrorListener,
//OnVideoSizeChangedListener, OnClickListener, OnInfoListener, OnSeekBarChangeListener, MediaPlayerControl, YouTubePlayer.OnInitializedListener {
//
//	private static final String TAG = myMediaPlayer1.class.getName();
//	public static myMediaPlayer1 instance = null;
//
//	/*
//	 * UI
//	 */
//	// top layout
//	private LinearLayout		layout_title;
//	private ImageButton			btn_back;
//
//	// left layout
//	private LinearLayout		layout_left;
//	private ImageButton			btn_vote_down, btn_vote_up;
//	private TextView			txt_vote_down_count, txt_vote_up_count;
//	private TextView			txt_video_title, txt_video_desc;
//
//	// right layout
//	private ListView 			lst_video;
//
//	// bottom layout
//	private LinearLayout		layout_control;
//	private SeekBar        		mProgress;
//	private ImageView         	btn_share, btn_fullscreen, btn_backward, btn_play, btn_forward;
//	private TextView            txt_current_time, txt_end_time;
//
//	// center layout
//	private CenterLayout		vitamio_view;
//	private TextView 			txt_download_speed;
//	private SurfaceView	 		mVideoSurface;
//	private YouTubePlayerView	youtube_view;
//	private VideoView			android_videoview;
//
//	// anchor view of full screen mode
//	private VideoControllerView controller;
//
//	// progress dialog
//	private ProgressDialog		pDialog;
//	
//	// facebook share
//	private UiLifecycleHelper	uiHelper;
//
//	/*
//	 * Data
//	 */
//	public static int			RQ_SEND_MAIL = 0;
//
//	private String 				mChannelID;
//	private String				mChannelName;
//
//	public static MediaPlayer 	mMediaPlayer;
//	private SurfaceHolder 		holder;
//	private	YouTubePlayer		mYoutubePlayer = null;
//	private boolean				mYoutubePlayerInitialized = false;
//	
//	StringBuilder               mFormatBuilder;
//	Formatter                   mFormatter;
//	private int 				mVideoWidth;
//	private int 				mVideoHeight;
//	private int 				mVideoSurfaceWidth;
//	private int 				mVideoSurfaceHeight;
//
//	private boolean 			mIsVideoSizeKnown = false;
//	private boolean 			mIsVideoReadyToBePlayed = false;
//	private boolean				mIsActivityPaused = false; // save playing progress of player when call onpause/onresume & fullscren / normal screen
//	private long				mCurrentProgress = 0; // save playing progress of player when call onpause/onresume & fullscren / normal screen
//
//	private Handler 			mUpdateProgresshandler = new Handler();
//	public int					current_video_index = 0;
//
//	private boolean				mFullscreenMode = false;
//
//	public FavoriteDatabase 	mFavDatabase;
//	
//
//	/*
//	 * Activity's functions
//	 */
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		instance = this;
//
//		if (!LibsChecker.checkVitamioLibs(this))
//			return;
//
//		setContentView(R.layout.activity_my_media_player);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//		// get data from main activity
//		//		intent.putExtra("tid", item.tid);
//		//		intent.putExtra("vid", item.vid);
//		//		intent.putExtra("name", item.name);
//		//		intent.putExtra("format", item.format);
//		//		intent.putExtra("weight", item.weight);
//		//		intent.putExtra("uri", item.uri);
//		Intent intent = getIntent();
//		if (intent != null) {
//			mChannelID = intent.getStringExtra("tid"); 
//			mChannelName = intent.getStringExtra("name");
//		}
//		
//		// Youtube view
//		youtube_view = (YouTubePlayerView) findViewById(R.id.youtube_view);
//		youtube_view.initialize(Constants.YOUTUBE_API_KEY, this);
//		youtube_view.setVisibility(View.GONE);
//		
//		// android video view
//		android_videoview = (VideoView) findViewById(R.id.android_videoview);
//		
//		// for facebook share
//		uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
//			@Override
//			public void call(Session session, SessionState state, Exception exception) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		uiHelper.onCreate(savedInstanceState);
//	}
//	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		
//		if (mIsActivityPaused) {
//			try {
//				/*
//				 * TODO: Set path variable to progressive streamable mp4 or
//				 * 3gpp format URL. Http protocol should be used.
//				 * Mediaplayer can only play "progressive streamable
//				 * contents" which basically means: 1. the movie atom has to
//				 * precede all the media data atoms. 2. The clip has to be
//				 * reasonably interleaved.
//				 */
//				mMediaPlayer = new MediaPlayer(this);
//				
//				mMediaPlayer.setDataSource(this, Uri.parse(AppCommonInfo.VideoList.get(0).uri));
//				mMediaPlayer.setDisplay(holder);
//				mMediaPlayer.prepareAsync();
//				mMediaPlayer.setOnBufferingUpdateListener(this);
//				mMediaPlayer.setOnCompletionListener(this);
//				mMediaPlayer.setOnPreparedListener(this);
//				mMediaPlayer.setOnVideoSizeChangedListener(this);
//				mMediaPlayer.setOnErrorListener(this);
//				mMediaPlayer.setOnInfoListener(this);
//				
//				setVolumeControlStream(AudioManager.STREAM_MUSIC);
//
//			} catch (Exception e) {
//				Log.e(TAG, "error: " + e.getMessage(), e);
//			}
//		}
//		
//		uiHelper.onResume();
//	}
//	
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		// TODO Auto-generated method stub
//		super.onSaveInstanceState(outState);
//		
//		uiHelper.onSaveInstanceState(outState);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		
//		mIsActivityPaused = true;
//		
//		releaseMediaPlayer();
//		doCleanUp();
//
//		uiHelper.onPause();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		
//		releaseMediaPlayer();
//		doCleanUp();
//		
//		uiHelper.onDestroy();
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		
//		// TODO Auto-generated method stub
//		if (requestCode == RQ_SEND_MAIL) {
//			current_video_index++;
//			playNewVideo();
//		}
//		
//		super.onActivityResult(requestCode, resultCode, data);
//
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
//	}
//	
//	private void initControllerView() {
//		btn_back = (ImageButton) findViewById(R.id.btn_back);
//		btn_back.setOnClickListener(this);
//
//		mVideoSurface = (SurfaceView)findViewById(R.id.vitamio_surface);
//		mVideoSurface.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//			@Override
//			public void onLayoutChange(View v, int left, int top, int right,
//					int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//				// save size of video surface
//				mVideoSurfaceHeight = bottom - top;
//				mVideoSurfaceWidth = right - left;
//			}
//		});
//		holder = mVideoSurface.getHolder();
//		holder.addCallback(this);
//		holder.setFormat(PixelFormat.RGBA_8888);
//		
//		// buttons for Thumb down/up
//		btn_vote_down = (ImageButton) findViewById(R.id.btn_vote_down);
//		btn_vote_down.setOnClickListener(this);
//		btn_vote_up = (ImageButton) findViewById(R.id.btn_vote_up);
//		btn_vote_up.setOnClickListener(this);
//
//		// text for thumb down / up count
//		txt_vote_down_count = (TextView) findViewById(R.id.txt_vote_down_count);
//		txt_vote_up_count = (TextView) findViewById(R.id.txt_vote_up_count);
//
//		// text for video
//		txt_video_title = (TextView) findViewById(R.id.txt_video_title);
//		txt_video_desc = (TextView) findViewById(R.id.txt_video_desc);
//
//		// progress bar
//		txt_end_time = (TextView) findViewById(R.id.txt_end_time);
//		txt_current_time = (TextView) findViewById(R.id.txt_current_time);
//		mFormatBuilder = new StringBuilder();
//		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
//		mProgress = (SeekBar) findViewById(R.id.mediacontroller_progress);
//		mProgress.setOnSeekBarChangeListener(this);
//
//		// player
//		txt_download_speed = (TextView) findViewById(R.id.txt_download_speed);
////		if (mYoutubePlayerInitialized)
////			txt_download_speed.setVisibility(View.GONE);
//		btn_share = (ImageView) findViewById(R.id.btn_share);
//		btn_fullscreen = (ImageView) findViewById(R.id.btn_fullscreen);
//		btn_backward = (ImageView) findViewById(R.id.btn_backward);
//		btn_play = (ImageView) findViewById(R.id.btn_play);
//		btn_forward = (ImageView) findViewById(R.id.btn_forward);
//		btn_share.setOnClickListener(this);
//		btn_fullscreen.setOnClickListener(this);
//		btn_backward.setOnClickListener(this);
//		btn_play.setOnClickListener(this);
//		btn_forward.setOnClickListener(this);
//
//		// video ListView
//		lst_video = (ListView) findViewById(R.id.lst_video);
//		LazyAdapter adapter = new LazyAdapter(this, AppCommonInfo.VideoList);
//		lst_video.setAdapter(adapter);
//		adapter.notifyDataSetChanged();
//
//		// layout for full screen
//		layout_title = (LinearLayout) findViewById(R.id.layout_title);
//		layout_left = (LinearLayout) findViewById(R.id.layout_left);
//		layout_control = (LinearLayout) findViewById(R.id.layout_control);
////		if (mYoutubePlayerInitialized)
////			layout_control.setVisibility(View.GONE);
//		vitamio_view = (CenterLayout) findViewById(R.id.vitamio_view);
//
//		// anchor view
//		controller = new VideoControllerView(this);
//		controller.setPrevNextListeners(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				forwardPlay();
//			}
//		}, new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				backwardPlay();
//			}
//		});
//		
//		current_video_index = 0;
//		playNewVideo();
//	}
//	
//	private void setEnableUI(boolean flag) {
//		btn_vote_down.setEnabled(flag);
//		btn_vote_up.setEnabled(flag);
//
//		btn_share.setEnabled(flag);
//		btn_fullscreen.setEnabled(flag);
//		btn_backward.setEnabled(flag);
//		btn_play.setEnabled(flag);
//		btn_backward.setEnabled(flag);
//		mProgress.setEnabled(flag);
//
//		lst_video.setEnabled(flag);
//	}
//
//	@Override
//	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
//		// Log.d(TAG, "onBufferingUpdate percent:" + percent);
//	}
//
//	@Override
//	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//		Log.v(TAG, "onVideoSizeChanged called");
//		if (width == 0 || height == 0) {
//			Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
//			return;
//		}
//		mIsVideoSizeKnown = true;
//
//		mVideoWidth = width;
//		mVideoHeight = height;
//
//		int realWidth = 0;
//		int realHeight = 0;
//
//		if (width != 0 && height != 0) {
//			float ratioWidth = (float)mVideoSurfaceWidth / (float)width;
//			float ratioHeight = (float)mVideoSurfaceHeight / (float)height;
//			if (ratioWidth < ratioHeight ) {  
//				realWidth = (int)(width * ratioWidth);
//				realHeight = (int)(height * ratioWidth);
//			} else {
//				realWidth = (int)(width * ratioHeight);
//				realHeight = (int)(height * ratioHeight);
//			}
//
//			ViewGroup.LayoutParams param = mVideoSurface.getLayoutParams();
//			param.width = realWidth;
//			param.height = realHeight;
//		}
//
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//			startVideoPlayback();
//		}
//	}
//
//	@Override
//	public void onPrepared(MediaPlayer mediaplayer) {
//		Log.d(TAG, "onPrepared called");
//		mIsVideoReadyToBePlayed = true;
//
//		// set controller's media player
//		controller.setMediaPlayer(this);
//		controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
//
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//			// set pause button
//			btn_play.setImageResource(R.drawable.bg_btn_media_pause);
//
//			// set progress bar
//			mProgress.setMax((int)mMediaPlayer.getDuration());
//			updatePosition();
//
//			startVideoPlayback();
//		}
//	}
//
//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//		Log.d(TAG, "surfaceChanged called");
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
//		Log.d(TAG, "surfaceDestroyed called");
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		Log.d(TAG, "surfaceCreated called");
//	}
//
//
//	boolean isStart = false;
//
//	@Override
//	public boolean onInfo(MediaPlayer mp, int what, int extra) {
//	    switch (what) {
//	    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//	    	break;
//	    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//	    	break;
//	    	
//	    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//	    	//Display video download speed
//	    	txt_download_speed.setText("" + extra + " kb/s");
//	        break;
//	    }
//	    return true;
//	}
//	
//	@Override
//	public boolean onError(MediaPlayer mp, int what, int extra) {
//		// TODO Auto-generated method stub
//		// hide progress
//		hideProgressDialog();
//
//		// show alert dialog
//		new AlertDialog.Builder(this)
//		.setTitle("Hi !!!")
//		.setIcon(android.R.drawable.ic_dialog_alert)
//		.setMessage("There is some error on the video file or URL, please Notice that to Blast Site Administrator.")
//		.setPositiveButton("Yes, I will send", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				if (AppCommonInfo.VideoList.size() > current_video_index && current_video_index >= 0) {
//					VideoModel.DetailInfo info = AppCommonInfo.VideoList.get(current_video_index);
//
//					// send email to administrator
//					String strbody = "PLEASE CHECK THIS VIDEO FILE OR URL\n\n"
//							+ "**********************\n\n" 
//							+ "Category:= " + info.category_name + "\n\n"
//							+ "Title:= " + info.title + "\n\n"
//							+ "Url:= " + info.uri.substring(0, 9) + "..." + info.uri.substring(info.uri.length()-10, info.uri.length()) + "\n\n"
//							+ "**********************\n\n"
//							+ "  When I play this video, i got some error, please check this video file.\n\nthanks."; 
//
//
//					Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//					intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { Constants.ADMIN_EMAIL_ADDRESS });
//					intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Please check Video URL");
//					intent.putExtra(android.content.Intent.EXTRA_TEXT, strbody);
//					intent.setType("html/text");
//
//					startActivityForResult(Intent.createChooser(intent, "Send mail..."), RQ_SEND_MAIL);
//				}
//
//			}
//		})
//		.setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				// if we donot check current_video_index is last index, the app will enter the endless looping.
//				if (current_video_index != AppCommonInfo.VideoList.size()-1) {
//					current_video_index++;
//					playNewVideo();
//				}
//			}
//		})
//		.show();
//
//		return true;
//	}
//
//	private void releaseMediaPlayer() {
//		if (mMediaPlayer != null) {
//			mCurrentProgress = mMediaPlayer.getCurrentPosition();
//			
//			mMediaPlayer.release();
//			mMediaPlayer = null;
//		}
//	}
//
//	private void doCleanUp() {
//		mVideoWidth = 0;
//		mVideoHeight = 0;
//		mIsVideoReadyToBePlayed = false;
//		mIsVideoSizeKnown = false;
//	}
//
//	private void startVideoPlayback() {
//		Log.v(TAG, "startVideoPlayback");
//		// hide progress bar
//		hideProgressDialog();
//
//		// enable UI
//		setEnableUI(true);
//
//		// start video
//		holder.setFixedSize(mVideoWidth, mVideoHeight);
//		mMediaPlayer.start();
//		
//		// from onresume function after calling onpause
//		if (mIsActivityPaused) {
//			mMediaPlayer.seekTo(mCurrentProgress);
//			mCurrentProgress = 0;
//			mIsActivityPaused = false;
//		}
//	}
//
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.btn_back:
//			myOnbackPressed();
//			break;
//
//		case R.id.btn_vote_down:
//			Toast.makeText(this, "clicked thumb down", Toast.LENGTH_LONG).show();
//			doVoteDown();
//			break;
//
//		case R.id.btn_vote_up:
//			Toast.makeText(this, "clicked thumb up", Toast.LENGTH_LONG).show();
//			doVoteUp();
//			break;
//
//		case R.id.btn_share:
//			shareVideo();
//			break;
//
//		case R.id.btn_fullscreen:
//			showFullscreen();
//			break;
//
//		case R.id.btn_backward:
//			backwardPlay();
//			break;
//
//		case R.id.btn_play:
//			doPauseResume();
//			break;
//
//		case R.id.btn_forward:
//			forwardPlay();
//			break;
//		}
//	}
//
//	@Override
//	public void onCompletion(MediaPlayer mp) {
//		// TODO Auto-generated method stub
//		// hide progress
//		hideProgressDialog();
//
//		// set button pause
//		btn_play.setImageResource(R.drawable.btn_media_play_normal);
//
//		current_video_index ++;
//		playNewVideo();
//	}
//
//	/**
//	 * Show the controller on screen. It will go away
//	 * automatically after 'timeout' milliseconds of inactivity.
//	 * @param timeout The timeout in milliseconds. Use 0 to show
//	 * the controller until hide() is called.
//	 */
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (mFullscreenMode) {
//			controller.show();
//		}
//
//		return false;
//	}
//
//	private String stringForTime(int timeMs) {
//		int totalSeconds = timeMs / 1000;
//
//		int seconds = totalSeconds % 60;
//		int minutes = (totalSeconds / 60) % 60;
//		int hours   = totalSeconds / 3600;
//
//		mFormatBuilder.setLength(0);
//		if (hours > 0) {
//			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
//		} else {
//			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
//		}
//	}
//
//	private void doPauseResume() {
//		if (mMediaPlayer == null) {
//			return;
//		}
//
//		// set media player
//		if (mMediaPlayer.isPlaying()) {
//			mMediaPlayer.pause();
//		} else {
//			mMediaPlayer.start();
//		}
//
//		// set play or pause button
//		if (mMediaPlayer.isPlaying()) {
//			btn_play.setImageResource(R.drawable.bg_btn_media_pause);
//		} else {
//			btn_play.setImageResource(R.drawable.bg_btn_media_play);
//		}
//	}
//
//	public void playNewVideo() {
//		Toast.makeText(this, "Play Next Video", Toast.LENGTH_LONG).show();
//
//		ArrayList<VideoModel.DetailInfo> list = AppCommonInfo.VideoList;
//		if (current_video_index < 0) {
//			current_video_index = 0;
//			return;
//		}
//		if (current_video_index >= list.size()) {
//			current_video_index = list.size() - 1;
//			return;
//		}
//
//		// play new video with URL info
//		String video_url = list.get(current_video_index).uri;
//		if (TextUtils.isEmpty(video_url))
//			return;
//		
//		// set vote_up count and vote_down count
//		txt_vote_up_count.setText(list.get(current_video_index).vote_up_count);
//		txt_vote_down_count.setText(list.get(current_video_index).vote_down_count);
//
//		// set video title and description
//		if (list.get(current_video_index).title != null) {
//			txt_video_title.setText(list.get(current_video_index).title);
//		} else  {
//			txt_video_title.setText("");
//		}
//		if (list.get(current_video_index).description != null) {
//			txt_video_desc.setText(list.get(current_video_index).description);
//		} else  {
//			txt_video_desc.setText("");
//		}
//
//		// disable all UI
//		setEnableUI(false);
//
//		if (YoutubeUtil.isYoutubeURL(video_url) && mYoutubePlayerInitialized) {
//			vitamio_view.setVisibility(View.GONE);
//			youtube_view.setVisibility(View.VISIBLE);
//			
//			String video_id = YoutubeUtil.parseYoutubeVideoId(video_url);
//			mYoutubePlayer.cueVideo(video_id);
//			mYoutubePlayer.play();
//			
//		} else {
//			vitamio_view.setVisibility(View.VISIBLE);
//			youtube_view.setVisibility(View.GONE);
//			
//			// show progress bar
//			showProgressDialog("Video is loading. please wait...");
//
//			vitamio_view.removeView(mVideoSurface);
//			mVideoSurface = new VideoView(this);
//			mVideoSurface.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//				@Override
//				public void onLayoutChange(View v, int left, int top, int right,
//						int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//					// save size of video surface
//					mVideoSurfaceHeight = bottom - top;
//					mVideoSurfaceWidth = right - left;
//				}
//			});
//			vitamio_view.addView(mVideoSurface, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//			holder = mVideoSurface.getHolder();
//			holder.addCallback(this);
//			holder.setFormat(PixelFormat.RGBA_8888); 
//			
//			try {
//				/*
//				 * TODO: Set path variable to progressive streamable mp4 or
//				 * 3gpp format URL. Http protocol should be used.
//				 * Mediaplayer can only play "progressive streamable
//				 * contents" which basically means: 1. the movie atom has to
//				 * precede all the media data atoms. 2. The clip has to be
//				 * reasonably interleaved.
//				 */
//				// Create a new media player and set the listeners
//				if (mMediaPlayer != null) {
//					releaseMediaPlayer();
//					doCleanUp();
//				}
//
//				mMediaPlayer = new MediaPlayer(this);
//				
//				mMediaPlayer.setDataSource(this, Uri.parse(video_url));
//				mMediaPlayer.setDisplay(holder);
//				mMediaPlayer.prepareAsync();
//				mMediaPlayer.setOnBufferingUpdateListener(this);
//				mMediaPlayer.setOnCompletionListener(this);
//				mMediaPlayer.setOnPreparedListener(this);
//				mMediaPlayer.setOnVideoSizeChangedListener(this);
//				mMediaPlayer.setOnErrorListener(this);
//				mMediaPlayer.setOnInfoListener(this);
//				
//				setVolumeControlStream(AudioManager.STREAM_MUSIC);
//
//			} catch (Exception e) {
//				Log.e(TAG, "error: " + e.getMessage(), e);
//			}
//		}
//	}
//	
//	private void shareVideo() {
//		String items[] = {"Share via Email", "Share via Facbook"};
//		new AlertDialog.Builder(this)
//        .setSingleChoiceItems(items, 0, null)
//        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                dialog.dismiss();
//                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
//                // Do something useful withe the position of the selected radio button
//                if (selectedPosition == 0) {
//                	shareVideoViaEmail();
//                } else if (selectedPosition == 1) {
//                	shareVideoViaFacebook();
//                }
//            }
//        })
//        .show();
//	}
//	
//	private void shareVideoViaEmail() {
//		String strbody = "Good Video\n\n"
//				+ "**********************\n\n" 
//				+ "Title: " + AppCommonInfo.VideoList.get(current_video_index).title + "\n\n"
//				+ "Url: " + AppCommonInfo.VideoList.get(current_video_index).uri + "\n\n"
//				+ "**********************\n\n"
//				+ "Please Enjoy. :)";
//
//		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//		intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {""});
//		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share video of Blast");
//		intent.putExtra(android.content.Intent.EXTRA_TEXT, strbody);
//		intent.setType("html/text");
//
//		startActivity(Intent.createChooser(intent, "Send mail..."));
//	}
//	
//	private void shareVideoViaFacebook() {
//		
//		FacebookDialog shareDlg = null;
//		try {
//			shareDlg = new FacebookDialog.ShareDialogBuilder(this)
//			.setName("Blast Video")
//			.setCaption("There is good video here, please enjoy.")
//			.setPicture(AppCommonInfo.VideoList.get(current_video_index).thumbnail_img)
//			.setDescription("Title: " + AppCommonInfo.VideoList.get(current_video_index).title + "</br>"
//					+ "Description: " + AppCommonInfo.VideoList.get(current_video_index).description + "</br>"
//					+ "Url: " + AppCommonInfo.VideoList.get(current_video_index).uri)
//			.setLink(ServerConfig.HOST)
//			.build();
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			Toast.makeText(this, "It seems the Facebook App is not installed. Please install Facebook App First for Sharing.", Toast.LENGTH_LONG).show();
//		}
//		
//		if (shareDlg != null) {
//			uiHelper.trackPendingDialogCall(shareDlg.present());
//		}
//	}
//
//	private void showFullscreen() {
//		Animation slide_out_up = AnimationUtils.loadAnimation(this, R.anim.slide_out_up);
//		layout_title.startAnimation(slide_out_up);
//
//		Animation slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
//		layout_left.startAnimation(slide_out_left);
//
//		Animation slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
//		lst_video.startAnimation(slide_out_right);
//
//		Animation slide_out_down = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
//		layout_control.startAnimation(slide_out_down);
//
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				layout_title.setVisibility(View.GONE);
//				layout_left.setVisibility(View.GONE);
//				lst_video.setVisibility(View.GONE);
//				layout_control.setVisibility(View.GONE);
//			}
//		}, getResources().getInteger(R.integer.slide_animation_time));
//
//		// set fullscreen mode flag
//		mFullscreenMode = true;
//
//		// play video
//		playNewVideo();
//	}
//
//	private void doVoteDown() {
//		forwardPlay();
//	}
//
//	private void doVoteUp() {
//		mFavDatabase = new FavoriteDatabase(this);
//		mFavDatabase.open();
//
//		Cursor cur = mFavDatabase.getSameURL(AppCommonInfo.VideoList.get(current_video_index).uri);
//		if (cur != null && cur.getCount() > 0) {
//			Toast.makeText(this, "Already in Fav List", Toast.LENGTH_LONG).show();
//
//		} else {
//			VideoModel.DetailInfo item = AppCommonInfo.VideoList.get(current_video_index);
//			long dd = mFavDatabase.InsertOne(
//					item.title,
//					item.description, 
//					item.category_name, 
//					item.thumbnail_img,
//					item.uri,
//					item.vote_up_count,
//					item.vote_down_count
//					);
//
//			if (dd > 0) {
//				Toast.makeText(this, "Current video added as Favorite", Toast.LENGTH_LONG).show();
//			}
//		}
//	}
//
//	private void showNormalScreen() {
//		Animation slide_in_down = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
//		layout_title.startAnimation(slide_in_down);
//
//		Animation slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
//		layout_left.startAnimation(slide_in_right);
//
//		Animation slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
//		lst_video.startAnimation(slide_in_left);
//
//		Animation slide_in_up = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
//		layout_control.startAnimation(slide_in_up);
//
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				layout_title.setVisibility(View.VISIBLE);
//				layout_left.setVisibility(View.VISIBLE);
//				lst_video.setVisibility(View.VISIBLE);
//				layout_control.setVisibility(View.VISIBLE);
//			}
//		}, getResources().getInteger(R.integer.slide_animation_time));
//
//		// set fullscreen mode flag
//		mFullscreenMode = false;
//
//		// play video
//		playNewVideo();
//	}
//
//	private void backwardPlay() {
//		current_video_index--;
//		playNewVideo();
//	}
//
//	private void forwardPlay() {
//		current_video_index++;
//		playNewVideo();
//	}
//
//	private final Runnable updatePositionRunnable = new Runnable() {
//		public void run() {
//			updatePosition();
//		}
//	};
//
//	private void updatePosition() {
//		if (updatePositionRunnable == null || mUpdateProgresshandler == null)
//			return;
//
//		if (!mIsVideoReadyToBePlayed || !mIsVideoSizeKnown)
//			return;
//
//		mUpdateProgresshandler.removeCallbacks(updatePositionRunnable);
//
//		if (mMediaPlayer != null) {
//			int duration = (int)mMediaPlayer.getDuration();
//			int position = (int)mMediaPlayer.getCurrentPosition();
//
//			mProgress.setProgress(position);
//
//			if (txt_end_time != null)
//				txt_end_time.setText(stringForTime(duration));
//			if (txt_current_time != null)
//				txt_current_time.setText(stringForTime(position));
//		}
//
//		mUpdateProgresshandler.postDelayed(updatePositionRunnable, 500);
//	}
//
//	@Override
//	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//		// TODO Auto-generated method stub
//		if (fromUser) {
//			mMediaPlayer.seekTo(progress);
//		}
//	}
//
//	@Override
//	public void onStartTrackingTouch(SeekBar seekBar) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onStopTrackingTouch(SeekBar seekBar) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void getVideoInfoFromServer_for_test() {
//		// temp data source code
//		VideoModel.DetailInfo item0 = new VideoModel.DetailInfo();
//		item0.id = "3";
//		item0.title = "Good TV Channel 1";
//		item0.description = "d - Good TV Channel 1";
//		item0.thumbnail_img = "https://photos-1.dropbox.com/t/0/AAAgSFoqakFzPFCXvxicxidNK4D5x3TnPkOPdPZvD-orNw/12/245471872/png/1024x768/2/_/0/2/image01.png/EARprzPO2UeSWR3Y76l0xugF1M7CtlCtlbE8ef0X0aw";
//		//item0.uri = "http://live1.goodtv.org/osmflivech1.m3u8";
//		item0.uri = "https://dl.dropboxusercontent.com/s/4ueon4mlnkofu1w/01.mp4";
//		item0.vote_up_count = "2566";
//		item0.vote_down_count = "176";
//
//		VideoModel.DetailInfo item1 = new VideoModel.DetailInfo();
//		item1.id = "0";
//		item1.title = "ETV";
//		item1.description = "d - ETV";
//		item1.thumbnail_img = "https://photos-5.dropbox.com/t/0/AAC3nC7vZrxtH8-7luFo_ydk3T75MO6UO19ise3oRaE0eg/12/245471872/png/1024x768/2/_/0/2/face.png/pNCgLKHL14Q1NIV9uadpyL4hVsVJTg1mYzfXwUZh3ms";
//		//item1.uri = "rtmp://213.55.98.102/live/livestream";
//		item1.uri = "https://dl.dropboxusercontent.com/s/8keqr0p9p9j7m0h/02.mp4";
//		item1.vote_up_count = "30002";
//		item1.vote_down_count = "236";
//
//		VideoModel.DetailInfo item2 = new VideoModel.DetailInfo();
//		item2.id = "2";
//		item2.title = "Taiwan Television (TTV)";
//		item2.description = "d - Taiwan Television (TTV)";
//		item2.thumbnail_img = "https://photos-1.dropbox.com/t/0/AABDayo3Eq5GwpVKQQTK6d5hQHnz3b_sMcO2rnHRtIggyQ/12/245471872/png/1024x768/2/_/0/2/helpbg.png/LrlPSphf9jTzZ74jJfPWKmS4EGWM0Un7oNeBbfnfLYU";
//		//item2.uri = "rtmp://www.gggg-box.com.cn/live/CH08";
//		item2.uri = "https://dl.dropboxusercontent.com/s/7bdp4u10bntsfbm/03.mp4";
//		item2.vote_up_count = "150";
//		item2.vote_down_count = "23678";
//
//		VideoModel.DetailInfo item3 = new VideoModel.DetailInfo();
//		item3.id = "1";
//		item3.title = "Kanal 0";
//		item3.description = "d - Kanal 0";
//		item3.thumbnail_img = "https://photos-6.dropbox.com/t/0/AADpgBsvJqTjK0uUShW89AnHZa2-85rjSSaU67QD9O0kYg/12/245471872/png/1024x768/2/_/0/2/help.png/dTr7ua22Srz-Lxo9kKv6VlO2805Q_U3452XRfM2jz-Y";
//		//item3.uri = "mms://stream.kompasbg.com:8081";
//		item3.uri = "https://dl.dropboxusercontent.com/s/17a95kcivp98ule/04.flv";
//		item3.vote_up_count = "317";
//		item3.vote_down_count = "46667";
//
//		VideoModel.DetailInfo item4 = new VideoModel.DetailInfo();
//		item4.id = "4";
//		item4.title = "Good TV Channel 2";
//		item4.description = "d - Good TV Channel 2";
//		item4.thumbnail_img = "https://photos-1.dropbox.com/t/0/AAD3E19vZrWz5jyv4EZcXnx8H7nzBBSVAQxrfl6ICz_1mw/12/245471872/png/1024x768/2/_/0/2/image02.png/6II7rN76gBx5UtzR-OvqkZxQzRfxaiQ0hXOuQ07WrWs";
//		//item4.uri = "http://live4.goodtv.org/osmflivech4.m3u8";
//		item4.uri = "https://dl.dropboxusercontent.com/s/rlqxw04jjbvmq4s/05.flv";
//		item4.vote_up_count = "27272";
//		item4.vote_down_count = "109";
//
//		VideoModel.DetailInfo item5 = new VideoModel.DetailInfo();
//		item5.id = "5";
//		item5.title = "WTB TV";
//		item5.description = "d - WTB TV";
//		item5.thumbnail_img = "https://photos-5.dropbox.com/t/0/AABEppfTNb0i3EWkqOXfYjQR_zOhBfHHq9SI2bu90e7gQw/12/245471872/png/1024x768/2/_/0/2/image03.png/pQXKVeWxDoYFyc9Nnft-Z4stk7bTC5k8w92Zkci8e4E";
//		//item5.uri = "mms://channel.nbtv.tw/buddha2oo";
//		item5.uri = "https:/dl.dropboxusercontent.com/s/enm9z2vflqpkoo8/06.flv";
//		item5.vote_up_count = "5595";
//		item5.vote_down_count = "29";
//
//		VideoModel.DetailInfo item6 = new VideoModel.DetailInfo();
//		item6.id = "6";
//		item6.title = "ViVa TV";
//		item6.description = "d - ViVa TV";
//		item6.thumbnail_img = "https://photos-6.dropbox.com/t/0/AAApzj9Tdk4NQpkY8wm9jSaqX7npkJ69zhq2EMSUdvB7ew/12/245471872/png/1024x768/2/_/0/2/image04.png/p_g-sU-uhXjT1YoWUss1Jvpgvf0dCM-m31UwxvJBYMQ";
//		//item6.uri = "mms://mediacenter.vivatv.com.tw/vivatv";
//		item6.uri = "https://dl.dropboxusercontent.com/s/sq4wnyhp5au5b6v/07.flv";
//		item6.vote_up_count = "70";
//		item6.vote_down_count = "11";
//
//		VideoModel.DetailInfo item7 = new VideoModel.DetailInfo();
//		item7.id = "7";
//		item7.title = "UCTV";
//		item7.description = "d - UCTV";
//		item7.thumbnail_img = "https://photos-2.dropbox.com/t/0/AAAPv6C_Q30OUm-zAMotmE0ce0MA_K5y2-J0GaZA0g8UtQ/12/245471872/png/1024x768/2/_/0/2/image05.png/5FsOqYiaxwSZnfPzW2GoDxtfipwmgxqgx03mWSVVjn8";
//		//item7.uri = "rtmp://124.219.69.161:1935/live/livestream";
//		item7.uri = "https://dl.dropboxusercontent.com/s/88y8myt0ln3owvm/08.flv";
//		item7.vote_up_count = "124";
//		item7.vote_down_count = "35";
//
//		VideoModel.DetailInfo item8 = new VideoModel.DetailInfo();
//		item8.id = "8";
//		item8.title = "India TV";
//		item8.description = "d - India TV";
//		item8.thumbnail_img = "https://photos-6.dropbox.com/t/0/AACe0568LNuyL1Fn1YJ3zyVtDxpQdo4ORLIeD4LvsYiCIQ/12/245471872/png/1024x768/2/_/0/2/musicplus_back.png/W0g23jILW5vqsrKrStK8gTJyfYk1QF3NFUuj-4X2fhc";
//		//item8.uri = "rtmp://cdn3.scieron.com/live/nd24tv.flv";
//		item8.uri = "https://dl.dropboxusercontent.com/s/q4i6h548c3836i0/09.flv";
//		item8.vote_up_count = "945";
//		item8.vote_down_count = "64";
//
//		VideoModel.DetailInfo item9 = new VideoModel.DetailInfo();
//		item9.id = "9";
//		item9.title = "TV9 News (English)";
//		item9.description = "d - TV9 News (English)";
//		item9.thumbnail_img = "https://photos-4.dropbox.com/t/0/AABg0WYV3cgDP6VVVIQdMbbCKkF9MGRfCTuWH3tr2DMI8Q/12/245471872/png/1024x768/2/_/0/2/radio21_back.png/L9Jmbwac0izaBoUTCfoewYEADVWhkOEjo1teqbF3ji4";
//		//item9.uri = "http://mayatv.in:1935/news9/_definst_/livestream3/playlist.m3u8?wowzasessionid=1065943465";
//		item9.uri = "https://dl.dropboxusercontent.com/s/z1v2te2brvi7uaa/10.flv";
//		item9.vote_up_count = "2145";
//		item9.vote_down_count = "2014";
//
//		AppCommonInfo.VideoList.clear();
//
//		AppCommonInfo.VideoList.add(item0);
//		AppCommonInfo.VideoList.add(item1);
//		AppCommonInfo.VideoList.add(item2);
//		AppCommonInfo.VideoList.add(item3);
//		AppCommonInfo.VideoList.add(item4);
//		AppCommonInfo.VideoList.add(item5);
//		AppCommonInfo.VideoList.add(item6);
//		AppCommonInfo.VideoList.add(item7);
//		AppCommonInfo.VideoList.add(item8);
//		AppCommonInfo.VideoList.add(item9);
//
//		initControllerView();
//		setEnableUI(false);
//		doCleanUp();
//	}
//
//	private void getVideoInfoFromServer() {
//		if (TextUtils.isEmpty(mChannelID))
//			return;
//
//		showProgressDialog("Getting Video List by Channel, Please wait...");
//
//		BaseTask.run(new TaskListener() {
//			@Override
//			public Object onTaskRunning(int taskId, Object data) {
//				Object result = Server.GetVideoListByChannel(mChannelID);
//
//				if (result instanceof VideoModel.Result) {
//					VideoModel.Result info_list = (VideoModel.Result) result;
//					saveToGlovalVideoList(info_list.result);
//					return null;
//				}
//				
//				return ((String) result);
//			}
//
//			@Override
//			public void onTaskResult(int taskId, Object result) {
//				// Dismiss the progress dialog
//				hideProgressDialog();
//
//				if (result == null) {
//					Toast.makeText(myMediaPlayer1.this, "Get Video List Success", Toast.LENGTH_LONG).show();
//					
//					if (AppCommonInfo.VideoList.size() > 0) {
//						GetVoteInfoFromServer(AppCommonInfo.VideoList.get(0).id);
//					}
//
//					initControllerView();
//					setEnableUI(false);
//					doCleanUp();
//
//				} else {
//					Toast.makeText(myMediaPlayer1.this, "Get Video List Fail: " + (String)result, Toast.LENGTH_LONG).show();
//				}
//			}
//
//			@Override
//			public void onTaskProgress(int taskId, Object... values) {
//			}
//
//			@Override
//			public void onTaskPrepare(int taskId, Object data) {
//			}
//
//			@Override
//			public void onTaskCancelled(int taskId) {
//			}
//		});
//	}
//	
//	private void GetVoteInfoFromServer(final String video_id) {
//		if (TextUtils.isEmpty(video_id))
//			return;
//
//		showProgressDialog("Please wait. Get Vote Status of Video...");
//		
//		BaseTask.run(new TaskListener() {
//			@Override
//			public Object onTaskRunning(int taskId, Object data) {
//				Object result = Server.GetVoteStatusOfVideo(video_id);
//
//				if (result instanceof VideoModel.Result) {
//					VideoModel.Result info_list = (VideoModel.Result) result;
//
////					if (info_list.status == ServerConfig.STATUS_SUCCESS) {
////						return null;
////
////					} else {
////						return info_list.result;
////					}
//
//				} else if (result instanceof String) {
//					return ErrorModel.GetErrorString((String)result);
//				}
//
//				return ((String) result);
//			}
//
//			@Override
//			public void onTaskResult(int taskId, Object result) {
//				// hide the progress dialog
//				hideProgressDialog();
//
//				if (result == null) {
//					Toast.makeText(myMediaPlayer1.this, "Get Vote Status Success", Toast.LENGTH_LONG).show();
//				} else {
//					Toast.makeText(myMediaPlayer1.this, "Get Vote Status Fail: " + (String)result, Toast.LENGTH_LONG).show();
//				}
//			}
//
//			@Override
//			public void onTaskProgress(int taskId, Object... values) {
//			}
//
//			@Override
//			public void onTaskPrepare(int taskId, Object data) {
//			}
//
//			@Override
//			public void onTaskCancelled(int taskId) {
//			}
//		});
//	}
//
//	/*
//	 * Save video infomation to App Common Video information list
//	 */
//	private void saveToGlovalVideoList(ArrayList<VideoModel.Info> info_list) {
//		AppCommonInfo.VideoList.clear();
//
//		for (int i = 0; i < info_list.size(); i++) {
//			VideoModel.Info item = info_list.get(i);
//
//			VideoModel.DetailInfo detail_item = new VideoModel.DetailInfo();
//			detail_item.id = item.nid;
//			detail_item.category_name = item.Channel;
//			detail_item.title = item.node_title;
//			detail_item.description = item.Description;
//			detail_item.thumbnail_img = "";
//			detail_item.vote_up_count = "";
//			detail_item.vote_down_count = "";
//			
//			if (mYoutubePlayerInitialized) {
//				detail_item.uri = item.Video;
//			} else  {
//				if (YoutubeUtil.isYoutubeURL(item.Video)) {
//					String video_id = YoutubeUtil.parseYoutubeVideoId(item.Video);
//					RMYoutubeExtractor.video_url_map.clear();
//					try {
//						RMYoutubeExtractor.extractVideobyId(video_id);
//					} catch (MalformedURLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					if (RMYoutubeExtractor.video_url_map.containsKey(RMYoutubeExtractor.VideoQuality.Small240)) {
//						URL url = (URL)RMYoutubeExtractor.video_url_map.get(RMYoutubeExtractor.VideoQuality.Small240);
//						detail_item.uri = url.toString();
//					} else if (RMYoutubeExtractor.video_url_map.containsKey(RMYoutubeExtractor.VideoQuality.Medium360)) {
//						URL url = (URL)RMYoutubeExtractor.video_url_map.get(RMYoutubeExtractor.VideoQuality.Medium360);
//						detail_item.uri = url.toString();
//					} else if (RMYoutubeExtractor.video_url_map.containsKey(RMYoutubeExtractor.VideoQuality.HD720)) {
//						URL url = (URL)RMYoutubeExtractor.video_url_map.get(RMYoutubeExtractor.VideoQuality.HD720);
//						detail_item.uri = url.toString();
//					} else if (RMYoutubeExtractor.video_url_map.containsKey(RMYoutubeExtractor.VideoQuality.HD1080)) {
//						URL url = (URL)RMYoutubeExtractor.video_url_map.get(RMYoutubeExtractor.VideoQuality.HD1080);
//						detail_item.uri = url.toString();
//					}
//					
//					detail_item.uri = "http://r14---sn-ogueynes.googlevideo.com/videoplayback?key=yt5&signature=C88AC081667FA39E89F2CF0B41E45255BE50AFC2.59C5497FF07D8D0EBFE91796002F5872F77055AE&source=youtube&expire=1402444969&id=o-AB857uqa94BlLRt95oo_U-koCVGKeEcMtYQyPPE2ZCL3&mv=m&ms=au&ipbits=0&itag=36&upn=8nJ91Yt2SV0&sparams=id%2Cip%2Cipbits%2Citag%2Csource%2Cupn%2Cexpire&fexp=908571%2C913434%2C916612%2C917000%2C919815%2C923341%2C924613%2C930008%2C931975%2C932617%2C934026&mws=yes&sver=3&ip=106.186.123.78&mt=1402420669";
//					//[1]	(null)	(long)22 : @"http://r14---sn-ogueynes.googlevideo.com/videoplayback?key=yt5&signature=BA09E943E228C6C3EE6D6313784D48DC77CC8018.A8FAFFBBC82ECC849E8D0AA112F5EBC739A697A7&ratebypass=yes&source=youtube&expire=1402444969&id=o-AB857uqa94BlLRt95oo_U-koCVGKeEcMtYQyPPE2ZCL3&mv=m&ms=au&ipbits=0&itag=22&upn=8nJ91Yt2SV0&sparams=id%2Cip%2Cipbits%2Citag%2Cratebypass%2Csource%2Cupn%2Cexpire&fexp=908571%2C913434%2C916612%2C917000%2C919815%2C923341%2C924613%2C930008%2C931975%2C932617%2C934026&mws=yes&sver=3&ip=106.186.123.78&mt=1402420669"
//					//[2]	(null)	(long)18 : @"http://r14---sn-ogueynes.googlevideo.com/videoplayback?key=yt5&signature=CC45D39C828BFC58838B4A1A6CA3097DCD43145F.448B6E933781CEEC74245FEDDEF13B0EEE592726&ratebypass=yes&source=youtube&expire=1402444969&id=o-AB857uqa94BlLRt95oo_U-koCVGKeEcMtYQyPPE2ZCL3&mv=m&ms=au&ipbits=0&itag=18&upn=8nJ91Yt2SV0&sparams=id%2Cip%2Cipbits%2Citag%2Cratebypass%2Csource%2Cupn%2Cexpire&fexp=908571%2C913434%2C916612%2C917000%2C919815%2C923341%2C924613%2C930008%2C931975%2C932617%2C934026&mws=yes&sver=3&ip=106.186.123.78&mt=1402420669"	
//					
//				
//				} else {
//					detail_item.uri = item.Video;
//				}
//			}
//
//			AppCommonInfo.VideoList.add(detail_item);
//		}
//	}
//
//	public class LazyAdapter extends BaseAdapter {
//		Activity act;
//		ArrayList<VideoModel.DetailInfo> mArrayList;
//		String mitem[];
//		private LayoutInflater inflater = null;
//
//		public LazyAdapter(Activity a, ArrayList<VideoModel.DetailInfo> category) {
//			act = a;
//			if (category == null) {
//				this.mArrayList = new ArrayList<VideoModel.DetailInfo>();
//			} else {
//				this.mArrayList = category;
//			}
//
//			inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//
//		public int getCount() {
//			return mArrayList.size();
//		}
//
//		public Object getItem(int position) {
//			return position;
//		}
//
//		public long getItemId(int position) {
//			return position;
//		}
//
//		class ViewHolder {
//			public RelativeLayout item_layout;
//			public ImageView img_thumbnail;
//			public ImageButton btn_play;
//		}
//
//		public View getView(final int position, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = inflater.inflate(R.layout.row_video_vertical, null);
//				holder = new ViewHolder();
//				holder.item_layout = (RelativeLayout) convertView.findViewById(R.id.item_layout);
//				holder.img_thumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);
//				holder.btn_play = (ImageButton) convertView.findViewById(R.id.btn_play);
//				convertView.setTag(holder);
//
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			try {
//				ViewGroup.LayoutParams params = holder.item_layout.getLayoutParams();
//				params.height = (int)(Constants.SCREEN_WIDTH * 0.12);
//
//				myImageLoader.showImage(holder.img_thumbnail, mArrayList.get(position).thumbnail_img);
//				holder.btn_play.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						current_video_index = position;
//						playNewVideo();
//					}
//				});
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return convertView;
//		}
//	}
//
//	private void hideProgressDialog() {
//		if (pDialog == null)
//			return;
//
//		pDialog.dismiss();
//	}
//
//	private void showProgressDialog(String message) {
//		if (pDialog == null) {
//			pDialog = new ProgressDialog(this);
//		}
//
//		pDialog.setMessage(message);
//		pDialog.setCancelable(true);
//		pDialog.show();
//	}
//
//	@Override
//	public void start() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void pause() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public int getDuration() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int getCurrentPosition() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void seekTo(int pos) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public boolean isPlaying() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public int getBufferPercentage() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public boolean canPause() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean canSeekBackward() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean canSeekForward() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isFullScreen() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void showFullScreen() {
//		// TODO Auto-generated method stub
//		showNormalScreen();
//	}
//	
//	private void myOnbackPressed() {
//		if (mFullscreenMode) {
//			showNormalScreen();
//
//		} else {
//			new AlertDialog.Builder(this)
//			.setTitle("Hi")
//			.setIcon(android.R.drawable.ic_dialog_info)
//			.setMessage("Are you stop to play video?")
//			.setPositiveButton("Yes, Stop", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					myMediaPlayer1.super.onBackPressed();
//				}
//			})
//			.setNegativeButton("No, Continue", null)
//			.show();
//		}
//	}
//
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		myOnbackPressed();
//	}
//
//	@Override
//	public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
//		// TODO Auto-generated method stub
//		mYoutubePlayerInitialized = false;
//		Toast.makeText(this, "Youtube Player Initialize Fail", Toast.LENGTH_LONG).show();
//		
//		
//		getVideoInfoFromServer();
//	}
//
//	@Override
//	public void onInitializationSuccess(Provider arg0, YouTubePlayer player, boolean arg2) {
//		// TODO Auto-generated method stub
//		mYoutubePlayerInitialized = false;
//		Toast.makeText(this, "Youtube Player Initialize Success", Toast.LENGTH_LONG).show();
//
//		mYoutubePlayer = player;
//		mYoutubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
//		
//		getVideoInfoFromServer();
//	}
//}
