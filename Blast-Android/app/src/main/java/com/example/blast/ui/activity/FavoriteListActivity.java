package com.example.blast.ui.activity;

import java.util.ArrayList;

import com.example.blast.utils.BaseTask;
import com.example.blast.Constants;
import com.example.blast.R;
import com.example.blast.model.ChannelModel;
import com.example.blast.model.FavoriteModel;
import com.example.blast.utils.myImageLoader;
import com.example.blast.utils.FontUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FavoriteListActivity extends Activity {

	// for data
	private ArrayList<FavoriteModel> mVideoList = new ArrayList<FavoriteModel>();
	
	// for UI
	private View btn_back;
	private ImageView btn_channel;
	private boolean channel_audio_flag = true;
	private ListView lst_video;
	private View btn_refresh;
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		
		btn_back = findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FavoriteListActivity.this.finish();
			}
		});
		
		lst_video = (ListView) findViewById(R.id.lst_video);
		lst_video.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int position, long id) {
				// TODO Auto-generated method stub
//				if (position >= 0 && position < mVideoList.size()) {
//					ChannelModel.Info item = mVideoList.get(position);
//					Intent intent = new Intent(FavoriteListActivity.this, VitamioMediaPlayer.class);
//					intent.putExtra("flag", "favorite_list");
//					intent.putExtra("tid", item.tid);
//					intent.putExtra("vid", item.vid);
//					intent.putExtra("name", item.name);
//					intent.putExtra("format", item.format);
//					intent.putExtra("weight", item.weight);
//					intent.putExtra("uri", item.uri);
//					
//					startActivity(intent);
//				}
			}
		});
		
		btn_refresh = findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FavoriteListActivity.this.getChannelListFromServer();
			}
		});
		
		pDialog = new ProgressDialog(FavoriteListActivity.this);
		pDialog.setMessage("Getting Channel List, Please wait...");
		pDialog.setCancelable(true);
		pDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (mVideoList == null || mVideoList.size() == 0) {
					// show refresh button
					lst_video.setVisibility(View.GONE);
					btn_refresh.setVisibility(View.VISIBLE);
					
					if (BaseTask.instance != null) {
						BaseTask.instance.cancel(true);
						BaseTask.instance = null;
					}
				}
			}
		});
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getChannelListFromServer();
				//getVideoListFromServer();
			}
		}, 1000);
	}

	@SuppressLint("DefaultLocale")
	private void getChannelListFromServer() {
//		pDialog.show();
//		BaseTask.run(new TaskListener() {
//			@Override
//			public Object onTaskRunning(int taskId, Object data) {
//				Object result = Server.GetChannelList();
//
//				if (result instanceof ChannelModel.InfoList) {
//					ChannelModel.InfoList info_list = (ChannelModel.InfoList) result;
//
//					if (info_list.status == ServerConfig.STATUS_SUCCESS) {
//						mVideoList = info_list.result;
//						
//						return null;
//
//					} else {
//						return info_list.result;
//					}
//				}
//
//				return ((String) result);
//			}
//
//			@Override
//			public void onTaskResult(int taskId, Object result) {
//				// Dismiss the progress dialog
//				if (pDialog.isShowing())
//					pDialog.dismiss();
//
//				if (result == null) {
//					if (mVideoList == null || mVideoList.size() == 0) {
//						// show refresh button
//						lst_video.setVisibility(View.GONE);
//						btn_refresh.setVisibility(View.VISIBLE);
//						
//					} else {
//						// hide refresh button and show list
//						lst_video.setVisibility(View.VISIBLE);
//						btn_refresh.setVisibility(View.GONE);
//						
//						LazyAdapter adapter = new LazyAdapter(FavoriteListActivity.this, mVideoList);
//						lst_video.setAdapter(adapter);
//						adapter.notifyDataSetChanged();
//					}
//
//				} else {
//					// show refresh button
//					lst_video.setVisibility(View.GONE);
//					btn_refresh.setVisibility(View.VISIBLE);
//
//					String errStr = (String) result;
//					Toast.makeText(FavoriteListActivity.this, errStr, Toast.LENGTH_LONG).show();
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
	}
	
	public class LazyAdapter extends BaseAdapter {
		Activity act;
		ArrayList<ChannelModel.Info> mArrayList;
		String mitem[];
		private LayoutInflater inflater = null;

		public LazyAdapter(Activity a, ArrayList<ChannelModel.Info> category) {
			act = a;
			if (category == null) {
				this.mArrayList = new ArrayList<ChannelModel.Info>();
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
			public ImageView img_channel;
			public TextView txt_channel_name;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_category, null);
				holder = new ViewHolder();
				holder.item_layout = (RelativeLayout) convertView.findViewById(R.id.item_layout);
				holder.img_channel = (ImageView) convertView.findViewById(R.id.img_channel);
				holder.txt_channel_name = (TextView) convertView.findViewById(R.id.txt_channel_name);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			try {
				ViewGroup.LayoutParams params = holder.item_layout.getLayoutParams();
				params.height = (int)(Constants.SCREEN_WIDTH * 0.5);
				
				myImageLoader.showImage(holder.img_channel, "");
				holder.txt_channel_name.setText(mArrayList.get(position).name);
				holder.txt_channel_name.setTypeface(FontUtil.GetDeJaFont(FavoriteListActivity.this));

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("Hi !!!")
		.setMessage("Do you want exit?")
		.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				FavoriteListActivity.super.onBackPressed();
			}
		})
		.setNegativeButton("Cancel", null)
		.show();
	}
}
