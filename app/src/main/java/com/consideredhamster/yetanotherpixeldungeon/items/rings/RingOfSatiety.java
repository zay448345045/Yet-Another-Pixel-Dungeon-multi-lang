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
package com.consideredhamster.yetanotherpixeldungeon.items.rings;

import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;

import java.util.Locale;

public class RingOfSatiety extends Ring {

    {
        name = Ml.g("items.rings.ringofsatiety.name");
        shortName = "Sa";
    }

    @Override
    protected RingBuff buff() {
        return new Satiety();
    }

    @Override
    public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if (isIdentified()) {
            mainEffect = String.format(Locale.getDefault(), "%.0f", 100 * Ring.effect(bonus) / 2);
            sideEffect = String.format(Locale.getDefault(), "%.0f", 100 * Ring.effect(bonus) / 3);
        }

        StringBuilder desc = new StringBuilder(
                Ml.g("items.rings.ringofsatiety.desc_1")
        );

        if (!dud) {

            desc.append("\n\n");

            desc.append(super.desc());

            desc.append(" ");

            desc.append(
                    Ml.g("items.rings.ringofsatiety.desc_2", mainEffect, sideEffect)
            );
        }
        return desc.toString();
    }

    public class Satiety extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                    Ml.g("items.rings.ringofsatiety.satiety.desc_1") :
                    Ml.g("items.rings.ringofsatiety.satiety.desc_2");
        }
    }
}
