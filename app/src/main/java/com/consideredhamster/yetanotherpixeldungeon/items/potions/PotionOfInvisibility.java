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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;

public class PotionOfInvisibility extends Potion {

    public static final float DURATION = 15f;

    {
        name = "Potion of Invisibility";
        shortName = "In";
    }

    @Override
    protected void apply(Hero hero) {
        BuffActive.add(hero, Invisibility.class, DURATION);
        setKnown();
    }

    @Override
    public String desc() {
        return Ml.g("items.potions.potionofinvisibility.desc");
    }

    @Override
    public int price() {
        return isTypeKnown() ? 70 * quantity : super.price();
    }

    @Override
    public float brewingChance() {
        return 0.65f;
    }

}
