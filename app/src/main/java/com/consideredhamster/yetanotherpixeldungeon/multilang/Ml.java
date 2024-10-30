package com.consideredhamster.yetanotherpixeldungeon.multilang;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.watabou.noosa.Game;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Ml {

    private static Map<String, String> translations = new HashMap<>();
    private static final String TRANSLATION_PATH = Ml.g("multilang.ml.translation_path");
    private static final String PREFS_NAME = Ml.g("multilang.ml.prefs_name");
    private static final String LANGUAGE_KEY = Ml.g("multilang.ml.language_key");

    public static void initialize() {
        String savedLanguageCode = loadLanguageSetting();

        if (savedLanguageCode != null) {
            initialize(savedLanguageCode);
        } else {
            String systemLanguageCode = Locale.getDefault().getLanguage();
            String appLanguageCode = mapSystemLanguageToAppLanguage(systemLanguageCode);
            initialize(appLanguageCode);
        }
    }

    public static void initialize(String languageCode) {
        translations.clear();

        saveLanguageSetting(languageCode);

        AssetManager assetManager = Game.instance.getAssets();
        String fileName = Ml.g("multilang.ml.filename", TRANSLATION_PATH, languageCode);

        try (InputStream is = assetManager.open(fileName)) {
            String jsonStr = convertStreamToString(is);
            JSONObject jsonObject = new JSONObject(jsonStr);

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);
                translations.put(key, value);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private static void saveLanguageSetting(String languageCode) {
        Context context = Game.instance.getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LANGUAGE_KEY, languageCode);
        editor.apply();
    }

    private static String loadLanguageSetting() {
        Context context = Game.instance.getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(LANGUAGE_KEY, null);
    }

    public static String g(String key, Object... args) {
        String template = translations.get(key);
        if (template == null) {
            // Key not found; return the key itself or a placeholder
            return key;
        }

        // Replace placeholders {0}, {1}, etc., with the provided arguments
        return formatString(template, args);
    }

    private static String formatString(String template, Object... args) {
        String result = template;
        for (int i = 0; i < args.length; i++) {
            String placeholder = Ml.g("multilang.ml.placeholder", i);
            result = result.replace(placeholder, args[i].toString());
        }
        return result;
    }

    private static String mapSystemLanguageToAppLanguage(String systemLanguageCode) {
        switch (systemLanguageCode) {
            case "es":
                return Ml.g("multilang.ml.mapsystemlanguagetoapplanguage");
            case "fr":
                return Ml.g("multilang.ml.mapsystemlanguagetoapplanguage_2");
            case "pt":
                return Ml.g("multilang.ml.mapsystemlanguagetoapplanguage_3");
            case "ru":
                return Ml.g("multilang.ml.mapsystemlanguagetoapplanguage_4");
            case "ja":
                return Ml.g("multilang.ml.mapsystemlanguagetoapplanguage_5");
            case "zh":
                return Ml.g("multilang.ml.mapsystemlanguagetoapplanguage_6");
            default:
                return Ml.g("multilang.ml.mapsystemlanguagetoapplanguage_7");
        }
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
