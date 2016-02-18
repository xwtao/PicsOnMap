package com.modi.activity;

import java.util.ArrayList;
import java.util.List;

import activityLoc.xwt.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

public class Activity_Truck extends Activity implements
		OnGetSuggestionResultListener {
	private List<String> list=new ArrayList<String>();
	private MyApplication application;
	private String city;
	private MapView mMapView;	
	private BaiduMap mBaiduMap;
	private SuggestionSearch mSuggestionSearch = null;

	private LocationClient mLocClient;

	private AutoCompleteTextView startWorldsView, endWorldsView;

	private ArrayAdapter<String> sugAdapter;
	private boolean isStart=false;
	private boolean isFirstLoc=true;
	private EditText timepick;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (MyApplication) getApplication();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_truck);
		initMap();
		initLoc();
		initTimePick();
		initSuggest();
	}



	protected void initMap() {
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showZoomControls(false);//隐藏缩放控件
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(15);
		mBaiduMap.animateMapStatus(u);
		mBaiduMap.setMapStatus(u);		
		mBaiduMap.setMyLocationEnabled(true);
	}


	private void initTimePick() {
		timepick=(EditText)findViewById(R.id.time);
		timepick.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				 TimePickerFragment  timePicker = new TimePickerFragment(); 				

			if(hasFocus) timePicker.showTimePickerDialog(timePicker);
				
			}
		});
		
	}
	protected void initSuggest(){
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);	
		startWorldsView = (AutoCompleteTextView) findViewById(R.id.startAuto);
		endWorldsView = (AutoCompleteTextView) findViewById(R.id.endAuto);
		startWorldsView.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				isStart=true;
				if (cs.length() <= 0) {
					return;
				}
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});
		
		endWorldsView.addTextChangedListener(new TextWatcher() {
			

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				isStart=false;
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});
	}
	// 设置相关参数
		private void initLoc() {
			mLocClient = new LocationClient(this.getApplicationContext());
			mLocClient.registerLocationListener(new BDLocationListener() {			
				
				@Override
				public void onReceiveLocation(BDLocation location) {
					if(location==null) return;
					city=location.getCity();
					//mLocClient.requestPoi();
					MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
					LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
					if (isFirstLoc) {						
						MapStatusUpdate	u = MapStatusUpdateFactory.newLatLng(ll);
						mBaiduMap.animateMapStatus(u);
						isFirstLoc=false;
					}				
					//添加头像
					mBaiduMap.addOverlay(new MarkerOptions().position(ll)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.icon_marka)));
					
				}
				@Override
				public void onReceivePoi(BDLocation location) {
					if(location==null) return;
			
					
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
			//	option.setScanSpan(5000); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
				mLocClient.setLocOption(option);
			}
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();

		mMapView = null;

		mSuggestionSearch.destroy();

		super.onDestroy();
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		//sugAdapter.clear();
		sugAdapter = new ArrayAdapter<String>(this, R.layout.activity_autostyle);
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			{
				if (info.key != null){	
				
					sugAdapter.add(info.key);}
				Log.e("http5", info.key);
			}
		}
		if (isStart)
			startWorldsView.setAdapter(sugAdapter);		//初始时无值会有问题
		else endWorldsView.setAdapter(sugAdapter);
		sugAdapter.notifyDataSetChanged();
	}

}
	

