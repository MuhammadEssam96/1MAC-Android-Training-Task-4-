package com.gmail.essam96.muhammad.trainingtask4;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by muhammadessam on 3/25/18.
 */

public class Message {
    private static final int MESSAGE_STATE_WAITING = 0;
    private static final int MESSAGE_STATE_SENT = 1;
    private static final int MESSAGE_STATE_DELIVERD = 2;
    private static final int MESSAGE_STATE_READ = 3;

    private static final int MESSAGE_TYPE_TEXT = 0;
    private static final int MESSAGE_TYPE_PHOTO = 1;
    private static final int MESSAGE_TYPE_VIDEO = 2;

    private String text, timeStamp;
    private Bitmap bitmap;
    private Long timeStampLong, timeSince;
    private boolean isOutcoming, isPhoto, isVideo;
    private int messageState;


    Message(String text, String timeStamp, Long timeStampLong ,boolean isOutcoming){
        this.text = text;
        this.timeStamp = timeStamp;
        this.isOutcoming = isOutcoming;
        this.timeStampLong = timeStampLong;
        messageState = MESSAGE_STATE_WAITING;
    }

    Message(Bitmap bitmap, String timeStamp, Long timeStampLong , boolean isOutcoming){
        this.bitmap = bitmap;
        this.timeStamp = timeStamp;
        this.isOutcoming = isOutcoming;
        this.timeStampLong = timeStampLong;
        this.isPhoto = true;
        messageState = MESSAGE_STATE_WAITING;
    }

    public void setText(String newText){
        text = newText;
    }

    @NonNull String getMessageText(){
        return text;
    }

    @NonNull  String getTimeStamp(){
        return timeStamp;
    }

    @NonNull Long getTimeStampLong(){
        return timeStampLong;
    }

    boolean getMessageOutcoming(){
        return isOutcoming;
    }

    void setTimeSince(Long timeSince){
        this.timeSince = timeSince;
    }

    @NonNull Long getTimeSince(){
        return timeSince;
    }

    public void setMessageState(int messageState) {
        this.messageState = messageState;
    }

    public int getMessageState() {
        return messageState;
    }

    public Bitmap getMessageBitmap() {
        return bitmap;
    }

    public boolean getMessageIsPhoto() {
        return isPhoto;
    }

}
