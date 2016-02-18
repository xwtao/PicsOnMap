package com.modi.activity;

import java.util.ArrayList;
import java.util.List;

import util.T;
import activityLoc.xwt.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class Activity_ReadPerson extends Activity   {
	private List<LatLng> list=new ArrayList<LatLng>();
	private MapView mMapView;	
	private BaiduMap mBaiduMap;
	private boolean isFirstLoc=true;
	private LocationClient mLocClient;
	private InfoWindow mInfoWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_read_person);
		
		list.add(new LatLng(28, 141.0001));
		list.add(new LatLng(28, 140));
		list.add(new LatLng(28, 110.00002));
		list.add(new LatLng(29.00001, 140));
		list.add(new LatLng(28.000001, 141.0000002));
		
		initMap();
		initLoc();
		initClickPic();
		
	}

	protected void initMap() {
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showZoomControls(false);//隐藏缩放控件
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(14);
		mBaiduMap.animateMapStatus(u);
		mBaiduMap.setMapStatus(u);		
		mBaiduMap.setMyLocationEnabled(true);
	}


	

	// 设置相关参数
		private void initLoc() {
			mLocClient = new LocationClient(this.getApplicationContext());
			mLocClient.registerLocationListener(new BDLocationListener() {				
				@Override
				public void onReceiveLocation(BDLocation location) {
					if(location==null) return;
					LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
					MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDerect()).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
					if (isFirstLoc) {						
						MapStatusUpdate	u = MapStatusUpdateFactory.newLatLng(ll);
						mBaiduMap.animateMapStatus(u);
						isFirstLoc=false;
					}
					Log.i("1", ll.latitude+" "+ll.longitude);
					//添加头像
					for(int i=0;i<list.size();i++)
					addPic(list.get(i), R.drawable.taxi, i+"");//最后1位为传入的id					
				}
				
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
	
	//添加头像
	public void addPic(LatLng ll,int pic,String id){
		mBaiduMap.addOverlay(new MarkerOptions().position(ll)
				.icon(BitmapDescriptorFactory
						.fromResource(pic)).title(id));
	}

// 点击头像事件
	private void initClickPic() {
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				final String id=marker.getTitle();//点击头像的id
				T.showShort(Activity_ReadPerson.this,id);
				//Log.i("idii", id);	
				
			/*   点击头像后在头像上显示按钮,listener为按钮点击事件*/
			 	/*Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);		*/
				LayoutInflater inflater=getLayoutInflater();
				View view=inflater.inflate(R.layout.pupwindow, null);
				TextView teView=(TextView)view.findViewById(R.id.pup_text);
				teView.setText(id);
				Button button=(Button)view.findViewById(R.id.pup_but);
				button.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(Activity_ReadPerson.this,ReadLocation.class);
						Point point=new Point(list.get(Integer.parseInt(id)).latitude,(list.get(Integer.parseInt(id)).longitude));
						intent.putExtra("location", point);
						startActivity(intent);
					}
				});
				
				LatLng ll = marker.getPosition();	
							
				OnInfoWindowClickListener	listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
												
							
						}
					};
					
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -47, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);//显示按钮
					//mBaiduMap.hideInfoWindow();//隐藏按钮
				return true;			
			}
			});
		
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
}

