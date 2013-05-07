package com.thoughtworks.imeeting.tasks;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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
			return token;
		} catch (GooglePlayServicesAvailabilityException playEx) {
			Log.v(Keys.TAG, "No Google Play found");
	         GooglePlayServicesUtil.getErrorDialog( playEx.getConnectionStatusCode(),
									                (Activity) context,
									                RequestCodes.NO_PLAY_SERVICE);	             
		} catch (UserRecoverableAuthException userAuthEx) {
			Log.v(Keys.TAG, "User Auth Exception");
			Intent intent = userAuthEx.getIntent();
			((BaseActivity)context).chooseAccountForAuthentication(intent);
    	}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    protected void onPostExecute(String token) {	
    	if(token!=null) ((BaseActivity)context).handleToken(token);
    }
}
