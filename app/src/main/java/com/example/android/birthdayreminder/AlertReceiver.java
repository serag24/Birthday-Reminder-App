package com.example.android.birthdayreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    public static final String LAST_NAME_KEY = "last_name";
    public static final String NOT_ID1_KEY = "not_id1_key";
    public static final String NOT_ID2_KEY = "not_id2_key";

    public static final String notificationTitle1 = "'s birthday will be in 7 days!";
    //TODO 1 Maybe add some emojis
    public static final String notificationMessage1 = "Get ready for some cake!";

    public static final String notificationTitle2 = "'s birthday is today!";
    public static final String notificationMessage2 = "Don't forget to send them your wishes!";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(AlertReceiver.LAST_NAME_KEY)) {
            NotificationHelper mNotificationHelper = new NotificationHelper(context);
            String lastName = intent.getStringExtra(AlertReceiver.LAST_NAME_KEY);

            if (intent.hasExtra(AlertReceiver.NOT_ID1_KEY)) {

                int notificationId1 = Integer.parseInt(intent.getStringExtra(AlertReceiver.NOT_ID1_KEY));

                NotificationCompat.Builder nb1 = mNotificationHelper.getNotificationBuilder(
                        lastName + notificationTitle1,  notificationMessage1
                );
                mNotificationHelper.getNotificationManager().notify(notificationId1, nb1.build());
            } else if (intent.hasExtra(AlertReceiver.NOT_ID2_KEY)) {

                int notificationId2 = Integer.parseInt(intent.getStringExtra(AlertReceiver.NOT_ID2_KEY));

                NotificationCompat.Builder nb2 = mNotificationHelper.getNotificationBuilder(
                        lastName + notificationTitle2,  notificationMessage2
                );
                mNotificationHelper.getNotificationManager().notify(notificationId2, nb2.build());
            }


        }
    }
}

