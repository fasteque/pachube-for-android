package com.fasteque.pachubewidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class PachubeWidgetConfig extends Activity
{
	private Context self = this;
	
	int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	EditText cFeedID;
	EditText cPachubeApiKey;
	Spinner cUpdateRate;
	
	int feedID = -1;
	String pachubeApiKey = null;
	int updateRate = -1;
	boolean configDone = false;
	
	final static int rateValue[] = {60, 300, 600, 1800, 3600};
	
    private static final String PREFS_NAME = "com.fasteque.PachubeWdiget";
    private static final String PREF_FEEDID_KEY = "FEEDID_";
    private static final String PREF_PACHUBEAPIKEY_KEY = "PACHUBEAPIKEY_";
    private static final String PREF_UPDATERATE_KEY = "UPDATERATE_";
    private static final String PREF_CONFIG_KEY = "CONFIG_";
    
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        // This will cause the widget host to cancel out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);
        
        setContentView(R.layout.pachubewidget_config);
        
        cFeedID = (EditText)findViewById(R.id.feedId);
        cPachubeApiKey = (EditText)findViewById(R.id.pachubeApiKey);
        
        cUpdateRate = (Spinner) findViewById(R.id.updateRate);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.updateRates, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cUpdateRate.setAdapter(adapter);
        
        cUpdateRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {              
                updateRate = rateValue[(int) parent.getItemIdAtPosition(pos)];
            }
            
            public void onNothingSelected(AdapterView<?> parent)
            {
            	
            }
        });
        
        final Button save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(saveOnClickListener);

        final Button cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(cancelOnClickListener);
        
        /* Find the widget id from the intent.
         * The configuration Activity should always return a result.
         * The result should include the AppWidget ID passed by the Intent that launched the Activity.
         */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        
        if (extras != null)
        {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        
        // If they gave us an intent without the widget id, just bail.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            finish();
        }
        
        cFeedID.requestFocus();
	}
    
    View.OnClickListener saveOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
    		try
    		{
    			feedID = Integer.parseInt(cFeedID.getText().toString());
    		}
    		catch(NumberFormatException e)
    		{
    			cFeedID.getText().clear();
    		}
    		
    		pachubeApiKey = cPachubeApiKey.getText().toString();
    		
    		if((feedID != -1) && (pachubeApiKey != null))
    		{
	    		configDone = true;
	            
	    		final Context context = PachubeWidgetConfig.this;
	            
	    		savePrefs(context, appWidgetId, feedID, pachubeApiKey, updateRate, configDone);
	    		
	    		// setup intent to update the onUpdate method
	    		 
	            Intent frameUpdate = new Intent();
	     
	            frameUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	     
	            frameUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	     
	            sendBroadcast(frameUpdate);
	    		
	    		/*
	            // Push widget update to surface with newly set prefix
	            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	            PachubeWidget.updateAppWidget(context, appWidgetManager, appWidgetId, feedID, pachubeApiKey, updateRate);
				*/
	    		
	            // Make sure we pass back the original appWidgetId
	            Intent resultValue = new Intent();
	            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	            setResult(RESULT_OK, resultValue);
	            finish();
    		}
        }
    };
    
    
    View.OnClickListener cancelOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            finish();
        }
    };
    
    // Write to the SharedPreferences object for this widget
    static void savePrefs(Context context, int appWidgetId, int feedID, String pachubeApiKey, int updateRate, boolean configDone)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_FEEDID_KEY + appWidgetId, feedID);
        prefs.putString(PREF_PACHUBEAPIKEY_KEY + appWidgetId, pachubeApiKey);
        prefs.putInt(PREF_UPDATERATE_KEY + appWidgetId, updateRate);
        prefs.putBoolean(PREF_CONFIG_KEY + appWidgetId, configDone);
        prefs.commit();
    }

    static int loadFeedIDKeyPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int feedID = prefs.getInt(PREF_FEEDID_KEY + appWidgetId, 0);
        
        return feedID;
        /*
        if (prefix != null)
        {
            return prefix;
        }
        else
        {
            return context.getString(R.string.appwidget_prefix_default);
        }
        */
    }
    
    static String loadPachubeApiKeyPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String pachubeApiKey = prefs.getString(PREF_PACHUBEAPIKEY_KEY + appWidgetId, null);
        
        return pachubeApiKey;
        /*
        if (prefix != null)
        {
            return prefix;
        }
        else
        {
            return context.getString(R.string.appwidget_prefix_default);
        }
        */
    }
    
    static int loadUpdateRateKeyPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int updateRate = prefs.getInt(PREF_UPDATERATE_KEY + appWidgetId, -1);
        
        return updateRate;
        /*
        if (prefix != null)
        {
            return prefix;
        }
        else
        {
            return context.getString(R.string.appwidget_prefix_default);
        }
        */
    }

    static boolean loadConfigKeyPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        
        return prefs.getBoolean(PREF_CONFIG_KEY + appWidgetId, false);
        
        /*
        if (prefix != null)
        {
            return prefix;
        }
        else
        {
            return context.getString(R.string.appwidget_prefix_default);
        }
        */
    }
    
    // TODO : DELETE PREFS
    
}
