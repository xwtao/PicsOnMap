package com.modi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Affair;
import util.DateUtil;
import util.T;
import activityLoc.xwt.R;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.modi.adapter.sendBookAdapter;

public class FinishSearch extends PopupWindow {


	private EditText startText;
	private EditText endText;
	private ListView listView;
	private View mMenuView;
	private MyApplication application;
	private List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	private List<String> iDlist=new ArrayList<String>();
	private Handler handler;
	private String point="";
	private String location="";
	public FinishSearch(final Activity context,OnClickListener itemsOnClick) {
		super(context);
		application=(MyApplication)context.getApplication();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_finishsearch, null);
		startText = (EditText) mMenuView.findViewById(R.id.start);		
		endText = (EditText) mMenuView.findViewById(R.id.end);	
		listView=(ListView)mMenuView.findViewById(R.id.ListViewFinishS);
		startText.setText(application.getBeginAddress());
		endText.setText(application.getEndAddress());	
		 handler=new Handler(){
			 @Override
				public void handleMessage(Message msg){
				if(msg.what==0){
					BaseAdapter adapter=new sendBookAdapter(context, list, R.layout.item_driver,new String[]{"realName","sendBook"} , new int[]{R.id.driver,R.id.sendBook});
				//ArrayAdapter<String> adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
				if(list.size()!=0)	listView.setAdapter(adapter);
				}
				if(msg.what==1) {
					T.showShortCenter(context, "订单发送成功！");
					dismiss();}
			}};
		
			new Thread(new Runnable() {				
				@Override
				public void run() {
					double x=application.getBdLocation().getLongitude();		
					double y=application.getBdLocation().getLatitude();				
					point=x+";"+y;
					try {
						location=application.getBdLocation().getProvince()+application.getBdLocation().getCity();
						String	aString = Affair.nearByDriver(point,location,"1");
					
					//Log.i("11",aString);
					JSONArray driverArray = new JSONArray(aString);	
					Log.i("1",driverArray.length()+"");
					for(int i=0;i<driverArray.length();i++){						
						JSONObject object2=driverArray.getJSONObject(i);		
						Map< String, Object> map=new HashMap<String, Object>();
						map.put("realName", "司机:"+object2.getString("realName")+" 距我:"+object2.getString("distance")+"米");
						map.put("sendBook", new ImageButton(context));
						list.add(map);
						iDlist.add(object2.getString("id"));
						}
					Message msg=new Message();
					msg.what=0;
					handler.sendMessage(msg);
					} catch (Exception e) {
						Log.i("1",e.toString()+"");
						e.printStackTrace();
					}
				}
			}).start();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, final int n,
					long arg3) {
			new Thread(new Runnable() {				
					@Override
					public void run() {
				try {							
				Affair.addRequest(iDlist.get(n),startText.getText().toString(),endText.getText().toString(),point,DateUtil.getNow(),"面议",location);
				Log.i("1",DateUtil.getNow()+"a");
				Message msg=new Message();
				msg.what=1;
				handler.sendMessage(msg);
				} catch (Exception e) {
					Log.i("1",e.toString()+"");
					e.printStackTrace();
				}
					}}).start();
			}
			
		});
		
		this.setContentView(mMenuView);
	
		this.setWidth(LayoutParams.FILL_PARENT);
		
		this.setHeight(LayoutParams.WRAP_CONTENT);
		
		this.setFocusable(true);
	
		this.setAnimationStyle(R.style.PopupAnimation);
	   

		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						//dismiss();
					}
				}				
				return true;
			}
		});

	}

}
