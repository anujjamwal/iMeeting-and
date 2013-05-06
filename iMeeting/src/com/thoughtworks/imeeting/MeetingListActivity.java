package com.thoughtworks.imeeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.services.calendar.model.Event;
import com.thoughtworks.imeeting.MeetingListAdapter;
import com.thoughtworks.imeeting.tasks.CreateEventTask;
import com.thoughtworks.imeeting.tasks.FetchEventListTask;

public class MeetingListActivity extends BaseActivity{
	private String calendarId;
	private String roomName;
	private Long[] duration = new Long[2];
	private AlertDialog quickBookDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
				
		Intent intent = getIntent();
		calendarId = intent.getStringExtra(Keys.CALENDAR_ID);
		roomName = intent.getStringExtra(Keys.ROOM_NAME);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meeting_list);

    }
	
	@Override
	protected void onCalendarServiceReady(){
		getProgressDialog().setMessage("Fetching Events ...");
		getProgressDialog().show();
		new FetchEventListTask(service, this, calendarId).execute();
	}
	
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
	    	case R.id.action_settings:
	    		Toast.makeText(getApplicationContext(), "Open Settings", Keys.TOAST_SHORT).show();
	    		Intent intent = new Intent(this, SettingsActivity.class);
	    		startActivity(intent);
	    		break;
    	}
  		return true;
  	}
	
	@Override 
	public void onMeetingCreated() {
		super.onMeetingCreated();
		onCalendarServiceReady();
	}
	
	public void onEventListFetched(List<Event> events) {
		getProgressDialog().hide();
		populateEventListView(events);
		if(!calendarId.equalsIgnoreCase(Keys.SELF_CALENDAR_ID))
			presentQuickBookMenu(events);
	}

	private void presentQuickBookMenu(List<Event> events) {
		long eventStart = new Date().getTime() + 172800000L;
		if(events.size()>0){
			eventStart = events.get(0).getStart().getDateTime().getValue();
		}
		
		long now = new Date().getTime();
		long timeInterval = (eventStart - now)/(60000);
		Log.v(Keys.TAG, "Time Interval: "+timeInterval);
		Log.v(Keys.TAG, "Event Start: "+eventStart);
		Log.v(Keys.TAG, "Now Start: "+now);
		if(timeInterval > 15) {
			ArrayList<String> menuList = new ArrayList<String>();
			if(timeInterval >= 60) {
				menuList.add("30 Minutes");
				menuList.add("1 Hour");
				duration[0] = 1800000L;
				duration[1] = 3600000L;
			} else if(timeInterval > 45 ) {
				menuList.add("30 Minutes");
				menuList.add(timeInterval + " Minutes");
				duration[0] = 1800000L;
				duration[1] = timeInterval*60000;
			} else {
				menuList.add(timeInterval + " Minutes");
				duration[0] = timeInterval*60000;
			}
			
			quickBookDialog = new AlertDialog.Builder(MeetingListActivity.this)
				.setTitle("Quick Book")
				.setItems(menuList.toArray(new String[0]),new DialogInterface.OnClickListener() {
					 public void onClick(DialogInterface dialog, int item) {
						 dialog.dismiss();
						 Date startTime = new Date(new Date().getTime() + 120000);
						  switch (item) {
						  case 0:
							  createEvent(startTime, duration[0] - 120000);
							  Toast.makeText(MeetingListActivity.this, "Quick Book "+roomName+" for "+duration[0]/60000+" min", Keys.TOAST_SHORT).show();
						  break;
						  case 1:
							  createEvent(startTime, duration[1] - 120000);
							  Toast.makeText(MeetingListActivity.this, "Quick Book "+roomName+" for "+duration[1]/60000+" min", Keys.TOAST_SHORT).show();
						  break;
						  }
					 }
				}).create();
			quickBookDialog.show();
		}
		
	}

	private void populateEventListView(List<Event> events) {
		final ListView listview = (ListView) findViewById(R.id.listView1);
//		List<String> eventList = new ArrayList<String>();
//		for(Event event: events) {
//			eventList.add(event.getSummary()+" "+event.getStart().toString());
//		}
		listview.setAdapter(new MeetingListAdapter(events, this));
	}
	
	private void createEvent(Date startTime, Long duration) {
		String name = prefs.getString(Keys.DEFAULT_EVENT_NAME_KEY, getResources().getString(R.string.default_event_name));
		getProgressDialog().setMessage("Booking "+roomName+"...");
		getProgressDialog().show();
		Date endTime = new Date(new Date().getTime() + duration);
		new CreateEventTask(service, MeetingListActivity.this, calendarId).execute(name, roomName, startTime, endTime);
	}
	
}
