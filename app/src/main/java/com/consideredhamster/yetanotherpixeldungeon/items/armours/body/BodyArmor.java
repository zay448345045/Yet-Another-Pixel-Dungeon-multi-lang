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
package com.consideredhamster.yetanotherpixeldungeon.items.armours.body;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Durability;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Featherfall;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HeroSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.watabou.utils.GameMath;

public abstract class BodyArmor extends Armour {

    public int appearance;

    public BodyArmor(int tier) {

        super(tier);

    }

    @Override
    public boolean doEquip(Hero hero) {

        detach(hero.belongings.backpack);

        if ((hero.belongings.armor == null || hero.belongings.armor.doUnequip(hero, true, false)) &&
                (bonus >= 0 || isCursedKnown() || !detectCursed(this, hero))) {

            hero.belongings.armor = this;

            ((HeroSprite) hero.sprite).updateArmor();

            onEquip(hero);

            hero.spendAndNext(time2equip(hero));

            return true;

        } else {

            QuickSlot.refresh();
            hero.spendAndNext(time2equip(hero) * 0.5f);
            if (!collect(hero.belongings.backpack)) {
                Dungeon.level.drop(this, hero.pos).sprite.drop();
            }
            return false;

        }
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)) {

            hero.belongings.armor = null;

            ((HeroSprite) hero.sprite).updateArmor();

            return true;

        } else {

            return false;

        }
    }

    @Override
    public boolean isEquipped(Hero hero) {
        return hero.belongings.armor == this;
    }

    @Override
    public int maxDurability() {
        return 150;
    }

    @Override
    protected float time2equip(Hero hero) {
        return super.speedFactor(hero) * 3;
    }

    @Override
    public int dr(int bonus) {
        return tier * (4 + state)
                + (glyph instanceof Durability || bonus >= 0 ? tier * bonus : 0)
                + (glyph instanceof Durability && bonus >= 0 ? 2 + bonus : 0);
    }

    @Override
    public int str(int bonus) {
        return 9 + tier * 4 - bonus * (glyph instanceof Featherfall ? 2 : 1);
    }

    @Override
    public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown(isIdentified());
        float penalty = GameMath.gate(0, penaltyBase(Dungeon.hero, strShown(isIdentified())), 20) * 2.5f;
        float armor = Math.max(0, isIdentified() ? dr() : dr(0));

        StringBuilder info = new StringBuilder(desc());

        // if( !descType().isEmpty() ) {
        //
        //     info.append( p );
        //
        //     info.append( descType() );
        // }

        info.append(p);

        if (isIdentified()) {
            info.append(Ml.g("items.armours.body.bodyarmor.info_3", tier, !descType().isEmpty() ? descType() : "", itemStr,
                    isRepairable() ? Ml.g("items.armours.body.bodyarmor.info_repairable", stateToString(state)) : "",
                    armor));

            info.append(p);

            if (itemStr > heroStr) {
                info.append(Ml.g("items.armours.body.bodyarmor.info_4", penalty, (int) (100 - 10000 / (100 + penalty))));
            } else if (itemStr < heroStr) {
                String decrease = penalty > 0 ? Ml.g("items.armours.body.bodyarmor.info_decrease_only", penalty) : Ml.g("items.armours.body.bodyarmor.info_not_decreased");
                info.append(Ml.g("items.armours.body.bodyarmor.info_5", decrease, ((float) (heroStr - itemStr) / 2)));
            } else {
                String decreased = penalty > 0 ? Ml.g("items.armours.body.bodyarmor.info_decreased_by", penalty) +
                        " " + Ml.g("items.armours.body.bodyarmor.info_penalty_reduced") : Ml.g("items.armours.body.bodyarmor.info_not_decreased");
                info.append(Ml.g("items.armours.body.bodyarmor.info_6", decreased));
            }
        } else {
            info.append(Ml.g("items.armours.body.bodyarmor.info_7", tier, !descType().isEmpty() ? descType() : "", itemStr,
                    isRepairable() ? Ml.g("items.armours.body.bodyarmor.info_repairable", stateToString(state)) : "",
                    armor));

            info.append(p);

            if (itemStr > heroStr) {
                info.append(Ml.g("items.armours.body.bodyarmor.info_8", penalty, (int) (100 - 10000 / (100 + penalty))));
            } else if (itemStr < heroStr) {
                String decrease = penalty > 0 ? Ml.g("items.armours.body.bodyarmor.info_decrease_only", penalty) : Ml.g("items.armours.body.bodyarmor.info_not_decreased");
                info.append(Ml.g("items.armours.body.bodyarmor.info_9", decrease, ((float) (heroStr - itemStr) / 2)));
            } else {
                String decreased = penalty > 0 ? Ml.g("items.armours.body.bodyarmor.info_decreased_by", penalty) : Ml.g("items.armours.body.bodyarmor.info_not_decreased");
                info.append(Ml.g("items.armours.body.bodyarmor.info_10", decreased));
            }
        }

        info.append(p);

        if (isEquipped(Dungeon.hero)) {
            info.append(Ml.g("items.armours.body.bodyarmor.info_11", name));
        } else if (Dungeon.hero.belongings.backpack.contains(this)) {
            info.append(Ml.g("items.armours.body.bodyarmor.info_12", name));
        } else {
            info.append(Ml.g("items.armours.body.bodyarmor.info_13", name));
        }

        info.append(s);

        if (isIdentified() && bonus > 0) {
            info.append(Ml.g("items.armours.body.bodyarmor.info_14"));
        } else if (isCursedKnown()) {
            if (bonus >= 0) {
                info.append(Ml.g("items.armours.body.bodyarmor.info_15"));
            } else {
                info.append(Ml.g("items.armours.body.bodyarmor.info_16", name));
            }
        } else {
            info.append(Ml.g("items.armours.body.bodyarmor.info_17", name));
        }

        info.append(s);

        if (isEnchantKnown() && glyph != null) {
            String prefix = (isIdentified() && bonus != 0) ? Ml.g("items.armours.body.bodyarmor.info_18_prefix_also") : Ml.g("items.armours.body.bodyarmor.info_18_prefix_however");
            info.append(Ml.g("items.armours.body.bodyarmor.info_18", prefix, glyph.desc(this)));
        }

        info.append(Ml.g("items.armours.body.bodyarmor.info_19", lootChapterAsString()));

        return info.toString();
    }

    @Override
    public int price() {
        int price = 20 + state * 10;

        price *= lootChapter() + 1;

        if (isIdentified()) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6;
        } else if (isCursedKnown() && bonus >= 0) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        if (glyph != null && isEnchantKnown()) {
            price += price / 4;
        }

        return price;
    }
}
