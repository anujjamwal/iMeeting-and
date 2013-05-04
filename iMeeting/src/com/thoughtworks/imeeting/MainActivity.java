package com.thoughtworks.imeeting;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.thoughtworks.imeeting.tasks.GoogleAuthenticationTask;



public class MainActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.getLogger("com.google.api.client").setLevel(Level.ALL);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
    }

	class GetCalendarDetails extends AsyncTask<Calendar, Void, Void> {

		// Declare UI elemnts here

		        @Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		              // show ProgressDialog and initilize Ui elemnts
		        }
		       
		        @Override
				protected Void doInBackground(Calendar... params) {

		        	String pageToken = null;
		    		CalendarList calendarList;
		    		try {
		    		do {
		    		  calendarList = params[0].calendarList().list().setPageToken(pageToken).execute();
		    		  List<CalendarListEntry> items = calendarList.getItems();

		    		  if(items != null) {
		    		    for (CalendarListEntry calendarListEntry : items) {
		    		      System.out.println(calendarListEntry.getSummary());
		    		    }
		    		  }
		    		  pageToken = calendarList.getNextPageToken();
		    		} while (pageToken != null);
		    		}catch(GoogleJsonResponseException e){
		    			System.out.println("Invalidating token "+token);
		    			GoogleAuthUtil.invalidateToken(getApplicationContext(), token);
		    		}catch(Exception e) {
		    			e.printStackTrace();
		    		}
		    		return null;
		        }
		       
		        protected void onPostExecute() {
		            // Update UI when doInBackground exection completes
		        }


		    }  
//	
//	private class DownloadFilesTask extends AsyncTask<String, Integer, String> {
//		@Override
//	     protected String doInBackground(String... accountName) {
//			try {
//				token = GoogleAuthUtil.getToken(getApplicationContext(), accountName[0], "oauth2:" + CalendarScopes.CALENDAR);
//			} catch (GooglePlayServicesAvailabilityException playEx) {
//		         GooglePlayServicesUtil.getErrorDialog(
//		                 playEx.getConnectionStatusCode(),
//		                 (Activity)getApplicationContext(),
//		                 RequestCodes.NO_PLAY_SERVICE);
//		             
//            } catch (UserRecoverableAuthException userAuthEx) {
//              // Start the user recoverable action using the intent returned by
//              // getIntent()
//        	   Intent intent = userAuthEx.getIntent();
//			   startActivityForResult(
//                      intent,
//                      RequestCodes.ACCOUNT_PERMISSION);
//            }
//			catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (GoogleAuthException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return token;
//	     }
//
//	     protected void onProgressUpdate(Integer... progress) {
//	         
//	     }
//
//	     protected void onPostExecute(String token) {
//	    	 System.out.println("In Post Execute");
//	    	 System.out.println("Token "+token);
//	    	 prefs.edit().putString(Keys.ACCESS_TOKEN_KEY, token);
//	         try {
//				tryToken(token);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	     }
//	 }
	
	
}
