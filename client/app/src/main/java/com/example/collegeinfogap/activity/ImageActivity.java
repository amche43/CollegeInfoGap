package com.example.collegeinfogap.activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ImageView imageView = new ImageView(this);

        setContentView(imageView);

        String image = getIntent().getStringExtra("image");

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if(image != null){

            imageView.setImageURI(Uri.parse(image));

        }

    }
}