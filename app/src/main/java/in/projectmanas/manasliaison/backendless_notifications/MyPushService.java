package in.projectmanas.manasliaison.backendless_notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.backendless.push.BackendlessPushService;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.activities.AboutActivity;
import in.projectmanas.manasliaison.activities.HomeActivity;
import in.projectmanas.manasliaison.activities.InterviewActivity;
import in.projectmanas.manasliaison.activities.LoginActivity;
import in.projectmanas.manasliaison.activities.OrientationActivity;
import in.projectmanas.manasliaison.activities.ProfileActivity;
import in.projectmanas.manasliaison.activities.SupportActivity;
import in.projectmanas.manasliaison.activities.TaskPhaseActivity;

/**
 * Created by knnat on 9/28/2017.
 */

public class MyPushService extends BackendlessPushService {
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onRegistered(Context context, String registrationId) {
        //Toast.makeText(context, "device registered" + registrationId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnregistered(Context context, Boolean unregistered) {
        Toast.makeText(context,
                "device unregistered",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("Intent Keys", String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
        }
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//Here you put the Activity that you want to open when you click the Notification

//(and you can pass some Bundle/Extra if you want to add information about the Notification)

        Intent notificationIntent;
        String activityName = intent.getStringExtra("android-content-sound");
        if (AboutActivity.class.getName().contains(activityName))
            notificationIntent = new Intent(context, AboutActivity.class);
        else if (HomeActivity.class.getName().contains(activityName))
            notificationIntent = new Intent(context, HomeActivity.class);
        else if (InterviewActivity.class.getName().contains(activityName))
            notificationIntent = new Intent(context, InterviewActivity.class);
        else if (OrientationActivity.class.getName().contains(activityName))
            notificationIntent = new Intent(context, OrientationActivity.class);
        else if (SupportActivity.class.getName().contains(activityName))
            notificationIntent = new Intent(context, SupportActivity.class);
        else if (TaskPhaseActivity.class.getName().contains(activityName))
            notificationIntent = new Intent(context, TaskPhaseActivity.class);
        else if (ProfileActivity.class.getName().contains(activityName))
            notificationIntent = new Intent(context, ProfileActivity.class);
        else notificationIntent = new Intent(context, LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

//Custom notification

        NotificationCompat.Builder notification = (NotificationCompat.Builder) new NotificationCompat.Builder(context)

                .setContentTitle(intent.getStringExtra("android-content-title"))
                .setContentText(message)
                .setSmallIcon(R.drawable.pm_logo_50x50)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        notification.setContentIntent(contentIntent);

//Dismiss notification when click on it

        notification.setAutoCancel(true);

        mNotificationManager.notify(NOTIFICATION_ID, notification.build());

// super.onMessage(context, intent);


        return false;
    }

    @Override
    public void onError(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
