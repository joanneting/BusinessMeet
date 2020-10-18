package tw.com.businessmeet.device;

import tw.com.businessmeet.device.actionhandler.supplier.ActionHandlerSupplier;

public interface DeviceFinder {
    String EXTRA_FOUNDED_DEVICE_DETAIL = "founded_device_detail";

    default void find(ActionHandlerSupplier actionHandlerSupplier) {
        find(actionHandlerSupplier, null);
    }

    void find(ActionHandlerSupplier actionHandlerSupplier, ActionListener actionListener);

    void cancel();
}
