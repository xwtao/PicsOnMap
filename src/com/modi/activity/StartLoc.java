package com.modi.activity;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.T;
import activityLoc.xwt.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class StartLoc extends Activity implements OnGetSuggestionResultListener,android.view.View.OnClickListener {
	private LocationClient mLocClient;
	private EditText locText; 
	private Button finishButton;
	private ListView listView;
	private boolean hasPoi=false;
	private String city="";
	private SuggestionSearch mSuggestionSearch=null;
	private List<String> list=new ArrayList<String>();
	private ArrayAdapter<String> sugAdapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mLocClient = new LocationClient(this.getApplicationContext());
		setContentView(R.layout.activity_startloc);
		locText=(EditText)findViewById(R.id.EditText1);
		//KeyBoardUtils.closeKeybord(locText, StartLoc.this);
	/*	locText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);  
		locText.setInputType(InputType.TYPE_NULL);*/
		
		finishButton=(Button)findViewById(R.id.search);
		listView=(ListView)findViewById(R.id.ListView1);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		finishButton.setOnClickListener(this);
		initLoc();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {				
				String StartAddress=((TextView)view).getText().toString();		
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)). 
			     hideSoftInputFromWindow(locText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS); 	
				Intent intent=new Intent();
				intent.putExtra("StartAddress", StartAddress);
				setResult(RESULT_OK,intent);
				finish();
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			}
		});
		locText.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() <= 0) {
					return;
				}			
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(s.toString()).city(city));				
			}
		

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void	back(View  view){
		StartLoc.this.finish();
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}
	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			{if (info.key != null)
				sugAdapter.add(info.key+" "+info.district);
			Log.e("http5", info.key);
			}
		}
		sugAdapter.notifyDataSetChanged();
	}

	
	@Override
	public void onDestroy() {
		mLocClient.stop();
		mSuggestionSearch.destroy();
		super.onDestroy();
	}

	// 设置相关参数
	private void initLoc() {
		mLocClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				if(location==null) return;
				city=location.getCity();
				mLocClient.requestPoi();
				
			}
			@Override
			public void onReceivePoi(BDLocation location) {
				if(location==null) return;
			//	Log.e("http", location.getPoi());	
				try {
					JSONObject   poiArray = new JSONObject (location.getPoi());
				//	list=new ArrayList<String>();
					//String s=poiArray.getString("p");
					JSONArray object=poiArray.getJSONArray("p");
					//JSONArray object = new JSONArray(s); 
					Log.e("http",location.getPoi());
				/*	JSONObject object2=object.getJSONObject(0);
					locText.setText(object2.getString("name"));*/
					for(int i=0;i<object.length();i++){						
						JSONObject object2=object.getJSONObject(i);
						//Log.e("http", object2.getString("name")+" "+object2.getString("dis"));	
					list.add(object2.getString("name"));						
					}
					sugAdapter=new ArrayAdapter<String>(StartLoc.this, android.R.layout.simple_list_item_1,list);
					listView.setAdapter(sugAdapter);
					
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
		//	option.setScanSpan(5000); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
			mLocClient.setLocOption(option);
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.search:
				String string=locText.getText().toString().trim();
				Log.e("1", string);
				if(string.equals("")) T.showLongCenter(StartLoc.this, "输入不能为空!");
				else {
					Intent intent=new Intent();
					intent.putExtra("StartAddress", string);
					setResult(RESULT_OK,intent);
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)). 
				     hideSoftInputFromWindow(locText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS); 					
					finish();
					overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				}				
				break;

			default:
				break;
			}
			
		}

}
