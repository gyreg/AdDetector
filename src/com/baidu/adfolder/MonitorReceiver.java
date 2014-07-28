package com.baidu.adfolder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * monitor the app change
 * @author fengyajie
 *
 */
public class MonitorReceiver extends BroadcastReceiver {

	public static final int ADDED = 1;
	public static final int REMOVED = 2;
	public static final int REPLACED = 3;
	@Override
	public void onReceive(Context context, Intent intent) {
		
	   String package_name = intent.getDataString().replace("package:", "");
	   if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){
		  
		   Intent service = new Intent(context, ScanServive.class);
		   service.putExtra("SCAN_TYPE", ADDED);
		   service.putExtra("package", package_name);
		   context.startService(service);
	   }
	   else if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
		  
		   Intent service = new Intent(context, ScanServive.class);
		   service.putExtra("SCAN_TYPE", REMOVED);
		   service.putExtra("package", package_name);
		   context.startService(service);
	   }
       else if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){
    	  
    	   Intent service = new Intent(context, ScanServive.class);
    	   service.putExtra("SCAN_TYPE", REPLACED);
		   service.putExtra("package", package_name);
		   context.startService(service);
	   }
	}
	
	

}
