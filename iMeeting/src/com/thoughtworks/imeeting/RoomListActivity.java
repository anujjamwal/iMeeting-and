package com.thoughtworks.imeeting;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RoomListActivity extends Activity {
	private String[] rooms;
	private String[] room_ids;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_list); 
		
		rooms = getResources().getStringArray(R.array.gurgaon_rooms);
		room_ids = getResources().getStringArray(R.array.gurgaon_rooms_id);
		listView = (ListView) findViewById(R.id.roomListView);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,  rooms));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent dataIntent = new Intent(Keys.SCAN_RESULT_INTENT);
                dataIntent.putExtra(Keys.SCAN_RESULT, room_ids[arg2]);
                dataIntent.putExtra(Keys.SCAN_RESULT_TYPE, Keys.ROOM_LIST);
                LocalBroadcastManager.getInstance(RoomListActivity.this).sendBroadcast(dataIntent);				
			}
		});
	}

}
