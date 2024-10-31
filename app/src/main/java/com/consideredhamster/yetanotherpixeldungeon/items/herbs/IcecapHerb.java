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
package com.consideredhamster.yetanotherpixeldungeon.items.herbs;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.ColdResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfConfusionGas;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfFrigidVapours;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfInvisibility;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class IcecapHerb extends Herb {

    private static final ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x2244FF);

    {
        name = Ml.g("items.herbs.icecapherb.name");
        image = ItemSpriteSheet.HERB_ICECAP;

        cooking = MintyMeat.class;
        message = Ml.g("items.herbs.icecapherb.message");

        mainPotion = PotionOfFrigidVapours.class;

        subPotions.add(PotionOfConfusionGas.class);
        subPotions.add(PotionOfInvisibility.class);
    }

    private static void onConsume(Hero hero, float duration) {

        BuffActive.add(hero, ColdResistance.class, duration);
        Debuff.remove(hero, Frozen.class);

    }

    @Override
    public void onConsume(Hero hero) {
        super.onConsume(hero);
        onConsume(hero, DURATION_HERB);
    }

    @Override
    public int price() {
        return 15 * quantity;
    }

    @Override
    public String desc() {
        return Ml.g("items.herbs.icecapherb.desc");
    }

    public static class MintyMeat extends MeatStewed {

        {
            name = Ml.g("items.herbs.icecapherb.name_2");
            spiceGlow = BLUE;
            message = Ml.g("items.herbs.icecapherb.message_2");
        }

        @Override
        public void onConsume(Hero hero) {
            super.onConsume(hero);
            IcecapHerb.onConsume(hero, DURATION_MEAT);
        }

        @Override
        public String desc() {
            return Ml.g("items.herbs.icecapherb.desc_2");
        }

        @Override
        public int price() {
            return 30 * quantity;
        }
    }
}

