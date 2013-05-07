package com.thoughtworks.imeeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


@SuppressLint("SimpleDateFormat")
public class MeetingBookDialog extends Dialog {
	private static final int MIN_15 = 900000;
	private static final int MIN_30 = 1800000;
	private Context context;
	private TextView text;
	private Button confirmButton;
	private Button cancelButton;
	private long startTime;
	private long endTime;
	private String eventName;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
		text.setText(eventName, TextView.BufferType.NORMAL);
	}

	public MeetingBookDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	public MeetingBookDialog(Context context, long start, long end, String name) {
		super(context);
		this.context = context;
		startTime = start;
		endTime = end;
		eventName = name;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_book);
		
		text = (TextView) findViewById(R.id.meeting_title);		
		text.setText(eventName, TextView.BufferType.NORMAL);

		confirmButton = (Button) findViewById(R.id.confirm_book);
		cancelButton = (Button) findViewById(R.id.cancel_book);
		
		cancelButton.setOnClickListener(clickListener);
		confirmButton.setOnClickListener(clickListener);
		
		long start = startTime/MIN_15*MIN_15;
		String day;
		int today = new Date().getDay();
		long interval = (endTime - startTime)/MIN_30;
		Spinner spinner = (Spinner) findViewById(R.id.slot_spinner);
		List <String> list = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
		while(interval > 0) {
			Date date = new Date(start);
			 day = date.getDay() > today ? "Tomorrow" : "Today"; 
			list.add(day +", "+sdf.format(date));
			start += MIN_30;
			interval --;
		}
		spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list));
	}
	
	
	private android.view.View.OnClickListener clickListener = new  android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dismiss();
			
		}
	};

}
