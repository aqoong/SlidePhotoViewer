# SlidePhotoViewer
[Android CustomView] SlidePhotoViewer
- Normal Mode<br>
![Alt Text](https://github.com/aqoong/SlidePhotoViewer/raw/master/sample.gif)
- Preview Mode<br>
![Alt Text](https://github.com/aqoong/SlidePhotoViewer/raw/master/sample2.gif)

# How to Using
- .xml
```
<com.aqoong.lib.slidephotoviewer.SlidePhotoViewer
        android:id="@+id/slideViewer"
        android:background="@color/colorAccent"
        android:layout_width="match_parent"
        android:layout_height="100dp"
	app:itemBackground="@drawable/slidephotoviewer_default_item_background"
	app:sidePreview="true"				//side preview function
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
  1.2.0 <br>
- If you don't use androidx<br>
  1.1.0 <br>

# Using Library
- glide:4.9.0 (https://github.com/bumptech/glide)
- PageIndicatorView:1.0.3 (https://github.com/romandanylyk/PageIndicatorView)

# another
- preview logic (https://jayrambhia.com/blog/android-viewpager-cards-1)
