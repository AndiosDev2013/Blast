package com.example.blast.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtil {
	public static Typeface font_deja = null;
	public static Typeface font_monaco = null;
	public static Typeface font_roboto = null;
	
	public static Typeface GetDeJaFont(Context context) {
		if (font_deja == null) {
			font_deja = Typeface.createFromAsset(context.getAssets(), "DejaVuSerif_Italic.ttf");
		}
		return font_deja;
	}
	
	public static Typeface GetMonacoFont(Context context) {
		if (font_monaco == null) {
			font_monaco = Typeface.createFromAsset(context.getAssets(), "Monaco.ttf");
		}
		return font_monaco;
	}
	
	public static Typeface getRobotoFont(Context context) {
		if (font_roboto == null) {
			font_roboto = Typeface.createFromAsset(context.getAssets(), "Roboto_BlackItalic.ttf");
		}
		return font_roboto;
	}
}
