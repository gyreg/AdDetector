package com.baidu.adfolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import dalvik.system.DexFile;
import android.R.bool;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Utils
 * @author fengyajie
 *
 */
public class Util {


	/**
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<AppInfo> getAllPackage(Context context){
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		AppInfo appInfo;

		PackageManager pm = context.getPackageManager();

		List<PackageInfo> packs = pm.getInstalledPackages(0);  
		for(PackageInfo pi:packs){  

          if((pi.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0&&(pi.applicationInfo.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)==0){         
        	  appInfo = new AppInfo();
              appInfo.setPackgeName(pi.applicationInfo.packageName);
              List<String> all_class;
              all_class =getAllClass(pi.applicationInfo.packageName);
              appInfo.setClasses(all_class);
              appList.add(appInfo);
          }  
   }		
		return appList;
   }
	

/**
 * 
 * @param package_name
 * @return
 */
    @SuppressWarnings("rawtypes")
    public static List<String> getAllClass(String package_name){
	  List<String> all_class = new ArrayList<String>();
	  String sourceDir = "/data/app/" + package_name + "-1.apk";
	  File localFile = new File(sourceDir);
      try{
    	  Object clas = new DexFile(localFile).entries();
    	  if (((Enumeration) clas).hasMoreElements()){
    		  do{
    			  String single_class = (String) ((Enumeration) clas)
                          .nextElement();
    			  all_class.add(single_class);
    		  }
    		  while((((Enumeration) clas).hasMoreElements()));
    	  } 
      }
      catch(Exception e){
    	  e.printStackTrace();
      }
	  return all_class;
  }

 /**
  * 
  * @param fileName
  * @param context
  * @return
  */
  public static String getFromAssets(String fileName, Context context)
  {
	  String Result = "";
	  try{
	  InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
	  BufferedReader bufReader = new BufferedReader(inputReader);
	  String line = "";
	  while((line = bufReader.readLine()) != null){
		  Result += line;
	  }
	  }
	  catch(IOException e){
		  e.printStackTrace();
	  }
	  return Result;
  }
  
 /**
  * 
  * @param result
  * @return
  */
  public static List<Map<String, ArrayList<String>>> parseJson(String result)
  {
	  List<Map<String, ArrayList<String>>> list = new ArrayList<Map<String, ArrayList<String>>>();  
	  Map<String,ArrayList<String>> map = null;  
	  JSONTokener jsonParser = new JSONTokener(result); 
	  try {
		JSONObject data = (JSONObject) jsonParser.nextValue();
		JSONArray jsonArray = data.getJSONArray("pluglist");
		for (int i = 0; i < jsonArray.length(); i++) {   
            JSONObject item = jsonArray.getJSONObject(i); // 
            String plugname = item.getString("plugname");     // 
            String sig = item.getString("sig");  
            List<String> sigs;
            sigs = new ArrayList<String>();
            if(sig.contains(";")){
            	String[] si = sig.split(";");
            	for(String s : si){
            		   sigs.add(s);
            }}else{
            	sigs.add(sig);
            }
            
            map = new HashMap<String, ArrayList<String>>(); // 
            map.put(plugname, (ArrayList<String>) sigs);            
            list.add(map);
        }   
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	  return list;
  }
  
 /**
  * 
  * @param classes
  * @param sig_list
  * @return
  */
  public static String compare_sig(List<String> classes, List<Map<String, ArrayList<String>>> sig_list){
	  List<String> ad_names;
	  String ad_name = "";
	  ad_names = new ArrayList<String>();
	  for(Map<String, ArrayList<String>> map_sig : sig_list){
			@SuppressWarnings("rawtypes")
			Iterator iter = map_sig.entrySet().iterator();     
	        // the first method to travel the map  
	        while (iter.hasNext()) {  
	            @SuppressWarnings("unchecked")
				Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>) iter.next();               
	            String key = entry.getKey();  	            
	            ArrayList<String> sig_lis = entry.getValue();
	            for(String single_sig : sig_lis){
	            	if(classes.contains(single_sig)){
	            		ad_names.add(key);
	            		break;
	            	}
	            }
		}
		}
	  if(ad_names.size() != 0){
			ad_name = listToString(ad_names, ";");
	   }
	  return ad_name;
  }
  
 /**
  * 
  * @param list
  * @param separator
  * @return
  */
  public static String listToString(List<String> list, String separator) {  
		StringBuilder sb = new StringBuilder();  
		for (int i = 0; i < list.size(); i++) {  
		    sb.append(list.get(i)).append(separator);  
		}  
		return sb.toString().substring(0,sb.toString().length()-1);  
}
 
 
  
}
