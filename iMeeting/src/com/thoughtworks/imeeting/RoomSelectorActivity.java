package com.thoughtworks.imeeting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class RoomSelectorActivity extends TabActivity {
	private TabHost tabHost;
	private ScanResultReceiver scanResultReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_selector);
		
		scanResultReceiver = new ScanResultReceiver();
		tabHost = getTabHost();
		
		TabSpec qrSpec = tabHost.newTabSpec("QRScan");
		qrSpec.setIndicator("Scan", null);
		
		Intent intent = new Intent(this, CaptureActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); 
		qrSpec.setContent(intent);
		
		TabSpec roomListSpec = tabHost.newTabSpec("RoomList");
		roomListSpec.setIndicator("Rooms", null);
		Intent intent2 = new Intent(this, RoomListActivity.class);
		roomListSpec.setContent(intent2);
				

		tabHost.addTab(qrSpec);
		tabHost.addTab(roomListSpec);
	}
	
	private class ScanResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String result = intent.getStringExtra(Keys.SCAN_RESULT);
			String result_type = intent.getStringExtra(Keys.SCAN_RESULT_TYPE);
			handleMeetingRoomId(result, result_type);
		}
		
	}
	
	@Override
	public void onResume() {
	  super.onResume();

	  // Register mMessageReceiver to receive messages.
	  LocalBroadcastManager.getInstance(this).registerReceiver(scanResultReceiver,
	      new IntentFilter(Keys.SCAN_RESULT_INTENT));
	  requestScan();
	}
	
	@Override
	protected void onPause() {
	  // Unregister since the activity is not visible
		LocalBroadcastManager.getInstance(this).unregisterReceiver(scanResultReceiver);
		super.onPause();
	} 
	
	final String MEETING_ROOM_ID_REGEX = "i-meeting\\\\(.+)=(.+)";
	private void handleMeetingRoomId(String id, String type) {
		Pattern pattern = Pattern.compile(MEETING_ROOM_ID_REGEX);

        Matcher matcher = pattern.matcher(id);
        if(matcher.matches()) {
            String roomName = matcher.group(1);
            String calendarId = matcher.group(2);
            
            Toast.makeText(getApplicationContext(), ""+roomName, Keys.TOAST_SHORT).show();
            
            Intent intent = new Intent(this, MeetingListActivity.class);
            intent.putExtra(Keys.CALENDAR_ID, calendarId);
            intent.putExtra(Keys.ROOM_NAME, roomName);
            startActivity(intent);
        } else if(!type.equalsIgnoreCase(Keys.ROOM_LIST)) {
         	requestScan();
        }
	}

	protected void requestScan() {
		Intent dataIntent = new Intent(Keys.SCAN_COMMAND_INTENT);
		LocalBroadcastManager.getInstance(this).sendBroadcast(dataIntent);
	}

}
