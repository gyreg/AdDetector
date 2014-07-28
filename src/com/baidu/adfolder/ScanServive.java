package com.baidu.adfolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

/**
 * Scan the app for change
 * 
 * @author fengyajie
 * 
 */
public class ScanServive extends Service {

	private String packName;
	private int scanType;
	private AdResultManager manager;

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		packName = intent.getExtras().getString("package");
		scanType = intent.getExtras().getInt("SCAN_TYPE");
		manager = AdResultManager
				.getInstance(ScanServive.this);
		// scanType = 1 install app ;scanType = 2 remove app; scanType = 3
		// replace app
		
		if (scanType == 1 || scanType == 3) {
			new Thread() {
				public void run() {
			
					String Result = Util.getFromAssets("baidu_ad_sig",
							ScanServive.this);
					List<Map<String, ArrayList<String>>> sig_list = Util
							.parseJson(Result);
					List<String> classes = Util.getAllClass(packName);
					String ad_name = Util.compare_sig(classes, sig_list);

					if (ad_name != "") {
						manager.insertRecord(packName, ad_name);
					} else {
						if (scanType == 3) {
							if (manager.isContainApp(packName)) {

								manager.deleteOneRecord(packName);
							}
						}
					}

				};
			}.start();
		} else if (scanType == 2) {
			if (manager.isContainApp(packName)) {
				manager.deleteOneRecord(packName);
			}
		}

	}

}
