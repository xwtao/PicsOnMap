package com.modi.activity;

import java.io.File;
import java.net.URISyntaxException;

import activityLoc.xwt.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class ReadLocation extends Activity {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	MyLocation location=new MyLocation();
	MapView mMapView;
	BaiduMap mBaiduMap;
	BDLocation loc=null;
	boolean isLocated=false;
	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button exitButton;
	boolean isFirstLoc = true;// 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readlocation);
		exitButton = (Button) findViewById(R.id.button1);
		mCurrentMode = LocationMode.NORMAL;
		

		
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		
		Intent intent = getIntent();
		location = (MyLocation)intent.getSerializableExtra("location"); 
		if (intent.hasExtra("location")&&location.point!=null) {
			/*// 当用intent参数时，设置中心点为指定点
			Bundle b = intent.getExtras();
			LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
			mMapView = new MapView(this,
					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
							.target(p).build()));*/
			
			
			
			LatLng ll = new LatLng(location.point.x,
					location.point.y);
		//	Log.d("location1", " 1"+location.point.x+" "+location.point.y);

		MapStatusUpdate
		u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
		
		u=MapStatusUpdateFactory.zoomTo(15);
		mBaiduMap.setMapStatus(u);
		
		mBaiduMap.addOverlay(new MarkerOptions().position(ll)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		} 
	}
	public void startNavi(View view){

		 try {Intent intent = new Intent();
		 Intent intent1 = getIntent();
			location = (MyLocation)intent1.getSerializableExtra("location"); 
               intent = Intent.getIntent("intent://map/direction?destination=latlng:"+location.point.x+","+location.point.y+"|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
               if(isInstallByread("com.baidu.BaiduMap")){
                   startActivity(intent); //启动调用
                    Log.e("GasStation", "百度地图客户端已经安装") ;
               }else{
                    Log.e("GasStation", "没有安装百度地图客户端") ;
                    LatLng pt1 = new LatLng(loc.getLatitude(), loc.getLongitude());
            		LatLng pt2 = new LatLng(location.point.x, location.point.y);
            		// 构建 导航参数
            		if(isLocated)		{
            			NaviPara para = new NaviPara();
            		
            		para.startPoint = pt1;                                                                                                                                                        
            		para.endPoint = pt2;
            		BaiduMapNavigation.openBaiduMapNavi(para, this);
               }
               }
           } catch (URISyntaxException e) {
               e.printStackTrace();
           }
	}
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			isLocated=true;
			loc=location;
			/*MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}*/
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
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
		super.onDestroy();
	}
	 private boolean isInstallByread(String packageName) {    
	     return new File("/data/data/" + packageName).exists();    
	    }

}
