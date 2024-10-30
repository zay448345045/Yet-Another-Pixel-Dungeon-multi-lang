/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
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
package com.consideredhamster.yetanotherpixeldungeon;

import android.content.SharedPreferences;

import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.watabou.noosa.Game;

enum Preferences {

    INSTANCE;

    public static final String KEY_LANDSCAPE = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_landscape");
    public static final String KEY_IMMERSIVE = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_immersive");
    public static final String KEY_GOOGLE_PLAY = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_google_play");
    public static final String KEY_SCALE_UP = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_scale_up");
    public static final String KEY_MUSIC = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_music");
    public static final String KEY_BUTTONS = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_buttons");
    public static final String KEY_SOUND_FX = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_sound_fx");
    public static final String KEY_ZOOM = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_zoom");
    public static final String KEY_LAST_CLASS = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_last_class");
    public static final String KEY_CHALLENGES = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_challenges");
    public static final String KEY_DIFFICULTY = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_difficulty");
    public static final String KEY_DONATED = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_donated");
    public static final String KEY_INTRO = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_intro");
    public static final String KEY_BRIGHTNESS = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_brightness");
    public static final String KEY_SEARCH_BTN = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_search_btn");
    public static final String KEY_LOADING_TIPS = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_loading_tips");
    public static final String KEY_LAST_VERSION = Ml.g("com.consideredhamster.yetanotherpixeldungeon.unknownclass.key_last_version");

    private SharedPreferences prefs;

    private SharedPreferences get() {
        if (prefs == null) {
            prefs = Game.instance.getPreferences(Game.MODE_PRIVATE);
        }
        return prefs;
    }

    int getInt(String key, int defValue) {
        return get().getInt(key, defValue);
    }

    boolean getBoolean(String key, boolean defValue) {
        return get().getBoolean(key, defValue);
    }

    String getString(String key, String defValue) {
        return get().getString(key, defValue);
    }

    void put(String key, int value) {
        get().edit().putInt(key, value).commit();
    }

    void put(String key, boolean value) {
        get().edit().putBoolean(key, value).commit();
    }

    void put(String key, String value) {
        get().edit().putString(key, value).commit();
    }
}
