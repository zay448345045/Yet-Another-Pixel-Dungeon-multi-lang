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
package com.consideredhamster.yetanotherpixeldungeon.items.misc;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfPhaseWarp;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.scenes.InterlevelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class LloydsBeacon extends Item {

    private static final String TXT_PREVENTING = Ml.g("items.misc.lloydsbeacon.txt_preventing");

    private static final String TXT_CREATURES = Ml.g("items.misc.lloydsbeacon.txt_creatures");

    private static final String TXT_RETURN = Ml.g("items.misc.lloydsbeacon.txt_return");

    private static final String TXT_INFO = Ml.g("items.misc.lloydsbeacon.txt_info");

    private static final String TXT_SET = Ml.g("items.misc.lloydsbeacon.txt_set");

    public static final float TIME_TO_USE = 1;

    public static final String AC_SET = Ml.g("items.misc.lloydsbeacon.ac_set");
    public static final String AC_RETURN = Ml.g("items.misc.lloydsbeacon.ac_return");

    private int returnDepth = -1;
    private int returnPos;

    {
        name = "lloyd's beacon";
//		image = ItemSpriteSheet.BEACON;

        unique = true;
    }

    private static final String DEPTH = Ml.g("items.misc.lloydsbeacon.depth");
    private static final String POS = Ml.g("items.misc.lloydsbeacon.pos");

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DEPTH, returnDepth);
        if (returnDepth != -1) {
            bundle.put(POS, returnPos);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        returnDepth = bundle.getInt(DEPTH);
        returnPos = bundle.getInt(POS);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SET);
        if (returnDepth != -1) {
            actions.add(AC_RETURN);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        if (action == AC_SET || action == AC_RETURN) {

//			if (Dungeon.bossLevel()) {
//				hero.spend( LloydsBeacon.TIME_TO_USE );
//				GLog.w( TXT_PREVENTING );
//				return;
//			}

//            if (Dungeon.level.noTeleport()) {
//				hero.spend( LloydsBeacon.TIME_TO_USE );
//				GLog.w( TXT_CREATURES );
//				return;
//			}

            for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
                if (Actor.findChar(hero.pos + Level.NEIGHBOURS8[i]) != null) {
                    GLog.w(TXT_CREATURES);
                    return;
                }
            }
        }

        if (action == AC_SET) {

            returnDepth = Dungeon.depth;
            returnPos = hero.pos;

            hero.spend(LloydsBeacon.TIME_TO_USE);
            hero.busy();

            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.SND_BEACON);

            GLog.i(TXT_RETURN);

        } else if (action == AC_RETURN) {

            if (returnDepth == Dungeon.depth) {

                int pos = returnPos;

                reset(hero);

                ScrollOfPhaseWarp.appear(hero, pos);
                Dungeon.level.press(pos, hero);
                Dungeon.observe();

            } else {

                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                InterlevelScene.returnDepth = returnDepth;
                InterlevelScene.returnPos = returnPos;
                reset(hero);
                Game.switchScene(InterlevelScene.class);

            }


        } else {

            super.execute(hero, action);

        }
    }

    public void reset(Hero ch) {
//		returnDepth = -1;

        returnDepth = Dungeon.depth;
        returnPos = ch.pos;
    }

    private static final Glowing WHITE = new Glowing(0xFFFFFF);

    @Override
    public Glowing glowing() {
        return returnDepth != -1 ? WHITE : null;
    }

    @Override
    public String info() {
        return TXT_INFO + (returnDepth == -1 ? "" : Utils.format(TXT_SET, returnDepth));
    }
}
