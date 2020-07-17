package com.sharpirate.notime.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {

    private static final Preferences prefs = Gdx.app.getPreferences("com.sharpirate.notime.prefs");

    public static short readScore() {
        return Short.valueOf(prefs.getString("score", "0"));
    }

    public static void writeScore(short score) {
        if(readScore() < score) {
            writeString("score", String.valueOf(score));
        }
    }

    private static void writeString(String key, String value) {
        prefs.putString(key, value);
        prefs.flush();
    }
}
