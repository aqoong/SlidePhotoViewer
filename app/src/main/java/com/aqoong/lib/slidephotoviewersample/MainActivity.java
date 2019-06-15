package com.aqoong.lib.slidephotoviewersample;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;



import com.aqoong.lib.slidephotoviewer.MaxSizeException;
import com.aqoong.lib.slidephotoviewer.SlidePhotoViewer;

public class MainActivity extends AppCompatActivity
{
    SlidePhotoViewer mSlideViewr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlideViewr = findViewById(R.id.slideViewer);

        try {
            mSlideViewr.addResource(R.drawable.ic_test, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "TEST", Toast.LENGTH_LONG).show();
                }
            });
            mSlideViewr.addResource(R.drawable.ic_launcher_foreground, null);
            mSlideViewr.addResource(R.drawable.ic_test, null);
            mSlideViewr.addResource(R.drawable.ic_launcher_foreground, null);
        } catch (MaxSizeException e) {
            e.printStackTrace();
        }
    }
}
