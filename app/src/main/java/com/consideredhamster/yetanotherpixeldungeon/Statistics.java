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

import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.watabou.utils.Bundle;

public class Statistics {

    public static int goldCollected;
    public static int itemsUpgraded;
    public static int deepestFloor;
    public static int enemiesSlain;
    public static int foodEaten;
    public static int potionsCooked;
    public static int piranhasKilled;
    public static int nightHunt;
    public static int ankhsUsed;

    public static float duration;

    public static boolean qualifiedForNoKilling = false;
    public static boolean completedWithNoKilling = false;

    public static boolean amuletObtained = false;

    public static void reset() {

        goldCollected = 0;
        itemsUpgraded = 0;
        deepestFloor = 0;
        enemiesSlain = 0;
        foodEaten = 0;
        potionsCooked = 0;
        piranhasKilled = 0;
        nightHunt = 0;
        ankhsUsed = 0;

        duration = 0;

        qualifiedForNoKilling = false;

        amuletObtained = false;

    }

    private static final String GOLD = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.gold");
    private static final String UPGRADES = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.upgrades");
    private static final String DEEPEST = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.deepest");
    private static final String SLAIN = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.slain");
    private static final String FOOD = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.food");
    private static final String ALCHEMY = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.alchemy");
    private static final String PIRANHAS = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.piranhas");
    private static final String NIGHT = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.night");
    private static final String ANKHS = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.ankhs");
    private static final String DURATION = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.duration");
    private static final String AMULET = Ml.g("com.consideredhamster.yetanotherpixeldungeon.statistics.amulet");

    public static void storeInBundle(Bundle bundle) {
        bundle.put(GOLD, goldCollected);
        bundle.put(UPGRADES, itemsUpgraded);
        bundle.put(DEEPEST, deepestFloor);
        bundle.put(SLAIN, enemiesSlain);
        bundle.put(FOOD, foodEaten);
        bundle.put(ALCHEMY, potionsCooked);
        bundle.put(PIRANHAS, piranhasKilled);
        bundle.put(NIGHT, nightHunt);
        bundle.put(ANKHS, ankhsUsed);
        bundle.put(DURATION, duration);
        bundle.put(AMULET, amuletObtained);
    }

    public static void restoreFromBundle(Bundle bundle) {
        goldCollected = bundle.getInt(GOLD);
        itemsUpgraded = bundle.getInt(UPGRADES);
        deepestFloor = bundle.getInt(DEEPEST);
        enemiesSlain = bundle.getInt(SLAIN);
        foodEaten = bundle.getInt(FOOD);
        potionsCooked = bundle.getInt(ALCHEMY);
        piranhasKilled = bundle.getInt(PIRANHAS);
        nightHunt = bundle.getInt(NIGHT);
        ankhsUsed = bundle.getInt(ANKHS);
        duration = bundle.getFloat(DURATION);
        amuletObtained = bundle.getBoolean(AMULET);
    }

}
