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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.MindResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Tormented;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfConfusionGas;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfMindVision;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfRage;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class DreamfoilHerb extends Herb {

    private static final ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0xAA00AA);

    {
        name = Ml.g("items.herbs.dreamfoilherb.name");
        image = ItemSpriteSheet.HERB_DREAMFOIL;

        cooking = SweetMeat.class;
        message = Ml.g("items.herbs.dreamfoilherb.message");

        mainPotion = PotionOfMindVision.class;

        subPotions.add(PotionOfConfusionGas.class);
        subPotions.add(PotionOfRage.class);
    }

    public static void onConsume(Hero hero, float duration) {

        BuffActive.add(hero, MindResistance.class, duration);

        Debuff.remove(hero, Vertigo.class);
        Debuff.remove(hero, Charmed.class);
        Debuff.remove(hero, Tormented.class);

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
        return Ml.g("items.herbs.dreamfoilherb.desc");
    }

    public static class SweetMeat extends MeatStewed {

        {
            name = Ml.g("items.herbs.dreamfoilherb.name_2");
            spiceGlow = PURPLE;
            message = Ml.g("items.herbs.dreamfoilherb.message_2");
        }

        @Override
        public void onConsume(Hero hero) {
            super.onConsume(hero);
            DreamfoilHerb.onConsume(hero, DURATION_MEAT);
        }

        @Override
        public String desc() {
            return Ml.g("items.herbs.dreamfoilherb.desc_2");
        }

        @Override
        public int price() {
            return 20 * quantity;
        }
    }
}

