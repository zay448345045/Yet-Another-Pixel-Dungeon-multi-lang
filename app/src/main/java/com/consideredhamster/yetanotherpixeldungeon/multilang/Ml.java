package com.consideredhamster.yetanotherpixeldungeon.multilang;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import com.watabou.noosa.BitmapText;
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
    private static final String TRANSLATION_PATH = "multilang/";
    private static final String PREFS_NAME = "LocalizationPrefs";
    private static final String LANGUAGE_KEY = "language";

    public static void initialize() {
        String savedLanguageCode = loadLanguageSetting();


//        initialize("cn");

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
        BitmapText.currentLanguageCode = languageCode;
        BitmapText.setFont();

        AssetManager assetManager = Game.instance.getAssets();
        String fileName = TRANSLATION_PATH + languageCode + ".json";

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

    public static void saveLanguageSetting(String languageCode) {
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
            String placeholder = "{" + i + "}";
            result = result.replace(placeholder, args[i].toString());
        }
//        Log.i("multilang", "formatString: " + result);
        return result;
    }

    private static String mapSystemLanguageToAppLanguage(String systemLanguageCode) {
        switch (systemLanguageCode) {
            case "es":
                return "es";
            case "fr":
                return "fr";
            case "pt":
                return "pt";
            case "ru":
                return "ru";
            case "ja":
                return "ja";
            case "zh":
                return "cn";
            default:
                return "en";
        }
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
