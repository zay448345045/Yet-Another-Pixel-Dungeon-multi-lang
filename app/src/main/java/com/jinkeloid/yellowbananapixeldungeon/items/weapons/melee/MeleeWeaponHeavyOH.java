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

package com.jinkeloid.yellowbananapixeldungeon.items.weapons.melee;

import com.jinkeloid.yellowbananapixeldungeon.actors.hero.Hero;
import com.jinkeloid.yellowbananapixeldungeon.items.weapons.enchantments.Tempered;

public abstract class MeleeWeaponHeavyOH extends MeleeWeapon {

    public MeleeWeaponHeavyOH(int tier) {

        super(tier);

    }

    @Override
    public String descType() {
//        return "This is a _tier-" + appearance + " heavy one-handed weapon_. It can be paired with any weapon, shield or wand " +
//                "without any strength requirement penalties.";
        return "heavy one-handed";
    }

    @Override
    public int min( int bonus ) {
        return super.min(bonus) + ( enchantment instanceof Tempered ? 1 : 0 ) + 1;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) + ( enchantment instanceof Tempered ? 1 : 0 ) + 4;
    }

    @Override
    public int dmgMod() {
        return super.dmgMod() + 1 ;
    }

    @Override
    public int str(int bonus) {
        return super.str(bonus) + 3;
    }

    @Override
    public int penaltyBase(Hero hero, int str) {
        return super.penaltyBase(hero, str) - 4;
    }

}
