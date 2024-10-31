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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfPhaseWarp extends Scroll {

    public static final String TXT_TELEPORTED_VISITED = Ml.g("items.scrolls.scrollofphasewarp.txt_teleported_visited");

    public static final String TXT_TELEPORTED_UNKNOWN = Ml.g("items.scrolls.scrollofphasewarp.txt_teleported_unknown");

    public static final String TXT_NO_TELEPORT = Ml.g("items.scrolls.scrollofphasewarp.txt_no_teleport");

    {
        name = Ml.g("items.scrolls.scrollofphasewarp.name");
        shortName = "Ph";

        spellSprite = SpellSprite.SCROLL_TELEPORT;
        spellColour = SpellSprite.COLOUR_WILD;
    }

    @Override
    protected void doRead() {

        int pos = curUser.pos;

//        pos = Dungeon.level.randomRespawnCell( false, true );

        ArrayList<Integer> cells = new ArrayList<>();

        for (int cell = 0; cell < Level.LENGTH; cell++) {

            if (!Level.solid[cell] && Level.passable[cell] && Actor.findChar(cell) == null && Level.distance(pos, cell) > 4) {
                cells.add(cell);
            }
        }

        while (pos == curUser.pos || !PathFinder.buildDistanceMap(curUser.pos, pos, Level.passable)) {

            pos = Random.element(cells);

        }

        if (pos == -1) {

            GLog.w(TXT_NO_TELEPORT);

        } else {

            ScrollOfPhaseWarp.appear(curUser, pos);
            Dungeon.level.press(pos, curUser);

            BuffActive.add(curUser, Vertigo.class, Random.Float(5f, 10f));
            Dungeon.observe();

        }

//        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
//        Game.switchScene( InterlevelScene.class );

        super.doRead();
    }

    public static void appear(Char ch, int pos) {

        ch.sprite.interruptMotion();

        ch.move(pos);
        ch.sprite.place(pos);

        if (ch.invisible == 0) {
            ch.sprite.alpha(0);
            ch.sprite.parent.add(new AlphaTweener(ch.sprite, 1, 0.4f));
        }

        ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
        Sample.INSTANCE.play(Assets.SND_TELEPORT);
    }

    @Override
    public String desc() {
        return Ml.g("items.scrolls.scrollofphasewarp.desc");
    }

    @Override
    public int price() {
        return isTypeKnown() ? 75 * quantity : super.price();
    }
}
