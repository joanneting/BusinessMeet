package tw.com.businessmeet.device.beacon;

import android.os.Parcel;

import androidx.annotation.NonNull;

import org.altbeacon.beacon.Beacon;

import tw.com.businessmeet.device.FoundedDeviceDetail;

public class BeaconDeviceDetail implements FoundedDeviceDetail {
    private final Beacon beacon;

    public BeaconDeviceDetail(Beacon beacon) {
        this.beacon = beacon;
    }

    protected BeaconDeviceDetail(Parcel in) {
        beacon = in.readParcelable(Beacon.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(beacon, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BeaconDeviceDetail> CREATOR = new Creator<BeaconDeviceDetail>() {
        @Override
        public BeaconDeviceDetail createFromParcel(Parcel in) {
            return new BeaconDeviceDetail(in);
        }

        @Override
        public BeaconDeviceDetail[] newArray(int size) {
            return new BeaconDeviceDetail[size];
        }
    };

    @Override
    @NonNull
    public String getIdentifier() {
        return beacon.getId1().toString();
    }

    @Override
    public double getDistance() {
        return beacon.getDistance();
    }

    @Override
    public String getBluetoothAddress() {
        return beacon.getBluetoothAddress();
    }
}
