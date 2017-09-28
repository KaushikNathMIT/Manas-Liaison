package in.projectmanas.manasliaison.backendless_notifications;

import com.backendless.push.BackendlessBroadcastReceiver;
import com.backendless.push.BackendlessPushService;

/**
 * Created by knnat on 9/28/2017.
 */

public class MyPushReceiver extends BackendlessBroadcastReceiver {
    @Override
    public Class<? extends BackendlessPushService> getServiceClass() {
        return MyPushService.class;
    }
}