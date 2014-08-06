package com.baidu.adfolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class FloatFolder extends Activity {
	SharedPreferences sharedPreferences;
	private static final String PREFERENCE_KEY_SHORTCUT_EXISTS = "IsShortCutExists";
	boolean exists = false;
	Util util = new Util();
	ArrayList<AppInfo> appList;
	AppInfo appInfo = new AppInfo();
	String packagename = "";
	List<String> all_class;
	Handler mHandler = new Handler();
	AdResultManager mManager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		exists = sharedPreferences.getBoolean(PREFERENCE_KEY_SHORTCUT_EXISTS,
				false);
		// create shortcut
		// if first time create it
		
		if (!exists) {
		    initAdDB();
			setUpShortCut();
		} else {
			Intent intent = new Intent(FloatFolder.this,
					FloatWindowService.class);
			startService(intent);
		}
		finish();

	}

	private void initAdDB() {
		mManager = AdResultManager.getInstance(FloatFolder.this);
		mHandler.post(readsig);
	}

	/*
	 * create shortcut
	 */
	private void setUpShortCut() {
		String name = getResources().getString(R.string.folderName);
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

		shortcut.putExtra("duplicate", false);

		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.putExtra("tName", name);
		shortcutIntent.setClassName("com.baidu.adfolder",
				"com.baidu.adfolder.FloatFolder");
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		sendBroadcast(shortcut);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(PREFERENCE_KEY_SHORTCUT_EXISTS, true);
		editor.commit();
	}

	// read the ad sigs
	Runnable readsig = new Runnable() {

		@Override
		public void run() {
			String Result = Util
					.getFromAssets("baidu_ad_sig", FloatFolder.this);
			List<Map<String, ArrayList<String>>> sig_list = Util
					.parseJson(Result);
			appList = Util.getAllPackage(FloatFolder.this);
			for (AppInfo info : appList) {
				all_class = info.getClasses();
				String ad_name = "";
				ad_name = Util.compare_sig(all_class, sig_list);
				String package_name = info.getPackgeName();
				if (ad_name != "") {
					mManager.insertRecord(package_name, ad_name);
				}

			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
