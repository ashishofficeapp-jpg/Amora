package com.meetlive.app.push_notification;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meetlive.app.R;
import com.meetlive.app.activity.InboxDetails;
import com.meetlive.app.activity.IncomingCallScreen;
import com.meetlive.app.activity.MainActivity;
import com.meetlive.app.utils.NotificationCallBack;
import com.meetlive.app.utils.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.meetlive.app.utils.SessionManager.PROFILE_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    private static NotificationCallBack notificationCallBack;

    public static void setNotificationCallBack(NotificationCallBack paramnotificationCallBack) {
        notificationCallBack = paramnotificationCallBack;
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    void chatNotification(RemoteMessage remoteMessage) {
        String sented = remoteMessage.getData().get("sented");
        String senderId = remoteMessage.getData().get("user");

        HashMap<String, String> user = new SessionManager(this).getUserDetails();
        String currentReceiver = new SessionManager(this).getCurrentReceiver();
        String userId = user.get(PROFILE_ID);

        if (sented != null && sented.equals(userId)) {
            if (!currentReceiver.equals(senderId)) {

                if (isAppBackground()) {
                    sendChatNotification(remoteMessage);
                }

                // Send notification on contact list callback
                if (notificationCallBack != null) {
                    notificationCallBack.onGetMessage(remoteMessage);
                }
            }
        }
    }

    public static boolean isAppBackground() {
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        return myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
    }

    private void sendChatNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent intent = new Intent(this, InboxDetails.class);
        intent.putExtra("receiver_id", user);
        intent.putExtra("receiver_name", title);
        intent.putExtra("receiver_image", "demo");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_ONE_SHOT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());
    }


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        chatNotification(remoteMessage);

        try {
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());


                Map<String, String> data = remoteMessage.getData();
                JSONObject object = new JSONObject(data.get("data"));
                String title = object.getString("title");

                if (title.equals("request_accepted")) {
                    String user_name = "00##00";
                    Intent myIntent = new Intent("KAL-JOINBROADCALL");
                    myIntent.putExtra("action", "forbroadcallrequest");
                    myIntent.putExtra("username", user_name);
                    this.sendBroadcast(myIntent);
                    return;
                }

                if (title.equals("calljoin")) {
                    String user_name = object.getString("user_name");
                    String sender_id = object.getString("sender_id");
                    Intent myIntent = new Intent("KAL-JOINBROADCALL");
                    myIntent.putExtra("action", "forbroadcallrequest");
                    myIntent.putExtra("username", user_name);
                    myIntent.putExtra("senderid", sender_id);
                    this.sendBroadcast(myIntent);
                    return;
                }

                if (title.equals("Audio call")) {
                    String token = object.getString("message");
                    String caller_name = object.getString("user_name");
                    String caller_image = object.getString("profile_image");
                    String unique_id = object.getString("unique_id");
                    String outgoing_time = object.getString("outgoing_time");
                    String user_points = object.getString("user_points");
                    String receiver_audio_callRate = object.getString("receiver_audio_call_rate");
                    String receiver_id = object.getString("receiver_id");

                 /*   Log.e("title", object.getString("title"));
                    Log.e("message", object.getString("message"));
                    Log.e("user_name", object.getString("user_name"));
                    Log.e("profile_image", object.getString("profile_image"));
                    Log.e("unique_id", object.getString("unique_id"));
                    Log.e("outgoing_time", object.getString("outgoing_time"));
                    Log.e("audioHostRate", object.getString("receiver_audio_call_rate"));
*/

                    long outgoingTime = Long.parseLong(outgoing_time);
                    long diffInMs = System.currentTimeMillis() - outgoingTime;
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
                    if (diffInSec < 26) {
                        Intent incoming = new Intent(this, IncomingCallScreen.class);
                        incoming.putExtra("token", token);
                        incoming.putExtra("caller_name", caller_name);
                        incoming.putExtra("caller_image", caller_image);
                        incoming.putExtra("UNIQUE_ID", unique_id);
                        incoming.putExtra("convId", object.optString("conversation_id"));
                        incoming.putExtra("userId", object.optString("sender_id"));
                        incoming.putExtra("callType", "audio");
                        incoming.putExtra("userpoints", user_points);
                        incoming.putExtra("receiveraudiocallRate", receiver_audio_callRate);
                        incoming.putExtra("receiverid", receiver_id);
                        incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(incoming);
                    } else {
                        sendNotification("Missed Audio Call", caller_name + " has called you");
                    }
                    return;
                }

                if (title.equals("gift Request")) {
                    String giftId = object.getString("gift_id");
                    Intent myIntent = new Intent("FBR-GIFTREC");
                    myIntent.putExtra("action", giftId);
                    myIntent.putExtra("from", "request");
                    this.sendBroadcast(myIntent);
                    return;
                }

                if (title.equals("gift")) {
                    String giftId = object.getString("gift_id");
                    Intent myIntent = new Intent("FBR-GIFTREC");
                    myIntent.putExtra("action", giftId);
                    myIntent.putExtra("from", "send");
                    this.sendBroadcast(myIntent);
                    return;
                }

                if (title.equals("logout")) {
                    Intent myIntent = new Intent("FBR-IMAGE");
                    myIntent.putExtra("action", "logout");
                    this.sendBroadcast(myIntent);
                    return;
                }

                if (object.optString("id").equals("1")) {
                    String msg = object.getString("message");
                    sendNotification("Zeeplive", msg);
                } else {

                    String token = object.getString("message");
                    String caller_name = object.getString("user_name");
                    String caller_image = object.getString("profile_image");
                    String unique_id = object.getString("unique_id");
                    String outgoing_time = object.getString("outgoing_time");

                 /*   Log.e("title", object.getString("title"));
                    Log.e("message", object.getString("message"));
                    Log.e("user_name", object.getString("user_name"));
                    Log.e("profile_image", object.getString("profile_image"));
                    Log.e("unique_id", object.getString("unique_id"));
                    Log.e("outgoing_time", object.getString("outgoing_time"));
*/

                    long outgoingTime = Long.parseLong(outgoing_time);
                    long diffInMs = System.currentTimeMillis() - outgoingTime;
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
                    if (diffInSec < 26) {
                        Intent incoming = new Intent(this, IncomingCallScreen.class);
                        incoming.putExtra("token", token);
                        incoming.putExtra("caller_name", caller_name);
                        incoming.putExtra("caller_image", caller_image);
                        incoming.putExtra("UNIQUE_ID", unique_id);
                        incoming.putExtra("convId", object.optString("conversation_id"));
                        incoming.putExtra("userId", object.optString("sender_id"));

                        incoming.putExtra("callType", "video");
                        incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(incoming);
                    } else {
                        sendNotification("Missed Video Call", caller_name + " has called you");
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String status, String comment) {
        //  Intent intent = new Intent(this, Splash.class);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(status)
                .setContentText(comment)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent resultIntent = new Intent(this, InboxDetails.class);
        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent resultPendingIntent = PendingIntent.getActivities(
                this, 0,
                new Intent[]{backIntent, resultIntent}, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(resultPendingIntent);


        /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //  stackBuilder.addNextIntent(new Intent(this, MainActivity.class));

        stackBuilder.addNextIntent(new Intent(this, InboxDetails.class));
//        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_ONE_SHOT
        );

        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());*/

       /* NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(status)
                .setContentText(comment)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_ONE_SHOT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());*/
    }

    private void callNotification(String title, String comment, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_ONE_SHOT
        );

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.btn_startcall, "Answer", resultPendingIntent).build();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setContentTitle(getString(R.string.app_name))
                .setContentTitle(title)
                .setContentText(comment)
                .addAction(action)
                .setAutoCancel(true)
                .addAction(R.drawable.btn_endcall, "Decline", resultPendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());
    }
}