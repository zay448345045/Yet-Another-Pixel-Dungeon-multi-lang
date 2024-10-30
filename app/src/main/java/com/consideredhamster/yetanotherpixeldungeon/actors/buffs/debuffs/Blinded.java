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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs;

import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Blinded extends Debuff {

    public final static String TXT_CANNOT_READ = Ml.g("actors.buffs.debuffs.blinded.txt_cannot_read");

    @Override
    public String toString() {
        return Ml.g("actors.buffs.debuffs.blinded.tostring");
    }

    @Override
    public String statusMessage() {
        return Ml.g("actors.buffs.debuffs.blinded.statusmessage");
    }

    @Override
    public String playerMessage() {
        return Ml.g("actors.buffs.debuffs.blinded.playermessage");
    }

    @Override
    public int icon() {
        return BuffIndicator.BLINDED;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.add( CharSprite.State.VERTIGO );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.VERTIGO );
//    }

    @Override
    public String description() {
        return Ml.g("actors.buffs.debuffs.blinded.description");
    }
}
