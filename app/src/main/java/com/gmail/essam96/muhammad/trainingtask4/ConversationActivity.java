package com.gmail.essam96.muhammad.trainingtask4;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ConversationActivity extends AppCompatActivity implements SendMediaDialogFragment.OnCompleteListener {

    private static final int MESSAGE_STATE_WAITING = 0;
    private static final int MESSAGE_STATE_SENT = 1;
    private static final int MESSAGE_STATE_DELIVERED = 2;
    private static final int MESSAGE_STATE_READ = 3;

    private TextView contactStatus;
    private EditText messageEditText;
    private ImageButton cameraButton;
    private ArrayList<Message> messages = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private boolean isOutcoming = true;

    private MediaPlayer sentMessageSoundPlayer, receivedMessageSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messageAdapter = new MessageAdapter(this, messages);
        messageAdapter.setNotifyOnChange(true);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(messageAdapter);
        contactStatus = findViewById(R.id.contact_status_TextView);
        messageEditText = findViewById(R.id.messageEditText);
        cameraButton = findViewById(R.id.cameraButton);
        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton contactAvatar = findViewById(R.id.contactAvatar);
        ImageButton switchBetweenOutcomingAndIncoming = findViewById(R.id.compare_arrows_button);
        ImageButton makeMessageStateSentButton = findViewById(R.id.make_sent_button);
        ImageButton makeMessageStateDeliveredAndSeen = findViewById(R.id.make_seen_button);
        ImageButton sendButton = findViewById(R.id.sendButton);
        getWindow().setBackgroundDrawableResource(R.drawable.default_wallpaper);
        sentMessageSoundPlayer = MediaPlayer.create(this, R.raw.send_message);
        receivedMessageSoundPlayer = MediaPlayer.create(this, R.raw.incoming);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        contactAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switchBetweenOutcomingAndIncoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOutcoming){
                    isOutcoming = false;
                    Toast toast = Toast.makeText(ConversationActivity.this, "Now will receive.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    isOutcoming = true;
                    Toast toast = Toast.makeText(ConversationActivity.this, "Now will send.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        makeMessageStateSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int messagesCount = messages.size();
                for (int i = 0; i < messagesCount; i++){
                    Message message = messages.get(i);
                    if(message.getMessageOutcoming()){
                        if(message.getMessageState() == MESSAGE_STATE_WAITING){
                            message.setMessageState(MESSAGE_STATE_SENT);
                            sentMessageSoundPlayer.start();
                            messageAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        makeMessageStateDeliveredAndSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int messagesCount = messages.size();
                for (int i = 0; i < messagesCount; i++){
                    Message message = messages.get(i);
                    if(message.getMessageOutcoming()){
                        if(message.getMessageState() == MESSAGE_STATE_SENT){
                            message.setMessageState(MESSAGE_STATE_DELIVERED);
                            messageAdapter.notifyDataSetChanged();
                        } else if (message.getMessageState() == MESSAGE_STATE_DELIVERED){
                            message.setMessageState(MESSAGE_STATE_READ);
                            messageAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactStatus.setText(getString(R.string.typing));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (messageEditText.getText().toString().length() == 0) {
                    cameraButton.setVisibility(View.VISIBLE);
                } else {
                    cameraButton.setVisibility(View.GONE);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMessagePriorSendingThenSend();
                if(messages.size() >= 1){
                    adjustTimeCountersForEachMessage();
                }
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                SendMediaDialogFragment sendMediaDialogFragment = SendMediaDialogFragment.newInstance();
                sendMediaDialogFragment.show(fragmentManager, "tag");
            }
        });
    }

    private void checkMessagePriorSendingThenSend(){
        if(isMessageEmpty(messageEditText.getText().toString())){
            Toast toast = Toast.makeText(ConversationActivity.this, R.string.message_empty, Toast.LENGTH_SHORT);
            toast.show();
            contactStatus.setText(getString(R.string.online));
        } else {
            sendTextMessage(messageEditText.getText().toString());
        }
    }

    private boolean isMessageEmpty(String messageText){
        if (messageText.isEmpty()){
            return true;
        } else {
            for (int i = 0; i < messageText.length(); i++){
                if(messageText.charAt(i) != ' '){
                    return false;
                }
            }
            return true;
        }
    }

    private void sendTextMessage(String messageText){
        Long timeStampLong = System.currentTimeMillis();
        String timeStamp = formatTime(timeStampLong, getResources().getConfiguration().locale);
        messages.add(new Message(messageText, timeStamp, timeStampLong, isOutcoming));
        if(!isOutcoming){
            receivedMessageSoundPlayer.start();
        }
        messageAdapter.notifyDataSetChanged();
        messageEditText.setText("");
        contactStatus.setText(getString(R.string.online));
    }

    private String formatTime(long millis, Locale locale) {
        String timeStamp;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", locale);
        Date resultDate = new Date(millis);
        timeStamp = simpleDateFormat.format(resultDate);
        return timeStamp;
    }

    private void adjustTimeCountersForEachMessage(){
        Long messageTimeStamp, seconds;
        Long timeAtAdjusting = System.currentTimeMillis();
        for(int i = 0; i < messages.size(); i++){
            Message message = messages.get(i);
            messageTimeStamp = timeAtAdjusting - message.getTimeStampLong();
            seconds = messageTimeStamp / 1000;
            message.setTimeSince(seconds);
            messageAdapter.notifyDataSetChanged();
        }
    }

    private void sendPhotoMessage(Bitmap bitmap, Uri imageUri){
        Long timeStampLong = System.currentTimeMillis();
        String timeStamp = formatTime(timeStampLong, getResources().getConfiguration().locale);
        messages.add(new Message(bitmap, timeStamp, timeStampLong, isOutcoming, true, imageUri));
        if(!isOutcoming){
            receivedMessageSoundPlayer.start();
        }
        messageAdapter.notifyDataSetChanged();
        messageEditText.setText("");
        contactStatus.setText(getString(R.string.online));
    }

    private Bitmap bitmap;

    @Override
    public void onThumbnailComplete(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void onFullSizePhotoComplete(Uri imageUri) {
        sendPhotoMessage(bitmap, imageUri);
        if(messages.size() >= 1){
            adjustTimeCountersForEachMessage();
        }
    }
}