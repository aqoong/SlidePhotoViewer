# SlidePhotoViewer
[Android CustomView] SlidePhotoViewer

![Alt Text](https://github.com/aqoong/SlidePhotoViewer/raw/master/sample.gif)

# How to Using
- .xml
```
<com.aqoong.lib.slidephotoviewer.SlidePhotoViewer
        android:id="@+id/slideViewer"
        android:background="@color/colorAccent"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        app:useIndicator="true"
        app:autoSlide="true"
        app:maxSize="10"                                //image list max size 
        app:autoSlide_repeat_time="1000"                //millisecond
        app:placeholder="@drawable/ic_placeholder"      //image placeholder
        />
```


[![](https://jitpack.io/v/aqoong/SlidePhotoViewer.svg)](https://jitpack.io/#aqoong/SlidePhotoViewer)<br>
```
 	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
`
dependencies {
        implementation 'com.github.aqoong:SlidePhotoViewer:TAG'
}
`
[TAG list]
- AndroidX support<br>
  0.2.4 <br>

# Using Library
- glide:4.9.0 (https://github.com/bumptech/glide)
- PageIndicatorView:1.0.3 (https://github.com/romandanylyk/PageIndicatorView)
