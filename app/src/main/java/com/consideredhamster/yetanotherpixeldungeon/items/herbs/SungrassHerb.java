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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.BodyResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfBlessing;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfMending;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfShield;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;


public class SungrassHerb extends Herb {

    private static final ItemSprite.Glowing YELLOW = new ItemSprite.Glowing(0xFFFF44);

    {
        name = Ml.g("items.herbs.sungrassherb.name");
        image = ItemSpriteSheet.HERB_SUNGRASS;

        cooking = SavoryMeat.class;
message = Ml.g("items.herbs.sungrassherb.message");

        mainPotion = PotionOfMending.class;

        subPotions.add(PotionOfBlessing.class);
        subPotions.add(PotionOfShield.class);
    }

    private static void onConsume(Hero hero, float duration) {

        BuffActive.add(hero, BodyResistance.class, duration);

        Debuff.remove(hero, Poisoned.class);
        Debuff.remove(hero, Crippled.class);
        Debuff.remove(hero, Withered.class);

    }

    @Override
    public void onConsume(Hero hero) {
        super.onConsume(hero);
        onConsume(hero, DURATION_HERB);
    }

    @Override
    public int price() {
        return 10 * quantity;
    }

    @Override
    public String desc() {
        return Ml.g("items.herbs.sungrassherb.desc");
    }

    public static class SavoryMeat extends MeatStewed {

        {
            name = Ml.g("items.herbs.sungrassherb.name_2");
            spiceGlow = YELLOW;
message = Ml.g("items.herbs.sungrassherb.message_2");
        }

        @Override
        public void onConsume(Hero hero) {
            super.onConsume(hero);
            SungrassHerb.onConsume(hero, DURATION_MEAT);
        }

        @Override
        public String desc() {
            return Ml.g("items.herbs.sungrassherb.desc_2");
        }

        @Override
        public int price() {
            return 20 * quantity;
        }
    }
}
