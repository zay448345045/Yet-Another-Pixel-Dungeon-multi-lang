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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class AcidResistance extends ElementResistance {

    @Override
    public String toString() {
        return Ml.g("actors.buffs.bonuses.resistances.acidresistance.tostring");
    }

    @Override
    public String statusMessage() {
        return Ml.g("actors.buffs.bonuses.resistances.acidresistance.statusmessage");
    }

    @Override
    public String playerMessage() {
        return Ml.g("actors.buffs.bonuses.resistances.acidresistance.playermessage");
    }

    @Override
    public int icon() {
        return BuffIndicator.RESIST_ACID;
    }

    @Override
    public Class<? extends Element> resistance() {
        return Element.Acid.class;
    }

    @Override
    public String description() {
        return Ml.g("actors.buffs.bonuses.resistances.acidresistance.description");
    }
}
