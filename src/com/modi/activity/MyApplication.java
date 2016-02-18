package com.modi.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionSearch;

public class MyApplication extends Application {
	
	public BDLocation getBdLocation() {
		return bdLocation;
	}


	public void setBdLocation(BDLocation bdLocation) {
		this.bdLocation = bdLocation;
	}


	public String getBeginAddress() {
		return beginAddress;
	}


	public void setBeginAddress(String startAddress) {
		this.beginAddress = startAddress;
	}


	public String getEndAddress() {
		return endAddress;
	}


	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		mLocClient = new LocationClient(this.getApplicationContext());
		initLoc();
	}
	
	
	private void initLoc() {
		mLocClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				if(location==null||location.getAddrStr()==null) return;
				Log.e("1", location.toString()+"finish");
				bdLocation=location;
				setBeginAddress(location.getDistrict()+location.getStreet());
				city=location.getCity();
				ll = new LatLng(location.getLatitude(),location.getLongitude());
				//mLocClient.requestPoi();
							
				
			
				
			}
			@Override
			public void onReceivePoi(BDLocation location) {
				if(location==null||location.getPoi()==null) return;
			location.setLatitude(ll.latitude);
			location.setLongitude(ll.longitude);
				try {
					JSONObject   poiArray = new JSONObject (location.getPoi());
				//	list=new ArrayList<String>();
					//String s=poiArray.getString("p");
					JSONArray object=poiArray.getJSONArray("p");
					//JSONArray object = new JSONArray(s); 
					Log.e("http",object.length()+"a");
				/*	JSONObject object2=object.getJSONObject(0);
					locText.setText(object2.getString("name"));*/
					for(int i=0;i<object.length();i++){						
						JSONObject object2=object.getJSONObject(i);
						//Log.e("http", object2.getString("name")+" "+object2.getString("dis"));	
					list.add(object2.getString("name"));						
					}
					/*sugAdapter=new ArrayAdapter<String>(StartLoc.this, android.R.layout.simple_list_item_1,list);
					listView.setAdapter(sugAdapter);*/
					
				} catch (JSONException e) {
					Log.e("http",e.toString());
					e.printStackTrace();
				}
				
			}
		});
		
		
		setLocationOption();
		mLocClient.start();
		
	}
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型			
		option.setAddrType("all");
		option.setPoiNumber(10);
		option.disableCache(true);
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		option.setScanSpan(5000); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		mLocClient.setLocOption(option);
	}
	private LocationClient mLocClient;
	private String city="";
	private LatLng ll;	
	private String beginAddress="";
	private String endAddress="";
	private BDLocation bdLocation=null;
	private List<String> list=new ArrayList<String>();
}