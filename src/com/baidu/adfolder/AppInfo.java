package com.baidu.adfolder;

import java.util.List;

import android.graphics.drawable.Drawable;


public class AppInfo {
	
	
	public AppInfo() {
		
	}
	public AppInfo(Drawable icon, String name, String packgeName,
			String[] adList) {
		super();
		this.icon = icon;
		this.name = name;
		this.packgeName = packgeName;
		this.adList = adList;
	}
	private Drawable icon;
	private String name;
	private String packgeName;
	private String[] adList;
	private List<String> classes;
	
	public String[] getAdList() {
		return adList;
	}
	public void setAdList(String[] adList) {
		this.adList = adList;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackgeName() {
		return packgeName;
	}
	public void setPackgeName(String packgeName) {
		this.packgeName = packgeName;
	}
	public List<String> getClasses() {
		return classes;
	}
	public void setClasses(List<String> classes) {
		this.classes = classes;
	}
	public String getAdListToStr(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < adList.length; i++) {
			sb.append(adList[i]+"\n");
		}
		return sb.toString();
	}
	
}
