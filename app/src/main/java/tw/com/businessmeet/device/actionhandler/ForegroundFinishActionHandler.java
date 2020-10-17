package tw.com.businessmeet.device.actionhandler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityCompat;

import tw.com.businessmeet.RequestCode;

public class ForegroundFinishActionHandler extends AbstractFinishActionHandler {
    private final Activity activity;

    public ForegroundFinishActionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void handle(Context context, Intent intent) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RequestCode.REQUEST_LOCATION
        );
    }
}
