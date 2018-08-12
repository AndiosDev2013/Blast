package com.example.blast;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class myImageLoader {
	
	private static ImageLoader instance = ImageLoader.getInstance();
	
	public static DisplayImageOptions options;

	public static void init() {
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.movie_image_background)
			.showImageForEmptyUri(R.drawable.movie_image_background)
			.showImageOnFail(R.drawable.movie_image_background)
			.cacheInMemory(false)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(0))
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.build();
	}
	
	public static void clearCache() {
		if (instance != null) {
			instance.clearDiscCache();
			instance.clearMemoryCache();
		}
	}
	
	public static void stop() {
		if (instance != null) {
			instance.stop();
		}
	}
	
	public static final int IMAGE_TYPE_MAN = 0;
	public static final int IMAGE_TYPE_WOMAN = 1;
	public static final int IMAGE_TYPE_SQUARE = 2;
	public static final int IMAGE_TYPE_SMALL_RECT = 3;
	public static final int IMAGE_TYPE_LAGRERECT = 4;
	
	public static void showImage(ImageView imgView, String url) {
		if (TextUtils.isEmpty(url))
			return;
		
		DisplayImageOptions option = options;
		
		try {
			instance.displayImage(url, imgView, option, animateFirstListener);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
