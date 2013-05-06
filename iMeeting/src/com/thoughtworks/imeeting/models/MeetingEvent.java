package com.thoughtworks.imeeting.models;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

public class MeetingEvent {
	private DateTime startTime;
	private DateTime endTime;
	private String title;
	private boolean emptySlot;
	private String creator;
	
	public MeetingEvent(DateTime startTime, DateTime endTime) {
		this(startTime, endTime, "Available", true, null);
	}
	
	public String getCreator() {
		return creator;
	}

	public boolean isEmptySlot() {
		return emptySlot;
	}

	public MeetingEvent(DateTime startTime, DateTime endTime, String title,
			boolean emptySlot, String creator) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.emptySlot = emptySlot;
		this.creator = creator;
	}

	public MeetingEvent(Event event) {
		this(event.getStart().getDateTime(), event.getEnd().getDateTime(), event.getSummary(), false, event.getCreator().getDisplayName());
	}
	
	public DateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	public DateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
