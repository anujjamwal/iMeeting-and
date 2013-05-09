package com.thoughtworks.imeeting;

import java.util.Date;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.dm.zbar.android.scanner.ZBarScannerActivity;

public class CaptureActivity extends ZBarScannerActivity { 
	private ScanCommandReceiver scanCommandReceiver;
	private boolean scan = true;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scanCommandReceiver = new ScanCommandReceiver();
	}
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();
        
        int result = 0;
        if (scan) {
        	Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);
        	result = mScanner.scanImage(barcode);
        }

        if (result != 0) {
            SymbolSet syms = mScanner.getResults();
            for (Symbol sym : syms) {
                String symData = sym.getData();
                if (!TextUtils.isEmpty(symData)) {
                	scan = false;
                    Intent dataIntent = new Intent(Keys.SCAN_RESULT_INTENT);
                    dataIntent.putExtra(Keys.SCAN_RESULT, symData);
                    dataIntent.putExtra(Keys.SCAN_RESULT_TYPE, ""+sym.getType());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(dataIntent);
                    break;
                }
            }
        }
    }
	
	private class ScanCommandReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			scan = true;
		}
		
	}
	
	@Override
	public void onResume() {
	  super.onResume();
	  // Register mMessageReceiver to receive messages.
	  LocalBroadcastManager.getInstance(this).registerReceiver(scanCommandReceiver,
	      new IntentFilter(Keys.SCAN_COMMAND_INTENT));
	}
	
	@Override
	protected void onPause() {
	  // Unregister since the activity is not visible
		LocalBroadcastManager.getInstance(this).unregisterReceiver(scanCommandReceiver);
		super.onPause();
	} 

}
