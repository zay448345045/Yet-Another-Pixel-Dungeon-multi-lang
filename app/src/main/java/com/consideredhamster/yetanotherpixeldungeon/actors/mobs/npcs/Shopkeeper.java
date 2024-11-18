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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Journal;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ShopkeeperHumanSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndTradeItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Shopkeeper extends NPC {

    private static final String TXT_GREETINGS = Ml.g("actors.mobs.npcs.shopkeeper.txt_greetings");

    private static String[][] LINES_THREATENED = {

            {
                    Ml.g("items.misc.guard.lines_threatened_1"),
                    Ml.g("items.misc.guard.lines_threatened_2"),
                    Ml.g("items.misc.guard.lines_threatened_3"),
                    Ml.g("items.misc.guard.lines_threatened_4"),
                    Ml.g("items.misc.guard.lines_threatened_5"),
            },
            {
                    Ml.g("items.misc.guard.lines_caught_1"),
                    Ml.g("items.misc.guard.lines_caught_2"),
                    Ml.g("items.misc.guard.lines_caught_3"),
            },
            {
                    Ml.g("items.misc.guard.lines_threatened_6"),
                    Ml.g("items.misc.guard.lines_threatened_7"),
                    Ml.g("items.misc.guard.lines_threatened_8"),
            },
            {
                    Ml.g("items.misc.guard.lines_threatened_9"),
                    Ml.g("items.misc.guard.lines_threatened_10"),
                    Ml.g("items.misc.guard.lines_threatened_11"),
            },
    };

    private static String[][] LINES_CAUGHT = {

            {
                    Ml.g("items.misc.guard.lines_caught_4"),
                    Ml.g("items.misc.guard.lines_caught_5"),
                    Ml.g("items.misc.guard.lines_caught_6"),
                    Ml.g("items.misc.guard.lines_caught_7"),
                    Ml.g("items.misc.guard.lines_caught_8"),
            },
            {
                    Ml.g("items.misc.guard.lines_caught_9"),
                    Ml.g("items.misc.guard.lines_caught_10"),
                    Ml.g("items.misc.guard.lines_caught_11"),
                    Ml.g("items.misc.guard.lines_caught_12"),
            },
            {
                    Ml.g("items.misc.guard.lines_caught_13"),
                    Ml.g("items.misc.guard.lines_caught_14"),
                    Ml.g("items.misc.guard.lines_caught_15"),
                    Ml.g("items.misc.guard.lines_caught_16"),
            },
            {
                    Ml.g("items.misc.guard.lines_caught_17"),
                    Ml.g("items.misc.guard.lines_caught_18"),
                    Ml.g("items.misc.guard.lines_caught_19"),
            },
    };

    private static String[][] LINES_STOLEN = {

            {
                    Ml.g("items.misc.guard.lines_stolen_1"),
                    Ml.g("items.misc.guard.lines_stolen_2"),
                    Ml.g("items.misc.guard.lines_stolen_3"),
            },
            {
                    Ml.g("items.misc.guard.lines_stolen_4"),
                    Ml.g("items.misc.guard.lines_stolen_5"),
                    Ml.g("items.misc.guard.lines_stolen_6"),
            },
            {
                    Ml.g("items.misc.guard.lines_stolen_7"),
                    Ml.g("items.misc.guard.lines_stolen_8"),
                    Ml.g("items.misc.guard.lines_stolen_9"),
            },
            {
                    Ml.g("items.misc.guard.lines_stolen_10"),
                    Ml.g("items.misc.guard.lines_stolen_11"),
                    Ml.g("items.misc.guard.lines_stolen_12"),
            },
    };

    private int threatened = 0;
    private boolean seenBefore = false;

    {
        name = Ml.g("actors.mobs.npcs.shopkeeper.name");
        spriteClass = ShopkeeperHumanSprite.class;
    }


    @Override
    protected boolean act() {

        if (noticed) {
            noticed = false;
        }

        Dungeon.level.updateFieldOfView(this);

        if (!seenBefore && Dungeon.visible[pos] && Level.fieldOfView[Dungeon.hero.pos]) {
            Journal.add(Journal.Feature.SHOP);
            seenBefore = true;
            greetings();
        }

        throwItem();

        sprite.turnTo(pos, Dungeon.hero.pos);
        spend(TICK);
        return true;
    }

    @Override
    public void damage(int dmg, Object src, Element type) {
        onAssault();
    }

    @Override
    public boolean add(Buff buff) {
        if (buff instanceof Debuff) {
            onAssault();
        }

        return false;
    }

    protected void greetings() {
        yell(Utils.format(TXT_GREETINGS));
    }

    protected void onAssault() {

        if (threatened < LINES_THREATENED.length) {
            yell(LINES_THREATENED[threatened][Random.Int(LINES_THREATENED[threatened].length)]);
        }

        if (threatened >= 3) {
            runAway();
        } else if (threatened >= 2) {
            callForHelp();
        }

        threatened++;
    }

    public void onStealing() {

        if (threatened < LINES_STOLEN.length) {
            yell(LINES_STOLEN[threatened][Random.Int(LINES_STOLEN[threatened].length)]);
        }

        if (threatened >= 3) {
            runAway();
//        } else if( threatened >= 2 ) {
//            callForHelp();
        }

        threatened++;
    }

    public void onCaught() {

        if (threatened < LINES_CAUGHT.length) {
            yell(LINES_CAUGHT[threatened][Random.Int(LINES_CAUGHT[threatened].length)]);
        }

        if (threatened >= 3) {
            runAway();
        } else if (threatened >= 2) {
            callForHelp();
        }

        threatened++;
    }

    protected void callForHelp() {

        for (Mob mob : Dungeon.level.mobs) {
            if (mob.pos != pos) {
                mob.beckon(pos);
            }
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.center(pos).start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        }

        Sample.INSTANCE.play(Assets.SND_CHALLENGE);
    }

    protected void runAway() {

        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.FOR_SALE) {

                CellEmitter.get(heap.pos).burst(Speck.factory(Speck.WOOL), 5);
                heap.destroy();

            }
        }

        destroy();
        sprite.killAndErase();

        Journal.remove(Journal.Feature.SHOP);
        CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 10);
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public String description() {
        return Ml.g("actors.mobs.npcs.shopkeeper.description");
    }

    public float stealingChance(Item item) {

        float baseChance = item.stealingDifficulty() * (threatened + 1);

        return Math.max(0.0f, 1.0f - baseChance / Dungeon.hero.stealth());

    }

    public static WndBag sell() {
        return GameScene.selectItem(itemSelector, WndBag.Mode.FOR_SALE, "Select an item to sell");
    }

    private static WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null) {
                WndBag parentWnd = sell();
                GameScene.show(new WndTradeItem(item, parentWnd));
            }
        }
    };

    @Override
    public void interact() {
        sell();
    }

    private static final String SEENBEFORE = "seenbefore";
    private static final String THREATENED = "threatened";

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SEENBEFORE, seenBefore);
        bundle.put(THREATENED, threatened);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        seenBefore = bundle.getBoolean(SEENBEFORE);
        threatened = bundle.getInt(THREATENED);
    }
}
