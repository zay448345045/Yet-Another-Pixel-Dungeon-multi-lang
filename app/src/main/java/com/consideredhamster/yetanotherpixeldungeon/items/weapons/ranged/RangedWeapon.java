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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.ThrowingWeaponAmmo;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;

public abstract class RangedWeapon extends Weapon {

    public RangedWeapon(int tier) {

        super();

        this.tier = tier;

    }

    protected static final String AC_SHOOT = Ml.g("items.weapons.ranged.rangedweapon.ac_shoot");
    protected static final String TXT_SELF_TARGET = Ml.g("items.weapons.ranged.rangedweapon.txt_self_target");
    protected static final String TXT_TARGET_CHARMED = Ml.g("items.weapons.ranged.rangedweapon.txt_target_charmed");
    protected static final String TXT_NOTEQUIPPED = Ml.g("items.weapons.ranged.rangedweapon.txt_notequipped");
    protected static final String TXT_AMMO_NEEDED = Ml.g("items.weapons.ranged.rangedweapon.txt_ammo_needed");

    public Class<? extends ThrowingWeaponAmmo> ammunition() {
        return null;
    }

    public boolean checkAmmo(Hero hero, boolean showMessage) {
        return false;
    }

    @Override
    public String status() {
        return isEquipped(Dungeon.hero) && ammunition().isInstance(Dungeon.hero.belongings.weap2) ? Integer.toString(Dungeon.hero.belongings.weap2.quantity) : null;
    }

    @Override
    public String equipAction() {
        return AC_SHOOT;
    }

    @Override
    public String quickAction() {
        return isEquipped(Dungeon.hero) ? AC_UNEQUIP : AC_EQUIP;
    }

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add( AC_SHOOT );
//        return actions;
//    }

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isRepairable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isEnchantKnown() {
        return known >= ENCHANT_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

//	@Override
//	public Item random() {
//		super.random();
//
//        bonus = Random.NormalIntRange( -3, +3 );
//
//		if (Random.Int( 7 + bonus ) == 0) {
//			enchant();
//		}
//
//        randomize_state();
//
//		return this;
//	}

    @Override
    public float stealingDifficulty() {
        return 0.75f;
    }

    @Override
    public int price() {

        int price = 20 + state * 10;

        price *= lootChapter();

        if (isIdentified()) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6;
        } else if (isCursedKnown() && bonus >= 0) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        if (enchantment != null && isEnchantKnown()) {
            price += price / 4;
        }

        return price;
    }
}
