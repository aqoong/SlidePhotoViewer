package com.aqoong.lib.slidephotoviewer;

import android.os.Build;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Andy
 * <p>
 * email : han.andy@cherrypica.com
 * date  : 2019-06-14
 **/
public class TransFromViewPager implements ViewPager.PageTransformer
{
    private int baseElevation;
    private int raisingElevation;
    private float smallerScale;
    private float startOffset;

    public TransFromViewPager(int baseElevation, int raisingElevation, float smallerScale, float startOffset) {
        this.baseElevation = baseElevation;
        this.raisingElevation = raisingElevation;
        this.smallerScale = smallerScale;
        this.startOffset = startOffset;
    }

    @Override
    public void transformPage(View page, float position) {
        float absPosition = Math.abs(position - startOffset);

        if (absPosition >= 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                page.setElevation(baseElevation);
            }
            page.setScaleY(smallerScale);
        } else {
            // This will be during transformation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                page.setElevation(((1 - absPosition) * raisingElevation + baseElevation));
            }
            page.setScaleY((smallerScale - 1) * absPosition + 1);
        }
    }
}
