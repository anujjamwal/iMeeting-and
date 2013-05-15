package com.thoughtworks.imeeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.thoughtworks.imeeting.models.MeetingEvent;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MeetingListAdapter extends BaseAdapter{
	
	Context context;
	private List<MeetingEvent> list;
	public MeetingListAdapter(List<Event> eventList, Context context) {
		this.context = context;
		list = new ArrayList<MeetingEvent>();
		int size = eventList.size();
		DateTime now = new DateTime(new Date()), eventTime;
		DateTime end = new DateTime((now.getValue() + 172800000l)/86400000l * 86400000l);
		for(int i=0;i<size;i++) {
				Event event = eventList.get(i);
				eventTime = event.getStart().getDateTime();
				findAndPopulateFreeSlots(now, eventTime);
				list.add(new MeetingEvent(event));
				now=event.getEnd().getDateTime();
			}
		
		findAndPopulateFreeSlots(now, end);
	}

	private void findAndPopulateFreeSlots(DateTime now, DateTime eventTime) {
		if(now.getValue() + 1800000 < eventTime.getValue()) {
			if(new Date(now.getValue()).getDay() < new Date(eventTime.getValue()).getDay()){
				DateTime midnight = new DateTime(now.getValue() / 86400000l * 86400000l);
				list.add(new MeetingEvent(now, midnight));
				list.add(new MeetingEvent(midnight, eventTime));
			} else {
				list.add(new MeetingEvent(now, eventTime));
			}			
		}
	}

	public List<MeetingEvent> getList() {
		return list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
        MeetingEvent holder = list.get(position);
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.meeting_list_item, parent, false);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");

        ((TextView)row.findViewById(R.id.meeting_title)).setText(holder.getTitle());
        ((TextView)row.findViewById(R.id.start_time)).setText((sdf.format(new Date(holder.getStartTime().getValue()))));
        ((TextView)row.findViewById(R.id.end_time)).setText(sdf.format(new Date(holder.getEndTime().getValue())));
        ((TextView)row.findViewById(R.id.meeting_date)).setText((sdf2.format(new Date(holder.getStartTime().getValue()))));
        ((TextView)row.findViewById(R.id.meeting_creator)).setText(holder.getCreator());
        
        return row;
	}

}
