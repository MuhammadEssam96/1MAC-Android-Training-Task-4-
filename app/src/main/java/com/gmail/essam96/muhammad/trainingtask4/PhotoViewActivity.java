package com.gmail.essam96.muhammad.trainingtask4;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class PhotoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ImageView photoView = findViewById(R.id.message_photo_view);

        Uri imageUriString = Uri.parse(getIntent().getStringExtra("PHOTO_URI"));

        photoView.setImageURI(imageUriString);
    }
}
