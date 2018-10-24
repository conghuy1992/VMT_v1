package com.monamedia.vmt.controller.FCM;

import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.model.NotificationDto;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null)
            return;
        Log.d(TAG, "onMessageReceived");
        Log.e(TAG, "From: " + remoteMessage.getFrom());//4103456745464mka

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            scheduleJob();
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
//            scheduleJob();
            Map<String, String> data = remoteMessage.getData();
            if (data == null) return;
            NotificationDto notificationDto = new NotificationDto();
            if (data.containsKey("link"))
                notificationDto.link = (String) data.get("link").trim();
            if (data.containsKey("image"))
                notificationDto.image = (String) data.get("image").trim();
            if (data.containsKey("title"))
                notificationDto.title = (String) data.get("title").trim();
            if (data.containsKey("message"))
                notificationDto.message = (String) data.get("message").trim();

            Utils.getBitmap(MyFirebaseMessagingService.this, notificationDto);
        }
    }

    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }
}
