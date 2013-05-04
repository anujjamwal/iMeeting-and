package com.thoughtworks.imeeting;

import java.util.Arrays;

import com.google.android.gms.common.AccountPicker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.thoughtworks.imeeting.tasks.GoogleAuthenticationTask;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	protected String accountName;
	protected String token;
	protected SharedPreferences prefs;
	protected Calendar service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		prefs = this.getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE);
		accountName = prefs.getString(Keys.ACCOUNT_NAME_KEY, null);
		token = prefs.getString(Keys.ACCESS_TOKEN_KEY, null);
		
		if (accountName == null) {
			Log.v(Keys.TAG, "Requesting user to select an account");
			Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
			         false, null, null, null, null);
			startActivityForResult(intent, RequestCodes.ACCOUNT_PICKER);
			
		} else if(token == null) {
			Log.v(Keys.TAG, "Initiating google token fetch");
			new GoogleAuthenticationTask(this).execute(accountName);
			
		} else {
			createCalendarService();
			
		}
	}
	
	@Override
	protected void onActivityResult( final int requestCode, final int resultCode,
	         final Intent data) {
	     if (requestCode == RequestCodes.ACCOUNT_PICKER && resultCode == RESULT_OK) {
	         accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	         Log.v(Keys.TAG, "User selected the account: "+accountName);
	         prefs.edit().putString(Keys.ACCOUNT_NAME_KEY, accountName).commit();	         
	         new GoogleAuthenticationTask(this).execute(accountName);
	         
	     } else if (requestCode == RequestCodes.ACCOUNT_PERMISSION && resultCode == RESULT_OK) {
	    	 Log.v(Keys.TAG, "User permitted the given: "+accountName);
	    	 new GoogleAuthenticationTask(this).execute(accountName);
	    	 
	     } else if (requestCode == RequestCodes.NO_PLAY_SERVICE && resultCode == RESULT_OK){
	    	 Log.v(Keys.TAG, "No Google Plus found on device ");
	     }
	 }
	
	public void handleToken( String token) {
		Log.v(Keys.TAG, "Handle token request received");
		this.token = token;
		prefs.edit().putString(Keys.ACCESS_TOKEN_KEY, token);
		createCalendarService();
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
	}

}
