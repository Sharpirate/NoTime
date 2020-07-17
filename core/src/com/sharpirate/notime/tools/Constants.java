package com.sharpirate.notime.tools;

import com.badlogic.gdx.math.Vector2;

public final class Constants {

    public static final byte DEADS_FOR_ADS = 5;

    public static final float PPM = 125f;
    public static final Vector2 GRAVITY = new Vector2(0, -9.8f);
    public static final float STEP = 1/120f;

    public static final short BIT_PLAYER = 2;
    public static final short BIT_PLATFORM = 8;
    public static final short BIT_COLLIDER = 16;

    public static final float WORLD_WIDTH = 890f;
    public static final float WORLD_HEIGHT = 668f;

    public static float touchAreas[] = new float[3];
    public static float drawAreas[] = new float[3];

    public static void initAreas(float[] areas, float height) {
        for(byte index = 0; index < areas.length; index++) {
            if(index == 0) {
                areas[index] = height / areas.length;
            }
            else {
                areas[index] = areas[index-1] + areas[0];
            }
        }
    }
}
