package com.modi.activity;

import activityLoc.xwt.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class Activity_MainUI extends Activity {

	@ViewInject(R.id.moto)
	private Button moto;

	@ViewInject(R.id.car)
	private Button car;

	@ViewInject(R.id.bus)
	private Button bus;

	@ViewInject(R.id.metre)
	private Button metre;

	@ViewInject(R.id.truck)
	private Button truck;


	@ViewInject(R.id.errand)
	private Button errand;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_ui);	
		ViewUtils.inject(this);
		initClick();

	}

	private void initClick() {
		moto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_MainUI.this, MainModi.class);
				// intent.putExtra("CurrentLoc",loc.getAddrStr());
				startActivity(intent);
				overridePendingTransition(R.anim.new_dync_in_from_right,
						R.anim.new_dync_out_to_left);

			}
		});
		
		truck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_MainUI.this, Activity_Truck.class);
				// intent.putExtra("CurrentLoc",loc.getAddrStr());
				startActivity(intent);
				overridePendingTransition(R.anim.new_dync_in_from_right,
						R.anim.new_dync_out_to_left);

			}
		});

	}

	

	@Override
	protected void onPause() {
	
		super.onPause();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
}
