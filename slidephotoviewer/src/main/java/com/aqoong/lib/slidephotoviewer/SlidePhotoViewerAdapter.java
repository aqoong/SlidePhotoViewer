package com.aqoong.lib.slidephotoviewer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;

public class SlidePhotoViewerAdapter extends PagerAdapter
{
    private final String TAG = getClass().getSimpleName();

    private Context             mContext;
    private LayoutInflater      mLayoutInflater;
    private RequestManager      imageRequestManager;

    private ArrayList<SlidePhotoObject>   imageResourceList;
    private int                 mPlaceHolderResource;
    private int                 mItemBackgroundRescource;
    private boolean             useZoom;

    public SlidePhotoViewerAdapter(Context context, int itemBackground){
        this(context, itemBackground, null);
    }

    public SlidePhotoViewerAdapter(Context context, int itemBackground, ArrayList<SlidePhotoObject> resources){
        mContext = context;
        this.mItemBackgroundRescource = itemBackground;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageRequestManager = Glide.with(mContext);

        imageResourceList = resources;
        if(imageResourceList == null){
            imageResourceList = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return imageResourceList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((LinearLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View itemView = mLayoutInflater.inflate(R.layout.slidephotoviewer_pager_item, container, false);
        View imageView = null;
        if(useZoom) {
            imageView = (TouchImageView)itemView.findViewById(R.id.slidephotoviewer_image_zoom);
//            setPhotoZoom((TouchImageView) imageView);
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView = (AppCompatImageView)itemView.findViewById(R.id.slidephotoviewer_image);
            imageView.setVisibility(View.VISIBLE);
        }

        imageView.setBackgroundResource(mItemBackgroundRescource);

        RequestOptions requestOptions = new RequestOptions();
        try
        {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.placeholder(mPlaceHolderResource);
            requestOptions.fitCenter();

            imageRequestManager.load(imageResourceList.get(position).resource)
                    .apply(requestOptions)
                    .into(useZoom?(TouchImageView)imageView:(AppCompatImageView)imageView);
            imageView.setOnClickListener(imageResourceList.get(position).listener);
        }
        catch (NoSuchMethodError e){
            e.printStackTrace();
        }

        container.addView(itemView);

        return itemView;
    }

    private void setPhotoZoom(final TouchImageView imageView){
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageView.resetZoom();
                } else {
                    if (imageView.getCurrentZoom() > 1.8f) {
                        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                        dialog.setContentView(R.layout.slidephotoviewer_pager_item);
                        TouchImageView image = dialog.findViewById(R.id.slidephotoviewer_image_zoom);
                        image.setVisibility(View.VISIBLE);
                        image.setImageDrawable(imageView.getDrawable());
                        image.setMaxZoom(1.8f);

                        image.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                imageView.resetZoom();
                                dialog.dismiss();
                                return true;
                            }
                        });
                        dialog.show();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((LinearLayout)object);
    }

    public void setPlaceHolderResource(int placeHolderResource){
        this.mPlaceHolderResource = placeHolderResource;
    }

    public void removePhoto(ViewPager pager, SlidePhotoObject resource){
        removePhoto(pager, imageResourceList.indexOf(resource));
    }
    public void removePhoto(ViewPager pager, int position){
        pager.setAdapter(null);
        imageResourceList.remove(position);
        pager.setAdapter(this);
    }
    public void addPhoto(SlidePhotoObject resource){
        addPhoto(imageResourceList.size(), resource);
    }
    public void addPhoto(int position, SlidePhotoObject resource){
        imageResourceList.add(position, resource);
    }

    public void clearList(ViewPager pager){
        pager.setAdapter(null);
        imageResourceList.clear();
        pager.setAdapter(this);
    }
    public void setItemBackground(int backgroundImage){
        this.mItemBackgroundRescource = backgroundImage;
        this.notifyDataSetChanged();
    }

    public SlidePhotoObject getResource(int position){
        return imageResourceList.get(position);
    }
    public ArrayList<SlidePhotoObject> getResourceList(){return imageResourceList;}
    public void usePhotoZoom(boolean use){
        this.useZoom = use;
        this.notifyDataSetChanged();
    }
    public boolean useZoom(){return this.useZoom;}
}
