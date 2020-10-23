package tw.com.businessmeet.device.actionhandler.supplier;

import tw.com.businessmeet.device.actionhandler.ActionHandler;
import tw.com.businessmeet.device.enumerate.FindAction;

public interface ActionHandlerSupplier {
    ActionHandler get(FindAction action);
}
