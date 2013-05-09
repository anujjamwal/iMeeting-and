package com.thoughtworks.imeeting;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.thoughtworks.imeeting.tasks.GoogleAuthenticationTask;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	protected String accountName;
	protected String token;
	protected Calendar service;
	protected ProgressDialog progressDialog;
	protected boolean showProgress = true; 
	
	protected ProgressDialog getProgressDialog(){
		if(progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Authenticating ...");
			progressDialog.setIndeterminate(true);
			progressDialog.hide();
		}
		return progressDialog;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.getLogger("com.google.api.client").setLevel(Level.ALL);
		super.onCreate(savedInstanceState);
				
		SharedPreferences prefs = getApplicationContext().getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
		accountName = prefs.getString(Keys.ACCOUNT_NAME_KEY, null);
		prefs.getString(Keys.DEFAULT_EVENT_NAME_KEY, getResources().getString(R.string.default_event_name));
		token = prefs.getString(Keys.ACCESS_TOKEN_KEY, null);

		if (accountName == null) {
			Log.v(Keys.TAG, "Requesting user to select an account");
			Intent intent = AccountPicker.newChooseAccountIntent(null,
					null, new String[] { "com.google" }, false, null, null,
					null, null);
			startActivityForResult(intent, RequestCodes.ACCOUNT_PICKER);

		} else if (token == null) {
			Log.v(Keys.TAG, "Initiating google token fetch");
			authenticateWithGoogle();

		} else {
			createCalendarService();

		}
	}

	
	protected void onCalendarServiceReady(){
		
	}

	private void authenticateWithGoogle() {
		if(showProgress)  getProgressDialog().show();
		new GoogleAuthenticationTask(this).execute(accountName);
	}	

	public void onMeetingCreated(Event event) {
		hideProgressDialog();
	}

	protected void hideProgressDialog() {
		if(getProgressDialog().isShowing()) getProgressDialog().hide();
	}
	
	@Override
	protected void onActivityResult( final int requestCode, final int resultCode, final Intent data) {
		hideProgressDialog();
		
	     if (requestCode == RequestCodes.ACCOUNT_PICKER && resultCode == RESULT_OK) {
	         accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	         Log.v(Keys.TAG, "User selected the account: "+accountName);
	         SharedPreferences prefs = getApplicationContext().getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
	         prefs.edit().putString(Keys.ACCOUNT_NAME_KEY, accountName).commit();	         
	         authenticateWithGoogle();
	         
	     } else if (requestCode == RequestCodes.ACCOUNT_PERMISSION && resultCode == RESULT_OK) {
	    	 Log.v(Keys.TAG, "User permitted the given: "+accountName);
	    	 authenticateWithGoogle();
	    	 
	     } else if (requestCode == RequestCodes.NO_PLAY_SERVICE && resultCode == RESULT_OK){
	    	 Log.v(Keys.TAG, "No Google Plus found on device ");
	     }
	 }
	
	public void handleToken( String token) {
		Log.v(Keys.TAG, "Handle token request received");
		this.token = token;
		SharedPreferences prefs = getApplicationContext().getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
		prefs.edit().putString(Keys.ACCESS_TOKEN_KEY, token).commit();
		createCalendarService();
	}
	
	public void chooseAccountForAuthentication(Intent intent) {
		Log.v(Keys.TAG, "Requesting account permission");
		startActivityForResult( intent, RequestCodes.ACCOUNT_PERMISSION);
	}
	
	public void invalidateToken() {
		Log.v(Keys.TAG, "Invalidating the token");
		SharedPreferences prefs = getApplicationContext().getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
		prefs.edit().remove(Keys.ACCESS_TOKEN_KEY).commit();
		GoogleAuthUtil.invalidateToken(getApplicationContext(), token);
		authenticateWithGoogle();
	}
	
	private void createCalendarService() {
		GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
		details.setClientId(Credentials.CLIENT_ID);
		details.setAuthUri(Credentials.AUTH_URI);
		details.setTokenUri(Credentials.TOKEN_URI);
		details.setRedirectUris(Arrays.asList(Credentials.REDIRECT_URIS));

		GoogleClientSecrets secrets = new GoogleClientSecrets();
		secrets.setInstalled(details);

		GoogleCredential.Builder builder1 = new GoogleCredential.Builder();
		builder1.setClientSecrets(secrets);
		GoogleCredential credential = builder1.build();
		credential.setAccessToken(token);

		HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
		JsonFactory JSON_FACTORY = new GsonFactory();
		Calendar.Builder builder2 = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential);
		builder2.setApplicationName(getString(R.string.app_name));
		service = builder2.build();
		Log.v(Keys.TAG, "Creating Calendar google service instance");
		onCalendarServiceReady();
	}
	
	@Override
	public void onStop(){
		getProgressDialog().dismiss();
		super.onStop();
	}

}
