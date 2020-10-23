package tw.com.businessmeet.device.actionhandler.supplier;

import java.util.HashMap;
import java.util.Map;

import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.device.ActionListener;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.actionhandler.ActionHandler;
import tw.com.businessmeet.device.actionhandler.BackgroundFinishActionHandler;
import tw.com.businessmeet.device.actionhandler.BackgroundFoundActionHandler;
import tw.com.businessmeet.device.actionhandler.BackgroundStateChangeActionHandler;
import tw.com.businessmeet.device.enumerate.FindAction;

public class BackgroundActionHandlerSupplier implements ActionHandlerSupplier {
    private final Map<FindAction, ActionHandler> actionHandlerMap = new HashMap<>();

    public BackgroundActionHandlerSupplier(
            NotificationService notificationService,
            DeviceFinder finder
    ) {
        this(notificationService, finder, null);
    }

    public BackgroundActionHandlerSupplier(
            NotificationService notificationService,
            DeviceFinder finder,
            ActionListener actionListener
    ) {
        actionHandlerMap.put(FindAction.FOUND, new BackgroundFoundActionHandler(notificationService));
        actionHandlerMap.put(FindAction.CHANGE, new BackgroundStateChangeActionHandler());
        actionHandlerMap.put(FindAction.FINISH, new BackgroundFinishActionHandler(finder, this, actionListener));
    }

    @Override
    public ActionHandler get(FindAction action) {
        return actionHandlerMap.get(action);
    }
}
