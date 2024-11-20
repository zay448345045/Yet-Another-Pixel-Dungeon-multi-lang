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

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.watabou.glwrap.Quad;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.PointF;

import android.graphics.RectF;
import android.util.Log;

public class BitmapTextMultiline extends Component {

	public boolean[] mask;

	public int maxWidth = Integer.MAX_VALUE;
	public int nLines;

	private String text;
	private List<String> tokens;
	private ArrayList<BitmapText> words = new ArrayList<>();

	private int size;
	private float zoom;
	private int color = -1;

	private static final String SPACE = " ";
	private static final String NEWLINE = "\n";
	private static final String UNDERSCORE = "_";

	private boolean chinese = false;

	public BitmapTextMultiline(String text, BitmapText.Font font) {
		this.size = 5;
		text(text);
	}

	public BitmapTextMultiline(int size) {
		this.size = size;
	}

	public BitmapTextMultiline(String text, int size) {
		this.size = size;
		text(text);
	}

	public void text(String text) {
		this.text = text;

		if (text != null && !text.equals("")) {
			//conversion for chinese text

			chinese = text.replaceAll("\\p{script=Han}", "").length() != text.length();

			if (chinese) {
				tokens = Arrays.asList(text.split(""));
			} else {
				tokens = Arrays.asList(text.split("(?<= )|(?= )|(?<=\n)|(?=\n)"));
			}
			build();
		}
	}

	public void text(String text, int maxWidth) {
		this.maxWidth = maxWidth;
		text(text);
	}

	public String text() {
		return text;
	}

	public void maxWidth(int maxWidth) {
		if (this.maxWidth != maxWidth) {
			this.maxWidth = maxWidth;
			layout();
		}
	}

	public int maxWidth() {
		return maxWidth;
	}

	private synchronized void build() {
		clear();
		words = new ArrayList<>();
		boolean highlighting = false;
		for (String str : tokens) {
			if (str.equals(UNDERSCORE)) {
				highlighting = !highlighting;
			} else if (str.equals(NEWLINE)) {
				words.add(null);
			} else if (!str.equals(SPACE)) {
				BitmapText word;

				int start = 0;
				int end = str.length();

				while (start < end && (str.charAt(start) == ',' || str.charAt(start) == '.')) {
					start++;
				}
				while (end > start && (str.charAt(end - 1) == ',' || str.charAt(end - 1) == '.')) {
					end--;
				}

				String coreStr = str.substring(start, end);

				boolean startsWithUnderscore = coreStr.startsWith(UNDERSCORE);
				boolean endsWithUnderscore = coreStr.endsWith(UNDERSCORE);

				if (startsWithUnderscore) {
					coreStr = coreStr.substring(1);
				}
				if (endsWithUnderscore) {
					coreStr = coreStr.substring(0, coreStr.length() - 1);
				}

				String reconstructedStr = str.substring(0, start) + coreStr + str.substring(end);

				if (startsWithUnderscore && endsWithUnderscore) {
					word = new BitmapText(reconstructedStr, size);
					word.hardlight(0xFFFF44);
				} else {
					if (startsWithUnderscore) {
						highlighting = !highlighting;
					}
					word = new BitmapText(reconstructedStr, size);
					if (highlighting) {
						word.hardlight(0xFFFF44);
					} else if (color != -1) {
						word.hardlight(color);
					}
					if (endsWithUnderscore) {
						highlighting = !highlighting;
					}
				}

				word.scale.set(zoom);
				words.add(word);
				add(word);

				if (height < word.baseLine()) {
					height = word.baseLine();
				}
			}
		}
		layout();
	}

	public synchronized void zoom(float zoom) {
		this.zoom = zoom;
		for (BitmapText word : words) {
			if (word != null) word.scale.set(zoom);
		}
	}

	public synchronized void hardlight(int color) {
		this.color = color;
		for (BitmapText word : words) {
			if (word != null) word.hardlight(color);
		}
	}

	public synchronized void invert() {
		if (words != null) {
			for (BitmapText word : words) {
				if (word != null) {
					word.ra = 0.77f;
					word.ga = 0.73f;
					word.ba = 0.62f;
					word.rm = -0.77f;
					word.gm = -0.73f;
					word.bm = -0.62f;
				}
			}
		}
	}

	public void measure(){
		layout();
	}

	@Override
	public synchronized void layout() {
		super.layout();
		float x = this.x;
		float y = this.y;
		float height = 0;
		nLines = 1;

		for (BitmapText word : words) {
			if (word == null) {
				//newline
				y += height + 0.4f;
				x = this.x;
				nLines++;
			} else {
				if (word.height() > height) height = word.baseLine();

				if ((x - this.x) + word.width() > maxWidth) {
					y += height + 0.4f;
					x = this.x;
					nLines++;
				}

				word.x = x;
				word.y = y;
//				PixelScene.align(word);
				x += word.width();
				if (!chinese) x++;
				else x--;

				if ((x - this.x) > width) width = (x - this.x);

			}
		}
		this.height = (y - this.y) + height + 0.4f;
	}
}
