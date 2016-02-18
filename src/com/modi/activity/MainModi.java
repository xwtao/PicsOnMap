package com.modi.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.T;
import activityLoc.xwt.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class MainModi extends Activity implements OnGetGeoCoderResultListener,
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
		OnGetShareUrlResultListener, BaiduMap.OnMarkerClickListener {
	MyApplication application;
	GeoCoder mSearch = null;
	private FinishSearch menuWindow;
	String city = "";
	// 定位相关

	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private InfoWindow mInfoWindow;
	BitmapDescriptor mCurrentMarker;
	MyLocation location = new MyLocation();
	String url = "";
	Boolean isPoi = false, isSearched = false, hasReturnAddress = false;
	public BDLocation location1;
	MapView mMapView;
	BaiduMap mBaiduMap;
	Button ActivityButton, StartButton;
	private LatLng currentPt;
	// UI相关
	// OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位

	String beginAddress = "", endAddress = "", begin = "";

	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private SDKReceiver mReceiver;
	/**
	 * 搜索关键字输入窗口
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;
	private ShareUrlSearch mShareUrlSearch = null;
	private RadioGroup mRadioGroup1;
	private RadioButton mRadio1;
	private RadioButton mRadio2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (MyApplication) getApplication();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_location);
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		mShareUrlSearch = ShareUrlSearch.newInstance();
		mShareUrlSearch.setOnGetShareUrlResultListener(this);

		StartButton = new Button(getApplicationContext());
		StartButton.setBackgroundResource(R.drawable.popup);

		requestLocButton = (Button) findViewById(R.id.button1);

		initOtherModi();

		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText("普通");
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText("罗盘");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showZoomControls(false);// 隐藏缩放控件
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(15);
		mBaiduMap.animateMapStatus(u);

		mBaiduMap.setMapStatus(u);
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				currentPt = point;
				LatLng ptCenter = new LatLng(currentPt.latitude,
						currentPt.longitude);
				// 反Geo搜索
				/*
				 * mSearch.reverseGeoCode(new ReverseGeoCodeOption()
				 * .location(ptCenter));
				 */
				// Marker marker=null;marker.setTitle("dfg");
				/*
				 * mShareUrlSearch.requestLocationShareUrl(new
				 * LocationShareURLOption().location(currentPt).snippet("测试分享点")
				 * .name(marker.getTitle()));
				 */

			}

			public boolean onMapPoiClick(MapPoi arg0) {
				/*
				 * PoiInfo info = getPoiResult().getAllPoi().get(i); currentAddr
				 * = info.address; mShareUrlSearch .requestPoiDetailShareUrl(new
				 * PoiDetailShareURLOption() .poiUid(info.uid));
				 */

				beginAddress = arg0.getName();
				isPoi = true;
				currentPt = arg0.getPosition();
				LatLng ptCenter = new LatLng(currentPt.latitude,
						currentPt.longitude);
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));

				return false;
			}
		});
		mBaiduMap.setOnMarkerClickListener(this);
		/*
		 * mBaiduMap.setOnMarkerClickListener(new
		 * BaiduMap.OnMarkerClickListener() {
		 * 
		 * @Override public boolean onMarkerClick(final Marker marker) { Button
		 * button = new Button(getApplicationContext());
		 * button.setBackgroundResource(R.drawable.popup);
		 * OnInfoWindowClickListener listener = null; button.setText("设为目的地");
		 * listener = new OnInfoWindowClickListener() { public void
		 * onInfoWindowClick() { LatLng ll = marker.getPosition(); LatLng llNew
		 * = new LatLng(ll.latitude + 0.005, ll.longitude + 0.005);
		 * marker.setPosition(llNew); mBaiduMap.hideInfoWindow(); } }; LatLng ll
		 * = marker.getPosition(); mInfoWindow = new
		 * InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -40,
		 * listener); mBaiduMap.showInfoWindow(mInfoWindow); return false; } });
		 */
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		// option.setLocationMode(LocationMode.Hight_Accuracy);
		// option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
		option.setAddrType("all"); // 返回的定位结果包含地址信息
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000); // 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true); // 禁止启用缓存定位
		option.setPoiNumber(10); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocClient.setLocOption(option);
		mLocClient.start();

		// mLocClient.requestLocation();
		// if (mLocClient != null && mLocClient.isStarted())
		// mLocClient.requestPoi();

		// setContentView(R.layout.activity_poisearch);`
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey1);
		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		final OnClickListener itemsOnClick = new OnClickListener() {

			public void onClick(View v) {
				menuWindow.dismiss();
				switch (v.getId()) {
				/*
				 * case R.id.btn_take_photo: break; case R.id.btn_pick_photo:
				 * break;
				 */
				default:
					break;
				}

			}

		};
		keyWorldsView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int arg2, long arg3) {
				isSearched = true;

				/*
				 * int selectedPosition = adapterView.getSelectedItemPosition();
				 * String aa= sugAdapter.getItem(selectedPosition);
				 */

				TextView textview = (TextView) view;
				String aa = textview.getText().toString();
				endAddress = "<br/>" + "到 <font color=blue>" + aa + "</font>";
				// Log.e("http2", aa + " " + StartButton.getText());
				try {
					/*
					 * StartButton= new Button(getApplicationContext());
					 * //要再实例化一次否则会报空指针异常
					 * StartButton.setBackgroundResource(R.drawable.popup);
					 * StartButton.setText(Html.fromHtml(locString+"<br/>"+
					 * "到 <font color=blue>"+aa+"</font>"));
					 * StartButton.setTextColor(Color.BLACK);
					 * //StartButton.setTextSize(15); OnInfoWindowClickListener
					 * listener = null;
					 * 
					 * listener = new OnInfoWindowClickListener() { public void
					 * onInfoWindowClick() { Intent intent=new
					 * Intent(LocationMain.this,StartLoc.class);
					 * startActivity(intent); } };
					 */
					MapStatusUpdate u = MapStatusUpdateFactory
							.newLatLng(myListener.point);
					mBaiduMap.animateMapStatus(u);
					// mInfoWindow = new
					// InfoWindow(BitmapDescriptorFactory.fromView(StartButton),
					// myListener.point, -10, listener);
					// mBaiduMap.showInfoWindow(mInfoWindow);
					// u=MapStatusUpdateFactory.zoomTo(16);
					mBaiduMap.animateMapStatus(u);
					/*
					 * new Thread() {
					 * 
					 * @Override public void run() { try { Thread.sleep(800); }
					 * catch (InterruptedException e) { e.printStackTrace(); } }
					 * }.start(); mBaiduMap.animateMapStatus(u1);
					 */
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
							.

							hideSoftInputFromWindow(
									keyWorldsView.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					/*
					 * Intent intent=new Intent();
					 * intent.putExtra("beginAddress", beginAddress);
					 * intent.putExtra("endAddress", endAddress);
					 */
					application.setBeginAddress(beginAddress);
					application.setEndAddress(aa);
					menuWindow = new FinishSearch(MainModi.this, itemsOnClick);
					// 显示窗口
					menuWindow.showAtLocation(
							MainModi.this.findViewById(R.id.main),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

				} catch (Exception e) {
					Log.e("http4", e.toString());
					e.printStackTrace();
				}
				/*
				 * Message msg=new Message(); msg.obj=aa;
				 * handler.sendMessage(msg);
				 */
			}

		});

		/*
		 * keyWorldsView.setOnFocusChangeListener(new OnFocusChangeListener() {
		 * 
		 * public void onFocusChange(View v, boolean hasFocus) {
		 * 
		 * InputMethodManager imm = (InputMethodManager)
		 * getSystemService(Context.INPUT_METHOD_SERVICE);
		 * 
		 * if (hasFocus) {//如果有焦点就显示软件盘
		 * 
		 * imm.showSoftInputFromInputMethod(keyWorldsView.getWindowToken() , 0);
		 * 
		 * } else {//否则就隐藏 try {
		 * imm.hideSoftInputFromWindow(keyWorldsView.getWindowToken(), 0); }
		 * catch (Exception e) { System.out.println(" null！！！！！"); } } } });
		 * //点击空格键隐藏软键盘 keyWorldsView.setOnKeyListener(new View.OnKeyListener(){
		 * public boolean onKey(View v, int keyCode, KeyEvent event) {
		 * 
		 * InputMethodManager imm = (InputMethodManager)
		 * getSystemService(Context.INPUT_METHOD_SERVICE);
		 * 
		 * if(keyCode ==
		 * KeyEvent.KEYCODE_SPACE){//keyCode==KeyEvent.KEYCODE_ENTER
		 * //searchauto.clearFocus();
		 * imm.hideSoftInputFromWindow(keyWorldsView.getWindowToken(), 0);
		 * 
		 * return true; } return false; } });
		 */

		keyWorldsView.addTextChangedListener(new TextWatcher() {

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

				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});

	}

	private void initOtherModi() {
		mRadioGroup1 = (RadioGroup) findViewById(R.id.typegroup);
		mRadio1 = (RadioButton) findViewById(R.id.moto);
		mRadio2 = (RadioButton) findViewById(R.id.other);
		mRadioGroup1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == mRadio2.getId()) {
					Intent intent = new Intent(MainModi.this, Activity_Truck.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_from_down,
							R.anim.out_to_up);
					mRadio1.setChecked(true);
				}
			}
		});
	}

	/**
	 * 定位SDK监听函数
	 */

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
		mSearch.destroy();
		mShareUrlSearch.destroy();
		mMapView = null;

		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		// 取消监听 SDK 广播
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) { // 点击
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		mBaiduMap.setOnMarkerClickListener(this);
		// mBaiduMap。setOnMarkerClickListener(new on);
		String PoiAddress = beginAddress;

		if (PoiAddress.contains("\\"))
			PoiAddress = PoiAddress.replace("\\", "");

		location.Poi = PoiAddress;
		location.Adress = result.getAddress();
		location.Poi_Adress = PoiAddress + ":" + result.getAddress();

		if (isPoi) {

			Toast.makeText(this, location.Poi_Adress + url, Toast.LENGTH_LONG)
					.show();
			keyWorldsView.setText(location.Poi_Adress);
		} else {
			Toast.makeText(this, location.Adress + url, Toast.LENGTH_LONG)
					.show();
			keyWorldsView.setText(location.Adress);
		}
		keyWorldsView.setTextColor(Color.DKGRAY);
		isPoi = false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 影响搜索按钮点击事件
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		mLocClient.requestPoi();
		// EditText editCity = (EditText) findViewById(R.id.city1);
		// EditText editSearchKey = (EditText) findViewById(R.id.searchkey1);
		String keyString = keyWorldsView.getText().toString();
		if (keyString.contains(":"))
			keyString = keyString.substring(0, keyString.indexOf(":"));
		Log.e("keyString", keyString);
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city)
				.keyword(keyString).pageNum(load_Index));
	}

	/*
	 * public void goToNextPage(View v) { load_Index++;
	 * searchButtonProcess(null); }
	 */

	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(this, "未找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
		}
	}

	public void onGetPoiDetailResult(final PoiDetailResult result) { // poi
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		} else {
			String PoiAddress = result.getName();

			if (PoiAddress.contains("\\"))
				PoiAddress = PoiAddress.replace("\\", "");

			location.Poi = PoiAddress;
			location.Adress = result.getAddress();
			location.Poi_Adress = PoiAddress + ":" + result.getAddress();
			keyWorldsView.setText(location.Poi_Adress);
			// location.latLng=result.getLocation();

			Button button = new Button(getApplicationContext());
			button.setBackgroundResource(R.drawable.popup);
			OnInfoWindowClickListener listener = null;
			button.setText("设为目的地");
			button.setTextColor(Color.BLACK);
			listener = new OnInfoWindowClickListener() {
				public void onInfoWindowClick() {

					LatLng ll = result.getLocation();
					location.point = new Point(ll.latitude, ll.longitude);
					Toast toast = Toast.makeText(getApplicationContext(),
							"目的地点设置成功！", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					// finish();
					/**/
					mBaiduMap.hideInfoWindow();
				}
			};
			LatLng ll = result.getLocation();
			mInfoWindow = new InfoWindow(
					BitmapDescriptorFactory.fromView(button), ll, -40, listener);
			mBaiduMap.showInfoWindow(mInfoWindow);

			Toast.makeText(this, location.Poi_Adress, Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		// sugAdapter.clear();
		sugAdapter = new ArrayAdapter<String>(this, R.layout.activity_autostyle);

		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			{
				if (info.key != null)
					sugAdapter.add(info.key);
				// Log.e("http5", info.key);
			}
		}
		keyWorldsView.setAdapter(sugAdapter);
		sugAdapter.notifyDataSetChanged();
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));
			// }
			return true;
		}
	}

	@Override
	public void onGetLocationShareUrlResult(ShareUrlResult result) {
		// 分享短串结果
		url = result.getUrl();
		Toast.makeText(this, result.getUrl(), Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {

		Toast.makeText(this, result.getUrl() + ",poi", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		ActivityButton = new Button(getApplicationContext());
		ActivityButton.setBackgroundResource(R.drawable.popup);
		OnInfoWindowClickListener listener = null;
		ActivityButton.setText("设为目的地");
		ActivityButton.setTextColor(Color.BLACK);
		listener = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {
				LatLng ll = marker.getPosition();
				location.point = new Point(ll.latitude, ll.longitude);
				// Log.e("latlng",
				// location.latLng.latitude+" "+location.latLng.longitude);
				// ActivityButton.setText("设置成功!");
				// ActivityButton.setBackgroundColor(Color.RED);
				Toast toast = Toast.makeText(getApplicationContext(),
						"目的地点设置成功！", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				mBaiduMap.hideInfoWindow();
				// finish();
			}
		};
		LatLng ll = marker.getPosition();
		mInfoWindow = new InfoWindow(
				BitmapDescriptorFactory.fromView(ActivityButton), ll, -40,
				listener);
		mBaiduMap.showInfoWindow(mInfoWindow);
		return false;
	}

	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d("http", "action: " + s);
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				T.showLong(MainModi.this, "请检查网络！");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				T.showLong(MainModi.this, "网络出错");
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			beginAddress = data.getStringExtra("StartAddress");
			hasReturnAddress = true;
			break;

		default:
			break;
		}
	}

	public class MyLocationListenner implements BDLocationListener {
		public LatLng point;
		String Address = "";

		@Override
		public void onReceiveLocation(BDLocation loc) {
			// map view 销毁后不在处理新接收的位置
			if (loc == null || mMapView == null || loc.getAddrStr() == null)
				return;// location.

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(loc.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(loc.getLatitude())
					.longitude(loc.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			LatLng ll;

			city = loc.getCity();
			ll = new LatLng(loc.getLatitude(), loc.getLongitude());
			point = ll;
			try {/*
				 * StartButton= new Button(getApplicationContext());
				 * StartButton.setBackgroundResource(R.drawable.popup);
				 * if(loc.getStreet()==null) { locString="无法定位，请检查网络！";
				 * //isFirstLoc=true;
				 * 
				 * 
				 * StartButton.setTextColor(Color.BLACK);
				 * StartButton.setText(Html.fromHtml(locString)); return;} else{
				 * locString
				 * ="我从 "+"<font color=blue>"+loc.getDistrict()+loc.getStreet
				 * ()+"</font> 上车"+locString0;
				 * 
				 * } StartButton.setTextColor(Color.BLACK);
				 * StartButton.setText(Html.fromHtml(locString));
				 * StartButton.setTextSize(15); OnInfoWindowClickListener
				 * listener = null;
				 * 
				 * listener = new OnInfoWindowClickListener() { public void
				 * onInfoWindowClick() { Intent intent=new
				 * Intent(LocationMain.this,StartLoc.class);
				 * //intent.putExtra("CurrentLoc",loc.getAddrStr());
				 * startActivity(intent); } }; mInfoWindow = new
				 * InfoWindow(BitmapDescriptorFactory.fromView(StartButton), ll,
				 * -10, listener); mBaiduMap.showInfoWindow(mInfoWindow);
				 */
				Address = loc.getDistrict() + loc.getStreet();
			} catch (Exception e) {
				Log.e("httpp", e.toString());
			}

			if (isFirstLoc) {
				// mLocClient.requestPoi();
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				isFirstLoc = false;
			}
			//
			mLocClient.requestPoi();
		}

		@Override
		public void onReceivePoi(BDLocation location) {
			if (location == null || location.getPoi() == null)
				return;
			location.setLatitude(point.latitude);
			location.setLongitude(point.longitude);
			JSONObject poiArray;
			try {
				poiArray = new JSONObject(location.getPoi());
				JSONArray object = poiArray.getJSONArray("p");
				// Log.e("http", object.length() + "a");
				JSONObject object2 = object.getJSONObject(0);
				if (!hasReturnAddress)
					beginAddress = object2.getString("name") + "";
			} catch (JSONException e) {
				e.printStackTrace();
			}
			StartButton = new Button(getApplicationContext());
			StartButton.setBackgroundResource(R.drawable.popup);
			if (location.getStreet() == null) {
				begin = "无法定位，请检查网络！";
				// isFirstLoc=true;

				StartButton.setTextColor(Color.BLACK);
				StartButton.setText(Html.fromHtml(begin));
				return;
			} else {
				if (!isSearched)
					endAddress = "<br/><font color=gray>(" + Address
							+ ")</font>";
				begin = "我从 " + "<font color=blue>" + beginAddress
						+ "</font> 上车" + endAddress;// "<br/>"

			}
			StartButton.setTextColor(Color.BLACK);
			StartButton.setText(Html.fromHtml(begin));
			StartButton.setTextSize(15);
			OnInfoWindowClickListener listener = null;

			listener = new OnInfoWindowClickListener() {
				public void onInfoWindowClick() {
					StartButton
							.setBackgroundResource(R.drawable.map_bg_bubble_pressed);
					Intent intent = new Intent(MainModi.this, StartLoc.class);
					// intent.putExtra("CurrentLoc",loc.getAddrStr());
					startActivityForResult(intent, 1);
					overridePendingTransition(R.anim.new_dync_in_from_right,
							R.anim.new_dync_out_to_left);
					// overridePendingTransition(R.anim.zoom_enter,
					// R.anim.zoom_exit);
				}
			};
			mInfoWindow = new InfoWindow(
					BitmapDescriptorFactory.fromView(StartButton), point, -10,
					listener);
			mBaiduMap.showInfoWindow(mInfoWindow);

		}

	}
	/*
	 * public static boolean isConnect(Context context) {
	 * 
	 * // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） try {
	 * 
	 * ConnectivityManager connectivity = (ConnectivityManager) context
	 * .getSystemService(Context.CONNECTIVITY_SERVICE); if (connectivity !=
	 * null) { // 获取网络连接管理的对象 NetworkInfo info =
	 * connectivity.getActiveNetworkInfo();
	 * 
	 * if (info != null && info.isConnected()) {
	 * 
	 * // 判断当前网络是否已经连接 if (info.getState() == NetworkInfo.State.CONNECTED) {
	 * return true; } } } } catch (Exception e) { Log.v("error", e.toString());
	 * } return false; }
	 */

}
