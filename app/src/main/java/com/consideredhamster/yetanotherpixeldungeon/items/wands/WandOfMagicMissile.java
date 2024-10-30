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
package com.consideredhamster.yetanotherpixeldungeon.items.wands;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Splash;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.SparkParticle;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class WandOfMagicMissile extends WandCombat {

    {
        name = "Wand of Magic Missile";
        image = ItemSpriteSheet.WAND_MAGICMISSILE;
    }

    @Override
    public float effectiveness(int bonus) {
        return super.effectiveness(bonus) * 1.50f;
    }

    @Override
    protected void onZap(int cell) {

        int power = damageRoll();

        Splash.at(cell, 0x33FFFFFF, (int) Math.sqrt(power) + 2);

        Char ch = Actor.findChar(cell);

        if (ch != null) {

            if (Char.hit(curUser, ch, true, true)) {

                ch.damage(Char.absorb(damageRoll(), ch.armorClass()), curUser, Element.ENERGY);
                ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, Random.IntRange(2 + power / 10, 4 + power / 5));

            } else {

                ch.missed();

            }
        }
    }

    @Override
    public String desc() {
        return Ml.g("items.wands.wandofmagicmissile.desc");
    }

}
