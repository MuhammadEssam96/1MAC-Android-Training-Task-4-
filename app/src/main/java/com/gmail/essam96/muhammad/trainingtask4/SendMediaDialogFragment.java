package com.gmail.essam96.muhammad.trainingtask4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class SendMediaDialogFragment extends DialogFragment {

    public  interface OnCompleteListener {
        void onThumbnailComplete(Bitmap bitmap);
    }

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
                Toast.makeText(getContext(), "Please note that this will only send a thumbnail of the shot, It is still WIP and will be fully implemented soon.", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
            }
        });

        captureVideoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This feature will be implemented soon", Toast.LENGTH_SHORT).show();
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
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
}