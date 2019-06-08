package com.aqoong.lib.slidephotoviewer;


import android.view.View;

public class SlidePhotoObject {
    Object resource;
    View.OnClickListener listener;

    public SlidePhotoObject(Object resource, View.OnClickListener listener){
        this.resource = resource;
        this.listener = listener;
    }
}
