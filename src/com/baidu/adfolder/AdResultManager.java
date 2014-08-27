package com.baidu.adfolder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.CursorLoader;
import android.util.Log;

public class AdResultManager {

	private String dbName = "ad.db";
	private int version = 1;
	private SQLiteDatabase mDB;
	private AdDataOpenHelper mDatabaseHelper;
	private Context mContext;
	public static Handler sHandler;

	public static String tableName = "ad_result";
	public static final String COLUMN_AD_NAME = "ad_name";
	public static final String COLUMN_APP_PACKAGENAME = "package_name";
	private static AdResultManager instance;

	public AdResultManager(Context context) {
		mDatabaseHelper = new AdDataOpenHelper(context);
		mDB = mDatabaseHelper.getWritableDatabase();
		mContext = context;
	}

	public SQLiteDatabase getAdResultDatabase() {
		return mDB;
	}

	public static synchronized AdResultManager getInstance(Context context) {
		if (instance == null) {
			instance = new AdResultManager(context);
		}
		return instance;
	}

	class AdDataOpenHelper extends SQLiteOpenHelper {

		// create table
		private String sql = "CREATE TABLE if not exists " + tableName + " (`"
				+ COLUMN_APP_PACKAGENAME + "` varchar NOT NULL PRIMARY KEY, `"
				+ COLUMN_AD_NAME + "` varchar)";

		public AdDataOpenHelper(Context context) {
			super(context, dbName, null, version);
		}

		public AdDataOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		public AdDataOpenHelper(Context context, String name, int version) {
			super(context, name, null, version);

		}

		public AdDataOpenHelper(Context context, String name) {
			this(context, name, version);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}

	/**
	 * 
	 * @return all the ad apps
	 * 
	 */
	public List<AppInfo> getAllRecords() {
		String sql = "select * from ad_result";
		List<AppInfo> list = new ArrayList<AppInfo>();

		Cursor cursor = mDB.rawQuery(sql, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String pkgName = cursor.getString(cursor
						.getColumnIndex(COLUMN_APP_PACKAGENAME));
				String[] adList = cursor.getString(
						cursor.getColumnIndex(COLUMN_AD_NAME)).split(
						";");
				PackageManager pm = mContext.getPackageManager();

				try {
					ApplicationInfo info = pm.getApplicationInfo(pkgName,
							PackageManager.GET_META_DATA);
					Drawable icon = info.loadIcon(pm);
					String name = (String) info.loadLabel(pm);
					list.add(new AppInfo(icon, name, pkgName, adList));
				} catch (NameNotFoundException e) {
					
					e.printStackTrace();
				}

			}
			cursor.close();
		}

		return list;
	}

	/**
	 * 
	 * @param package_name
	 */
	public void deleteOneRecord(String package_name) {
		String sql = "delete from ad_result where package_name = ?";
		try {
			mDB.execSQL(sql, new String[] { package_name });
		} catch (SQLException e) {
			Log.i("com.baidu.adfolder", "delete failed");
		}
	}

	@SuppressWarnings("finally")
	public boolean isContainApp(String package_name) {
		boolean Flag = false;
		try {
			String sql = "select * from ad_result where package_name = ?";
			Cursor cursor = mDB.rawQuery(sql, new String[] { package_name });
			if (cursor.moveToFirst()) {
				Flag = true;
			}
			cursor.close();
		} catch (SQLException e) {
			Log.i("com.baidu.adfolder", "select failed");
		} finally {

			return Flag;
		}
	}

	public void insertRecord(String package_name, String ad_name) {
		String sql = "replace into ad_result(package_name,ad_name) values(?,?)";
		try {
			mDB.execSQL(sql, new Object[] { package_name, ad_name });
		} catch (SQLException e) {
			Log.i("com.baidu.adfolder", "update failed");
		}

	}

}
