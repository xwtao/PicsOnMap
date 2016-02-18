package com.modi.activity;

import java.io.Serializable;

class MyLocation implements Serializable{
	 // private static final long serialVersionUID = -7060210544600464481L; 
	Point point;//坐标，(latLng.longitude,latLng.latitude)
	String Poi;//在地理信息系统中,一个POI可以是一栋房子、一个商铺、一个邮筒、一个公交站等
	String Adress;//地址
	String Poi_Adress;//Poi+Adress
	//BDLocation BdLocation;	//保存了首次定位时的地理信息
}

class Point implements Serializable{
	public double x;
	public double y;
public Point(){
		
	}
	public Point(double a,double b){
		x=a;
		y=b;
	}		
}
