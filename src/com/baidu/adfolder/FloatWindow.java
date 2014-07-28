package com.baidu.adfolder;


import java.util.List;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindow extends LinearLayout {

	public static int viewWidth;
	public static int viewHeight;
	private GridView appWall;
	private Context mContext;

	public FloatWindow(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_folder, this);
		View view = findViewById(R.id.big_window_layout);
		TextView tv_title = (TextView) findViewById(R.id.Ctext);
		tv_title.setText(R.string.folderName);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		Button close = (Button) findViewById(R.id.finishBtn);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// close float window，stop service.
				MyWindowManager.removeBigWindow(context);
				Intent intent = new Intent(getContext(),
						FloatWindowService.class);
				context.stopService(intent);
			}
		});

		AdResultManager manager = AdResultManager.getInstance(mContext);
		List<AppInfo> apps = manager.getAllRecords();
		appWall = (GridView) findViewById(R.id.app_wall);
		if (!apps.isEmpty()) {
			System.out.println(apps.toString());
			AppWallAdapter adapter = new AppWallAdapter(context, 0, apps,
					appWall);
			appWall.setAdapter(adapter);
			appWall.setOnItemClickListener(new ItemClickListener());
			appWall.setOnItemLongClickListener(new ItemLongClickListener());
		} else {
			appWall.setVisibility(View.GONE);
			TextView tv_note = (TextView) findViewById(R.id.tv_note);
			tv_note.setVisibility(View.VISIBLE);
			
		}

	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

		}

	}

	class ItemLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			return false;
		}

	}

}
