package com.gmail.essam96.muhammad.trainingtask4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class SendMediaDialogFragment extends DialogFragment {

    public  interface OnCompleteListener {
        void onThumbnailComplete(Bitmap bitmap);
        void onFullSizePhotoComplete(Uri imageUri);
    }

    private OnCompleteListener listener;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public SendMediaDialogFragment(){

    }

    public static SendMediaDialogFragment newInstance(){
        return new SendMediaDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_media_options, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView capturePhotoTexView = view.findViewById(R.id.capturePhotoTextView);
        TextView captureVideoTextView = view.findViewById(R.id.captureVideoTextView);

        capturePhotoTexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        captureVideoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Implement this.
            }
        });
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.96), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e){
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(getContext(), "com.gmail.essam96.muhammad.trainingtask4.fileprovider", photoFile);
                sendBackPhoto(photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(imageBitmap != null){
                sendBackResult(imageBitmap);
                super.dismiss();
            }
        }
    }

    public void sendBackResult(Bitmap bitmap){
        OnCompleteListener listener = (OnCompleteListener) getActivity();
        listener.onThumbnailComplete(bitmap);
    }

    public void sendBackPhoto(Uri imageURI){
        OnCompleteListener listener = (OnCompleteListener) getActivity();
        listener.onFullSizePhotoComplete(imageURI);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {

        String imageFileName = "JPEG_1" + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory("Android/data/com.gmail.essam96.muhammad.trainingtask4/files/Pictures");
        File storageDir2;
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = new File(storageDir, imageFileName);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}