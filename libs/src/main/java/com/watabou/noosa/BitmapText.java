/*
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.noosa;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Matrix;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.watabou.glwrap.Texture;
import android.graphics.RectF;
import android.net.wifi.MloLink;

public class BitmapText extends Image {
	private static Canvas canvas = new Canvas();
	private static Paint painter = new Paint();

	public static int font;
	private static Typeface customFont;

	private static Typeface bitmapFont;
	private static int DEFAULT_SIZE = 12;

	private static final String fontPath = "font/";

	private static final String latinPixelFont = "pixel_font.ttf";

	private static final String cjkPixelFont = "Founder.ttf";

	private static final String bitmapPixelFont = "04B_03__.ttf";

	public static String currentLanguageCode = "en";

	//this is basically a LRU cache. capacity is determined by character count, not entry count.
	private static LinkedHashMap<String, BitmapText.CachedText> textCache = new LinkedHashMap<>(700, 0.75f, true);

	private static int cachedChars = 0;

	private static final int GC_TRIGGER = 1250;
	private static final int GC_TARGET = 1000;

	private static void runGC(){
		Iterator<Map.Entry<String, BitmapText.CachedText>> it = textCache.entrySet().iterator();
		while (cachedChars > GC_TARGET && it.hasNext()){
			BitmapText.CachedText cached = it.next().getValue();
			if (cached.activeTexts.isEmpty()) {
				cachedChars -= cached.length;
				cached.texture.delete();
				it.remove();
			}
		}
	}

	private int size;
	private String text;
	private BitmapText.CachedText cache;

	private boolean needsRender = false;

	public BitmapText(Font font){
		this.text = text;
		this.size = DEFAULT_SIZE;
		needsRender = true;
		measure(this);
	}

	public BitmapText(String text, Font font){
		this.text = text;
		this.size = DEFAULT_SIZE;
		needsRender = true;
		measure(this);
	}

	public BitmapText( ){
		text = null;
	}

	public BitmapText(int size ){
		text = null;
		this.size = size;
	}

	public BitmapText(String text, int size){
		this.text = text;
		this.size = size;

		needsRender = true;
		measure(this);
	}

	public void text( String text ){
		this.text = text;

		needsRender = true;
		measure(this);
	}

	public String text(){
		return text;
	}

	public void size( int size ){
		this.size = size;
		needsRender = true;
		measure(this);
	}

	public float baseLine(){
		return size * scale.y;
	}

	public static void measure(){

	}
	protected static synchronized void measure(BitmapText r){

		if ( r.text == null || r.text.equals("") ) {
			r.text = "";
			r.width=r.height=0;
			r.visible = false;
			return;
		} else {
			r.visible = true;
		}

		painter.setTextSize(r.size);
		painter.setAntiAlias(true);

		if (customFont != null && bitmapFont != null) {
			painter.setTypeface(r.size >= 12 ? customFont : bitmapFont);
		} else {
			painter.setTypeface(Typeface.DEFAULT);
		}

		painter.setARGB(0xff, 0, 0, 0);
		painter.setStyle(Paint.Style.STROKE);
		painter.setStrokeWidth(r.size / 5f);

		r.width = (painter.measureText(r.text)+ (r.size/5f));
		r.height = (-painter.ascent() + painter.descent()+ (r.size/5f));
	}

	private static synchronized void render(BitmapText r){
		r.needsRender = false;

		if (r.cache != null)
			r.cache.activeTexts.remove(r);

		String key = "text:" + r.size + " " + r.text;
		if (textCache.containsKey(key)){
			r.cache = textCache.get(key);
			r.texture = r.cache.texture;
			r.frame(r.cache.rect);
			r.cache.activeTexts.add(r);
		} else {

			measure(r);

			if (r.width == 0 || r.height == 0)
				return;

			//bitmap has to be in a power of 2 for some devices (as we're using openGL methods to render to texture)
			Bitmap bitmap = Bitmap.createBitmap(Integer.highestOneBit((int)r.width)*2, Integer.highestOneBit((int)r.height)*2, Bitmap.Config.ARGB_4444);
			bitmap.eraseColor(0x00000000);

			canvas.setBitmap(bitmap);
			canvas.drawText(r.text, (r.size/10f), r.size, painter);

			//paint inner text
			painter.setARGB(0xff, 0xff, 0xff, 0xff);
			painter.setStyle(Paint.Style.FILL);

			canvas.drawText(r.text, (r.size/10f), r.size, painter);

			r.texture = new SmartTexture(bitmap, Texture.NEAREST, Texture.CLAMP, true);

			RectF rect = r.texture.uvRect(0, 0, (int)r.width, (int)r.height);
			r.frame(rect);

			r.cache = new BitmapText.CachedText();
			r.cache.rect = rect;
			r.cache.texture = r.texture;
			r.cache.length = r.text.length();
			r.cache.activeTexts = new HashSet<>();
			r.cache.activeTexts.add(r);

			cachedChars += r.cache.length;
			textCache.put("text:" + r.size + " " + r.text, r.cache);

			if (cachedChars >= GC_TRIGGER){
				runGC();
			}
		}
	}

	@Override
	protected void updateMatrix() {
		super.updateMatrix();
		//the y value is set at the top of the character, not at the top of accents.
		Matrix.translate( matrix, 0, -Math.round((baseLine()*0.15f)/scale.y) );
	}

	@Override
	public void draw() {
		if (needsRender)
			render(this);
		if (texture != null)
			super.draw();
	}

	@Override
	public void destroy() {
		if (cache != null)
			cache.activeTexts.remove(this);
		super.destroy();
	}

	public static void clearCache(){
		for (BitmapText.CachedText cached : textCache.values()){
			cached.texture.delete();
		}
		cachedChars = 0;
		textCache.clear();
	}

	public static void reloadCache(){
		for (BitmapText.CachedText txt : textCache.values()){
			txt.texture.reload();
		}
	}

	public static void setFont(){
		String customPath = fontPath + latinPixelFont;
		if (currentLanguageCode.equals("cn") || currentLanguageCode.equals("ja") || currentLanguageCode.equals("ko")){
			customPath = fontPath + cjkPixelFont;
		}
		String bitmapPath = fontPath + bitmapPixelFont;

		customFont = Typeface.createFromAsset(Game.instance.getAssets(), customPath);
		bitmapFont = Typeface.createFromAsset(Game.instance.getAssets(), bitmapPath);
		clearCache();
	}

	public static Typeface getCustomFont(){
		return customFont;
	}

	public static Typeface getBitmapFont(){
		return bitmapFont;
	}

	private static class CachedText{
		public SmartTexture texture;
		public RectF rect;
		public int length;
		public HashSet<BitmapText> activeTexts;
	}

	public static class Font extends TextureFilm {

		public static final String LATIN_UPPER =
				" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public static final String LATIN_FULL =
				" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007F";

		public SmartTexture texture;

		public float tracking = 0;
		public float baseLine;

		public boolean autoUppercase = false;

		public float lineHeight;

		protected Font( SmartTexture tx ) {
			super( tx );

			texture = tx;
		}

		public Font( SmartTexture tx, int width, String chars ) {
			this( tx, width, tx.height, chars );
		}

		public Font( SmartTexture tx, int width, int height, String chars ) {
			super( tx );

			texture = tx;

			autoUppercase = chars.equals( LATIN_UPPER );

			int length = chars.length();

			float uw = (float)width / tx.width;
			float vh = (float)height / tx.height;

			float left = 0;
			float top = 0;
			float bottom = vh;

			for (int i=0; i < length; i++) {
				RectF rect = new RectF( left, top, left += uw, bottom );
				add( chars.charAt( i ), rect );
				if (left >= 1) {
					left = 0;
					top = bottom;
					bottom += vh;
				}
			}

			lineHeight = baseLine = height;
		}

		protected void splitBy( Bitmap bitmap, int height, int color, String chars ) {

			autoUppercase = chars.equals( LATIN_UPPER );
			int length = chars.length();

			int width = bitmap.getWidth();
			float vHeight = (float)height / bitmap.getHeight();

			int pos;

			spaceMeasuring:
			for (pos=0; pos <  width; pos++) {
				for (int j=0; j < height; j++) {
					if (bitmap.getPixel( pos, j ) != color) {
						break spaceMeasuring;
					}
				}
			}
			add( ' ', new RectF( 0, 0, (float)pos / width, vHeight ) );

			for (int i=0; i < length; i++) {

				char ch = chars.charAt( i );
				if (ch == ' ') {
					continue;
				} else {

					boolean found;
					int separator = pos;

					do {
						if (++separator >= width) {
							break;
						}
						found = true;
						for (int j=0; j < height; j++) {
							if (bitmap.getPixel( separator, j ) != color) {
								found = false;
								break;
							}
						}
					} while (!found);

					add( ch, new RectF( (float)pos / width, 0, (float)separator / width, vHeight ) );
					pos = separator + 1;
				}
			}

			lineHeight = baseLine = height( frames.get( chars.charAt( 0 ) ) );
		}

		public static Font colorMarked( Bitmap bmp, int color, String chars ) {
			Font font = new Font( TextureCache.get( bmp ) );
			font.splitBy( bmp, bmp.getHeight(), color, chars );
			return font;
		}

		public static Font colorMarked( Bitmap bmp, int height, int color, String chars ) {
			Font font = new Font( TextureCache.get( bmp ) );
			font.splitBy( bmp, height, color, chars );
			return font;
		}

		public RectF get( char ch ) {
			return super.get( autoUppercase ? Character.toUpperCase( ch ) : ch );
		}
	}
}
