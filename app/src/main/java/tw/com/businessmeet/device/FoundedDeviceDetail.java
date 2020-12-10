package tw.com.businessmeet.device;

import android.os.Parcelable;

import androidx.annotation.NonNull;

public interface FoundedDeviceDetail extends Parcelable/*序列化*/ {
    @NonNull
    String getIdentifier();

    double getDistance();

    String getBluetoothAddress();
}
