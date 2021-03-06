package com.thoughtworks.imeeting.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.thoughtworks.imeeting.Keys;
import com.thoughtworks.imeeting.MeetingListActivity;

public class FetchEventListTask extends AsyncTask<Void, Integer, List<Event>> {
	private Context context;
	private String calendarId;
	private Calendar service;
	private boolean unauthorised;
	
	public FetchEventListTask(Calendar service, Context context) {
		 this( service, context, Keys.SELF_CALENDAR_ID);
	}
	
	public FetchEventListTask(Calendar service, Context context, String calendarId) {
		this.service = service;
		this.context = context;
		this.calendarId = calendarId;
	}
	
	@Override
    protected List<Event> doInBackground(Void... params) {
		String pageToken = null;
		List<Event> items = new ArrayList<Event>();
		try {
			do {
			  Events events;
				java.util.Calendar cal = java.util.Calendar.getInstance();			
				Date now = cal.getTime();
				cal.add(java.util.Calendar.DATE, 2);
				Date max = cal.getTime();
				events = service
						.events()
						.list(calendarId)
						.setTimeMin(new DateTime(now)).setSingleEvents(true)
						.setTimeMax(new DateTime(max)).setOrderBy("startTime")
						.setTimeZone(cal.getTimeZone().toString())
						.setPageToken(pageToken).execute();
			  if(events != null && events.getItems() != null){
				  items.addAll(events.getItems());
			  }
			  pageToken = events.getNextPageToken();
			} while (pageToken != null);
			
			return items;
		} catch(GoogleJsonResponseException e){
			e.printStackTrace();
			if(e.getStatusCode() == 401) unauthorised = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
    }

    protected void onPostExecute(List<Event> events) {	
    	if(unauthorised) {
    		((com.thoughtworks.imeeting.BaseActivity)context).invalidateToken();
    	} else {
    		((MeetingListActivity)context).onEventListFetched(events);
    	}
    }
}
