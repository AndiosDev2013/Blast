package com.example.blast.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.blast.AppConstants;
import com.example.blast.AppGlobals;
import com.example.blast.AppPreferences;
import com.example.blast.R;
import com.example.blast.http.Server;
import com.example.blast.model.ChannelModel;
import com.example.blast.utils.BaseTask;
import com.example.blast.utils.BaseTask.TaskListener;
import com.example.blast.utils.FontUtil;
import com.example.blast.utils.myImageLoader;

import java.util.ArrayList;

public class MainActivity extends Activity {

	// for data
	private ArrayList<ChannelModel.Info> mChannelList = new ArrayList<>();

	// for UI
	private ImageView btn_channel;
	private boolean channel_audio_flag = true;
	private ListView lst_category;
	private View btn_refresh;
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.btn_setting).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});

		btn_channel = findViewById(R.id.btn_channel);
		btn_channel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				channel_audio_flag = !channel_audio_flag;
				if (channel_audio_flag) {
					btn_channel.setImageResource(R.drawable.btn_tog_on);
				} else {
					btn_channel.setImageResource(R.drawable.btn_tog_off);
				}
			}
		});
		
		TextView text_channel_audio = findViewById(R.id.text_channel_audio);
		text_channel_audio.setTypeface(FontUtil.GetDeJaFont(this));
		
		lst_category = findViewById(R.id.lst_category);
		lst_category.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int position, long id) {
				// TODO Auto-generated method stub
				if (position >= 0 && position < mChannelList.size()) {
					ChannelModel.Info item = mChannelList.get(position);
					Intent intent = new Intent(MainActivity.this, myMediaPlayer2.class);
					intent.putExtra("tid", item.tid);
					intent.putExtra("vid", item.vid);
					intent.putExtra("name", item.name);
					intent.putExtra("format", item.format);
					intent.putExtra("weight", item.weight);
					intent.putExtra("uri", item.uri);
					
					startActivity(intent);
				}
			}
		});
		
		btn_refresh = findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.getChannelListFromServer();
			}
		});
		
		pDialog = new ProgressDialog(MainActivity.this);
		pDialog.setMessage("Getting Channel List, Please wait...");
		pDialog.setCancelable(false);
		
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
		pDialog.show();
		BaseTask.run(new TaskListener() {
			@Override
			public Object onTaskRunning(int taskId, Object data) {
				Object result = Server.GetChannelList();

				if (result instanceof ChannelModel.Result) {
					ChannelModel.Result info_list = (ChannelModel.Result) result;
					mChannelList = info_list.result;
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
					if (mChannelList == null || mChannelList.size() == 0) {
						// show refresh button
						lst_category.setVisibility(View.GONE);
						btn_refresh.setVisibility(View.VISIBLE);
						
					} else {
						// hide refresh button and show list
						lst_category.setVisibility(View.VISIBLE);
						btn_refresh.setVisibility(View.GONE);
						
						LazyAdapter adapter = new LazyAdapter(MainActivity.this, mChannelList);
						lst_category.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}

				} else {
					// show refresh button
					lst_category.setVisibility(View.GONE);
					btn_refresh.setVisibility(View.VISIBLE);

					String errStr = (String) result;
					Toast.makeText(MainActivity.this, errStr, Toast.LENGTH_LONG).show();
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
	
	public class LazyAdapter extends BaseAdapter {
		Activity act;
		ArrayList<ChannelModel.Info> mArrayList;
		private LayoutInflater inflater;

		LazyAdapter(Activity a, ArrayList<ChannelModel.Info> category) {
			act = a;
			if (category == null) {
				this.mArrayList = new ArrayList<>();
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
				holder.item_layout = convertView.findViewById(R.id.item_layout);
				holder.img_channel = convertView.findViewById(R.id.img_channel);
				holder.txt_channel_name = convertView.findViewById(R.id.txt_channel_name);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			try {
				ViewGroup.LayoutParams params = holder.item_layout.getLayoutParams();
				params.height = (int)(AppGlobals.SCREEN_WIDTH * 0.5);
				
				myImageLoader.showImage(holder.img_channel, "");
				holder.txt_channel_name.setText(mArrayList.get(position).name);
				holder.txt_channel_name.setTypeface(FontUtil.GetDeJaFont(MainActivity.this));

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
				/*
				 * Reset Configuration Information
				 */
				AppPreferences.setInt(AppPreferences.KEY.LOGIN_MODE, AppConstants.LOGIN_TYPE_UNKNOWN);
				AppPreferences.removeKey(AppPreferences.KEY.USER_ID);
				AppPreferences.removeKey(AppPreferences.KEY.USER_AVATAR_URL);

				// finish
				MainActivity.super.onBackPressed();
			}
		})
		.setNegativeButton("Cancel", null)
		.show();
	}
}
