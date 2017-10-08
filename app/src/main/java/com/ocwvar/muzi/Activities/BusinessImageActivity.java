package com.ocwvar.muzi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.ocwvar.muzi.R;
import com.squareup.picasso.Picasso;

public class BusinessImageActivity extends Activity {
    ImageView imageView;
    String imagePath = "";
    Intent sourceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_image);
        imageView = (ImageView) findViewById(R.id.dialog_image);
        sourceIntent = getIntent();
        imagePath = sourceIntent.getStringExtra("ImagePath");
        if(!("".equals(imagePath))){
            Picasso
                    .with(imageView.getContext())
                    .load(imagePath)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }

    }
}
