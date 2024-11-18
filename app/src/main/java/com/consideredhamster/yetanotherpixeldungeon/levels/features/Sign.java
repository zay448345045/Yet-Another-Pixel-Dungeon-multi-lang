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
package com.consideredhamster.yetanotherpixeldungeon.levels.features;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.levels.DeadEndLevel;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndMessage;

public class Sign {

    private static final String TXT_DEAD_END = Ml.g("levels.features.sign.txt_dead_end");

    private static final String[] TIPS = {

            Ml.g("levels.features.sign.TIPS_1"),
            Ml.g("levels.features.sign.TIPS_2"),
            Ml.g("levels.features.sign.TIPS_3"),
            Ml.g("levels.features.sign.TIPS_4"),
            Ml.g("levels.features.sign.TIPS_5"),
            Ml.g("levels.features.sign.TIPS_6"),

            Ml.g("levels.features.sign.TIPS_7"),
            Ml.g("levels.features.sign.TIPS_8"),
            Ml.g("levels.features.sign.TIPS_9"),
            Ml.g("levels.features.sign.TIPS_10"),
            Ml.g("levels.features.sign.TIPS_11"),
            Ml.g("levels.features.sign.TIPS_12"),

            Ml.g("levels.features.sign.TIPS_13"),
            Ml.g("levels.features.sign.TIPS_14"),
            Ml.g("levels.features.sign.TIPS_15"),
            Ml.g("levels.features.sign.TIPS_16"),
            Ml.g("levels.features.sign.TIPS_17"),
            Ml.g("levels.features.sign.TIPS_18"),

            Ml.g("levels.features.sign.TIPS_19"),
            Ml.g("levels.features.sign.TIPS_20"),
            Ml.g("levels.features.sign.TIPS_21"),
            Ml.g("levels.features.sign.TIPS_22"),
            Ml.g("levels.features.sign.TIPS_23"),
            Ml.g("levels.features.sign.TIPS_24"),

            Ml.g("levels.features.sign.TIPS_25"),

            Ml.g("levels.features.sign.TIPS_26"),
            Ml.g("levels.features.sign.TIPS_27"),
            Ml.g("levels.features.sign.TIPS_28"),
            Ml.g("levels.features.sign.TIPS_29"),

            "",

            "",

//            "greetings, mortal" +
//                "\n\nare you ready to die?",
//            "my servants can smell your blood, human",
//            "worship me, and i may yet be merciful" +
//                "\n\nthen again, maybe not",
//            "you have played this game for too long, mortal" +
//                "\n\ni think i shall remove you from the board"
    };

    private static final String TXT_NOMESSAGE = Ml.g("levels.features.sign.txt_nomessage");

    //	public static void read( int pos ) {
    public static void read() {

        if (Dungeon.level instanceof DeadEndLevel) {

            GameScene.show(new WndMessage(TXT_DEAD_END));

        } else {

            int index = Dungeon.depth - 1;

            if (index < TIPS.length && TIPS[index] != "") {
                GameScene.show(new WndMessage(TIPS[index]));
            } else {
                GameScene.show(new WndMessage(TXT_NOMESSAGE));
//				Level.set( pos, Terrain.EMBERS );
//				GameScene.updateMap( pos );
//				GameScene.discoverTile( pos, Terrain.SIGN );

//				CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
//				Sample.INSTANCE.play( Assets.SND_BURNING );

//				GLog.w( TXT_BURN );

            }
        }
    }
}
