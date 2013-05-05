package com.thoughtworks.imeeting;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.dm.zbar.android.scanner.ZBarScannerActivity;

public class CaptureActivity extends ZBarScannerActivity {
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();

        Image barcode = new Image(size.width, size.height, "Y800");
        barcode.setData(data);

        int result = mScanner.scanImage(barcode);

        if (result != 0) {
            SymbolSet syms = mScanner.getResults();
            for (Symbol sym : syms) {
                String symData = sym.getData();
                if (!TextUtils.isEmpty(symData)) {
                    Intent dataIntent = new Intent(Keys.SCAN_RESULT_INTENT);
                    dataIntent.putExtra(Keys.SCAN_RESULT, symData);
                    dataIntent.putExtra(Keys.SCAN_RESULT_TYPE, sym.getType());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(dataIntent);
                    break;
                }
            }
        }
    }

}
