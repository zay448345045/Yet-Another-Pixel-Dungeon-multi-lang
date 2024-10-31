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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.MagicalResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfInvisibility;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfLevitation;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWisdom;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.UnstablePotion;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class FeyleafHerb extends Herb {

    private static final ItemSprite.Glowing CYAN = new ItemSprite.Glowing(0xa1ffff);

    {
        name = Ml.g("items.herbs.feyleafherb.name");
        image = ItemSpriteSheet.HERB_FEYLEAF;

        cooking = TenderMeat.class;
        message = "That herb had a very delicate taste.";

        //these herbs cannot be brewed with themselves
        mainPotion = UnstablePotion.class;

        subPotions.add(PotionOfInvisibility.class);
        subPotions.add(PotionOfLevitation.class);
        subPotions.add(PotionOfWisdom.class);
    }

    private static void onConsume(Hero hero, float duration) {

        BuffActive.add(hero, MagicalResistance.class, duration);

    }

    @Override
    public void onConsume(Hero hero) {
        super.onConsume(hero);
        onConsume(hero, DURATION_HERB);
    }

    @Override
    public int price() {
        return 20 * quantity;
    }

    @Override
    public String desc() {
        return Ml.g("items.herbs.feyleafherb.desc");
    }

    public static class TenderMeat extends MeatStewed {

        {
            name = Ml.g("items.herbs.feyleafherb.name_2");
            spiceGlow = CYAN;
            message = "That meat had a very delicate taste.";
        }

        @Override
        public void onConsume(Hero hero) {
            super.onConsume(hero);
            FeyleafHerb.onConsume(hero, DURATION_MEAT);
        }

        @Override
        public String desc() {
            return Ml.g("items.herbs.feyleafherb.desc_2");
        }

        @Override
        public int price() {
            return 40 * quantity;
        }

    }
}

