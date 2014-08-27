package com.baidu.adfolder;


import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatWindow extends LinearLayout {

	public static int viewWidth;
	public static int viewHeight;
	private GridView appWall;
	private Context mContext;
	private List<AppInfo> apps;
	public FloatWindow(final Context context) {
		super(context);
		mContext = context;
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
		apps = manager.getAllRecords();
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
			PackageManager packageManager = mContext.getPackageManager();
			String adPackName = apps.get(position).getPackgeName();
			Intent intent = packageManager.getLaunchIntentForPackage(adPackName);
			mContext.startActivity(intent);
			MyWindowManager.removeBigWindow(mContext);

		}

	}

	class ItemLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			final AppInfo appInfo = apps.get(position);
			appWall.setVisibility(View.GONE);
			LinearLayout ll_adinfo = (LinearLayout) findViewById(R.id.LL_AdInfo);
			ll_adinfo.setVisibility(View.VISIBLE);
			ImageView pack_icon = (ImageView) findViewById(R.id.pack_icon);
			pack_icon.setImageDrawable(appInfo.getIcon());
			TextView pack_lable = (TextView) findViewById(R.id.pack_label);
			pack_lable.setText(appInfo.getName());
			TextView tv_ads = (TextView) findViewById(R.id.tv_ads);
			tv_ads.setText(appInfo.getAdListToStr());
			ImageView uninstall = (ImageView) findViewById(R.id.uninstall);
			uninstall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
			        
			        Uri packageURI=Uri.parse("package:"+appInfo.getPackgeName());			       
			        Intent intent=new Intent(Intent.ACTION_DELETE);		
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        intent.setData(packageURI);			       
			        mContext.startActivity(intent);	         
			        MyWindowManager.removeBigWindow(mContext);
					
				}
			});
			return false;
		}

	}
	

}
