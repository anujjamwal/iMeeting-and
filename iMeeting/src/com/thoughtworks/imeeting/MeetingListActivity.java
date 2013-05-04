package com.thoughtworks.imeeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.services.calendar.model.Event;
import com.thoughtworks.imeeting.tasks.FetchEventListTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MeetingListActivity extends BaseActivity{
	private String calendarId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.getLogger("com.google.api.client").setLevel(Level.ALL);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.meeting_list);
		
		Intent intent = getIntent();
		calendarId = intent.getStringExtra(Keys.CALENDAR_ID);		
    }
	
	@Override
	protected void onCalendarServiceReady(){
		progressDialog.setMessage("Fetching Events ...");
		progressDialog.show();
		new FetchEventListTask(service, this).execute();
	}
	
	public void onEventListFetched(List<Event> events) {
		progressDialog.hide();
		populateEventListView(events);
		presentQuickBookMenu(events);
	}

	private void presentQuickBookMenu(List<Event> events) {
		Event event = events.get(0);
		
		long eventStart = event.getStart().getDateTime().getValue();
		long now = new Date().getTime();
		long timeInterval = (eventStart - now)/(60000);
		Log.v(Keys.TAG, "Time Interval: "+timeInterval);
		Log.v(Keys.TAG, "Event Start: "+eventStart);
		Log.v(Keys.TAG, "Now Start: "+now);
		if(timeInterval > 0) {
			ArrayList<String> menuList = new ArrayList<String>();
			if(timeInterval >= 60) {
				menuList.add("30 Minutes");
				menuList.add("1 Hour");
			} else if(timeInterval > 45 ) {
				menuList.add("30 Minutes");
				menuList.add(timeInterval + " Minutes");
			} else if(timeInterval > 15 ) {
				menuList.add(timeInterval + " Minutes");
			}
			
			final AlertDialog.Builder menuAleart = new AlertDialog.Builder(MeetingListActivity.this);
			
			menuAleart.setTitle("Quick Book");
			menuAleart.setItems(menuList.toArray(new String[0]),new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int item) {
			  switch (item) {
			  case 0:
				  Toast.makeText(MeetingListActivity.this, "function 1 called", Toast.LENGTH_SHORT).show();
			  break;
			  case 1:
				  Toast.makeText(MeetingListActivity.this, "function 2 called", Toast.LENGTH_SHORT).show();
			  break;
			  }
			 }
			});
			AlertDialog menuDrop = menuAleart.create();
			menuDrop.show();
		}
		
	}

	private void populateEventListView(List<Event> events) {
		final ListView listview = (ListView) findViewById(R.id.listView1);
		List<String> eventList = new ArrayList<String>();
		for(Event event: events) {
			eventList.add(event.getSummary()+" "+event.getStart().toString());
		}
		listview.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,  eventList));
	}
	
	

}
