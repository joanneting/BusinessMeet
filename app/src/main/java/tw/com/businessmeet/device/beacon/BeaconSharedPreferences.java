package tw.com.businessmeet.device.beacon;

import android.content.Context;
import android.content.SharedPreferences;

public class BeaconSharedPreferences {
    private static final String KEY_IDENTIFIER = "identifier";

    private final SharedPreferences sharedPreferences;

    public BeaconSharedPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences("Beacon", Context.MODE_PRIVATE);
    }

    public String getIdentifier() {
        return sharedPreferences.getString(KEY_IDENTIFIER, null);
    }

    public void setIdentifier(String identifier) {
        sharedPreferences.edit().putString(KEY_IDENTIFIER, identifier).apply();
    }
}
