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
package com.consideredhamster.yetanotherpixeldungeon;

import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Explosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.hazards.FieryRune;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.DwarfMonk;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.GnollBrute;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Golem;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mimic;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Piranha;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Rat;
import com.consideredhamster.yetanotherpixeldungeon.actors.special.Pushing;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Chasm;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.BoulderTrap;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.Trap;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;

public abstract class ResultDescriptions {

    //    public static final String FAIL	= Ml.g("resultdescriptions.FAIL");
    public static final String WIN = Ml.g("resultdescriptions.WIN");

    public static String generateResult(Object killedBy, Element killedWith) {

        return Utils.capitalize(
                killedBy == Dungeon.hero
                        ? Ml.g("resultdescriptions.killedWith_killed") + (Dungeon.hero.heroClass == HeroClass.ACOLYTE ? Ml.g("resultdescriptions.generateResult_1") : Ml.g("resultdescriptions.generateResult_2"))
                        : Ml.g("resultdescriptions.killedWith_" + killedWith(killedBy, killedWith)) + Ml.g("resultdescriptions.generateResult_3") + Ml.g("resultdescriptions.killedBy_" + killedBy(killedBy))
        );
    }

    public static String generateMessage(Object killedBy, Element killedWith) {

        return (
                killedBy == Dungeon.hero
                        ? Ml.g("resultdescriptions.generateMessage_1") + Ml.g("resultdescriptions.killedWith_" + killedWith(killedBy, killedWith)) + Ml.g("resultdescriptions.generateMessage_2")
                        : Ml.g("resultdescriptions.generateMessage_3") + Ml.g("resultdescriptions.killedBy_" + killedBy(killedBy)) + Ml.g("resultdescriptions.killedWith_" + killedWith(killedBy, killedWith))
        ) + Ml.g("resultdescriptions.generateMessage_5");
    }

    private static String killedWith(Object killedBy, Element killedWith) {

        String result = Ml.g("resultdescriptions.killedWith_killed");

        if (killedWith == null) {

            if (killedBy instanceof Mob) {

                Mob mob = (Mob) killedBy;

                if (Bestiary.isBoss(mob) || mob instanceof Rat) {

                    result = Ml.g("resultdescriptions.killedWith_defeated");

                } else if (mob instanceof GnollBrute) {

                    result = Ml.g("resultdescriptions.killedWith_murderized");

                } else if (mob instanceof DwarfMonk) {

                    result = Ml.g("resultdescriptions.killedWith_facefisted");

                } else if (mob instanceof Golem) {

                    result = Ml.g("resultdescriptions.killedWith_squashed_flat");

                } else if (mob instanceof Piranha) {

                    result = Ml.g("resultdescriptions.killedWith_eaten");

                } else if (mob instanceof Mimic) {

                    result = Ml.g("resultdescriptions.killedWith_ambushed");

                }

            } else if (killedBy instanceof Pushing || killedBy instanceof BoulderTrap) {

                result = Ml.g("resultdescriptions.killedWith_crushed");

            }

            //        } else if( killedWith instanceof DamageType.Flame) {
            //            result = Ml.g("resultdescriptions.killedWith_burned_to_crisp");
            //        } else if( killedWith instanceof DamageType.Frost) {
            //            result = Ml.g("resultdescriptions.killedWith_chilled_to_death");
        } else if (killedWith instanceof Element.Shock) {
            result = Ml.g("resultdescriptions.killedWith_electrocuted");
        } else if (killedWith instanceof Element.Acid) {
            result = Ml.g("resultdescriptions.killedWith_dissolved");
        } else if (killedWith instanceof Element.Explosion) {
            result = Ml.g("resultdescriptions.killedWith_blown_up");
            //        } else if( killedWith instanceof DamageType.Mind) {
            //            result = Ml.g("resultdescriptions.killedWith_");
            //        } else if( killedWith instanceof DamageType.Body) {
            //            result = Ml.g("resultdescriptions.killedWith_drained");
            //        } else if( killedWith instanceof DamageType.Unholy) {
            //            result = Ml.g("resultdescriptions.killedWith_withered");
            //        } else if( killedWith instanceof DamageType.Energy) {
            //            result = Ml.g("resultdescriptions.killedWith_disintegrated");
        }

        return result;
    }

    private static String killedBy(Object killedBy) {

        String result = Ml.g("resultdescriptions.killedBy_something");

        if (killedBy instanceof Mob) {
            Mob mob = ((Mob) killedBy);
            result = Bestiary.isBoss(mob) ? mob.name : Ml.g("resultdescriptions.killedBy_" + Utils.indefinite(mob.name));
        } else if (killedBy instanceof Blob) {
            Blob blob = ((Blob) killedBy);
            result = Ml.g("resultdescriptions.killedBy_" + Utils.indefinite(blob.name));
        } else if (killedBy instanceof Weapon.Enchantment) {
            result = Ml.g("resultdescriptions.killedBy_enchanted_weapon");
        } else if (killedBy instanceof Armour.Glyph) {
            result = Ml.g("resultdescriptions.killedBy_enchanted_armor");
        } else if (killedBy instanceof Buff) {
            if (killedBy instanceof Crippled) {
                result = Ml.g("resultdescriptions.killedBy_excessive_bleeding");
            } else if (killedBy instanceof Poisoned) {
                result = Ml.g("resultdescriptions.killedBy_poison");
            } else if (killedBy instanceof Satiety) {
                result = Ml.g("resultdescriptions.killedBy_starvation");
            } else if (killedBy instanceof Burning) {
                result = Ml.g("resultdescriptions.killedBy_being_burned_alive");
            } else if (killedBy instanceof Corrosion) {
                result = Ml.g("resultdescriptions.killedBy_caustic_ooze");
            }
        } else if (killedBy instanceof Trap) {
            result = Ml.g("resultdescriptions.killedBy_a_trap");
        } else if (killedBy instanceof Chasm) {
            result = Ml.g("resultdescriptions.killedBy_gravity");
        } else if (killedBy instanceof Pushing) {
            result = Ml.g("resultdescriptions.killedBy_knockback");
        } else if (killedBy instanceof Explosion) {
            result = Ml.g("resultdescriptions.killedBy_explosion");
        } else if (killedBy instanceof FieryRune) {
            result = Ml.g("resultdescriptions.killedBy_your_own_firebrand_rune");
        }

        return result;
    }

}
