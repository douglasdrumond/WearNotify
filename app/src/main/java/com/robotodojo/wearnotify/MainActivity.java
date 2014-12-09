package com.robotodojo.wearnotify;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String EXTRA_VOICE_REPLY = "extra_voice_reply";


    private TextView mReplyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button notifyOnlyButton = (Button) findViewById(R.id.button_notify_only);
        Button notifyReplyButton = (Button) findViewById(R.id.button_notify_reply);
        Button notifyActionButton = (Button) findViewById(R.id.button_notify_action);
        Button clearAllButton = (Button) findViewById(R.id.button_clear_all);

        mReplyTextView = (TextView) findViewById(R.id.textview_reply);

        notifyOnlyButton.setOnClickListener(this);
        notifyReplyButton.setOnClickListener(this);
        notifyActionButton.setOnClickListener(this);
        clearAllButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CharSequence wearMessage = getMessageTextFromWear(getIntent());
        if (wearMessage != null) {
            mReplyTextView.setText(wearMessage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_notify_only:
                showSimpleNotification();
                break;
            case R.id.button_notify_action:
                showActionNotification();
                break;
            case R.id.button_notify_reply:
                showReplyNotification();
                break;
            case R.id.button_clear_all:
                clearAll();
                break;
        }
    }

    private void clearAll() {
        mReplyTextView.setText("");
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.cancelAll();
    }

    private CharSequence getMessageTextFromWear(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(EXTRA_VOICE_REPLY);
        }
        return null;
    }

    private void showReplyNotification() {
        int notificationId = 1;

        String replyLabel = getResources().getString(R.string.reply_watch_label);

        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(replyLabel)
                .build();

        Intent replyIntent = new Intent(this, MainActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getActivity(this, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_action_email,
                        getString(R.string.reply_watch_label), replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.WearableExtender wearableExtender
                = new NotificationCompat.WearableExtender();
        wearableExtender.addAction(replyAction);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Título")
                        .setContentText("Conteúdo")
                        .setContentIntent(viewPendingIntent)
                        .extend(wearableExtender);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }

    private void showActionNotification() {
        int notificationId = 1;

        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Título")
                        .setContentText("Conteúdo")
                        .setContentIntent(viewPendingIntent)
                        .addAction(android.R.drawable.sym_action_chat, "Talk", viewPendingIntent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void showSimpleNotification() {
        int notificationId = 1;

        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Título")
                        .setContentText("Conteúdo")
                        .setContentIntent(viewPendingIntent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
