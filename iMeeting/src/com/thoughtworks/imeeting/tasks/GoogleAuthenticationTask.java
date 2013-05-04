package com.thoughtworks.imeeting.tasks;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.services.calendar.CalendarScopes;
import com.thoughtworks.imeeting.*;

public class GoogleAuthenticationTask extends AsyncTask<String, Integer, String> {
	private Context context;
	private String token;
	
	public GoogleAuthenticationTask(Context context) {
		this.context = context;
	}
	
	@Override
    protected String doInBackground(String... accountName) {
		try {
			token = GoogleAuthUtil.getToken(context, accountName[0], "oauth2:" + CalendarScopes.CALENDAR);
		} catch (GooglePlayServicesAvailabilityException playEx) {
	         GooglePlayServicesUtil.getErrorDialog( playEx.getConnectionStatusCode(),
									                (Activity) context,
									                RequestCodes.NO_PLAY_SERVICE);	             
		} catch (UserRecoverableAuthException userAuthEx) {
			Intent intent = userAuthEx.getIntent();
			((Activity)context).startActivityForResult( intent, RequestCodes.ACCOUNT_PERMISSION);
    	}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
    }

    protected void onPostExecute(String token) {	
    	((BaseActivity)context).handleToken(token);
    }
}
