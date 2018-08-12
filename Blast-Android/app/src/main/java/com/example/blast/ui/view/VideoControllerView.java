/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.blast.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.example.blast.AppConstants;
import com.example.blast.AppGlobals;
import com.example.blast.R;
import com.example.blast.model.VideoModel;
import com.example.blast.ui.activity.myMediaPlayer;
import com.example.blast.utils.myImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

public class VideoControllerView extends FrameLayout {
	private static final String TAG = "VideoControllerView";

	public MediaPlayerControl  mController;
	private Context             mContext;
	private ViewGroup           mAnchor;
	private View                mRoot;

	private ImageView			btn_fullscreen;
	private ImageView			btn_backward;
	private ImageView			btn_play;
	private ImageView			btn_forward;
	private ProgressBar         mProgress;
	private TextView            txt_current_time, txt_end_time;
	private boolean             mShowing;
	private boolean             mDragging;
	private static final int    sDefaultTimeout = 8000;
	private static final int    FADE_OUT = 1;
	private static final int    SHOW_PROGRESS = 2;
	private OnClickListener mNextListener, mPrevListener;
	StringBuilder               mFormatBuilder;
	Formatter                   mFormatter;
	//    private ImageButton         mFfwdButton;
	//    private ImageButton         mRewButton;

	private Handler             mHandler = new MessageHandler(this);

	public VideoControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRoot = null;
		mContext = context;

		Log.i(TAG, TAG);
	}

	public VideoControllerView(Context context) {
		super(context);
		mContext = context;

		Log.i(TAG, TAG);
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		if (mRoot != null)
			initControllerView(mRoot);
	}

	public void setMediaPlayer(MediaPlayerControl controller) {
		mController = controller;
		//updatePausePlay();
	}

	/**
	 * Set the view that acts as the anchor for the control view.
	 * This can for example be a VideoView, or your Activity's main view.
	 * @param view The view to which to anchor the controller when it is visible.
	 */
	public void setAnchorView(ViewGroup view) {
		mAnchor = view;

		LayoutParams frameParams = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT
				);

		removeAllViews();
		View v = makeControllerView();
		addView(v, frameParams);
	}

	/**
	 * Create the view that holds the widgets that control playback.
	 * Derived classes can override this to create their own.
	 * @return The controller view.
	 * @hide This doesn't work as advertised
	 */
	protected View makeControllerView() {
		LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRoot = inflate.inflate(R.layout.media_controller, null);

		initControllerView(mRoot);

		return mRoot;
	}

	private void initControllerView(View v) {
		btn_fullscreen = (ImageView) v.findViewById(R.id.btn_fullscreen);
		btn_fullscreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doToggleFullscreen();
				show(sDefaultTimeout);
			}
		});

		btn_play = (ImageView) v.findViewById(R.id.btn_play);
		if (btn_play != null) {
			btn_play.requestFocus();
			btn_play.setOnClickListener(mPauseListener);
		}

		//        mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
		//        if (mFfwdButton != null) {
		//            mFfwdButton.setOnClickListener(mFfwdListener);
		//            if (!mFromXml) {
		//                mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
		//            }
		//        }
		//
		//        mRewButton = (ImageButton) v.findViewById(R.id.rew);
		//        if (mRewButton != null) {
		//            mRewButton.setOnClickListener(mRewListener);
		//            if (!mFromXml) {
		//                mRewButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
		//            }
		//        }

		// By default these are hidden. They will be enabled when setPrevNextListeners() is called 
		btn_forward = (ImageView) v.findViewById(R.id.btn_forward);
		btn_backward = (ImageView) v.findViewById(R.id.btn_backward);

		mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_progress);
		if (mProgress != null) {
			if (mProgress instanceof SeekBar) {
				SeekBar seeker = (SeekBar) mProgress;
				seeker.setOnSeekBarChangeListener(mSeekListener);
			}
			mProgress.setMax(1000);
		}

		txt_current_time = (TextView) v.findViewById(R.id.txt_current_time);
		txt_end_time = (TextView) v.findViewById(R.id.txt_end_time);
		
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

		installPrevNextListeners();

		HorizontalListView lst_video = (HorizontalListView) v.findViewById(R.id.lst_video);
		ViewGroup.LayoutParams params = lst_video.getLayoutParams();
		params.height = (int) (AppConstants.SCREEN_WIDTH * 0.15);
		LazyAdapter mAdapter = new LazyAdapter(this.getContext(), AppGlobals.VideoList);
		lst_video.setAdapter(mAdapter);
	}

	/**
	 * Show the controller on screen. It will go away
	 * automatically after 3 seconds of inactivity.
	 */
	public void show() {
		show(sDefaultTimeout);
	}

	/**
	 * Disable pause or seek buttons if the stream cannot be paused or seeked.
	 * This requires the control interface to be a MediaPlayerControlExt
	 */
	private void disableUnsupportedButtons() {
		if (mController == null) {
			return;
		}

		try {
			if (btn_play != null && !mController.canPause()) {
				btn_play.setEnabled(false);
			}
			//            if (mRewButton != null && !mPlayer.canSeekBackward()) {
			//                mRewButton.setEnabled(false);
			//            }
			//            if (mFfwdButton != null && !mPlayer.canSeekForward()) {
			//                mFfwdButton.setEnabled(false);
			//            }
		} catch (IncompatibleClassChangeError ex) {
			// We were given an old version of the interface, that doesn't have
			// the canPause/canSeekXYZ methods. This is OK, it just means we
			// assume the media can be paused and seeked, and so we don't disable
			// the buttons.
		}
	}

	/**
	 * Show the controller on screen. It will go away
	 * automatically after 'timeout' milliseconds of inactivity.
	 * @param timeout The timeout in milliseconds. Use 0 to show
	 * the controller until hide() is called.
	 */
	public void show(int timeout) {
		if (!mShowing && mAnchor != null) {
			setProgress();
			if (btn_play != null) {
				btn_play.requestFocus();
			}
			//disableUnsupportedButtons();

			LayoutParams tlp = new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					Gravity.BOTTOM
					);

			mAnchor.addView(this, tlp);
			Animation slide_up = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_in_up);
			startAnimation(slide_up);
			mShowing = true;
		}
		//updatePausePlay();

		// cause the progress bar to be updated even if mShowing
		// was already true.  This happens, for example, if we're
		// paused with the progress bar showing the user hits play.
		mHandler.sendEmptyMessage(SHOW_PROGRESS);

		Message msg = mHandler.obtainMessage(FADE_OUT);
		if (timeout != 0) {
			mHandler.removeMessages(FADE_OUT);
			mHandler.sendMessageDelayed(msg, timeout);
		}
	}

	public boolean isShowing() {
		return mShowing;
	}

	/**
	 * Remove the controller from the screen.
	 */
	public void hide() {
		if (mAnchor == null) {
			return;
		}

		try {
			Animation slide_down = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_out_down);
			startAnimation(slide_down);

			mAnchor.removeView(this);
			mHandler.removeMessages(SHOW_PROGRESS);
			
		} catch (IllegalArgumentException ex) {
			Log.w("MediaController", "already removed");
		}
		
		mShowing = false;
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

	private int setProgress() {
		if (mController == null || mDragging) {
			return 0;
		}

		int position = mController.getCurrentPosition();
		int duration = mController.getDuration();
		if (mProgress != null) {
			if (duration > 0) {
				// use long to avoid overflow
				long pos = 1000L * position / duration;
				mProgress.setProgress( (int) pos);
			}
			int percent = mController.getBufferPercentage();
			mProgress.setSecondaryProgress(percent * 10);
		}

		if (txt_current_time != null)
			txt_current_time.setText(stringForTime(position));
		if (txt_end_time != null)
			txt_end_time.setText(stringForTime(duration));

		return position;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		show(sDefaultTimeout);
		return true;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		show(sDefaultTimeout);
		return false;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (mController == null) {
			return true;
		}

		int keyCode = event.getKeyCode();
		final boolean uniqueDown = event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN;
		if (keyCode ==  KeyEvent.KEYCODE_HEADSETHOOK
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
				|| keyCode == KeyEvent.KEYCODE_SPACE) {
			if (uniqueDown) {
				doPauseResume();
				show(sDefaultTimeout);
				if (btn_play != null) {
					btn_play.requestFocus();
				}
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
			if (uniqueDown && !mController.isPlaying()) {
				mController.start();
				updatePausePlay();
				show(sDefaultTimeout);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
				|| keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
			if (uniqueDown && mController.isPlaying()) {
				mController.pause();
				updatePausePlay();
				show(sDefaultTimeout);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
				|| keyCode == KeyEvent.KEYCODE_VOLUME_UP
				|| keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
			// don't show the controls for volume adjustment
			return super.dispatchKeyEvent(event);
		} else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
			if (uniqueDown) {
				hide();
			}
			return true;
		}

		show(sDefaultTimeout);
		return super.dispatchKeyEvent(event);
	}

	private OnClickListener mPauseListener = new OnClickListener() {
		public void onClick(View v) {
			doPauseResume();
			show(sDefaultTimeout);
		}
	};


	public void updatePausePlay() {
		if (mRoot == null || btn_play == null || mController == null) {
			return;
		}

		if (mController.isPlaying()) {
			btn_play.setImageResource(R.drawable.bg_btn_media_pause);
		} else {
			btn_play.setImageResource(R.drawable.bg_btn_media_play);
		}
	}

	private void doPauseResume() {
		if (mController == null) {
			return;
		}

		if (mController.isPlaying()) {
			mController.pause();
		} else {
			mController.start();
		}
		updatePausePlay();
	}

	private void doToggleFullscreen() {
		if (mController == null) {
			return;
		}

		mController.showFullScreen();
	}

	// There are two scenarios that can trigger the seekbar listener to trigger:
	//
	// The first is the user using the touchpad to adjust the posititon of the
	// seekbar's thumb. In this case onStartTrackingTouch is called followed by
	// a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
	// We're setting the field "mDragging" to true for the duration of the dragging
	// session to avoid jumps in the position in case of ongoing playback.
	//
	// The second scenario involves the user operating the scroll ball, in this
	// case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
	// we will simply apply the updated position without suspending regular updates.
	private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
		public void onStartTrackingTouch(SeekBar bar) {
			show(3600000);

			mDragging = true;

			// By removing these pending progress messages we make sure
			// that a) we won't update the progress while the user adjusts
			// the seekbar and b) once the user is done dragging the thumb
			// we will post one of these messages to the queue again and
			// this ensures that there will be exactly one message queued up.
			mHandler.removeMessages(SHOW_PROGRESS);
		}

		public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
			if (mController == null) {
				return;
			}

			if (!fromuser) {
				// We're not interested in programmatically generated changes to
				// the progress bar's position.
				return;
			}

			long duration = mController.getDuration();
			long newposition = (duration * progress) / 1000L;
			mController.seekTo( (int) newposition);
			if (txt_current_time != null)
				txt_current_time.setText(stringForTime( (int) newposition));
		}

		public void onStopTrackingTouch(SeekBar bar) {
			mDragging = false;
			setProgress();
			updatePausePlay();
			show(sDefaultTimeout);

			// Ensure that progress is properly updated in the future,
			// the call to show() does not guarantee this because it is a
			// no-op if we are already showing.
			mHandler.sendEmptyMessage(SHOW_PROGRESS);
		}
	};

	@Override
	public void setEnabled(boolean enabled) {
		if (btn_play != null) {
			btn_play.setEnabled(enabled);
		}
		//        if (mFfwdButton != null) {
		//            mFfwdButton.setEnabled(enabled);
		//        }
		//        if (mRewButton != null) {
		//            mRewButton.setEnabled(enabled);
		//        }
		if (btn_forward != null) {
			btn_forward.setEnabled(enabled && mNextListener != null);
		}
		if (btn_backward != null) {
			btn_backward.setEnabled(enabled && mPrevListener != null);
		}
		if (mProgress != null) {
			mProgress.setEnabled(enabled);
		}
		disableUnsupportedButtons();
		super.setEnabled(enabled);
	}

	//	private View.OnClickListener mRewListener = new View.OnClickListener() {
	//		public void onClick(View v) {
	//			if (mPlayer == null) {
	//				return;
	//			}
	//
	//			int pos = mPlayer.getCurrentPosition();
	//			pos -= 5000; // milliseconds
	//			mPlayer.seekTo(pos);
	//			setProgress();
	//
	//			show(sDefaultTimeout);
	//		}
	//	};
	//
	//	private View.OnClickListener mFfwdListener = new View.OnClickListener() {
	//		public void onClick(View v) {
	//			if (mPlayer == null) {
	//				return;
	//			}
	//
	//			int pos = mPlayer.getCurrentPosition();
	//			pos += 15000; // milliseconds
	//			mPlayer.seekTo(pos);
	//			setProgress();
	//
	//			show(sDefaultTimeout);
	//		}
	//	};

	private void installPrevNextListeners() {
		if (btn_forward != null) {
			btn_forward.setOnClickListener(mNextListener);
			btn_forward.setEnabled(mNextListener != null);
		}

		if (btn_backward != null) {
			btn_backward.setOnClickListener(mPrevListener);
			btn_backward.setEnabled(mPrevListener != null);
		}
	}

	public void setPrevNextListeners(OnClickListener next, OnClickListener prev) {
		mNextListener = next;
		mPrevListener = prev;

		if (mRoot != null) {
			installPrevNextListeners();

			if (btn_forward != null) {
				btn_forward.setVisibility(View.VISIBLE);
			}
			if (btn_backward != null) {
				btn_backward.setVisibility(View.VISIBLE);
			}
		}
	}

	public class LazyAdapter extends BaseAdapter {
		Context mContext;
		ArrayList<VideoModel.DetailInfo> mArrayList;
		String mitem[];
		private LayoutInflater inflater = null;

		public LazyAdapter(Context context, ArrayList<VideoModel.DetailInfo> category) {
			mContext = context;
			if (category == null) {
				this.mArrayList = new ArrayList<VideoModel.DetailInfo>();
			} else {
				this.mArrayList = category;
			}

			inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				convertView = inflater.inflate(R.layout.row_video_horizontal, null);
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
				params.width = (int)(AppConstants.SCREEN_HEIGHT * 0.15);

				myImageLoader.showImage(holder.img_thumbnail, mArrayList.get(position).thumbnail_img);
				holder.btn_play.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (myMediaPlayer.instance != null) {
							myMediaPlayer.instance.current_video_index = position;
							myMediaPlayer.instance.playNewVideo();
						}
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	public interface MediaPlayerControl {
		void    start();
		void    pause();
		int     getDuration();
		int     getCurrentPosition();
		void    seekTo(int pos);
		boolean isPlaying();
		int     getBufferPercentage();
		boolean canPause();
		boolean canSeekBackward();
		boolean canSeekForward();
		boolean isFullScreen();
		void    showFullScreen();
	}

	private static class MessageHandler extends Handler {
		private final WeakReference<VideoControllerView> mView; 

		MessageHandler(VideoControllerView view) {
			mView = new WeakReference<VideoControllerView>(view);
		}
		@Override
		public void handleMessage(Message msg) {
			VideoControllerView view = mView.get();
			if (view == null || view.mController == null) {
				return;
			}

			int pos;
			switch (msg.what) {
			case FADE_OUT:
				view.hide();
				break;
			case SHOW_PROGRESS:
				pos = view.setProgress();
				if (!view.mDragging && view.mShowing && view.mController.isPlaying()) {
					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000 - (pos % 1000));
				}
				break;
			}
		}
	}
}