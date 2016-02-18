package com.modi.adapter;

import java.util.List;
import java.util.Map;

import activityLoc.xwt.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class sendBookAdapter extends BaseAdapter {
   

    private class buttonViewHolder {
        TextView driver;        
        ImageButton btnCall;      
    }

    private List<Map<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private buttonViewHolder holder;

    public sendBookAdapter(Context c, List<Map<String, Object>> appList,
            int resource, String[] from, int[] to) {
        mAppList = appList;
        mContext = c;      
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position) {
        mAppList.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            holder = (buttonViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_driver, null);
            holder = new buttonViewHolder();
            holder.driver = (TextView) convertView
                    .findViewById(valueViewID[0]);                  
            holder.btnCall = (ImageButton) convertView
                    .findViewById(valueViewID[1]);         
            convertView.setTag(holder);
        }

        Map<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String planTime = (String) appInfo.get(keyString[0]);
          
            holder.driver.setText(planTime);          
            holder.btnCall.setOnClickListener(new lvButtonListener(mContext,position));          
        }
        return convertView;
    }

    class lvButtonListener implements OnClickListener {
        private int position;
        private  Context context;

        lvButtonListener(Context context,int pos) {
        	this.context=context;
            position = pos;
        }

        public void onClick(View v) {
            int vid = v.getId();
            if (vid == holder.btnCall.getId()) {
            	//context.
				util.T.showShortCenter(context, "订单发送成功！");
            }
           
        }

	
    }
}
