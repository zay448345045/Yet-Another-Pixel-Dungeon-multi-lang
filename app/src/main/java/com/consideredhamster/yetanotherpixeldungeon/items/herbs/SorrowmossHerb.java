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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.AcidResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfCausticOoze;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfToxicGas;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWebbing;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class SorrowmossHerb extends Herb {

    private static final ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x009900);

    {
        name = Ml.g("items.herbs.sorrowmossherb.name");
        image = ItemSpriteSheet.HERB_SORROWMOSS;

        cooking = BitterMeat.class;
message = Ml.g("items.herbs.sorrowmossherb.message");

        mainPotion = PotionOfCausticOoze.class;

        subPotions.add(PotionOfToxicGas.class);
        subPotions.add(PotionOfWebbing.class);
    }

    private static void onConsume(Hero hero, float duration) {
        BuffActive.add(hero, AcidResistance.class, duration);
        Debuff.remove(hero, Corrosion.class);
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
        return Ml.g("items.herbs.sorrowmossherb.desc");
    }

    public static class BitterMeat extends MeatStewed {

        {
            name = Ml.g("items.herbs.sorrowmossherb.name_2");
            spiceGlow = GREEN;
message = Ml.g("items.herbs.sorrowmossherb.message_2");
        }

        @Override
        public void onConsume(Hero hero) {
            super.onConsume(hero);
            SorrowmossHerb.onConsume(hero, DURATION_MEAT);
        }

        @Override
        public String desc() {
            return Ml.g("items.herbs.sorrowmossherb.desc_2");
        }

        @Override
        public int price() {
            return 30 * quantity;
        }
    }
}

