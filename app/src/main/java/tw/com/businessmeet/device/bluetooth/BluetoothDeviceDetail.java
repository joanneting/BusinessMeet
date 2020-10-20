package tw.com.businessmeet.device.bluetooth;

import android.content.Intent;
import android.os.Parcel;

import androidx.annotation.NonNull;

import tw.com.businessmeet.device.FoundedDeviceDetail;
import tw.com.businessmeet.helper.BluetoothHelper;

public class BluetoothDeviceDetail implements FoundedDeviceDetail {
    private final Intent intent;
    private final String identifier;

    public BluetoothDeviceDetail(Intent intent) {
        this.intent = intent;
        this.identifier = BluetoothHelper.getBluetoothAddress(intent);
    }

    protected BluetoothDeviceDetail(Parcel in) {
        intent = in.readParcelable(Intent.class.getClassLoader());
        identifier = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(intent, flags);
        dest.writeString(identifier);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BluetoothDeviceDetail> CREATOR = new Creator<BluetoothDeviceDetail>() {
        @Override
        public BluetoothDeviceDetail createFromParcel(Parcel in) {
            return new BluetoothDeviceDetail(in);
        }

        @Override
        public BluetoothDeviceDetail[] newArray(int size) {
            return new BluetoothDeviceDetail[size];
        }
    };

    @Override
    @NonNull
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public double getDistance() {
        return BluetoothHelper.getDistance(intent);
    }

    @Override
    public String getBluetoothAddress() {
        return BluetoothHelper.getBluetoothAddress(intent);
    }
}
