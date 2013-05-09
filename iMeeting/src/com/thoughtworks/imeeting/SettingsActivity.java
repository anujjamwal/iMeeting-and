package com.thoughtworks.imeeting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private EditText accountName;
	private EditText eventName;

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // use an inflater to populate the ActionBar with items
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
		
	@Override
  	public boolean onOptionsItemSelected(MenuItem item){
    	// same as using a normal menu
    	switch(item.getItemId()) {
//	    	case R.id.action_settings:
//	    		Toast.makeText(getApplicationContext(), "Open Settings", Keys.TOAST_SHORT).show();
//	    		break;
    	}
  		return true;
  	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		accountName = (EditText) findViewById(R.id.account_name);
		eventName = (EditText) findViewById(R.id.default_event_name);
		SharedPreferences prefs = this.getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
		
		accountName.setText(prefs.getString(Keys.ACCOUNT_NAME_KEY, null), TextView.BufferType.NORMAL);
		eventName.setText(prefs.getString(Keys.DEFAULT_EVENT_NAME_KEY, getResources().getString(R.string.default_event_name)), TextView.BufferType.NORMAL);
		
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 new AlertDialog.Builder(SettingsActivity.this)
				 .setTitle("Do you want to clear account ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						SharedPreferences prefs = SettingsActivity.this.getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
						prefs.edit().remove(Keys.ACCOUNT_NAME_KEY).commit();
						Toast.makeText(SettingsActivity.this, "Account Cleared", Keys.TOAST_SHORT).show();
						SettingsActivity.this.finish();
					}
				}).setNegativeButton("No", null).create().show();
			}
		});
		
		findViewById(R.id.save_settings).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String name = eventName.getText().toString();
				if(name == null || name.equals("") || name.matches("^\\s+")) {
					Toast.makeText(SettingsActivity.this, "Invalid Event Name", Keys.TOAST_MEDIUM).show();
					return;
				}
				SharedPreferences prefs = SettingsActivity.this.getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
				prefs.edit().putString(Keys.DEFAULT_EVENT_NAME_KEY, name).commit();
				Toast.makeText(SettingsActivity.this, "Settings Updated", Keys.TOAST_SHORT).show();
				SettingsActivity.this.finish();
			}
		});
	}
	
}
