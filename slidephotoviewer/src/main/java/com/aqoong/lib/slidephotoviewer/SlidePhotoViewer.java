package com.aqoong.lib.slidephotoviewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SlidePhotoViewer extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();

    private int     resPlaceholder;
    private int     mMaxSize;
    private boolean autoSlide;
    private boolean useIndicator;
    private long    autoSlideRepeatTime;
    private boolean useSidePreview;
    private int     resItemBackground;
    private boolean useZoom;

    private SlidePhotoViewerAdapter vAdapter;
    private ViewPager vPager;
    private PageIndicatorView       itemIndicator;

    private final int defPadding = 30;


    public SlidePhotoViewer(Context context) {
        this(context, null);
    }

    public SlidePhotoViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidePhotoViewer);
        try{
            resPlaceholder  = typedArray.getResourceId(R.styleable.SlidePhotoViewer_placeholder, R.drawable.ic_placeholder);
            mMaxSize        = typedArray.getInteger(R.styleable.SlidePhotoViewer_maxSize, 10);
            autoSlide       = typedArray.getBoolean(R.styleable.SlidePhotoViewer_autoSlide, true);
            useIndicator    = typedArray.getBoolean(R.styleable.SlidePhotoViewer_useIndicator, true);
            autoSlideRepeatTime = typedArray.getInteger(R.styleable.SlidePhotoViewer_autoSlide_repeat_time, 3000);
            useSidePreview  = typedArray.getBoolean(R.styleable.SlidePhotoViewer_sidePreview, false);
            resItemBackground = typedArray.getResourceId(R.styleable.SlidePhotoViewer_itemBackground, R.drawable.slidephotoviewer_default_item_background);
            useZoom         = typedArray.getBoolean(R.styleable.SlidePhotoViewer_useZoom, false);
        }finally {
            typedArray.recycle();
        }

        initView();
    }

    private void initView(){
        vAdapter = new SlidePhotoViewerAdapter(getContext(), resItemBackground);
        vAdapter.setPlaceHolderResource(resPlaceholder);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.slidephotoviewer_pager, this);

        itemIndicator   = parentView.findViewById(R.id.slidephotoviewer_indicator);
        vPager          = parentView.findViewById(R.id.slidephotoviewer_viewpager);

        vPager.setAdapter(vAdapter);
        setSidePreview(useSidePreview);
        itemIndicator.setVisibility(useIndicator?VISIBLE:GONE);

        autoSlide(autoSlide);
        usePhotoZoom(useZoom);
    }
    public void setSidePreview(boolean use){
        setSidePreview(use, use==false?0:defPadding);
    }
    public void setSidePreview(boolean use, int padding){
        vPager.setClipToPadding(!use);
        if(use)
        {
            int convertedPadding = convertPixelsToDp(padding, getContext());
            Point screen = new Point();
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getSize(screen);
            float startOffset = (float) convertedPadding / (float) (screen.x - 2 * convertedPadding);
            itemIndicator.setTranslationY(itemIndicator.getTranslationY() - convertedPadding);
            vPager.setPadding(convertedPadding, convertedPadding, convertedPadding, convertedPadding);
            vPager.setOffscreenPageLimit(3);
            vPager.setPageMargin(50);
            vPager.setPageTransformer(false, new TransFromViewPager(0, 0, 0.7f, startOffset));
        }else{
            vPager.setOffscreenPageLimit(1);
            vPager.setPageMargin(0);
            vPager.setPadding(0, 0, 0, 0);
            vPager.setPageTransformer(true, null);
        }
    }

    private void nextSlide(){
        vPager.setCurrentItem(calNextItem(), true);
    }
    private int calNextItem(){
        int nextId = vPager.getCurrentItem() + 1;

        return nextId == vAdapter.getCount() ? 0 : nextId;
    }
    public void usePhotoZoom(boolean use){
        this.useZoom = use;
        vAdapter.usePhotoZoom(useZoom);
    }
    public void autoSlide(boolean start){
        autoSlide(start, this.autoSlideRepeatTime);
    }

    public void autoSlide(boolean start, long reapeatTime){
        this.autoSlideRepeatTime = reapeatTime;

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                nextSlide();
            }
        };

        Timer timer = new Timer();
        if(start) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            }, 1000, autoSlideRepeatTime);
        }else{
            timer.cancel();
        }
    }

    public void setReasourceList(ArrayList<Object> resList) throws MaxSizeException{
        clearData();

        for(int i = 0 ; i < resList.size() ; i++) {
            if(i >= mMaxSize){
                throw new MaxSizeException("Over Max Size. Check your setting.");
            }
            if(resList.get(i) instanceof SlidePhotoObject){
                vAdapter.addPhoto((SlidePhotoObject) resList.get(i));
            }else{
                vAdapter.addPhoto(new SlidePhotoObject(resList.get(i), null));
            }

        }
        vAdapter.notifyDataSetChanged();
    }
    public void addResource(Object res) throws MaxSizeException {
        if(res instanceof SlidePhotoObject){
            SlidePhotoObject sObj = (SlidePhotoObject)res;
            addResource(sObj.resource, ((SlidePhotoObject) res).listener);
        }else{
            addResource(res, null);
        }
    }
    public void addResource(Object res, OnClickListener listener) throws MaxSizeException{
        if(vAdapter.getCount() >= mMaxSize){
            throw new MaxSizeException("Over Max Size. Check your setting.");
        }
        vAdapter.addPhoto(new SlidePhotoObject(res, listener));
        vAdapter.notifyDataSetChanged();
    }

    public void clearData(){
        vAdapter.clearList(vPager);
    }

    public void setPlaceHolderRes(int res){
        vAdapter.setPlaceHolderResource(res);
    }

    public int getResSize(){
        return vAdapter.getCount();
    }
    public SlidePhotoObject getResource(int index){
        return vAdapter.getResource(index);
    }

    public void setItemBackground(int backgroundImage){
        vAdapter.setItemBackground(backgroundImage);
    }


    private int convertPixelsToDp(float px, Context context) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
        return value;
    }

}
