package tw.com.businessmeet.device.actionhandler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import tw.com.businessmeet.device.ActionListener;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.actionhandler.supplier.ActionHandlerSupplier;

public class BackgroundFinishActionHandler extends AbstractFinishActionHandler {
    private static final Handler HANDLER = new Handler();
    private final DeviceFinder finder;
    private final ActionHandlerSupplier supplier;
    private final ActionListener actionListener;

    public BackgroundFinishActionHandler(DeviceFinder finder, ActionHandlerSupplier supplier) {
        this(finder, supplier, null);
    }

    public BackgroundFinishActionHandler(DeviceFinder finder, ActionHandlerSupplier supplier, ActionListener actionListener) {
        this.finder = finder;
        this.supplier = supplier;
        this.actionListener = actionListener;
    }

    @Override
    public void handle(Context context, Intent intent) {
        HANDLER.postDelayed(() -> finder.find(supplier, actionListener), 100_000);
    }
}
