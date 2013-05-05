package com.thoughtworks.imeeting;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {      
		this.showProgress = false;
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar
		setContentView(R.layout.activity_main);
    }
	
	@Override
	protected void onCalendarServiceReady(){
		Intent intent = new Intent(MainActivity.this, RoomSelectorActivity.class);
		MainActivity.this.startActivity(intent);
		MainActivity.this.finish();
	}

}
