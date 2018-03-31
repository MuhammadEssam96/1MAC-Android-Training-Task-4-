package com.gmail.essam96.muhammad.trainingtask4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by muhammadessam on 3/25/18.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    private static final int MESSAGE_STATE_SENT = 1;
    private static final int MESSAGE_STATE_DELIVERD = 2;
    private static final int MESSAGE_STATE_READ = 3;

    private static final int LAYOUT_FOR_OUTCOMING_MESSAGE = 0;
    private static final int LAYOUT_FOR_INCOMING_MESSAGE = 1;

    private static final int SECOND = 1;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    private static final int WEEK = 7 * DAY;
    private static final int MONTH = 4 * WEEK;
    private static final int YEAR = 12 * MONTH;

    MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        if (message.getMessageOutcoming()) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        final Message message = getItem(position);
        final int getType = getItemViewType(position);
        if (listItemView == null) {
            if (getType == LAYOUT_FOR_OUTCOMING_MESSAGE) {
                if(message.getMessageIsPhoto()){
                    listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_outcoming_photo, parent, false);
                } else if (!message.getMessageIsPhoto()){
                    listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_outcoming, parent, false);
                }
            } else if (getType == LAYOUT_FOR_INCOMING_MESSAGE) {
                if(message.getMessageIsPhoto()){
                    listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_incoming_photo, parent, false);
                } else if (!message.getMessageIsPhoto()){
                    listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_incoming, parent, false);
                }
            }
        }
        ConstraintLayout constraintLayout = listItemView.findViewById(R.id.chatConstraint);
        Locale locale = getContext().getResources().getConfiguration().locale;

        if (!message.getMessageIsPhoto()){
            TextView messageText = listItemView.findViewById(R.id.messageText);
            messageText.setText(message.getMessageText());
            if (messageText.getText().toString().length() <= 14) {
                ConstraintSet set = new ConstraintSet();
                set.clone(constraintLayout);
                set.connect(R.id.timeSince, ConstraintSet.END, R.id.timeStampTextView, ConstraintSet.START);
                set.applyTo(constraintLayout);
            }
        } else if (message.getMessageIsPhoto()){
            ImageView messagePhoto = listItemView.findViewById(R.id.messagePhoto);
            messagePhoto.setImageBitmap(message.getMessageBitmap());
            listItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToStartPhotoActivity = new Intent(getContext(), PhotoViewActivity.class);
                    intentToStartPhotoActivity.putExtra("PHOTO_URI", message.getImageUri());
                    getContext().startActivity(intentToStartPhotoActivity);
                }
            });
        }

        TextView timeStamp = listItemView.findViewById(R.id.timeStampTextView);
        timeStamp.setText(message.getTimeStamp());

        TextView timeSince = adjustMessageTimeSinceTextViewBasedOnLocale(locale, listItemView, message);

        ImageView messageStateImageView = listItemView.findViewById(R.id.messageStateImageView);
        if(message.getMessageState() == MESSAGE_STATE_SENT){
            messageStateImageView.setImageResource(R.drawable.msg_status_server_receive);
        } else if (message.getMessageState() == MESSAGE_STATE_DELIVERD){
            messageStateImageView.setImageResource(R.drawable.msg_status_client_received);
        } else if (message.getMessageState() == MESSAGE_STATE_READ){
            messageStateImageView.setImageResource(R.drawable.msg_status_client_read);
        }

        return listItemView;
    }

    private TextView adjustMessageTimeSinceTextViewBasedOnLocale(Locale locale, View listItemView, Message message){
        TextView messageTimeSinceTextView = listItemView.findViewById(R.id.timeSince);
        if(locale.toString().contains("en")){
            String timesinceString = setTimeSinceTextViewForEnglish(message);
            messageTimeSinceTextView.setText(timesinceString);
        } else if (locale.toString().contains("ar")){
            String timesinceString = setTimeSinceTextViewForArabic(message);
            messageTimeSinceTextView.setText(timesinceString);
        }
        return messageTimeSinceTextView;
    }

    private String setTimeSinceTextViewForEnglish(Message message) {
        int timeSince = message.getTimeSince().intValue();
        if (timeSince <= 6) {
            return getContext().getResources().getString(R.string.just_now);
        } else {
            if (timeSince < MINUTE) {
                return String.valueOf(timeSince) + getContext().getResources().getString(R.string.seconds_ago);
            } else if (timeSince == MINUTE || timeSince <= 2 * MINUTE) {
                return String.valueOf(timeSince / MINUTE) + getContext().getResources().getString(R.string.minute_ago);
            } else {
                if (timeSince < HOUR) {
                    return String.valueOf(timeSince / MINUTE) + getContext().getResources().getString(R.string.minutes_ago);
                } else if (timeSince == HOUR || timeSince <= 2 * HOUR) {
                    return String.valueOf(timeSince / HOUR) + getContext().getResources().getString(R.string.hour_ago);
                } else {
                    if (timeSince < DAY) {
                        return String.valueOf(timeSince / HOUR) + getContext().getResources().getString(R.string.hours_ago);
                    } else if (timeSince == DAY || timeSince <= 2 * DAY) {
                        return String.valueOf(timeSince / DAY) + getContext().getResources().getString(R.string.day_ago);
                    } else {
                        if (timeSince < WEEK) {
                            return String.valueOf(timeSince / DAY) + getContext().getResources().getString(R.string.days_ago);
                        } else if (timeSince == WEEK || timeSince <= 2 * WEEK) {
                            return String.valueOf(timeSince / WEEK) + getContext().getResources().getString(R.string.week_ago);
                        } else {
                            if (timeSince < MONTH) {
                                return String.valueOf(timeSince / WEEK) + getContext().getResources().getString(R.string.weeks_ago);
                            } else if (timeSince == MONTH || timeSince <= 2 * MONTH) {
                                return String.valueOf(timeSince / MONTH) + getContext().getResources().getString(R.string.month_ago);
                            } else {
                                if (timeSince < YEAR) {
                                    return String.valueOf(timeSince / MONTH) + getContext().getResources().getString(R.string.months_ago);
                                } else if (timeSince == YEAR || timeSince <= 2 * YEAR) {
                                    return String.valueOf(timeSince / YEAR) + getContext().getResources().getString(R.string.year_ago);
                                } else {
                                    return String.valueOf(timeSince / YEAR) + getContext().getResources().getString(R.string.years_ago);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String setTimeSinceTextViewForArabic(Message message){
        int timeSince = message.getTimeSince().intValue();
        String space = getContext().getResources().getString(R.string.space);

        String justNow = getContext().getResources().getString(R.string.time_since_just_now);
        String from = getContext().getResources().getString(R.string.time_since_from);

        String wa7ed = getContext().getResources().getString(R.string.time_since_wa7d_m);
        String wa7da = getContext().getResources().getString(R.string.time_since_wa7d_f);

        String sanya = getContext().getResources().getString(R.string.time_since_from_seconds_1);
        String swany = getContext().getResources().getString(R.string.time_since_from_seconds_2);

        String d2e2a = getContext().getResources().getString(R.string.time_since_from_minitues_1);
        String d2e2ten = getContext().getResources().getString(R.string.time_since_from_minitues_2);
        String d2ay2 = getContext().getResources().getString(R.string.time_since_from_minitues_3);

        String sa3a = getContext().getResources().getString(R.string.time_since_from_hours_1);
        String sa3ten = getContext().getResources().getString(R.string.time_since_from_hours_2);
        String sa3at = getContext().getResources().getString(R.string.time_since_from_hours_3);

        String youm = getContext().getResources().getString(R.string.time_since_from_days_1);
        String youmen = getContext().getResources().getString(R.string.time_since_from_days_2);
        String ayam = getContext().getResources().getString(R.string.time_since_from_days_3);

        String esbo3 = getContext().getResources().getString(R.string.time_since_from_weeks_1);
        String esbo3en = getContext().getResources().getString(R.string.time_since_from_weeks_2);
        String asabe3 = getContext().getResources().getString(R.string.time_since_from_weeks_3);

        String shahr = getContext().getResources().getString(R.string.time_since_from_months_1);
        String shahren = getContext().getResources().getString(R.string.time_since_from_months_2);
        String ashhor = getContext().getResources().getString(R.string.time_since_from_months_3);
        String shohor = getContext().getResources().getString(R.string.time_since_from_months_4);

        String sana = getContext().getResources().getString(R.string.time_since_from_years_1);
        String sanaten = getContext().getResources().getString(R.string.time_since_from_years_2);
        String sanawat = getContext().getResources().getString(R.string.time_since_from_years_3);

        if(timeSince <= 6 * SECOND){
            return justNow;
        } else {
            if (timeSince < MINUTE){
                if (timeSince < 11 * SECOND){
                    return from + space + String.valueOf(timeSince) + space + swany;
                } else {
                    return from + space + String.valueOf(timeSince) + space + sanya;
                }
            } else if (timeSince == MINUTE || timeSince < 2 * MINUTE){
                return from + space + d2e2a + space + wa7da;
            } else {
                if(timeSince < HOUR){
                    if (timeSince == 2 * MINUTE || timeSince < 3 * MINUTE){
                        return from + space + d2e2ten;
                    } else {
                        if (timeSince < 11 * MINUTE){
                            return from + space + String.valueOf(timeSince / MINUTE) + space + d2ay2;
                        } else {
                            return from + space + String.valueOf(timeSince / MINUTE) + space + d2e2a;
                        }
                    }
                } else if (timeSince == HOUR || timeSince < 2 * HOUR){
                    return from + space + sa3a + space +wa7da;
                } else {
                    if (timeSince < DAY){
                        if (timeSince == 2 * HOUR|| timeSince < 3 * HOUR){
                            return from + space + sa3ten;
                        } else {
                            if(timeSince < 11 * HOUR){
                                return from + space + String.valueOf(timeSince / HOUR) + space + sa3at;
                            } else {
                                return from + space + String.valueOf(timeSince / HOUR) + space + sa3a;
                            }
                        }
                    } else if (timeSince == DAY || timeSince < 2 * DAY){
                        return from + space + youm + wa7ed;
                    } else {
                        if (timeSince < WEEK){
                            if(timeSince == 2 * DAY|| timeSince < 3 * DAY){
                                return from + space + youmen;
                            } else {
                                if(timeSince < 11 * DAY){
                                    return from + space + String.valueOf(timeSince / DAY) + space + ayam;
                                } else if (timeSince >= 11 * DAY){
                                    return from + space + String.valueOf(timeSince / DAY) + space + youm;
                                }
                            }
                        } else if (timeSince == WEEK || timeSince < 2 * WEEK){
                            return from + space + esbo3 + wa7ed;
                        } else {
                            if(timeSince < MONTH){
                                if (timeSince == 2 * WEEK|| timeSince < 3 * WEEK){
                                    return from + space + esbo3en;
                                } else {
                                    if(timeSince < 11 * WEEK){
                                        return from + space + String.valueOf(timeSince / WEEK) + space + asabe3;
                                    } else if (timeSince >= 11 * WEEK){
                                        return from + space + String.valueOf(timeSince / WEEK) + space + esbo3;
                                    }
                                }
                            } else if (timeSince == MONTH || timeSince < 2 * MONTH){
                                return from + space + shahr + wa7ed;
                            } else {
                                if (timeSince < YEAR){
                                    if(timeSince == 2 * MONTH|| timeSince < 3 * MONTH){
                                        return from + space + shahren;
                                    } else {
                                        if(timeSince < 11 * MONTH){
                                            return from + space + String.valueOf(timeSince / MONTH) + space + shohor;
                                        } else {
                                            return from + space + String.valueOf(timeSince / MONTH) + space + ashhor;
                                        }
                                    }
                                } else if (timeSince == YEAR || timeSince < 2 * YEAR){
                                    return from + space + sana + wa7da;
                                } else if (timeSince == 2 * YEAR || timeSince < 3 * YEAR){
                                    return from + space + sanaten;
                                } else {
                                    if (timeSince < 11 * YEAR){
                                        return from + space + String.valueOf(timeSince / YEAR) + space + sanawat;
                                    } else {
                                        return from + space + String.valueOf(timeSince / YEAR) + space + sana;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return space;
    }
}