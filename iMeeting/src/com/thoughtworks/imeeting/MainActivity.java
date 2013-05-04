package com.thoughtworks.imeeting;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.Bundle;




public class MainActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.getLogger("com.google.api.client").setLevel(Level.ALL);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
    }

//	class GetCalendarDetails extends AsyncTask<Calendar, Void, Void> {
//
//		// Declare UI elemnts here
//
//		        @Override
//		        protected void onPreExecute() {
//		            super.onPreExecute();
//		              // show ProgressDialog and initilize Ui elemnts
//		        }
//		       
//		        @Override
//				protected Void doInBackground(Calendar... params) {
//
//		        	String pageToken = null;
//		    		CalendarList calendarList;
//		    		try {
//		    		do {
//		    		  calendarList = params[0].calendarList().list().setPageToken(pageToken).execute();
//		    		  List<CalendarListEntry> items = calendarList.getItems();
//
//		    		  if(items != null) {
//		    		    for (CalendarListEntry calendarListEntry : items) {
//		    		      System.out.println(calendarListEntry.getSummary());
//		    		    }
//		    		  }
//		    		  pageToken = calendarList.getNextPageToken();
//		    		} while (pageToken != null);
//		    		}catch(GoogleJsonResponseException e){
//		    			System.out.println("Invalidating token "+token);
//		    			GoogleAuthUtil.invalidateToken(getApplicationContext(), token);
//		    		}catch(Exception e) {
//		    			e.printStackTrace();
//		    		}
//		    		return null;
//		        }
//		       
//		        protected void onPostExecute() {
//		            // Update UI when doInBackground exection completes
//		        }
//
//
//		    }  
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
