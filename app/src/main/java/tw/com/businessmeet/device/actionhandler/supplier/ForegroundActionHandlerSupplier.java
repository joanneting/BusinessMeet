package tw.com.businessmeet.device.actionhandler.supplier;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import tw.com.businessmeet.device.EmptyMatchListener;
import tw.com.businessmeet.device.MatchListener;
import tw.com.businessmeet.device.actionhandler.ActionHandler;
import tw.com.businessmeet.device.actionhandler.ForegroundFinishActionHandler;
import tw.com.businessmeet.device.actionhandler.ForegroundFoundActionHandler;
import tw.com.businessmeet.device.actionhandler.ForegroundStateChangeActionHandler;
import tw.com.businessmeet.device.enumerate.FindAction;

public class ForegroundActionHandlerSupplier implements ActionHandlerSupplier {
    private final Map<FindAction, ActionHandler> actionHandlerMap = new HashMap<>();

    public ForegroundActionHandlerSupplier(Activity activity) {
        this(activity, new EmptyMatchListener());
    }

    public ForegroundActionHandlerSupplier(Activity activity, MatchListener matchListener) {
        actionHandlerMap.put(FindAction.FOUND, new ForegroundFoundActionHandler(matchListener));
        actionHandlerMap.put(FindAction.CHANGE, new ForegroundStateChangeActionHandler(activity));
        actionHandlerMap.put(FindAction.FINISH, new ForegroundFinishActionHandler(activity));
    }

    @Override
    public ActionHandler get(FindAction action) {
        return actionHandlerMap.get(action);
    }
}
