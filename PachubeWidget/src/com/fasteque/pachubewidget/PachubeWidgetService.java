package com.fasteque.pachubewidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.RemoteViews;

public class PachubeWidgetService extends Service
{
	public static final String UPDATE = "update";
	//private static final String PREFS_NAME = "com.fasteque.PachubeWdiget";
	private NetworkInfo nwkInfo = null;
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		String command = intent.getAction();
		
		int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
		
		RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.pachubewidget_layout);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		
		//SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		
		
		if(command.equals(UPDATE))
		{
			if(isOnline())
			{
				ParsedFeed feed = RestClient.connect("http://www.pachube.com/api/feeds/" +
													PachubeWidgetConfig.loadFeedIDKeyPref(getApplicationContext(), appWidgetId) + 
													".xml", 
													PachubeWidgetConfig.loadPachubeApiKeyPref(getApplicationContext(), appWidgetId));
				
				if(feed != null)
				{
					remoteView.setTextViewText(R.id.feed_title, feed.getFeedTitle());
					
					remoteView.setTextViewText(R.id.feed_description, feed.getFeedDescription());
					
					if(feed.getFeedStatus().equals("frozen"))
						remoteView.setTextColor(R.id.feed_status, Color.GRAY);
					if(feed.getFeedStatus().equals("live"))
						remoteView.setTextColor(R.id.feed_status, Color.GREEN);
					remoteView.setTextViewText(R.id.feed_status, feed.getFeedStatus());
					
					// TODO : loop all over the data
					if(feed.feedData != null)
					{
						remoteView.setTextViewText(R.id.feed_data_tag, feed.feedData.get(0).getTag());
						
						if(feed.feedData.get(0).getValue().equals(""))
							remoteView.setTextViewText(R.id.feed_data_value, "N/A");
						else
							remoteView.setTextViewText(R.id.feed_data_value, feed.feedData.get(0).getValue());
						
						remoteView.setTextViewText(R.id.feed_data_unit, feed.feedData.get(0).getUnitName());
					}
				}
			}
			else
			{
				remoteView.setTextViewText(R.id.feed_title, String.valueOf(PachubeWidgetConfig.loadFeedIDKeyPref(getApplicationContext(), appWidgetId)));
				
				remoteView.setTextViewText(R.id.feed_status, getString(R.string.no_connection));
			}
			
			// apply changes to widget
			appWidgetManager.updateAppWidget(appWidgetId, remoteView);
		}
		
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	private boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		nwkInfo = cm.getActiveNetworkInfo();
		 
		if((nwkInfo == null) || !nwkInfo.isConnected())
			return false;
		 
		// check if roaming: to disable internet while roaming, just return false.
		if(nwkInfo.isRoaming())
			return true;
		
		return true; 
	}

}
