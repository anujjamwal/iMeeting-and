package com.thoughtworks.imeeting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private EditText accountName;
	private EditText eventName;
	private SharedPreferences prefs;
	
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
		prefs = this.getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
		
		accountName.setText(prefs.getString(Keys.ACCOUNT_NAME_KEY, null), TextView.BufferType.NORMAL);
		eventName.setText(prefs.getString(Keys.DEFAULT_EVENT_NAME_KEY, null), TextView.BufferType.NORMAL);
		
		findViewById(R.id.save_settings).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String name = eventName.getText().toString();
				if(name == null || name.equals("") || name.matches("^\\s+")) {
					Toast.makeText(SettingsActivity.this, "Invalid Event Name", Keys.TOAST_MEDIUM).show();
					return;
				}
				prefs.edit().putString(Keys.DEFAULT_EVENT_NAME_KEY, name).commit();
				Toast.makeText(SettingsActivity.this, "Settings Updated", Keys.TOAST_SHORT).show();
				SettingsActivity.this.finish();
			}
		});
	}
	
}
